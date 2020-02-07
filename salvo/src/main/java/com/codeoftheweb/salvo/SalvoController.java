package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@CrossOrigin(origins = "http://localhost:8081/")
@RestController
@RequestMapping("/api")

public class SalvoController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository repositoryGamePlayer;

    @Autowired
    private PlayerRepository playerRepository;
    @CrossOrigin(origins = "http://localhost:8081/")
    @RequestMapping(path="/games")
    public List<Object> getAll(Authentication auth) {
        System.out.println("logged in user " + auth.getName());
       List<Object> games_info = new ArrayList<>();

        gameRepository.findAll().forEach(game -> {
            Map<String, Object> GamesJson= new HashMap<>();
            GamesJson.put("game_id", game.getGame_id());
            GamesJson.put("creation_date", game.getCreationDate().toString());
            GamesJson.put("game_player", getGamePlayer(game));

            games_info.add(GamesJson);
        });
        return games_info;
    }

    public List<Object> getGamePlayer (Game game) {
        List<Object> players_in_game = new ArrayList<>();
        game.gamePlayers.forEach (GP -> {
            Map<String, Object> GamesJson= new HashMap<>();
            GamesJson.put("game_player_id", GP.getGamePlayer_id());
            GamesJson.put("player", getPlayer(GP));

            players_in_game.add(GamesJson);
        });
        return players_in_game;
    }

    public List<Object> getPlayer (GamePlayer gamePlayer){
        Game game = gamePlayer.getGame();
        List<Object> player = new ArrayList<>();
        Map<String,Object> PlayerJson = new HashMap<>();
        PlayerJson.put("username", gamePlayer.getPlayer().getUserName());
        PlayerJson.put("missionstatement", gamePlayer.getPlayer().getMissionstatement());
        PlayerJson.put("playerID", gamePlayer.getPlayer().getId());
        PlayerJson.put("scores", gamePlayer.getPlayer().getCurrentScore(game));
        PlayerJson.put("total", gamePlayer.getPlayer().getTotal());

            player.add(PlayerJson);
            return player;
    }
    @CrossOrigin(origins = "http://localhost:8081/")
    @RequestMapping(path="/game_view/{gamePlayerId}")
    public List<Object> getGame(@PathVariable long gamePlayerId) {
        List<Object> game_info = new ArrayList<>();

        GamePlayer gamePlayer = repositoryGamePlayer.getOne(gamePlayerId);
        Game game = gamePlayer.getGame();
        Map<String, Object> GameViewJson= new HashMap<>();
        GameViewJson.put("game_id", gamePlayer.getGame().getGame_id());
        GameViewJson.put("creation_date", gamePlayer.getGame().getCreationDate());
        GameViewJson.put("player", gamePlayer.getPlayer());
        if(game.getGamePlayers().size()==2){
            GameViewJson.put("salvoesOpponent", salvoesInfo(gamePlayer.getOpponent(gamePlayer)));
            GameViewJson.put("opponent", gamePlayer.getOpponent(gamePlayer).getPlayer().getUserName());
        }
        else {
            GameViewJson.put("salvoesOpponent", null);
            GameViewJson.put("opponent", null);
        }
        GameViewJson.put("game_player_id", gamePlayerId);
        GameViewJson.put("ships", shipsInfo(gamePlayer));
        GameViewJson.put("salvoes", salvoesInfo(gamePlayer));
        GameViewJson.put("scores", gamePlayer.getPlayer().getCurrentScore(gamePlayer.getGame()));

        game_info.add(GameViewJson);
        return game_info;
    }

   List<Object> shipsInfo(GamePlayer gameplayer) {
        List<Object> ship_info = new ArrayList<>();
        gameplayer.getShips().forEach (ship -> {
            Map<String, Object> ShipTypLocJson = new HashMap<>();
            ShipTypLocJson.put("type", ship.getType());
            ShipTypLocJson.put("shipLocation", ship.getShipLocation());

            ship_info.add(ShipTypLocJson);
        });
        return ship_info;
    }

    List<Object> salvoesInfo(GamePlayer gamePlayer) {
        List<Object> salvo_info = new ArrayList<>();
        int turn = 1;
        Set<Salvo> mySalvo = gamePlayer.getSalvoes();
        for (Salvo salvo : mySalvo) {
            Map<String, Object> SalvoTurnLocJson = new HashMap<>();
            SalvoTurnLocJson.put("turn", turn);
            SalvoTurnLocJson.put("salvoLocation", salvo.getSalvoLocation());
            turn += 1;
            salvo_info.add(SalvoTurnLocJson);
        }
        return salvo_info;
    }
    @CrossOrigin(origins = "http://localhost:8081/")
    @RequestMapping(path="/leaderboard")
    public List<Object> getLeaderboard() {
        List<Object> leaderboard_info = new ArrayList<>();
        playerRepository.findAll().forEach(playerForLeaderboard -> {
            Map<String, Object> LeaderboardJson = new HashMap<>();
            LeaderboardJson.put("player", playerForLeaderboard.getUserName());
            LeaderboardJson.put("wins", playerForLeaderboard.getWins());
            LeaderboardJson.put("losses", playerForLeaderboard.getLosses());
            LeaderboardJson.put("ties", playerForLeaderboard.getTies());
            LeaderboardJson.put("total", playerForLeaderboard.getTotal());

            leaderboard_info.add(LeaderboardJson);
        });
        return leaderboard_info;
    }

    @Bean
    public PasswordEncoder passwordEncoder2() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @CrossOrigin(origins = "http://localhost:8081/")
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String userName, @RequestParam String password, @RequestParam String missionstatement) {
        System.out.println("pw: " +password + " user: "+ userName);
        if (userName.isEmpty() ||  password.isEmpty() || missionstatement.isEmpty()) {
            return new ResponseEntity<>("something's missing", HttpStatus.FORBIDDEN);
        }
        if (playerRepository.findByUserName(userName) !=  null) {
            System.out.println(playerRepository.findByUserName(userName));
            return new ResponseEntity<>("name already in use", HttpStatus.CONFLICT);
        }
        Player newPlayer= playerRepository.save(new Player(userName, passwordEncoder2().encode(password), missionstatement));
        System.out.println("new player "+ newPlayer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // add a createGame method
    @CrossOrigin(origins = "http://localhost:8081/")
    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(Authentication authentication) {
        Player currentUser = playerRepository.findByUserName(authentication.getName());
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Game game = new Game(new Date());
        GamePlayer gamePlayer = new GamePlayer(currentUser, game);
        gameRepository.save(game);
        repositoryGamePlayer.save(gamePlayer);
        return new ResponseEntity<>(doMap("id",gamePlayer.getGamePlayer_id()), HttpStatus.CREATED);
    }

    public Map<String,Object> doMap(String key, Object value){
        Map<String, Object>  horn = new HashMap<>();
        horn.put(key, value);

        return horn;
    }
    @CrossOrigin(origins = "http://localhost:8081/")
    @RequestMapping(path = "/games/join", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame( @RequestParam Long gameId, Authentication authentication) {

        System.out.println(gameId);
        System.out.println(authentication.getName());
        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());

        Optional<Game> currentGame = gameRepository.findById(gameId);

        if(currentGame.isPresent()) {
            final Object[] body = {""};
            final long gpid;
            currentGame.get().getGamePlayers().forEach(gamePlayer -> {
                if (gamePlayer.getPlayer() != loggedPlayer && currentGame.get().getGamePlayers().size()<2){
                    GamePlayer newGamePlayer = repositoryGamePlayer.save(new GamePlayer(loggedPlayer, currentGame.get()));
                    currentGame.get().addGamePlayer(newGamePlayer);
                    body[0] = (newGamePlayer.getGamePlayer_id());
                }
                else body[0] = "GO AWAY: YOU'RE ALREADY IN THIS GAME!";
            });
            return new ResponseEntity<>(doMap("id", body[0]), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("no game found", HttpStatus.CONFLICT);
        }
    }
    @CrossOrigin(origins = "http://localhost:8081/")
    @RequestMapping(path = "/games/players/{gameId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Object> addShips(@PathVariable long gameId, @RequestBody List<Ship> ships, Authentication authentication) {
        System.out.println(ships);
        Player currentUser = playerRepository.findByUserName(authentication.getName());
        GamePlayer gamePlayer = repositoryGamePlayer.findById(gameId).orElse(null);
        if (authentication.getName() == null) {
            return new ResponseEntity<>("there is no current user logged in", HttpStatus.UNAUTHORIZED);
        }
        if (currentUser == null) {
            return new ResponseEntity<>("there is no game player with the given ID", HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.getPlayer().getUserName() != authentication.getName()) {
            return new ResponseEntity<>("the current user is not the game player the ID references", HttpStatus.UNAUTHORIZED);
        }
        if (ships.size() == 5) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            ships.forEach(shippo -> {
                gamePlayer.addShip(shippo);
                repositoryGamePlayer.save(gamePlayer);

            });
            return new ResponseEntity<>(ships, HttpStatus.CREATED);
        }
    }
}