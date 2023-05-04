package model;

import lombok.*;

import java.util.List;

/**
 * Order class is used to model the orders of the store. An order has a client, a list of products and a total price.
 * The relationship between the order and the products is many to many and uni-directional.
 */
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
