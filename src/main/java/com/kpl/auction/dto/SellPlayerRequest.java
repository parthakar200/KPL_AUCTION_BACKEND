package com.kpl.auction.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class SellPlayerRequest {
    private String teamId;
    private int amount;
}
