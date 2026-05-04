package com.kpl.auction.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class IncrementBidRequest {
    private int amount;
}
