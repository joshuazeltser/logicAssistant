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
        assertTrue(proof.printErrors().equals("RULE ERROR: And Introduction cannot be used here\n<br>"));

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
        assertTrue(proof.printErrors().equals("RULE ERROR: And Elimination cannot be used here\n<br>"));

    }

    @Test
    public void ruleErrorTest3() throws SyntaxException {
        String str = "A";
        String str1 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);


        Expression expr1 = new Expression(RuleType.OR_INTRO);
        expr1.addToExpression(str1);
        expr1.addReferenceLine("1");


        proof.addExpression(expr);
        proof.addExpression(expr1);

        assertFalse(proof.isProofValid());
        assertTrue(proof.printErrors().equals("RULE ERROR: Or Introduction cannot be used here\n<br>"));
    }

    @Test
    public void ruleErrorTest4() throws SyntaxException {
        String str = "A";
        String str1 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);


        Expression expr1 = new Expression(RuleType.OR_ELIM);
        expr1.addToExpression(str1);
        expr1.addReferenceLine("1");


        proof.addExpression(expr);
        proof.addExpression(expr1);

        assertFalse(proof.isProofValid());
        assertTrue(proof.printErrors().equals("RULE ERROR: Or Elimination cannot be used here\n<br>"));

    }

    @Test
    public void ruleErrorTest5() throws SyntaxException {
        String str = "A";
        String str1 = "B";
        String str2 = "C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.IMPLIES_INTRO);
        expr2.addToExpression(str2);
        expr2.addReferenceLine("1");
        expr2.addReferenceLine("2");


        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        assertFalse(proof.isProofValid());
        assertTrue(proof.printErrors().equals("RULE ERROR: Implies Introduction cannot be used here\n<br>"));
    }

    @Test
    public void ruleErrorTest6() throws SyntaxException {
        String str = "A";
        String str1 = "B";
        String str2 = "C";

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

        assertFalse(proof.isProofValid());
        assertTrue(proof.printErrors().equals("RULE ERROR: Implies Elimination cannot be used here\n<br>"));
    }

    @Test
    public void ruleErrorTest7() throws SyntaxException {
        String str = "A";
        String str1 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);


        Expression expr1 = new Expression(RuleType.DOUBLE_NOT_ELIMINATION);
        expr1.addToExpression(str1);
        expr1.addReferenceLine("1");


        proof.addExpression(expr);
        proof.addExpression(expr1);

        assertFalse(proof.isProofValid());
        assertTrue(proof.printErrors().equals("RULE ERROR: Double Not Elimination cannot be used here\n<br>"));

    }

    @Test
    public void ruleErrorTest8() throws SyntaxException {
        String str = "A";
        String str1 = "B";
        String str2 = "C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.NOT_ELIM);
        expr2.addToExpression(str2);
        expr2.addReferenceLine("1");
        expr2.addReferenceLine("2");


        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        assertFalse(proof.isProofValid());

        assertTrue(proof.printErrors().equals("RULE ERROR: Not Elimination cannot be used here\n<br>"));
    }
}
