package pl.wajhub.server.exception;

public class EventDuplicateNameException extends RuntimeException {
    public EventDuplicateNameException(String name) {
        super("Name: "+name+" is already in use!");
    }
}
