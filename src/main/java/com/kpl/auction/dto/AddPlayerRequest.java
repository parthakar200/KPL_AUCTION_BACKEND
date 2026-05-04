package com.kpl.auction.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class AddPlayerRequest {
    private String name;
    private String category;
    private String role;
    private int basePrice;
}
