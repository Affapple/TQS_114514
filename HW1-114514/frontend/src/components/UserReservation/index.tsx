import { getReservation } from "@api/Reservation";
import { Reservation } from "@Types/Reservation";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import styles from './styles.module.css';

function UserReservation({ reservation }: { reservation: Reservation }) {
  const [error, setError] = useState<string>("");

  return (
      <div className={styles.reservationCard}>
        <h2 className={styles.title}>Reservation Details</h2>
        <div className={styles.details}>
          <div className={styles.detailRow}>
            <span className={styles.label}>Code:</span>
            <span className={styles.value}>{reservation.code}</span>
          </div>
          <div className={styles.detailRow}>
            <span className={styles.label}>Status:</span>
            <span className={styles.value}>{reservation.status}</span>
          </div>
          <div className={styles.detailRow}>
            <span className={styles.label}>Restaurant ID:</span>
            <span className={styles.value}>{reservation.restaurantId}</span>
          </div>
          <div className={styles.detailRow}>
            <span className={styles.label}>Menu ID:</span>
            <span className={styles.value}>{reservation.menuId}</span>
        </div>
      </div>
    </div>
  );
}

export default UserReservation;
