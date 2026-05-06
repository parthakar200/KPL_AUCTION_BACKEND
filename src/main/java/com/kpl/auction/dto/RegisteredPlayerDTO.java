package com.kpl.auction.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisteredPlayerDTO {
    private String id;
    private String name;
    private String role;
    private String category;
    private int basePrice;
    private String phone;
    private String registeredAt;
    private boolean addedToAuction;
    private String photo;
}
