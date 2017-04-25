package model;

import javassist.expr.Expr;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joshuazeltser on 18/01/2017.
 */
public class Proof {

    private List<Expression> expressions;

    private List<String> errors;

    private String proofString;
    private String proofLabels;

    private List<Proof> proofSteps;

    private Expression resultExpr;

    public Proof() {
        expressions = new LinkedList<>();
        errors = new LinkedList<>();
        proofString = "";
        proofLabels = "";
        proofSteps = new LinkedList<>();
    }



    public String frontEndFunctionality(String proof, String rule) throws SyntaxException{
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

        if (!proof.equals("") && !rule.equals("")) {
            String[] expr = proof.split("\\r?\\n");
            String[] exprRule = rule.split("\\r?\\n");

            //check rules are all present
            if (expr.length != exprRule.length) {
                errors.add("ERROR: A rule is missing on a line of the proof");
                return;
            }


            for (int i = 0; i < expr.length; i++) {
                // split rules by space
                String[] components = exprRule[i].split(" ");
                Expression newExpr = new Expression(convertStringToRule(components[0]));
                try {
                    newExpr.addToExpression(expr[i]);

                } catch (SyntaxException s) {
                    errors = new LinkedList<>();
                    errors.add("LINE " + (i+1) + " - " + s.getMessage());
                    //if there is a syntax error don't display other error messages
                   return;
                }

                if (!components[0].equals("GIVEN") && !components[0].equals("ASSUMPTION")) {
                    String comps = removeBracketsFromString(components[1]);
                    String[] lines = comps.split(",");
                    for (int j = 0; j < lines.length; j++) {
                        newExpr.addReferenceLine(lines[j]);
                    }
                }
                addExpression(newExpr);
            }


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
            case "DoubleNot-Elim": return RuleType.DOUBLE_NOT_ELIMINATION;
            default: return RuleType.INVALID;
        }
    }

    public boolean isProofValid() throws SyntaxException {

        for (int i = expressions.size()-1; i >= 0; i--) {
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
                case DOUBLE_NOT_ELIMINATION: isDoubleNotElimValid(expressions.get(i)); break;
                case INVALID: return false;
                default: break;
            }
            expressions.remove(i);
        }
        return errors.isEmpty();
    }

    public String frontEndProofValidity() throws SyntaxException {
        if (proofString.equals("") || proofLabels.equals("")) {
            return "";
        } else if (isProofValid()) {
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

    public boolean isAndElimValid(Expression e1) {

        int ref1;
        try {
            ref1 = e1.getReferenceLine().get(0) - 1;

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


    public boolean isDoubleNotElimValid(Expression e1) {

        int ref1;

        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
        } catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: A valid line must be referenced to use this rule");
            return false;
        }

        Expression expr = expressions.get(ref1);
        if (expr.contains(new Operator("NOT", OperatorType.NOT))) {
            expr.removeNcomponents(1);
            if (expr.contains(new Operator("NOT", OperatorType.NOT))) {
                expr.removeNcomponents(1);
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

        if (expr.equals(e1)) {
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


    public List<Proof> getProofSteps() {
        return proofSteps;
    }

    public void setProofSteps(List<Proof> proofSteps) {
        this.proofSteps = proofSteps;
    }

    // functionality to solve a proof step by step in order to suggest hints

    private Proof foundResult(List<Proof> possProofs) {
        for (Proof p : possProofs) {
            if (p.expressions.get(p.expressions.size()-1).equals(resultExpr)) {
                return p;
            }
        }
        //remember to set this up in front end
        return null;
    }

    private int timeoutCount = 0;






    public Proof nextStep() throws SyntaxException {

        Proof possResult = null;

        boolean box = false;

        Expression lhsImplies = new Expression();
        Expression rhsImplies = new Expression();

        while (possResult == null) {



            if (box) {
                for (Proof p : proofSteps) {
                    if (p.expressions.get(p.expressions.size()-1).equals(rhsImplies)) {
                        box = false;
                        Expression e = new Expression(RuleType.IMPLIES_INTRO);
                        e.addToExpression(lhsImplies + " -> " + rhsImplies);
                        p.addExpression(e);
                    }
                }
                possResult = foundResult(proofSteps);
                if (possResult != null) {
                    break;
                }

            }

            //if result expression includes a ->
            if (resultExpr.contains(new Operator("IMPLIES", OperatorType.IMPLIES)) && !box) {
                possResult = tryOnlyElimination();
                if (possResult != null) {
                    break;
                }
                
                box = true;
                List<Expression> sides = resultExpr.splitExpressionBy(OperatorType.IMPLIES);

                lhsImplies = sides.get(0);
                rhsImplies = sides.get(1);

                for (Proof p : proofSteps) {
                    p.addExpression(lhsImplies);
                }
            }

            //check for simple solution
            if (resultExpr.contains(new Operator("OR", OperatorType.OR))) {
                possResult = tryOrIntroduction();
                if (possResult != null) {
                    break;
                }
            }

            //check if and elimination is possible
            possResult = tryAndEliminationStep();

            if (possResult != null) {
                break;
            }

            //check if implies elimination is possible
            possResult = tryImpliesElimination();
            if (possResult != null) {
                break;
            }

            //check if not elimination is possible
            possResult = tryNotElimination();
            if (possResult != null) {
                break;
            }

            //check if Double not elimination is possible
            possResult = tryDoubleNotElimination();
            if (possResult != null) {
                break;
            }

            //check if iff elimination is possible
            possResult = tryOnlyElimination();
            if (possResult != null) {
                break;
            }

            //check for several eliminations before starting introductions
            if (timeoutCount < 2) {
                timeoutCount++;
                continue;
            }

            if (resultExpr.contains(new Operator("AND", OperatorType.AND))) {//temp fix

                //check if and introduction is possible
                possResult = tryAndIntroduction();
                if (possResult != null) {
                    break;
                }
                possResult = tryOrIntroduction();
                if (possResult != null) {
                    break;
                }

            } else  if (resultExpr.contains(new Operator("OR", OperatorType.OR))){

                possResult = tryOrIntroduction();
                if (possResult != null) {
                    break;
                }
                possResult = tryAndIntroduction();
                if (possResult != null) {
                    break;
                }

            }

            if (timeoutCount < 5) {
                timeoutCount++;
            } else {
                break;
            }
        }
        return possResult;
    }

    private Proof tryOrIntroduction() throws SyntaxException {
        List<List<String>> toBeOrIntro = new LinkedList<>();

        for (int i = 0; i < proofSteps.size(); i++) {
            List<String> props = new LinkedList<>();
            for (Expression e : proofSteps.get(i).expressions) {
                for (Proposition p : e.listPropositions()) {
                    if (!props.contains(p.toString())) {
                        props.add(p.toString());
                    }
                }
                for (Proposition p : resultExpr.listPropositions()) {
                    if (!props.contains(p.toString())) {
                        props.add(p.toString());
                    }
                }

                if (!props.contains(e.toString())) {
                    props.add(e.toString());
                }

                if (!props.contains(resultExpr.toString()))
                    props.add(resultExpr.toString());

            }
            for (Expression p : proofSteps.get(i).expressions) {
                for (String p1 : props) {
                    if (!p.toString().equals(p1.toString())) {
                        Expression or = new Expression(RuleType.OR_INTRO);
                        or.addToExpression(p.toString() + " | " + p1.toString());
                        List<String> pair = new LinkedList<>();
                        pair.add(Integer.toString(i));
                        pair.add(or.toString());
                        toBeOrIntro.add(pair);
                    }
                }
            }

        }

//        System.out.println(toBeOrIntro);
        proofSteps = updateProofs(toBeOrIntro, RuleType.OR_INTRO);
        return foundResult(proofSteps);
    }

    private Proof tryAndIntroduction() throws SyntaxException {
        List<List<String>> toBeAndIntro = new LinkedList<>();

        for (int i = 0; i < proofSteps.size(); i++) {
            for (Expression e : proofSteps.get(i).expressions) {
                for (Expression e1 : proofSteps.get(i).expressions) {
                    if (!e.equals(e1)) {
                        Expression and = new Expression(RuleType.AND_INTRO);
                        and.addToExpression(e.toString() + " ^ " + e1.toString());
                        List<String> pair = new LinkedList<>();
                        pair.add(Integer.toString(i));
                        pair.add(and.toString());
                        toBeAndIntro.add(pair);
                    }
                }
            }
        }

        proofSteps = updateProofs(toBeAndIntro, RuleType.AND_INTRO);
        return foundResult(proofSteps);
    }

    private Proof tryOnlyElimination() throws SyntaxException {
        List<List<String>> toBeIffEliminated = new LinkedList<>();
        int count;
        for (int i = 0; i < proofSteps.size(); i++) {
            count = 1;
            for (Expression e : proofSteps.get(i).expressions) {
                if (e.contains(new Operator("ONLY", OperatorType.ONLY))) {
                    List<Expression> sides = e.splitExpressionBy(OperatorType.ONLY);

                    Expression lhs = sides.get(0);
                    lhs.addReferenceLine(Integer.toString(count));
                    Expression rhs = sides.get(1);
                    rhs.addReferenceLine(Integer.toString(count));

                    Expression result = new Expression();
                    Expression result1 = new Expression();

                    result.addToExpression(lhs + " -> " + rhs);
                    result1.addToExpression(rhs + " -> " + lhs);


                    result.addReferenceLine(Integer.toString(count));
                    if (proofSteps.get(i).isOnlyEliminationValid(result)) {
                        result.setRuleType(RuleType.ONLY_ELIM);
                        List<String> pair = new LinkedList<>();
                        pair.add(Integer.toString(i));
                        pair.add(result.toString());
                        toBeIffEliminated.add(pair);

                    }

                    result1.addReferenceLine(Integer.toString(count));
                    if (proofSteps.get(i).isOnlyEliminationValid(result1)) {

                        result1.setRuleType(RuleType.ONLY_ELIM);
                        List<String> pair = new LinkedList<>();
                        pair.add(Integer.toString(i));
                        pair.add(result1.toString());
                        toBeIffEliminated.add(pair);
                    }
                }
                count++;
            }
        }

        proofSteps = updateProofs(toBeIffEliminated, RuleType.ONLY_ELIM);
        return foundResult(proofSteps);
    }

    private Proof tryDoubleNotElimination() throws SyntaxException {
        List<List<String>> toBeDoubleNotEliminated = new LinkedList<>();
        int count;
        for (int i = 0; i < proofSteps.size(); i++) {
            count = 1;
            for (Expression e : proofSteps.get(i).expressions) {
                if (e.contains(new Operator("NOT", OperatorType.NOT))) {
                    Expression temp = new Expression();
                    temp.addToExpression(e.toString());
                    temp.removeNcomponents(1);

                    if (temp.contains(new Operator("NOT", OperatorType.NOT))) {
                        Expression temp1 = new Expression();
                        temp1.addToExpression(temp.toString());
                        temp1.removeNcomponents(1);
                        List<String> pair = new LinkedList<>();
                        pair.add(Integer.toString(i));
                        pair.add(temp1.toString());
                        toBeDoubleNotEliminated.add(pair);
                    }
                }
            }
            count++;
        }

        proofSteps = updateProofs(toBeDoubleNotEliminated, RuleType.DOUBLE_NOT_ELIMINATION);
        return foundResult(proofSteps);
    }

    private Proof tryNotElimination() throws SyntaxException {
        List<List<String>> toBeNotEliminated = new LinkedList<>();
        int count;
        for (int i = 0; i < proofSteps.size(); i++) {
            count = 1;
            for (Expression e : proofSteps.get(i).expressions) {
                if (e.contains(new Operator("NOT", OperatorType.NOT))) {
                    Expression temp = new Expression();
                    temp.addToExpression(e.toString());

                    temp.removeNcomponents(1);
                    int count1 = 1;
                    for (Expression e1 : proofSteps.get(i).expressions) {

                        if (e1.equals(temp)) {
                            List<String> pair = new LinkedList<>();
                            pair.add(Integer.toString(i));
                            pair.add("FALSE");
                            toBeNotEliminated.add(pair);
                        }
                    }
                }
            }
            count++;
        }

        proofSteps = updateProofs(toBeNotEliminated, RuleType.NOT_ELIM);
        return foundResult(proofSteps);
    }

    private Proof tryImpliesElimination() throws SyntaxException {
        int count;
        List<List<String>> toBeEliminated = new LinkedList<>();
        for (int i = 0; i < proofSteps.size(); i++) {
            count = 1;
            for (Expression e : proofSteps.get(i).expressions) {

                if (e.contains(new Operator("IMPLIES", OperatorType.IMPLIES))) {

                    List<Expression> sides = e.splitExpressionBy(OperatorType.IMPLIES);

                    Expression rhs = sides.get(1);
                    rhs.addReferenceLine(Integer.toString(count));

                    Expression lhs = sides.get(0);

                    int count1 = 1;
                    for (Expression expr1 : proofSteps.get(i).expressions) {

                        if (expr1.equals(lhs)) {

                            rhs.addReferenceLine(Integer.toString(count1));

                            if (proofSteps.get(i).isImpliesElimValid(rhs)) {
                                if (proofSteps.get(i).expressions.contains(rhs)) {
                                    break;
                                }
                                rhs.setRuleType(RuleType.IMPLIES_ELIM);
                                List<String> pair = new LinkedList<>();
                                pair.add(Integer.toString(i));
                                pair.add(rhs.toString());
                                toBeEliminated.add(pair);
                            }
                            break;
                        }
                        count1++;
                    }

                }
                count++;

            }


        }

        proofSteps = updateProofs(toBeEliminated, RuleType.IMPLIES_ELIM);
        return foundResult(proofSteps);
    }

    private Proof tryAndEliminationStep() throws SyntaxException {
        int count;
        List<List<String>> toBeAndEliminated = new LinkedList<>();

        for (int i = 0; i < proofSteps.size(); i++) {
            count = 1;
            for (Expression e : proofSteps.get(i).expressions) {
                if (e.contains(new Operator("AND", OperatorType.AND))) {
                    List<Expression> sides = e.splitExpressionBy(OperatorType.AND);

                    Expression lhs = sides.get(0);
                    lhs.addReferenceLine(Integer.toString(count));
                    Expression rhs = sides.get(1);
                    rhs.addReferenceLine(Integer.toString(count));

                    if (proofSteps.get(i).isAndElimValid(lhs)) {
                        if (proofSteps.get(i).expressions.contains(lhs)) {
                            break;
                        }
                        lhs.setRuleType(RuleType.AND_ELIM);
                        List<String> pair = new LinkedList<>();
                        pair.add(Integer.toString(i));
                        pair.add(lhs.toString());
                        toBeAndEliminated.add(pair);

                    }
                    if (proofSteps.get(i).isAndElimValid(rhs)) {
                        if (proofSteps.get(i).expressions.contains(rhs)) {
                            break;
                        }
                        rhs.setRuleType(RuleType.AND_ELIM);
                        List<String> pair = new LinkedList<>();
                        pair.add(Integer.toString(i));
                        pair.add(rhs.toString());
                        toBeAndEliminated.add(pair);
                    }
                }
                count++;
            }
        }


        proofSteps = updateProofs(toBeAndEliminated, RuleType.AND_ELIM);
//        System.out.println(proofSteps);
        return foundResult(proofSteps);
    }

    private List<Proof> updateProofs(List<List<String>> toBeEliminated, RuleType type) throws SyntaxException {
        List<Proof> newPossProofs = new LinkedList<>();
        if (!toBeEliminated.isEmpty()) {
            for (List<String> pair : toBeEliminated) {
                for (int i = 0; i < proofSteps.size(); i++) {

                    if (pair.get(0).equals(Integer.toString(i))) {
                        Proof temp = new Proof();
                        temp.expressions.addAll(proofSteps.get(i).expressions);
                        Expression e = new Expression(type);
                        e.addToExpression(pair.get(1));
                        temp.expressions.add(e);
                        newPossProofs.add(temp);
                    } else {
                        newPossProofs.add(proofSteps.get(i));
                    }

                }
            }
            return newPossProofs;
        }
        return proofSteps;
    }

    public Expression getResultExpr() {
        return resultExpr;
    }

    public void setResultExpr(Expression resultExpr) {
        this.resultExpr = resultExpr;
    }
}
