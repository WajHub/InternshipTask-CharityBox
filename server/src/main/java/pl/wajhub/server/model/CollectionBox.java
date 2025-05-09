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
    @JoinColumn(name = "event_uuid")
    private FundraisingEvent event;

    @OneToMany(mappedBy = "collectionBox", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<BoxMoney> boxMoneySet;

    public boolean isEmpty(){
        if (boxMoneySet!=null){
            var sum = boxMoneySet.stream()
                        .mapToDouble(BoxMoney::getAmount)
                        .sum();
            return (sum == 0);
        }
        return true;
    }

}
