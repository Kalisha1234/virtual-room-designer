package com.roomdesigner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_furniture")
public class RoomFurniture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String catalogId;
    private String name;
    private double width;
    private double length;
    private double x;
    private double y;
    private int rotation;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    protected RoomFurniture() {
    }

    public RoomFurniture(String catalogId, String name, double width, double length, double x, double y, int rotation, String color) {
        this.catalogId = catalogId;
        this.name = name;
        this.width = width;
        this.length = length;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.color = color;
    }

    public Long getId() { return id; }
    public String getCatalogId() { return catalogId; }
    public String getName() { return name; }
    public double getWidth() { return width; }
    public double getLength() { return length; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getRotation() { return rotation; }
    public String getColor() { return color; }

    public void setRoom(Room room) { this.room = room; }

    public void update(double x, double y, int rotation, String color) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.color = color;
    }
}
