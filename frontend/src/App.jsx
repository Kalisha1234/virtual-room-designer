import { useEffect, useMemo, useState } from 'react';
import RoomCanvas from './components/RoomCanvas';
import RoomSettings from './components/RoomSettings';
import FurniturePanel from './components/FurniturePanel';
import {
  addFurnitureToRoom,
  createRoom,
  deleteFurnitureFromRoom,
  deleteRoom,
  getFurnitureCatalog,
  getRoomFurniture,
  getRooms,
  updateFurnitureInRoom,
  updateRoom
} from './services/roomService';

const emptyForm = {
  name: 'Studio',
  width: 5,
  length: 4,
  floorColor: '#d9c9aa',
  wallColor: '#f6f1e8'
};

export default function App() {
  const [rooms, setRooms] = useState([]);
  const [catalog, setCatalog] = useState([]);
  const [selectedRoomId, setSelectedRoomId] = useState(null);
  const [selectedFurnitureId, setSelectedFurnitureId] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [status, setStatus] = useState('');

  const selectedRoom = useMemo(
    () => rooms.find((room) => room.id === selectedRoomId) || null,
    [rooms, selectedRoomId]
  );

  const loadRooms = async () => {
    try {
      const roomsList = await getRooms();
      const loadedRooms = await Promise.all(
        roomsList.map(async (room) => ({
          ...room,
          furniture: await getRoomFurniture(room.id)
        }))
      );
      setRooms(loadedRooms);
      if (!selectedRoomId && loadedRooms.length > 0) {
        setSelectedRoomId(loadedRooms[0].id);
      }
    } catch (error) {
      setStatus(error.message);
    }
  };

  const loadCatalog = async () => {
    try {
      const items = await getFurnitureCatalog();
      setCatalog(items);
    } catch (error) {
      setStatus(error.message);
    }
  };

  useEffect(() => {
    loadRooms();
    loadCatalog();
  }, []);

  const handleSelectRoom = (roomId) => {
    setSelectedRoomId(roomId);
    setSelectedFurnitureId(null);
    const room = rooms.find((item) => item.id === roomId);
    if (room) {
      setForm({
        name: room.name,
        width: room.width,
        length: room.length,
        floorColor: room.floorColor,
        wallColor: room.wallColor
      });
    }
  };

  const resetForm = () => {
    setSelectedRoomId(null);
    setSelectedFurnitureId(null);
    setForm(emptyForm);
  };

  const handleRoomSubmit = async () => {
    try {
      const payload = {
        name: form.name,
        width: Number(form.width),
        length: Number(form.length),
        floorColor: form.floorColor,
        wallColor: form.wallColor
      };

      let savedRoom;
      if (selectedRoomId) {
        savedRoom = await updateRoom(selectedRoomId, payload);
        setStatus('Room updated');
      } else {
        savedRoom = await createRoom(payload);
        setStatus('Room created');
      }

      await loadRooms();
      if (savedRoom?.id) {
        handleSelectRoom(savedRoom.id);
      }
    } catch (error) {
      setStatus(error.message);
    }
  };

  const handleDeleteRoom = async () => {
    if (!selectedRoomId) return;

    try {
      await deleteRoom(selectedRoomId);
      setStatus('Room deleted');
      resetForm();
      await loadRooms();
    } catch (error) {
      setStatus(error.message);
    }
  };

  const handleAddFurniture = async (item) => {
    if (!selectedRoomId) {
      setStatus('Select a room before adding furniture');
      return;
    }

    try {
      const placed = await addFurnitureToRoom(selectedRoomId, {
        catalogId: item.id,
        x: 1,
        y: 1,
        rotation: 0,
        color: item.color
      });
      setSelectedFurnitureId(placed.id);
      await loadRooms();
      setStatus(`${item.name} added to the room`);
    } catch (error) {
      setStatus(error.message);
    }
  };

  const handleFurnitureChange = async (nextFurniture) => {
    if (!selectedRoomId || !selectedFurnitureId) return;

    try {
      const roomItem = selectedRoom.furniture.find((item) => item.id === selectedFurnitureId);
      await updateFurnitureInRoom(selectedRoomId, selectedFurnitureId, {
        catalogId: roomItem.catalogId,
        x: Number(nextFurniture.x),
        y: Number(nextFurniture.y),
        rotation: Number(nextFurniture.rotation),
        color: nextFurniture.color
      });
      await loadRooms();
      setSelectedFurnitureId(nextFurniture.id);
    } catch (error) {
      setStatus(error.message);
    }
  };

  const handleDeleteFurniture = async () => {
    if (!selectedRoomId || !selectedFurnitureId) return;

    try {
      await deleteFurnitureFromRoom(selectedRoomId, selectedFurnitureId);
      setSelectedFurnitureId(null);
      await loadRooms();
      setStatus('Furniture removed');
    } catch (error) {
      setStatus(error.message);
    }
  };

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <h1>Virtual Room Designer</h1>
        <p className="subtitle">A beginner-friendly room planner</p>

        <div className="room-list-panel panel">
          <h3>Saved rooms</h3>
          {rooms.length === 0 ? (
            <p className="muted">No rooms yet.</p>
          ) : (
            rooms.map((room) => (
              <button
                key={room.id}
                type="button"
                className={`room-chip ${room.id === selectedRoomId ? 'active' : ''}`}
                onClick={() => handleSelectRoom(room.id)}
              >
                {room.name}
              </button>
            ))
          )}
        </div>

        <RoomSettings
          room={selectedRoom}
          form={form}
          onChange={setForm}
          onSubmit={handleRoomSubmit}
          onReset={resetForm}
          onDelete={handleDeleteRoom}
          isEditing={Boolean(selectedRoomId)}
        />
      </aside>

      <main className="workspace">
        <RoomCanvas room={selectedRoom} selectedFurnitureId={selectedFurnitureId} onFurnitureSelect={setSelectedFurnitureId} />

        <FurniturePanel
          catalog={catalog}
          room={selectedRoom}
          selectedFurnitureId={selectedFurnitureId}
          onAddFurniture={handleAddFurniture}
          onChangeFurniture={handleFurnitureChange}
          onDeleteFurniture={handleDeleteFurniture}
        />
      </main>

      {status && <div className="status-banner">{status}</div>}
    </div>
  );
}
