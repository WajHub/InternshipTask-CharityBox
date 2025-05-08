package pl.wajhub.server.exception;

import java.util.UUID;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(UUID uuid) {
        super("Fundraising Event not found with uuid: "+uuid+"!");
    }
}
