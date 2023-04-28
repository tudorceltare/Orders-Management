package model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Order {
    private int id;
    private int clientId;
    private List<Integer> productIds;
    private double totalPrice;
}
