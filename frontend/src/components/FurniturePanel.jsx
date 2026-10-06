export default function FurniturePanel({ catalog, room, selectedFurnitureId, onAddFurniture, onChangeFurniture, onDeleteFurniture }) {
  const selectedItem = room?.furniture?.find((item) => item.id === selectedFurnitureId);

  return (
    <div className="panel">
      <h3>Furniture catalogue</h3>
      <div className="catalog-grid">
        {catalog.map((item) => (
          <button
            key={item.id}
            type="button"
            className="catalog-item"
            onClick={() => onAddFurniture(item)}
            style={{ backgroundColor: item.color }}
          >
            {item.name}
          </button>
        ))}
      </div>

      {selectedItem ? (
        <div className="selected-furniture">
          <h4>{selectedItem.name}</h4>

          <label>
            Color
            <input
              type="color"
              value={selectedItem.color || '#b0bfaa'}
              onChange={(event) => onChangeFurniture({ ...selectedItem, color: event.target.value })}
            />
          </label>

          <div className="two-col compact">
            <label>
              X
              <input
                type="number"
                step="0.25"
                value={selectedItem.x}
                onChange={(event) => onChangeFurniture({ ...selectedItem, x: Number(event.target.value) })}
              />
            </label>
            <label>
              Y
              <input
                type="number"
                step="0.25"
                value={selectedItem.y}
                onChange={(event) => onChangeFurniture({ ...selectedItem, y: Number(event.target.value) })}
              />
            </label>
          </div>

          <div className="movement-controls">
            <button type="button" onClick={() => onChangeFurniture({ ...selectedItem, x: Number((selectedItem.x - 0.25).toFixed(2)) })}>←</button>
            <button type="button" onClick={() => onChangeFurniture({ ...selectedItem, x: Number((selectedItem.x + 0.25).toFixed(2)) })}>→</button>
            <button type="button" onClick={() => onChangeFurniture({ ...selectedItem, y: Number((selectedItem.y - 0.25).toFixed(2)) })}>↑</button>
            <button type="button" onClick={() => onChangeFurniture({ ...selectedItem, y: Number((selectedItem.y + 0.25).toFixed(2)) })}>↓</button>
          </div>

          <div className="button-row">
            <button type="button" onClick={() => onChangeFurniture({ ...selectedItem, rotation: (selectedItem.rotation + 15) % 360 })}>Rotate +15°</button>
            <button type="button" onClick={() => onChangeFurniture({ ...selectedItem, rotation: (selectedItem.rotation - 15 + 360) % 360 })}>Rotate -15°</button>
          </div>

          <button type="button" className="danger" onClick={onDeleteFurniture}>
            Remove furniture
          </button>
        </div>
      ) : (
        <p className="muted">Pick a room item to adjust position, color, or rotation.</p>
      )}
    </div>
  );
}
