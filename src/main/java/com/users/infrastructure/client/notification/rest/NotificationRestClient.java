package com.users.infrastructure.client.notification.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.users.infrastructure.client.notification.dto.AccessAlertRequest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST client for the Notification Service.
 * Simulated — no real endpoint exists for this demo.
 */
@RegisterRestClient(configKey = "notification-service")
@Path("/notifications")
public interface NotificationRestClient {

    @POST
    @Path("/access-alert")
    @Consumes(MediaType.APPLICATION_JSON)
    Response sendAccessAlert(AccessAlertRequest request);
}
