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
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "event_uuid")
    private FundraisingEvent event;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "box_balance_mapping",
            joinColumns = {@JoinColumn(name = "box_id", referencedColumnName = "uuid")})
    @MapKeyColumn(name = "currency_code")
    @Column(name = "balance")
    @Builder.Default
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

    public void transfer(String currencyCode, Double amount){
        if(balance.containsKey(currencyCode))
            balance.put(currencyCode, balance.get(currencyCode)+amount);
        else
            balance.put(currencyCode, amount);
    }

}
