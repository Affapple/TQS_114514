import { CacheStats } from "@Types/CacheStats";
import { Forecast } from "@Types/Forecast";
import apiClient from "@api/ApiClient";
import { AxiosResponse } from "axios";

function getWeatherCacheStats(): Promise<AxiosResponse<CacheStats>> {
  const response = apiClient.get("/weather/cache/stats");
  return response;
}

function getWeatherForecast(): Promise<AxiosResponse<Forecast[]>> {
  const response = apiClient.get("/weather/forecast");
  return response;
};

export { getWeatherCacheStats, getWeatherForecast};
