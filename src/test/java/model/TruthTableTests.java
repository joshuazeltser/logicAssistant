package model;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

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


        tt.convertToTruthValues(expr);

    }

    @Test
    public void binaryPermutationTest2() throws SyntaxException {
        String str = "A ^ (!B | C)";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        tt.convertToTruthValues(expr);

    }

    @Test
    public void binaryPermutationTest3() throws SyntaxException {
        String str = "(A -> B) | (C -> D)";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);


        tt.convertToTruthValues(expr);

    }


}
