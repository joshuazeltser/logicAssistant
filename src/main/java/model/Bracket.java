package model;

/**
 * Created by joshuazeltser on 22/06/2017.
 */
public class Bracket implements Component {


    private String bracketName;

    private BracketType bracketType;


    public Bracket(String bracketName, BracketType bracketType) {
        this.bracketName = bracketName;
        this.bracketType = bracketType;
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

        Bracket expr2 = (Bracket) c;

        return toString().equals(expr2.toString());
    }

    @Override
    public synchronized String toString() {
        return bracketName;
    }

    public String getBracketName() {
        return bracketName;
    }

    public void setBracketName(String bracketName) {
        this.bracketName = bracketName;
    }

    public BracketType getBracketType() {
        return bracketType;
    }

    public void setBracketType(BracketType bracketType) {
        this.bracketType = bracketType;
    }
}

