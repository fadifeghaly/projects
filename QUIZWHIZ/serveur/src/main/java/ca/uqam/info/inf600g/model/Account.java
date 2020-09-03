package ca.uqam.info.inf600g.model;

public class Account {

    private String identifier;
    private String name;

    public Account() {
    } // required for marshalling purpose

    public Account(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

}
