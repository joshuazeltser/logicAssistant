package logic;

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
}
