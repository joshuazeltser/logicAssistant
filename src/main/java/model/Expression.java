package model;

import javassist.compiler.ast.Expr;

import javax.validation.constraints.Null;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;


/**
 * Created by joshuazeltser on 03/01/2017.
 */
public class Expression {

    private List<Component> expression;



    private RuleType ruleType;


    public Expression(RuleType ruleType) {
        expression = new LinkedList<>();

        this.ruleType = ruleType;
    }

    public Expression() {
        expression = new LinkedList<>();
    }



    public boolean addToExpression(String input) throws SyntaxException {

        if (!checkBrackets(input)) {
            throw new SyntaxException("Syntax Error: Mismatched brackets");
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
                    case "|": throw new SyntaxException("Syntax Error: You cannot use " + tokens[i] +" operator " +
                            "at this part of an expression");
                }
            }
            if (i==0 && tokens[i].charAt(0) == ')') {
                throw new SyntaxException("Syntax Error: You cannot use " + tokens[i] +" operator " +
                        "at this part of an expression");
            }

            if ((i == (tokens.length - 1)) && (tokens[i].contains("(") || tokens[i].equals("!"))) {
                throw new SyntaxException("Syntax Error: You cannot use " + tokens[i] +" operator " +
                        "at this part of an expression");
            }
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

    private String removeFirstChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(1, s.length());
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
//        System.out.println(expression.get(ops[0]+2).toString().equals("CLOSE"));
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




        System.out.println("Syntax error");
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

        if (num > 1) {
//            System.out.println(num);
            int index = surroundedByBrackets(type);
            System.out.println(index);
//            System.out.println(thisExpression);
            lhsExpr = new Expression(ruleType);
            lhsExpr.expression = thisExpression.subList(0, index);


//            System.out.println(lhsExpr.expression);
            rhsExpr = new Expression(ruleType);
            rhsExpr.expression = thisExpression.subList(index+1 , thisExpression.size());


//            System.out.println(rhsExpr.expression);
        } else {
            for (int i = 0; i < thisExpression.size(); i++) {

             if (thisExpression.get(i) instanceof Operator) {
                  if (((Operator) thisExpression.get(i)).getType() == type) {
                      lhsExpr = new Expression(ruleType);
                      lhsExpr.expression = thisExpression.subList(0, i);
                      rhsExpr = new Expression(ruleType);
                      rhsExpr.expression = thisExpression.subList(i+1 , thisExpression.size());
                  }
             }

            }
        }
//        System.out.println(lhsExpr.expression.get(0));


        result.add(lhsExpr);
        result.add(rhsExpr);

        return result;


    }

    public String convertListToString(List<Component> list) {
        String result = "";
        for (Component c : list) {
            result += c.toString();
        }
        return result;
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

    public boolean doubleNot() {

        int count = 0;
        for (Component c : expression) {
            if (c instanceof Operator) {
                if (((Operator) c).getType() == OperatorType.NOT) {
                    count++;
                }
                if (count == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeNcomponents(int n) {
        for (int i = 0; i < n; i++) {
            expression.remove(0);
        }

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
        int waiting = 0;

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

        System.out.println(result);


        return checkBrackets(result);


    }


}
