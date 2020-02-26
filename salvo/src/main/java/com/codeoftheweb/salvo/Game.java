package com.codeoftheweb.salvo;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long game_id;
    public Date creationDate;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<Score> scores;

/*    @JsonIgnore
    public Set<Score> getScore() {
        return scores;
    }*/

    public void setScore(Set<Score> scores) {
        this.scores = scores;
    }


    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Game(){}

    public Game(Date date) {
        this.creationDate = date;
    }

    public long getGame_id() {
        return game_id;
    }

    public void setGame_id(long game_id) {
        this.game_id = game_id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

   /* @JsonIgnore
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }*/

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }


   /* public void addScore(Score score) {
        score.setGame(this);
        scores.add(score);
    }*/
}
