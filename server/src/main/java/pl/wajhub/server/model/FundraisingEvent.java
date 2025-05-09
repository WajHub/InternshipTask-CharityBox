package pl.wajhub.server.model;

import jakarta.persistence.*;
import lombok.*;

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
    private MyCurrency currency;

    @Builder.Default
    @Column(nullable = false)
    private Double amount = 0.0;

    @OneToMany(mappedBy = "event", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<CollectionBox> collectionBoxSet;
}
