package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.CurrencyConvertor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private CurrencyConvertor cc = new CurrencyConvertor();

    /**
     * getter for conversion rate between two currencies
     * @param from currency to convert
     * @param to to be converted to
     * @param date date of the expense/conversion
     * @return the rate which is mostly a number around 1
     */
    @GetMapping(path = {"/{from}/{to}/{date}"})
    public ResponseEntity<Double> getRate(@PathVariable("from") String from,
                                          @PathVariable("to") String to,
                                          @PathVariable("date")LocalDate date) {

        List<String> currencies = Arrays.asList("EUR", "USD", "CHF");

        if(!currencies.contains(from) || !currencies.contains(to)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(cc.getRate(from, to, date));

    }
}
