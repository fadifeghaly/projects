package ca.uqam.info.inf600g.data;

import ca.uqam.info.inf600g.model.HelperAccount;
import ca.uqam.info.inf600g.model.ResidentAccount;

import java.util.*;


public class AccountsCollection {

    /*******************************************
     * Static access to the account collection *
     *******************************************/

    private static AccountsCollection database;

    public static AccountsCollection getAccess() {
        if (database == null)
            database = new AccountsCollection();
        return database;
    }


    /*******************************************
     * Private construction and data structure *
     *******************************************/

    private Map<String, ResidentAccount> ResidentsAccounts;
    private Map<String, HelperAccount> HelpersAccounts;

    private AccountsCollection() {
        this.ResidentsAccounts = new HashMap<>();
        this.HelpersAccounts = new HashMap<>();
        initialize();
    }

    /********************************
     * Account collection interface *
     ********************************/

    public Set<ResidentAccount> getResidentsAccounts() {
        return new HashSet<>(this.ResidentsAccounts.values());
    }

    public Set<HelperAccount> getHelpersAccounts() {
        return new HashSet<>(this.HelpersAccounts.values());
    }

    public ResidentAccount getResidentByIdentifier(String identifier) {
        if (this.ResidentsAccounts.containsKey(identifier))
            return this.ResidentsAccounts.get(identifier);
        return null;
    }

    public HelperAccount getHelperByIdentifier(String identifier) {
        if (this.HelpersAccounts.containsKey(identifier))
            return this.HelpersAccounts.get(identifier);
        return null;
    }

    public Map<String, Map<String, ArrayList<Integer>>> getResidentsScores() {
        Map<String, Map<String, ArrayList<Integer>>> results = new HashMap<>();

        for (Map.Entry<String, ResidentAccount> entry : ResidentsAccounts.entrySet())
            results.put(entry.getValue().getName(), entry.getValue().getScore());

        return results;
    }

    public void setResidentsAccounts(Map<String, ResidentAccount> residentsAccounts) {
        ResidentsAccounts = residentsAccounts;
    }

    public Map<String, ResidentAccount> getResidentsAccountsMap() {
        return ResidentsAccounts;
    }

    /****************
     * Data for MVP *
     ****************/

    private void initialize() {
        HelperAccount laurie = new HelperAccount(
                "laurie",
                "Laurie",
                "123456");
        this.HelpersAccounts.put(laurie.getIdentifier(), laurie);
        ////
        ResidentAccount ginette = new ResidentAccount(
                "ginette",
                "Ginette");
        ginette.setPointOfInterest(Arrays.asList("Allemagne", "Histoire", "Politique"));
        ginette.fetchQuizzesList();
        ginette.initProfil();
        this.ResidentsAccounts.put(ginette.getIdentifier(), ginette);
        ////
        ResidentAccount robert = new ResidentAccount(
            "robert",
            "Robert");
        robert.setPointOfInterest(Arrays.asList("Olympiques", "Allemagne"));
        robert.fetchQuizzesList();
        robert.initProfil();
        this.ResidentsAccounts.put(robert.getIdentifier(), robert);
    }

}
