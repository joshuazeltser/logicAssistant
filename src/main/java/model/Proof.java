package model;


import javassist.compiler.ast.Expr;
import javassist.compiler.ast.Symbol;

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

    public Proof() {
        expressions = new LinkedList<>();
        errors = new LinkedList<>();
        proofString = "";
        proofLabels = "";
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
            result.replaceAll("\n", "\\n");
            System.out.println(result);
        }
        return result;
    }

    public void separateByNewLine(String proof, String rule) throws SyntaxException {

        if (!proof.equals("") && !rule.equals("")) {
            String[] expr = proof.split("\\r?\\n");
            String[] exprRule = rule.split("\\r?\\n");

            for (int i = 0; i < expr.length; i++) {
                String[] components = exprRule[i].split(" ");
                Expression newExpr = new Expression(convertStringToRule(components[0]));
                try {
                    System.out.println(expr[i]);
                    newExpr.addToExpression(expr[i]);

                } catch (SyntaxException s) {
                    errors.add(s.getMessage());
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

        String result = "";

        int count = 1;

        for (Expression expr : expressions) {
            result += count + " " + expr.toString() + "\n";
            count++;
        }
        return result;
    }

    public boolean isAndIntroValid(Expression e1) {

        if (!e1.contains(new Operator("AND",OperatorType.AND))) {
            errors.add("RULE ERROR: And Introduction has not been used here");
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
            errors.add("RULE ERROR: Two lines must be referenced when using this rule");
            return false;
        }

        if (!expressions.get(ref1).equals(lhs) && !expressions.get(ref1).equals(rhs)) {
            errors.add("RULE ERROR: This line cannot be used for this And Introduction");
            return false;
        }

        if (!expressions.get(ref2).equals(lhs) && !expressions.get(ref2).equals(rhs)) {
            errors.add("RULE ERROR: The line referenced cannot be used for this And Introduction");
            return false;
        }

        return true;
    }

    public boolean isAndElimValid(Expression e1) {

        int ref1;
        try {
            ref1 = e1.getReferenceLine().get(0) - 1;

            if (!expressions.get(ref1).contains(new Operator("AND", OperatorType.AND))) {
                errors.add("RULE ERROR: The line referenced cannot be used for this And Elimination");
                return false;
            }
        } catch (IndexOutOfBoundsException e) {
            errors.add("RULE ERROR: A valid line must be referenced to use this rule");
            return false;
        }


    List<Expression> sides = expressions.get(ref1).splitExpressionBy(OperatorType.AND);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);



        if (lhs.equals(e1) || rhs.equals(e1)) {
            return true;
        }

        errors.add("RULE ERROR: And Elimination cannot be used here with this reference");
        return false;
    }

    public boolean isOrIntroValid(Expression e1) {

        if (!e1.contains(new Operator("OR", OperatorType.OR))) {
            errors.add("RULE ERROR: This line cannot be used for Or Introduction");
            return false;
        }

        List<Expression> sides = e1.splitExpressionBy(OperatorType.OR);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        int ref1;
        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
        } catch (IndexOutOfBoundsException e) {
            errors.add("RULE ERROR: A valid line must be referenced to use this rule");
            return false;
        }

            if (expressions.get(ref1).equals(lhs) || expressions.get(ref1).equals(rhs)) {
                return true;
            }

        errors.add("RULE ERROR: Or Introduction cannot be used here with this reference");
        return false;
    }


    public boolean isImpliesIntroValid(Expression e1) {

        if (!e1.contains(new Operator("IMPLIES", OperatorType.IMPLIES))) {
            errors.add("RULE ERROR: This line cannot be used for Implies Introduction");
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
            errors.add("RULE ERROR: Two valid lines must be referenced to use this rule");
            return false;
        }

        Expression assumption = expressions.get(ref1);
        Expression conclusion = expressions.get(ref2);

        if (assumption.equals(lhs) && assumption.getRuleType() == RuleType.ASSUMPTION &&
                conclusion.equals(rhs)) {
            return true;
        }

        errors.add("RULE ERROR: Implies Introduction cannot be used here");
        return false;
    }

    public boolean isImpliesElimValid(Expression e1) {

        int ref1;
        int ref2;

        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
            ref2 = e1.getReferenceLine().get(1) - 1;
        } catch (IndexOutOfBoundsException e) {
            errors.add("RULE ERROR: Two valid lines must be referenced to use this rule");
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
            errors.add("RULE ERROR: This reference cannot be used for Implies Elimination");
            return false;
        }

        errors.add("RULE ERROR: Implies Elimination cannot be used here");
        return false;
    }


    public boolean isDoubleNotElimValid(Expression e1) {

        int ref1;

        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
        } catch (IndexOutOfBoundsException e) {
            errors.add("RULE ERROR: A valid line must be referenced to use this rule");
            return false;
        }

        Expression expr = expressions.get(ref1);
        if (expr.contains(new Operator("NOT", OperatorType.NOT))) {
            expr.removeNcomponents(1);
            if (expr.contains(new Operator("NOT", OperatorType.NOT))) {
                expr.removeNcomponents(1);
            } else {
                errors.add("RULE ERROR: This reference cannot be used for NotNot Elimination as there is " +
                        "no double negation");
                return false;
            }
        } else {
            errors.add("RULE ERROR: This reference cannot be used for NotNot Elimination as there is " +
                    "no double negation");
            return false;
        }

        if (expr.equals(e1)) {
            return true;
        }

        errors.add("RULE ERROR: Double Not Elimination cannot be used here");
        return false;
    }

    public boolean isNotElimValid(Expression e1) throws SyntaxException {
        if (!e1.toString().equals("FALSE")) {
            errors.add("RULE ERROR: Not Elimination cannot be used here as this line is not FALSE");
            return false;
        }

        int ref1 = e1.getReferenceLine().get(0) - 1;
        int ref2 = e1.getReferenceLine().get(1) - 1;

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


        errors.add("RULE ERROR: Not Elimination cannot be used here");
        return false;

    }

    public boolean isOnlyEliminationValid(Expression e1) throws SyntaxException {

        int ref1 = e1.getReferenceLine().get(0) - 1;
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
            }
        }

        errors.add("RULE ERROR: Only Elimination cannot be used here");
        return false;
    }

    public boolean isOnlyIntroValid(Expression e1) throws SyntaxException {

        if (!e1.contains(new Operator("ONLY", OperatorType.ONLY))) {
            errors.add("RULE ERROR: Only Introduction cannot be used here");
            return false;
        }

        List<Expression> sides = e1.splitExpressionBy(OperatorType.ONLY);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        Expression result = new Expression();
        Expression result1 = new Expression();

        result.addToExpression(lhs + " -> " + rhs);
        result1.addToExpression(rhs + " -> " + lhs);

        int ref1 = e1.getReferenceLine().get(0) - 1;
        Expression expr = expressions.get(ref1);

        int ref2 = e1.getReferenceLine().get(1) - 1;
        Expression expr1 = expressions.get(ref2);

        if ((expr.equals(result) && expr1.equals(result1)) ||  (expr1.equals(result) && expr.equals(result1)) ){
            return true;
        }

        errors.add("RULE ERROR: Only Introduction cannot be used here");
        return false;
    }

    public boolean isNotIntroductionValid(Expression e1) throws SyntaxException {


        int ref1 = e1.getReferenceLine().get(0) - 1;
        Expression expr = expressions.get(ref1);

        int ref2 = e1.getReferenceLine().get(1) - 1;
        Expression expr1 = expressions.get(ref2);

        if (expr.equalExceptFirst(e1) && expr.getRuleType() == RuleType.ASSUMPTION && expr1.toString().equals("FALSE")) {
            return true;
        }


        errors.add("RULE ERROR: Not Introduction cannot be used here");
        return false;
    }

    public boolean isOrEliminationValid(Expression e1) throws SyntaxException {

        try {
            int ref1 = e1.getReferenceLine().get(0) - 1;
            Expression expr1 = expressions.get(ref1);

            int ref2 = e1.getReferenceLine().get(1) - 1;
            Expression expr2 = expressions.get(ref2);

            int ref3 = e1.getReferenceLine().get(2) - 1;
            Expression expr3 = expressions.get(ref3);

            int ref4 = e1.getReferenceLine().get(3) - 1;
            Expression expr4 = expressions.get(ref4);

            int ref5 = e1.getReferenceLine().get(4) - 1;
            Expression expr5 = expressions.get(ref5);


            if (expr1.contains(new Operator("OR", OperatorType.OR))) {
                List<Expression> sides = expr1.splitExpressionBy(OperatorType.OR);

                Expression lhs = sides.get(0);
                Expression rhs = sides.get(1);

                if (lhs.equals(expr2) && rhs.equals(expr4) && expr3.equals(expr5) && expr3.equals(e1)) {
                    return true;
                }
            }

        } catch(IndexOutOfBoundsException e) {

        }
        errors.add("RULE ERROR: Or Elimination cannot be used here");
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
}
