package server;

import java.time.LocalDate;


public class CurrencyConvertor {

    private static final double USD = 0.92;
    private static final double CHF = 1.02;

    /**
     * constructor for currency convertor
     */
    public CurrencyConvertor() {
    }

    /**
     * implementation of currency convertor
     * @param current to be converted
     * @param toConvert to be converted to
     * @param date date of conversion
     * @return rate
     */
    public double getRate(String current, String toConvert, LocalDate date) {

        if(current.equals(toConvert)) return 1;

        // Calculate a symmetric adjustment factor based on the date
        double adjustmentFactorUSD = 1 + Math.sin(date.toEpochDay()) / 20.0;
        double adjustmentFactorCHF = 1 + Math.cos(date.toEpochDay()) / 20.0;

        // Apply the adjustment factor to both currencies
        double adjustedUSD = USD * adjustmentFactorUSD;
        double adjustedCHF = CHF * adjustmentFactorCHF;

        double rate;

        if(current.equals("EUR")) {

            if(toConvert.equals("USD")) rate = 1/adjustedUSD;
            else rate = 1/adjustedCHF;

        } else if(current.equals("USD")) {

            if(toConvert.equals("EUR")) rate = adjustedUSD;
            else rate = adjustedUSD/adjustedCHF;

        } else {

            if(toConvert.equals("USD")) rate = adjustedCHF/adjustedUSD;
            else rate = adjustedCHF;
        }

        return rate;
    }

}
