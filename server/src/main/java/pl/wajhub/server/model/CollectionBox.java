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
public class CollectionBox {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "event_uuid", nullable = false)
    private FundraisingEvent event;

    @OneToMany(mappedBy = "collectionBox")
    private Set<BoxMoney> boxMoneySet;

}
