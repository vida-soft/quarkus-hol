package com.vidasoft.magman.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("user")
@Tag(name = "User resource", description = "Contains endpoints to create and log user into the system")
public interface UserAPI {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "registerUser",
            summary = "Register new user",
            description = "Allows to create a new user to the system"
    )
    @RequestBody(
            name = "newUserDTO",
            description = "Data transfer object providing the data for a new user",
            content = @Content(
                    schema = @Schema(
                            implementation = UserDTO.class,
                            example = """
                                    {
                                      "userName": "cj_123",
                                      "firstName": "Cave",
                                      "lastName": "Johnson",
                                      "email": "cave@contoso.com",
                                      "password": "Very$tr0ngPSW",
                                      "userType": "SUBSCRIBER"
                                    }
                                    """
                    )
            )
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "The user has successfully been created."
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "The user already exists or the request is malformed. Please read the error message."
            )
    })
    Response registerUser(@Valid @NotNull NewUserDTO newUserDTO);

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "loginUser",
            summary = "Log in",
            description = "Logs the user into the system and returns authentication token"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            schema = @Schema(implementation = UserDTO.class)
                    ),
                    headers = @Header(
                            name = "Authorization",
                            description = "JWT token for making requests on MagMan",
                            schema = @Schema(
                                    implementation = String.class,
                                    example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9..."
                            )
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid login body. Please check the username and password and try again."
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "A user with these credentials could not be found or does not exist."
            )
    })
    Response loginUser(@Valid @NotNull LoginDTO login);
}
