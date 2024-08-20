package other;

import org.junit.jupiter.api.Test;
import server.CurrencyConvertor;

import java.time.LocalDate;
import java.util.Date;



public class CurrencyConvertorTest {

    CurrencyConvertor cc = new CurrencyConvertor();

    @Test
    public void test1() {

        String first = "EUR";
        String sec = "chf";
        LocalDate date = LocalDate.of(2024,1,1);

        System.out.println(date.toEpochDay());

        System.out.println(cc.getRate(first, sec, date));
    }

    @Test
    public void test2() {

    }
}
