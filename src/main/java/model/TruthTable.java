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



        List<String> propositions =  new LinkedList<>();

        for (Expression expr : premises) {
            List<Proposition> props = expr.listPropositions();

            for (Proposition p : props) {
                if (!propositions.contains(p.toString())) {
                    propositions.add(p.toString());
                }
            }
        }

        List<Proposition> props = result.listPropositions();

        for (Proposition p : props) {
            if (!propositions.contains(p.toString())) {
                propositions.add(p.toString());
            }
        }

        int[][] perms = generatePermutations(propositions.size());


        String[][] truthTable =
                new String[(int) Math.pow(2, propositions.size())+1][propositions.size() + premises.size() + 1];


        // Fill top row of table
        for (String[] row : truthTable) {
            Arrays.fill(row, "0");
        }


        for (int i = 0; i < propositions.size(); i++) {
            truthTable[0][i] = propositions.get(i);
        }
        int count = 1;
        for (int j = propositions.size(); j < propositions.size() + premises.size(); j++) {
            truthTable[0][j] = Integer.toString(count);
            count++;
        }

        truthTable[0][propositions.size() + premises.size()] = "R";

        //fill in all permutations of propositions
        for (int i = 1; i < (int) Math.pow(2, propositions.size()) + 1; i++) {
            for (int j = 0; j < propositions.size(); j++) {
                truthTable[i][j] = Integer.toString(perms[i-1][j]);
            }
        }

        //fill in all evaluated truth values for expressions
        for (int i = 0; i < premises.size(); i++) {
           addExpressionValues(truthTable, propositions, premises.get(i), i);
        }

        addExpressionValues(truthTable,propositions, result, premises.size());












        //print results for testing purposes
        for (int i = 0; i < (int) Math.pow(2, propositions.size()) + 1; i++) {
            for (int j = 0; j < propositions.size() + premises.size() + 1; j++) {
                System.out.print(truthTable[i][j] + " ");
            }
            System.out.println();
        }





        return false;
    }

    private void addExpressionValues(String[][] truthTable, List<String> propositions,
                                     Expression expr, int i) throws SyntaxException {

        LinkedHashMap<LinkedHashMap<Proposition, Integer>, Integer> exprTruth = convertToTruthValues(expr);
        System.out.println(exprTruth);


        List<Proposition> p = expr.listPropositions();
        List<String> str = new LinkedList<>();

        for (Proposition pr : p) {
            str.add(pr.toString());
        }


        List<String> zeroColumn = new LinkedList<>();

        for (String pr : propositions) {
            if (!str.contains(pr.toString())) {
                zeroColumn.add(pr.toString());
            }
        }
        System.out.println(str);
        System.out.println(zeroColumn);
        System.out.println();

        List<Integer> columns = new LinkedList<>();

        for (Map.Entry<LinkedHashMap<Proposition, Integer>, Integer> map : exprTruth.entrySet()) {

            for (Map.Entry<Proposition, Integer> innerMap : map.getKey().entrySet()) {

                System.out.println(innerMap.getKey());
                for (int j = 0; j < propositions.size(); j++) {
                    if (innerMap.getKey().toString().equals(truthTable[0][j])) {
                        columns.add(j);
                    }
                }

            }
            break;
        }

        //list of list of values of props used in each row
        List<List<Integer>> values = new LinkedList<>();
        List<Integer> premiseVals = new LinkedList<>();

        for (Map.Entry<LinkedHashMap<Proposition, Integer>, Integer> map : exprTruth.entrySet()) {
            List<Integer> newMap = new LinkedList<>();
            for (Map.Entry<Proposition, Integer> innerMap : map.getKey().entrySet()) {
                for (String s : propositions) {
                    if (s.equals(innerMap.getKey().toString())) {
                        newMap.add(innerMap.getValue());
                    }
                }
            }
            values.add(newMap);
            premiseVals.add(map.getValue());
        }
        System.out.println(values);
        System.out.println(premiseVals);

        boolean notRow = false;
        int premiseCount = 0;
        for (List<Integer> row : values) {
            for (int q = 1; q <  (int) Math.pow(2, propositions.size())+1; q++) {
                int count1 = 0;
                for (Integer num : columns) {
                    if (Integer.parseInt(truthTable[q][num]) != row.get(count1)) {
                        notRow = true;
                        break;
                    }
                    count1++;
                }
                count1 = 0;
                if (!notRow) {
                    truthTable[q][propositions.size() + i] = premiseVals.get(premiseCount).toString();
                }
                notRow = false;

            }
            premiseCount++;

        }

    }


    public LinkedHashMap<LinkedHashMap<Proposition, Integer>,Integer> convertToTruthValues(Expression expr) throws SyntaxException {

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

    public LinkedHashMap<LinkedHashMap<Proposition, Integer>,Integer> evaluateTruthValues(List<String> values, List<LinkedHashMap<Proposition, Integer>> perms) {

        LinkedHashMap<LinkedHashMap<Proposition, Integer>,Integer> results = new LinkedHashMap<>();
        int[] result = new int[values.size()];
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
//            if (vals.peek() == 1) {
//
                results.put(perms.get(count), vals.peek());



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
