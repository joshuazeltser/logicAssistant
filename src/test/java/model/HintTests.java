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

        Proof res = proof.nextStep(proofs);
//        System.out.println("result " + res);
        assertTrue(proof.nextStep(proofs).toString().equals("[A AND B, C, B]"));
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

        Proof res = proof.nextStep(proofs);

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

        Proof res = proof.nextStep(proofs);

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

        Proof res = proof.nextStep(proofs);

        System.out.println("result " + res);
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

        Proof res = proof.nextStep(proofs);

//        System.out.println("result " + res);
        assertTrue(res.toString().equals("[A ONLY B, B IMPLIES A]"));
    }


}
