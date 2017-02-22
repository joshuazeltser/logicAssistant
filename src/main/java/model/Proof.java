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

    public void separateByNewLine(String proof, String rule)  {

        if (!proof.equals("") && !rule.equals("")) {
            String[] expr = proof.split("\\r?\\n");
            String[] exprRule = rule.split("\\r?\\n");

            for (int i = 0; i < expr.length; i++) {
                Expression newExpr = new Expression(convertStringToRule(exprRule[i]));
                try {
                    newExpr.addToExpression(expr[i]);
                } catch (SyntaxException e) {
                    errors.add(e.getMessage());
                }

                addExpression(newExpr);
            }
        }

    }

    public String printErrors() {
        String result = "";
        for (String str : errors) {
            result += str + "\n";
        }
        return result;
    }

    private RuleType convertStringToRule(String rule) {
        switch (rule) {
            case "GIVEN": return RuleType.GIVEN;
            case "ASSUMPTION": return RuleType.ASSUMPTION;
            case "AND_INTRO": return RuleType.AND_INTRO;
            case "AND_ELIM": return RuleType.AND_ELIM;
            case "OR_INTRO": return RuleType.OR_INTRO;
            case "OR_ELIM": return RuleType.OR_ELIM;
            case "NOT_INTRO": return RuleType.NOT_INTRO;
            case "NOT_ELIM": return RuleType.NOT_ELIM;
            case "IMPLIES_INTRO": return RuleType.IMPLIES_INTRO;
            case "IMPLIES_ELIM": return RuleType.IMPLIES_ELIM;
            case "ONLY_INTRO": return RuleType.ONLY_INTRO;
            case "ONLY_ELIM": return RuleType.ONLY_ELIM;
            default: return RuleType.INVALID;
        }
    }

    public boolean isProofValid() throws SyntaxException {


        for (int i = expressions.size()-1; i >= 0; i--) {
            switch (expressions.get(i).getRuleType()) {
                case AND_ELIM: if (!isAndElimValid(expressions.get(i))) {
                    System.out.println("AND_ELIM error");
                    return false;
                } break;
                case AND_INTRO: if (!isAndIntroValid(expressions.get(i))) {
                    System.out.println("AND_INTRO error");
                    return false;
                } break;
                case OR_ELIM: if (!isOrEliminationValid(expressions.get(i))) {
                    System.out.println("OR_ELIM error");
                    return false;
                } break;
                case OR_INTRO: if (!isOrIntroValid(expressions.get(i))) {
                    System.out.println("OR_INTRO error");
                    return false;
                } break;
                case IMPLIES_ELIM: if (!isImpliesElimValid(expressions.get(i))) {
                    System.out.println("IMPLIES_ELIM error");

                    return false;
                } break;
                case IMPLIES_INTRO: if (!isImpliesIntroValid(expressions.get(i))) {
                    System.out.println("IMPLIES_INTRO error");
                    return false;
                } break;
                case NOT_ELIM: if (!isNotElimValid(expressions.get(i))) {
                    System.out.println("NOT_ELIM error");
                    return false;
                } break;
                case NOT_INTRO: if (!isNotIntroductionValid(expressions.get(i))) {
                    System.out.println("NOT_INTRO error");
                    return false;
                } break;
                case ONLY_ELIM: if (!isOnlyEliminationValid(expressions.get(i))) {
                    System.out.println("ONLY_ELIM error");
                    return false;
                } break;
                case ONLY_INTRO: if (!isOnlyIntroValid(expressions.get(i))) {
                    System.out.println("ONLY_INTRO error");
                    return false;
                } break;
                case INVALID: return false;
                default: break;
            }
            expressions.remove(i);
        }
        return true;
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

            List<Expression> sides = e1.splitExpressionBy(OperatorType.AND);


            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            int ref1 = e1.getReferenceLine().get(0) - 1;
            int ref2 = e1.getReferenceLine().get(1) - 1;

            if((expressions.get(ref1).equals(lhs) && expressions.get(ref2).equals(rhs))
                    || (expressions.get(ref2).equals(lhs) && expressions.get(ref1).equals(rhs))) {
                return true;
            }

        return false;
    }

    public boolean isAndElimValid(Expression e1) {

        int ref1 = e1.getReferenceLine().get(0) - 1;

        List<Expression> sides = expressions.get(ref1).splitExpressionBy(OperatorType.AND);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        if (lhs.equals(e1) || rhs.equals(e1)) {
            return true;
        }

        return false;
    }

    public boolean isOrIntroValid(Expression e1) {

        List<Expression> sides = e1.splitExpressionBy(OperatorType.OR);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        int ref1 = e1.getReferenceLine().get(0) - 1;

            if (expressions.get(ref1).equals(lhs) || expressions.get(ref1).equals(rhs)) {
                return true;
            }

        return false;
    }




    public boolean isImpliesIntroValid(Expression e1) {


        List<Expression> sides = e1.splitExpressionBy(OperatorType.IMPLIES);

        Expression lhs = sides.get(0);
        Expression rhs = sides.get(1);

        int ref1 = e1.getReferenceLine().get(0) - 1;
        int ref2 = e1.getReferenceLine().get(1) - 1;

        Expression assumption = expressions.get(ref1);
        Expression conclusion = expressions.get(ref2);

        if (assumption.equals(lhs) && assumption.getRuleType() == RuleType.ASSUMPTION &&
                conclusion.equals(rhs)) {
            return true;
        }

        return false;
    }

    public boolean isImpliesElimValid(Expression e1) {

        int ref1 = e1.getReferenceLine().get(0) - 1;
        int ref2 = e1.getReferenceLine().get(1) - 1;

        Expression a = expressions.get(ref1);
        Expression b = expressions.get(ref2);

        Expression other = new Expression();

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
        }

        return false;
    }


    public boolean isNotElimValid(Expression e1) {

        for (Expression expr : expressions) {
            if (expr.doubleNot()) {
                expr.removeNcomponents(2);
                if (expr.equals(e1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnlyEliminationValid(Expression e1) throws SyntaxException {
        for (Expression expr : expressions) {

            if (expr.contains(new Operator("ONLY", OperatorType.ONLY))) {
                int numAnd = expr.countOperator(OperatorType.ONLY);
                int count = 0;
                while (count != numAnd) {

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
                    count++;
                }

            }
        }
        return false;
    }

    public boolean isOnlyIntroValid(Expression e1) throws SyntaxException {

        int numAnd = e1.countOperator(OperatorType.ONLY);
        int count = 0;
        while (count != numAnd) {
            List<Expression> sides = e1.splitExpressionBy(OperatorType.ONLY);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            Expression result = new Expression();
            Expression result2 = new Expression();


            result.addToExpression(lhs + " -> " + rhs);

            result2.addToExpression(rhs + " -> " + lhs);



            for (Expression expr : expressions) {
                if (expr.equals(result) || expr.equals(result2)) {
                    if (expr.equals(result) || expr.equals(result2)) {
                        return true;
                    }
                }
            }

            count++;
        }
        return false;
    }

    public boolean isNotIntroductionValid(Expression e1) throws SyntaxException {

        e1.removeNcomponents(1);

        for (Expression expr : expressions) {
            if (expr.equals(e1)) {
                Expression saved = e1;
                for (Expression expr1 : expressions) {
                    Expression e = new Expression();
                    String temp = "!" + saved.toString();

                    if (saved.toString() != "" ) {
                        e.addToExpression(temp);
                    }


                    for (Expression expr2 : expressions) {
                        if ((expr2.toString().equals(e.toString()))) {
                            return true;
                        }
                    }
                    saved = expr1;
                }
            }
        }
        return false;
    }

    public boolean isOrEliminationValid(Expression e1) throws SyntaxException {
        List<Expression> assumptions = new LinkedList<>();

        for (Expression expr : expressions) {
            if (expr.getRuleType() == RuleType.ASSUMPTION) {
                assumptions.add(expr);
            }
        }
        for (int i = 0; i < assumptions.size(); i++) {
            for (int j = 0; i != j && j < assumptions.size(); j++) {
                Expression orExpr = new Expression();
                Expression orOppositeExpr = new Expression();
                orExpr.addToExpression(assumptions.get(j).toString() + " | " + assumptions.get(i).toString());
                orOppositeExpr.addToExpression(assumptions.get(i).toString() + " | " + assumptions.get(j).toString());

                if (expressions.contains(orExpr) || expressions.contains(orOppositeExpr)) {
                    Expression a = assumptions.get(j);
                    Expression b = assumptions.get(i);

                    int lhsIndex = expressions.indexOf(a);
                    int rhsIndex = expressions.indexOf(b);

                    for (int k = lhsIndex + 1; k < expressions.size(); k++) {
                        if (expressions.get(k).equals(e1)) {
                            for (int l = rhsIndex + 1; l < expressions.size(); l++) {
                                if (expressions.get(l).equals(e1)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

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
