package model;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 18/01/2017.
 */
public class RuleTests {

    @Test
    public void splitTest() {
        String simple = "(A | B) ^ C";

        Expression expression = new Expression(RuleType.GIVEN);

        expression.addToExpression(simple);

        List<Expression> sides = expression.splitExpressionBy(OperatorType.AND);

        assertTrue(sides.get(0).toString().equals("OPEN A OR B CLOSE"));

        assertTrue(sides.get(1).toString().equals("C"));
    }

    @Test
    public void andIntroductionValidity() {
        Proof proof = new Proof();

        String str1 = "A";

        String str2 = "B";

        String str3 = "C";

        String str4 = "D";

        String str5 = "A ^ B";

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

        proof.addExpression(expr1);

        proof.addExpression(expr2);

        proof.addExpression(expr3);

        proof.addExpression(expr4);

        assertTrue(Rules.isAndIntroValid(expr5, proof));

    }

    @Test
    public void complexAndIntroductionValidity() {

        Proof proof = new Proof();

        String str1 = "(A -> B)";

        String str2 = "B ^ C";

        String str3 = "(D | E) | F";

        String str4 = "G";

        String str5 = "(A -> B) ^ (D | E) | F";

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

        proof.addExpression(expr1);

        proof.addExpression(expr2);

        proof.addExpression(expr3);

        proof.addExpression(expr4);

        assertTrue(Rules.isAndIntroValid(expr5, proof));

    }

    @Test
    public void impliesIntroductionValidity() {
        Proof proof = new Proof();

        String str1 = "A"; //given

        String str2 = "B"; //given

        String str3 = "C"; //assume

        String str4 = "A ^ B"; //and intro

        String str5 = "C -> A ^ B"; //wanted

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.ASSUMPTION);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.AND_INTRO);
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.IMPLIES_INTRO);
        expr5.addToExpression(str5);

        proof.addExpression(expr1);

        proof.addExpression(expr2);

        proof.addExpression(expr3);

        assertTrue(Rules.isAndIntroValid(expr4, proof));

        proof.addExpression(expr4);

        assertTrue(Rules.isImpliesIntroValid(expr5, proof));
    }

    @Test
    public void andElimSimpleTest() {
        Proof proof = new Proof();

        String str = "A ^ B";
        String str1 = "A";
        String str2 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.AND_ELIM);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.AND_ELIM);
        expr2.addToExpression(str2);

        proof.addExpression(expr);

        assertTrue(Rules.isAndElimValid(expr1, proof));
        assertTrue(Rules.isAndElimValid(expr2, proof));
    }

    @Test
    public void complexAndElimTest() {
        Proof proof = new Proof();

        String str = "A -> B";

        String str1 = "C";

        String str2 = "A <-> C";

        String str3 = "A ^ D";

        String str4 = "D";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str3);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.GIVEN);
        expr3.addToExpression(str1);

        Expression expr4 = new Expression(RuleType.AND_ELIM);
        expr4.addToExpression(str4);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);

        assertTrue(Rules.isAndElimValid(expr4, proof));

    }




}
