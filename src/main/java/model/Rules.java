package model;

import java.util.List;

/**
 * Created by joshuazeltser on 18/01/2017.
 */
public class Rules {

    public static boolean isAndIntroValid(Expression e1, Proof proof) {

        List<Expression> sides = e1.splitExpressionBy(OperatorType.AND);


        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        for (Expression expr : proof.getExpressions()) {

            if (expr.equals(lhs)) {
                for (Expression expr1 : proof.getExpressions()) {
                    if (expr1.equals(rhs)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isAndElimValid(Expression e1, Proof proof) {
        for (Expression expr : proof.getExpressions()) {

            if (expr.contains(new Operator("AND", OperatorType.AND))) {
                List<Expression> sides = expr.splitExpressionBy(OperatorType.AND);


                Expression lhs = sides.get(0);
                Expression rhs = sides.get(1);

                if (lhs.equals(e1) || rhs.equals(e1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOrIntroValid(Expression e1, Proof proof) {
        List<Expression> sides = e1.splitExpressionBy(OperatorType.OR);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        for (Expression expr : proof.getExpressions()) {
            if (expr.equals(lhs) || expr.equals(rhs)) {
                return true;
            }
        }
        return false;
    }




    public static boolean isImpliesIntroValid(Expression e1, Proof proof) {
        List<Expression> sides = e1.splitExpressionBy(OperatorType.IMPLIES);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        boolean left = true;
        for (Expression expr : proof.getExpressions()) {
            if (expr.equals(lhs) && expr.getRuleType() == RuleType.ASSUMPTION && left) {
                left = false;
                continue;
            }

            if (expr.equals(rhs) && expr.getRuleType() != RuleType.ASSUMPTION && !left) {
                return true;
            }
        }
        return false;

    }


}
