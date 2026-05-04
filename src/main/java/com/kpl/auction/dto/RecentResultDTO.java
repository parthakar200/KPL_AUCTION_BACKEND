package com.kpl.auction.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RecentResultDTO {
    private String playerId;
    private String teamId;
    private int amount;
    private String status;
    private long ts;
}
