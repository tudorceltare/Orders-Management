package model;

import lombok.*;

/**
 * Client class is used to model the clients of the store.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Client {
    private int id;
    private String name;
    private String email;
}
