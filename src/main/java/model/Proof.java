package model;


import javassist.compiler.ast.Expr;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joshuazeltser on 18/01/2017.
 */
public class Proof {

    private List<Expression> expressions;

    private String proofString;

    public Proof() {
        expressions = new LinkedList<>();
        proofString = "";
    }

    public String separateByNewLine(String str) {
        String result = "";

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\n') {
                Expression newExpr = new Expression();
                newExpr.addToExpression(result);
                addExpression(newExpr);
                result = "";
            } else {
                result += str.charAt(i);
                if (i == str.length() - 1) {
                    Expression newExpr = new Expression();
                    newExpr.addToExpression(result);
                    addExpression(newExpr);
                }

            }

        }
        return toString();
    }

    public boolean isProofValid() {

        for (int i = expressions.size()-1; i >= 0; i--) {
            switch (expressions.get(i).getRuleType()) {
                case AND_ELIM: if (!isAndElimValid(expressions.get(i))) return false; break;
                case AND_INTRO: if (!isAndIntroValid(expressions.get(i))) return false; break;
                case OR_ELIM: if (!isOrEliminationValid(expressions.get(i))) return false; break;
                case OR_INTRO: if (!isOrIntroValid(expressions.get(i))) return false; break;
                case IMPLIES_ELIM: if (!isImpliesElimValid(expressions.get(i))) return false; break;
                case IMPLIES_INTRO: if (!isImpliesIntroValid(expressions.get(i))) return false; break;
                case NOT_ELIM: if (!isNotElimValid(expressions.get(i))) return false; break;
                case NOT_INTRO: if (!isNotIntroductionValid(expressions.get(i))) return false; break;
                case ONLY_ELIM: if (!isOnlyEliminationValid(expressions.get(i))) return false; break;
                case ONLY_INTRO: if (!isOnlyIntroValid(expressions.get(i))) return false; break;
                default: break;
            }
            expressions.remove(i);
        }
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

    public boolean isNotIntroductionValid(Expression e1) {

        e1.removeNcomponents(1);

        for (Expression expr : expressions) {
            if (expr.equals(e1)) {
                Expression saved = e1;
                for (Expression expr1 : expressions) {
                    Expression e = new Expression();
                    String temp = "!" + saved.toString();

                    if (saved.toString() != "" ) {
                        e.addToExpression(temp);
                    }


                    for (Expression expr2 : expressions) {
                        if ((expr2.toString().equals(e.toString()))) {
                            return true;
                        }
                    }
                    saved = expr1;
                }
            }
        }
        return false;
    }

    public boolean isOrEliminationValid(Expression e1) {
        List<Expression> assumptions = new LinkedList<>();

        for (Expression expr : expressions) {
            if (expr.getRuleType() == RuleType.ASSUMPTION) {
                assumptions.add(expr);
            }
        }
        for (int i = 0; i < assumptions.size(); i++) {
            for (int j = 0; i != j && j < assumptions.size(); j++) {
                Expression orExpr = new Expression();
                Expression orOppositeExpr = new Expression();
                orExpr.addToExpression(assumptions.get(j).toString() + " | " + assumptions.get(i).toString());
                orOppositeExpr.addToExpression(assumptions.get(i).toString() + " | " + assumptions.get(j).toString());

                if (expressions.contains(orExpr) || expressions.contains(orOppositeExpr)) {
                    Expression a = assumptions.get(j);
                    Expression b = assumptions.get(i);

                    int lhsIndex = expressions.indexOf(a);
                    int rhsIndex = expressions.indexOf(b);

                    for (int k = lhsIndex + 1; k < expressions.size(); k++) {
                        if (expressions.get(k).equals(e1)) {
                            for (int l = rhsIndex + 1; l < expressions.size(); l++) {
                                if (expressions.get(l).equals(e1)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }


    public String getProofString() {
        return proofString;
    }

    public void setProofString(String proofString) {
        this.proofString = proofString;
    }
}
