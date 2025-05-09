package pl.wajhub.server.exception;

import java.util.UUID;

public class CollectionBoxNotFoundException extends RuntimeException {
  public CollectionBoxNotFoundException(UUID uuid) {
    super("Collection Box not found with uuid: "+uuid+"!");
  }
}
