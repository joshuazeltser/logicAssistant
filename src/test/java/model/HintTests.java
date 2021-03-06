package model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 14/04/2017.
 */
public class HintTests {

    Proof proof = new Proof();


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

//        System.out.println(proof.solveProof());

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

//        System.out.println(proof.solveProof());
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

//        System.out.println(proof.solveProof());
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

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[NOT A, A AND B, A, FALSE]"));
    }

    @Test
    public void bracketsIntroTest() throws SyntaxException {
        String str = "A ^ C\n\n(A ^ B) -> C";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str,rules);

        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A AND C, A AND B, A, C, B, OPEN A AND B CLOSE IMPLIES C]"));
    }

    @Test
    public void simpleOnlyIntroSolverTest() throws SyntaxException {
        String str = "A -> B\nB -> A\n\nA <-> B";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);


//        System.out.println(proof.solveProof());
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
    public void hintTest1() throws SyntaxException {
        String str = "\nA | !A";

        proof.frontEndFunctionality(str, "");

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "!(A | !A)\n\nA | !A";
        String rules = "ASSUMPTION\n";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "!(A | !A)\nA\n\nA | !A";
        rules = "ASSUMPTION\nASSUMPTION";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));

        str = "!(A | !A)\nA\nA | !A\n\nA | !A";
        rules = "ASSUMPTION\nASSUMPTION\nOr-Intro (2)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Elimination"));

        str = "!(A | !A)\nA\nA | !A\nFALSE\n\nA | !A";
        rules = "ASSUMPTION\nASSUMPTION\nOr-Intro (2)\nNot-Elim (1,3)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Introduction"));

        str = "!(A | !A)\nA\nA | !A\nFALSE\n!A\n\nA | !A";
        rules = "ASSUMPTION\nASSUMPTION\nOr-Intro (2)\nNot-Elim (1,3)\nNot-Intro (2,4)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));

        str = "!(A | !A)\nA\nA | !A\nFALSE\n!A\nA | !A\n\nA | !A";
        rules = "ASSUMPTION\nASSUMPTION\nOr-Intro (2)\nNot-Elim (1,3)\nNot-Intro (2,4)\nOr-Intro (5)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Elimination"));

        str = "!(A | !A)\nA\nA | !A\nFALSE\n!A\nA | !A\nFALSE\n\nA | !A";
        rules = "ASSUMPTION\nASSUMPTION\nOr-Intro (2)\nNot-Elim (1,3)\nNot-Intro (2,4)\nOr-Intro (5)\nNot-Elim (1,6)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Introduction"));

        str = "!(A | !A)\nA\nA | !A\nFALSE\n!A\nA | !A\nFALSE\n!!(A | !A)\nA | !A";
        rules = "ASSUMPTION\nASSUMPTION\nOr-Intro (2)\nNot-Elim (1,3)\nNot-Intro (2,4)\nOr-Intro (5)\nNot-Elim (1,6)" +
                "\nNot-Intro (1,7)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Not Elimination"));

        str = "!(A | !A)\nA\nA | !A\nFALSE\n!A\nA | !A\nFALSE\n!!(A | !A)\nA | !A";
        rules = "ASSUMPTION\nASSUMPTION\nOr-Intro (2)\nNot-Elim (1,3)\nNot-Intro (2,4)\nOr-Intro (5)\nNot-Elim (1,6)" +
                "\nNot-Intro (1,7)\nDoubleNot-Elim (8)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }



    @Test
    public void hintTest4() throws SyntaxException {
        String str = "\nA";

        proof.frontEndFunctionality(str, "");

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Cannot generate hint for this proof"));
    }

    @Test
    public void hintTest2() throws SyntaxException {
        String str = "S -> B\nB -> W\n!W\n\n!S";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "S -> B\nB -> W\n!W\nS\n\n!S";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "S -> B\nB -> W\n!W\nS\nB\n\n!S";
        rules +="\nImplies-Elim (1,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "S -> B\nB -> W\n!W\nS\nB\nW\n\n!S";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Elimination"));

        str = "S -> B\nB -> W\n!W\nS\nB\nW\nFALSE\n!S";
        rules +="\nNot-Elim (3,6)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Introduction"));

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
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A | B\nA -> C\nB -> C\nA\n\nC";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A | B\nA -> C\nB -> C\nA\nC\n\nC";
        rules +="\nImplies-Elim (2,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A | B\nA -> C\nB -> C\nA\nC\nB\n\nC";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A | B\nA -> C\nB -> C\nA\nC\nB\nC\nC";
        rules +="\nImplies-Elim (3,6)";
        proof.frontEndFunctionality(str, rules);

//                 System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Elimination"));

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
        assertTrue(proof.generateHint().equals("Hint: Not Not Elimination"));

        str = "!!A\nA -> B\nA\nB";
        rules +="\nDoubleNot-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//                        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

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
        assertTrue(proof.generateHint().equals("Hint: Only Introduction"));

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
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A | B ^ D\nA -> B\nA | B\n\nB";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A | B ^ D\nA -> B\nA | B\nD\n\nB";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\n\nB";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\nB\n\nB";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\nB\nB ^ D\n\nB";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\nB\nB ^ D\nB\nB";
        rules +="\nAnd-Elim (7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Elimination"));

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
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A ^ (B -> D)\nB\nB -> D\nD";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

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
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "!(A | B)\nA\n\n!A";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));

        str = "!(A | B)\nA\nA | B\n\n!A";
        rules +="\nOr-Intro (2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Elimination"));

        str = "!(A | B)\nA\nA | B\nFALSE\n!A";
        rules +="\nNot-Elim (1,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Introduction"));

        rules +="\nNot-Intro (2,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint(false));
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest10() throws SyntaxException {
        String str = "A ^ B\nC\n\nD -> (B ^ C)";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A ^ B\nC\nD\n\nD -> (B ^ C)";
        rules = "GIVEN\nGIVEN\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A ^ B\nC\nD\nB\n\nD -> (B ^ C)";
        rules = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Introduction"));

        str = "A ^ B\nC\nD\nB\nB ^ C\n\nD -> (B ^ C)";
        rules = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)\nAnd-Intro (2,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction"));

        str = "A ^ B\nC\nD\nB\nB ^ C\nD -> (B ^ C)";
        rules = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)\nAnd-Intro (2,4)\nImplies-Intro (3,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest11() throws SyntaxException {
        String str = "A -> (B -> C)\n\nA ^ B -> C";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A -> (B -> C)\nA ^ B\n\nA ^ B -> C";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A -> (B -> C)\nA ^ B\nA\n\nA ^ B -> C";
        rules +="\nAnd-Elim (2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A -> (B -> C)\nA ^ B\nA\nB -> C\n\nA ^ B -> C";
        rules +="\nImplies-Elim (1,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A -> (B -> C)\nA ^ B\nA\nB -> C\nB\n\nA ^ B -> C";
        rules +="\nAnd-Elim (2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A -> (B -> C)\nA ^ B\nA\nB -> C\nB\nC\nA ^ B -> C";
        rules +="\nImplies-Elim (4,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction"));

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
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\n\nD | E";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\n\nD | E";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\n\nD | E";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nD | E\n\nD | E";
        rules +="\nOr-Intro (6)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nD | E\nC\n\nD | E";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nD | E\nC\nD\n\nD | E";
        rules +="\nImplies-Elim (3,8)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nD | E\nC\nD\nD | E\nD | E";
        rules +="\nOr-Intro (9)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Elimination"));

        rules +="\nOr-Elim (4,5,7,8,10)";

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
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "B ^ E\nE -> C\nA\n\nA -> (B -> C)";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "B ^ E\nE -> C\nA\nB\n\nA -> (B -> C)";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "B ^ E\nE -> C\nA\nB\nE\n\nA -> (B -> C)";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "B ^ E\nE -> C\nA\nB\nE\nC\n\nA -> (B -> C)";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction"));

        str = "B ^ E\nE -> C\nA\nB\nE\nC\nB -> C\nA -> (B -> C)";
        rules +="\nImplies-Intro (4,6)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction"));

        rules +="\nImplies-Intro (3,7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }


    @Test
    public void hintTest14() throws SyntaxException {
        String str = "A\nA -> B\nB -> C\n\n(A ^ B) ^ C";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A\nA -> B\nB -> C\nB\n\n(A ^ B) ^ C";
        rules = "GIVEN\nGIVEN\nGIVEN\nImplies-Elim (1,2)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A\nA -> B\nB -> C\nB\nC\n\n(A ^ B) ^ C";
        rules = "GIVEN\nGIVEN\nGIVEN\nImplies-Elim (1,2)\nImplies-Elim (3,4)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Introduction"));

        str = "A\nA -> B\nB -> C\nB\nC\nA ^ B\n\n(A ^ B) ^ C";
        rules = "GIVEN\nGIVEN\nGIVEN\nImplies-Elim (1,2)\nImplies-Elim (3,4)\nAnd-Intro (1,4)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Introduction"));

        str = "A\nA -> B\nB -> C\nB\nC\nA ^ B\n(A ^ B) ^ C";
        rules = "GIVEN\nGIVEN\nGIVEN\nImplies-Elim (1,2)\nImplies-Elim (3,4)\nAnd-Intro (1,4)\nAnd-Intro (5,6)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest15() throws SyntaxException {
        String str = "\nA -> A | C";

        proof.frontEndFunctionality(str,"");

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A\n\nA -> A | C";
        String rules ="ASSUMPTION\n\n";
        proof.frontEndFunctionality(str, rules);

//      System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));

        str = "A\nA | C\nA -> A | C";
        rules = "ASSUMPTION\nOr-Intro (1)\n";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction"));

        rules +="Implies-Intro (1,2)";
        proof.frontEndFunctionality(str, rules);

        //        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest16() throws SyntaxException {
        String str = "(A ^ B) ^ C\n\nB | D";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "(A ^ B) ^ C\nA ^ B\n\nB | D";
        rules = "GIVEN\nAnd-Elim (1)";

        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "(A ^ B) ^ C\nA ^ B\nB\n\nB | D";
        rules = "GIVEN\nAnd-Elim (1)\nAnd-Elim (2)";

        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "(A ^ B) ^ C\nA ^ B\nB\nC\n\nB | D";
        rules = "GIVEN\nAnd-Elim (1)\nAnd-Elim (2)\nAnd-Elim (1)";

        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));

        str = "(A ^ B) ^ C\nA ^ B\nB\nC\nB | D";
        rules = "GIVEN\nAnd-Elim (1)\nAnd-Elim (2)\nAnd-Elim (1)\nOr-Intro (3)";

        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));


    }



    @Test
    public void multipleGoalTest1() throws SyntaxException {

        String str = "A ^ B\nC\nD\n\nB ^ C\n\nD -> B ^ C";
        String rules = "GIVEN\nGIVEN\nASSUMPTION\n\n\nImplies-Intro (3,5)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A AND B, C, D, B, B AND C, D IMPLIES B AND C]"));

    }


    @Test
    public void multipleGoalTest2() throws SyntaxException {

        String str = "A ^ B\nD -> E\nC\nD\n\nB ^ C ^ E\n\nD -> B ^ C ^ E";
        String rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\n\nImplies-Elim (3,5)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString()
                .equals("[A AND B, D IMPLIES E, C, D, E, B, C AND E, B AND C AND E, D IMPLIES B AND C AND E]"));
    }


    @Test
    public void multipleGoalHintTest1() throws SyntaxException {

        String str = "A ^ B\nC\nD\n\nB ^ C\nD -> B ^ C";

        String rules = "GIVEN\nGIVEN\nASSUMPTION";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        String str1 = "A ^ B\nC\nD\nB\nB ^ C\nD -> B ^ C";
        String rules1 = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)";

        proof.frontEndFunctionality(str1, rules1);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Introduction"));
//
        String rules2 = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)\nAnd-Intro (4,2)";

        proof.frontEndFunctionality(str1, rules2);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction"));

    }

    @Test
    public void multipleGoalHintTest2() throws SyntaxException {


        String str = "A ^ B\nD -> E\nC\nD\n\nB ^ C ^ E\nD -> B ^ C ^ E";
        String rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION";

        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A ^ B\nD -> E\nC\nD\nE\n\nB ^ C ^ E\nD -> B ^ C ^ E";
        rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,4)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A ^ B\nD -> E\nC\nD\nE\nB\n\nB ^ C ^ E\nD -> B ^ C ^ E";
        rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,4)\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Introduction"));

        str = "A ^ B\nD -> E\nC\nD\nE\nB\nC ^ E\nB ^ C ^ E\nD -> B ^ C ^ E";
        rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,4)\nAnd-Elim (1)\nAnd-Intro (3,5)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Introduction"));

        rules = "GIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,4)\nAnd-Elim (1)\nAnd-Intro (3,5)\nAnd-Intro (6,7)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction"));
//
        rules += "\nImplies-Intro (4,8)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void multipleGoalHintTest3() throws SyntaxException {

        String str = "!(A | B)\nA\n\nFALSE\n!A";
        String rules = "GIVEN\nASSUMPTION";


        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));

        str = "!(A | B)\nA\nA | B\nFALSE\n!A";
        rules = "GIVEN\nASSUMPTION\nOr-Intro (2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Elimination"));

        rules = "GIVEN\nASSUMPTION\nOr-Intro (2)\nNot-Elim (1,3)\n";
        proof.frontEndFunctionality(str, rules);

        //        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Introduction"));

        rules = "GIVEN\nASSUMPTION\nOr-Intro (2)\nNot-Elim (1,3)\nNot-Intro (2,4)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void multipleGoalHintTest4() throws SyntaxException {

        String str = "A | B\nA -> D\nB -> D\nD -> E\nA\n\nE\nB\n\nE\n\nE";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nASSUMPTION\n\nASSUMPTION";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A | B\nA -> D\nB -> D\nD -> E\nA\nD\nE\nB\n\nE\n\nE";
        rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,5)\n\nASSUMPTION";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,5)\nImplies-Elim (4,6)\nASSUMPTION";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A | B\nA -> D\nB -> D\nD -> E\nA\nD\nE\nB\nD\nE\nE";
        rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,5)\nImplies-Elim (4,6)\nASSUMPTION" +
                "\nImplies-Elim (3,8)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,5)\nImplies-Elim (4,6)\nASSUMPTION" +
                "\nImplies-Elim (3,8)\nImplies-Elim (4,9)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Elimination"));

        rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nASSUMPTION\nImplies-Elim (2,5)\nImplies-Elim (4,6)\nASSUMPTION" +
                "\nImplies-Elim (3,8)\nImplies-Elim (4,9)\nOr-Elim (1,5,7,8,10)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));


    }

    @Test
    public void multipleGoalHintTest5() throws SyntaxException {
        String str = "B ^ E\nE -> C\nA\nB\n\nB -> C\nA -> (B -> C)";
        String rules = "GIVEN\nGIVEN\nASSUMPTION\nASSUMPTION";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "B ^ E\nE -> C\nA\nB\nE\n\nB -> C\nA -> (B -> C)";
        rules = "GIVEN\nGIVEN\nASSUMPTION\nASSUMPTION\nAnd-Elim (1)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));
//
        str = "B ^ E\nE -> C\nA\nB\nE\nC\nB -> C\nA -> (B -> C)";
        rules = "GIVEN\nGIVEN\nASSUMPTION\nASSUMPTION\nAnd-Elim (1)\nImplies-Elim (2,5)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction"));
////
        rules = "GIVEN\nGIVEN\nASSUMPTION\nASSUMPTION\nAnd-Elim (1)\nImplies-Elim (2,5)\nImplies-Intro (4,6)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction"));

        rules = "GIVEN\nGIVEN\nASSUMPTION\nASSUMPTION\nAnd-Elim (1)\nImplies-Elim (2,5)\nImplies-Intro (4,6)" +
                "\nImplies-Intro (3,7)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Test
    public void newLineTest() throws SyntaxException {

        String str = "A -> B\nB -> C\nA\n\nC";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, B IMPLIES C, A, B, C]"));

    }

    @Test
    public void multiCombinationTest1() throws SyntaxException {

        String str = "A ^ B\nA -> E\nA\n\nE | B";
        String rules = "GIVEN\nGIVEN\nAnd-Elim (1)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A ^ B\nA -> E\nB\nE | B";
        rules = "GIVEN\nGIVEN\nAnd-Elim (1)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));

    }

    @Test
    public void multiCombinationTest2() throws SyntaxException {

        String str = "A\nA -> B\nD ^ E\nB -> E\n\nE | Z";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A\nA -> B\nD ^ E\nB -> E\nE\n\nE | Z";
        rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nAnd-Elim (3)";

        proof.frontEndFunctionality(str, rules);
//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));
    }

    @Test
    public void multiCombinationTest3() throws SyntaxException {

        String str = "A ^ B\nA -> E\nE -> B\nD\n\nB ^ D";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A ^ B\nA -> E\nE -> B\nD\nA\n\nB ^ D";
        rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nAnd-Elim (1)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination"));

        str = "A ^ B\nA -> E\nE -> B\nD\nB\n\nB ^ D";
        rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nAnd-Elim (1)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Introduction"));

    }



    @Test
    public void advancedHintTest1() throws SyntaxException {

        proof.setAdvancedHints(true);

        String str = "!!A\nA -> B\n\nB";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Not Elimination, using lines: 1"));

        str = "!!A\nA -> B\nA\nB";
        rules +="\nDoubleNot-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//                        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination, using lines: 2, 3"));

        rules +="\nImplies-Elim (2,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Test
    public void advancedHintTest2() throws SyntaxException {

        proof.setAdvancedHints(true);

        String str = "B ^ E\nE -> C\n\nA -> (B -> C)";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "B ^ E\nE -> C\nA\n\nA -> (B -> C)";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "B ^ E\nE -> C\nA\nB\n\nA -> (B -> C)";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination, using lines: 1"));

        str = "B ^ E\nE -> C\nA\nB\nE\n\nA -> (B -> C)";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination, using lines: 2, 5"));

        str = "B ^ E\nE -> C\nA\nB\nE\nC\n\nA -> (B -> C)";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction, using lines: 4, 6"));

        str = "B ^ E\nE -> C\nA\nB\nE\nC\nB -> C\nA -> (B -> C)";
        rules +="\nImplies-Intro (4,6)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction, using lines: 3, 7"));

        rules +="\nImplies-Intro (3,7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void advancedHintTest3() throws SyntaxException {
        proof.setAdvancedHints(true);

        String str = "!(A | B)\n\n!A";
        String rules = "GIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "!(A | B)\nA\n\n!A";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction, using lines: 2"));

        str = "!(A | B)\nA\nA | B\n\n!A";
        rules +="\nOr-Intro (2)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Elimination, using lines: 1, 3"));

        str = "!(A | B)\nA\nA | B\nFALSE\n!A";
        rules +="\nNot-Elim (1,3)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Not Introduction, using lines: 2, 4"));

        rules +="\nNot-Intro (2,4)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void advancedHintTest4() throws SyntaxException {

        proof.setAdvancedHints(true);

        String str = "A ^ (B | C)\nB -> D\nC -> D\n\nD | E";
        String rules = "GIVEN\nGIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination, using lines: 1"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\n\nD | E";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\n\nD | E";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination, using lines: 2, 5"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\n\nD | E";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
//        assertTrue(proof.generateHint().contains("Hint: Or Introduction, using lines: 6"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nD | E\n\nD | E";
        rules +="\nOr-Intro (6)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nD | E\nC\n\nD | E";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination, using lines: 3, 8"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nD | E\nC\nD\n\nD | E";
        rules +="\nImplies-Elim (3,8)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
//        assertTrue(proof.generateHint().contains("Hint: Or Introduction, using lines: 9"));

        str = "A ^ (B | C)\nB -> D\nC -> D\nB | C\nB\nD\nD | E\nC\nD\nD | E\nD | E";
        rules +="\nOr-Intro (9)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Elimination, using lines: 4, 5, 7, 8, 10"));

        rules +="\nOr-Elim (4,5,7,8,10)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void advancedHintTest5() throws SyntaxException {

        proof.setAdvancedHints(true);

        String str = "A -> B\nB -> A\nA <-> B";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Only Introduction, using lines: 1, 2"));

        rules +="\nOnly-Intro (1,2)";
        proof.frontEndFunctionality(str, rules);

//                System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));
    }

    @Test
    public void advancedHintTest6() throws SyntaxException {

        proof.setAdvancedHints(true);

        String str = "A ^ B\nC\nD\n\nB ^ C\nD -> B ^ C";

        String rules = "GIVEN\nGIVEN\nASSUMPTION";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination, using lines: 1"));

        String str1 = "A ^ B\nC\nD\nB\nB ^ C\nD -> B ^ C";
        String rules1 = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)";

        proof.frontEndFunctionality(str1, rules1);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Introduction, using lines: 4, 2"));
//
        String rules2 = "GIVEN\nGIVEN\nASSUMPTION\nAnd-Elim (1)\nAnd-Intro (4,2)";

        proof.frontEndFunctionality(str1, rules2);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Introduction, using lines: 3, 5"));

    }

    @Test
    public void advancedHintTest7() throws SyntaxException {

        proof.setAdvancedHints(true);

        String str = "A | B ^ D\nA -> B\n\nB";
        String rules = "GIVEN\nGIVEN";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination, using lines: 1"));

        str = "A | B ^ D\nA -> B\nA | B\n\nB";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination, using lines: 1"));

        str = "A | B ^ D\nA -> B\nA | B\nD\n\nB";
        rules +="\nAnd-Elim (1)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\n\nB";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Implies Elimination, using lines: 2, 5"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\nB\n\nB";
        rules +="\nImplies-Elim (2,5)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\nB\nB ^ D\n\nB";
        rules +="\nASSUMPTION";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination, using lines: 7"));

        str = "A | B ^ D\nA -> B\nA | B\nD\nA\nB\nB ^ D\nB\nB";
        rules +="\nAnd-Elim (7)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Elimination, using lines: 1, 5, 6, 7, 8"));

        rules +="\nOr-Elim (1,5,6,7,8)";
        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Proof already successfully solved"));

    }

    @Test
    public void lemmaTest1() throws SyntaxException {

        String str = "A | !A\n\nA";
        String rules = "Lemma1 ()";


        proof.frontEndFunctionality(str, rules);

        assertTrue(proof.isProofValid());

    }

    @Test
    public void lemmaTest2() throws SyntaxException {


        String str = "D\nA\nB\nA ^ B -> C\nC\n\nC ^ D";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nLemma1 (2,3,4)";

        proof.frontEndFunctionality(str, rules);


        assertTrue(proof.isProofValid());

    }

    @Test
    public void lemmaTest3() throws SyntaxException {


        String str = "E\nA\nB\nA ^ B -> C\nC\nD ^ A ^ B\n";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nLemma1 (2,3,4)\nLemma2 (1)";

        proof.frontEndFunctionality(str, rules);

       assertFalse(proof.isProofValid());

    }

    @Test
    public void lemmaTest4() throws SyntaxException {


        String str = "D\nE ^ F\nA\nB\nC\nA ^ B ^ C\nX\nZ\nE\nF\nD ^ F ^ E\nD ^ X ^ Z";
        String rules = "GIVEN\nGIVEN\nGIVEN\nGIVEN\nGIVEN\nLemma2 (3,4,5)\nGIVEN\nGIVEN\nAnd-Elim (2)\n" +
                "And-Elim (2)\nLemma2 (1,9,10)\nLemma2 (1,7,8)";

        proof.frontEndFunctionality(str, rules);


        assertTrue(proof.isProofValid());

    }

    @Test
    public void hintTest17() throws SyntaxException {


        String str = "A ∧ (B ∨ C)\n\n(A ∧ B) ∨ (A ∧ C)";
        String rules = "GIVEN\n";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A ∧ (B ∨ C)\nA\n\n(A ∧ B) ∨ (A ∧ C)";
        rules = "GIVEN\nAnd-Elim (1)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Elimination"));

        str = "A ∧ (B ∨ C)\nA\nB | C\n\n(A ∧ B) ∨ (A ∧ C)";
        rules = "GIVEN\nAnd-Elim (1)\nAnd-Elim (1)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A ∧ (B ∨ C)\nA\nB | C\nB\n\n(A ∧ B) ∨ (A ∧ C)";
        rules = "GIVEN\nAnd-Elim (1)\nAnd-Elim (1)\nASSUMPTION";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Introduction"));

        str = "A ∧ (B ∨ C)\nA\nB | C\nB\nA ^ B\n\n(A ∧ B) ∨ (A ∧ C)";
        rules = "GIVEN\nAnd-Elim (1)\nAnd-Elim (1)\nASSUMPTION\nAnd-Intro (2,4)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));

        str = "A ∧ (B ∨ C)\nA\nB | C\nB\nA ^ B\nA ∧ B ∨ (A ∧ C)\n\n(A ∧ B) ∨ (A ∧ C)";
        rules = "GIVEN\nAnd-Elim (1)\nAnd-Elim (1)\nASSUMPTION\nAnd-Intro (2,4)\nOr-Intro (5)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Assumption"));

        str = "A ∧ (B ∨ C)\nA\nB | C\nB\nA ^ B\nA ∧ B ∨ (A ∧ C)\nC\n\n(A ∧ B) ∨ (A ∧ C)";
        rules = "GIVEN\nAnd-Elim (1)\nAnd-Elim (1)\nASSUMPTION\nAnd-Intro (2,4)\nOr-Intro (5)\nASSUMPTION";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: And Introduction"));

        str = "A ∧ (B ∨ C)\nA\nB | C\nB\nA ^ B\nA ∧ B ∨ (A ∧ C)\nC\nA ^ C\n\n(A ∧ B) ∨ (A ∧ C)";
        rules = "GIVEN\nAnd-Elim (1)\nAnd-Elim (1)\nASSUMPTION\nAnd-Intro (2,4)\nOr-Intro (5)\nASSUMPTION\n" +
                "And-Intro (2,7)";

        proof.frontEndFunctionality(str, rules);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint().equals("Hint: Or Introduction"));


        str = "A ∧ (B ∨ C)\nA\nB | C\nB\nA ^ B\nA ∧ B ∨ (A ∧ C)\nC\nA ^ C\nA ∧ B ∨ A ∧ C\n(A ∧ B) ∨ (A ∧ C)";
        rules = "GIVEN\nAnd-Elim (1)\nAnd-Elim (1)\nASSUMPTION\nAnd-Intro (2,4)\nOr-Intro (5)\nASSUMPTION\n" +
                "And-Intro (2,7)\nOr-Intro (8)";

        proof.frontEndFunctionality(str, rules);

    }

}
