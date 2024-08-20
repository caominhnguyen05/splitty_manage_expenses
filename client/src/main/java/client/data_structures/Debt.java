package client.data_structures;


import java.util.Objects;


public class  Debt {

    private String creditor; // Person who needs to be paid
    private String debtor; // Person who needs to pay
    private double amount;
    /**
     * Constructor for a debt object.
     *
     * @param creditor the person who paid
     * @param debtor   the person who owes the creditor
     * @param amount   amount of money to be paid
     */
    public Debt(String creditor, String debtor, double amount) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
    }

    /**
     * Getter method for the creditor.
     *
     * @return the creditor's name
     */
    public String getCreditor() {
        return creditor;
    }

    /**
     * Getter for the debtor.
     *
     * @return the debtor's name
     */
    public String getDebtor() {
        return debtor;
    }

    /**
     * Getter for the amount of money.
     *
     * @return the amount of money debtor should pay the creditor
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Adds an amount to the debt from Creditor to Debtor
     *
     * @param amount the amount to add
     */
    /**
     * sets amount
     * @param amount amount of debt
     */
    public void setAmount(double amount) { this.amount = amount; }

    /**
     * adds to the amount
     * @param amount
     */
    public void addAmount(double amount) {
        this.amount += amount;
    }

    /**
     * Equals method for a Debt
     *
     * @param o the object to compare with
     * @return boolean value
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        Debt that = (Debt) o;
        return Double.compare(amount, that.amount) == 0 && Objects.equals(creditor, that.creditor)
                && Objects.equals(debtor, that.debtor);
    }

    /**
     * Hashcode method for a Debt
     *
     * @return integer hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(creditor, debtor, amount);
    }

    /**
     * toString method for a Debt
     *
     * @return string representation of a debt
     */
    @Override
    public String toString() {
        return debtor + " owes " + amount + " to " + creditor;
    }
}
