package model;

import java.util.*;

import static model.OperatorType.*;

/**
 * Created by joshuazeltser on 18/01/2017.
 */
public class Proof {

    private List<Expression> expressions;

    private List<String> errors;

    protected String proofString;
    protected String proofLabels;

    private Expression resultExpr;

    private List<Expression> list_proof;

    private List<Expression> list_goals;
    private List<Expression> solvedProof;

    private List<Expression> assumptions;

    private Map<RuleType, Expression> introductions;
    private boolean terminate;
    private List<Expression> inscope;
    private List<Expression> temp1;
    private int rhsIndex;

    private boolean firstOrRound;

    private Stack<Expression> lhsStack;
    private Stack<Expression> rhsStack;
    private Stack<Expression> orElimStack;
    private boolean orsLeft;

    private List<Expression> extra_list_proof;


    public Proof() {
        expressions = new LinkedList<>();
        errors = new LinkedList<>();
        proofString = "";
        proofLabels = "";
        resultExpr = new Expression();
        list_goals = new LinkedList<>();
        list_proof = new LinkedList<>();
        extra_list_proof = new LinkedList<>();
        assumptions = new LinkedList<>();
        introductions = new LinkedHashMap<>();
        temp1 = new LinkedList<>();
        firstOrRound = true;
        lhsStack = new Stack<>();
        rhsStack = new Stack<>();
        orElimStack = new Stack<>();
        orsLeft = false;
    }

    public String frontEndFunctionality(String proof, String rule) throws SyntaxException{

        expressions.clear();

        String result = "";

        try {
            separateByNewLine(proof, rule);

            result = frontEndProofValidity();

        } catch (SyntaxException s) {
            errors.add(s.getMessage());
        }

        if (errors.size() > 0) {
            result = printErrors();
        }

        return result;
    }

    public void separateByNewLine(String proof, String rule) throws SyntaxException {
        int count = 0;

        if (!proof.equals("")) {
            String[] steps = proof.split("\\r?\\n");
            String[] exprRule = rule.split("\\r?\\n");

            String expr[] = new String[steps.length];
            for (int i = 0; i < steps.length; i++) {
                if (!steps[i].equals("") && steps[i].charAt(0) == '-') {
                    int count1 = 0;
                    for (int j = 0; j < steps[i].length(); j++) {

                        if (steps[i].charAt(j) != '-') {
                            break;
                        }
                        count1++;
                    }

                    expr[i] = steps[i].substring(count1);

                } else {
                    expr[i] = steps[i];
                }
            }

            for (int i = 0; i < expr.length-1; i++) {
                String toAdd = "";
                for (int j = 0; j < expr[i].length(); j++) {
                        if (expr[i].charAt(j) == ' ' && expr[i].charAt(j+1) == ' ') {
                           for (int k = j+2; k < expr[i].length(); k++) {
                               toAdd += expr[i].charAt(k);
                           }
                           break;
                        }
                        toAdd += expr[i].charAt(j);

                }
                expr[i] = toAdd;

            }
            int limit = 0;
            if (expr.length == exprRule.length) {
                limit = exprRule.length;
            } else {
                limit = expr.length - 1;
            }

            for (int i = 0; i < limit; i++) {
                if (i >= exprRule.length) {
                    Expression newExpr = new Expression();

                    if (!expr[i].equals("")) {

                        newExpr.addToExpression(expr[i]);
                    } else {
                        if (i == expr.length-2) {
                            continue;
                        }
                    }

                    addExpression(newExpr);
                    continue;
                }
                String[] components = exprRule[i].split(" ");
                Expression newExpr = new Expression(convertStringToRule(components[0]));
                try {
                    if (!expr[i].equals("")) {
                        newExpr.addToExpression(expr[i]);
                    } else {
                        if (i == expr.length-2) {
                            continue;
                        }
                        addExpression(newExpr);
                        continue;
                    }



                } catch (SyntaxException s) {

                    errors = new LinkedList<>();
                    errors.add("LINE " + (i + 1) + " - " + s.getMessage());
                    //if there is a syntax error don't display other error messages
                    return;
                }


                if (!components[0].equals("GIVEN") && !components[0].equals("ASSUMPTION") &&
                        !components[0].equals("Lemma")) {
                    if (components.length == 1) {

                        String comps = removeBracketsFromString(components[0]);
                        String[] lines = comps.split(",");


                        for (int j = 0; j < lines.length; j++) {
                            if (!lines[j].equals("")) {
                                newExpr.addReferenceLine(lines[j]);
                            }
                        }
                    } else {
                        if (components[1].equals("")) {
                            count++;
                            continue;
                        }
                        String comps = removeBracketsFromString(components[1]);

                        String[] lines = comps.split(",");
                        for (int j = 0; j < lines.length; j++) {
                            newExpr.addReferenceLine(lines[j]);
                        }
                    }
                }

                addExpression(newExpr);
            }


            Expression res = new Expression();
            res.addToExpression(expr[expr.length-1-count]);
            setResultExpr(res);
        } else {
            return;
        }

    }

    private String removeBracketsFromString(String str) {
        String result = "";


        for (int i = 1; i < str.length()-1; i++) {
            result += str.charAt(i);
        }
        return result.toString();
    }

    public String printErrors() {
        String result = "";
        for (String str : errors) {
            result += str + "\n<br>";
        }
        return result;
    }

    private RuleType convertStringToRule(String rule) {
        switch (rule) {
            case "Lemma":
            case "GIVEN": return RuleType.GIVEN;
            case "ASSUMPTION": return RuleType.ASSUMPTION;
            case "And-Intro": return RuleType.AND_INTRO;
            case "And-Elim": return RuleType.AND_ELIM;
            case "Or-Intro": return RuleType.OR_INTRO;
            case "Or-Elim": return RuleType.OR_ELIM;
            case "Not-Intro": return RuleType.NOT_INTRO;
            case "Not-Elim": return RuleType.NOT_ELIM;
            case "Implies-Intro": return RuleType.IMPLIES_INTRO;
            case "Implies-Elim": return RuleType.IMPLIES_ELIM;
            case "Only-Intro": return RuleType.ONLY_INTRO;
            case "Only-Elim": return RuleType.ONLY_ELIM;
            case "DoubleNot-Elim": return RuleType.DOUBLE_NOT_ELIM;
            case "Available": return RuleType.AVAILABLE;
            case "": return RuleType.EMPTY;
            default: return RuleType.INVALID;

        }
    }

    private String ruleFormatting(String rule) {
        switch (rule) {
            case "GIVEN": return "Given";
            case "ASSUMPTION": return "Assumption";
            case "AND_INTRO": return "And Introduction";
            case "AND_ELIM": return "And Elimination";
            case "OR_INTRO": return "Or Introduction";
            case "OR_ELIM": return "Or Elimination";
            case "NOT_INTRO": return "Not Introduction";
            case "NOT_ELIM": return "Not Elimination";
            case "IMPLIES_INTRO": return "Implies Introduction";
            case "IMPLIES_ELIM": return "Implies Elimination";
            case "ONLY_INTRO": return "Only Introduction";
            case "ONLY_ELIM": return "Only Elimination";
            case "DOUBLE_NOT_ELIM": return "Not Not Elimination";
            default: return "";
        }
    }

    public boolean isProofValid() throws SyntaxException {

        for (Expression expr : expressions) {
            if (expr.toString().equals("")) {
                return true;
            }
        }
        for (int i = expressions.size()-1; i >= 0; i--) {
            if (expressions.get(i).toString().equals("")) {
                break;
            }
            if (expressions.get(i).getRuleType() == null) {
                break;
            }
            switch (expressions.get(i).getRuleType()) {
                case AND_ELIM: isAndElimValid(expressions.get(i)); break;
                case AND_INTRO: isAndIntroValid(expressions.get(i)); break;
                case OR_ELIM: isOrEliminationValid(expressions.get(i)); break;
                case OR_INTRO: isOrIntroValid(expressions.get(i)); break;
                case IMPLIES_ELIM: isImpliesElimValid(expressions.get(i)); break;
                case IMPLIES_INTRO: isImpliesIntroValid(expressions.get(i)); break;
                case NOT_ELIM: isNotElimValid(expressions.get(i)); break;
                case NOT_INTRO: isNotIntroductionValid(expressions.get(i)); break;
                case ONLY_ELIM: isOnlyEliminationValid(expressions.get(i)); break;
                case ONLY_INTRO: isOnlyIntroValid(expressions.get(i)); break;
                case DOUBLE_NOT_ELIM: isDoubleNotElimValid(expressions.get(i)); break;
                case AVAILABLE: isAvailableRuleValid(expressions.get(i)); break;
                case INVALID: return false;
                case EMPTY: return true;
                default:
                    if (expressions.get(i).getRuleType() == null) {
                        errors.add("Proof validation not possible as some expressions do not have associated rules");
                    }
                    break;
            }
        }
        return errors.isEmpty();
    }

    public String frontEndProofValidity() throws SyntaxException {
        if (proofString.equals("") || proofLabels.equals("")) {
            //change message???
            return "";
        } else if (isProofValid()) {
            if (resultExpr.getRuleType() == null && !resultExpr.equals(expressions.get(expressions.size()-1))) {
                return "Proof validation not possible as result has not been proven";
            }
            return "Proof is Valid";
        }
        return "Proof is INVALID!";
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void addExpression(Expression expr) {
        expressions.add(expr);
    }

    @Override
    public String toString() {

        return expressions.toString();
    }



    private List<List<Integer>> findBoxIndexes() {
        List<List<Integer>> boxes = new LinkedList<>();
        for (int j = 0; j < expressions.size(); j++) {

            if (expressions.get(j).getRuleType() == RuleType.ASSUMPTION) {
                List<Integer> box = new LinkedList<>();
                box.add(j);



                for (int i = j+1; i < expressions.size(); i++) {

                    if (expressions.get(i).getRuleType() == RuleType.IMPLIES_INTRO
                            || expressions.get(i).getRuleType() == RuleType.OR_ELIM
                            || expressions.get(i).getRuleType() == RuleType.NOT_ELIM
                            && !expressions.get(i).isMarked()) {
                        expressions.get(i).setMarked(true);
                        box.add(i-1);
                        boxes.add(box);
                        break;
                    }
                }

            }
        }
        return boxes;
    }

    public boolean isAvailableRuleValid(Expression e1) {
        int ref1;

        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
        } catch (IndexOutOfBoundsException ioe) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Two lines must be referenced when " +
                    "using this rule");
            return false;
        }

        Boolean x = checkReferenceInScope1(e1, ref1);
        if (x != null) return x;

        if (!e1.equals(expressions.get(ref1))) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: This line cannot be used for " +
                    "this rule");
            return false;
        }

        return true;

    }

    public boolean isAndIntroValid(Expression e1) {

        if (!e1.contains(new Operator("AND",OperatorType.AND))) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: And Introduction cannot " +
                    "be used here");
            return false;
        }
        List<Expression> sides = e1.splitExpressionBy(OperatorType.AND);
        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);
        int ref1;
        int ref2;

        try {
           ref1 = e1.getReferenceLine().get(0) - 1;
           ref2 = e1.getReferenceLine().get(1) - 1;


        } catch (IndexOutOfBoundsException ioe) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Two lines must be referenced when " +
                    "using this rule");
            return false;
        }

        Boolean x = checkReferencesInScope2(e1, ref1, ref2);
        if (x != null) return x;


        if (!expressions.get(ref1).equals(lhs) && !expressions.get(ref1).equals(rhs)) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: This line cannot be used for " +
                    "this And Introduction");
            return false;
        }

        if (!expressions.get(ref2).equals(lhs) && !expressions.get(ref2).equals(rhs)) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: The line referenced cannot be used " +
                    "for this And Introduction");
            return false;
        }

        return true;
    }

    private Boolean checkReferencesInScope2(Expression e1, int ref1, int ref2) {

        List<List<Integer>> boxes = findBoxIndexes();
        int index = findExpressionIndex(e1);

        for (List<Integer> box : boxes) {
            if (index <= box.get(1) && index >= box.get(0)) {
                break;
            }
            if ((ref1 > box.get(0) && ref1 < box.get(1)) && expressions.get(ref1).getRuleType() != RuleType.ASSUMPTION
                    || (ref2 > box.get(0) && ref2 < box.get(1) &&
                    expressions.get(ref2).getRuleType() != RuleType.ASSUMPTION)) {
                errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: You cannot reference a line " +
                        "that is inside a completed box");
                return false;
            }
        }
        return null;
    }


    public boolean isAndElimValid(Expression e1) {

        int ref1;
        try {
            ref1 = e1.getReferenceLine().get(0) - 1;

            Boolean x = checkReferenceInScope1(e1, ref1);
            if (x != null) return x;

            if (!expressions.get(ref1).contains(new Operator("AND", OperatorType.AND))) {
                errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: The line referenced cannot be " +
                        "used for this And Elimination");
                return false;
            }
        } catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: A valid line must be referenced to " +
                    "use this rule");
            return false;
        }


    List<Expression> sides = expressions.get(ref1).splitExpressionBy(OperatorType.AND);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);



        if (lhs.equals(e1) || rhs.equals(e1)) {
            return true;
        }

        errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: And Elimination cannot be used here " +
                "with this reference");
        return false;
    }

    private Boolean checkReferenceInScope1(Expression e1, int ref1) {
        List<List<Integer>> boxes = findBoxIndexes();

        int index = findExpressionIndex(e1);

        for (List<Integer> box : boxes) {
            if (index <= box.get(1) && index >= box.get(0)) {
                break;
            }
            if ((ref1 > box.get(0) && ref1 < box.get(1)) &&
                    expressions.get(ref1).getRuleType() != RuleType.ASSUMPTION) {
                errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: You cannot reference a line " +
                        "that is inside a completed box");
                return false;
            }
        }
        return null;
    }

    public boolean isOrIntroValid(Expression e1) {

        if (!e1.contains(new Operator("OR", OperatorType.OR))) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: This line cannot be used for " +
                    "Or Introduction");
            return false;
        }

        List<Expression> sides = e1.splitExpressionBy(OperatorType.OR);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        int ref1;
        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
        } catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: A valid line must be referenced " +
                    "to use this rule");
            return false;
        }

        Boolean x = checkReferenceInScope1(e1, ref1);
        if (x != null) return x;

        if (expressions.get(ref1).equals(lhs) || expressions.get(ref1).equals(rhs)) {
                return true;
        }

        errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Or Introduction cannot be used here " +
                "with this reference");
        return false;
    }


    public boolean isImpliesIntroValid(Expression e1) {

        if (!e1.contains(new Operator("IMPLIES", OperatorType.IMPLIES))) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: This line cannot be used for " +
                    "Implies Introduction");
            return false;
        }

        List<Expression> sides = e1.splitExpressionBy(OperatorType.IMPLIES);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        int ref1;
        int ref2;

        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
            ref2 = e1.getReferenceLine().get(1) - 1;
        } catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Two valid lines must be referenced " +
                    "to use this rule");
            return false;
        }

        Boolean x = checkReferencesInScope2(e1, ref1, ref2);
        if (x != null) return x;

        Expression assumption = expressions.get(ref1);
        Expression conclusion = expressions.get(ref2);

        if (assumption.equals(lhs) && assumption.getRuleType() == RuleType.ASSUMPTION &&
                conclusion.equals(rhs)) {
            return true;
        }

        errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Implies Introduction cannot be used here");
        return false;
    }

    public boolean isImpliesElimValid(Expression e1) {

        int ref1;
        int ref2;

        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
            ref2 = e1.getReferenceLine().get(1) - 1;
        } catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Two valid lines must be " +
                    "referenced to use this rule");
            return false;
        }

        Boolean x = checkReferencesInScope2(e1, ref1, ref2);
        if (x != null) return x;

        Expression a = expressions.get(ref1);
        Expression b = expressions.get(ref2);

        if (a.contains(new Operator("IMPLIES", OperatorType.IMPLIES))) {
            List<Expression> sides = a.splitExpressionBy(OperatorType.IMPLIES);
            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            if (rhs.equals(e1) && lhs.equals(b)) {
                return true;
            }
        }

        if (b.contains(new Operator("IMPLIES", OperatorType.IMPLIES))) {
            List<Expression> sides = b.splitExpressionBy(OperatorType.IMPLIES);
            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            if (rhs.equals(e1) && lhs.equals(a)) {
                return true;
            }
        } else {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: This reference cannot be used for " +
                    "Implies Elimination");
            return false;
        }

        errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Implies Elimination cannot be used here");
        return false;
    }

    private int findExpressionIndex(Expression e1) {
        return expressions.indexOf(e1);
    }


    public boolean isDoubleNotElimValid(Expression e1) throws SyntaxException {

        int ref1;

        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
        } catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: A valid line must be referenced to use this rule");
            return false;
        }

        Boolean x = checkReferenceInScope1(e1, ref1);
        if (x != null) return x;

        Expression temp1 = new Expression();
        Expression expr = expressions.get(ref1);
        if (expr.contains(new Operator("NOT", NOT))) {
            Expression temp = new Expression();
            temp.addToExpression(expr.toString());
            temp.removeNcomponents(1);
            if (temp.contains(new Operator("NOT", NOT))) {

                temp1.addToExpression(temp.toString());
                temp1.removeNcomponents(1);
            } else {
                errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: This reference cannot be used " +
                        "for NotNot Elimination as there is no double negation");
                return false;
            }
        } else {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: This reference cannot be used for " +
                    "NotNot Elimination as there is no double negation");
            return false;
        }

        if (temp1.equals(e1)) {
            return true;
        }

        errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Double Not Elimination cannot" +
                " be used here");
        return false;
    }

    public boolean isNotElimValid(Expression e1) throws SyntaxException {
        if (!e1.toString().equals("FALSE")) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Not Elimination cannot be used " +
                    "here as this line is not FALSE");
            return false;
        }

        int ref1;
        int ref2;

        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
            ref2 = e1.getReferenceLine().get(1) - 1;
        }  catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Two valid lines must be referenced" +
                    " to use this rule");
            return false;
        }

        Boolean x = checkReferencesInScope2(e1, ref1, ref2);
        if (x != null) return x;

        Expression a = expressions.get(ref1);
        Expression b = expressions.get(ref2);


        Expression c = new Expression();
        c.addToExpression("" + a);

        if (c.toString().equals("") || c.equalExceptFirst(b)) {
            return true;
        }

        Expression d = new Expression();

        d.addToExpression("" + b);
        if (d.toString().equals("") || d.equalExceptFirst(a)) {
            return true;
        }


        errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Not Elimination cannot be used here");
        return false;

    }

    public boolean isOnlyEliminationValid(Expression e1) throws SyntaxException {

        int ref1;
        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
        } catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: A valid line must be " +
                    "used to use this rule");
            return false;
        }

        Boolean x = checkReferenceInScope1(e1, ref1);
        if (x != null) return x;

        Expression expr = expressions.get(ref1);

        if (expr.contains(new Operator("ONLY", OperatorType.ONLY))) {

            List<Expression> sides = expr.splitExpressionBy(OperatorType.ONLY);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            Expression result = new Expression();

            result.addToExpression(lhs + " -> " + rhs);

            if (result.equals(e1)) {
                return true;
            }

            Expression result1 = new Expression();

            result1.addToExpression(rhs + " -> " + lhs);
            if (result1.equals(e1)) {
                 return true;
            } else {
                errors.add("LINE " + (expressions.indexOf(e1) + 1) + " -RULE ERROR: This reference cannot be used" +
                        " for Only Elimination");
                return false;
            }
        }

        errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Only Elimination cannot be used here" +
                " as there is no ONLY operator in this expression");
        return false;
    }

    public boolean isOnlyIntroValid(Expression e1) throws SyntaxException {

        if (!e1.contains(new Operator("ONLY", OperatorType.ONLY))) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Only Introduction cannot be used" +
                    " here as the expression doesn't contain an ONLY operator");
            return false;
        }

        List<Expression> sides = e1.splitExpressionBy(OperatorType.ONLY);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        Expression result = new Expression();
        Expression result1 = new Expression();

        result.addToExpression(lhs + " -> " + rhs);
        result1.addToExpression(rhs + " -> " + lhs);

        Expression expr;
        Expression expr1;
        try {
            int ref1 = e1.getReferenceLine().get(0) - 1;
            expr = expressions.get(ref1);

            int ref2 = e1.getReferenceLine().get(1) - 1;
            expr1 = expressions.get(ref2);

            Boolean x = checkReferencesInScope2(e1, ref1, ref2);
            if (x != null) return x;

        } catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Two valid lines must be referenced " +
                    "to use this rule");
            return false;
        }



        if ((expr.equals(result) && expr1.equals(result1)) ||  (expr1.equals(result) && expr.equals(result1)) ){
            return true;
        }

        errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Only Introduction cannot be used here");
        return false;
    }

    public boolean isNotIntroductionValid(Expression e1) throws SyntaxException {

        Expression expr;
        Expression expr1;

        try {
            int ref1 = e1.getReferenceLine().get(0) - 1;
            expr = expressions.get(ref1);

            int ref2 = e1.getReferenceLine().get(1) - 1;
            expr1 = expressions.get(ref2);

            Boolean x = checkReferencesInScope2(e1, ref1, ref2);
            if (x != null) return x;


        } catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Two valid lines must be referenced " +
                    "to use this rule");
            return false;
        }

        if (expr.equalExceptFirst(e1) && expr.getRuleType() == RuleType.ASSUMPTION && expr1.toString().equals("FALSE")) {
            return true;
        }


        errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Not Introduction cannot be used here");
        return false;
    }

    public boolean isOrEliminationValid(Expression e1) throws SyntaxException {

        Expression expr1;
        Expression expr2;
        Expression expr3;
        Expression expr4;
        Expression expr5;

        try {
            int ref1 = e1.getReferenceLine().get(0) - 1;
            expr1 = expressions.get(ref1);

            int ref2 = e1.getReferenceLine().get(1) - 1;
            expr2 = expressions.get(ref2);

            int ref3 = e1.getReferenceLine().get(2) - 1;
            expr3 = expressions.get(ref3);

            int ref4 = e1.getReferenceLine().get(3) - 1;
            expr4 = expressions.get(ref4);

            int ref5 = e1.getReferenceLine().get(4) - 1;
            expr5 = expressions.get(ref5);

            Boolean x = checkReferencesScope5(e1, ref1, ref2, ref3, ref4, ref5);
            if (x != null) return x;

        } catch(IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Five valid lines must " +
                    "be referenced to use this rule");
            return false;
        }

            if (expr1.contains(new Operator("OR", OperatorType.OR))) {
                List<Expression> sides = expr1.splitExpressionBy(OperatorType.OR);

                Expression lhs = sides.get(0);
                Expression rhs = sides.get(1);

                if (lhs.equals(expr2) && rhs.equals(expr4) && expr3.equals(expr5) && expr3.equals(e1)) {
                    return true;
                }
            } else {
                errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: There is no OR operator in the" +
                        " expression you are using for OR Elimination");
                return false;
            }

        errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: Or Elimination cannot be used here");
        return false;
    }

    private Boolean checkReferencesScope5(Expression e1, int ref1, int ref2, int ref3, int ref4, int ref5) {
        List<List<Integer>> boxes = findBoxIndexes();

        int index = findExpressionIndex(e1);

        for (List<Integer> box : boxes) {
            if (index <= box.get(1) && index >= box.get(0)) {
                break;
            }
            if ((ref1 > box.get(0) && ref1 < box.get(1)) && expressions.get(ref1).getRuleType() != RuleType.ASSUMPTION
                    || (ref2 > box.get(0) && ref2 < box.get(1) &&
                    expressions.get(ref2).getRuleType() != RuleType.ASSUMPTION)
                    || (ref3 > box.get(0) && ref3 < box.get(1) &&
                    expressions.get(ref3).getRuleType() != RuleType.ASSUMPTION)
                    || (ref4 > box.get(0) && ref4 < box.get(1) &&
                    expressions.get(ref4).getRuleType() != RuleType.ASSUMPTION)
                    || (ref5 > box.get(0) && ref5 < box.get(1) &&
                    expressions.get(ref5).getRuleType() != RuleType.ASSUMPTION)) {
                errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: You cannot reference a line " +
                        "that is inside a completed box");
                return false;
            }
        }
        return null;
    }

    private void removeErrorDuplicates() {
        Set<String> hs = new HashSet<>();
        hs.addAll(errors);
        errors.clear();
        errors.addAll(hs);
    }

    public String generateHint() throws SyntaxException {

        if (resultExpr.toString().equals("")) {
            return "";
        }
        solvedProof = solveProof();

        if (isProofValid()) {



            if (solvedProof != null) {

                for (int i = 0; i < expressions.size(); i++) {
                    if (expressions.get(i).getRuleType() == RuleType.EMPTY || expressions.get(i).getRuleType() == null) {

                        return "Hint: " + ruleFormatting(solvedProof.get(i).getRuleType().toString());
                    }
                    if (expressions.get(i).toString().equals("")) {
                        int index;
                        if (expressions.size() == solvedProof.size()) {
                            index = i + 1;
                        } else {
                            index = i;
                        }

                        return "Hint: " + ruleFormatting(solvedProof.get(index).getRuleType().toString());
                    }


                }

                if (solvedProof.size() <= expressions.size()) {
                    return "Proof already successfully solved";
                }

                if (expressions.isEmpty()) {
                    return "Hint: " + ruleFormatting(solvedProof.get(0).getRuleType().toString());
                }


                if (solvedProof.get(expressions.size() - 1).equals(expressions.get(expressions.size() - 1))) {
                    return "Hint: " + ruleFormatting(solvedProof.get(expressions.size()).getRuleType().toString());
                } else {
                    return "Hint: Go back a step, you are going in the wrong direction";
                }
            } else {
                return "Cannot generate hint for this proof";
            }
        } else {

            removeErrorDuplicates();
            return printErrors();
        }
    }

    private boolean endAssumes(RuleType type) {
        return type == RuleType.IMPLIES_INTRO || type == RuleType.NOT_INTRO || type == RuleType.OR_ELIM;
    }

    private void unmarkExpressions() {
        for (Expression expr : list_proof) {
            expr.setMarked(false);
        }

        for (Expression expr : expressions) {
            expr.setMarked(false);
        }
    }

    private boolean elimType(RuleType type) {
        return type == RuleType.AND_ELIM || type == RuleType.ONLY_ELIM || type == RuleType.IMPLIES_ELIM ||
                type == RuleType.NOT_ELIM || type == RuleType.DOUBLE_NOT_ELIM;
    }


    public List<Expression> solveProof() throws SyntaxException {
        list_proof.clear();
        list_goals.clear();

        inscope = new LinkedList<>();
        inscope.clear();

        firstOrRound = true;
        unmarkExpressions();


        list_goals.add(resultExpr);


        if (!expressions.isEmpty() && !expressions.get(0).toString().equals("")
                && expressions.get(0).getRuleType() == RuleType.GIVEN) {
            for (int i = 0; i < expressions.size(); i++) {

                if (expressions.get(i).getRuleType() != RuleType.ASSUMPTION
                        && !expressions.get(i).toString().equals("")) {
                    list_proof.add(expressions.get(i));
                } else {
                    for (int j = expressions.size()-1; j > i; j--) {

                        if (expressions.get(j).toString().equals("")) {
                            break;
                        }

                        if (!expressions.get(j).toString().equals("") && (endAssumes(expressions.get(j).getRuleType())
                                || !expressions.get(j).isMarked())) {

                            if (elimType(expressions.get(j).getRuleType())) {
                                break;
                            }

                            if (expressions.get(j).getRuleType() != RuleType.ASSUMPTION ) {

                                list_goals.add(  expressions.get(j));

                                expressions.get(j).setMarked(true);
                            }
                            if (expressions.get(i+1).getRuleType() == RuleType.ASSUMPTION) {
                                continue;
                            } else {
                                break;
                            }
                        }

                    }
                    break;

                }


            }
        }

        for (Expression e : list_proof) {

            if (e.getRuleType() == RuleType.NOT_INTRO || e.getRuleType() == RuleType.IMPLIES_INTRO) {
                for (int j = inscope.size() - 1; j > -1; j--) {
                    if (inscope.get(j).getRuleType() == RuleType.ASSUMPTION && !inscope.get(j).isMarked()) {
                        inscope.get(j).setMarked(true);
                        break;
                    }
                    inscope.remove(j);
                }
            }

            inscope.add(e);
        }

        Expression current_goal = new Expression();
        current_goal.addToExpression(list_goals.get(0).toString());

        boolean specialCase = false;

        if (resultExpr.contains(new Operator("IMPLIES", IMPLIES)) ||
                resultExpr.getFirstComp().equals(new Operator("NOT", NOT))) {

            specialCase = true;
            for (Expression e : list_proof) {
                if (e.contains(new Operator("ONLY", ONLY))) {
                    specialCase = false;
                }
            }

        }




        unmarkExpressions();
        terminate = false;
        assumptions.clear();
        introductions.clear();

        int timeOutCount = 0;
        while (!terminate) {
            timeOutCount++;

            if (timeOutCount > 20) {
                break;
            }

            if (current_goal.contains(new Operator("IMPLIES", IMPLIES))
                    || (resultExpr.contains(new Operator("IMPLIES", IMPLIES)) && timeOutCount < 1)
                    && !current_goal.equals(resultExpr)) {

                specialCase = true;
                for (Expression e : list_proof) {
                    if (e.contains(new Operator("ONLY", ONLY))) {
                        specialCase = false;
                    }
                }

            }

            if (checkIfExpressionsReached(current_goal)) {

                if (orsLeft) {
                    applyIntroductionRule(current_goal);
                } else {
                    if (list_goals.size() > 1) {
                        list_goals.remove(list_goals.size()-1);
                        current_goal = list_goals.get(list_goals.size() - 1);
                        applyIntroductionRule(current_goal);
                        current_goal = list_goals.get(list_goals.size() - 1);
                    }
                }

                if (current_goal.equals(list_goals.get(0)) && list_proof.get(list_proof.size()-1).equals(current_goal)
                        && list_goals.size() == 1 && !orsLeft) {
                    return list_proof;
                }
                continue;
            } else {
                if (!specialCase) {
                    if (eliminationRuleApplicable()) {

                        specialCase = false;

                        continue;
                    }
                }

                specialCase = false;

                    //procedure 2
                if (!current_goal.toString().equals("FALSE")) {
                        //procedure 2.1

                    setupIntroductionRules(current_goal);

                    current_goal = list_goals.get(list_goals.size() - 1);

                    continue;
                } else {
                    //procedure 2.2
                    falseRulesSetup();

                    current_goal = list_goals.get(list_goals.size() - 1);

                    continue;
                }
            }

        }
        return null;
    }

    private void printRuleType() {
        for (Expression expr : list_proof) {
            System.out.print(expr.getRuleType() + ", ");
        }
        System.out.println();
    }

    private void falseRulesSetup() throws SyntaxException {
        for (Expression expr : list_proof) {
            if (expr.getFirstComp().equals(new Operator("NOT", NOT))) {
                Expression temp = new Expression();
                temp.addToExpression(expr.toString());
                temp.removeNcomponents(1);

                if (temp.getFirstComp().toString().equals("OPEN")
                        && temp.getLastComp().toString().equals("CLOSE")) {
                    temp.removeNcomponents(1);
                    temp.removeLast();

                }

                if (!list_goals.contains(temp)) {
                    list_goals.add(temp);
                } else {
                    if (!resultExpr.equals(temp)) {
                        list_goals.remove(temp);
                    }
                    list_goals.add(temp);
                }
            }
        }
    }

    private void setupIntroductionRules(Expression expr) throws SyntaxException {

        if (expr.contains(new Operator("IMPLIES", IMPLIES))) {
            Expression e = new Expression(expr.getRuleType());
            e.addToExpression(expr.toString());
            List<Expression> sides = e.splitExpressionBy(OperatorType.IMPLIES);



            Expression lhs = sides.get(0);
            lhs.setRuleType(RuleType.ASSUMPTION);
            Expression rhs = sides.get(1);

            if (!assumptions.contains(lhs)) {

                assumptions.add(lhs);

                if (checkBracketValidity(lhs) && checkBracketValidity(rhs)) {
                    if (!list_goals.contains(rhs)) {
                        list_goals.add(rhs);
                    }
                    list_proof.add(lhs);

                    inscope.add(lhs);
                    return;
                }
            }
        }

        if (expr.getFirstComp().toString().equals("NOT")) {

            Expression temp = new Expression(RuleType.ASSUMPTION);
            temp.addToExpression(expr.toString());
            temp.removeNcomponents(1);

            if (checkBracketValidity(temp)) {
                    if (!assumptions.contains(temp)) {

                        assumptions.add(temp);
                        list_proof.add(temp);
                        inscope.add(temp);
                        Expression subGoal = new Expression();
                        subGoal.addToExpression("FALSE");
                        list_goals.add(subGoal);
                        return;
                    }
            }
        }

        if (expr.contains(new Operator("AND", AND))) {
            List<Expression> sides = expr.splitExpressionBy(OperatorType.AND);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            if (checkBracketValidity(lhs) && checkBracketValidity(rhs)) {
                list_goals.add(rhs);
                list_goals.add(lhs);
                return;
            }

        }




        boolean skip = true;
        if (firstOrRound) {
            for (Expression expr1 : list_proof) {
                if (expr1.getRuleType() == RuleType.GIVEN || resultExpr.contains(new Operator("IMPLIES", IMPLIES))) {
                    skip = false;
                }
            }
            firstOrRound = false;
        } else {
            skip = false;
        }

        if (expr.contains(new Operator("OR", OR)) && !skip) {

            List<Expression> sides = expr.splitExpressionBy(OperatorType.OR);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            if (checkBracketValidity(lhs) && checkBracketValidity(rhs)) {

                int count1 = 0;
                int count2 = 0;
                for (Expression e : list_proof) {
                    for (Proposition p : e.listPropositions()) {
                        if (lhs.listPropositions().toString().toLowerCase().contains(p.toString().toLowerCase())) {
                            count1++;
                        }

                        if (rhs.listPropositions().toString().toLowerCase().contains(p.toString().toLowerCase())) {
                            count2++;
                        }
                    }
                }

                if (count1 > count2) {
                    firstOrRound = true;
                } else {
                    firstOrRound = false;
                }


                if (list_proof.contains(lhs) || firstOrRound) {
                    firstOrRound = false;
                    list_goals.add(lhs);
                    return;
                }
                if (list_proof.contains(rhs) || !firstOrRound) {
                    list_goals.add(rhs);
                    return;
                }
            }

        }

        if (expr.contains(new Operator("ONLY", ONLY))) {
            List<Expression> sides = expr.splitExpressionBy(OperatorType.ONLY);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            if (checkBracketValidity(lhs) && checkBracketValidity(rhs)) {
                Expression result = new Expression();
                result.addToExpression(lhs + " -> " + rhs);
                Expression result1 = new Expression();
                result1.addToExpression(rhs + " -> " + lhs);

                list_goals.add(result);
                list_goals.add(result1);
                return;
            }

        }



        for (Expression e : list_proof) {
            if (e.contains(new Operator("OR", OR)) && !e.isMarked()) {
                extra_list_proof.clear();
                temp1 = new LinkedList<>();
                orElimStack.clear();
                lhsStack.clear();
                rhsStack.clear();

                List<Expression> sides = e.splitExpressionBy(OperatorType.OR);
                sides.get(0).setRuleType(RuleType.ASSUMPTION);
                sides.get(1).setRuleType(RuleType.ASSUMPTION);
                lhsStack.push(sides.get(0));
                rhsStack.push(sides.get(1));


                if (checkBracketValidity(sides.get(0)) && checkBracketValidity(sides.get(1))) {
                    rhsIndex = list_proof.size();

                    extra_list_proof.addAll(list_proof);

                    orsLeft = true;
                    list_proof.add(lhsStack.pop());
                    inscope.add(list_proof.get(list_proof.size()-1));

                    list_goals.add(expr);
                    orElimStack.push(expr);

                    return;
                }
            }
        }

        //Negate the result round
        Expression newExpr = new Expression(RuleType.ASSUMPTION);

        newExpr.addToExpression("!(" + expr.toString() + ")");
        if (!assumptions.contains(newExpr)) {
            assumptions.add(newExpr);
            list_proof.add(newExpr);
            inscope.add(newExpr);

            Expression introExpr = new Expression();
            introExpr.addToExpression("!!(" + expr.toString() + ")");
            list_goals.add(introExpr);

            Expression falseExpression = new Expression(RuleType.DOUBLE_NOT_ELIM);
            falseExpression.addToExpression("FALSE");
            list_goals.add(falseExpression);




        } else {
            terminate = true;
        }


    }



    private void applyIntroductionRule(Expression expr) throws SyntaxException {

        if (!orElimStack.isEmpty()) {
            if (list_goals.get(list_goals.size() - 1).equals(orElimStack.peek())) {
                if (lhsStack.size() < rhsStack.size()) {

                    temp1.addAll(list_proof);
                    list_proof = extra_list_proof;
                    list_proof.add(rhsStack.pop());
                    for (int j = inscope.size()-1; j > -1; j--) {
                        if (inscope.get(j).getRuleType() == RuleType.ASSUMPTION && !inscope.get(j).isMarked()) {
                            inscope.get(j).setMarked(true);
                            break;
                        }
                        inscope.remove(j);
                    }
                    inscope.add(list_proof.get(list_proof.size()-1));
                } else {

                    for (int i = rhsIndex; i < list_proof.size(); i++) {
                        temp1.add(list_proof.get(i));
                    }

                    Expression expr1 = list_goals.get(list_goals.size() - 1);
                    expr1.setRuleType(RuleType.OR_ELIM);
                    temp1.add(expr1);
                    inscope.add(expr1);
                    list_proof = temp1;
                    orsLeft = false;
                    orElimStack.pop();
                    return;
                }
            }
        }



        if (list_goals.get(list_goals.size()-1).contains(new Operator("IMPLIES", IMPLIES))) {
            Expression e = new Expression(expr.getRuleType());
            e.addToExpression(expr.toString());
            List<Expression> sides = e.splitExpressionBy(OperatorType.IMPLIES);

            Expression rhs = new Expression(sides.get(1).getRuleType());
            rhs.addToExpression(sides.get(1).toString());
            if (list_proof.contains(rhs) && checkBracketValidity(rhs)) {

                introductions.put(RuleType.IMPLIES_INTRO, list_goals.get(list_goals.size()-1));

                Expression e1 = new Expression(RuleType.IMPLIES_INTRO);
                e1.addToExpression(list_goals.get(list_goals.size()-1).toString());
                list_goals.remove(list_goals.size()-1);
                list_goals.add(e1);
                if (!list_proof.get(list_proof.size()-1).equals(list_goals.get(list_goals.size()-1)) &&
                        !inscope.contains(list_goals.get(list_goals.size() - 1))) {
                    list_proof.add(list_goals.get(list_goals.size() - 1));


                    inscope.add(list_goals.get(list_goals.size()-1));
                    for (int j = inscope.size()-2; j > -1; j--) {
                        if (inscope.get(j).getRuleType() == RuleType.ASSUMPTION && !inscope.get(j).isMarked()) {
                            inscope.get(j).setMarked(true);
                            break;
                        }
                        inscope.remove(j);
                    }

                }
                return;
            }
        }

        if (list_goals.get(list_goals.size()-1).contains(new Operator("AND", AND))) {
            List<Expression> sides = list_goals.get(list_goals.size()-1).splitExpressionBy(OperatorType.AND);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            if (list_proof.contains(lhs) && list_proof.contains(rhs) && checkBracketValidity(lhs)
                    && checkBracketValidity(rhs)) {

                if (inscope.contains(lhs) || inscope.contains(rhs)) {
                    introductions.put(RuleType.AND_INTRO, list_goals.get(list_goals.size() - 1));

                    Expression e1 = new Expression(RuleType.AND_INTRO);
                    e1.addToExpression(list_goals.get(list_goals.size() - 1).toString());
                    list_goals.remove(list_goals.size() - 1);
                    list_goals.add(e1);
                    list_proof.add(list_goals.get(list_goals.size() - 1));
                    inscope.add(list_goals.get(list_goals.size() - 1));
                }

                return;
            }
        }



        if (list_goals.get(list_goals.size()-1).getFirstComp().toString().equals("NOT")) {
            if (list_proof.get(list_proof.size()-1).toString().equals("FALSE")
                    && checkBracketValidity(list_proof.get(list_proof.size()-1))) {

                introductions.put(RuleType.NOT_INTRO, list_goals.get(list_goals.size() -1));

                list_goals.get(list_goals.size()-1).setRuleType(RuleType.NOT_INTRO);
                list_proof.add(list_goals.get(list_goals.size()-1));

                inscope.add(list_goals.get(list_goals.size()-1));
                for (int j = inscope.size()-2; j > -1; j--) {
                    if (inscope.get(j).getRuleType() == RuleType.ASSUMPTION && !inscope.get(j).isMarked()) {
                        inscope.get(j).setMarked(true);
                        break;
                    }
                    inscope.remove(j);
                }

                return;
            }
        }

        if (list_goals.get(list_goals.size()-1).contains(new Operator("OR", OR))) {
            List<Expression> sides = list_goals.get(list_goals.size()-1).splitExpressionBy(OperatorType.OR);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);



            if (!list_proof.get(list_proof.size()-1).equals(list_goals.get(list_goals.size()-1))) {
                if ((list_proof.contains(lhs) || list_proof.contains(rhs)) && checkBracketValidity(lhs)
                        && checkBracketValidity(rhs)) {

                    if (inscope.contains(lhs) || inscope.contains(rhs)) {


                        introductions.put(RuleType.OR_INTRO, list_goals.get(list_goals.size() - 1));

                        list_goals.get(list_goals.size() - 1).setRuleType(RuleType.OR_INTRO);
                        list_proof.add(list_goals.get(list_goals.size() - 1));
                        inscope.add(list_goals.get(list_goals.size()-1));

                        return;
                    }
                }
            }
        }

        if (list_goals.get(list_goals.size()-1).contains(new Operator("ONLY", ONLY))) {
            List<Expression> sides = list_goals.get(list_goals.size()-1).splitExpressionBy(OperatorType.ONLY);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            if (checkBracketValidity(lhs) && checkBracketValidity(rhs)) {
                Expression result = new Expression();
                result.addToExpression(lhs + " -> " + rhs);
                Expression result1 = new Expression();
                result1.addToExpression(rhs + " -> " + lhs);

                if (list_proof.contains(result) && list_proof.contains(result1)) {
                    if (!list_proof.get(list_proof.size()-1).equals(list_goals.get(list_goals.size()-1))) {
                        introductions.put(RuleType.ONLY_INTRO, list_goals.get(list_goals.size() - 1));

                        list_goals.get(list_goals.size() - 1).setRuleType(RuleType.ONLY_INTRO);
                        list_proof.add(list_goals.get(list_goals.size() - 1));
                    }

                    return;
                }
            }
        }
    }

    private boolean checkBracketValidity(Expression expr) {
        return ((expr.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                expr.contains(new Operator("CLOSE", CLOSE_BRACKET))) ||
                (!expr.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                        !expr.contains(new Operator("CLOSE", CLOSE_BRACKET))));
    }

    private boolean allMarked() {
        return false;
    }

    private void applyEliminationRule(Expression expr) {
            list_proof.add(expr);
            inscope.add(expr);
    }

    private boolean eliminationRuleApplicable() throws SyntaxException {

        Proof newProof = new Proof();
        newProof.expressions = list_proof;

        boolean implies = tryImpliesElim(newProof);
        newProof.expressions = list_proof;

        boolean and = tryAndElim(newProof);
        newProof.expressions = list_proof;

        boolean only = tryOnlyElim(newProof);
        newProof.expressions = list_proof;


        boolean notNot = tryDoubleNotElim(newProof);
        newProof.expressions = list_proof;

        boolean not = false;
        if (!notNot) {
            not = tryNotElim(newProof);
            newProof.expressions = list_proof;
        }





        return and || implies || only || not || notNot ;
    }




    private boolean tryNotElim(Proof newProof) throws SyntaxException {
        int count = 1;
        boolean changed = false;

        for (int i = 0; i < newProof.expressions.size(); i++) {
            if (newProof.expressions.get(i).toString().equals("")) {
                continue;
            }
            if (newProof.expressions.get(i).getFirstComp().equals(new Operator("NOT", NOT))) {
                Expression notResult = new Expression();
                notResult.addToExpression("FALSE");

                Expression temp = new Expression();
                temp.addToExpression(newProof.expressions.get(i).toString());
                temp.removeNcomponents(1);

                notResult.addReferenceLine(Integer.toString(count));

                int count1 = 1;
                for (int j = 0; j < newProof.expressions.size(); j++) {
                    if (newProof.expressions.get(j).equals(temp)) {
                        notResult.addReferenceLine(Integer.toString(count1));
                        if (newProof.isNotElimValid(notResult)) {
                            notResult.setRuleType(RuleType.NOT_ELIM);
                            if (!list_proof.get(list_proof.size()-1).toString().equals("FALSE")) {

                                applyEliminationRule(notResult);
                                changed = true;
                            }
                        }
                    }
                    count1++;
                }
            }
            count++;

        }
        return changed;
    }

    private boolean tryDoubleNotElim(Proof newProof) throws SyntaxException {
        int count = 1;
        boolean changed = false;




        for (int i = 0; i < newProof.expressions.size(); i++) {
            if (newProof.expressions.get(i).toString().equals("")) {
                continue;
            }
            if (newProof.expressions.get(i).getFirstComp().equals(new Operator("NOT", NOT))) {
                Expression temp = new Expression();
                temp.addToExpression(newProof.expressions.get(i).toString());
                temp.removeNcomponents(1);
                if (temp.getFirstComp().toString().equals("NOT")) {
                    Expression temp1 = new Expression();
                    temp1.addToExpression(temp.toString());
                    temp1.removeNcomponents(1);
                    temp1.addReferenceLine(Integer.toString(count));
                    if (newProof.isDoubleNotElimValid(temp1)) {
                        if (inscope.contains(temp1)) {
                            break;
                        }
                        temp1.setRuleType(RuleType.DOUBLE_NOT_ELIM);
//                        if (!list_proof.contains(temp1)) {
                            applyEliminationRule(temp1);
                            changed = true;
//                        }
                    }

                }
            }
            count++;

        }



        return changed;
    }

    private boolean doneFirst = false;
    private boolean tryOnlyElim(Proof newProof) throws SyntaxException {
        int count = 1;
        boolean changed = false;

        for (int i = 0; i < newProof.expressions.size(); i++) {
            if (newProof.expressions.get(i).contains(new Operator("ONLY", ONLY))) {
                List<Expression> sides = newProof.expressions.get(i).splitExpressionBy(OperatorType.ONLY);

                Expression lhs = sides.get(0);
                Expression rhs = sides.get(1);

                Expression result1 = new Expression();
                result1.addToExpression(lhs.toString() + " -> " + rhs.toString());
                result1.addReferenceLine(Integer.toString(count));

                Expression result2 = new Expression();
                result2.addToExpression(rhs.toString() + " -> " + lhs.toString());
                result2.addReferenceLine(Integer.toString(count));


                if (list_goals.get(list_goals.size()-1).equals(result2)) {
                    doneFirst = true;
                }

                if (newProof.isOnlyEliminationValid(result1) && !doneFirst) {
                    result1.setRuleType(RuleType.ONLY_ELIM);
                    if (!list_proof.contains(result1)) {
                        applyEliminationRule(result1);
                        changed = true;
                        doneFirst = true;
                        break;
                    }
                }

                if (newProof.isOnlyEliminationValid(result2) && doneFirst) {
                    result2.setRuleType(RuleType.ONLY_ELIM);
                    if (!list_proof.contains(result2)) {
                        applyEliminationRule(result2);
                        changed = true;
                        doneFirst = false;
                    }
                }
            }
            count++;

        }
        return changed;
    }

    private boolean tryImpliesElim(Proof newProof) throws SyntaxException {
        int count = 1;
        boolean changed = false;

        for (int i = 0; i < newProof.expressions.size(); i++) {
            if (newProof.expressions.get(i).contains(new Operator("IMPLIES", IMPLIES))) {
                List<Expression> sides = newProof.expressions.get(i).splitExpressionBy(OperatorType.IMPLIES);

                Expression lhs = sides.get(0);
                lhs.addReferenceLine(Integer.toString(count));
                Expression rhs = sides.get(1);
                rhs.addReferenceLine(Integer.toString(count));

                int count1 = 1;
                for (int j = 0; j < newProof.expressions.size(); j++) {
                    if (newProof.expressions.get(j).equals(lhs)) {
                        rhs.addReferenceLine(Integer.toString(count1));

                        if (newProof.isImpliesElimValid(rhs) &&
                                (rhs.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                                        rhs.contains(new Operator("CLOSE",CLOSE_BRACKET))) ||
                                (!rhs.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                                        !rhs.contains(new Operator("CLOSE",CLOSE_BRACKET)))) {
                            rhs.setRuleType(RuleType.IMPLIES_ELIM);
                            if (!newProof.expressions.contains(rhs)) {
                                applyEliminationRule(rhs);
                                changed = true;
                            }
                        }
                    }
                    count1++;
                }
            }
            count++;
        }
        return changed;
    }

    private boolean doneLeft = false;
    private boolean tryAndElim(Proof newProof) throws SyntaxException {
        boolean changed = false;
        int count = 1;
        for (int i = 0; i < newProof.expressions.size(); i++) {
            if (newProof.expressions.get(i).contains(new Operator("AND", AND))) {


                Expression e = new Expression(newProof.expressions.get(i).getRuleType());
                e.addToExpression(newProof.expressions.get(i).toString());
                List<Expression> sides = e.splitExpressionBy(OperatorType.AND);

                Expression lhs = sides.get(0);
                lhs.addReferenceLine(Integer.toString(count));
                Expression rhs = sides.get(1);
                rhs.addReferenceLine(Integer.toString(count));


                if (!resultExpr.listPropositions().toString().toLowerCase()
                        .contains(lhs.toString().toLowerCase())
                        && !lhs.listPropositions().toString().toLowerCase()
                        .contains(resultExpr.toString().toLowerCase()) && !resultExpr.toString().equals("FALSE")) {
                    doneLeft = true;
                } else {
                    doneLeft = false;
                }




                if (newProof.isAndElimValid(lhs) &&
                        (lhs.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                                lhs.contains(new Operator("CLOSE",CLOSE_BRACKET))) ||
                        (!lhs.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                                !lhs.contains(new Operator("CLOSE",CLOSE_BRACKET))) && !doneLeft) {
                    Expression result = new Expression(RuleType.AND_ELIM);
                    result.addToExpression(lhs.toString());
                    if (!newProof.expressions.contains(result)) {
                        applyEliminationRule(result);
                        changed = true;
                        doneLeft = true;
                        break;
                    }
                    doneLeft =true;
                }

                if (newProof.isAndElimValid(rhs) &&
                        (rhs.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                                rhs.contains(new Operator("CLOSE",CLOSE_BRACKET))) ||
                        (!rhs.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                                !rhs.contains(new Operator("CLOSE",CLOSE_BRACKET))) && doneLeft) {

                    Expression result = new Expression(RuleType.AND_ELIM);
                    result.addToExpression(rhs.toString());
                    if (!newProof.expressions.contains(result)) {

                        applyEliminationRule(result);


                        changed = true;
                        doneLeft =  false;
                    }

                }
            }
            count++;
        }



        return changed;
    }



    private boolean checkIfExpressionsReached(Expression current) throws SyntaxException {

        if (list_proof.isEmpty()) {
            return false;
        }

        return inscope.contains(current);
    }


    public String getProofString() {
        return proofString;
    }

    public void setProofString(String proofString) {
        this.proofString = proofString;
    }

    public String getProofLabels() {
        return proofLabels;
    }

    public void setProofLabels(String proofLabels) {
        this.proofLabels = proofLabels;
    }

    public Expression getResultExpr() {
        return resultExpr;
    }

    public void setResultExpr(Expression resultExpr) {
        this.resultExpr = resultExpr;
    }

    public String printTest() {
        return "Hello World";
    }

}
