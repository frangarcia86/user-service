package com.users.infrastructure.client;

import com.users.domain.model.User;
import com.users.domain.port.NotificationPort;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationClient implements NotificationPort {

    // TODO: inject a Quarkus REST client (e.g. @RestClient NotificationRestClient)
    //       pointing to the Notification Service base URL configured via
    //       quarkus.rest-client.notification-service.url in application.properties

    @Override
    public void notifyAccessAlert(User user) {
        Log.infof("Sending access alert notification for user '%s' <%s> (id: %s)",
                user.getName(), user.getEmail(), user.getId());

        // TODO: POST /notifications/access-alert
        //       body: { "userId": "<uuid>", "email": "<email>", "name": "<name>" }
        //       Expected 202 Accepted — notifies the user that someone accessed their account.
    }
}
