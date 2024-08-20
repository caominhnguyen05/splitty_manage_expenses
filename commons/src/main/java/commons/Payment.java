package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Payment {
    @Id
    @GeneratedValue(generator = Generator.generatorName)
    @GenericGenerator(name = Generator.generatorName, strategy = "commons.Generator")
    public long id;

    public double amount;
    public String currency;
    public Date date;
    public String payer;
    public String receiver;

    /**
     * Constructs a new Payment with the specified amount, currency, description, and date.
     *
     * @param payer       the "payer" of the payment
     * @param receiver    the receiver of the payment
     * @param amount      the amount of the payment
     * @param currency    the currency of the payment
     */
    public Payment(String payer, String receiver, double amount, String currency) {
        this.id = -1;
        this.payer = payer;
        this.receiver = receiver;
        this.amount = amount;
        this.currency = currency;
        this.date = new Date();
    }

    /**
     * Constructs a new empty Payment.
     */
    public Payment() {

    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }


    /**
     * Returns a hash code value for the payment object.
     *
     * @return a hash code value for this payment object
     */
    @Override
    public int hashCode() {

        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Returns a string representation of the payment object.
     *
     * @return a string representation of the payment object.
     */
    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(this.date);

        return this.payer + " paid " + this.receiver +
                " " + String.format("%.2f", this.amount) + " " +
                this.currency + " on " + formattedDate;
    }
}
