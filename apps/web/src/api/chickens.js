import http from "@/api/http.js";

export async function getChickens() {
  const { data } = await http.get("/api/v1/chickens");
  return data.payload || [];
}

export async function getChicken(id) {
  try {
    const { data } = await http.get(`/api/v1/chickens/${id}`);
    return data.payload || [];
  } catch (error) {
    console.error(error);
    return null;
  }
}