package model;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 14/04/2017.
 */
public class HintTests {

    Proof proof = new Proof();

    @Test
    public void andEliminationHintTest() throws SyntaxException {
        String str = "A ^ B";
        String str1 = "C";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();
//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A AND B, C, B]"));
    }

    @Test
    public void impliesEliminationHintTest() throws SyntaxException {
        String str = "A -> B";
        String str1 = "A";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A IMPLIES B, A, B]"));
    }

    @Test
    public void impliesAndEliminationHintTest() throws SyntaxException {
        String str = "A ^ B";
        String str1 = "B -> C";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "C";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A AND B, B IMPLIES C, B, C]"));
    }

    @Test
    public void impliesAndEliminationRecursiveHintTest() throws SyntaxException {
        String str1 = "A ^ B";
        String str = "B -> C";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "C";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[B IMPLIES C, A AND B, B, C]"));
    }

    @Test
    public void onlyEliminationHintTest() throws SyntaxException {
        String str = "A <-> B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);


        proof.addExpression(expr);

        String result = "B -> A";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A ONLY B, B IMPLIES A]"));
    }

    @Test
    public void onlyImpliesEliminationHintTest() throws SyntaxException {
        String str = "A <-> B";
        String str1 = "A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);


        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A ONLY B, A, A IMPLIES B, B]"));
    }

    @Test
    public void notEliminationHintTest() throws SyntaxException {
        String str = "!A";
        String str1 = "A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);


        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "FALSE";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[NOT A, A, FALSE]"));
    }

    @Test
    public void notImpliesEliminationHintTest() throws SyntaxException {
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
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A IMPLIES B, A, NOT B, B, FALSE]"));
    }

    @Test
    public void simpleNotNotEliminationHintTest() throws SyntaxException {
        String str = "!!A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        proof.addExpression(expr);

        String result = "A";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[NOT NOT A, A]"));
    }

    @Test
    public void impliesNotNotEliminationHintTest() throws SyntaxException {
        String str = "!!A";
        String str1 = "A -> B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[NOT NOT A, A IMPLIES B, A, B]"));
    }

    @Test
    public void simpleAndIntroHintTest() throws SyntaxException {
        String str = "A";
        String str1 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "A ^ B";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A, B, A AND B]"));
    }

    @Test
    public void largeAndIntroHintTest() throws SyntaxException {
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
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A, B, C, D, E IMPLIES F, O, S, Y IMPLIES F, X, P, S AND P]"));
    }

    @Test
    public void iffImpliesAndHintTest() throws SyntaxException {
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

        String result = "A ^ C";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A IMPLIES B, B IMPLIES C, A, B, C, A AND C]"));
    }

    @Test
    public void simpleOrIntroHintTest() throws SyntaxException {
        String str = "A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        proof.addExpression(expr);

        String result = "A | B";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A, A OR B]"));
    }

    @Test
    public void largeOrIntroHintTest() throws SyntaxException {
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
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);
        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A, B, C, D, E IMPLIES F, O, S, Y IMPLIES F, X, S OR P]"));
    }

    @Test
    public void OrImpliesAndHintTest() throws SyntaxException {
        String str = "A ^ B";
        String str1 = "A -> C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "A ^ B | C";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
//        not guaranteed to be the shortest proof
        assertTrue(res.toString().equals("[A AND B, A IMPLIES C, A AND B OR C]"));
    }

    @Test
    public void simpleImpliesAtEndIntroHintTest() throws SyntaxException {
        String str = "C ^ B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        proof.addExpression(expr);

        String result = "A -> B";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
        assertTrue(res.toString() .equals("[C AND B, A, B, A IMPLIES B]"));
    }

    @Test
    public void harderImpliesAtEndIntroHintTest() throws SyntaxException {
        String str = "A -> C";
        String str1 = "C -> D";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "A -> D";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        System.out.println("result " + res);
//        not guaranteed to be the shortest proof
        assertTrue(res.toString() .equals("[A IMPLIES C, C IMPLIES D, A, C, D, A IMPLIES D]"));
    }

    @Test
    public void orEliminationHintTest() throws SyntaxException {
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
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        for (Expression e : res.getExpressions()) {
//            System.out.println(e.getRuleType());
//        }

//        System.out.println("result " + res);
//        not guaranteed to be the shortest proof
        assertTrue(res.toString() .equals("[A OR B, A IMPLIES C, B IMPLIES C, A, C, B, C, C]"));
    }

    @Test
    public void orEliminationHintTest2() throws SyntaxException {
        String str = "A | B ^ D";
        String str1 = "A -> B ";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);



        proof.addExpression(expr);
        proof.addExpression(expr1);

        String result = "B";
        Expression resultExpr = new Expression();
        resultExpr.addToExpression(result);
        proof.setResultExpr(resultExpr);

        List<Proof> proofs = new LinkedList<>();

        proofs.add(proof);

        proof.setProofSteps(proofs);

        Proof res = proof.nextStep();

//        for (Expression e : res.getExpressions()) {
//            System.out.println(e.getRuleType());
//        }

//        System.out.println("result " + res);
//        not guaranteed to be the shortest proof
        assertTrue(res.toString() .equals("[A OR B AND D, A IMPLIES B, A, B, B AND D, B, B]"));
    }
}
