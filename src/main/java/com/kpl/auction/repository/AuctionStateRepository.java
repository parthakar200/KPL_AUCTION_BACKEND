package com.kpl.auction.repository;

import com.kpl.auction.model.AuctionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionStateRepository extends JpaRepository<AuctionState, Long> {}