package com.kpl.auction.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auction_state")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuctionState {

    /** Always a single row with id = 1 */
    @Id
    private Long id;

    @Column(length = 50)
    private String currentBidPlayerId;

    private int currentBidAmount;

    @Column(length = 50)
    private String currentBidLeadingTeamId;

    /** Comma-separated player IDs in queue order */
    @Column(columnDefinition = "TEXT")
    private String queue;

    @Column(length = 10)
    private String activeQueueTab;

    /** JSON string for recent results (last 6) */
    @Column(columnDefinition = "TEXT")
    private String recentResultsJson;
}