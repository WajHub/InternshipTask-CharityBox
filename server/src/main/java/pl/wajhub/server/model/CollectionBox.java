package pl.wajhub.server.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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

    @ElementCollection
    @CollectionTable(name = "box_balance_mapping",
            joinColumns = {@JoinColumn(name = "box_id", referencedColumnName = "uuid")})
    @MapKeyColumn(name = "currency")
    @Column(name = "balance")
    private Map<String, Double> balance = new HashMap<>();

    public boolean isEmpty(){
        if (balance!=null){
            var sum = balance.values().stream()
                    .mapToDouble(Double::valueOf)
                    .sum();
            return (sum == 0);
        }
        return true;
    }

}
