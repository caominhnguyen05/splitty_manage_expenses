package client.data_structures;

public class Pair<Participant, Double> {
    private Participant participant;
    private Double balance;

    /**
     * creates a new Pair object
     * @param participant
     * @param balance
     */
    public Pair(Participant participant, Double balance) {
        this.participant = participant;
        this.balance = balance;
    }

    /**
     * returns the Participant object of the current Pair
     * @return Participant
     */
    public Participant getParticipant() {
        return participant;
    }

    /**
     * sets a new Participant for the current Pair
     * @param participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     *
     * @return the amount/balance of the Participant of the current Pair
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * sets a new balance for the Participant of the current Pair
     * @param balance
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
