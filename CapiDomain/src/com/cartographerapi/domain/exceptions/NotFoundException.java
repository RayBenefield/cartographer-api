package com.cartographerapi.domain.exceptions;

import java.lang.RuntimeException;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super("[404 Not Found] " + message);
    }
}
