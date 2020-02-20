package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    //private String salvoLocation;
    private int turn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="salvo_location")
    private List<String> salvoLocation = new ArrayList<>();

    public Salvo() {
    }

    public Salvo(int turn, List<String> salvoLocation) {
        this.turn = turn;
        this.salvoLocation = salvoLocation;
    }

    public void setSalvoLocation(List<String> salvoLocation) {
        this.salvoLocation = salvoLocation;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getSalvoLocation() {
        return salvoLocation;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public String toString() {
        return "Salvo{" +
                "id=" + id +
                ", turn=" + turn +
                ", gamePlayer=" + gamePlayer +
                ", salvoLocation=" + salvoLocation +
                '}';
    }
}
