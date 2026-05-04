package com.kpl.auction.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerDTO {
    private String id;
    private String name;
    private String category;
    private String role;
    private int basePrice;
    private Integer soldPrice;
    private String teamId;
    private String status;
}
