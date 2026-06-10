package com.users.domain.port;

import com.users.domain.model.User;

public interface NotificationPort {

    void notifyAccessAlert(User user);
}
