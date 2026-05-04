package com.kpl.auction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "players")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Player {

    @Id
    @Column(nullable = false, unique = true, length = 50)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    /** A, B, or C */
    @Column(nullable = false, length = 2)
    private String category;

    /** BAT, BWL, AR, WK */
    @Column(nullable = false, length = 5)
    private String role;

    @Column(nullable = false)
    private int basePrice;

    private Integer soldPrice;

    /** FK to Team.id — nullable if not yet sold */
    @Column(length = 50)
    private String teamId;

    /** pending | sold | unsold */
    @Column(nullable = false, length = 10)
    private String status;
}