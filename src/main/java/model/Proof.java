package model;


import javassist.compiler.ast.Expr;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joshuazeltser on 18/01/2017.
 */
public class Proof {

    private List<Expression> expressions;

    public Proof() {
        expressions = new LinkedList<>();
    }

    public boolean isProofValid() {
        return true;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void addExpression(Expression expr) {
        expressions.add(expr);
    }

    @Override
    public String toString() {

        String result = "";

        int count = 1;

        for (Expression expr : expressions) {
            result += count + " " + expr.toString() + "\n";
            count++;
        }
        return result;
    }

    public boolean isAndIntroValid(Expression e1) {

        int numAnd = e1.countOperator(OperatorType.AND);
        int count = 0;
        while (count != numAnd) {

            List<Expression> sides = e1.splitExpressionBy(OperatorType.AND, count);


            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            for (Expression expr : expressions) {

                if (expr.equals(lhs)) {
                    for (Expression expr1 : expressions) {
                        if (expr1.equals(rhs)) {
                            return true;
                        }
                    }
                }
            }
            count++;
        }
        return false;
    }

    public boolean isAndElimValid(Expression e1) {

        for (Expression expr : expressions) {

            if (expr.contains(new Operator("AND", OperatorType.AND))) {
                int numAnd = expr.countOperator(OperatorType.AND);
                int count = 0;
                while (count != numAnd) {

                    List<Expression> sides = expr.splitExpressionBy(OperatorType.AND, count);

                    Expression lhs = sides.get(0);
                    Expression rhs = sides.get(1);

                    if (lhs.equals(e1) || rhs.equals(e1)) {
                        return true;
                    }
                    count++;
                }

            }
        }
        return false;
    }

    public boolean isOrIntroValid(Expression e1) {
        int numAnd = e1.countOperator(OperatorType.OR);
        int count = 0;
        while (count != numAnd) {
            List<Expression> sides = e1.splitExpressionBy(OperatorType.OR, count);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            for (Expression expr : expressions) {
                if (expr.equals(lhs) || expr.equals(rhs)) {
                    return true;
                }
            }
            count++;
        }
        return false;
    }




    public boolean isImpliesIntroValid(Expression e1) {
        int numAnd = e1.countOperator(OperatorType.IMPLIES);
        int count = 0;
        while (count != numAnd) {
            List<Expression> sides = e1.splitExpressionBy(OperatorType.IMPLIES, count);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            boolean left = true;
            for (Expression expr : expressions) {
                if (expr.equals(lhs) && expr.getRuleType() == RuleType.ASSUMPTION && left) {
                    left = false;
                    continue;
                }

                if (expr.equals(rhs) && expr.getRuleType() != RuleType.ASSUMPTION && !left) {
                    return true;
                }
            }
            count++;
        }
        return false;
    }

    public boolean isImpliesElimValid(Expression e1) {

        for (Expression expr : expressions) {
            if (expr.contains(new Operator("IMPLIES", OperatorType.IMPLIES))) {
                int numAnd = expr.countOperator(OperatorType.IMPLIES);
                int count = 0;
                while (count != numAnd) {
                    List<Expression> sides = expr.splitExpressionBy(OperatorType.IMPLIES, count);

                    Expression lhs = sides.get(0);
                    Expression rhs = sides.get(1);

                    if (rhs.equals(e1)) {
                        for (Expression expr2 : expressions) {
                            if (expr2.equals(lhs)) {
                                return true;
                            }
                        }
                    }
                count++;
                }
            }
        }
        return false;
    }


    public boolean isNotElimValid(Expression e1) {

        for (Expression expr : expressions) {
            if (expr.doubleNot()) {
                expr.removeNcomponents(2);
                if (expr.equals(e1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnlyEliminationValid(Expression e1) {
        for (Expression expr : expressions) {

            if (expr.contains(new Operator("ONLY", OperatorType.ONLY))) {
                int numAnd = expr.countOperator(OperatorType.ONLY);
                int count = 0;
                while (count != numAnd) {

                    List<Expression> sides = expr.splitExpressionBy(OperatorType.ONLY, count);

                    Expression lhs = sides.get(0);
                    Expression rhs = sides.get(1);


                    lhs.addToExpression("-> " + rhs);

                    if (lhs.equals(e1)) {
                        return true;
                    }

                    List<Expression> sides2 = lhs.splitExpressionBy(OperatorType.IMPLIES, count);

                    Expression lhs2 = sides2.get(0);
                    Expression rhs2 = sides2.get(1);

                    rhs2.addToExpression("-> " + lhs2);

                    if (rhs2.equals(e1)) {
                        return true;
                    }
                    count++;
                }

            }
        }
        return false;
    }

    public boolean isOnlyIntroValid(Expression e1) {

        int numAnd = e1.countOperator(OperatorType.ONLY);
        int count = 0;
        while (count != numAnd) {
            List<Expression> sides = e1.splitExpressionBy(OperatorType.ONLY, count);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            lhs.addToExpression("-> " + rhs);

            List<Expression> sides2 = lhs.splitExpressionBy(OperatorType.IMPLIES, count);

            Expression lhs2 = sides2.get(0);
            Expression rhs2 = sides2.get(1);

            rhs2.addToExpression("-> " + lhs2);

            for (Expression expr : expressions) {
                if (expr.equals(lhs) || expr.equals(rhs2)) {
                    if (expr.equals(lhs) || expr.equals(rhs2)) {
                        return true;
                    }
                }
            }

            count++;
        }
        return false;
    }


}
