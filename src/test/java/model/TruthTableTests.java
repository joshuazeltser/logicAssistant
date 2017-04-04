package model;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by joshuazeltser on 29/03/2017.
 */
public class TruthTableTests {

    @Test
    public void binaryPermutationTest1() throws SyntaxException {
        String str = "A ^ B";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        String expected = "{{A=0, B=0}=0, {A=1, B=0}=0, {A=0, B=1}=0, {A=1, B=1}=1}";

        assertTrue(tt.convertToTruthValues(expr).toString().equals(expected));

    }

    @Test
    public void binaryPermutationTest2() throws SyntaxException {
        String str = "A ^ (!B | C)";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        String expected = "{{A=0, B=0, C=0}=0, {A=1, B=0, C=0}=1, {A=0, B=1, C=0}=0, {A=1, B=1, C=0}=0, " +
                "{A=0, B=0, C=1}=0, {A=1, B=0, C=1}=1, {A=0, B=1, C=1}=0, {A=1, B=1, C=1}=1}";

//        System.out.println(tt.convertToTruthValues(expr));
        assertTrue(tt.convertToTruthValues(expr).toString().equals(expected));

    }

    @Test
    public void binaryPermutationTest3() throws SyntaxException {
        String str = "(A -> B) | (C -> D)";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        tt.convertToTruthValues(expr);

        String expected = "{{A=0, B=0, C=0, D=0}=1, {A=1, B=0, C=0, D=0}=1, {A=0, B=1, C=0, D=0}=1, " +
                "{A=1, B=1, C=0, D=0}=1, {A=0, B=0, C=1, D=0}=1, {A=1, B=0, C=1, D=0}=0, {A=0, B=1, C=1, D=0}=1, " +
                "{A=1, B=1, C=1, D=0}=1, {A=0, B=0, C=0, D=1}=1, {A=1, B=0, C=0, D=1}=1, {A=0, B=1, C=0, D=1}=1, " +
                "{A=1, B=1, C=0, D=1}=1, {A=0, B=0, C=1, D=1}=1, {A=1, B=0, C=1, D=1}=1, {A=0, B=1, C=1, D=1}=1, " +
                "{A=1, B=1, C=1, D=1}=1}";

//        System.out.println(tt.convertToTruthValues(expr));
        assertTrue(tt.convertToTruthValues(expr).toString().equals(expected));
    }

    @Test
    public void binaryPermutationTest4() throws SyntaxException {
        String str = "(A -> B) <-> C";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        tt.convertToTruthValues(expr);

        String expected = "{{A=0, B=0, C=0}=0, {A=1, B=0, C=0}=1, {A=0, B=1, C=0}=0, {A=1, B=1, C=0}=0, " +
                "{A=0, B=0, C=1}=1, {A=1, B=0, C=1}=0, {A=0, B=1, C=1}=1, {A=1, B=1, C=1}=1}";

//        System.out.println(tt.convertToTruthValues(expr));

        assertTrue(tt.convertToTruthValues(expr).toString().equals(expected));
    }

    @Test
    public void binaryPermutationTest5() throws SyntaxException {
        String str = "(A -> C) -> (B ^ D)";

        Expression expr = new Expression();

        expr.addToExpression(str);
        List<Expression> list = new LinkedList<>();

        TruthTable tt = new TruthTable(list,expr);

        tt.convertToTruthValues(expr);

        String expected = "{{A=0, C=0, B=0, D=0}=0, {A=1, C=0, B=0, D=0}=1, {A=0, C=1, B=0, D=0}=0," +
                " {A=1, C=1, B=0, D=0}=0, {A=0, C=0, B=1, D=0}=0, {A=1, C=0, B=1, D=0}=1, {A=0, C=1, B=1, D=0}=0," +
                " {A=1, C=1, B=1, D=0}=0, {A=0, C=0, B=0, D=1}=0, {A=1, C=0, B=0, D=1}=1, {A=0, C=1, B=0, D=1}=0, " +
                "{A=1, C=1, B=0, D=1}=0, {A=0, C=0, B=1, D=1}=1, {A=1, C=0, B=1, D=1}=1, {A=0, C=1, B=1, D=1}=1, " +
                "{A=1, C=1, B=1, D=1}=1}";

//        System.out.println(tt.convertToTruthValues(expr));

        assertTrue(tt.convertToTruthValues(expr).toString().equals(expected));
    }

    @Test
    public void proofValidationTest1() throws SyntaxException {
        String str = "A ^ C";
        String str1 = "C";


        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.AND_ELIM);
        expr1.addReferenceLine("1");
        expr1.addToExpression(str1);


        List<Expression> premises = new LinkedList<>();
        premises.add(expr);

        TruthTable tt = new TruthTable(premises,expr1);

        assertTrue(tt.validateProof());
    }

    @Test
    public void proofValidationTest2() throws SyntaxException {
        String str = "B";

        String str3 = "A | D";

        String str4 = "B";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr3 = new Expression(RuleType.GIVEN);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.AND_ELIM);
        expr4.addReferenceLine("4");
        expr4.addToExpression(str4);


        List<Expression> premises = new LinkedList<>();
        premises.add(expr);
        premises.add(expr3);

        TruthTable tt = new TruthTable(premises,expr4);

        assertTrue(tt.validateProof());
    }

    @Test
    public void proofValidationTest4() throws SyntaxException {
        String str = "(P -> Q) ^ (R -> S)";

        String str1 = "P | R";

        String str2 = "Q | S";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.OR_INTRO);
        expr2.addToExpression(str2);


        List<Expression> premises = new LinkedList<>();
        premises.add(expr);
        premises.add(expr1);

        TruthTable tt = new TruthTable(premises,expr2);

        assertTrue(tt.validateProof());
    }

    @Test
    public void proofValidationTest5() throws SyntaxException {
        String str = "P";

        String str1 = "Q";

        String str2 = "P ^ Q";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.AND_INTRO);
        expr2.addToExpression(str2);


        List<Expression> premises = new LinkedList<>();
        premises.add(expr);
        premises.add(expr1);

        TruthTable tt = new TruthTable(premises,expr2);

        assertTrue(tt.validateProof());
    }

    @Test
    public void proofValidationTest6() throws SyntaxException {
        String str = "A -> (B | !C)";

        String str1 = "D -> C";

        String str2 = "A";

        String str3 = "!B";

        String str4 = "!C";

        Expression expr = new Expression(RuleType.GIVEN);
        expr.addToExpression(str);

        Expression expr1 = new Expression(RuleType.GIVEN);
        expr1.addToExpression(str1);

        Expression expr2 = new Expression(RuleType.GIVEN);
        expr2.addToExpression(str2);

        Expression expr3 = new Expression(RuleType.IMPLIES_ELIM);
        expr3.addToExpression(str3);

        Expression expr4 = new Expression(RuleType.OR_ELIM);
        expr4.addToExpression(str4);


        List<Expression> premises = new LinkedList<>();
        premises.add(expr);
        premises.add(expr1);
        premises.add(expr2);
        premises.add(expr3);

        TruthTable tt = new TruthTable(premises,expr4);

        assertTrue(tt.validateProof());
    }

}


