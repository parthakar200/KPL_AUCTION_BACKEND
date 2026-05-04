package com.kpl.auction.dto;
import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuctionStateDTO {
    private List<TeamDTO>        teams;
    private List<PlayerDTO>      players;
    private List<String>         queue;
    private CurrentBidDTO        currentBid;
    private String               activeQueueTab;
    private List<RecentResultDTO> recentResults;
}
