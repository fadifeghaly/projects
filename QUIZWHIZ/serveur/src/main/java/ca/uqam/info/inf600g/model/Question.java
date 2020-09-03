package ca.uqam.info.inf600g.model;


public class Question {

    private String question;
    private String choice1;
    private String choice2;
    private String choice3;
    private int correctAnswer;

    public Question() {
    } // required for marshalling purpose

    public Question(String question, String choice1, String choice2, String choice3, int correctAnswer) {
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getChoice1() {
        return choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}

