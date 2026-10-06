package com.roomdesigner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double width;
    private double length;
    private String floorColor;
    private String wallColor;

    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomFurniture> furniture = new ArrayList<>();

    protected Room() {
    }

    public Room(String name, double width, double length, String floorColor, String wallColor) {
        this.name = name;
        this.width = width;
        this.length = length;
        this.floorColor = floorColor;
        this.wallColor = wallColor;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getWidth() { return width; }
    public double getLength() { return length; }
    public String getFloorColor() { return floorColor; }
    public String getWallColor() { return wallColor; }
    public List<RoomFurniture> getFurniture() { return furniture; }

    public void update(String name, double width, double length, String floorColor, String wallColor) {
        this.name = name;
        this.width = width;
        this.length = length;
        this.floorColor = floorColor;
        this.wallColor = wallColor;
    }

    public void addFurniture(RoomFurniture item) {
        furniture.add(item);
        item.setRoom(this);
    }

    public void removeFurniture(RoomFurniture item) {
        furniture.remove(item);
        item.setRoom(null);
    }
}
