package model;


import java.util.LinkedList;
import java.util.List;


/**
 * Created by joshuazeltser on 03/01/2017.
 */
public class Expression {

    private List<Component> expression;

    private String expressionString;


    public Expression() {
        expression = new LinkedList<>();
        expressionString = "";
    }

    public void addToExpression(String input) {

        String[] tokens = input.split(" ");

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
                    while (token.contains("(")) {
                        expression.add(new Operator("OPEN", OperatorType.OPEN_BRACKET));
                        token = token.substring(1);
                    }

                    int count = 0;
                    while (token.contains(")")) {
                        token = removeLastChar(token);
                        count++;
                    }

                    expression.add(new Proposition(token));

                    while (count > 0) {
                        expression.add(new Operator("CLOSE", OperatorType.CLOSE_BRACKET));
                        count--;
                    }
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

    public String getExpressionString() {
        return expressionString;
    }

    public void setExpressionString(String expressionString) {
        this.expressionString = expressionString;
    }

    private String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }

    public List<Expression> splitExpressionBy(OperatorType type) {

        List<Expression> result = new LinkedList<>();
        int count = 0;
        for (Component c : expression) {
            count++;
            if (c instanceof Operator) {
                if (((Operator) c).getType() == type) {

                    Expression lhsExpr = new Expression();
                    lhsExpr.expression = expression.subList(0, count-1);
                    result.add(lhsExpr);

                    Expression rhsExpr = new Expression();
                    rhsExpr.expression = expression.subList(count , expression.size());
                    result.add(rhsExpr);

                    return result;
                }
            }
        }
        //exception handling
        return null;
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
}
