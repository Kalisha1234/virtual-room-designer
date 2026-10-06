export default function RoomSettings({ room, form, onChange, onSubmit, onReset, onDelete, isEditing }) {
  const field = (event) => {
    onChange({ ...form, [event.target.name]: event.target.value });
  };

  return (
    <div className="panel">
      <h3>{isEditing ? 'Edit room' : 'Create room'}</h3>

      <div className="field-group">
        <label>
          Room name
          <input name="name" value={form.name} onChange={field} placeholder="Studio" />
        </label>
      </div>

      <div className="two-col">
        <label>
          Width (m)
          <input name="width" type="number" min="2" step="0.5" value={form.width} onChange={field} />
        </label>
        <label>
          Length (m)
          <input name="length" type="number" min="2" step="0.5" value={form.length} onChange={field} />
        </label>
      </div>

      <div className="two-col">
        <label>
          Floor color
          <input name="floorColor" type="color" value={form.floorColor} onChange={field} />
        </label>
        <label>
          Wall color
          <input name="wallColor" type="color" value={form.wallColor} onChange={field} />
        </label>
      </div>

      <div className="button-row">
        <button className="primary" type="button" onClick={onSubmit}>
          {isEditing ? 'Save room' : 'Create room'}
        </button>
        <button type="button" className="secondary" onClick={onReset}>
          Clear
        </button>
      </div>

      {room && (
        <button type="button" className="danger" onClick={onDelete}>
          Delete selected room
        </button>
      )}
    </div>
  );
}
