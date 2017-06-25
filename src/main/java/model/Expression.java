package model;

import org.apache.commons.lang3.StringEscapeUtils;
import java.util.*;
import java.util.stream.Collectors;

import static model.BracketType.CLOSE_BRACKET;
import static model.BracketType.OPEN_BRACKET;
import static model.OperatorType.NOT;


/**
 * Created by joshuazeltser on 03/01/2017.
 */
public class Expression {

    private List<Component> expression;

    private List<Integer> lines;

    private RuleType ruleType;

    private boolean marked;

    private boolean lemmaMarked;

    private Integer lemmaNum;

    private final List<String> operatorNames = Arrays.asList("AND", "OR", "IMPLIES", "ONLY");


    public Expression(RuleType ruleType) {
        expression = new LinkedList<>();
        lines = new LinkedList<>();
        this.ruleType = ruleType;
        lemmaMarked = false;
        marked = false;
        lemmaNum = 0;
    }

    public Expression() {
        expression = new LinkedList<>();
        lines = new LinkedList<>();
        marked = false;
    }

    public void addReferenceLine(String a) throws SyntaxException {

        try {
            int num = Integer.parseInt(a);
            lines.add(num);
        } catch (NumberFormatException e) {
            throw new SyntaxException("Syntax Error: " + a + " is an invalid line number");
        }

    }

    // Compares the structures of two expression
    public boolean compareStructure(Expression e2) {

        if (expression.size() != e2.expression.size()) {
            return false;
        }


        for (int i = 0; i < expression.size(); i++) {
            if (expression.get(i).equals(new Operator("NOT", NOT))) {
                if (!e2.expression.get(i).equals(new Operator("NOT", NOT))) {
                    return false;
                }
            }

            if (expression.get(i).equals(new Bracket("OPEN", OPEN_BRACKET))) {
                if (!e2.expression.get(i).equals(new Bracket("OPEN", OPEN_BRACKET))) {
                    return false;
                }
            }

            if (expression.get(i).equals(new Bracket("CLOSE", CLOSE_BRACKET))) {
                if (!e2.expression.get(i).equals(new Bracket("CLOSE", CLOSE_BRACKET))) {
                    return false;
                }
            }

            if (isOperator(expression.get(i).toString())) {
                if (!expression.get(i).equals(e2.expression.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Integer> getReferenceLine() {
        return lines;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }


    // Create internal representation of expression if all checks pass
    public boolean addToExpression(String input) throws SyntaxException {

        if (!checkBrackets(input)) {
            throw new SyntaxException("Syntax Error: Mismatched brackets");
        }

        for (int i = 0; i < input.length()-1; i++) {
            if (input.charAt(i) == ' ' && input.charAt(i+1) == ' ') {
                throw new SyntaxException("Syntax Error: You cannot use a space twice in a row in an expression ");
            }
        }

        String[] tokens = input.split(" ");

        tokens = convertHTMLToChar(tokens);

        syntaxCheck(tokens);

        for (int i = 0; i < tokens.length; i++) {

            String token = tokens[i];
            switch (token) {
                case "|":
                    expression.add(new Operator("OR", OperatorType.OR));
                    break;
                case "^":
                    expression.add(new Operator("AND", OperatorType.AND));
                    break;
                case "->":
                    expression.add(new Operator("IMPLIES", OperatorType.IMPLIES));
                    break;
                case "<->":
                    expression.add(new Operator("ONLY", OperatorType.ONLY));
                    break;
                case "(":
                    expression.add(new Bracket("OPEN", BracketType.OPEN_BRACKET));
                    break;
                case ")":
                    expression.add(new Bracket("CLOSE", BracketType.CLOSE_BRACKET));
                    break;

                default:
                    while (token.charAt(0) == '!') {
                         expression.add(new Operator("NOT", NOT));

                        token = token.substring(1);
                    }
                    while (token.contains("(")) {

                        expression.add(new Bracket("OPEN", BracketType.OPEN_BRACKET));

                        token = token.substring(1);
                    }

                    int count = 0;
                    while (token.contains(")")) {
                        token = removeLastChar(token);
                        count++;
                    }

                    while (token.charAt(0) == '!') {
                        expression.add(new Operator("NOT", NOT));
                        token = token.substring(1);
                    }

                    expression.add(new Proposition(token));

                    while (count > 0) {
                        expression.add(new Bracket("CLOSE", BracketType.CLOSE_BRACKET));
                        count--;
                    }

            }
        }

        return true;

    }

    private String[] convertHTMLToChar(String[] tokens) {

        for (int i = 0; i < tokens.length; i++) {

            String op = StringEscapeUtils.unescapeHtml4(tokens[i]);

            if (op.equals(StringEscapeUtils.unescapeHtml4("&and;"))) {
                tokens[i] = "^";
            } else if (op.equals(StringEscapeUtils.unescapeHtml4("&or;"))) {
                tokens[i] = "|";
            } else if (op.equals(StringEscapeUtils.unescapeHtml4("&rarr;"))) {
                tokens[i] = "->";
            } else if (op.equals(StringEscapeUtils.unescapeHtml4("&harr;"))) {
                tokens[i] = "<->";
            } else if (op.equals(StringEscapeUtils.unescapeHtml4("&perp;"))) {
                tokens[i] = "FALSE";
            } else {
                if (!tokens[i].equals("")) {
                    if (StringEscapeUtils.unescapeHtml4(tokens[i].charAt(0) + "")
                            .equals(StringEscapeUtils.unescapeHtml4("&not;"))) {
                        if (StringEscapeUtils.unescapeHtml4(tokens[i].charAt(1) + "")
                                .equals(StringEscapeUtils.unescapeHtml4("&not;"))) {
                            tokens[i] = "!!" + tokens[i].substring(2);
                        } else {
                            tokens[i] = "!" + tokens[i].substring(1);
                        }
                    }
                }
            }

        }


        return tokens;

    }

    private String convertStringToHTML(String str) {

        switch (str) {
            case "^": return "&and;";
            case "|": return "&or;";
            case "->": return "&rarr;";
            case "<->": return "&harr;";
            case "!": return "&not;";
            case "FALSE": return "&bot;";
            default: return str;
        }
    }

    public void syntaxCheck(String[] tokens) throws SyntaxException {

        for (int i = 0; i < tokens.length; i++) {
            if (i == 0 || i == tokens.length - 1) {
                switch (tokens[i]) {
                    case "^":
                    case "->":
                    case "<->":
                    case "|": throw new SyntaxException("Syntax Error: You cannot use "
                            + convertStringToHTML(tokens[i]) +" operator at this part of an expression");
                }
            }

            if (i==0 && tokens[i].charAt(0) == ')') {
                throw new SyntaxException("Syntax Error: You cannot use " + tokens[i] +"" +
                        " operator at this part of an expression");
            }

            if (tokens[i].charAt(tokens[i].length()-1) == '!' || tokens[i].charAt(tokens[i].length()-1) == '(') {
                throw new SyntaxException("Syntax Error: You cannot use " +
                        convertStringToHTML(tokens[i].charAt(tokens[i].length()-1) + "")
                        + " " + "operator at the end of an expression");
            }

            if ((i == (tokens.length - 1)) && (tokens[i].equals("(") || tokens[i].equals("!"))) {
                throw new SyntaxException("Syntax Error: You cannot use " + convertStringToHTML(tokens[i]) +" " +
                        "operator at this part of an expression");
            }



            if (!tokens[i].equals("(") && !tokens[i].equals(")") && !tokens[i].equals("!") &&
                    !tokens[i].equals("NOT") && i < tokens.length-1 && !tokens.equals("OPEN")) {

                if (tokens[i].equals(tokens[i+1])) {
                    throw new SyntaxException("Syntax Error: You cannot use " + convertStringToHTML(tokens[i]) +"" +
                            " twice in a row as part of an expression");
                }
            }
            if (i < tokens.length-1) {
                if (isOperator(tokens[i]) && isOperator(tokens[i + 1])) {
                    throw new SyntaxException("Syntax Error: You cannot use an operator twice in a row as part " +
                            "of an expression");
                }


                if (!isOperator(tokens[i]) && !isOperator(tokens[i + 1]) && !tokens[i].equals("NOT")
                        && !tokens[i + 1].equals("NOT") && !tokens[i].equals("OPEN")
                        && !tokens[i + 1].equals("OPEN")  && !tokens[i].equals("CLOSE")
                        && !tokens[i + 1].equals("CLOSE")) {
                    throw new SyntaxException("Syntax Error: You cannot use two Propositions " +
                            "in a row as part of an expression");
                }
            }


            if (!inWhiteList(tokens[i])) {
                throw new SyntaxException("Syntax Error: " + convertStringToHTML(tokens[i]) + " contains invalid" +
                        " Components for use in an Expression");
            }

        }
    }

    // Check whether string is a valid input
    private boolean inWhiteList(String str) {

        char[] chars = str.toCharArray();

        if (isOperator(str)) {
            return true;
        }

        for (int i = 0; i < chars.length; i++) {

            if (!Character.isLetter(chars[i]) && chars[i] != '(' && chars[i] != ')' && chars[i] != '!') {
                return false;
            }
        }

        return true;
    }


    public List<String> replacePropositions(List<LinkedHashMap<Proposition, Integer>> permMapList)
            throws SyntaxException {

        List<String> results = new LinkedList<>();

        boolean not = false;

        for (Map permMap : permMapList) {
            String str = "(";

            for (Component c : expression) {
                if (permMap.containsKey(c)) {
                    str += permMap.get(c) + " ";
                    if (!not) {
                        str += " ";
                    } else {
                        str += ")";
                        not = false;
                    }
                } else if (c.equals(new Operator("AND", OperatorType.AND))) {
                    str += "&";
                } else if (c.equals(new Operator("OR", OperatorType.OR))) {
                    str += "|";
                } else if (c.equals(new Bracket("OPEN", BracketType.OPEN_BRACKET))) {
                    str += "(";
                } else if (c.equals(new Bracket("CLOSE", BracketType.CLOSE_BRACKET))) {
                    str += ")";
                } else if (c.equals(new Operator("NOT", NOT))) {
                    str += "!(";
                    not = true;
                } else if (c.equals(new Operator("IMPLIES", OperatorType.IMPLIES))) {
                    str += ">";
                } else if (c.equals(new Operator("ONLY", OperatorType.ONLY))) {
                    str += "~";
                } else {
                    str += c;
                    if (!not) {
                        str += " ";
                    } else {
                        str += ")";
                        not = false;
                    }
                }

            }

            str += ")";
            results.add(str);
        }
        return results;

    }

    private boolean isOperator(String str) {
        String and = StringEscapeUtils.unescapeHtml4("&and;");
        String or = StringEscapeUtils.unescapeHtml4("&or;");
        String implies = StringEscapeUtils.unescapeHtml4("&rarr;");
        String only = StringEscapeUtils.unescapeHtml4("&harr;");

        switch (str) {
            case "AND":
            case "OR":
            case "IMPLIES":
            case "ONLY":
            case "^":
            case "->":
            case "<->":
            case "|": return true;
            default:
                if (str.equals(and) || str.equals(or) || str.equals(implies) || str.equals(only)) {
                    return true;
                }

                return false;
        }
    }

    // Produce a list of propositions found in this expression
    public List<Proposition> listPropositions() {
        List<Proposition> props = new LinkedList<>();

        for (Component expr : expression) {
            if (expr instanceof Proposition) {
                if (isOperator(expr.toString()) || expr.toString().equals("OPEN") || expr.toString().equals("CLOSE")) {
                    continue;
                }
                props.add((Proposition) expr);

            }
        }
        return props;
    }

    @Override
    public String toString() {
        String result = "";
        int count = 0;

        List<String> strings = expression.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        for (String c : strings) {

            result += c.toString();
            if (count < strings.size() - 1) {
                result += " ";
            }
            count++;
        }
        return result;
    }


    private String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }

    // Count number of times the given operator can be found in this expression
    public int countOperator(OperatorType type) {
        int count = 0;
        for (Component c : expression) {
            if (c instanceof Operator) {
                if (((Operator) c).getType() == type) {
                    count++;
                }
            }
        }
        return count;
    }

    // Split expression by given operator
    public List<Expression> splitExpressionBy(OperatorType type) {

        List<Component> thisExpression = expression;


        if (expression.size() <= 1) {
            Expression end = new Expression();
            end.expression.addAll(thisExpression);

            List<Expression> res = new LinkedList<>();
            res.add(end);
            return res;
         }

        // remove brackets if necessary
        if (thisExpression.get(0).toString().equals("OPEN")
                    && thisExpression.get(thisExpression.size() - 1).toString().equals("CLOSE")) {

                thisExpression.remove(0);
                thisExpression.remove(thisExpression.size() - 1);

                Expression temp = new Expression();
                temp.expression.addAll(thisExpression);

                if (!temp.checkBracketValidity()) {
                    thisExpression.add(0, new Bracket("OPEN", OPEN_BRACKET));

                    thisExpression.add(new Bracket("CLOSE", CLOSE_BRACKET));
                }

        }

        List<Expression> result = new LinkedList<>();

        Expression lhsExpr = new Expression();
        Expression rhsExpr = new Expression();

        // generate lhs and rhs expressions
        for (int i = 0; i < thisExpression.size(); i++) {


            if (thisExpression.get(i) instanceof Operator || operatorNames.contains(thisExpression.get(i).toString())) {

                OperatorType t;
                switch (thisExpression.get(i).toString()) {
                    case "AND":
                        t = OperatorType.AND;
                        break;
                    case "OR":
                        t = OperatorType.OR;
                        break;
                    case "IMPLIES":
                        t = OperatorType.IMPLIES;
                        break;
                    case "ONLY":
                        t = OperatorType.ONLY;
                        break;
                    default:
                        t = ((Operator) thisExpression.get(i)).getType();
                }

                List<Component> temp = new LinkedList<>();
                temp.addAll(thisExpression);

                if (t == type) {
                    lhsExpr = new Expression(ruleType);
                    lhsExpr.expression = thisExpression.subList(0, i);


                    rhsExpr = new Expression(ruleType);
                    rhsExpr.expression = temp.subList(i + 1, thisExpression.size());

                    if (lhsExpr.checkBracketValidity() && rhsExpr.checkBracketValidity()) {
                        break;
                    } else {
                        continue;
                    }
                }
            }

        }

        result.add(lhsExpr);
        result.add(rhsExpr);

        return result;


    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        Expression expr2 = (Expression) o;


        // ignore outer brackets
        return toString().equals(expr2.toString()) ||
                ("OPEN " + toString() + " CLOSE").equals(expr2.toString()) ||
                toString().equals("OPEN " + expr2.toString() + " CLOSE") ||
                ("OPEN " + toString() + " CLOSE").equals("OPEN " + expr2.toString() + " CLOSE");
    }

    // Compare propositions and operators of two expressions
    public boolean quickEquals(Expression e2) {
        List<Proposition> list1 = listPropositions();

        List<Proposition> list2 = e2.listPropositions();

        List<String> ops1 = listOperators();
        List<String> ops2 = e2.listOperators();

        return list1.toString().equals(list2.toString()) && ops1.equals(ops2);


    }

    private List<String> listOperators() {
        List<String> res = new LinkedList<>();

        for (Component e : expression) {
            if (isOperator(e.toString())) {
                res.add(e.toString());
            }
        }

        return res;
    }


    public RuleType getRuleType() {
        return ruleType;
    }

    public boolean contains(Component c) {
        List<Component> list = new LinkedList<>();
        list.addAll(expression);

        for (Component component : list) {
            if(c.toString().equals(component.toString())) {
                return true;
            }
        }

        return false;
    }

    // remove n components from the start of the expression
    public void removeNcomponents(int n) {
        for (int i = 0; i < n; i++) {
            expression.remove(0);
        }

    }

    public void removeLast() {
        expression.remove(expression.size()-1);
    }

    // Ensure that all brackets are matching in the string version of the expression
    public static boolean checkBrackets(String str)
    {
        if (str.isEmpty())
            return true;

        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++)
        {
            char current = str.charAt(i);
            if (current == '(')
            {
                stack.push(current);
            }


            if (current == ')')
            {
                if (stack.isEmpty())
                    return false;

                char last = stack.peek();
                if (current == ')' && last == '(')
                    stack.pop();
                else
                    return false;
            }

        }

        return stack.isEmpty();
    }


    // Ensure that all brackets in the expression are matching
    protected boolean checkBracketValidity() {

        Stack<Component> stack = new Stack<>();
        for (int i = 0; i < expression.size(); i++) {

            Component current = expression.get(i);
            if (current.toString().equals("OPEN")) {
                stack.push(current);
            }


            if (current.toString().equals("CLOSE")) {

                if (stack.isEmpty()) {
                    return false;
                }

                Component last = stack.peek();

                if (current.toString().equals("CLOSE") && last.toString().equals("OPEN")) {
                    stack.pop();
                } else {
                    return false;
                }
            }

        }

        return stack.isEmpty();
    }

    public boolean equalExceptFirst(Expression e1) {


        if (expression.get(0).toString().equals("NOT")) {
            expression.remove(0);
            if (equals(e1)) {
                expression.add(0, new Operator("NOT", NOT));
                return true;
            }
            expression.add(0, new Operator("NOT", NOT));
        }

        if (e1.expression.get(0).toString().equals("NOT")) {
            e1.expression.remove(0);
            if (equals(e1)) {
                e1.expression.add(0, new Operator("NOT", NOT));
                return true;
            }
        }
        return false;
    }

    public Component getFirstComp() {
        if (expression.get(0) == null) {
            return null;
        }
        return expression.get(0);
    }

    public Component getLastComp() {
        return expression.get(expression.size()-1);
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public void removeRefs() {
        lines.clear();
    }

    public boolean isLemmaMarked() {
        return lemmaMarked;
    }

    public void setLemmaMarked(boolean lemmaMarked) {
        this.lemmaMarked = lemmaMarked;
    }

    public Integer getLemmaNum() {
        return lemmaNum;
    }

    public void setLemmaNum(Integer lemmaNum) {
        this.lemmaNum = lemmaNum;
    }
}
