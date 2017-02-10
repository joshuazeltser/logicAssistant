package model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 08/02/2017.
 */
public class DebugTests {

    @Test
    public void bracketsTest1() {
        Proof proof = new Proof();

        String str = "(P ^ Q) | (!P ^ R)";
        String str1 = "P ^ Q";
        String str2 = "Q";
        String str3 = "Q | R";
        String str4 = "!P ^ R";
        String str5 = "R";
        String str6 = "Q | R";
        String str7 = "Q | R";
        String str8 = "(P ^ Q) | (!P ^ R) -> (Q | R)";



        Expression expr = new Expression(RuleType.ASSUMPTION);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.ASSUMPTION);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.AND_ELIM);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.OR_INTRO);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.ASSUMPTION);
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.AND_ELIM);
        expr5.addToExpression(str5);

        Expression expr6 = new Expression(RuleType.OR_INTRO);
        expr6.addToExpression(str6);

        Expression expr7 = new Expression(RuleType.OR_ELIM);
        expr7.addToExpression(str7);

        Expression expr8 = new Expression(RuleType.IMPLIES_INTRO);
        expr8.addToExpression(str8);



//        System.out.println(expr.toString());
//        System.out.println(expr1.toString());
//        System.out.println(expr2.toString());
//        System.out.println(expr3.toString());
//        System.out.println(expr4.toString());
//        System.out.println(expr5.toString());
//        System.out.println(expr6.toString());
//        System.out.println(expr7.toString());
//        System.out.println(expr8.toString());


        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);
        proof.addExpression(expr5);
        proof.addExpression(expr6);
//        proof.addExpression(expr7);
        proof.addExpression(expr8);


        assertTrue(proof.isProofValid());

    }
}
