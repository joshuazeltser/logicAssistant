package model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 18/01/2017.
 */
public class RuleTests {

    Proof proof = new Proof();

    @Test
    public void splitTest() throws SyntaxException {
        String simple = "(A | B) ^ C";

        Expression expression = new Expression(RuleType.GIVEN);

        expression.addToExpression(simple);

        List<Expression> sides = expression.splitExpressionBy(OperatorType.AND);

        assertTrue(sides.get(0).toString().equals("OPEN A OR B CLOSE"));

        assertTrue(sides.get(1).toString().equals("C"));
    }

    @Test
    public void andIntroductionValidity() throws SyntaxException {

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

        Expression expr5 = new Expression(RuleType.AND_INTRO);
        expr5.addReferenceLine("1");
        expr5.addReferenceLine("2");
        expr5.addToExpression(str5);

        proof.addExpression(expr1);

        proof.addExpression(expr2);

        proof.addExpression(expr3);

        proof.addExpression(expr4);

        proof.addExpression(expr5);

        assertTrue(proof.isProofValid());

    }

    @Test
    public void multipleAndIntroductionValidity() throws SyntaxException {

        String str1 = "A";

        String str2 = "A ^ B";

        String str3 = "C";

        String str4 = "D";

        String str5 = "(A ^ B) ^ C";

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.GIVEN);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.GIVEN);
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.AND_INTRO);
        expr5.addReferenceLine("2");
        expr5.addReferenceLine("3");
        expr5.addToExpression(str5);

        proof.addExpression(expr1);

        proof.addExpression(expr2);

        proof.addExpression(expr3);

        proof.addExpression(expr4);
        proof.addExpression(expr5);

        assertTrue(proof.isProofValid());

    }

    @Test
    public void complexAndIntroductionValidity() throws SyntaxException {

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

        Expression expr5 = new Expression(RuleType.AND_INTRO);
        expr5.addReferenceLine("1");
        expr5.addReferenceLine("3");
        expr5.addToExpression(str5);

        proof.addExpression(expr1);

        proof.addExpression(expr2);

        proof.addExpression(expr3);

        proof.addExpression(expr4);

        proof.addExpression(expr5);


        assertTrue(proof.isProofValid());

    }

    @Test
    public void simpleImpliesIntroduction() throws SyntaxException {

        String str = "A";
        String str1 = "A | B";
        String str2 = "A -> (A | B)";

        Expression expr = new Expression(RuleType.ASSUMPTION);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.OR_INTRO);
        expr1.addReferenceLine("1");
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.IMPLIES_INTRO);
        expr2.addReferenceLine("1");
        expr2.addReferenceLine("2");
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        assertTrue(proof.isProofValid());


    }

    @Test
    public void impliesIntroductionValidity() throws SyntaxException {

        String str1 = "A -> (B -> C)"; //given

        String str2 = "A ^ B";

        String str3 = "A";

        String str4 = "B -> C";

        String str5 = "B";

        String str6 = "C";

        String str7 = "(A ^ B) -> C";

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.ASSUMPTION);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.AND_ELIM);
        expr3.addReferenceLine("2");
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.IMPLIES_ELIM);
        expr4.addReferenceLine("1");
        expr4.addReferenceLine("3");
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.AND_ELIM);
        expr5.addReferenceLine("2");
        expr5.addToExpression(str5);

        Expression expr6 = new Expression(RuleType.IMPLIES_ELIM);
        expr6.addReferenceLine("4");
        expr6.addReferenceLine("5");
        expr6.addToExpression(str6);

        Expression expr7 = new Expression(RuleType.IMPLIES_INTRO);
        expr7.addReferenceLine("2");
        expr7.addReferenceLine("6");
        expr7.addToExpression(str7);

        proof.addExpression(expr1);

        proof.addExpression(expr2);

        proof.addExpression(expr3);

        proof.addExpression(expr4);

        proof.addExpression(expr5);

        proof.addExpression(expr6);
        proof.addExpression(expr7);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void andElimSimpleTest() throws SyntaxException {
        String str = "A ^ C";
        String str1 = "A";
        String str2 = "C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.AND_ELIM);
        expr1.addReferenceLine("1");
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.AND_ELIM);
        expr2.addReferenceLine("1");
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void multipleAndElimSimpleTest() throws SyntaxException {
        String str = "(A ^ B) ^ C";
        String str1 = "A ^ B";
        String str2 = "C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.AND_ELIM);
        expr1.addReferenceLine("1");
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.AND_ELIM);
        expr2.addReferenceLine("1");
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void complexAndElimTest() throws SyntaxException {
        String str = "A -> B";

        String str1 = "C";

        String str2 = "A <-> C";

        String str3 = "A ^ D";

        String str4 = "D";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.GIVEN);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.AND_ELIM);
        expr4.addReferenceLine("4");
        expr4.addToExpression(str4);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void simpleOrIntroTest() throws SyntaxException {
        String str = "B";

        String str1 = "A ^ C";

        String str2 = "(A | B)";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.OR_INTRO);
        expr2.addReferenceLine("1");
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void multipleOrIntroTest() throws SyntaxException {
        String str = "A | B";

        String str1 = "A ^ C";

        String str2 = "(A | B) | C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.OR_INTRO);
        expr2.addReferenceLine("1");
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void andElimOrIntroTest() throws SyntaxException {
        String str = "P ^ Q";

        String str1 = "P";

        String str2 = "P | Q";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.AND_ELIM);
        expr1.addReferenceLine("1");
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.OR_INTRO);
        expr2.addReferenceLine("2");
        expr2.addToExpression(str2);

        proof.addExpression(expr);

        proof.addExpression(expr1);
        proof.addExpression(expr2);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void notNotSimpleTest() throws SyntaxException {
        String str = "!!A";

        String str1 = "A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.DOUBLE_NOT_ELIM);
        expr1.addReferenceLine("1");
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void simpleImpliesEliminationTest() throws SyntaxException {
        String str = "A";

        String str1 = "A -> B";

        String str2 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.IMPLIES_ELIM);
        expr2.addReferenceLine("1");
        expr2.addReferenceLine("2");
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        System.out.println(proof.printErrors());

        assertTrue(proof.isProofValid());
    }

    @Test
    public void complexImpliesEliminationTest() throws SyntaxException {
        String str = "A -> C";

        String str1 = "D ^ E";

        String str2 = "F | (G | R)";

        String str3 = "(A -> C) -> B";

        String str4 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.GIVEN);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.IMPLIES_ELIM);
        expr4.addReferenceLine("1");
        expr4.addReferenceLine("4");
        expr4.addToExpression(str4);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);


        assertTrue(proof.isProofValid());
    }

    @Test
    public void realNDProofTest1() throws SyntaxException {
        String str = "P";

        String str1 = "Q";

        String str2 = "(P ^ Q) -> R";

        String str3 = "P ^ Q";

        String str4 = "R";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.AND_INTRO);
        expr3.addReferenceLine("1");
        expr3.addReferenceLine("2");
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.IMPLIES_ELIM);
        expr4.addReferenceLine("3");
        expr4.addReferenceLine("4");
        expr4.addToExpression(str4);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);

        assertTrue(proof.isProofValid());

    }

    @Test
    public void simpleOnlyElimTest() throws SyntaxException {
        String str = "A <-> B";

        String str1 = "A -> B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.ONLY_ELIM);
        expr1.addReferenceLine("1");
        expr1.addToExpression(str1);

        proof.addExpression(expr);
        proof.addExpression(expr1);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void simpleOnlyIntroTest() throws SyntaxException {
        String str = "A -> B";

        String str1 = "B -> A";

        String str2 = "A <-> B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.ONLY_INTRO);
        expr2.addReferenceLine("1");
        expr2.addReferenceLine("2");
        expr2.addToExpression(str2);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void realNDProofTest2() throws SyntaxException {
        String str = "P";

        String str1 = "P <-> Q";

        String str2 = "P -> Q";

        String str3 = "Q";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.ONLY_ELIM);
        expr2.addReferenceLine("2");
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.IMPLIES_ELIM);
        expr3.addReferenceLine("1");
        expr3.addReferenceLine("3");
        expr3.addToExpression(str3);

        proof.addExpression(expr);

        proof.addExpression(expr1);

        proof.addExpression(expr2);

        proof.addExpression(expr3);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void notIntroSimpleTest() throws SyntaxException {
        String str = "!(A | B)";
        String str1 = "A";
        String str2 = "A | B";
        String str3 = "FALSE";
        String str4 = "!A";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.ASSUMPTION);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.OR_INTRO);
        expr2.addReferenceLine("2");
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.NOT_ELIM);
        expr3.addReferenceLine("1");
        expr3.addReferenceLine("3");
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.NOT_INTRO);
        expr4.addReferenceLine("2");
        expr4.addReferenceLine("4");
        expr4.addToExpression(str4);



        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);


        assertTrue(proof.isProofValid());
    }



    @Test
    public void simpleOrEliminationTest() throws SyntaxException {
        String str = "A -> C";
        String str1 = "B -> C";
        String str2 = "A | B";
        String str3 = "A";
        String str4 = "C";
        String str5 = "B";
        String str6 = "C";
        String str7 = "C";
        String str8 = "A | B -> C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.ASSUMPTION);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.ASSUMPTION);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.IMPLIES_ELIM);
        expr4.addReferenceLine("1");
        expr4.addReferenceLine("4");
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.ASSUMPTION);
        expr5.addToExpression(str5);

        Expression expr6 = new Expression(RuleType.IMPLIES_ELIM);
        expr6.addReferenceLine("2");
        expr6.addReferenceLine("6");
        expr6.addToExpression(str6);

        Expression expr7 = new Expression(RuleType.OR_ELIM);
        expr7.addReferenceLine("3");
        expr7.addReferenceLine("4");
        expr7.addReferenceLine("5");
        expr7.addReferenceLine("6");
        expr7.addReferenceLine("7");
        expr7.addToExpression(str7);

        Expression expr8 = new Expression(RuleType.IMPLIES_INTRO);
        expr8.addReferenceLine("3");
        expr8.addReferenceLine("8");
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

        assertTrue(proof.isProofValid());
    }

    @Test
    public void fullCheckerTestProof1() throws SyntaxException {
        String str = "P -> Q";
        String str1 = "Q -> R";
        String str2 = "P";
        String str3 = "Q";
        String str4 = "R";
        String str5 = "P -> R";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.ASSUMPTION);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.IMPLIES_ELIM);
        expr3.addReferenceLine("1");
        expr3.addReferenceLine("3");
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.IMPLIES_ELIM);
        expr4.addReferenceLine("2");
        expr4.addReferenceLine("4");
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.IMPLIES_INTRO);
        expr5.addReferenceLine("3");
        expr5.addReferenceLine("5");
        expr5.addToExpression(str5);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);
        proof.addExpression(expr5);

        assertTrue(proof.isProofValid());
    }


    @Test
    public void fullCheckerTestProof3() throws SyntaxException {
        String str = "!(A | !A)";
        String str1 = "A";
        String str2 = "A | !A";
        String str3 = "FALSE";
        String str4 = "!A";
        String str5 = "A | !A";
        String str6 = "FALSE";
        String str7 = "!!(A | !A)";
        String str8 = "A | !A";


        Expression expr = new Expression(RuleType.ASSUMPTION);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.ASSUMPTION);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.OR_INTRO);
        expr2.addReferenceLine("2");
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.NOT_ELIM);
        expr3.addReferenceLine("1");
        expr3.addReferenceLine("3");
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.NOT_INTRO);
        expr4.addReferenceLine("2");
        expr4.addReferenceLine("4");
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.OR_INTRO);
        expr5.addReferenceLine("5");
        expr5.addToExpression(str5);

        Expression expr6 = new Expression(RuleType.NOT_ELIM);
        expr6.addReferenceLine("1");
        expr6.addReferenceLine("6");
        expr6.addToExpression(str6);

        Expression expr7 = new Expression(RuleType.NOT_INTRO);
        expr7.addReferenceLine("1");
        expr7.addReferenceLine("7");
        expr7.addToExpression(str7);

        Expression expr8 = new Expression(RuleType.DOUBLE_NOT_ELIM);
        expr8.addReferenceLine("8");
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

        assertTrue(proof.isProofValid());
    }

    @Test
    public void fullCheckerTestProof4() throws SyntaxException {
        String str = "A";
        String str1 = "!A";
        String str2 = "FALSE";
        String str3 = "!!A";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.ASSUMPTION);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.NOT_ELIM);
        expr2.addReferenceLine("1");
        expr2.addReferenceLine("2");
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.NOT_INTRO);
        expr3.addReferenceLine("2");
        expr3.addReferenceLine("3");
        expr3.addToExpression(str3);



        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void fullCheckerTestProof5() throws SyntaxException {
        String str = "P -> Q";
        String str1 = "P | !P";
        String str2 = "P";
        String str3 = "Q";
        String str4 = "!P | Q";
        String str5 = "!P";
        String str6 = "!P | Q";
        String str7 = "!P | Q";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.ASSUMPTION);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.IMPLIES_ELIM);
        expr3.addReferenceLine("1");
        expr3.addReferenceLine("3");
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.OR_INTRO);
        expr4.addReferenceLine("4");
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.ASSUMPTION);
        expr5.addToExpression(str5);

        Expression expr6 = new Expression(RuleType.OR_INTRO);
        expr6.addReferenceLine("6");
        expr6.addToExpression(str6);

        Expression expr7 = new Expression(RuleType.OR_ELIM);
        expr7.addReferenceLine("2");
        expr7.addReferenceLine("3");
        expr7.addReferenceLine("5");
        expr7.addReferenceLine("6");
        expr7.addReferenceLine("7");
        expr7.addReferenceLine("8");
        expr7.addToExpression(str7);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);
        proof.addExpression(expr5);
        proof.addExpression(expr6);
        proof.addExpression(expr7);

        assertTrue(proof.isProofValid());
    }

    @Test
    public void fullCheckerTestProof6() throws SyntaxException {
        String str = "stranger -> barked";
        String str1 = "barked -> woke";
        String str2 = "!woke";
        String str3 = "stranger";
        String str4 = "barked";
        String str5 = "woke";
        String str6 = "FALSE";
        String str7 = "!stranger";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.ASSUMPTION);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.IMPLIES_ELIM);
        expr4.addReferenceLine("1");
        expr4.addReferenceLine("4");
        expr4.addToExpression(str4);

        Expression expr5 = new Expression(RuleType.IMPLIES_ELIM);
        expr5.addReferenceLine("2");
        expr5.addReferenceLine("5");
        expr5.addToExpression(str5);

        Expression expr6 = new Expression(RuleType.NOT_ELIM);
        expr6.addReferenceLine("3");
        expr6.addReferenceLine("6");
        expr6.addToExpression(str6);

        Expression expr7 = new Expression(RuleType.NOT_INTRO);
        expr7.addReferenceLine("4");
        expr7.addReferenceLine("7");
        expr7.addToExpression(str7);

        proof.addExpression(expr);
        proof.addExpression(expr1);
        proof.addExpression(expr2);
        proof.addExpression(expr3);
        proof.addExpression(expr4);
        proof.addExpression(expr5);
        proof.addExpression(expr6);
        proof.addExpression(expr7);

        assertTrue(proof.isProofValid());
    }


}
