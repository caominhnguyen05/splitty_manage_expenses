package commons;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

@Entity
public class Tag {
    @Id
    @GeneratedValue(generator = Generator.generatorName)
    @GenericGenerator(name = Generator.generatorName, strategy = "commons.Generator")
    public long id;

    @Column
    private String name;

    @Column
    private String color;

    /**
     * Constructor for a Tag, setting this.name and this.color
     * @param name String value of the name
     * @param color String representation of the color
     */
    public Tag(String name, String color) {
        this.id = -1;
        this.name = name;
        this.color = color;
    }

    /**
     * Empty constructor for a Tag
     */
    public Tag() {
    }

    /**
     * Getter for the name attribute of this object
     * @return returns the String value of the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the color attribute of this object
     * @return returns the String representation of the color
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for the name attribute of this Tag
     * @param name String value of the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for the color attribute of this Tag
     * @param color String representation of Color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Equals method for comparing this Tag to another object
     * @param o other object
     * @return returns true if other is determined to be equal to this, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;

        if (!Objects.equals(id, tag.id)) return false;
        if (!Objects.equals(name, tag.name)) return false;
        return Objects.equals(color, tag.color);
    }

    /**
     * Generates a hashcode of this Tag
     *
     * @return returns generated hashcode
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }

    /**
     * Creates a String representation of this Tag
     *
     * @return returns the created String
     */
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    /**
     * Getter for the id
     * @return returns the id of this Tag
     */
    public Long getId() {
        return id;
    }
}
