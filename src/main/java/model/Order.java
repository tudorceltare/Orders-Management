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
    private Client client;
    private List<Product> products;
    private double totalPrice;
}
