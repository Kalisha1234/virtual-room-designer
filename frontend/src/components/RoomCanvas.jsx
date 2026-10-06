export default function RoomCanvas({ room, selectedFurnitureId, onFurnitureSelect }) {
  if (!room) {
    return (
      <div className="empty-room-state">
        <h3>No room selected</h3>
        <p>Create a room or pick an existing one to begin designing.</p>
      </div>
    );
  }

  const roomWidthPx = Math.max(320, room.width * 42);
  const roomHeightPx = Math.max(260, room.length * 42);

  return (
    <div className="canvas-panel">
      <div className="room-header">
        <h2>{room.name}</h2>
        <span>
          {room.width}m × {room.length}m
        </span>
      </div>

      <div
        className="room-surface"
        style={{
          width: `${roomWidthPx}px`,
          height: `${roomHeightPx}px`,
          backgroundColor: room.floorColor || '#ddd',
          borderColor: room.wallColor || '#8a8a8a'
        }}
      >
        {room.furniture?.map((item) => {
          const left = (item.x / room.width) * 100;
          const top = (item.y / room.length) * 100;
          const widthPct = (item.width / room.width) * 100;
          const heightPct = (item.length / room.length) * 100;

          return (
            <button
              key={item.id}
              type="button"
              className={`room-furniture ${selectedFurnitureId === item.id ? 'selected' : ''}`}
              onClick={() => onFurnitureSelect(item.id)}
              style={{
                left: `${left}%`,
                top: `${top}%`,
                width: `${Math.max(12, widthPct)}%`,
                height: `${Math.max(12, heightPct)}%`,
                backgroundColor: item.color || '#b0bfaa',
                transform: `rotate(${item.rotation || 0}deg)`
              }}
            >
              <span>{item.name}</span>
            </button>
          );
        })}
      </div>
    </div>
  );
}
