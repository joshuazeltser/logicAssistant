package model;

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
        String str = "A ^ B";
        String str1 = "C";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

//        System.out.println(proof.solveProof());

//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[A AND B, C, A, B]"));
    }
//
    @Test
    public void impliesEliminationSolverTest() throws SyntaxException {
        String str = "A -> B";
        String str1 = "A";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);


//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, A, B]"));
    }

    @Test
    public void impliesAndEliminationSolverTest() throws SyntaxException {
        String str = "A ^ B";
        String str1 = "B -> C";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);



//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[A AND B, B IMPLIES C, A, B, C]"));
    }

//
    @Test
    public void onlyEliminationSolverTest() throws SyntaxException {
        String str = "A <-> B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);


        proof.addExpression(expr);

        String result = "B -> A";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A ONLY B, B IMPLIES A]"));
    }

//
    @Test
    public void notEliminationSolverTest() throws SyntaxException {
        String str = "!A";
        String str1 = "A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);


        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "FALSE";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);


//        System.out.println("result " + res);
        assertTrue(proof.solveProof().toString().equals("[NOT A, A, FALSE]"));
    }


    @Test
    public void simpleNotNotEliminationSolverTest() throws SyntaxException {
        String str = "!!A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        proof.addExpression(expr);

        String result = "A";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[NOT NOT A, A]"));
    }
//

//
    @Test
    public void simpleAndIntroSolverTest() throws SyntaxException {
        String str = "A";
        String str1 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "A ^ B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);


        assertTrue(proof.solveProof().toString().equals("[A, B, A AND B]"));
    }

    @Test
    public void harderAndIntroSolverTest() throws SyntaxException {
        String str = "A";
        String str1 = "A -> B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "A ^ B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A, A IMPLIES B, B, A AND B]"));
    }



    @Test
    public void largeAndIntroSolverTest() throws SyntaxException {
        String str = "A";
        String str1 = "B";
        String str2 = "C";
        String str3 = "D";
        String str4 = "E -> F";
        String str5 = "O";
        String str6 = "S";
        String str7 = "Y -> F";
        String str8 = "X";
        String str9 = "P";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.GIVEN);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.GIVEN);
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.GIVEN);
        expr5.addToExpression(str5);

        Expression expr6 = new Expression(RuleType.GIVEN);
        expr6.addToExpression(str6);

        Expression expr7 = new Expression(RuleType.GIVEN);
        expr7.addToExpression(str7);

        Expression expr8 = new Expression(RuleType.GIVEN);
        expr8.addToExpression(str8);

        Expression expr9 = new Expression(RuleType.GIVEN);
        expr9.addToExpression(str9);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);
        proof.addExpression(expr5);
        proof.addExpression(expr6);
        proof.addExpression(expr7);
        proof.addExpression(expr8);
        proof.addExpression(expr9);


        String result = "S ^ P";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A, B, C, D, E IMPLIES F, O, S, Y IMPLIES F, X, P, S AND P]"));
    }
//
    @Test
    public void iffImpliesAndSolverTest() throws SyntaxException {
        String str = "A -> B";
        String str1 = "B -> C";
        String str2 = "A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        String result = "C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, B IMPLIES C, A, B, C]"));
    }

    @Test
    public void fullProofTest() throws SyntaxException {

        String str = "!(A | B)";
        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        proof.addExpression(expr);

        String result = "!A";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[NOT OPEN A OR B CLOSE, A, A OR B, FALSE, NOT A]"));
    }


    @Test
    public void simpleOrIntroSolverTest() throws SyntaxException {
        String str = "A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        proof.addExpression(expr);

        String result = "A | B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A, A OR B]"));
    }

    @Test
    public void largeOrIntroSolverTest() throws SyntaxException {
        String str = "A";
        String str1 = "B";
        String str2 = "C";
        String str3 = "D";
        String str4 = "E -> F";
        String str5 = "O";
        String str6 = "S";
        String str7 = "Y -> F";
        String str8 = "X";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.GIVEN);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.GIVEN);
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.GIVEN);
        expr5.addToExpression(str5);

        Expression expr6 = new Expression(RuleType.GIVEN);
        expr6.addToExpression(str6);

        Expression expr7 = new Expression(RuleType.GIVEN);
        expr7.addToExpression(str7);

        Expression expr8 = new Expression(RuleType.GIVEN);
        expr8.addToExpression(str8);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);
        proof.addExpression(expr5);
        proof.addExpression(expr6);
        proof.addExpression(expr7);
        proof.addExpression(expr8);


        String result = "S | P";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A, B, C, D, E IMPLIES F, O, S, Y IMPLIES F, X, S OR P]"));
    }

    @Test
    public void OrImpliesAndSolverTest() throws SyntaxException {
        String str = "A ^ B";
        String str1 = "A -> C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "A ^ B | C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A AND B, A IMPLIES C, A, C, B, B OR C, A AND B OR C]"));
    }

    @Test
    public void simpleImpliesAtEndIntroSolverTest() throws SyntaxException {
        String str = "C ^ B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        proof.addExpression(expr);

        String result = "A -> B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[C AND B, A, C, B, A IMPLIES B]"));
    }

//
    @Test
    public void harderImpliesAtEndIntroSolverTest() throws SyntaxException {
        String str = "A -> C";
        String str1 = "C -> D";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "A -> D";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES C, C IMPLIES D, A, C, D, A IMPLIES D]"));
    }
//
    @Test
    public void orEliminationSolverTest() throws SyntaxException {
        String str = "A | B";
        String str1 = "A -> C";
        String str2 = "B -> C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        String result = "C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A OR B, A IMPLIES C, B IMPLIES C, A, C, B, C, C]"));
    }

    @Test
    public void orEliminationSolverTest2() throws SyntaxException {
        String str = "A | B ^ D";
        String str1 = "A -> B ";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A OR B AND D, A IMPLIES B, A OR B, D, A, B, B AND D, B, B]"));
    }
//
    @Test
    public void notIntroSolverTest() throws SyntaxException {
        String str = "S -> B";
        String str1 = "B -> W";
        String str2 = "!W";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);
        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        String result = "!S";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[S IMPLIES B, B IMPLIES W, NOT W, S, B, W, FALSE, NOT S]"));
    }

    @Test
    public void notElimolverTest() throws SyntaxException {
        String str = "!A";
        String str1 = "A ^ B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "FALSE";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[NOT A, A AND B, A, FALSE]"));
    }

    //TODO: to be fixed with brackets around A ^ B
    @Test
    public void bracketsIntroTest() throws SyntaxException {
        String str = "A ^ C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        proof.addExpression(expr);

        String result = "A ^ B -> C";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

//        System.out.println(proof.solveProof());
        assertTrue(proof.solveProof().toString().equals("[A AND C, A AND B, A, C, A AND B IMPLIES C]"));
    }

    @Test
    public void simpleOnlyIntroSolverTest() throws SyntaxException {
        String str = "A -> B";
        String str1 = "B -> A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "A <-> B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, B IMPLIES A, A ONLY B]"));
    }


    @Test
    public void simpleHintTest1() throws SyntaxException {
        String str = "!!A";
        String str1 = "A -> B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[NOT NOT A, A IMPLIES B, A, B]"));

    }

    @Test
    public void hintTest2() throws SyntaxException {
        String str = "S -> B";
        String str1 = "B -> W";
        String str2 = "!W";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);
        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        String result = "!S";
        proof.setResultString(result);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str3 = "S";
        Expression expr3 = new Expression(RuleType.ASSUMPTION);
        expr3.addToExpression(str3);
        proof.addExpression(expr3);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str4 = "B";
        Expression expr4 = new Expression(RuleType.IMPLIES_ELIM);
        expr4.addToExpression(str4);
        expr4.addReferenceLine("1");
        expr4.addReferenceLine("4");
        proof.addExpression(expr4);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str5 = "W";
        Expression expr5 = new Expression(RuleType.IMPLIES_ELIM);
        expr5.addToExpression(str5);
        expr5.addReferenceLine("2");
        expr5.addReferenceLine("5");
        proof.addExpression(expr5);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: NOT_ELIM"));

        String str6 = "FALSE";
        Expression expr6 = new Expression(RuleType.NOT_ELIM);
        expr6.addToExpression(str6);
        expr6.addReferenceLine("3");
        expr6.addReferenceLine("6");
        proof.addExpression(expr6);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: NOT_INTRO"));

        String str7 = "NOT S";
        Expression expr7 = new Expression(RuleType.NOT_INTRO);
        expr7.addToExpression(str7);
        expr7.addReferenceLine("4");
        expr7.addReferenceLine("7");
        proof.addExpression(expr7);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));

    }
//
    @Test
    public void hintTest3() throws SyntaxException {
        String str = "A | B";
        String str1 = "A -> C";
        String str2 = "B -> C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        String result = "C";
        proof.setResultString(result);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str3 = "A";
        Expression expr3 = new Expression(RuleType.ASSUMPTION);
        expr3.addToExpression(str3);
        proof.addExpression(expr3);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str4 = "C";
        Expression expr4 = new Expression(RuleType.IMPLIES_ELIM);
        expr4.addToExpression(str4);
        expr4.addReferenceLine("2");
        expr4.addReferenceLine("4");
        proof.addExpression(expr4);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str5 = "B";
        Expression expr5 = new Expression(RuleType.ASSUMPTION);
        expr5.addToExpression(str5);
        proof.addExpression(expr5);

//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str6 = "C";
        Expression expr6 = new Expression(RuleType.IMPLIES_ELIM);
        expr6.addToExpression(str6);
        expr6.addReferenceLine("3");
        expr6.addReferenceLine("6");
        proof.addExpression(expr6);

//                 System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_ELIM"));

        String str7 = "C";
        Expression expr7 = new Expression(RuleType.OR_ELIM);
        expr7.addToExpression(str7);
        expr7.addReferenceLine("1");
        expr7.addReferenceLine("4");
        expr7.addReferenceLine("5");
        expr7.addReferenceLine("6");
        expr7.addReferenceLine("7");
//        expr7.addReferenceLine("6");
        proof.addExpression(expr7);

//                         System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));

    }
//
    @Test
    public void hintTest4() throws SyntaxException {
        String str = "A -> B";
        String str1 = "A";
        String str2 = "!B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        String result = "FALSE";
        proof.setResultString(result);
        Expression res = new Expression();
        res.addToExpression(result);
        proof.setResultExpr(res);

        assertTrue(proof.solveProof().toString().equals("[A IMPLIES B, A, NOT B, B, FALSE]"));

    }


    @Test
    public void hintTest5() throws SyntaxException {
        String str = "!!A";
        String str1 = "A -> B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        proof.setResultString(result);
//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: DOUBLE_NOT_ELIM"));

        String str2 = "A";
        Expression expr2 = new Expression(RuleType.DOUBLE_NOT_ELIM);
        expr2.addToExpression(str2);
        expr2.addReferenceLine("1");
        proof.addExpression(expr2);

//                        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str3 = "B";
        Expression expr3 = new Expression(RuleType.IMPLIES_ELIM);
        expr3.addToExpression(str3);
        expr3.addReferenceLine("2");
        expr3.addReferenceLine("3");
        proof.addExpression(expr3);

//        System.out.println(proof.generateHint(false));
//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest6() throws SyntaxException {
        String str = "A -> B";
        String str1 = "B -> A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "A <-> B";
        proof.setResultString(result);

//        System.out.println(proof.generateHint());
        assertTrue(proof.generateHint(result).equals("Hint: ONLY_INTRO"));

        String str2 = "A <-> B";
        Expression expr2 = new Expression(RuleType.ONLY_INTRO);
        expr2.addToExpression(str2);
        expr2.addReferenceLine("1");
        expr2.addReferenceLine("2");
        proof.addExpression(expr2);


//                System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }

    @Test
    public void hintTest7() throws SyntaxException {
        String str = "A | B ^ D";
        String str1 = "A -> B ";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        proof.setResultString(result);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str2 = "A | B";
        Expression expr2 = new Expression(RuleType.AND_ELIM);
        expr2.addToExpression(str2);
        expr2.addReferenceLine("1");
        proof.addExpression(expr2);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str3 = "D";
        Expression expr3 = new Expression(RuleType.AND_ELIM);
        expr3.addToExpression(str3);
        expr3.addReferenceLine("1");
        proof.addExpression(expr3);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str4 = "A";
        Expression expr4 = new Expression(RuleType.ASSUMPTION);
        expr4.addToExpression(str4);
        proof.addExpression(expr4);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str5 = "B";
        Expression expr5 = new Expression(RuleType.IMPLIES_ELIM);
        expr5.addToExpression(str5);
        expr5.addReferenceLine("2");
        expr5.addReferenceLine("5");
        proof.addExpression(expr5);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str6 = "B ^ D";
        Expression expr6 = new Expression(RuleType.ASSUMPTION);
        expr6.addToExpression(str6);
        proof.addExpression(expr6);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str7 = "B";
        Expression expr7 = new Expression(RuleType.AND_ELIM);
        expr7.addToExpression(str7);
        expr7.addReferenceLine("7");
        proof.addExpression(expr7);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_ELIM"));

        String str8 = "B";
        Expression expr8 = new Expression(RuleType.OR_ELIM);
        expr8.addToExpression(str7);
        expr8.addReferenceLine("1");
        expr8.addReferenceLine("5");
        expr8.addReferenceLine("6");
        expr8.addReferenceLine("7");
        expr8.addReferenceLine("8");
        proof.addExpression(expr8);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));

    }

    @Test
    public void hintTest8() throws SyntaxException {
        String str = "A ^ (B -> D)";
        String str1 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);


        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "D";
        proof.setResultString(result);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str2 = "A";
        Expression expr2 = new Expression(RuleType.AND_ELIM);
        expr2.addToExpression(str2);
        expr2.addReferenceLine("1");
        proof.addExpression(expr2);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str4 = "B -> D";
        Expression expr4 = new Expression(RuleType.AND_ELIM);
        expr4.addToExpression(str4);
        expr4.addReferenceLine("1");
        proof.addExpression(expr4);

        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str3 = "D";
        Expression expr3 = new Expression(RuleType.IMPLIES_ELIM);
        expr3.addToExpression(str3);
        expr3.addReferenceLine("2");
        expr3.addReferenceLine("4");
        proof.addExpression(expr3);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }
////
    @Test
    //This test takes a lot of time due to OR_INTRO
    public void hintTest9() throws SyntaxException {
        String str = "!(A | B)";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        proof.addExpression(expr);

        String result = "!A";
        proof.setResultString(result);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str1 = "A";
        Expression expr1 = new Expression(RuleType.ASSUMPTION);
        expr1.addToExpression(str1);
        proof.addExpression(expr1);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_INTRO"));

        String str2 = "A | B";
        Expression expr2 = new Expression(RuleType.OR_INTRO);
        expr2.addToExpression(str2);
        expr2.addReferenceLine("2");
        proof.addExpression(expr2);

//        System.out.println(proof.generateHint(false));
        assertTrue(proof.generateHint(result).equals("Hint: NOT_ELIM"));

        String str3 = "FALSE";
        Expression expr3 = new Expression(RuleType.NOT_ELIM);
        expr3.addToExpression(str3);
        expr3.addReferenceLine("1");
        expr3.addReferenceLine("3");
        proof.addExpression(expr3);

//        System.out.println(proof.generateHint(false));
        assertTrue(proof.generateHint(result).equals("Hint: NOT_INTRO"));

        String str4 = "!A";
        Expression expr4 = new Expression(RuleType.NOT_INTRO);
        expr4.addToExpression(str4);
        expr4.addReferenceLine("2");
        expr4.addReferenceLine("4");
        proof.addExpression(expr4);

//        System.out.println(proof.generateHint(false));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }


    @Test
    public void hintTest11() throws SyntaxException {
        String str = "A -> (B -> C)";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);
        proof.addExpression(expr);

        String result = "A ^ B -> C";
        proof.setResultString(result);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str2 = "A ^ B";
        Expression expr2 = new Expression(RuleType.ASSUMPTION);
        expr2.addToExpression(str2);
        proof.addExpression(expr2);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str3 = "A";
        Expression expr3 = new Expression(RuleType.AND_ELIM);
        expr3.addToExpression(str3);
        expr3.addReferenceLine("2");
        proof.addExpression(expr3);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str4 = "B -> C";
        Expression expr4 = new Expression(RuleType.IMPLIES_ELIM);
        expr4.addToExpression(str4);
        expr4.addReferenceLine("1");
        expr4.addReferenceLine("3");
        proof.addExpression(expr4);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));
    }

    @Test
    public void hintTest1() throws SyntaxException {
        String str = "A ^ (B | C)";
        String str1 = "B -> D ";
        String str2 = "C -> D";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);


        String result = "D";
        proof.setResultString(result);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str3 = "A";
        Expression expr3 = new Expression(RuleType.AND_ELIM);
        expr3.addToExpression(str3);
        expr3.addReferenceLine("1");
        proof.addExpression(expr3);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str4 = "B | C";
        Expression expr4 = new Expression(RuleType.AND_ELIM);
        expr4.addToExpression(str4);
        expr4.addReferenceLine("1");
        proof.addExpression(expr4);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str5 = "B";
        Expression expr5 = new Expression(RuleType.ASSUMPTION);
        expr5.addToExpression(str5);
        proof.addExpression(expr5);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str6 = "D";
        Expression expr6 = new Expression(RuleType.IMPLIES_ELIM);
        expr6.addToExpression(str6);
        expr6.addReferenceLine("2");
        expr6.addReferenceLine("6");
        proof.addExpression(expr6);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str7 = "C";
        Expression expr7 = new Expression(RuleType.ASSUMPTION);
        expr7.addToExpression(str7);
        proof.addExpression(expr7);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str8 = "D";
        Expression expr8 = new Expression(RuleType.IMPLIES_ELIM);
        expr8.addToExpression(str8);
        expr8.addReferenceLine("3");
        expr8.addReferenceLine("8");
        proof.addExpression(expr8);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_ELIM"));

        String str9 = "D";
        Expression expr9 = new Expression(RuleType.OR_ELIM);
        expr9.addToExpression(str9);
        expr9.addReferenceLine("5");
        expr9.addReferenceLine("6");
        expr9.addReferenceLine("7");
        expr9.addReferenceLine("8");
        expr9.addReferenceLine("9");
        proof.addExpression(expr9);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }

    //TODO: fix this edge case
    @Test
    public void hintTest12() throws SyntaxException {
        String str = "A ^ (B | C)";
        String str1 = "B -> D ";
        String str2 = "C -> D";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);


        String result = "D | E";
        proof.setResultString(result);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str3 = "A";
        Expression expr3 = new Expression(RuleType.AND_ELIM);
        expr3.addToExpression(str3);
        expr3.addReferenceLine("1");
        proof.addExpression(expr3);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: AND_ELIM"));

        String str4 = "B | C";
        Expression expr4 = new Expression(RuleType.AND_ELIM);
        expr4.addToExpression(str4);
        expr4.addReferenceLine("1");
        proof.addExpression(expr4);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str5 = "B";
        Expression expr5 = new Expression(RuleType.ASSUMPTION);
        expr5.addToExpression(str5);
        proof.addExpression(expr5);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str6 = "D";
        Expression expr6 = new Expression(RuleType.IMPLIES_ELIM);
        expr6.addToExpression(str6);
        expr6.addReferenceLine("2");
        expr6.addReferenceLine("6");
        proof.addExpression(expr6);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: ASSUMPTION"));

        String str7 = "C";
        Expression expr7 = new Expression(RuleType.ASSUMPTION);
        expr7.addToExpression(str7);
        proof.addExpression(expr7);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: IMPLIES_ELIM"));

        String str8 = "D";
        Expression expr8 = new Expression(RuleType.IMPLIES_ELIM);
        expr8.addToExpression(str8);
        expr8.addReferenceLine("3");
        expr8.addReferenceLine("8");
        proof.addExpression(expr8);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_ELIM"));

        String str9 = "D";
        Expression expr9 = new Expression(RuleType.OR_ELIM);
        expr9.addToExpression(str9);
        expr9.addReferenceLine("5");
        expr9.addReferenceLine("6");
        expr9.addReferenceLine("7");
        expr9.addReferenceLine("8");
        expr9.addReferenceLine("9");
        proof.addExpression(expr9);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Hint: OR_INTRO"));

        String str10 = "D | E";
        Expression expr10 = new Expression(RuleType.OR_INTRO);
        expr10.addToExpression(str10);
        expr10.addReferenceLine("10");
        proof.addExpression(expr10);

//        System.out.println(proof.generateHint(result));
        assertTrue(proof.generateHint(result).equals("Proof already successfully solved"));
    }
}
