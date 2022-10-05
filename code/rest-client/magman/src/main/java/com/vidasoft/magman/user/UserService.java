package com.vidasoft.magman.user;

import com.vidasoft.magman.model.Author;
import com.vidasoft.magman.model.Manager;
import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.model.User;
import com.vidasoft.magman.security.PasswordService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    PasswordService passwordService;

    public User registerUser(String firstName, String lastName, String email, String username, String password, UserType userType) {
        User createdUser = null;
        switch (userType) {
            case AUTHOR:
                createdUser = new Author(username, password, firstName, lastName, email, true, 0);
                break;
            case MANAGER:
                createdUser = new Manager(username, password, firstName, lastName, email);
                break;
            case SUBSCRIBER:
                createdUser = new Subscriber(username, password, firstName, lastName, email,
                        null, LocalDate.now().plusYears(1), null);
                break;
        }

        var salt = passwordService.generateSalt();
        createdUser.salt = salt;
        createdUser.password = passwordService.encryptPassword(password, salt);

        createdUser.persist();
        return createdUser;
    }

    public Optional<User> loginUser(String username, String password) {
        User user = User.find("userName", username).firstResult();
        if (user != null) {
            var hashedPassword = passwordService.encryptPassword(password, user.salt);
            if (hashedPassword.equals(user.password)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

}
