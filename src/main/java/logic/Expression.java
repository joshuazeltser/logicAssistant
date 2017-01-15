package logic;


import java.util.LinkedList;
import java.util.List;


/**
 * Created by joshuazeltser on 03/01/2017.
 */
public class Expression {

    private List<Component> expression;

    private int id;

    private String info;


    public Expression() {
        expression = new LinkedList<>();
    }

    public void addToExpression(String input) {

        String[] tokens = input.split(" ");

        for (String token : tokens) {
            switch(token){
                case "|": expression.add(new Operator("OR", OperatorType.OR)); break;
                case "^": expression.add(new Operator("AND", OperatorType.AND)); break;
                case "->": expression.add(new Operator("IMPLIES", OperatorType.IMPLIES)); break;
                case "<->": expression.add(new Operator("ONLY", OperatorType.ONLY)); break;
                case "(": expression.add(new Operator("OPEN", OperatorType.OPEN_BRACKET)); break;
                case ")": expression.add(new Operator("CLOSE", OperatorType.CLOSE_BRACKET)); break;
                default:
                    token = token.replaceAll("\\p{P}","");
                    expression.add(new Proposition(token));
            }
        }
    }

    public List<Proposition> listPropositions() {
        List<Proposition> props = new LinkedList<>();

        for (Component expr : expression) {
            if (expr instanceof Proposition) {
                props.add((Proposition) expr);

            }
        }
        return props;
    }

    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    @Override
    public String toString() {
        String result = "";
        for (Component c : expression) {
            result += c.toString() + " ";
        }
        return result;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
