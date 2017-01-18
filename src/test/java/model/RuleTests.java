package model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 18/01/2017.
 */
public class RuleTests {

    @Test
    public void splitTest() {
        String simple = "(A | B) ^ C";

        Expression expression = new Expression();

        expression.addToExpression(simple);

        List<Expression> sides = expression.splitExpressionBy(OperatorType.AND);

        assertTrue(sides.get(0).toString().equals("OPEN A OR B CLOSE"));

        assertTrue(sides.get(1).toString().equals("C"));
    }

    @Test
    public void andIntroductionValidity() {
        Proof proof = new Proof();

        String str1 = "A";

        String str2 = "B";

        String str3 = "C";

        String str4 = "D";

        String str5 = "A ^ B";

        Expression expr1 = new Expression();
        expr1.addToExpression(str1);

        Expression expr2 = new Expression();
        expr2.addToExpression(str2);

        Expression expr3 = new Expression();
        expr3.addToExpression(str3);

        Expression expr4 = new Expression();
        expr4.addToExpression(str4);

        Expression expr5 = new Expression();
        expr5.addToExpression(str5);

        proof.addExpression(expr1);

        proof.addExpression(expr2);

        proof.addExpression(expr3);

        proof.addExpression(expr4);

        assertTrue(Rules.isAndIntroValid(expr5, proof));



    }
}
