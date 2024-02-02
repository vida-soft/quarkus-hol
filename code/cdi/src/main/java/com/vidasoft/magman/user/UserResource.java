package com.vidasoft.magman.user;

import com.vidasoft.magman.model.User;
import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

    @PostConstruct
    void init() {
        String requestPath = request.uri();
        var originIp = request.remoteAddress().toString();
        logger.log(Level.INFO, "URL call attempt {0} from {1}", new String[] {requestPath, originIp});
    }

    @PreDestroy
    void destroy() {
        String requestPath = request.uri();
        var originIp = request.remoteAddress().toString();
        logger.log(Level.INFO, "Scope completed for {0} from {1}", new String[] {requestPath, originIp});
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(NewUserDTO newUserDTO) {
        if (checkIfNull(newUserDTO.getUserName(), newUserDTO.getPassword(),
                newUserDTO.getFirstName(), newUserDTO.getLastName(), newUserDTO.getUserType(), newUserDTO.getEmail())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

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
    public Response loginUser(LoginDTO login) {
        if (checkIfNull(login.getUserName(), login.getPassword())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            Optional<User> loggedUser = userService.loginUser(login.getUserName(), login.getPassword());
            return loggedUser
                    .map(u -> Response.ok(new UserDTO(u)).build())
                    .orElseGet(() -> Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private boolean checkIfNull(Object... fields) {
        for (var field : fields) {
            if (field == null) {
                return true;
            }
        }

        return false;
    }

}
