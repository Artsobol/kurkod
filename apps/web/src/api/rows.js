import http from "@/api/http.js";

export async function getRows(workshopId) {
  try {
    const { data } = await http.get(`/api/v1/workshops/${workshopId}/rows`);
    return data.payload || [];
  } catch (error) {
    console.error("Ошибка при получении рядов:", error);
    return [];
  }
}

export async function getRow(workshopId, rowNumber) {
  try {
    const { data } = await http.get(`/api/v1/workshops/${workshopId}/rows/${rowNumber}`);
    return data.payload || null;
  } catch (error) {
    console.error("Ошибка при получении ряда:", error);
    return null;
  }
}

export async function createRow(workshopId, row) {
  try {
    const { data } = await http.post(`/api/v1/workshops/${workshopId}/rows`, row);
    return data;
  } catch (error) {
    console.error("Ошибка при создании ряда:", error);
    return null;
  }
}
