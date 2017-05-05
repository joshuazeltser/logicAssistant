package model;

import javassist.expr.Expr;

import java.util.LinkedList;
import java.util.List;

import static model.OperatorType.*;

/**
 * Created by joshuazeltser on 18/01/2017.
 */
public class Proof {

    private List<Expression> expressions;

    private List<String> errors;

    private String proofString;
    private String proofLabels;

    private List<Proof> proofSteps;


    private String resultString;

    private Expression resultExpr;

    private List<Expression> list_proof;

    private List<Expression> list_goals;


    public Proof() {
        expressions = new LinkedList<>();
        errors = new LinkedList<>();
        proofString = "";
        proofLabels = "";
        proofSteps = new LinkedList<>();
        resultString = "";
        resultExpr = new Expression();
        list_goals = new LinkedList<>();
        list_proof = new LinkedList<>();
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
            case "DoubleNot-Elim": return RuleType.DOUBLE_NOT_ELIM;
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
                case DOUBLE_NOT_ELIM: isDoubleNotElimValid(expressions.get(i)); break;
                case INVALID: return false;
                default: break;
            }
//            expressions.remove(i);
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


    public boolean isDoubleNotElimValid(Expression e1) throws SyntaxException {

        int ref1;

        try {
            ref1 = e1.getReferenceLine().get(0) - 1;
        } catch (IndexOutOfBoundsException e) {
            errors.add("LINE " + (expressions.indexOf(e1) + 1) + " - RULE ERROR: A valid line must be referenced to use this rule");
            return false;
        }
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



//    private List<Expression> completedGoals = new LinkedList<>();

    public String solveProof() throws SyntaxException {

        resultExpr.addToExpression(resultString);
        list_goals.add(resultExpr);

        if (!expressions.isEmpty()) {
            list_proof.addAll(expressions);
        }


        Expression current_goal = resultExpr;

        boolean specialCase = false;
        //special case
        if (resultExpr.contains(new Operator("IMPLIES", IMPLIES))) {
            specialCase = true;
        }
        while (true) {

            if (checkIfExpressionsReached(current_goal)) {
                if (list_goals.size() > 1) {
                    list_goals.remove(current_goal);
                    current_goal = list_goals.get(list_goals.size() - 1);
                    applyIntroductionRule(current_goal);
                }
                if (current_goal.equals(list_goals.get(0))) {

                    System.out.println("list_proof " + list_proof);
                    System.out.println("list_goals " + list_goals);
                    return list_proof.toString();
                }
                continue;

            } else {
                if (eliminationRuleApplicable() && !specialCase) {
                    specialCase = false;
                    continue;
                }
                    //procedure 2
                    if (!current_goal.toString().equals("FALSE")) {
                        //procedure 2.1
                        setupIntroductionRules(current_goal);
                        current_goal = list_goals.get(list_goals.size() - 1);
                        continue;
                    } else if (allMarked()) {
                        continue;
                    } else {
                        //procedure 2.2
                        falseRulesSetup();
                        continue;
                    }

            }
        }

//        return null;
    }

    private void falseRulesSetup() throws SyntaxException {
        for (Expression expr : list_proof) {
            if (expr.getFirstComp().equals(new Operator("NOT", NOT))) {
                Expression temp = new Expression();
                temp.addToExpression(expr.toString());
                temp.removeNcomponents(1);
                list_goals.add(temp);
            }
        }
    }

    private void setupIntroductionRules(Expression expr) throws SyntaxException {

        if (expr.contains(new Operator("AND", AND))) {
            List<Expression> sides = expr.splitExpressionBy(OperatorType.AND);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            list_goals.add(rhs);
            list_goals.add(lhs);

        } else if (expr.contains(new Operator("IMPLIES", IMPLIES))) {
            List<Expression> sides = expr.splitExpressionBy(OperatorType.IMPLIES);

            Expression lhs = sides.get(0);
            lhs.setRuleType(RuleType.ASSUMPTION);
            Expression rhs = sides.get(1);
            list_goals.add(rhs);
            list_proof.add(lhs);
        } else if (expr.getFirstComp().equals(new Operator("NOT", NOT))) {
            Expression temp = new Expression(RuleType.ASSUMPTION);
            temp.addToExpression(expr.toString());
            temp.removeNcomponents(1);

            list_proof.add(temp);

            Expression subGoal = new Expression();
            subGoal.addToExpression("FALSE");
            list_goals.add(subGoal);
        }
    }


    private void applyIntroductionRule(Expression expr) {

        if (list_goals.get(list_goals.size()-1).contains(new Operator("AND", AND))) {
            List<Expression> sides = list_goals.get(list_goals.size()-1).splitExpressionBy(OperatorType.AND);

            Expression lhs = sides.get(0);
            Expression rhs = sides.get(1);

            if (list_proof.contains(lhs) && list_proof.contains(rhs)) {
                list_goals.get(list_goals.size()-1).setRuleType(RuleType.AND_INTRO);
                list_proof.add(list_goals.get(list_goals.size()-1));

            }
        } else if (list_goals.get(list_goals.size()-1).contains(new Operator("IMPLIES", IMPLIES))) {
            List<Expression> sides = expr.splitExpressionBy(OperatorType.IMPLIES);

            Expression rhs = sides.get(1);
            if (list_proof.contains(rhs)) {
                list_goals.get(list_goals.size()-1).setRuleType(RuleType.IMPLIES_INTRO);
                list_proof.add(list_goals.get(list_goals.size()-1));
            }
        } else if (list_goals.get(list_goals.size()-1).getFirstComp().equals(new Operator("NOT", NOT))) {
            if (list_proof.get(list_proof.size()-1).toString().equals("FALSE")) {
                list_goals.get(list_goals.size()-1).setRuleType(RuleType.NOT_INTRO);
                list_proof.add(list_goals.get(list_goals.size()-1));
            }
        }
    }

    private boolean allMarked() {
        return false;
    }

    private void applyEliminationRule(Expression expr) {
            list_proof.add(expr);
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

        boolean not = tryNotElim(newProof);
        newProof.expressions = list_proof;

        boolean notNot = tryDoubleNotElim(newProof);
        newProof.expressions = list_proof;
        boolean or = tryOrElim(newProof);
        newProof.expressions = list_proof;

        return and || implies || only || not || notNot || or;
    }

    private boolean tryOrElim(Proof newProof) {
        int count = 1;
        boolean changed = false;
        for (int i = 0; i < newProof.expressions.size(); i++) {
            if (newProof.expressions.get(i).contains(new Operator("OR", OR))) {
                //complex case to be thought about
            }

            count++;
        }
        return changed;
    }

    private boolean tryNotElim(Proof newProof) throws SyntaxException {
        int count = 1;
        boolean changed = false;

        for (int i = 0; i < newProof.expressions.size(); i++) {
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
                            if (!list_proof.contains(notResult)) {
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
                        temp1.setRuleType(RuleType.DOUBLE_NOT_ELIM);
                        if (!list_proof.contains(temp1)) {
                            applyEliminationRule(temp1);
                            changed = true;
                        }
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

                if (newProof.isOnlyEliminationValid(result1)) {
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
                List<Expression> sides = newProof.expressions.get(i).splitExpressionBy(OperatorType.AND);

                Expression lhs = sides.get(0);
                lhs.addReferenceLine(Integer.toString(count));
                Expression rhs = sides.get(1);
                rhs.addReferenceLine(Integer.toString(count));

                if (newProof.isAndElimValid(lhs) &&
                        (lhs.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                                lhs.contains(new Operator("CLOSE",CLOSE_BRACKET))) ||
                        (!lhs.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                                !lhs.contains(new Operator("CLOSE",CLOSE_BRACKET)))) {
                    Expression result = new Expression(RuleType.AND_ELIM);
                    result.addToExpression(lhs.toString());
                    if (!list_proof.contains(result)) {
                        applyEliminationRule(result);
                        changed = true;
                        doneLeft = true;
                        break;
                    }

                }

                if (newProof.isAndElimValid(lhs) &&
                        (rhs.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                                rhs.contains(new Operator("CLOSE",CLOSE_BRACKET))) ||
                        (!rhs.contains(new Operator("OPEN", OPEN_BRACKET)) &&
                                !rhs.contains(new Operator("CLOSE",CLOSE_BRACKET))) && doneLeft) {

                    Expression result = new Expression(RuleType.AND_ELIM);
                    result.addToExpression(rhs.toString());
                    if (!list_proof.contains(result)) {
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







    private boolean checkIfFalseExpressionReached() throws SyntaxException {
        boolean exprApresent = false;
        boolean exprNotBpresent = false;
        for (Expression expr : list_proof) {
            Expression temp = new Expression();

            if (expr.getFirstComp().equals(new Operator("NOT", NOT))) {
                temp.addToExpression(expr.toString());
                temp.removeNcomponents(1);
                exprNotBpresent = true;
            } else {
                temp.addToExpression("!" + expr.toString());
                exprApresent = true;
            }
            for (Expression expr1 : list_proof) {
                if (expr1.equals(temp)) {
                    return false;
                }
            }
        }
        return exprApresent && exprNotBpresent;
    }

    private boolean checkIfExpressionsReached(Expression current) throws SyntaxException {
        for (Expression expr : list_proof) {

            if (expr.equals(current)) {
                return true;
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


    public List<Proof> getProofSteps() {
        return proofSteps;
    }

    public void setProofSteps(List<Proof> proofSteps) {
        this.proofSteps = proofSteps;
    }



    public Expression getResultExpr() {
        return resultExpr;
    }

    public void setResultExpr(Expression resultExpr) {
        this.resultExpr = resultExpr;
    }


    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

}
