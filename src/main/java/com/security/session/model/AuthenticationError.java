package com.security.session.model;

import java.io.Serializable;

public record AuthenticationError(int status, String error, String message, String path
) implements Serializable {
}
