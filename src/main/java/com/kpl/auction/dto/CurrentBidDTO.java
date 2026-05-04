package com.kpl.auction.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CurrentBidDTO {
    private String playerId;
    private int amount;
    private String leadingTeamId;
}
