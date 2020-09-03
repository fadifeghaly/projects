package ca.uqam.info.inf600g.services;

import ca.uqam.info.inf600g.data.AccountsCollection;
import ca.uqam.info.inf600g.model.HelperAccount;
import ca.uqam.info.inf600g.model.Quiz;
import ca.uqam.info.inf600g.model.ResidentAccount;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


@Path("/accounts")
@Tag(name = "Account management")
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {

    @Path("residents")
    @GET
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ResidentAccount.class))),
            description = "List of all available accounts")
    public Set<ResidentAccount> getAllResidentsAccounts() {
        return AccountsCollection.getAccess()
                .getResidentsAccounts();
    }

    @POST
    @Path("/resident")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response uploadFile(@FormDataParam("file") InputStream fileInputStream,
                               @FormDataParam("name") String name,
                               @FormDataParam("identifier") String ident,
                               @FormDataParam("pointsIntereset") String pointsInterest) throws Exception {
        String UPLOAD_PATH = "static/";
        ident = ident.replaceAll("\\s", "").toLowerCase();
        ResidentAccount res = AccountsCollection.getAccess().getResidentByIdentifier(ident);
        if (res == null) {

            try {
                int read = 0;
                byte[] bytes = new byte[1024];

                OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + ident + ".png"));
                while ((read = fileInputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
            } catch (IOException e) {
                throw new WebApplicationException("Error while uploading file. Please try again !!");
            }

            JSONArray listInterests = new JSONArray(pointsInterest);
            ArrayList<String> arrayListInterests = new ArrayList<>();
            for (int i = 0; i < listInterests.length(); i++) {
                arrayListInterests.add(listInterests.get(i).toString());
            }
            ResidentAccount ra = new ResidentAccount(ident, name);
            ra.setPointOfInterest(arrayListInterests);
            ra.fetchQuizzesList();
            ra.initProfil();
            Map<String, ResidentAccount> residents = AccountsCollection.getAccess().getResidentsAccountsMap();
            residents.put(ra.getIdentifier(), ra);
            AccountsCollection.getAccess().setResidentsAccounts(residents);
            return Response.ok().build();
        }
        return Response.status(999).build();
    }


    @Path("helpers")
    @GET
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = HelperAccount.class))),
            description = "List of all available accounts")
    public Set<HelperAccount> getAllHeleprsAccounts() {
        return AccountsCollection.getAccess()
                .getHelpersAccounts();
    }

    @GET
    @Path("resident/{identifier}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Quiz.class)),
            description = "profil of a given resident")
    public ResidentAccount getResidentProfilByIdentifier(@PathParam("identifier") String ident) {
        ResidentAccount ra = AccountsCollection.getAccess().getResidentByIdentifier(ident);
        if (null == ra)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return ra;
    }

    @GET
    @Path("helper/{identifier}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Quiz.class)),
            description = "profil of a given helper")
    public HelperAccount getHelperProfilByIdentifier(@PathParam("identifier") String ident) {
        HelperAccount ha = AccountsCollection.getAccess().getHelperByIdentifier(ident);
        if (null == ha)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return ha;
    }

}
