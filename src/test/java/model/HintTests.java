package model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 14/04/2017.
 */
public class HintTests {

    Proof proof = new Proof();

    @Test
    public void hintTest1() throws SyntaxException {
        String str = "A ^ B";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        proof.addExpression(expr);

        assertTrue(proof.generateHint().contains("AND_ELIM"));
    }

    @Test
    public void hintTest2() throws SyntaxException {
        String str = "C -> (A ^ B)";
        String str1 = "C";
        String str2 = "A ^ B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.IMPLIES_ELIM);
        expr2.addToExpression(str2);
        expr2.addReferenceLine("1");
        expr2.addReferenceLine("2");

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        assertTrue(proof.generateHint().contains("AND_ELIM"));
    }
}
