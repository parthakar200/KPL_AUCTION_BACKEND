package com.kpl.auction.repository;

import com.kpl.auction.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    List<Player> findByTeamId(String teamId);
    List<Player> findByStatus(String status);
}