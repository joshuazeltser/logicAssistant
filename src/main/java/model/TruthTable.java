package model;

import java.util.*;

/**
 * Created by joshuazeltser on 29/03/2017.
 */
public class TruthTable {

    private Expression result;

    private List<Expression> premises;

    private String ttPremises;
    private String ttResults;

    private List<String> errors;


    public TruthTable() {
        ttPremises = "";
        ttResults = "";
        premises = new LinkedList<>();
        result = new Expression();
        errors = new LinkedList<>();
    }

    public String frontEndFunctionality(String prems, String res) throws SyntaxException {

        if (!res.equals("")) {
            readPremisesFromInput(prems, res);

            if (!errors.isEmpty()) {
                return printErrors();
            }

            boolean result = validateProof();

            if (result) {
                return "Conclusion IS provable using these premises";
            } else {
                return "Conclusion is NOT provable using these premises";
            }
        }
        else {
            return "";
        }
    }

    public String frontEndLemmaFunctionality(String res) throws SyntaxException {

        if (!res.equals("")) {
            readPremisesFromInput("", res);
            if (!errors.isEmpty()) {
                return printErrors();
            }

            boolean result = validateProof();

            if (result) {
                return "Lemma is Valid";
            } else {
                return "This lemma cannot be proven by itself, so can't be used!\nTry adding it as a premise instead?";
            }
        }
        else {
            return "";
        }
    }

    public String printErrors() {
        String result = "";
        for (String str : errors) {
            result += str + "\n<br>";
        }
        return result;
    }

    public void readPremisesFromInput(String prems, String res) {

            Expression temp2 = new Expression(RuleType.GIVEN);
            try {
                temp2.addToExpression(res);
            } catch (SyntaxException se) {
                errors.add(se.getMessage());
                return;
            }
            result = temp2;

            if (prems.equals("")) {
                return;
            }

            String[] premList = prems.split("\\r?\\n");



            for (int i = 0; i < premList.length; i++) {
                Expression temp = new Expression(RuleType.GIVEN);
                try {
                    temp.addToExpression(premList[i]);
                } catch (SyntaxException se) {
                    errors.add("Line " + (i + 1) + " - Syntax Error: " + se.getMessage());
                    return;
                }
                premises.add(temp);
            }


    }

    public boolean validateProof() throws SyntaxException {

        //Find all propositions in proof
        List<String> propositions = generateProofPropositions();

        // Generate all possible permutations of these propositions in bits
        int[][] perms = generatePermutations(propositions.size());

        // Create a truth table
        String[][] truthTable =
                new String[(int) Math.pow(2, propositions.size())+1][propositions.size() + premises.size() + 1];

        // Fill the top row with the names of the propositions
        for (int i = 0; i < propositions.size(); i++) {
            truthTable[0][i] = propositions.get(i);
        }

        // fill the rest of the top row with premise numbers and R for result
        int count = 1;
        for (int j = propositions.size(); j < propositions.size() + premises.size(); j++) {
            truthTable[0][j] = Integer.toString(count);
            count++;
        }
        truthTable[0][propositions.size() + premises.size()] = "R";

        //fill truth table in with all permutations of propositions
        for (int i = 1; i < (int) Math.pow(2, propositions.size()) + 1; i++) {
            for (int j = 0; j < propositions.size(); j++) {
                truthTable[i][j] = Integer.toString(perms[i-1][j]);
            }
        }

        //fill in all evaluated truth values for expressions
        for (int i = 0; i < premises.size(); i++) {
           addExpressionValues(truthTable, propositions, premises.get(i), i);
        }
        addExpressionValues(truthTable, propositions, result, premises.size());

        // Check that in all rows that every premise evaluates to true the result also evaluates to true
        if (premises.size() == 0) {
            if (!checkResultValues(propositions, truthTable)) {
//                printTruthTable(propositions, truthTable);
                return false;
            }
        } else if (checkRowValidity(propositions, truthTable)) {
//            printTruthTable(propositions, truthTable);
            return false;
        }
//        printTruthTable(propositions, truthTable);
        return true;
    }

    private boolean checkResultValues(List<String> propositions, String[][] truthTable) {
        for (int i = 1; i < (int) Math.pow(2, propositions.size())+1; i++) {
            if (Integer.parseInt(truthTable[i][propositions.size() + premises.size()]) != 1) {
                return false;
            }
        }
        return true;
    }


    private boolean checkRowValidity(List<String> propositions, String[][] truthTable) {
        for (int i = 1; i < (int) Math.pow(2, propositions.size())+1; i++) {
            for (int j = propositions.size(); j < propositions.size() + premises.size() + 1; j++) {
                if (Integer.parseInt(truthTable[i][j]) != 1) {
                    if (j == propositions.size() + premises.size()) {
                        return true;
                    }
                    break;
                }

            }
        }
        return false;
    }

    private void printTruthTable(List<String> propositions, String[][] truthTable) {
        //print results for testing purposes
        for (int i = 0; i < (int) Math.pow(2, propositions.size()) + 1; i++) {
            for (int j = 0; j < propositions.size() + premises.size() + 1; j++) {
                System.out.print(truthTable[i][j] + " ");
            }
            System.out.println();
        }
    }

    private List<String> generateProofPropositions() {
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
        return propositions;
    }

    private void addExpressionValues(String[][] truthTable, List<String> propositions,
                                     Expression expr, int i) throws SyntaxException {

        //convert the expression to maps of permutations to their results
        LinkedHashMap<LinkedHashMap<Proposition, Integer>, Integer> exprTruth = convertToTruthValues(expr);

        List<Proposition> p = expr.listPropositions();
        List<String> str = new LinkedList<>();

        // Create list of propositions used in this expression
        for (Proposition pr : p) {
            str.add(pr.toString());
        }

        // Create list of propositions used in the proof but not in this expression
        List<String> zeroColumn = new LinkedList<>();
        for (String pr : propositions) {
            if (!str.contains(pr.toString())) {
                zeroColumn.add(pr.toString());
            }
        }
        List<Integer> columns = new LinkedList<>();

        // iterate through the map finding the columns in the truth table corresponding to the propositions in the expr
        for (Map.Entry<LinkedHashMap<Proposition, Integer>, Integer> map : exprTruth.entrySet()) {

            for (Map.Entry<Proposition, Integer> innerMap : map.getKey().entrySet()) {

                for (int j = 0; j < propositions.size(); j++) {
                    if (innerMap.getKey().toString().equals(truthTable[0][j])) {
                        columns.add(j);
                    }
                }

            }
            break;
        }

        //Create a list of the values of each proposition in each permutation as well as a list of the resulting values
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

        // add these truth values to the table
        addTruthResultsToTable(truthTable, propositions, i, columns, values, premiseVals);

    }

    private void addTruthResultsToTable(String[][] truthTable, List<String> propositions, int i, List<Integer> columns, List<List<Integer>> values, List<Integer> premiseVals) {
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

                        if (str.length() == 5 || ops.isEmpty()) {
                            break;
                        }

                        String op = ops.pop();
                        int v = vals.pop();
                        switch (op) {
                            case "&": v = andOperator(vals.pop(), v); break;
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

    private int andOperator(int x, int y) {
        if (x == 1 && y == 1) {
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


    public Expression getResult() {
        return result;
    }

    public void setResult(Expression result) {
        this.result = result;
    }



    public List<Expression> getPremises() {
        return premises;
    }

    public void setPremises(List<Expression> premises) {
        this.premises = premises;
    }

    public String getTtPremises() {
        return ttPremises;
    }

    public void setTtPremises(String ttPremises) {
        this.ttPremises = ttPremises;
    }

    public String getTtResults() {
        return ttResults;
    }

    public void setTtResults(String ttResults) {
        this.ttResults = ttResults;
    }
}
