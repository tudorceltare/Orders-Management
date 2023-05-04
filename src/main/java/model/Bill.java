package model;

import java.time.LocalDateTime;

public record Bill(int id, String message, LocalDateTime createdAt) {
}