package model;

import java.time.LocalDateTime;

/**
 * Bill class is used to model the bills of the store.
 * @param id
 * @param message
 * @param createdAt
 */
public record Bill(int id, String message, LocalDateTime createdAt) {
}