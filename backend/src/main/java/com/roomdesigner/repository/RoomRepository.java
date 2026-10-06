package com.roomdesigner.repository;

import com.roomdesigner.model.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
	@Override
	@EntityGraph(attributePaths = "furniture")
	List<Room> findAll();

	@Override
	@EntityGraph(attributePaths = "furniture")
	Optional<Room> findById(Long id);
}
