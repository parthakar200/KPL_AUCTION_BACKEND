//package com.kpl.auction.controller;
//
//import com.kpl.auction.dto.*;
//import com.kpl.auction.service.AuctionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class AuctionController {
//
//    private final AuctionService auctionService;
//
//    // ── Full State ─────────────────────────────────────────────────
//    // GET /api/state  →  returns the complete AuctionStateDTO
//    @GetMapping("/state")
//    public ResponseEntity<AuctionStateDTO> getFullState() {
//        return ResponseEntity.ok(auctionService.getFullState());
//    }
//
//    // POST /api/reset  →  wipe and re-seed to defaults
//    @PostMapping("/reset")
//    public ResponseEntity<Void> reset() {
//        auctionService.resetAuction();
//        return ResponseEntity.ok().build();
//    }
//
//    // ── Teams ──────────────────────────────────────────────────────
//    // GET  /api/teams
//    @GetMapping("/teams")
//    public ResponseEntity<List<TeamDTO>> getTeams() {
//        return ResponseEntity.ok(auctionService.getAllTeams());
//    }
//
//    // POST /api/teams
//    @PostMapping("/teams")
//    public ResponseEntity<TeamDTO> addTeam(@RequestBody AddTeamRequest req) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(auctionService.addTeam(req));
//    }
//
//    // DELETE /api/teams/{id}
//    @DeleteMapping("/teams/{id}")
//    public ResponseEntity<Void> deleteTeam(@PathVariable String id) {
//        auctionService.deleteTeam(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // ── Players ────────────────────────────────────────────────────
//    // GET  /api/players
//    @GetMapping("/players")
//    public ResponseEntity<List<PlayerDTO>> getPlayers() {
//        return ResponseEntity.ok(auctionService.getAllPlayers());
//    }
//
//    // POST /api/players
//    @PostMapping("/players")
//    public ResponseEntity<PlayerDTO> addPlayer(@RequestBody AddPlayerRequest req) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(auctionService.addPlayer(req));
//    }
//
//    // PATCH /api/players/{id}
//    @PatchMapping("/players/{id}")
//    public ResponseEntity<PlayerDTO> editPlayer(
//            @PathVariable String id,
//            @RequestBody EditPlayerRequest req) {
//        return ResponseEntity.ok(auctionService.editPlayer(id, req));
//    }
//
//    // DELETE /api/players/{id}
//    @DeleteMapping("/players/{id}")
//    public ResponseEntity<Void> removePlayer(@PathVariable String id) {
//        auctionService.removePlayer(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // ── Auction Actions ────────────────────────────────────────────
//    // POST /api/auction/start/{playerId}
//    @PostMapping("/auction/start/{playerId}")
//    public ResponseEntity<AuctionStateDTO> startBidding(@PathVariable String playerId) {
//        return ResponseEntity.ok(auctionService.startBidding(playerId));
//    }
//
//    // POST /api/auction/bid   body: { "teamId": "t1" }
//    @PostMapping("/auction/bid")
//    public ResponseEntity<AuctionStateDTO> placeBid(@RequestBody BidRequest req) {
//        return ResponseEntity.ok(auctionService.placeBid(req));
//    }
//
//    // POST /api/auction/increment   body: { "amount": 10 }
//    @PostMapping("/auction/increment")
//    public ResponseEntity<AuctionStateDTO> incrementBid(@RequestBody IncrementBidRequest req) {
//        return ResponseEntity.ok(auctionService.incrementBid(req));
//    }
//
//    // POST /api/auction/sell   body: { "teamId": "t1", "amount": 150 }
//    @PostMapping("/auction/sell")
//    public ResponseEntity<AuctionStateDTO> confirmSold(@RequestBody SellPlayerRequest req) {
//        return ResponseEntity.ok(auctionService.confirmSold(req));
//    }
//
//    // POST /api/auction/unsold
//    @PostMapping("/auction/unsold")
//    public ResponseEntity<AuctionStateDTO> markUnsold() {
//        return ResponseEntity.ok(auctionService.markUnsold());
//    }
//
//    // POST /api/auction/reauction/{playerId}
//    @PostMapping("/auction/reauction/{playerId}")
//    public ResponseEntity<AuctionStateDTO> reAuction(@PathVariable String playerId) {
//        return ResponseEntity.ok(auctionService.reAuction(playerId));
//    }
//
//    // POST /api/auction/rtm/{teamId}
//    @PostMapping("/auction/rtm/{teamId}")
//    public ResponseEntity<AuctionStateDTO> useRTM(@PathVariable String teamId) {
//        return ResponseEntity.ok(auctionService.useRTM(teamId));
//    }
//
//    // POST /api/auction/wildcard/{teamId}
//    @PostMapping("/auction/wildcard/{teamId}")
//    public ResponseEntity<AuctionStateDTO> useWildcard(@PathVariable String teamId) {
//        return ResponseEntity.ok(auctionService.useWildcard(teamId));
//    }
//
//    // POST /api/auction/tab/{tab}
//    @PostMapping("/auction/tab/{tab}")
//    public ResponseEntity<AuctionStateDTO> switchTab(@PathVariable String tab) {
//        return ResponseEntity.ok(auctionService.switchQueueTab(tab));
//    }
//
//    // ── Registered Players ─────────────────────────────────────────
//    // GET  /api/registered
//    @GetMapping("/registered")
//    public ResponseEntity<List<RegisteredPlayerDTO>> getRegistered() {
//        return ResponseEntity.ok(auctionService.getAllRegistered());
//    }
//
//    // POST /api/registered
//    @PostMapping("/registered")
//    public ResponseEntity<RegisteredPlayerDTO> addRegistered(
//            @RequestBody AddRegisteredPlayerRequest req) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(auctionService.addRegistered(req));
//    }
//
//    // PATCH /api/registered/{id}
//    @PatchMapping("/registered/{id}")
//    public ResponseEntity<RegisteredPlayerDTO> editRegistered(
//            @PathVariable String id,
//            @RequestBody AddRegisteredPlayerRequest req) {
//        return ResponseEntity.ok(auctionService.editRegistered(id, req));
//    }
//
//    // DELETE /api/registered/{id}
//    @DeleteMapping("/registered/{id}")
//    public ResponseEntity<Void> removeRegistered(@PathVariable String id) {
//        auctionService.removeRegistered(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // POST /api/registered/{id}/send-to-auction
//    @PostMapping("/registered/{id}/send-to-auction")
//    public ResponseEntity<PlayerDTO> sendToAuction(@PathVariable String id) {
//        return ResponseEntity.ok(auctionService.sendToAuction(id));
//    }
//}
















package com.kpl.auction.controller;

import com.kpl.auction.dto.*;
import com.kpl.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    // ── Full State ─────────────────────────────────────────────────
    // GET /api/state  →  returns the complete AuctionStateDTO
    @GetMapping("/state")
    public ResponseEntity<AuctionStateDTO> getFullState() {
        return ResponseEntity.ok(auctionService.getFullState());
    }

    // POST /api/reset  →  wipe and re-seed to defaults
    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        auctionService.resetAuction();
        return ResponseEntity.ok().build();
    }

    // ── Teams ──────────────────────────────────────────────────────
    // GET  /api/teams
    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getTeams() {
        return ResponseEntity.ok(auctionService.getAllTeams());
    }

    // POST /api/teams
    @PostMapping("/teams")
    public ResponseEntity<TeamDTO> addTeam(@RequestBody AddTeamRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auctionService.addTeam(req));
    }

    // PUT /api/teams/{id}  — edit name, color, or budget
    @PutMapping("/teams/{id}")
    public ResponseEntity<TeamDTO> editTeam(@PathVariable String id, @RequestBody AddTeamRequest req) {
        return ResponseEntity.ok(auctionService.editTeam(id, req));
    }

    // DELETE /api/teams/{id}
    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable String id) {
        auctionService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    // ── Players ────────────────────────────────────────────────────
    // GET  /api/players
    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getPlayers() {
        return ResponseEntity.ok(auctionService.getAllPlayers());
    }

    // POST /api/players
    @PostMapping("/players")
    public ResponseEntity<PlayerDTO> addPlayer(@RequestBody AddPlayerRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auctionService.addPlayer(req));
    }

    // PATCH /api/players/{id}
    @PatchMapping("/players/{id}")
    public ResponseEntity<PlayerDTO> editPlayer(
            @PathVariable String id,
            @RequestBody EditPlayerRequest req) {
        return ResponseEntity.ok(auctionService.editPlayer(id, req));
    }

    // DELETE /api/players/{id}
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> removePlayer(@PathVariable String id) {
        auctionService.removePlayer(id);
        return ResponseEntity.noContent().build();
    }

    // ── Auction Actions ────────────────────────────────────────────
    // POST /api/auction/start/{playerId}
    @PostMapping("/auction/start/{playerId}")
    public ResponseEntity<AuctionStateDTO> startBidding(@PathVariable String playerId) {
        return ResponseEntity.ok(auctionService.startBidding(playerId));
    }

    // POST /api/auction/bid   body: { "teamId": "t1" }
    @PostMapping("/auction/bid")
    public ResponseEntity<AuctionStateDTO> placeBid(@RequestBody BidRequest req) {
        return ResponseEntity.ok(auctionService.placeBid(req));
    }

    // POST /api/auction/increment   body: { "amount": 10 }
    @PostMapping("/auction/increment")
    public ResponseEntity<AuctionStateDTO> incrementBid(@RequestBody IncrementBidRequest req) {
        return ResponseEntity.ok(auctionService.incrementBid(req));
    }

    // POST /api/auction/sell   body: { "teamId": "t1", "amount": 150 }
    @PostMapping("/auction/sell")
    public ResponseEntity<AuctionStateDTO> confirmSold(@RequestBody SellPlayerRequest req) {
        return ResponseEntity.ok(auctionService.confirmSold(req));
    }

    // POST /api/auction/unsold
    @PostMapping("/auction/unsold")
    public ResponseEntity<AuctionStateDTO> markUnsold() {
        return ResponseEntity.ok(auctionService.markUnsold());
    }

    // POST /api/auction/reauction/{playerId}
    @PostMapping("/auction/reauction/{playerId}")
    public ResponseEntity<AuctionStateDTO> reAuction(@PathVariable String playerId) {
        return ResponseEntity.ok(auctionService.reAuction(playerId));
    }

    // POST /api/auction/rtm/{teamId}
    @PostMapping("/auction/rtm/{teamId}")
    public ResponseEntity<AuctionStateDTO> useRTM(@PathVariable String teamId) {
        return ResponseEntity.ok(auctionService.useRTM(teamId));
    }

    // POST /api/auction/wildcard/{teamId}
    @PostMapping("/auction/wildcard/{teamId}")
    public ResponseEntity<AuctionStateDTO> useWildcard(@PathVariable String teamId) {
        return ResponseEntity.ok(auctionService.useWildcard(teamId));
    }

    // POST /api/auction/tab/{tab}
    @PostMapping("/auction/tab/{tab}")
    public ResponseEntity<AuctionStateDTO> switchTab(@PathVariable String tab) {
        return ResponseEntity.ok(auctionService.switchQueueTab(tab));
    }

    // ── Registered Players ─────────────────────────────────────────
    // GET  /api/registered
    @GetMapping("/registered")
    public ResponseEntity<List<RegisteredPlayerDTO>> getRegistered() {
        return ResponseEntity.ok(auctionService.getAllRegistered());
    }

    // POST /api/registered
    @PostMapping("/registered")
    public ResponseEntity<RegisteredPlayerDTO> addRegistered(
            @RequestBody AddRegisteredPlayerRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auctionService.addRegistered(req));
    }

    // PATCH /api/registered/{id}
    @PatchMapping("/registered/{id}")
    public ResponseEntity<RegisteredPlayerDTO> editRegistered(
            @PathVariable String id,
            @RequestBody AddRegisteredPlayerRequest req) {
        return ResponseEntity.ok(auctionService.editRegistered(id, req));
    }

    // DELETE /api/registered/{id}
    @DeleteMapping("/registered/{id}")
    public ResponseEntity<Void> removeRegistered(@PathVariable String id) {
        auctionService.removeRegistered(id);
        return ResponseEntity.noContent().build();
    }

    // POST /api/registered/{id}/send-to-auction
    @PostMapping("/registered/{id}/send-to-auction")
    public ResponseEntity<PlayerDTO> sendToAuction(@PathVariable String id) {
        return ResponseEntity.ok(auctionService.sendToAuction(id));
    }
}