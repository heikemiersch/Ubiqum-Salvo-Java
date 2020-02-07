package com.codeoftheweb.salvo;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long gamePlayer_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer() {}

    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public long getGamePlayer_id() {
        return gamePlayer_id;
    }

    public void setGamePlayer_id(long gamePlayer_id) {
        this.gamePlayer_id = gamePlayer_id;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void addShip(Ship ship){
        System.out.println(ship);
        ship.setGamePlayer(this);
        this.ships.add(ship);
        // return a set of ships
    }

    public Set<Ship> getShips() {
        return ships;
    }

    //gamePlayer.getSalvoes() should return a list of salvo objects,
    //describing the salvo the gamePlayer has fired

    public void addSalvo(Salvo salvo){
        System.out.println(salvo);
        salvo.setGamePlayer(this);
        this.salvoes.add(salvo);
    }

    public GamePlayer getOpponent(GamePlayer gamePlayer){
        Map<String, GamePlayer> oppo = new HashMap<>();
        if(gamePlayer.getGame().getGamePlayers().size() == 2){
            gamePlayer.getGame().getGamePlayers()
                    .stream()
                    .forEach(gp -> {
                        if(gp.getGamePlayer_id() != gamePlayer.getGamePlayer_id()){
                            oppo.put("opponent", gp);
                        }
                    });
            return oppo.get("opponent");
        } else {
            return null;
        }
    }



}
