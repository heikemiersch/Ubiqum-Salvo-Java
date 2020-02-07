package com.codeoftheweb.salvo;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Score {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double score;
    private String paula;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    public void setScore(double score) {
        this.score = score;
    }

    public Score (){}

    public Score(double score, Game game, Player player) {
        this.score = score;
        this.game = game;
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Game getGame() {
       return game;
    }
//    @JsonIgnore
//    public Player getPlayer() {
//        return player;
//    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}