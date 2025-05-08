package pl.wajhub.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wajhub.server.model.CollectionBox;

import java.util.UUID;

public interface CollectionBoxRepository extends JpaRepository<CollectionBox, UUID> {
}
