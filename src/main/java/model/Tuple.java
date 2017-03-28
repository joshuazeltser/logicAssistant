package model;

/**
 * Created by joshuazeltser on 28/03/2017.
 */
public class Tuple {

    private String proof;

    private String rule;

    public Tuple(String proof, String rule) {
        this.proof = proof;
        this.rule = rule;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return proof + " , " + rule;
    }
}
