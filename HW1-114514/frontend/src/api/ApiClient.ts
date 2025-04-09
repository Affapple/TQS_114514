import axios from "axios";
const IP: string = import.meta.env.VITE_API_URL || "http://localhost";
const PORT: string = "8080";
const BASE_API_PATH: string = "api";
const API_VERSION: string = "v1";

const apiClient = axios.create({
  baseURL: `${IP}:${PORT}/${BASE_API_PATH}/${API_VERSION}`,
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClient;
