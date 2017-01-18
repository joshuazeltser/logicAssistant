package model;

/**
 * Created by joshuazeltser on 03/01/2017.
 */
public class Operator implements Component{

    public String name;

    public OperatorType type;

    public Operator(String name, OperatorType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperatorType getType() {
        return type;
    }

    public void setType(OperatorType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }


}
