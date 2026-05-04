package com.kpl.auction.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class AddTeamRequest {
    private String name;
    private String color;
    private int points;
}
