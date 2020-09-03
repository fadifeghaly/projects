package ca.uqam.info.inf600g.model;

public class Accessibility {

    private String description;

    public Accessibility() {
    } // required for marshalling purpose

    public Accessibility(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
