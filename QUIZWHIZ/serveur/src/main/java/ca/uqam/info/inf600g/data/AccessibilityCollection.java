package ca.uqam.info.inf600g.data;

import ca.uqam.info.inf600g.model.Accessibility;

import java.util.ArrayList;

public class AccessibilityCollection {

    /*************************************************
     * Static access to the accessibility collection *
     *************************************************/

    private static AccessibilityCollection database;

    public static AccessibilityCollection getAccess() {
        if (database == null)
            database = new AccessibilityCollection();
        return database;
    }

    private ArrayList<Accessibility> accessibilities = new ArrayList<>();

    private AccessibilityCollection() {
        initialize();
    }

    public ArrayList<Accessibility> getAccessibilities() {
        return accessibilities;
    }

    private void initialize() {
        Accessibility acc1 = new Accessibility("TTS");
        Accessibility acc2 = new Accessibility("Désactiver la correction");
        Accessibility acc3 = new Accessibility("DMLA");
        this.accessibilities.add(acc1);
        this.accessibilities.add(acc2);
        this.accessibilities.add(acc3);
    }
}
