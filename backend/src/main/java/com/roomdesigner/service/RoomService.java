package com.roomdesigner.service;

import com.roomdesigner.dto.FurnitureRequest;
import com.roomdesigner.dto.RoomRequest;
import com.roomdesigner.model.Room;
import com.roomdesigner.model.RoomFurniture;
import com.roomdesigner.repository.RoomFurnitureRepository;
import com.roomdesigner.repository.RoomRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomFurnitureRepository furnitureRepository;
    private final FurnitureService furnitureService;

    public RoomService(RoomRepository roomRepository, RoomFurnitureRepository furnitureRepository, FurnitureService furnitureService) {
        this.roomRepository = roomRepository;
        this.furnitureRepository = furnitureRepository;
        this.furnitureService = furnitureService;
    }

    @Transactional(readOnly = true)
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Room getRoom(Long id) {
        return findRoom(id);
    }

    @Transactional(readOnly = true)
    public List<RoomFurniture> getRoomFurniture(Long roomId) {
        findRoom(roomId);
        return furnitureRepository.findAllByRoomId(roomId);
    }

    public Room createRoom(RoomRequest request) {
        return roomRepository.save(new Room(request.name(), request.width(), request.length(), request.floorColor(), request.wallColor()));
    }

    public Room updateRoom(Long id, RoomRequest request) {
        Room room = findRoom(id);
        room.update(request.name(), request.width(), request.length(), request.floorColor(), request.wallColor());
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.delete(findRoom(id));
    }

    public RoomFurniture addFurniture(Long roomId, FurnitureRequest request) {
        Room room = findRoom(roomId);
        var definition = furnitureService.getCatalogItem(request.catalogId());
        RoomFurniture placed = new RoomFurniture(
            request.catalogId(), (String) definition.get("name"),
            (double) definition.get("width"), (double) definition.get("length"),
            request.x(), request.y(), request.rotation() == null ? 0 : request.rotation(),
            request.color() == null ? (String) definition.get("color") : request.color()
        );

        RoomFurniture saved = furnitureRepository.saveAndFlush(placed);
        room.addFurniture(saved);
        roomRepository.saveAndFlush(room);
        return saved;
    }

    public RoomFurniture updateFurniture(Long roomId, Long furnitureId, FurnitureRequest request) {
        if (request.catalogId() != null) {
            furnitureService.getCatalogItem(request.catalogId());
        }
        RoomFurniture placed = furnitureRepository.findByIdAndRoomId(furnitureId, roomId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Furniture item not found in this room"));
        placed.update(request.x(), request.y(), request.rotation(), request.color());
        return furnitureRepository.save(placed);
    }

    public void removeFurniture(Long roomId, Long furnitureId) {
        Room room = findRoom(roomId);
        RoomFurniture placed = furnitureRepository.findByIdAndRoomId(furnitureId, roomId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Furniture item not found in this room"));
        room.removeFurniture(placed);
        roomRepository.save(room);
    }

    private Room findRoom(Long id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
    }
}
