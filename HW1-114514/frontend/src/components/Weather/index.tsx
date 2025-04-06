import styles from './styles.module.css';

function Weather() {
  return (
    <div className={styles.mainContent}>
      <div className={styles.weatherCard}>
        <div className={styles.weatherIcon}>
          {/* Weather icon would go here */}
        </div>
        <div className={styles.weatherInfo}>
          <div className={styles.title}>Weather Component</div>
          <p className={styles.description}>Weather information will be displayed here</p>
        </div>
      </div>
    </div>
  );
}

export default Weather;
