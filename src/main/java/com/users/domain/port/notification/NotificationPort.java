package com.users.domain.port.notification;

import com.users.domain.model.User;

/**
 * Port for sending notifications triggered by domain events.
 */
public interface NotificationPort {

    /**
     * Sends an alert when a user account is accessed under suspicious conditions.
     */
    void notifyAccessAlert(User user);
}
