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

    public List<String> printBin(int numProps) {

        List<String> perms = new LinkedList<>();

        for (int i=0;i<Math.pow(2,numProps);i++){
            int mask = (int) (Math.pow(2,numProps) + 1);
            String perm = "";
            while (mask > 0){
                if ((mask & i) == 0){
                    perm += "0";
                } else {
                    perm += "1";
                }
                mask = mask >> 1;
            }
            perms.add(perm);
        }
        return perms;
    }

}
