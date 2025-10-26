import axios from "axios";

const http = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

http.interceptors.request.use(cfg => {
  const devToken = import.meta.env.VITE_DEV_TOKEN;
  if (devToken) {
    cfg.headers.Authorization = `Bearer ${devToken}`;
  }
  return cfg;
});

export default http;