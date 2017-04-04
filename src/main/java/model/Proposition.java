package model;

/**
 * Created by joshuazeltser on 03/01/2017.
 */
public class Proposition implements Component{

    private String name;

    public Proposition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
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

        Proposition expr2 = (Proposition) o;

        return toString().equals(expr2.toString());
    }
}
