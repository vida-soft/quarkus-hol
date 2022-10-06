package com.vidasoft.magman.user;

import com.vidasoft.magman.model.User;
import com.vidasoft.magman.security.JwtService;
import io.vertx.core.http.HttpServerRequest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Path("user")
public class UserResource {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    UserService userService;

    @Inject
    HttpServerRequest request;

    @Inject
    JwtService jwtService;

    @PostConstruct
    void init() {
        String requestPath = request.uri();
        var originIp = request.remoteAddress().toString();
        logger.log(Level.INFO, "URL call attempt {0} from {1}", new String[]{requestPath, originIp});
    }

    @PreDestroy
    void destroy() {
        String requestPath = request.uri();
        var originIp = request.remoteAddress().toString();
        logger.log(Level.INFO, "Scope completed for {0} from {1}", new String[]{requestPath, originIp});
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(@Valid @NotNull NewUserDTO newUserDTO) {
        Optional<User> existingUser = User.find("userName = ?1 or email = ?2", newUserDTO.getUserName(),
                newUserDTO.getEmail()).firstResultOptional();
        if (existingUser.isPresent()) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        userService.registerUser(newUserDTO.getFirstName(), newUserDTO.getLastName(), newUserDTO.getEmail(),
                newUserDTO.getUserName(), newUserDTO.getPassword(), newUserDTO.getUserType());
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(@Valid @NotNull LoginDTO login) {
        Optional<User> loggedUser = userService.loginUser(login.getUserName(), login.getPassword());
        return loggedUser
                .map(u -> Response.ok(new UserDTO(u))
                        .header("Authorization", jwtService.generateJWT(u))
                        .build())
                .orElseGet(() -> Response.status(Response.Status.UNAUTHORIZED).build());
    }

}
