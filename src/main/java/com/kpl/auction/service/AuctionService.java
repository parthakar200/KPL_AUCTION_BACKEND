package com.kpl.auction.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpl.auction.dto.*;
import com.kpl.auction.model.*;
import com.kpl.auction.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final TeamRepository             teamRepo;
    private final PlayerRepository           playerRepo;
    private final RegisteredPlayerRepository regRepo;
    private final AuctionStateRepository     stateRepo;
    private final ObjectMapper               objectMapper;

    private static final Long STATE_ID = 1L;

    // ── Seed / init ───────────────────────────────────────────────────────

    @Transactional
    public void seedDefaultDataIfEmpty() {
        if (teamRepo.count() == 0) {
            List<Team> teams = List.of(
                team("t1", "Kings XI",      "#f97316", 1000),
                team("t2", "Royal Tigers",  "#a855f7", 1000),
                team("t3", "Blue Warriors", "#06b6d4", 1000),
                team("t4", "Red Eagles",    "#ec4899", 1000)
            );
            teamRepo.saveAll(teams);
        }

        if (playerRepo.count() == 0) {
            List<Player> players = List.of(
                player("p1",  "Arjun Sharma",  "A", "BAT", 150),
                player("p2",  "Rohit Patel",   "A", "BAT", 130),
                player("p3",  "Vikas Singh",   "A", "BWL", 120),
                player("p4",  "Suresh Yadav",  "A", "AR",  110),
                player("p5",  "Manish Gupta",  "B", "BAT", 80),
                player("p6",  "Deepak Verma",  "B", "BWL", 75),
                player("p7",  "Ankur Tiwari",  "B", "AR",  70),
                player("p8",  "Pradeep Kumar", "B", "WK",  65),
                player("p9",  "Raju Chauhan",  "B", "BAT", 60),
                player("p10", "Sanjay Mishra", "C", "BWL", 40),
                player("p11", "Ajay Rawat",    "C", "BAT", 35),
                player("p12", "Ramesh Joshi",  "C", "BWL", 30),
                player("p13", "Kishore Nair",  "C", "WK",  30),
                player("p14", "Dinesh Pandey", "C", "AR",  25),
                player("p15", "Sunil Bhatt",   "C", "BAT", 25),
                player("p16", "Hari Shukla",   "C", "BWL", 20),
                player("p17", "Gopal Singh",   "C", "BAT", 20),
                player("p18", "Lallan Prasad", "C", "AR",  15),
                player("p19", "Munna Ali",     "C", "BWL", 15),
                player("p20", "Chotu Sharma",  "C", "BAT", 10)
            );
            playerRepo.saveAll(players);
        }

        if (stateRepo.count() == 0) {
            String queue = playerRepo.findAll().stream()
                .map(Player::getId).collect(Collectors.joining(","));
            stateRepo.save(AuctionState.builder()
                .id(STATE_ID).queue(queue)
                .currentBidAmount(0).activeQueueTab("BAT")
                .recentResultsJson("[]").build());
        }
    }

    // ── Full State ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public AuctionStateDTO getFullState() {
        List<Team>   teams   = teamRepo.findAll();
        List<Player> players = playerRepo.findAll();
        AuctionState as      = getState();

        return AuctionStateDTO.builder()
            .teams(teams.stream().map(this::toTeamDTO).collect(Collectors.toList()))
            .players(players.stream().map(this::toPlayerDTO).collect(Collectors.toList()))
            .queue(parseList(as.getQueue()))
            .currentBid(CurrentBidDTO.builder()
                .playerId(as.getCurrentBidPlayerId())
                .amount(as.getCurrentBidAmount())
                .leadingTeamId(as.getCurrentBidLeadingTeamId())
                .build())
            .activeQueueTab(as.getActiveQueueTab())
            .recentResults(parseRecentResults(as.getRecentResultsJson()))
            .build();
    }

    // ── Reset ──────────────────────────────────────────────────────────────

    @Transactional
    public void resetAuction() {
        playerRepo.deleteAll();
        teamRepo.deleteAll();
        stateRepo.deleteAll();
        seedDefaultDataIfEmpty();
    }

    // ── Teams ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<TeamDTO> getAllTeams() {
        return teamRepo.findAll().stream().map(this::toTeamDTO).collect(Collectors.toList());
    }

    @Transactional
    public TeamDTO addTeam(AddTeamRequest req) {
        if (req.getName() == null || req.getName().isBlank())
            throw new IllegalArgumentException("Team name cannot be empty");
        if (teamRepo.findAll().stream().anyMatch(t -> t.getName().equalsIgnoreCase(req.getName())))
            throw new IllegalArgumentException("Team name already exists");
        Team t = Team.builder()
            .id("team_" + System.currentTimeMillis())
            .name(req.getName()).color(req.getColor())
            .points(req.getPoints() > 0 ? req.getPoints() : 1000)
            .pointsSpent(0).rtmUsed(false).playerIds("").build();
        return toTeamDTO(teamRepo.save(t));
    }

    @Transactional
    public void deleteTeam(String teamId) {
        Team t = teamRepo.findById(teamId)
            .orElseThrow(() -> new NoSuchElementException("Team not found: " + teamId));
        if (!t.getPlayerIdList().isEmpty())
            throw new IllegalStateException("Cannot delete team with players. Release players first.");
        teamRepo.deleteById(teamId);
    }

    // ── Players ────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<PlayerDTO> getAllPlayers() {
        return playerRepo.findAll().stream().map(this::toPlayerDTO).collect(Collectors.toList());
    }

    @Transactional
    public PlayerDTO addPlayer(AddPlayerRequest req) {
        if (req.getName() == null || req.getName().isBlank())
            throw new IllegalArgumentException("Player name cannot be empty");
        if (req.getBasePrice() < 10 || req.getBasePrice() > 500)
            throw new IllegalArgumentException("Price must be 10–500");
        Player p = Player.builder()
            .id("p" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 3))
            .name(req.getName()).category(req.getCategory())
            .role(req.getRole() != null ? req.getRole() : "BAT")
            .basePrice(req.getBasePrice()).status("pending").build();
        p = playerRepo.save(p);
        // Add to queue
        AuctionState as = getState();
        List<String> q = new ArrayList<>(parseList(as.getQueue()));
        q.add(p.getId());
        as.setQueue(String.join(",", q));
        stateRepo.save(as);
        return toPlayerDTO(p);
    }

    @Transactional
    public PlayerDTO editPlayer(String playerId, EditPlayerRequest req) {
        Player p = playerRepo.findById(playerId)
            .orElseThrow(() -> new NoSuchElementException("Player not found"));
        if (req.getName() != null && !req.getName().isBlank()) p.setName(req.getName().trim());
        if (req.getCategory() != null) p.setCategory(req.getCategory());
        if (req.getRole() != null) p.setRole(req.getRole());
        if (req.getBasePrice() > 0) p.setBasePrice(req.getBasePrice());
        return toPlayerDTO(playerRepo.save(p));
    }

    @Transactional
    public void removePlayer(String playerId) {
        Player p = playerRepo.findById(playerId)
            .orElseThrow(() -> new NoSuchElementException("Player not found"));
        // If sold, refund team
        if ("sold".equals(p.getStatus()) && p.getTeamId() != null) {
            teamRepo.findById(p.getTeamId()).ifPresent(t -> {
                List<String> ids = t.getPlayerIdList();
                ids.remove(playerId);
                t.setPlayerIdList(ids);
                t.setPointsSpent(Math.max(0, t.getPointsSpent() - (p.getSoldPrice() != null ? p.getSoldPrice() : 0)));
                teamRepo.save(t);
            });
        }
        // Remove from queue
        AuctionState as = getState();
        List<String> q = new ArrayList<>(parseList(as.getQueue()));
        q.remove(playerId);
        as.setQueue(String.join(",", q));
        if (playerId.equals(as.getCurrentBidPlayerId())) {
            as.setCurrentBidPlayerId(null);
            as.setCurrentBidAmount(0);
            as.setCurrentBidLeadingTeamId(null);
        }
        stateRepo.save(as);
        playerRepo.deleteById(playerId);
    }

    // ── Auction Actions ────────────────────────────────────────────────────

    @Transactional
    public AuctionStateDTO startBidding(String playerId) {
        Player p = playerRepo.findById(playerId)
            .orElseThrow(() -> new NoSuchElementException("Player not found"));
        if (!"pending".equals(p.getStatus()))
            throw new IllegalStateException("Player is not in pending status");
        AuctionState as = getState();
        as.setCurrentBidPlayerId(playerId);
        as.setCurrentBidAmount(p.getBasePrice());
        as.setCurrentBidLeadingTeamId(null);
        stateRepo.save(as);
        return getFullState();
    }

    @Transactional
    public AuctionStateDTO placeBid(BidRequest req) {
        AuctionState as = getState();
        String playerId = as.getCurrentBidPlayerId();
        if (playerId == null) throw new IllegalStateException("No player on auction block");

        Player p = playerRepo.findById(playerId)
            .orElseThrow(() -> new NoSuchElementException("Player not found"));
        Team t = teamRepo.findById(req.getTeamId())
            .orElseThrow(() -> new NoSuchElementException("Team not found"));

        int remaining = t.getPoints() - t.getPointsSpent();
        if (remaining < as.getCurrentBidAmount())
            throw new IllegalStateException("Not enough points for " + t.getName());

        long catACount = t.getPlayerIdList().stream()
            .map(pid -> playerRepo.findById(pid).orElse(null))
            .filter(pl -> pl != null && "A".equals(pl.getCategory())).count();
        if ("A".equals(p.getCategory()) && catACount >= 3)
            throw new IllegalStateException("Category A limit (3) reached for " + t.getName());

        as.setCurrentBidLeadingTeamId(req.getTeamId());
        stateRepo.save(as);
        return getFullState();
    }

    @Transactional
    public AuctionStateDTO incrementBid(IncrementBidRequest req) {
        AuctionState as = getState();
        as.setCurrentBidAmount(as.getCurrentBidAmount() + req.getAmount());
        stateRepo.save(as);
        return getFullState();
    }

    @Transactional
    public AuctionStateDTO confirmSold(SellPlayerRequest req) {
        AuctionState as = getState();
        String playerId = as.getCurrentBidPlayerId();
        if (playerId == null) throw new IllegalStateException("No player on auction block");

        Player p = playerRepo.findById(playerId)
            .orElseThrow(() -> new NoSuchElementException("Player not found"));
        Team t = teamRepo.findById(req.getTeamId())
            .orElseThrow(() -> new NoSuchElementException("Team not found"));

        int amount = req.getAmount() > 0 ? req.getAmount() : as.getCurrentBidAmount();

        // Sell player
        p.setSoldPrice(amount);
        p.setTeamId(req.getTeamId());
        p.setStatus("sold");
        playerRepo.save(p);

        // Update team
        List<String> teamPlayers = t.getPlayerIdList();
        teamPlayers.add(playerId);
        t.setPlayerIdList(teamPlayers);
        t.setPointsSpent(t.getPointsSpent() + amount);
        teamRepo.save(t);

        // Update queue & recent results
        List<String> queue = new ArrayList<>(parseList(as.getQueue()));
        queue.remove(playerId);
        as.setQueue(String.join(",", queue));
        addRecentResult(as, playerId, req.getTeamId(), amount, "sold");

        // Auto-advance to next player in same role tab
        advanceToNextPlayer(as, queue, p.getRole());
        stateRepo.save(as);
        return getFullState();
    }

    @Transactional
    public AuctionStateDTO markUnsold() {
        AuctionState as = getState();
        String playerId = as.getCurrentBidPlayerId();
        if (playerId == null) throw new IllegalStateException("No player on auction block");

        Player p = playerRepo.findById(playerId)
            .orElseThrow(() -> new NoSuchElementException("Player not found"));
        p.setStatus("unsold");
        playerRepo.save(p);

        List<String> queue = new ArrayList<>(parseList(as.getQueue()));
        queue.remove(playerId);
        as.setQueue(String.join(",", queue));
        addRecentResult(as, playerId, null, 0, "unsold");
        advanceToNextPlayer(as, queue, p.getRole());
        stateRepo.save(as);
        return getFullState();
    }

    @Transactional
    public AuctionStateDTO reAuction(String playerId) {
        Player p = playerRepo.findById(playerId)
            .orElseThrow(() -> new NoSuchElementException("Player not found"));
        p.setStatus("pending");
        playerRepo.save(p);

        AuctionState as = getState();
        List<String> q = new ArrayList<>(parseList(as.getQueue()));
        if (!q.contains(playerId)) q.add(playerId);
        as.setQueue(String.join(",", q));
        as.setActiveQueueTab(p.getRole() != null ? p.getRole() : "BAT");
        as.setCurrentBidPlayerId(playerId);
        as.setCurrentBidAmount(p.getBasePrice());
        as.setCurrentBidLeadingTeamId(null);
        stateRepo.save(as);
        return getFullState();
    }

    @Transactional
    public AuctionStateDTO useRTM(String teamId) {
        Team t = teamRepo.findById(teamId)
            .orElseThrow(() -> new NoSuchElementException("Team not found"));
        if (t.isRtmUsed()) throw new IllegalStateException(t.getName() + " has already used RTM!");
        t.setRtmUsed(true);
        teamRepo.save(t);

        AuctionState as = getState();
        as.setCurrentBidLeadingTeamId(teamId);
        stateRepo.save(as);
        return getFullState();
    }

    @Transactional
    public AuctionStateDTO useWildcard(String teamId) {
        Team t = teamRepo.findById(teamId)
            .orElseThrow(() -> new NoSuchElementException("Team not found"));
        int remaining = t.getPoints() - t.getPointsSpent();
        if (remaining < 50) throw new IllegalStateException("Not enough points for wildcard!");

        AuctionState as = getState();
        as.setCurrentBidAmount(50);
        as.setCurrentBidLeadingTeamId(teamId);
        stateRepo.save(as);
        return getFullState();
    }

    @Transactional
    public AuctionStateDTO switchQueueTab(String tab) {
        AuctionState as = getState();
        as.setActiveQueueTab(tab);
        stateRepo.save(as);
        return getFullState();
    }

    // ── Registered Players ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<RegisteredPlayerDTO> getAllRegistered() {
        return regRepo.findAll().stream().map(this::toRegDTO).collect(Collectors.toList());
    }

    @Transactional
    public RegisteredPlayerDTO addRegistered(AddRegisteredPlayerRequest req) {
        if (req.getName() == null || req.getName().isBlank())
            throw new IllegalArgumentException("Player name cannot be empty");
        if (req.getBasePrice() < 10 || req.getBasePrice() > 500)
            throw new IllegalArgumentException("Base price must be 10–500");
        RegisteredPlayer rp = RegisteredPlayer.builder()
            .id("reg_" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 5))
            .name(req.getName().trim()).role(req.getRole())
            .category(req.getCategory()).basePrice(req.getBasePrice())
            .phone(req.getPhone() != null ? req.getPhone().trim() : "")
            .registeredAt(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .addedToAuction(false).build();
        return toRegDTO(regRepo.save(rp));
    }

    @Transactional
    public RegisteredPlayerDTO editRegistered(String id, AddRegisteredPlayerRequest req) {
        RegisteredPlayer rp = regRepo.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Registered player not found"));
        if (req.getName() != null && !req.getName().isBlank()) rp.setName(req.getName().trim());
        if (req.getRole() != null) rp.setRole(req.getRole());
        if (req.getCategory() != null) rp.setCategory(req.getCategory());
        if (req.getBasePrice() > 0) rp.setBasePrice(req.getBasePrice());
        if (req.getPhone() != null) rp.setPhone(req.getPhone().trim());
        return toRegDTO(regRepo.save(rp));
    }

    @Transactional
    public void removeRegistered(String id) {
        regRepo.deleteById(id);
    }

    @Transactional
    public PlayerDTO sendToAuction(String regId) {
        RegisteredPlayer rp = regRepo.findById(regId)
            .orElseThrow(() -> new NoSuchElementException("Registered player not found"));
        if (rp.isAddedToAuction())
            throw new IllegalStateException(rp.getName() + " is already in the auction queue!");
        AddPlayerRequest req = new AddPlayerRequest(rp.getName(), rp.getCategory(), rp.getRole(), rp.getBasePrice());
        PlayerDTO added = addPlayer(req);
        rp.setAddedToAuction(true);
        regRepo.save(rp);
        return added;
    }

    // ── Private helpers ────────────────────────────────────────────────────

    private AuctionState getState() {
        return stateRepo.findById(STATE_ID).orElseGet(() -> {
            AuctionState s = new AuctionState();
            s.setId(STATE_ID);
            s.setQueue("");
            s.setActiveQueueTab("BAT");
            s.setRecentResultsJson("[]");
            return stateRepo.save(s);
        });
    }

    private void advanceToNextPlayer(AuctionState as, List<String> queue, String role) {
        String tab = as.getActiveQueueTab();
        Optional<String> next = queue.stream().filter(pid -> {
            Player pl = playerRepo.findById(pid).orElse(null);
            return pl != null && "pending".equals(pl.getStatus()) && tab.equals(pl.getRole());
        }).findFirst();
        if (next.isPresent()) {
            Player nextP = playerRepo.findById(next.get()).orElse(null);
            if (nextP != null) {
                as.setCurrentBidPlayerId(nextP.getId());
                as.setCurrentBidAmount(nextP.getBasePrice());
                as.setCurrentBidLeadingTeamId(null);
                return;
            }
        }
        as.setCurrentBidPlayerId(null);
        as.setCurrentBidAmount(0);
        as.setCurrentBidLeadingTeamId(null);
    }

    private void addRecentResult(AuctionState as, String playerId, String teamId, int amount, String status) {
        List<RecentResultDTO> results = parseRecentResults(as.getRecentResultsJson());
        results.add(0, RecentResultDTO.builder()
            .playerId(playerId).teamId(teamId).amount(amount)
            .status(status).ts(System.currentTimeMillis()).build());
        if (results.size() > 6) results = results.subList(0, 6);
        try {
            as.setRecentResultsJson(objectMapper.writeValueAsString(results));
        } catch (Exception e) {
            as.setRecentResultsJson("[]");
        }
    }

    private List<String> parseList(String csv) {
        if (csv == null || csv.isBlank()) return new ArrayList<>();
        return Arrays.stream(csv.split(",")).filter(s -> !s.isBlank()).collect(Collectors.toList());
    }

    private List<RecentResultDTO> parseRecentResults(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return objectMapper.readValue(json, new TypeReference<List<RecentResultDTO>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private TeamDTO toTeamDTO(Team t) {
        return TeamDTO.builder()
            .id(t.getId()).name(t.getName()).color(t.getColor())
            .points(t.getPoints()).pointsSpent(t.getPointsSpent())
            .rtmUsed(t.isRtmUsed()).players(t.getPlayerIdList()).build();
    }

    private PlayerDTO toPlayerDTO(Player p) {
        return PlayerDTO.builder()
            .id(p.getId()).name(p.getName()).category(p.getCategory())
            .role(p.getRole()).basePrice(p.getBasePrice()).soldPrice(p.getSoldPrice())
            .teamId(p.getTeamId()).status(p.getStatus()).build();
    }

    private RegisteredPlayerDTO toRegDTO(RegisteredPlayer rp) {
        return RegisteredPlayerDTO.builder()
            .id(rp.getId()).name(rp.getName()).role(rp.getRole())
            .category(rp.getCategory()).basePrice(rp.getBasePrice())
            .phone(rp.getPhone()).registeredAt(rp.getRegisteredAt())
            .addedToAuction(rp.isAddedToAuction()).build();
    }

    // ── Default builders ───────────────────────────────────────────────────

    private Team team(String id, String name, String color, int points) {
        return Team.builder().id(id).name(name).color(color)
            .points(points).pointsSpent(0).rtmUsed(false).playerIds("").build();
    }

    private Player player(String id, String name, String cat, String role, int price) {
        return Player.builder().id(id).name(name).category(cat)
            .role(role).basePrice(price).status("pending").build();
    }
}