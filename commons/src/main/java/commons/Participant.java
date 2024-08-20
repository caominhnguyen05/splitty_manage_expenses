package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Participant {
    @Id
    @GeneratedValue(generator = Generator.generatorName)
    @GenericGenerator(name = Generator.generatorName, strategy = "commons.Generator")
    public long id;

    public String name;
    public String iban;
    public String bic;
    public String email;

    /**
     * Constructs a new Participant with the specified name, IBAN, BIC, and email.
     *
     * @param name  the name of the participant
     * @param iban  the IBAN of the participant
     * @param bic   the BIC of the participant
     * @param email the email address of the participant
     */
    public Participant(String name, String iban, String bic, String email) {
        this.id = -1;
        this.name = name;
        this.iban = iban;
        this.bic = bic;
        this.email = email;
    }

    /**
     * Constructs a new empty Participant.
     */
    @SuppressWarnings("unused")
    public Participant() {

    }

    /**
     * Indicates whether another object is equal to this
     *
     * @param obj the object to compare to
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hashcode method for participant.
     * @return hash code value.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * String representation of the participant.
     * @return string containing details about the participant
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
