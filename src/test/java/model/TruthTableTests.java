package model;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 29/03/2017.
 */
public class TruthTableTests {

    @Test
    public void binaryPermutationTest1() throws SyntaxException {
        String str = "A ^ B";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        List<Integer> expected = Arrays.asList(0,0,0,1);

        assertTrue(tt.convertToTruthValues(expr).equals(expected));

    }

    @Test
    public void binaryPermutationTest2() throws SyntaxException {
        String str = "A ^ (!B | C)";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        List<Integer> expected = Arrays.asList(0, 1, 0, 0, 0, 1, 0, 1);

        assertTrue(tt.convertToTruthValues(expr).equals(expected));

    }

    @Test
    public void binaryPermutationTest3() throws SyntaxException {
        String str = "(A -> B) | (C -> D)";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        tt.convertToTruthValues(expr);

        List<Integer> expected = Arrays.asList(1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);

        assertTrue(tt.convertToTruthValues(expr).equals(expected));
    }

    @Test
    public void binaryPermutationTest4() throws SyntaxException {
        String str = "(A -> B) <-> C";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        tt.convertToTruthValues(expr);

        List<Integer> expected = Arrays.asList(0, 1, 0, 0, 1, 0, 1, 1);

        assertTrue(tt.convertToTruthValues(expr).equals(expected));
    }

    @Test
    public void binaryPermutationTest5() throws SyntaxException {
        String str = "(A -> C) -> (B ^ D)";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        tt.convertToTruthValues(expr);

        List<Integer> expected = Arrays.asList(0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1);
        
        assertTrue(tt.convertToTruthValues(expr).equals(expected));
    }


}
