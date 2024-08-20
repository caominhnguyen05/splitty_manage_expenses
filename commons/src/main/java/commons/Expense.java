package commons;

import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Expense {
    @Id
    @GeneratedValue(generator = Generator.generatorName)
    @GenericGenerator(name = Generator.generatorName, strategy = "commons.Generator")
    public long id;

    public String title;
    public double amount;
    public String currency;
    @ManyToOne
    public Tag type;
    public Date creationDate;
    public LocalDate date;

    @ManyToOne(cascade = CascadeType.REMOVE)
    public Participant creditor;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)

    public List<Participant> debtors;

    /**
     * Constructs a new expense with the specified title, amount of money,
     * currency and expense type.
     *
     * @param title    the expense's title
     * @param amount   amount of money spent
     * @param currency the currency
     * @param type     the expense's type
     * @param date     the date on which the expense is made
     */
    public Expense(String title, double amount, String currency, Tag type, LocalDate date) {
        this.id = -1;
        this.title = title;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.creationDate = new Date();
        this.date = date;
        this.debtors = new ArrayList<>();
    }

    /**
     * Empty constructor for an expense
     */
    @SuppressWarnings("unused")
    public Expense() {
        this.debtors = new ArrayList<>();
    }

    /**
     * Equals method for an expense.
     *
     * @param obj the other object to compare to
     * @return true if this is equal to that object
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hashcode method for an expense object.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * String representation of an expense.
     *
     * @return a string describing the expense
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
