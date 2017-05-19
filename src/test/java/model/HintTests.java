package model;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 14/04/2017.
 */
public class HintTests {
//
    Proof proof = new Proof();
//
    @Test
    public void andEliminationSolverTest() throws SyntaxException {
        String str = "A ^ B\nC";
        String rules = "GIVEN\nGIVEN";

        String result = "B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[A AND B, C, B]"));
    }
//
    @Test
    public void impliesEliminationSolverTest() throws SyntaxException {
        String str = "A -> B\nA";
        String rules = "GIVEN\nGIVEN";


        String result = "B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, A, B]"));
    }

    @Test
    public void impliesAndEliminationSolverTest() throws SyntaxException {
        String str = "A ^ B\nB -> C";
        String rules = "GIVEN\nGIVEN";

        String result = "C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[A AND B, B IMPLIES C, B, C]"));
    }

//
    @Test
    public void onlyEliminationSolverTest() throws SyntaxException {
        String str = "A <-> B";
        String rules = "GIVEN";

        String result = "B -> A";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A ONLY B, B IMPLIES A]"));
    }

//
    @Test
    public void notEliminationSolverTest() throws SyntaxException {
        String str = "!A\nA";
        String rules = "GIVEN\nGIVEN";

        String result = "FALSE";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[NOT A, A, FALSE]"));
    }


    @Test
    public void simpleNotNotEliminationSolverTest() throws SyntaxException {
        String str = "!!A";
        String rules = "GIVEN";

        String result = "A";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[NOT NOT A, A]"));
    }
//

//
    @Test
    public void simpleAndIntroSolverTest() throws SyntaxException {
        String str = "A\nB";
        String rules = "GIVEN\nGIVEN";

        String result = "A ^ B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A, B, A AND B]"));
    }

    @Test
    public void harderAndIntroSolverTest() throws SyntaxException {
        String str = "A\nA -> B";
        String rules = "GIVEN\nGIVEN";

        String result = "A ^ B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A, A IMPLIES B, B, A AND B]"));
    }

    @Test
    public void largeAndIntroSolverTest() throws SyntaxException {
        String str = "A\nB\nC\nD\nE -> F\nO\nS\nY -> F\nX\nP";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN";

        String result = "S ^ P";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A, B, C, D, E IMPLIES F, O, S, Y IMPLIES F, X, P, S AND P]"));
    }
//
    @Test
    public void iffImpliesAndSolverTest() throws SyntaxException {
        String str = "A -> B\nB -> C\nA";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        String result = "C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, B IMPLIES C, A, B, C]"));
    }

    @Test
    public void fullProofTest() throws SyntaxException {

        String str = "!(A | B)";
        String rules = "GIVEN";

        String result = "!A";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[NOT OPEN A OR B CLOSE, A, A OR B, FALSE, NOT A]"));
    }


    @Test
    public void simpleOrIntroSolverTest() throws SyntaxException {
        String str = "A";
        String rules = "GIVEN";

        String result = "A | B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A, A OR B]"));
    }

    @Test
    public void largeOrIntroSolverTest() throws SyntaxException {
        String str = "A\nB\nC\nD\nE -> F\nO\nS\nY -> F\nX";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN";

        String result = "S | P";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A, B, C, D, E IMPLIES F, O, S, Y IMPLIES F, X, S OR P]"));
    }

    @Test
    public void OrImpliesAndSolverTest() throws SyntaxException {
        String str = "A ^ B\nA -> C";
        String rules = "GIVEN\nGIVEN";

        String result = "A ^ B | C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A AND B, A IMPLIES C, A, C, B, B OR C, A AND B OR C]"));
    }

    @Test
    public void simpleImpliesAtEndIntroSolverTest() throws SyntaxException {
        String str = "C ^ B";
        String rules = "GIVEN";

        String result = "A -> B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[C AND B, A, B, A IMPLIES B]"));
    }

//
    @Test
    public void harderImpliesAtEndIntroSolverTest() throws SyntaxException {
        String str = "A -> C\nC -> D";
        String rules = "GIVEN\nGIVEN";

        String result = "A -> D";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES C, C IMPLIES D, A, C, D, A IMPLIES D]"));
    }
//
    @Test
    public void orEliminationSolverTest() throws SyntaxException {
        String str = "A | B\nA -> C\nB -> C";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        String result = "C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A OR B, A IMPLIES C, B IMPLIES C, A, C, B, C, C]"));
    }

    @Test
    public void orEliminationSolverTest2() throws SyntaxException {
        String str = "A | B ^ D\nA -> B";
        String rules = "GIVEN\nGIVEN";

        String result = "B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A OR B AND D, A IMPLIES B, A OR B, D, A, B, B AND D, B, B]"));
    }
//
    @Test
    public void notIntroSolverTest() throws SyntaxException {
        String str = "S -> B\nB -> W\n!W";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        String result = "!S";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[S IMPLIES B, B IMPLIES W, NOT W, S, B, W, FALSE, NOT S]"));
    }

    @Test
    public void notElimolverTest() throws SyntaxException {
        String str = "!A\nA ^ B";
        String rules = "GIVEN\nGIVEN";

        String result = "FALSE";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[NOT A, A AND B, A, FALSE]"));
    }

    //TODO: to be fixed with brackets around A ^ B
    @Test
    public void bracketsIntroTest() throws SyntaxException {
        String str = "A ^ C";
        String rules = "GIVEN";

        String result = "(A ^ B) -> C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str,rules);

//        System.out.println(proof.solveProof());
//        assertTrue(proof.solveProof().toString().equals("[A AND C, A AND B, A, C, A AND B IMPLIES C]"));
    }

    @Test
    public void simpleOnlyIntroSolverTest() throws SyntaxException {
        String str = "A -> B\nB -> A";
        String rules = "GIVEN\nGIVEN";

        String result = "A <-> B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, B IMPLIES A, A ONLY B]"));
    }


    @Test
    public void notNotImpliesTest() throws SyntaxException {
        String str = "!!A\nA -> B";
        String rules = "GIVEN\nGIVEN";

        String result = "B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[NOT NOT A, A IMPLIES B, A, B]"));
    }

    @Test
    public void falseTest() throws SyntaxException {
        String str = "A -> B\nA\n!B";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        String result = "FALSE";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, A, NOT B, B, FALSE]"));

    }

    @Test
    public void hintTest2() throws SyntaxException {
        String str = "S -> B\nB -> W\n!W";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        String result = "!S";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nS";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nB";
        rules +="\nImplies-Elim (1,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nW";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: NOT_ELIM"));

        str += "\nFALSE";
        rules +="\nNot-Elim (3,6)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: NOT_INTRO"));

        str += "\n!S";
        rules +="\nNot-Intro (4,7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));

    }
//
    @Test
    public void hintTest3() throws SyntaxException {
        String str = "A | B\nA -> C\nB -> C";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        String result = "C";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nA";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nC";
        rules +="\nImplies-Elim (2,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nB";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nC";
        rules +="\nImplies-Elim (3,6)";
        proof.frontEndFunctionality(str, rules);

//                 System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_ELIM"));

        str += "\nC";
        rules +="\nOr-Elim (1,4,5,6,7)";
        proof.frontEndFunctionality(str, rules);

//                         System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest5() throws SyntaxException {
        String str = "!!A\nA -> B";
        String rules = "GIVEN\nGIVEN";

        String result = "B";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: DOUBLE_NOT_ELIM"));

        str += "\nA";
        rules +="\nDoubleNot-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//                        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nB";
        rules +="\nImplies-Elim (2,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest6() throws SyntaxException {
        String str = "A -> B\nB -> A";
        String rules = "GIVEN\nGIVEN";

        String result = "A <-> B";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint(result).equals("Hint: ONLY_INTRO"));

        str += "\nA <-> B";
        rules +="\nOnly-Intro (1,2)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest7() throws SyntaxException {
        String str = "A | B ^ D\nA -> B";
        String rules = "GIVEN\nGIVEN";

        String result = "B";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        str += "\nA | B";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        str += "\nD";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nA";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nB";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nB ^ D";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        str += "\nB";
        rules +="\nAnd-Elim (7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_ELIM"));

        str += "\nB";
        rules +="\nOr-Elim (1,5,6,7,8)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest8() throws SyntaxException {
        String str = "A ^ (B -> D)\nB";
        String rules = "GIVEN\nGIVEN";

        String result = "D";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        str += "\nB -> D";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nD";
        rules +="\nImplies-Elim (2,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest9() throws SyntaxException {
        String str = "!(A | B)";
        String rules = "GIVEN";

        String result = "!A";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nA";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_INTRO"));

        str += "\nA | B";
        rules +="\nOr-Intro (2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(false));
        assertTrue(proof.generateHint(result).equals("Hint: NOT_ELIM"));

        str += "\nFALSE";
        rules +="\nNot-Elim (1,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(false));
        assertTrue(proof.generateHint(result).equals("Hint: NOT_INTRO"));

        str += "\n!A";
        rules +="\nNot-Intro (2,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(false));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest11() throws SyntaxException {
        String str = "A -> (B -> C)";
        String rules = "GIVEN";

        String result = "A ^ B -> C";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nA ^ B";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        str += "\nA";
        rules +="\nAnd-Elim (2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nB -> C";
        rules +="\nImplies-Elim (1,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));
    }

    @Test
    public void hintTest12() throws SyntaxException {
        String str = "A ^ (B | C)\nB -> D\nC -> D";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        String result = "D | E";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        str += "\nB | C";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nB";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nD";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nC";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nD";
        rules +="\nImplies-Elim (3,7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_ELIM"));

        str += "\nD";
        rules +="\nOr-Elim (4,5,6,7,8)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_INTRO"));

        str += "\nD | E";
        rules +="\nOr-Intro (9)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest13() throws SyntaxException {
        String str = "B ^ E\nE -> C";
        String rules = "GIVEN\nGIVEN";

        String result = "A -> (B -> C)";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nA";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        str += "\nB";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        str += "\nE";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str += "\nC";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_INTRO"));

        str += "\nB -> C";
        rules +="\nImplies-Intro (4,6)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_INTRO"));

        str += "\nA -> (B -> C)";
        rules +="\nImplies-Intro (3,7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }


    //TODO: fix this case
    @Test
    public void hintTest14() throws SyntaxException {
        String str = "A\nA -> B\nB -> C";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        String result = "(A ^ B) ^ C";
        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
//        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

    }

    @Test
    public void hintTest15() throws SyntaxException {

        String result = "A -> A | C";
        proof.setResultString(result);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str = "A";
        String rules ="ASSUMPTION";
        proof.frontEndFunctionality(str, rules);

        //        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_INTRO"));

        str += "\nA | C";
        rules +="\nOr-Intro (1)";
        proof.frontEndFunctionality(str, rules);

        //        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_INTRO"));

        str += "\nA -> A | C";
        rules +="\nImplies-Intro (1,2)";
        proof.frontEndFunctionality(str, rules);

        //        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));

    }

    @Test
    public void multipleGoalTest1() throws SyntaxException {

        String str = "A ^ B\nC\nD\n...\nB ^ C\nD -> B ^ C";
        String rules = "GIVEN\nGIVEN\nASSUMPTION\nEMPTY\n\nImplies-Intro (3,5)";

        String result = "D -> B ^ C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A AND B, C, D, B, B AND C, D IMPLIES B AND C]"));

    }

    @Test
    public void multipleGoalTest2() throws SyntaxException {

        String str = "A ^ B\nD -> E\nC\nD\n...\nB ^ C ^ E\nD -> B ^ C ^ E";
        String rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\nEMPTY\n\nImplies-Elim (3,5)";

        String result = "D -> B ^ C ^ E";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString()
                .equals("[A AND B, D IMPLIES E, C, D, E, B, C AND E, B AND C AND E, D IMPLIES B AND C AND E]"));
    }

    @Test
    public void multipleGoalHintTest1() throws SyntaxException {

        String str = "A ^ B\nC\nD\n...\nB ^ C\nD -> B ^ C";

        String rules = "GIVEN\nGIVEN\nASSUMPTION\n\n\nImplies-Intro (3,5)";

        String result = "D -> B ^ C";

        proof.setResultString(result);

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str1 = "A ^ B\nC\nD\nB\nB ^ C\nD -> B ^ C";
        String rules1 = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)\n\nImplies-Intro (3,5)";

        proof.frontEndFunctionality(str1, rules1);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_INTRO"));

        String rules2 = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)\nAnd-Intro (2,4)\nImplies-Intro (3,5)";

        proof.frontEndFunctionality(str1, rules2);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));

    }

    @Ignore
    @Test
    public void multipleGoalHintTest2() throws SyntaxException {


        String str = "A ^ B";
        String str6 = "D -> E";
        String str1 = "C";
        String str2 = "D";
        String str3 = "...";
        String str4 = "B ^ C ^ E";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr6 = new Expression(RuleType.GIVEN);
        expr6.addToExpression(str6);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.ASSUMPTION);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.EMPTY);
//        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.EMPTY);
        expr4.addToExpression(str4);


        proof.addExpression(expr);
        proof.addExpression(expr6);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);

        String result = "D -> B ^ C ^ E";
        proof.setResultString(result);


//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str7 = "E";
        Expression expr7 = new Expression(RuleType.IMPLIES_ELIM);
        expr7.addToExpression(str7);
        expr7.addReferenceLine("2");
        expr7.addReferenceLine("4");
        proof.addAt(expr7, 4);

//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str8 = "B";
        Expression expr8 = new Expression(RuleType.AND_ELIM);
        expr8.addToExpression(str8);
        expr8.addReferenceLine("1");
        proof.addAt(expr8, 5);

//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_INTRO"));

        str3 = "C ^ E";
//        Expression expr9 = new Expression(RuleType.AND_INTRO);
        expr3.addToExpression(str3);
        expr3.setRuleType(RuleType.AND_INTRO);
        expr3.addReferenceLine("3");
        expr3.addReferenceLine("5");
//        proof.addAt(expr9, 6);

//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_INTRO"));

        expr4.setRuleType(RuleType.AND_INTRO);
        expr4.addReferenceLine("6");
        expr4.addReferenceLine("7");

//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_INTRO"));
//
        String str10 = "D -> B ^ C ^ E";
        Expression expr10 = new Expression(RuleType.IMPLIES_INTRO);
        expr10.addToExpression(str10);
        expr10.addReferenceLine("4");
        expr10.addReferenceLine("8");
        proof.addExpression(expr10);

//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }

    @Test
    public void multiComboTest1() throws SyntaxException {

        String str = "A ^ B";
        String str1 = "A -> E";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        proof.addExpression(expr);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);
        proof.addExpression(expr1);

        String result = "B | E";

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str2 = "B";
        Expression expr2 = new Expression(RuleType.AND_ELIM);
        expr2.addToExpression(str2);
        expr2.addReferenceLine("1");
        proof.addExpression(expr2);

        System.out.println(proof.generateHint(result));
    }

    @Ignore
    @Test
    public void multiComboTest2() throws SyntaxException {

        String str = "A";
        String str1 = "B";
        String str2 = "A -> B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        proof.addExpression(expr);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);
        proof.addExpression(expr1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);
        proof.addExpression(expr2);

        String result = "A ^ B";

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str3 = "A ^ B";
        Expression expr3 = new Expression(RuleType.AND_INTRO);
        expr3.addToExpression(str3);
        expr3.addReferenceLine("1");
        expr3.addReferenceLine("2");
        proof.addExpression(expr3);

        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }

    @Test
    public void multipleGoalHintTest3() throws SyntaxException {

        String str = "!(A | B)";
        String str1 = "A";
        String str2 = "...";
        String str3 = "FALSE";
        String str4 = "!A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        proof.addExpression(expr);

        Expression expr1 = new Expression(RuleType.ASSUMPTION);
        expr1.addToExpression(str1);
        proof.addExpression(expr1);

        Expression expr2 = new Expression(RuleType.EMPTY);
        proof.addExpression(expr2);

        Expression expr3 = new Expression(RuleType.EMPTY);
        expr3.addToExpression(str3);
        proof.addExpression(expr3);

        Expression expr4 = new Expression(RuleType.NOT_INTRO);
        expr4.addToExpression(str4);
        expr4.addReferenceLine("2");
        expr4.addReferenceLine("4");
        proof.addExpression(expr4);

        String result = "!A";

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_INTRO"));

        str2 = "A | B";
        expr2.addToExpression(str2);
        expr2.setRuleType(RuleType.OR_INTRO);
        expr2.addReferenceLine("2");

        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: NOT_ELIM"));

        expr3.setRuleType(RuleType.NOT_ELIM);
        expr3.addReferenceLine("1");
        expr3.addReferenceLine("3");

        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }

    @Ignore
    @Test
    public void multipleGoalHintTest4() throws SyntaxException {

        String str = "A | B";
        String str1 = "A -> D";
        String str2 = "B -> D";
        String str3 = "D -> E";
        String str4 = "A";
        String str5 = "...";
        String str6 = "E";
        String str7 = "B";
        String str8 = "...";
        String str9 = "E";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        proof.addExpression(expr);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);
        proof.addExpression(expr1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);
        proof.addExpression(expr2);

        Expression expr3 = new Expression(RuleType.GIVEN);
        expr3.addToExpression(str3);
        proof.addExpression(expr3);

        Expression expr4 = new Expression(RuleType.ASSUMPTION);
        expr4.addToExpression(str4);
        proof.addExpression(expr4);

        Expression expr5 = new Expression(RuleType.EMPTY);
        proof.addExpression(expr5);

        Expression expr6 = new Expression(RuleType.EMPTY);
        expr6.addToExpression(str6);
        proof.addExpression(expr6);

        Expression expr7 = new Expression(RuleType.ASSUMPTION);
        expr7.addToExpression(str7);
        proof.addExpression(expr7);

        Expression expr8 = new Expression(RuleType.EMPTY);
        proof.addExpression(expr8);

        Expression expr9 = new Expression(RuleType.EMPTY);
        expr9.addToExpression(str9);
        proof.addExpression(expr9);

        String result = "E";

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        str5 = "D";
        expr5.addToExpression(str5);
        expr5.setRuleType(RuleType.IMPLIES_ELIM);
        expr5.addReferenceLine("2");
        expr5.addReferenceLine("5");

        //        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        expr6.setRuleType(RuleType.IMPLIES_ELIM);
        expr6.addReferenceLine("4");
        expr6.addReferenceLine("6");

    }


}
