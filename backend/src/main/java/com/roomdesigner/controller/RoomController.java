package com.roomdesigner.controller;

import com.roomdesigner.dto.FurnitureRequest;
import com.roomdesigner.dto.RoomRequest;
import com.roomdesigner.model.Room;
import com.roomdesigner.model.RoomFurniture;
import com.roomdesigner.service.RoomService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getRooms() {
        return roomService.getRooms();
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody RoomRequest request) {
        Room room = roomService.createRoom(request);
        return ResponseEntity.created(URI.create("/api/rooms/" + room.getId())).body(room);
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable Long id) {
        return roomService.getRoom(id);
    }

    @GetMapping("/{roomId}/furniture")
    public List<RoomFurniture> getRoomFurniture(@PathVariable Long roomId) {
        return roomService.getRoomFurniture(roomId);
    }

    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        return roomService.updateRoom(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roomId}/furniture")
    public ResponseEntity<RoomFurniture> addFurniture(@PathVariable Long roomId, @Valid @RequestBody FurnitureRequest request) {
        RoomFurniture furniture = roomService.addFurniture(roomId, request);
        return ResponseEntity.created(URI.create("/api/rooms/" + roomId + "/furniture/" + furniture.getId())).body(furniture);
    }

    @PutMapping("/{roomId}/furniture/{furnitureId}")
    public RoomFurniture updateFurniture(@PathVariable Long roomId, @PathVariable Long furnitureId, @Valid @RequestBody FurnitureRequest request) {
        return roomService.updateFurniture(roomId, furnitureId, request);
    }

    @DeleteMapping("/{roomId}/furniture/{furnitureId}")
    public ResponseEntity<Void> removeFurniture(@PathVariable Long roomId, @PathVariable Long furnitureId) {
        roomService.removeFurniture(roomId, furnitureId);
        return ResponseEntity.noContent().build();
    }
}
