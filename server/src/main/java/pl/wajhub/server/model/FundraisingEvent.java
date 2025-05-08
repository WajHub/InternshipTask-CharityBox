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

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "event")
    private Set<Account> accountSet;

    @OneToMany(mappedBy = "event")
    private Set<CollectionBox> collectionBoxSet;
}
