import http, { setTokens, clearTokens } from "./http.js";

export const registerUser = async (data) => {
  const res = await http.post("/auth/register", data);

  const payload = res.data;
  setTokens({ token: payload.accessToken });

  return payload;
};

export const loginUser = async (data) => {
  const res = await http.post("/auth/login", data);

  const payload = res.data;
  setTokens({ token: payload.accessToken });

  return payload;
};

export const refreshToken = () => {
  return http.post("/auth/refresh");
};

export const logoutUser = async () => {
  try {
    await http.post("/auth/logout");
  } finally {
    clearTokens();
  }
};
