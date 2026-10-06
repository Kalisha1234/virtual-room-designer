const API_BASE = 'http://localhost:8080/api';

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    },
    ...options
  });

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get('content-type') || '';
  const data = contentType.includes('application/json') ? await response.json() : null;

  if (!response.ok) {
    throw new Error(data?.message || 'Request failed');
  }

  return data;
}

export const getRooms = () => request('/rooms');
export const getRoomById = (id) => request(`/rooms/${id}`);
export const getRoomFurniture = (roomId) => request(`/rooms/${roomId}/furniture`);
export const createRoom = (payload) => request('/rooms', { method: 'POST', body: JSON.stringify(payload) });
export const updateRoom = (id, payload) => request(`/rooms/${id}`, { method: 'PUT', body: JSON.stringify(payload) });
export const deleteRoom = (id) => request(`/rooms/${id}`, { method: 'DELETE' });

export const getFurnitureCatalog = () => request('/furniture');
export const addFurnitureToRoom = (roomId, payload) => request(`/rooms/${roomId}/furniture`, { method: 'POST', body: JSON.stringify(payload) });
export const updateFurnitureInRoom = (roomId, furnitureId, payload) => request(`/rooms/${roomId}/furniture/${furnitureId}`, { method: 'PUT', body: JSON.stringify(payload) });
export const deleteFurnitureFromRoom = (roomId, furnitureId) => request(`/rooms/${roomId}/furniture/${furnitureId}`, { method: 'DELETE' });
