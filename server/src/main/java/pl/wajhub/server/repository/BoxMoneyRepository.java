package pl.wajhub.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wajhub.server.model.BoxMoney;

import java.util.UUID;

public interface BoxMoneyRepository extends JpaRepository<BoxMoney, UUID> {
}
