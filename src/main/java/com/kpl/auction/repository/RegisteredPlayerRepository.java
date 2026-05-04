package com.kpl.auction.repository;

import com.kpl.auction.model.RegisteredPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredPlayerRepository extends JpaRepository<RegisteredPlayer, String> {}