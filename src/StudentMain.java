import operations.*;
import student.kt150599_ArticleOperation;
import student.kt150599_BuyerOperations;
import student.kt150599_CityOperations;
import student.kt150599_GeneralOperations;
import student.kt150599_OrderOperations;
import student.kt150599_ShopOperations;
import student.kt150599_TransactionOperations;

import org.junit.Test;
import tests.TestHandler;
import tests.TestRunner;

import java.util.Calendar;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = new kt150599_ArticleOperation(); // Change this for your implementation (points will be negative if interfaces are not implemented).
        BuyerOperations buyerOperations = new kt150599_BuyerOperations();
        CityOperations cityOperations = new kt150599_CityOperations();
        GeneralOperations generalOperations = new kt150599_GeneralOperations();
        OrderOperations orderOperations = new kt150599_OrderOperations();
        ShopOperations shopOperations = new kt150599_ShopOperations();
        TransactionOperations transactionOperations = new kt150599_TransactionOperations();
//
//        Calendar c = Calendar.getInstance();
//        c.clear();
//        c.set(2010, Calendar.JANUARY, 01);
//
//
//        Calendar c2 = Calendar.getInstance();
//        c2.clear();
//        c2.set(2010, Calendar.JANUARY, 01);
//
//        if(c.equals(c2)) System.out.println("jednako");
//        else System.out.println("nije jednako");

        TestHandler.createInstance(
                articleOperations,
                buyerOperations,
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,
                transactionOperations
        );

        TestRunner.runTests();
    }
}
