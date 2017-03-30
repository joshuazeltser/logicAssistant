package model;

import javassist.compiler.ast.Expr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public void evaluateTruthValues(Expression expr) throws SyntaxException {

        List<Proposition> propositions = expr.listPropositions();

        int[] result = new int[propositions.size()];

        int[][] permutations = printPermutations(propositions.size());

        List<Map<Proposition, Integer>> perms = new LinkedList<>();

        for (int i = 0; i < Math.pow(2, propositions.size()); i++) {
            Map<Proposition, Integer> permMap = new HashMap<>();
            for (int j = 0; j < propositions.size(); j++) {


                permMap.put(propositions.get(j), permutations[i][j]);
            }
            perms.add(permMap);
        }

        List<String> temp = expr.replacePropositions(perms);


        System.out.println(temp);
    }

    public int[][] printPermutations(int size) {

        int numRows = (int)Math.pow(2, size);
        int[][] perms = new int[numRows][size];

        for(int i = 0;i<perms.length;i++) {
            for(int j = 0; j < perms[i].length; j++) {

                int val = perms.length * j + i;
                int ret = (1 & (val >>> j));
                perms[i][j] = ret != 0 ? 1 : 0;
            }
        }
        return perms;
    }

}
