package model;

import javassist.compiler.ast.Expr;
import javassist.compiler.ast.IntConst;

import javax.validation.constraints.Null;
import java.util.*;

import static model.OperatorType.CLOSE_BRACKET;
import static model.OperatorType.OPEN_BRACKET;


/**
 * Created by joshuazeltser on 03/01/2017.
 */
public class Expression {

    private List<Component> expression;

    private List<Integer> lines;

    private RuleType ruleType;

    private boolean marked;

    private boolean introMarked;



    public Expression(RuleType ruleType) {
        expression = new LinkedList<>();
        lines = new LinkedList<>();
        this.ruleType = ruleType;
        introMarked = false;
        marked = false;
    }

    public Expression() {
        expression = new LinkedList<>();
        lines = new LinkedList<>();
        marked = false;
    }

    public void addReferenceLine(String a) throws SyntaxException {

        try {
            int num = Integer.parseInt(a);
            lines.add(num);
        } catch (NumberFormatException e) {
            throw new SyntaxException("Syntax Error: " + a + " is an invalid line number");
        }

    }

    public List<Integer> getReferenceLine() {
        return lines;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public boolean addToExpression(String input) throws SyntaxException {

        if (!checkBrackets(input)) {
            throw new SyntaxException("Syntax Error: Mismatched brackets");
        }

        for (int i = 0; i < input.length()-1; i++) {
            if (input.charAt(i) == ' ' && input.charAt(i+1) == ' ') {
                throw new SyntaxException("Syntax Error: You cannot use a space twice in a row in an expression ");
            }
        }

        String[] tokens = input.split(" ");

        syntaxCheck(tokens);
        for (int i = 0; i < tokens.length; i++) {

            String token = tokens[i];
            switch (token) {
                case "|":
                    expression.add(new Operator("OR", OperatorType.OR));
                    break;
                case "^":
                    expression.add(new Operator("AND", OperatorType.AND));
                    break;
                case "->":
                    expression.add(new Operator("IMPLIES", OperatorType.IMPLIES));
                    break;
                case "<->":
                    expression.add(new Operator("ONLY", OperatorType.ONLY));
                    break;
                case "(":
                    expression.add(new Operator("OPEN", OperatorType.OPEN_BRACKET));
                    break;
                case ")":
                    expression.add(new Operator("CLOSE", OperatorType.CLOSE_BRACKET));
                    break;

                default:
                    while (token.charAt(0) == '!') {
                         expression.add(new Operator("NOT", OperatorType.NOT));

                        token = token.substring(1);
                    }
                    while (token.contains("(")) {

                        expression.add(new Operator("OPEN", OperatorType.OPEN_BRACKET));

                        token = token.substring(1);
                    }

                    int count = 0;
                    while (token.contains(")")) {
                        token = removeLastChar(token);
                        count++;
                    }

                    while (token.charAt(0) == '!') {
                        expression.add(new Operator("NOT", OperatorType.NOT));
                        token = token.substring(1);
                    }

                    expression.add(new Proposition(token));

                    while (count > 0) {
                        expression.add(new Operator("CLOSE", OperatorType.CLOSE_BRACKET));
                        count--;
                    }

            }
        }

        return true;

    }

    public void syntaxCheck(String[] tokens) throws SyntaxException {

        for (int i = 0; i < tokens.length; i++) {
            if (i == 0 || i == tokens.length - 1) {
                switch (tokens[i]) {
                    case "^":
                    case "->":
                    case "<->":
                    case "|": throw new SyntaxException("Syntax Error: You cannot use "
                            + tokens[i] +" operator at this part of an expression");
                }
            }

            if (i==0 && tokens[i].charAt(0) == ')') {
                throw new SyntaxException("Syntax Error: You cannot use " + tokens[i] +"" +
                        " operator at this part of an expression");
            }

            if (tokens[i].charAt(tokens[i].length()-1) == '!' || tokens[i].charAt(tokens[i].length()-1) == '(') {
                throw new SyntaxException("Syntax Error: You cannot use " + tokens[i] +" " +
                        "operator at the end of an expression");
            }

            if ((i == (tokens.length - 1)) && (tokens[i].equals("(") || tokens[i].equals("!"))) {
                throw new SyntaxException("Syntax Error: You cannot use " + tokens[i] +" " +
                        "operator at this part of an expression");
            }



            if (!tokens[i].equals("(") && !tokens[i].equals(")") && !tokens[i].equals("!") &&
                    !tokens[i].equals("NOT") && i < tokens.length-1) {

                if (tokens[i].equals(tokens[i+1])) {
                    throw new SyntaxException("Syntax Error: You cannot use " + tokens[i] +"" +
                            " twice in a row as part of an expression");
                }
            }

            if (isOperator(tokens[i]) && isOperator(tokens[i+1]) && i < tokens.length-1) {
                throw new SyntaxException("Syntax Error: You cannot use " + tokens[i] +" " +
                        "twice in a row as part of an expression");
            }



            if (!inWhiteList(tokens[i])) {
                throw new SyntaxException("Syntax Error: " + tokens[i] + " contains invalid" +
                        " Components for use in an Expression");
            }

        }
    }

    private boolean inWhiteList(String str) {

        char[] chars = str.toCharArray();

        if (isOperator(str)) {
            return true;
        }

        for (int i = 0; i < chars.length; i++) {

            if (!Character.isLetter(chars[i]) && chars[i] != '(' && chars[i] != ')' && chars[i] != '!') {
                return false;
            }
        }

        return true;
    }


    public List<String> replacePropositions(List<LinkedHashMap<Proposition, Integer>> permMapList)
            throws SyntaxException {

        List<String> results = new LinkedList<>();

        boolean not = false;

        for (Map permMap : permMapList) {
            String str = "(";

            for (Component c : expression) {
                if (permMap.containsKey(c)) {
                    str += permMap.get(c) + " ";
                    if (!not) {
                        str += " ";
                    } else {
                        str += ")";
                        not = false;
                    }
                } else if (c.equals(new Operator("AND", OperatorType.AND))) {
                    str += "&";
                } else if (c.equals(new Operator("OR", OperatorType.OR))) {
                    str += "|";
                } else if (c.equals(new Operator("OPEN", OperatorType.OPEN_BRACKET))) {
                    str += "(";
                } else if (c.equals(new Operator("CLOSE", OperatorType.CLOSE_BRACKET))) {
                    str += ")";
                } else if (c.equals(new Operator("NOT", OperatorType.NOT))) {
                    str += "!(";
                    not = true;
                } else if (c.equals(new Operator("IMPLIES", OperatorType.IMPLIES))) {
                    str += ">";
                } else if (c.equals(new Operator("ONLY", OperatorType.ONLY))) {
                    str += "~";
                } else {
                    str += c;
                    if (!not) {
                        str += " ";
                    } else {
                        str += ")";
                        not = false;
                    }
                }

            }

            str += ")";
            results.add(str);
        }
        return results;

    }

    private boolean isOperator(String str) {
        switch (str) {
            case "^":
            case "->":
            case "<->":
            case "|": return true;
            default: return false;
        }
    }

    public List<Proposition> listPropositions() {
        List<Proposition> props = new LinkedList<>();

        for (Component expr : expression) {
            if (expr instanceof Proposition) {
                props.add((Proposition) expr);

            }
        }
        return props;
    }

    @Override
    public String toString() {
        String result = "";
        int count = 0;

        for (Component c : expression) {

            result += c.toString();
            if (count < expression.size() - 1) {
                result += " ";
            }
            count++;
        }
        return result;
    }


    private String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }

    public int surroundedByBrackets (OperatorType type) {

        int num = countOperator(type);
        int[] ops = new int[num];
        int counter = 0;

        for (int i = 0; i < expression.size(); i++) {
            if (expression.get(i) instanceof Operator) {
                if (((Operator) expression.get(i)).getType() == type) {
                    ops[counter] = i;
                    counter++;
                }
            }
        }
        for (int j = 0; j < ops.length; j++) {
            if (ops[j] < 2) {
                return ops[j];
            }

            if (ops[j] > expression.size()-2) {
                return ops[j];
            }
            if (!expression.get(ops[j]-2).toString().equals("OPEN")) {

                return ops[j];
            }
        }
        return 0;
    }


    public int countOperator(OperatorType type) {
        int count = 0;
        for (Component c : expression) {
            if (c instanceof Operator) {
                if (((Operator) c).getType() == type) {
                    count++;
                }
            }
        }
        return count;
    }


    private List<String> operatorNames = Arrays.asList("AND", "OR", "IMPLIES", "ONLY");
    public List<Expression> splitExpressionBy(OperatorType type) {

        List<Component> thisExpression = expression;

        if (thisExpression.get(0).toString().equals("OPEN")
                    && thisExpression.get(thisExpression.size() - 1).toString().equals("CLOSE")) {

                thisExpression.remove(0);
                thisExpression.remove(thisExpression.size() - 1);

        }

        List<Expression> result = new LinkedList<>();

        int num = countOperator(type);

        Expression lhsExpr = new Expression();
        Expression rhsExpr = new Expression();

//        if (num > 1) {
//            int index = surroundedByBrackets(type);
//
//            lhsExpr = new Expression(ruleType);
//            lhsExpr.expression = thisExpression.subList(0, index);
//
//            rhsExpr = new Expression(ruleType);
//            rhsExpr.expression = thisExpression.subList(index+1 , thisExpression.size());
//
//
//        } else {
            for (int i = 0; i < thisExpression.size(); i++) {


             if (thisExpression.get(i) instanceof Operator || operatorNames.contains(thisExpression.get(i).toString()) ) {

                 OperatorType t;
                 switch (thisExpression.get(i).toString()) {
                     case "AND" : t = OperatorType.AND; break;
                     case "OR" : t = OperatorType.OR; break;
                     case "IMPLIES" : t = OperatorType.IMPLIES; break;
                     case "ONLY" : t = OperatorType.ONLY; break;
                     default: t =((Operator) thisExpression.get(i)).getType();
                 }


                 if ( t == type) {
                      lhsExpr = new Expression(ruleType);
                      lhsExpr.expression = thisExpression.subList(0, i);
                      rhsExpr = new Expression(ruleType);
                      rhsExpr.expression = thisExpression.subList(i+1 , thisExpression.size());

                      if (checkBracketValidity(lhsExpr) && checkBracketValidity(rhsExpr)) {
                          break;
                      } else {
                          continue;
                      }
                  }
             }

            }

//        }
//        System.out.println(lhsExpr);

        result.add(lhsExpr);
        result.add(rhsExpr);

        return result;


    }
    private boolean checkBracketValidity(Expression expr) {
        return ((expr.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                expr.contains(new Operator("CLOSE", CLOSE_BRACKET))) ||
                (!expr.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                        !expr.contains(new Operator("CLOSE", CLOSE_BRACKET))));
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        Expression expr2 = (Expression) o;

        return toString().equals(expr2.toString()) ||
                ("OPEN " + toString() + " CLOSE").equals(expr2.toString()) ||
                toString().equals("OPEN " + expr2.toString() + " CLOSE") ||
                ("OPEN " + toString() + " CLOSE").equals("OPEN " + expr2.toString() + " CLOSE");
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public boolean contains(Component c) {
        for (Component component : expression) {
            if(c.toString().equals(component.toString())) {
                return true;
            }
        }

        return false;
    }

    public void removeNcomponents(int n) {
        for (int i = 0; i < n; i++) {
            expression.remove(0);
        }

    }

    public void removeLast() {
        expression.remove(expression.size()-1);
    }

    public static boolean checkBrackets(String str)
    {
        if (str.isEmpty())
            return true;

        Stack<Character> stack = new Stack<Character>();
        for (int i = 0; i < str.length(); i++)
        {
            char current = str.charAt(i);
            if (current == '{' || current == '(' || current == '[')
            {
                stack.push(current);
            }


            if (current == '}' || current == ')' || current == ']')
            {
                if (stack.isEmpty())
                    return false;

                char last = stack.peek();
                if (current == '}' && last == '{' || current == ')' && last == '(' || current == ']' && last == '[')
                    stack.pop();
                else
                    return false;
            }

        }

        return stack.isEmpty();
    }

    public static boolean externalBrackets(String str) {

        if (str.charAt(0) != '(' || str.charAt(str.length() - 1) != ')') {
            return false;
        }

        char[] array = str.toCharArray();

        String result = "";
        for (int i = 0; i < str.length(); i++) {
            if (i != 0 && i != str.length()-1) {
                result += array[i];
            }
        }

        return checkBrackets(result);


    }

    public boolean equalExceptFirst(Expression e1) {


        if (expression.get(0).toString().equals("NOT")) {
            expression.remove(0);
            if (equals(e1)) {
                expression.add(0, new Operator("NOT", OperatorType.NOT));
                return true;
            }
            expression.add(0, new Operator("NOT", OperatorType.NOT));
        }

        if (e1.expression.get(0).toString().equals("NOT")) {
            e1.expression.remove(0);
            if (equals(e1)) {
                e1.expression.add(0, new Operator("NOT", OperatorType.NOT));
                return true;
            }
        }
        return false;
    }

    public Component getFirstComp() {
        if (expression.get(0) == null) {
            return null;
        }
        return expression.get(0);
    }

    public Component getLastComp() {
        return expression.get(expression.size()-1);
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

}
