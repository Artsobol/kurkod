import axios from "axios";

const http = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  withCredentials: true,
});

const refreshHttp = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  withCredentials: true,
});

let accessToken = null;

export function setTokens({ token }) {
  accessToken = token;
}

export function clearTokens() {
  accessToken = null;
  localStorage.removeItem("token");
  localStorage.removeItem("refreshToken");
  sessionStorage.removeItem("token");
  sessionStorage.removeItem("refreshToken");
}

http.interceptors.request.use(cfg => {
  if (accessToken) {
    cfg.headers.Authorization = `Bearer ${accessToken}`;
  }
  return cfg;
});

let isRefreshing = false;
let queue = [];

http.interceptors.response.use(
  res => res,
  async err => {
    const original = err.config;
    const isAuthRequest = ["/auth/login", "/auth/register", "/auth/refresh"]
      .some(path => original?.url?.includes(path));

    if (err.response?.status !== 401 || original._retry || isAuthRequest) {
      return Promise.reject(err);
    }

    original._retry = true;

    if (isRefreshing) {
      return new Promise((resolve, reject) => queue.push({resolve, reject}))
        .then(token => {
          original.headers.Authorization = `Bearer ${token}`;
          return http(original);
        });
    }

    isRefreshing = true;

    try {
      const res = await refreshHttp.post("/auth/refresh");

      const token = res.data.accessToken;
      setTokens({token});

      queue.forEach(item => item.resolve(token));
      queue = [];

      original.headers.Authorization = `Bearer ${token}`;
      return http(original);

    } catch (e) {
      queue.forEach(item => item.reject(e));
      queue = [];
      clearTokens();

      if (!window.location.pathname.includes('/sign')) {
        window.location.href = '/sign';
      }

      return Promise.reject(e);
    } finally {
      isRefreshing = false;
    }
  }
);

export default http;
