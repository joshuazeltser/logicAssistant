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
        String str = "A ^ B\nC\n\nB";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A AND B, C, B]"));
    }
//
    @Test
    public void impliesEliminationSolverTest() throws SyntaxException {
        String str = "A -> B\nA\n\nB";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, A, B]"));
    }

    @Test
    public void impliesAndEliminationSolverTest() throws SyntaxException {
        String str = "A ^ B\nB -> C\n\nC";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[A AND B, B IMPLIES C, B, C]"));
    }

//
    @Test
    public void onlyEliminationSolverTest() throws SyntaxException {
        String str = "A <-> B\n\nB -> A";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A ONLY B, B IMPLIES A]"));
    }

//
    @Test
    public void notEliminationSolverTest() throws SyntaxException {
        String str = "!A\nA\n\nFALSE";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[NOT A, A, FALSE]"));
    }


    @Test
    public void simpleNotNotEliminationSolverTest() throws SyntaxException {
        String str = "!!A\n\nA";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[NOT NOT A, A]"));
    }
//

//
    @Test
    public void simpleAndIntroSolverTest() throws SyntaxException {
        String str = "A\nB\n\nA ^ B";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A, B, A AND B]"));
    }

    @Test
    public void harderAndIntroSolverTest() throws SyntaxException {
        String str = "A\nA -> B\n\nA ^ B";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A, A IMPLIES B, B, A AND B]"));
    }

    @Test
    public void largeAndIntroSolverTest() throws SyntaxException {
        String str = "A\nB\nC\nD\nE -> F\nO\nS\nY -> F\nX\nP\n\nS ^ P";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A, B, C, D, E IMPLIES F, O, S, Y IMPLIES F, X, P, S AND P]"));
    }
//
    @Test
    public void iffImpliesAndSolverTest() throws SyntaxException {
        String str = "A -> B\nB -> C\nA\n\nC";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, B IMPLIES C, A, B, C]"));
    }

    @Test
    public void fullProofTest() throws SyntaxException {

        String str = "!(A | B)\n\n!A";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[NOT OPEN A OR B CLOSE, A, A OR B, FALSE, NOT A]"));
    }


    @Test
    public void simpleOrIntroSolverTest() throws SyntaxException {
        String str = "A\n\nA | B";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A, A OR B]"));
    }

    @Test
    public void largeOrIntroSolverTest() throws SyntaxException {
        String str = "A\nB\nC\nD\nE -> F\nO\nS\nY -> F\nX\n\nS | P";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A, B, C, D, E IMPLIES F, O, S, Y IMPLIES F, X, S OR P]"));
    }

    @Test
    public void OrImpliesAndSolverTest() throws SyntaxException {
        String str = "A ^ B\nA -> C\n\nA ^ B | C";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A AND B, A IMPLIES C, A, C, B, B OR C, A AND B OR C]"));
    }

    @Test
    public void simpleImpliesAtEndIntroSolverTest() throws SyntaxException {
        String str = "C ^ B\n\nA -> B";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[C AND B, A, B, A IMPLIES B]"));
    }

//
    @Test
    public void harderImpliesAtEndIntroSolverTest() throws SyntaxException {
        String str = "A -> C\nC -> D\n\nA -> D";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES C, C IMPLIES D, A, C, D, A IMPLIES D]"));
    }
//
    @Test
    public void orEliminationSolverTest() throws SyntaxException {
        String str = "A | B\nA -> C\nB -> C\n\nC";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A OR B, A IMPLIES C, B IMPLIES C, A, C, B, C, C]"));
    }

    @Test
    public void orEliminationSolverTest2() throws SyntaxException {
        String str = "A | B ^ D\nA -> B\n\nB";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A OR B AND D, A IMPLIES B, A OR B, D, A, B, B AND D, B, B]"));
    }
//
    @Test
    public void notIntroSolverTest() throws SyntaxException {
        String str = "S -> B\nB -> W\n!W\n\n!S";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[S IMPLIES B, B IMPLIES W, NOT W, S, B, W, FALSE, NOT S]"));
    }

    @Test
    public void notElimolverTest() throws SyntaxException {
        String str = "!A\nA ^ B\n\nFALSE";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[NOT A, A AND B, A, FALSE]"));
    }

    //TODO: to be fixed with brackets around A ^ B
    @Test
    public void bracketsIntroTest() throws SyntaxException {
        String str = "A ^ C\n\n(A ^ B) -> C";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str,rules);

//        System.out.println(proof.solveProof());
//        assertTrue(proof.solveProof().toString().equals("[A AND C, A AND B, A, C, A AND B IMPLIES C]"));
    }

    @Test
    public void simpleOnlyIntroSolverTest() throws SyntaxException {
        String str = "A -> B\nB -> A\n\nA <-> B";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, B IMPLIES A, A ONLY B]"));
    }


    @Test
    public void notNotImpliesTest() throws SyntaxException {
        String str = "!!A\nA -> B\n\nB";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[NOT NOT A, A IMPLIES B, A, B]"));
    }

    @Test
    public void falseTest() throws SyntaxException {
        String str = "A -> B\nA\n!B\n\nFALSE";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, A, NOT B, B, FALSE]"));

    }

    @Test
    public void hintTest2() throws SyntaxException {
        String str = "S -> B\nB -> W\n!W\n\n!S";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "S -> B\nB -> W\n!W\nS\n\n!S";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "S -> B\nB -> W\n!W\nS\nB\n\n!S";
        rules +="\nImplies-Elim (1,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "S -> B\nB -> W\n!W\nS\nB\nW\n\n!S";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: NOT_ELIM"));

        str = "S -> B\nB -> W\n!W\nS\nB\nW\nFALSE\n!S";
        rules +="\nNot-Elim (3,6)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: NOT_INTRO"));

        rules +="\nNot-Intro (4,7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }
//
    @Test
    public void hintTest3() throws SyntaxException {
        String str = "A | B\nA -> C\nB -> C\n\nC";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "A | B\nA -> C\nB -> C\nA\n\nC";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "A | B\nA -> C\nB -> C\nA\nC\n\nC";
        rules +="\nImplies-Elim (2,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "A | B\nA -> C\nB -> C\nA\nC\nB\n\nC";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "A | B\nA -> C\nB -> C\nA\nC\nB\nC\nC";
        rules +="\nImplies-Elim (3,6)";
        proof.frontEndFunctionality(str, rules);

//                 System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: OR_ELIM"));

        rules +="\nOr-Elim (1,4,5,6,7)";
        proof.frontEndFunctionality(str, rules);

//                         System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest5() throws SyntaxException {
        String str = "!!A\nA -> B\n\nB";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: DOUBLE_NOT_ELIM"));

        str = "!!A\nA -> B\nA\nB";
        rules +="\nDoubleNot-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//                        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        rules +="\nImplies-Elim (2,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest6() throws SyntaxException {
        String str = "A -> B\nB -> A\nA <-> B";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ONLY_INTRO"));

        rules +="\nOnly-Intro (1,2)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest7() throws SyntaxException {
        String str = "A | B ^ D\nA -> B\n\nB";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        str = "A | B ^ D\nA -> B\nA | B\n\nB";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        str = "A | B ^ D\nA -> B\nA | B\nD\n\nB";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\n\nB";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\nB\n\nB";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\nB\nB ^ D\n\nB";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\nB\nB ^ D\nB\nB";
        rules +="\nAnd-Elim (7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: OR_ELIM"));

        rules +="\nOr-Elim (1,5,6,7,8)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest8() throws SyntaxException {
        String str = "A ^ (B -> D)\nB\n\nD";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        str = "A ^ (B -> D)\nB\nB -> D\nD";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        rules +="\nImplies-Elim (2,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest9() throws SyntaxException {
        String str = "!(A | B)\n\n!A";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "!(A | B)\nA\n\n!A";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: OR_INTRO"));

        str = "!(A | B)\nA\nA | B\n\n!A";
        rules +="\nOr-Intro (2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(false));
        assertTrue(proof.generateHint().equals("Hint: NOT_ELIM"));

        str = "!(A | B)\nA\nA | B\nFALSE\n!A";
        rules +="\nNot-Elim (1,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(false));
        assertTrue(proof.generateHint().equals("Hint: NOT_INTRO"));

        rules +="\nNot-Intro (2,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(false));
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest11() throws SyntaxException {
        String str = "A -> (B -> C)\n\nA ^ B -> C";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "A -> (B -> C)\nA ^ B\n\nA ^ B -> C";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        str = "A -> (B -> C)\nA ^ B\nA\n\nA ^ B -> C";
        rules +="\nAnd-Elim (2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "A -> (B -> C)\nA ^ B\nA\nB -> C\n\nA ^ B -> C";
        rules +="\nImplies-Elim (1,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        str = "A -> (B -> C)\nA ^ B\nA\nB -> C\nB\n\nA ^ B -> C";
        rules +="\nAnd-Elim (2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "A -> (B -> C)\nA ^ B\nA\nB -> C\nB\nC\nA ^ B -> C";
        rules +="\nImplies-Elim (4,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_INTRO"));

        rules +="\nImplies-Intro (2,6)";
        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest12() throws SyntaxException {
        String str = "A ^ (B | C)\nB -> D\nC -> D\n\nD | E";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\n\nD | E";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\n\nD | E";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\n\nD | E";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nC\n\nD | E";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nC\nD\n\nD | E";
        rules +="\nImplies-Elim (3,7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: OR_ELIM"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nC\nD\nD\nD | E";
        rules +="\nOr-Elim (4,5,6,7,8)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: OR_INTRO"));

        rules +="\nOr-Intro (9)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest13() throws SyntaxException {
        String str = "B ^ E\nE -> C\n\nA -> (B -> C)";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "B ^ E\nE -> C\nA\n\nA -> (B -> C)";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "B ^ E\nE -> C\nA\nB\n\nA -> (B -> C)";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        str = "B ^ E\nE -> C\nA\nB\nE\n\nA -> (B -> C)";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "B ^ E\nE -> C\nA\nB\nE\nC\n\nA -> (B -> C)";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_INTRO"));

        str = "B ^ E\nE -> C\nA\nB\nE\nC\nB -> C\nA -> (B -> C)";
        rules +="\nImplies-Intro (4,6)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_INTRO"));

        rules +="\nImplies-Intro (3,7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }


    //TODO: fix this case
    @Test
    public void hintTest14() throws SyntaxException {
        String str = "A\nA -> B\nB -> C\n\n(A ^ B) ^ C";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
//        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

    }

    @Test
    public void hintTest15() throws SyntaxException {
        String str = "\nA -> A | C";

        proof.frontEndFunctionality(str,"");

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: ASSUMPTION"));

        str = "A\n\nA -> A | C";
        String rules ="ASSUMPTION";
        proof.frontEndFunctionality(str, rules);

        //        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: OR_INTRO"));

        str = "A\nA | C\nA -> A | C";
        rules +="\nOr-Intro (1)";
        proof.frontEndFunctionality(str, rules);

        //        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_INTRO"));

        rules +="\nImplies-Intro (1,2)";
        proof.frontEndFunctionality(str, rules);

        //        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Ignore
    @Test
    public void multipleGoalTest1() throws SyntaxException {

        String str = "A ^ B\nC\nD\n...\nB ^ C\n\nD -> B ^ C";
        String rules = "GIVEN\nGIVEN\nASSUMPTION\n\n\nImplies-Intro (3,5)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A AND B, C, D, B, B AND C, D IMPLIES B AND C]"));

    }

    @Ignore
    @Test
    public void multipleGoalTest2() throws SyntaxException {

        String str = "A ^ B\nD -> E\nC\nD\n...\nB ^ C ^ E\n\nD -> B ^ C ^ E";
        String rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\nEMPTY\n\nImplies-Elim (3,5)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString()
                .equals("[A AND B, D IMPLIES E, C, D, E, B, C AND E, B AND C AND E, D IMPLIES B AND C AND E]"));
    }

    @Ignore
    @Test
    public void multipleGoalHintTest1() throws SyntaxException {

        String str = "A ^ B\nC\nD\n...\nB ^ C\n\nD -> B ^ C";

        String rules = "GIVEN\nGIVEN\nASSUMPTION\n\n\nImplies-Intro (3,5)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        String str1 = "A ^ B\nC\nD\nB\nB ^ C\n\nD -> B ^ C";
        String rules1 = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)\n\nImplies-Intro (3,5)";

        proof.frontEndFunctionality(str1, rules1);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_INTRO"));

        String rules2 = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)\nAnd-Intro (2,4)\nImplies-Intro (3,5)";

        proof.frontEndFunctionality(str1, rules2);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Ignore
    @Test
    public void multipleGoalHintTest2() throws SyntaxException {


        String str = "A ^ B\nD -> E\nC\nD\n...\n\nB ^ C ^ E";
        String rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\n\n";

        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

        str = "A ^ B\nD -> E\nC\nD\nE\n...\nB ^ C ^ E";
        rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,4)\n";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        str = "A ^ B\nD -> E\nC\nD\nE\nB\n\nB ^ C ^ E";
        rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,4)\nAnd-Elim (1)\n";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_INTRO"));

        str = "A ^ B\nD -> E\nC\nD\nE\nB\nC ^ E\nB ^ C ^ E";
        rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,4)\nAnd-Elim (1)\nAnd-Intro (3,5)\n";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_INTRO"));

        rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,4)\nAnd-Elim (1)\nAnd-Intro (3,5)\nAnd-Intro (2,3)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_INTRO"));
//
        str += "D -> B ^ C ^ E";
        rules += "\nImplies-Intro (4,8)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void multiComboTest1() throws SyntaxException {

        String str = "A ^ B\nA -> E\n\nB | E";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_ELIM"));

        str = "A ^ B\nA -> E\nB\nB | E";
        rules += "\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: OR_INTRO"));
    }

    @Test
    public void multiComboTest2() throws SyntaxException {

        String str = "A\nB\nA -> B\nA ^ B";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: AND_INTRO"));

        rules += "\nAnd-Intro (1,2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Ignore
    @Test
    public void multipleGoalHintTest3() throws SyntaxException {

        String str = "!(A | B)\nA\n...\nFALSE\n\n!A";
        String rules = "GIVEN\nASSUMPTION\n\n\nNot-Intro (2,4)";


        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: OR_INTRO"));

        str = "!(A | B)\nA\nA | B\nFALSE\n\n!A";
        rules = "GIVEN\nASSUMPTION\nOr-Intro (2)\n\nNot-Intro (2,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: NOT_ELIM"));

        rules = "GIVEN\nASSUMPTION\nOr-Intro (2)\nNot-Elim (1,3)\nNot-Intro (2,4)";
        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Ignore
    @Test
    public void multipleGoalHintTest4() throws SyntaxException {

        String str = "A | B\nA -> D\nB -> D\nD -> E\nA\n...\nE\nB\n...\nE";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nASSUMPTION\n\n\nASSUMPTION\n\n";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: IMPLIES_ELIM"));

    }

    @Test
    public void newLineTest() throws SyntaxException {

        String str = "A -> B\nB -> C\nA\n\nC";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, B IMPLIES C, A, B, C]"));

    }


}
