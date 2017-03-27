package model;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.validation.constraints.AssertTrue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 16/02/2017.
 */
public class ErrorTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    Proof proof = new Proof();

    @Test
    public void syntaxErrorTest1() {
        String str = "^ B";

        Expression expr = new Expression();
        try {
            expr.addToExpression(str);
            thrown.expect(SyntaxException.class);
        } catch (SyntaxException e) {

        }
    }

    @Test
    public void syntaxErrorTest2() {
        String str = "| B ^ A";

        Expression expr = new Expression();
        try {
            expr.addToExpression(str);
            thrown.expect(SyntaxException.class);
        } catch (SyntaxException e) {

        }
    }

    @Test
    public void syntaxErrorTest3() {
        String str = ") B";

        Expression expr = new Expression();
        try {
            expr.addToExpression(str);
            thrown.expect(SyntaxException.class);
        } catch (SyntaxException e) {

        }
    }

    @Test
    public void syntaxErrorTest4() {
        String str = "(A ^ B) !";

        Expression expr = new Expression();
        try {
            expr.addToExpression(str);
            thrown.expect(SyntaxException.class);
        } catch (SyntaxException e) {

        }
    }

    @Test
    public void syntaxErrorTest5() {
        String str = "(A | B) ^ A A";

        Expression expr = new Expression();
        try {
            expr.addToExpression(str);
            thrown.expect(SyntaxException.class);
        } catch (SyntaxException e) {

        }
    }

    @Test
    public void syntaxErrorTest6() {
        String str = "(A | B) ^ ^ (A | A)";

        Expression expr = new Expression();
        try {
            expr.addToExpression(str);
            thrown.expect(SyntaxException.class);
        } catch (SyntaxException e) {

        }
    }

    @Test
    public void syntaxErrorTest7() {
        String str = "A -> -> B";

        Expression expr = new Expression();
        try {
            expr.addToExpression(str);
            thrown.expect(SyntaxException.class);
        } catch (SyntaxException e) {

        }
    }

    @Test
    public void ruleErrorTest1() throws SyntaxException {
        String str = "A";
        String str1 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);


        Expression expr1 = new Expression(RuleType.AND_INTRO);
        expr1.addToExpression(str1);
        expr1.addReferenceLine("1");


        proof.addExpression(expr);
        proof.addExpression(expr1);

        assertFalse(proof.isProofValid());
        assertTrue(proof.printErrors().equals("RULE ERROR: And Introduction cannot be used here\n"));

    }

    @Test
    public void ruleErrorTest2() throws SyntaxException {
        String str = "A";
        String str1 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);


        Expression expr1 = new Expression(RuleType.AND_ELIM);
        expr1.addToExpression(str1);
        expr1.addReferenceLine("1");


        proof.addExpression(expr);
        proof.addExpression(expr1);

        assertFalse(proof.isProofValid());
        assertTrue(proof.printErrors().equals("RULE ERROR: And Elimination cannot be used here\n"));

    }
}
