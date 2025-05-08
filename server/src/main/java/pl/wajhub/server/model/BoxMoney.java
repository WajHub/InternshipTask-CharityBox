package pl.wajhub.server.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoxMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(nullable = false)
    private MyCurrency currency;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "collection_box_uuid")
    private CollectionBox collectionBox;
}
