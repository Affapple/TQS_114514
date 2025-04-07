import { useEffect, useState } from "react";
import { getWeatherForecast, getWeatherCacheStats } from "@api/Weather";
import { Forecast } from "@Types/Forecast";
import styles from "./styles.module.css";
import { format } from "date-fns";
import { pt } from "date-fns/locale";
import { CacheStats } from "@Types/CacheStats";

function Weather() {
  const [forecasts, setForecasts] = useState<Forecast[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [errorStats, setErrorStats] = useState<string>("Loading cache stats...");
  const [stats, setStats] = useState<CacheStats | null>(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const response = await getWeatherCacheStats();
        setStats(response.data);
      } catch (err) {
        setErrorStats("Failed to load cache stats");
        console.error(err);
      }
    };

    const fetchWeather = async () => {
      try {
        setLoading(true);
        const response = await getWeatherForecast();
        console.log(response.data);
        setForecasts(response.data);
      } catch (err) {
        setError("Failed to load weather data");
        console.error(err);
      } finally {
        setLoading(false);
        fetchStats();
      }
    };

    fetchWeather();
  }, []);

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return format(date, "EEE, d MMM", { locale: pt });
  };

  console.log("Stats: ",stats);

  if (loading) {
    return (
      <div className={styles.mainContent}>
        <div className={styles.weatherCard}>
          <div className={styles.loading}>Loading weather data...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.mainContent}>
        <div className={styles.weatherCard}>
          <div className={styles.error}>{error}</div>
        </div>
      </div>
    );
  }

  return (
    <>
      <div className={styles.mainContent}>
        <div className={styles.weatherBanner}>
          {forecasts.map((forecast) => (
            <div key={forecast.date} className={styles.weatherCard}>
              <div className={styles.date}>{formatDate(forecast.date)}</div>
              <div className={styles.weatherInfo}>
                <div className={styles.weatherInfoRow}>
                  <div className={styles.temperature}>
                    <span className={styles.minTemp}>
                      {Math.round(forecast.mintemp)}°
                    </span>
                    <span className={styles.maxtemp}>
                      {Math.round(forecast.maxtemp)}°
                    </span>
                  </div>
                  <div className={styles.weatherIcon}>
                    <img
                      src={forecast.icon}
                      alt={forecast.description}
                      className={styles.icon}
                    />
                  </div>
                </div>
                <div className={styles.description}>{forecast.description}</div>
                <div className={styles.humidity}>
                  <span className={styles.humidityIcon}>💧</span>
                  {forecast.avghumidity}%
                </div>
              </div>
            </div>
          ))}
        </div>

        {stats ? (
            <div className={styles.statsRow}>
              <div className={styles.statsItem}>
                <span className={styles.statsLabel}>Cache Hits</span>
                <span className={styles.statsValue}>{stats.cacheHits}</span>
              </div>
              <div className={styles.statsItem}>
                <span className={styles.statsLabel}>Cache Misses</span>
                <span className={styles.statsValue}>{stats.cacheMisses}</span>
              </div>
              <div className={styles.statsItem}>
                <span className={styles.statsLabel}>Total Calls</span>
                <span className={styles.statsValue}>{stats.totalCalls}</span>
              </div>
          </div>
        ) : (
          <div className={styles.statsRow}>
            <div className={styles.statsItem}>
              <span>{errorStats}</span>
            </div>
          </div>
        )}
      </div>
    </>
  );
}

export default Weather;
