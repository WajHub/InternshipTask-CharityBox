package pl.wajhub.server.exception;

public class CollectionBoxIsNotEmptyException extends RuntimeException {
    public CollectionBoxIsNotEmptyException() {

      super("You cannot assign not empty collection box!");
    }
}
