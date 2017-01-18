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
                System.out.println(expr.toString());
                for (Expression expr1 : proof.getExpressions()) {
                    if (expr1.equals(rhs)) {
                        return true;
                    }
                }
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
