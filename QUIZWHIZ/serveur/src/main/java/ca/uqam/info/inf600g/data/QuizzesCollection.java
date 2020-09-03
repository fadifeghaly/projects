package ca.uqam.info.inf600g.data;

import ca.uqam.info.inf600g.model.Quiz;

import java.util.*;

public class QuizzesCollection {

    /*******************************************
     * Static access to the quiz collection *
     *******************************************/

    private static QuizzesCollection database;

    public static QuizzesCollection getAccess() {
        if (database == null)
            database = new QuizzesCollection();
        return database;
    }

    /*******************************************
     * Private construction and data structure *
     *******************************************/

    private QuizzesFactory qf;

    public QuizzesCollection() {
        qf = new QuizzesFactory();
    }

    /******************************
     * Quiz collection interface *
     ******************************/

    public HashSet<Quiz> getAllQuizzes() {
        return new HashSet<>(qf.quizzesDB.values());
    }

    public Quiz findQuizByLabel(String label) {
        if (qf.quizzesDB.containsKey(label))
            return qf.quizzesDB.get(label);
        return null;
    }
}
