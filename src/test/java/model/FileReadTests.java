package model;

import org.junit.Test;

import java.util.List;

/**
 * Created by joshuazeltser on 28/03/2017.
 */
public class FileReadTests {

    @Test
    public void readFile() throws SyntaxException {
        Proof proof = new Proof();

        String result = proof.proofFromFile("testFiles/test1.la");

        System.out.println(result.toString());

    }
}
