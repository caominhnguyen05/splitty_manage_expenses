package client.data_structures;

public class Tag {

    private String name;
    private String backgroundColor;

    /**
     * Constructor for a tag
     *
     * @param name                  the name of the tag
     * @param backgroundColor       the background color of the tag
     */
    public Tag(String name, String backgroundColor) {
        this.name = name;
        this.backgroundColor = backgroundColor;
    }

    /**
     * Getter for name
     * @return  the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for backgroundColor
     * @return  the background color
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

}
