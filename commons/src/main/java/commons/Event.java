package commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    @Id
    @GeneratedValue(generator = Generator.generatorName)
    @GenericGenerator(name = Generator.generatorName, strategy = "commons.Generator")
    public long id;

    public String name;
    public Date creationDate;

    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<Participant> participants;

    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<Payment> payments;

    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<Expense> expenses;

    @OneToMany(cascade = CascadeType.REMOVE)
    public List<Tag> tags;

    /**
     * Constructs a new event with the given name.
     *
     * @param name event's name
     */
    public Event(String name) {
        this.id = -1;
        this.name = name;
        this.creationDate = new Date();
        participants = new ArrayList<>();
        payments = new ArrayList<>();
        expenses = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Empty constructor for an event.
     */
    public Event() {
        participants = new ArrayList<>();
        payments = new ArrayList<>();
        expenses = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Equals method for an Event.
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

    /**
     * Creates a String representation of all the participants in this Event
     * @return returns the generated String
     */
    public String participantsString() {
        StringBuilder result = new StringBuilder();

        if (!this.participants.isEmpty()) {
            for (int i = 0; i < this.participants.size() - 1; i++) {
                result.append(this.participants.get(i).name);
                result.append(", ");
            }
            result.append(this.participants.getLast().name);
        }
        else result.append("no participants");

        return result.toString();
    }

    /**
     * Setter method for the tags list: is a list of available tags for this event
     * @param tags List of Tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
