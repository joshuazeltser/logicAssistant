package model;

import javassist.compiler.ast.Expr;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joshuazeltser on 29/03/2017.
 */
public class TruthTable {

    private Expression result;

    private List<Expression> premises;


    public TruthTable(List<Expression> premises, Expression result) {
        this.premises = premises;
        this.result = result;
    }

    public int[] evaluateTruthValues(Expression expr) {

        List<Proposition> propositions = expr.listPropositions();

        int[] result = new int[propositions.size()];

        for (Proposition p : propositions) {
            // replace all p with 0


            // replace all p with 1
        }

        return result;

    }

    public void printBin(String soFar, int iterations) {
        if(iterations == 0) {
            System.out.println(soFar);
        }
        else {
            printBin(soFar + "0", iterations - 1);
            printBin(soFar + "1", iterations - 1);
        }
    }

}
