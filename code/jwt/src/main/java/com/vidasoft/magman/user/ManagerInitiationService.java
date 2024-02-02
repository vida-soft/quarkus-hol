package com.vidasoft.magman.user;

import com.vidasoft.magman.model.Manager;
import com.vidasoft.magman.security.PasswordService;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ManagerInitiationService {

    @Inject
    PasswordService passwordService;

    @Inject
    @ConfigProperty(name = "magman.manager.user", defaultValue = "manager")
    String managerUsername;

    @Inject
    @ConfigProperty(name = "magman.manager.password", defaultValue = "manager")
    String managerPassword;

    @Transactional
    public void createManager(@Observes StartupEvent startupEvent) {
        if (Manager.count() == 0) {
            var managerSalt = passwordService.generateSalt();
            var manager = new Manager(managerUsername, passwordService.encryptPassword(managerPassword, managerSalt),
                    "Manager", "User", "manager@vida-soft.com");
            manager.salt = managerSalt;
            manager.persist();
        }
    }
}
