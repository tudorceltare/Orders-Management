package model;

import lombok.*;

/**
 * Product class is used to model the products of the store.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Product {
    private int id;
    private String name;
    private float price;
    private int quantity;
}
