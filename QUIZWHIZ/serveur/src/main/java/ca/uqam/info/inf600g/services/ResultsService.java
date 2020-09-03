package ca.uqam.info.inf600g.services;

import ca.uqam.info.inf600g.data.AccountsCollection;
import ca.uqam.info.inf600g.model.Accessibility;
import ca.uqam.info.inf600g.model.ResidentAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONArray;
import org.json.CDL;
import org.apache.commons.io.FileUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Path("/results")
@Tag(name = "Results management")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResultsService {


    @GET
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json"),
            description = "ScoreBoard")
    public String getScoreBoard() {

        Map<String, Map<String, ArrayList<Integer>>> results = AccountsCollection.getAccess().getResidentsScores();
        ObjectMapper objectMapper = new ObjectMapper();
        String scores = "";
        try {
            scores = objectMapper.writeValueAsString(results);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return scores;
    }

    @GET
    @Path("/{identifier}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json"),
            description = "ScoreBoard")
    public String getResidentScore(@PathParam("identifier") String ident) {

        ResidentAccount ra = AccountsCollection.getAccess().getResidentByIdentifier(ident);
        if (null == ra)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return String.valueOf(ra.getScore());
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiResponse(description = "Update resident's score")
    public Map.Entry<String, ArrayList<Integer>> updateScore(@FormParam("identifier") String ident,
                                                             @FormParam("label") String label,
                                                             @FormParam("score") int score) {

        ResidentAccount ra = AccountsCollection.getAccess().getResidentByIdentifier(ident);
        Map.Entry<String, ArrayList<Integer>> res = null;
        if (null == ra)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        for (Map.Entry<String, ArrayList<Integer>> entry : ra.getScore().entrySet()) {
            if (entry.getKey().equals(label)) {
                entry.getValue().add(score);
                res = entry;
            }
        }
        return res;
    }

    @POST
    @Path("/timePerQuiz")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiResponse(description = "Save correct answers")
    public void saveCorrectAnswers(@FormParam("identifier") String ident,
                                   @FormParam("label") String label,
                                   @FormParam("time") int time,
                                   @FormParam("accFlag") String accFlag) {

        ResidentAccount ra = AccountsCollection.getAccess().getResidentByIdentifier(ident);
        Map<String, ArrayList<Integer>> target;
        if (accFlag.equals("ACC_ON"))
            target = ra.getDurationAverageAccON();
        else
            target = ra.getDurationAverageAccOFF();
        if (null == ra)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        for (Map.Entry<String, ArrayList<Integer>> entry : target.entrySet()) {
            if (entry.getKey().equals(label))
                entry.getValue().add(time);
        }
    }

    @GET
    @Path("timePerQuiz/{label}/{identifier}/{accFlag}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json"),
            description = "Get time average per quiz")
    public ArrayList<Integer> getResidentAnswers(@PathParam("label") String label,
                                                 @PathParam("identifier") String ident,
                                                 @PathParam("accFlag") String accFlag) {

        ResidentAccount ra = AccountsCollection.getAccess().getResidentByIdentifier(ident);
        Map<String, ArrayList<Integer>> target;
        if (accFlag.equals("ACC_ON"))
            target = ra.getDurationAverageAccON();
        else
            target = ra.getDurationAverageAccOFF();
        ArrayList<Integer> res = null;
        if (null == ra)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        for (Map.Entry<String, ArrayList<Integer>> entry : target.entrySet()) {
            if (entry.getKey().equals(label))
                res = entry.getValue();
        }
        return res;
    }

    @POST
    @Path("/logAnswers/{identifier}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiResponse(description = "Save resident's answers")
    public void saveCorrectAnswers(@FormParam("identifier") String ident,
                                   @FormParam("label") String label,
                                   @FormParam("question") String question,
                                   @FormParam("answerFlag") String answerFlag,
                                   @FormParam("accFlag") String accFlag) {

        Map<String, ArrayList<String>> target;
        ResidentAccount ra = AccountsCollection.getAccess().getResidentByIdentifier(ident);

        if (null == ra)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        if (answerFlag.equals("correct") && accFlag.equals("ACC_ON"))
            target = ra.getCorrectAnswersAccON();
        else if (answerFlag.equals("correct") && accFlag.equals("ACC_OFF"))
            target = ra.getCorrectAnswersAccOFF();
        else if (answerFlag.equals("wrong") && accFlag.equals("ACC_ON"))
            target = ra.getWrongAnswersAccON();
        else
            target = ra.getWrongAnswersAccOFF();

        for (Map.Entry<String, ArrayList<String>> entry : target.entrySet()) {
            if (entry.getKey().equals(label)) {
                entry.getValue().add(question);
            }
        }

    }

    @GET
    @Path("answers/{identifier}/{label}/{answerFlag}/{accFlag}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json"),
            description = "Get resident's answers")
    public Map.Entry<String, ArrayList<String>> getResidentAnswers(@PathParam("identifier") String ident,
                                                                   @PathParam("label") String label,
                                                                   @PathParam("answerFlag") String answerFlag,
                                                                   @PathParam("accFlag") String accFlag) {

        Map<String, ArrayList<String>> target;
        Map.Entry<String, ArrayList<String>> res = null;
        ResidentAccount ra = AccountsCollection.getAccess().getResidentByIdentifier(ident);

        if (answerFlag.equals("correct") && accFlag.equals("ACC_ON"))
            target = ra.getCorrectAnswersAccON();
        else if (answerFlag.equals("correct") && accFlag.equals("ACC_OFF"))
            target = ra.getCorrectAnswersAccOFF();
        else if (answerFlag.equals("wrong") && accFlag.equals("ACC_ON"))
            target = ra.getWrongAnswersAccON();
        else
            target = ra.getWrongAnswersAccOFF();

        if (null == ra)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        for (Map.Entry<String, ArrayList<String>> entry : target.entrySet()) {
            if (entry.getKey().equals(label)) {
                res = entry;
            }
        }
        return res;
    }

    @PUT
    @Path("/data")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiResponse(description = "Saves context data")
    public Response saveContextData(@FormParam("identifier") String ident,
                                    @FormParam("data") String data) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
        Date resultdate = new Date(System.currentTimeMillis());
        JSONArray contextData = new JSONArray(data);
        File file = new File("static/data/" + ident + "_" + sdf.format(resultdate) + ".csv");
        String csv = CDL.toString(contextData);
        FileUtils.writeStringToFile(file, csv, "UTF-8");

        return Response.status(Response.Status.ACCEPTED).build();
    }

}