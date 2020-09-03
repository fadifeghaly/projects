package ca.uqam.info.inf600g.model;

public class HelperAccount extends Account {

    private String password;

    public HelperAccount(String identifier, String name, String password) {
        super(identifier, name);
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
