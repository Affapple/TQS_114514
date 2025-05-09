import { Reservation } from "@Types/Reservation";
import styles from "./styles.module.css";
import { cancelReservation } from "@api/Reservation";
import { useState } from "react";

function UserReservation({
  reservation,
  setReservation,
}: {
  reservation: Reservation;
  setReservation: (reservation: Reservation) => void;
}) {
  const [message, setMessage] = useState<string>("");

  const handleCancelReservation = async () => {
    const response = await cancelReservation(reservation.code);
    setReservation({ ...reservation, status: "CANCELLED" });
    if (response.status === 200) {
      setMessage("Reserva cancelada com sucesso");
    } else {
      setMessage("Falha ao cancelar a reserva");
    }
  };

  return (
    <div className={styles.reservationCard}>
      <h2 className={styles.title}>Reservation Details</h2>
      <div className={styles.details}>
        <div className={styles.detailRow}>
          <span className={styles.label}>Code:</span>
          <span className={styles.value} id="reservation-code">
            {reservation.code}
          </span>
        </div>
        <div className={styles.detailRow}>
          <span className={styles.label}>Status:</span>
          <span className={styles.value} id="reservation-status">
            {reservation.status}
          </span>
        </div>
        <button
          id="cancel-reservation"
          className={styles.cancelButton}
          onClick={handleCancelReservation}
        >
          Cancelar Reserva
        </button>
      </div>

      {message && <span className={styles.message}>{message}</span>}
    </div>
  );
}

export default UserReservation;
