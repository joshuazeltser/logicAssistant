package model;

import javassist.compiler.ast.Expr;

import java.util.*;

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

    public void convertToTruthValues(Expression expr) throws SyntaxException {

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


        evaluateTruthValues(temp);
    }

    public void evaluateTruthValues(List<String> values) {

        for (String str : values) {

            Stack<String> ops  = new Stack<String>();
            Stack<Integer> vals = new Stack<Integer>();

            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);

                switch(c) {
                    case '(': break;
                    case '&':
                    case '|':
                    case '!':
                    case '>': /*implies operator*/
                    case '~': ops.push(c + ""); break; /*Only operator*/
                    case ')':
                        String op = ops.pop();
                        int v = vals.pop();
                        switch (op) {
                            case "&": v = vals.pop() & v; break;
                            case "|": v = vals.pop() | v; break;
                            case "!": v = notOperator(v); break;
                            case ">": v = impliesOperator(vals.pop(), v); break;
                            case "~" : v = onlyOperator(vals.pop(), v); break;
                        }
                        vals.push(v);
                        break;
                    case ' ': break;
                    default : vals.push(Integer.parseInt(c + ""));
                }
            }
            System.out.println(vals.pop());
        }

    }

    private int notOperator(int v) {

        if (v == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private int onlyOperator(int x, int y) {
        if (x == 1 && y == 1) {
            return 1;
        } else if (x == 1 && y == 0) {
            return 0;
        } else if (x == 0 && y == 1) {
            return 0;
        } else {
            return 1;
        }
    }

    private int impliesOperator(int x, int y) {
        if (x == 0) {
            return 1;
        } else if (y == 1) {
            return 1;
        } else {
            return 0;
        }
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
