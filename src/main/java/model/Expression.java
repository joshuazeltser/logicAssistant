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



    public boolean addToExpression(String input) {

        if (!checkBrackets(input)) {
            System.out.println("MISMATCHED BRACKETS");
            return false;
        }

        String[] tokens = input.split(" ");


        boolean added = false;
        if (!externalBrackets(input) && tokens.length > 1 && input.charAt(0) != '(' && !tokens[0].equals("->")) {
            expression.add(new Operator("OPEN", OperatorType.OPEN_BRACKET));
            added = true;
        }

        for (String token : tokens) {

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

        if (added) {
            expression.add(new Operator("CLOSE", OperatorType.CLOSE_BRACKET));

//            System.out.println("Token " + expression);
        }
        return true;

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

    public void addExpressionExternalBrackets() {



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



    public List<Expression> splitExpressionBy(OperatorType type, int num) {

        List<Component> thisExpression = expression;


//        if (type != OperatorType.IMPLIES && type != OperatorType.ONLY) {
            if (thisExpression.get(0).toString().equals("OPEN")
                    && thisExpression.get(thisExpression.size() - 1).toString().equals("CLOSE")) {

                thisExpression.remove(0);
                thisExpression.remove(thisExpression.size() - 1);

            }
//        }


        List<Expression> result = new LinkedList<>();

        int[] opIndex = new int[countOperator(type)];

        int counter = 0;
        for (int i = 0; i < thisExpression.size(); i++) {

            if (thisExpression.get(i) instanceof Operator) {
                if (((Operator) thisExpression.get(i)).getType() == type) {

                    opIndex[counter] = i;
                    counter++;
                }
            }

        }


        if (opIndex.length == 0) {
            return null;
        }

        Expression lhsExpr = new Expression(ruleType);
        lhsExpr.expression = thisExpression.subList(0, opIndex[num]);

        result.add(lhsExpr);



        Expression rhsExpr = new Expression(ruleType);
        rhsExpr.expression = thisExpression.subList(opIndex[num]+1 , thisExpression.size());
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

        return toString().equals(expr2.toString());
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
