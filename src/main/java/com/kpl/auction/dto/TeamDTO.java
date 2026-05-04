package com.kpl.auction.dto;
import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TeamDTO {
    private String id;
    private String name;
    private String color;
    private int points;
    private int pointsSpent;
    private boolean rtmUsed;
    private List<String> players;
}
