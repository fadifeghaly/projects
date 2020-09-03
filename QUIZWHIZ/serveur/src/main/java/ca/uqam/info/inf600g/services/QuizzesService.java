package ca.uqam.info.inf600g.services;

import ca.uqam.info.inf600g.data.QuizzesCollection;
import ca.uqam.info.inf600g.model.Quiz;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Set;

@Path("/quizzes")
@Tag(name = "Quiz viewer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuizzesService {


    @GET
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Quiz.class))),
            description = "List of all available quizzes")
    public Set<Quiz> getAllQuizzes() {
        return QuizzesCollection.getAccess().getAllQuizzes();
    }

    @GET
    @Path("/{label}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Quiz.class)),
            description = "Find a quiz using its label")
    public Quiz getGivenQuiz(@PathParam("label") String l) {
        Quiz quiz = QuizzesCollection.getAccess().findQuizByLabel(l);
        if (null == quiz)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return quiz;
    }

}