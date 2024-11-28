package com.exacaster.lighter.application.sessions;

import com.exacaster.lighter.application.ApplicationState;

public final class SessionUtils {

    private SessionUtils() {
    }

    public static ApplicationState adjustState(boolean noWaitingStatements, ApplicationState state) {
        return switch (state) {
            case BUSY -> noWaitingStatements ? ApplicationState.IDLE : state;
            case IDLE -> noWaitingStatements ? state : ApplicationState.BUSY;
            default -> state;
        };
    }
}
