package model;

import javassist.compiler.ast.Expr;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 18/01/2017.
 */
public class RuleTests {

    Proof proof = new Proof();
    @Test
    public void splitTest() {
        String simple = "(A | B) ^ C";

        Expression expression = new Expression(RuleType.GIVEN);

        expression.addToExpression(simple);

        List<Expression> sides = expression.splitExpressionBy(OperatorType.AND,0);

        assertTrue(sides.get(0).toString().equals("OPEN A OR B CLOSE"));

        assertTrue(sides.get(1).toString().equals("C"));
    }

    @Test
    public void andIntroductionValidity() {

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

        assertTrue(proof.isAndIntroValid(expr5));

    }

    @Test
    public void multipleAndIntroductionValidity() {

        String str1 = "A";

        String str2 = "A ^ B";

        String str3 = "C";

        String str4 = "D";

        String str5 = "A ^ B ^ C";

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

        assertTrue(proof.isAndIntroValid(expr5));

    }

    @Test
    public void complexAndIntroductionValidity() {

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

        assertTrue(proof.isAndIntroValid(expr5));

    }

    @Test
    public void impliesIntroductionValidity() {

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

        assertTrue(proof.isAndIntroValid(expr4));

        proof.addExpression(expr4);

        assertTrue(proof.isImpliesIntroValid(expr5));
    }

    @Test
    public void andElimSimpleTest() {
        String str = "A ^ C";
        String str1 = "A";
        String str2 = "C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.AND_ELIM);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.AND_ELIM);
        expr2.addToExpression(str2);

        proof.addExpression(expr);

        assertTrue(proof.isAndElimValid(expr1));
        assertTrue(proof.isAndElimValid(expr2));
    }

    @Test
    public void multipleAndElimSimpleTest() {
        String str = "A ^ B ^ C";
        String str1 = "A ^ B";
        String str2 = "C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.AND_ELIM);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.AND_ELIM);
        expr2.addToExpression(str2);

        proof.addExpression(expr);

        assertTrue(proof.isAndElimValid(expr1));
        assertTrue(proof.isAndElimValid(expr2));
    }

    @Test
    public void complexAndElimTest() {
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

        assertTrue(proof.isAndElimValid(expr4));
    }

    @Test
    public void simpleOrIntroTest() {
        String str = "B";

        String str1 = "A ^ C";

        String str2 = "A | B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.OR_INTRO);
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        assertTrue(proof.isOrIntroValid(expr2));
    }

    @Test
    public void multipleOrIntroTest() {
        String str = "A | B";

        String str1 = "A ^ C";

        String str2 = "A | B | C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.OR_INTRO);
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        assertTrue(proof.isOrIntroValid(expr2));
    }

    @Test
    public void andElimOrIntroTest() {
        String str = "P ^ Q";

        String str1 = "P";

        String str2 = "P | Q";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.AND_ELIM);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.OR_INTRO);
        expr2.addToExpression(str2);

        proof.addExpression(expr);

        assertTrue(proof.isAndElimValid(expr1));

        proof.addExpression(expr1);

        assertTrue(proof.isOrIntroValid(expr2));
    }




}
