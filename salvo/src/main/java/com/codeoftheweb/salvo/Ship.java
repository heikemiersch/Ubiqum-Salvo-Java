package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String type;
    //    (carrier 5 spaces, battleship 4, cruiser 3, submarine 3, destroyer 2)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="ship_location")
    private List<String> location = new ArrayList<>();

    public Ship() { }

    public Ship(String type, List location) {
        this.type = type;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    public GamePlayer getGamePlayer(){
        return this.gamePlayer;
    }

    public long getId() {
        return id;
    }

    public void setGame_id(long game_id) {
        this.id = game_id;
    }

    public List<String> getShipLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

}
