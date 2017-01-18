package model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by joshuazeltser on 03/01/2017.
 */
public interface Component {

    String toString();

    @SpringBootApplication
    class Application {

            public static void main(String[] args) {
                        SpringApplication.run(Application.class, args);
                        }

    }
}
