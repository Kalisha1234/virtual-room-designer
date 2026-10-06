package com.roomdesigner.repository;

import com.roomdesigner.model.RoomFurniture;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomFurnitureRepository extends JpaRepository<RoomFurniture, Long> {
    List<RoomFurniture> findAllByRoomId(Long roomId);
    Optional<RoomFurniture> findByIdAndRoomId(Long id, Long roomId);
}
