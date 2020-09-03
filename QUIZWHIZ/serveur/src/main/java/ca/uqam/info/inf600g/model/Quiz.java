package ca.uqam.info.inf600g.model;

import java.util.Set;

public class Quiz {

    private String label;
    private String name;
    private String description;
    private Set<Question> questionsList;

    public Quiz() {
    } // required for marshalling purpose

    public Quiz(String label, String name, String description, Set<Question> questionsList) {
        this.label = label;
        this.name = name;
        this.description = description;
        this.questionsList = questionsList;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Question> getQuestionsList() {
        return questionsList;
    }
}
