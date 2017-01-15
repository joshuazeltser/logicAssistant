/**
 * Created by joshuazeltser on 03/01/2017.
 */
public class TestMain {

    public static void main(String[] args) {

        BeanFactory factory = new XmlBeanFactory(new FileSystemResource("spring.xml"));

//        String test1 = "(A ^ B) | C";
//
//        Expression expr = new Expression();
//
//        expr.addToExpression(test1);
//
//        System.out.println(expr.toString());
//
//        for (Proposition p : expr.listPropositions()) {
//            System.out.print(p.toString() + " ");
//        }
    }
}
