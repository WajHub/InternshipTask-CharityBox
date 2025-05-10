package pl.wajhub.server.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import pl.wajhub.server.validation.CurrencyCodeConstraint;

import java.util.Set;
import java.util.UUID;

@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FundraisingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @CurrencyCodeConstraint
    @Schema(name = "Currency", example = "USD|EUR|PLN", description = "Currency code according to ISO 4217")
    private String currencyCode;

    @Builder.Default
    @Column(nullable = false)
    private Double amount = 0.0;

    @OneToMany(mappedBy = "event", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<CollectionBox> collectionBoxSet;
}
