package model;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by joshuazeltser on 16/02/2017.
 */
public class ErrorTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
}
