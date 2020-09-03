package ca.uqam.info.inf600g.services;

import ca.uqam.info.inf600g.data.AccountsCollection;
import ca.uqam.info.inf600g.model.HelperAccount;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login/helper/")
@Tag(name = "Login management")
@Produces(MediaType.APPLICATION_JSON)
public class LoginService {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiResponse(description = "Check if an user can login (DEMO ONLY)")
    public boolean login(@FormParam("identifier") String ident,
                         @FormParam("password") String pass) {
        HelperAccount ha = AccountsCollection.getAccess().getHelperByIdentifier(ident);
        if (null == ha)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        if (!ha.getPassword().equals(pass))
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);

        return true;
    }
}