package ru.voskhod.smev.message_exchange_service_client;

public final class ClientSideProcessingException extends Exception {

    private static final long serialVersionUID = -4646861090264408031L;

    public ClientSideProcessingException() {
    }

    public ClientSideProcessingException(String message) {
        super(message);
    }

    public ClientSideProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientSideProcessingException(Throwable cause) {
        super(cause);
    }
}
