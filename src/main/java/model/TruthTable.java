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

    public boolean validateProof() throws SyntaxException {

        List<List<Integer>> truthLists = new LinkedList<>();


        List<LinkedHashMap<Proposition, Integer>> resultMap = convertToTruthValues(result);



        for (int i = 0; i < premises.size()-1; i++) {
            List<LinkedHashMap<Proposition, Integer>> premiseMap1 = convertToTruthValues(premises.get(i));
            List<LinkedHashMap<Proposition, Integer>> premiseMap2 = convertToTruthValues(premises.get(i+1));

            if (!compareListMaps(premiseMap1, premiseMap2)) {
                return false;
            }
        }

        if (!compareListMaps(resultMap, convertToTruthValues(premises.get(premises.size()-1)))) {
            return false;
        }




        return true;
    }

    private boolean compareListMaps(List<LinkedHashMap<Proposition, Integer>> premiseMap1,
                                    List<LinkedHashMap<Proposition, Integer>> premiseMap2) {

        List<LinkedHashMap<Proposition, Integer>> first = new LinkedList<>();
        List<LinkedHashMap<Proposition, Integer>> second = new LinkedList<>();

        if (premiseMap1.size() > premiseMap2.size()) {
            first = premiseMap1;
            second = premiseMap2;
        } else {
            first = premiseMap2;
            second = premiseMap1;
        }

        System.out.println(first);
        System.out.println(second);

        for (LinkedHashMap<Proposition, Integer> map : second) {
            for (Map.Entry<Proposition, Integer> entry : map.entrySet()) {
                for (LinkedHashMap<Proposition, Integer> longMap : first) {
                    for (Map.Entry<Proposition, Integer> longMapEntry : longMap.entrySet()) {

                        if (entry.getKey().toString().equals(longMapEntry.getKey().toString())) {

                            if (entry.getValue() != longMapEntry.getValue()) {
                                return false;
                            }
                        }
                    }

                }
            }

        }
        return true;

    }

    public List<LinkedHashMap<Proposition, Integer>> convertToTruthValues(Expression expr) throws SyntaxException {

        List<Proposition> propositions = expr.listPropositions();

        int[][] permutations = generatePermutations(propositions.size());

        List<LinkedHashMap<Proposition, Integer>> perms = new LinkedList<>();


        for (int i = 0; i < Math.pow(2, propositions.size()); i++) {
            LinkedHashMap<Proposition, Integer> permMap = new LinkedHashMap<>();
            for (int j = 0; j < propositions.size(); j++) {

                permMap.put(propositions.get(j), permutations[i][j]);
            }

            perms.add(permMap);
        }

        List<String> replacedProps = expr.replacePropositions(perms);



        return evaluateTruthValues(replacedProps, perms);
    }

    public List<LinkedHashMap<Proposition, Integer>> evaluateTruthValues(List<String> values,
                                                               List<LinkedHashMap<Proposition, Integer>> perms) {

        List<LinkedHashMap<Proposition, Integer>> results = new LinkedList();
        int count = 0;
        for (String str : values) {

            Stack<String> ops  = new Stack<>();
            Stack<Integer> vals = new Stack<>();

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

                        if (str.length() == 6) {
                            break;
                        }
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
            if (vals.peek() == 1) {

                results.add(perms.get(count));
            }

            count++;
        }



        return results;
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

    public int[][] generatePermutations(int size) {

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
