package com.kpl.auction.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class AddRegisteredPlayerRequest {
    private String name;
    private String role;
    private String category;
    private int basePrice;
    private String phone;
}
