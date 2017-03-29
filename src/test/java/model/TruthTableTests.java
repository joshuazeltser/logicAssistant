package model;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joshuazeltser on 29/03/2017.
 */
public class TruthTableTests {

    @Test
    public void binaryPermutationTest1() {
        Expression expr = new Expression();
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        tt.printBin("", 4);
    }
}
