package com.kpl.auction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "registered_players")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisteredPlayer {

    @Id
    @Column(nullable = false, unique = true, length = 60)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 5)
    private String role;

    @Column(nullable = false, length = 2)
    private String category;

    @Column(nullable = false)
    private int basePrice;

    @Column(length = 20)
    private String phone;

    @Column(length = 30)
    private String registeredAt;

    @Column(nullable = false)
    private boolean addedToAuction;

    /** Base64-encoded player photo (data URI), nullable */
    @Column(columnDefinition = "TEXT")
    private String photo;
}