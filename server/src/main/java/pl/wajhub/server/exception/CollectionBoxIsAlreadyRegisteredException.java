package pl.wajhub.server.exception;

public class CollectionBoxIsAlreadyRegisteredException extends RuntimeException {
    public CollectionBoxIsAlreadyRegisteredException(String message) {
        super(message);
    }
}
