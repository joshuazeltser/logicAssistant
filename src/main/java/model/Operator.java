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
    public synchronized String toString() {
        return name;
    }

    @Override
    public boolean equals(Component c) {
        if (this == c) {
            return true;
        }

        if (c == null) {
            return false;
        }

        if (getClass() != c.getClass()) {
            return false;
        }

        Operator expr2 = (Operator) c;

        return name.equals(expr2.name) && type.equals(expr2.type);
    }


}
