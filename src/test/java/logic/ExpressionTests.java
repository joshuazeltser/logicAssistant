package logic;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTests {

    Expression expression = new Expression();

    @Test
    public void simpleCorrectPropositionsFromExpression() {
        String expr = "(A | B)";

        expression.addToExpression(expr);

        String result = "";

        for (Proposition p : expression.listPropositions()) {
            result += p.toString();
        }

        assertTrue(result.equals("AB"));
    }

    @Test
    public void correctPropositionsFromExpression() {
        String expr = "((A | B) ^ (C ^ D))";

        expression.addToExpression(expr);

        String result = "";

        for (Proposition p : expression.listPropositions()) {
            result += p.toString();
       }

        assertTrue(result.equals("ABCD"));
    }


    @Test
    public void correctConversionToComponents() {
        String test1 = "(A -> B) | C";

        expression.addToExpression(test1);

        System.out.println(expression.toString());
        assertTrue(expression.toString().equals("OPEN A IMPLIES B CLOSE OR C"));
    }

    @Test
    public void complexConversionToComponents() {
        String test1 = "((A -> B) | (C ^ D) ^ (E <-> F))";

        expression.addToExpression(test1);

        System.out.println(expression.toString());
        assertTrue(expression.toString().equals(
                "OPEN OPEN A IMPLIES B CLOSE OR OPEN C AND D CLOSE AND OPEN E ONLY F CLOSE CLOSE"));
    }

}