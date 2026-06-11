package com.users.domain.port.notification;

import com.users.domain.model.User;

public interface NotificationPort {

    void notifyAccessAlert(User user);
}
