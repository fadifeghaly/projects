package ca.uqam.info.inf600g.model;

import ca.uqam.info.inf600g.data.QuizzesCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResidentAccount extends Account {

    private List<String> pointOfInterest;
    private List<Quiz> quizzesList = new ArrayList<>();
    private Map<String, ArrayList<Integer>> score = new HashMap<>();
    private Map<String, ArrayList<Integer>> durationAverageAccOFF = new HashMap<>();
    private Map<String, ArrayList<Integer>> durationAverageAccON = new HashMap<>();
    private Map<String, ArrayList<String>> correctAnswersAccOFF = new HashMap<>();
    private Map<String, ArrayList<String>> correctAnswersAccON = new HashMap<>();
    private Map<String, ArrayList<String>> wrongAnswersAccOFF = new HashMap<>();
    private Map<String, ArrayList<String>> wrongAnswersAccON = new HashMap<>();
    private ArrayList<String> accessibilities = new ArrayList<>();

    public ResidentAccount(String identifier, String name) {
        super(identifier, name);
    }

    public List<String> getPointOfInterest() {
        return pointOfInterest;
    }

    public void setPointOfInterest(List<String> pointOfInterest) {
        this.pointOfInterest = pointOfInterest;
    }

    public List<Quiz> getQuizzesList() {
        return quizzesList;
    }

    public Map<String, ArrayList<Integer>> getScore() {
        return score;
    }

    public Map<String, ArrayList<String>> getCorrectAnswersAccOFF() {
        return correctAnswersAccOFF;
    }

    public Map<String, ArrayList<String>> getCorrectAnswersAccON() {
        return correctAnswersAccON;
    }

    public Map<String, ArrayList<String>> getWrongAnswersAccOFF() {
        return wrongAnswersAccOFF;
    }

    public Map<String, ArrayList<String>> getWrongAnswersAccON() {
        return wrongAnswersAccON;
    }

    public Map<String, ArrayList<Integer>> getDurationAverageAccOFF() {
        return durationAverageAccOFF;
    }

    public Map<String, ArrayList<Integer>> getDurationAverageAccON() {
        return durationAverageAccON;
    }

    public void setQuizzesList(List<Quiz> quizzesList) {
        this.quizzesList = quizzesList;
    }

    public void setAccessibilities(ArrayList<String> accessibilities) {
        this.accessibilities = accessibilities;
    }

    public ArrayList<String> getAccessibilities() {
        return accessibilities;
    }

    public void initScore() {
        for (String label : pointOfInterest) {
            score.put(label, new ArrayList<>());
        }
    }

    public void initTimePerQuizAccON() {
        for (String label : pointOfInterest) {
            durationAverageAccON.put(label, new ArrayList<>());
        }
    }

    public void initTimePerQuizAccOFF() {
        for (String label : pointOfInterest) {
            durationAverageAccOFF.put(label, new ArrayList<>());
        }
    }

    public void initCorrectAnswersAccON() {
        for (String label : pointOfInterest) {
            correctAnswersAccON.put(label, new ArrayList<>());
        }
    }

    public void initCorrectAnswersAccOFF() {
        for (String label : pointOfInterest) {
            correctAnswersAccOFF.put(label, new ArrayList<>());
        }
    }

    public void initWrongAnswersAccOFF() {
        for (String label : pointOfInterest) {
            wrongAnswersAccOFF.put(label, new ArrayList<>());
        }
    }

    public void initWrongAnswersAccON() {
        for (String label : pointOfInterest) {
            wrongAnswersAccON.put(label, new ArrayList<>());
        }
    }

    public void fetchQuizzesList() {
        for (String label : pointOfInterest) {
            Quiz quiz = QuizzesCollection.getAccess().findQuizByLabel(label);
            quizzesList.add(quiz);
        }
    }

    public void initProfil() {
        initScore();
        initCorrectAnswersAccOFF();
        initCorrectAnswersAccON();
        initWrongAnswersAccOFF();
        initWrongAnswersAccON();
        initTimePerQuizAccON();
        initTimePerQuizAccOFF();
    }

}
