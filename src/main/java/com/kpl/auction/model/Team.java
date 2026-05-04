package com.kpl.auction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Team {

    @Id
    @Column(nullable = false, unique = true, length = 50)
    private String id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 10)
    private String color;

    @Column(nullable = false)
    private int points;

    @Column(nullable = false)
    private int pointsSpent;

    @Column(nullable = false)
    private boolean rtmUsed;

    // Player IDs are stored as a comma-separated string for simplicity.
    // Player objects themselves are the source of truth via their teamId FK.
    @Column(columnDefinition = "TEXT")
    private String playerIds; // e.g. "p1,p2,p3"

    /** Helper: get playerIds as a mutable list */
    @Transient
    public List<String> getPlayerIdList() {
        if (playerIds == null || playerIds.isBlank()) return new ArrayList<>();
        return new ArrayList<>(List.of(playerIds.split(",")));
    }

    /** Helper: set playerIds from a list */
    public void setPlayerIdList(List<String> ids) {
        this.playerIds = ids == null || ids.isEmpty() ? "" : String.join(",", ids);
    }
}