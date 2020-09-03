package ca.uqam.info.inf600g.services;


import ca.uqam.info.inf600g.data.AccessibilityCollection;
import ca.uqam.info.inf600g.data.AccountsCollection;
import ca.uqam.info.inf600g.model.Accessibility;
import ca.uqam.info.inf600g.model.ResidentAccount;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;

@Path("/accessibility")
@Tag(name = "Accessibility management")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccessibilityService {

    @GET
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Accessibility.class))),
            description = "Get all available accessibilities")
    public ArrayList<Accessibility> getAllAccessibilities() {
        return AccessibilityCollection.getAccess().getAccessibilities();
    }

    @GET
    @Path("/{identifier}")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Accessibility.class))),
            description = "Get resident's accessibilities")
    public ArrayList<String> getResidentAccessibilities(@PathParam("identifier") String ident) {

        ResidentAccount ra = AccountsCollection.getAccess().getResidentByIdentifier(ident);
        if (null == ra)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return ra.getAccessibilities();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiResponse(description = "Add an accessibility")
    public Response addAccessibily(@FormParam("identifier") String ident,
                                   @FormParam("accessibility") String description) {

        ResidentAccount ra = AccountsCollection.getAccess().getResidentByIdentifier(ident);
        if (null == ra)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        ArrayList<String> accessibilities = ra.getAccessibilities();

        if (accessibilities.contains(description)) {
            return Response.status(Response.Status.FOUND).build();
        } else {
            accessibilities.add(description);
            ra.setAccessibilities(accessibilities);
        }
        return Response.status(Response.Status.CREATED).build();

    }


    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiResponse(description = "Update resident accessibilities")
    public Response disableAccessibily(@FormParam("identifier") String ident,
                                       @FormParam("accessibility") String accessibilities) {
        ResidentAccount ra = AccountsCollection.getAccess().getResidentByIdentifier(ident);
        if (null == ra)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        ArrayList<String> updateAccessibilities = new ArrayList<>();
        JSONArray newAccessibilities = new JSONArray(accessibilities);
        for(int i = 0; i < newAccessibilities.length();i++){
            updateAccessibilities.add(newAccessibilities.get(i).toString());
        }
        ra.setAccessibilities(updateAccessibilities);

        return Response.status(Response.Status.ACCEPTED).build();

    }

}
