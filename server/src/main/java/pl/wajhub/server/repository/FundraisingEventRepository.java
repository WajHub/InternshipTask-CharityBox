package pl.wajhub.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wajhub.server.model.FundraisingEvent;

import java.util.Optional;
import java.util.UUID;

public interface FundraisingEventRepository extends JpaRepository<FundraisingEvent, UUID> {
    Optional<FundraisingEvent> findByName(String name);
}
