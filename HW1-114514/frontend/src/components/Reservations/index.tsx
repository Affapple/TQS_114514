import { checkinReservation, cancelReservation, getAllReservations } from "@api/Reservation";
import { getRestaurantById } from "@api/Restaurant";
import { AppContext } from "@hooks/AppContext";
import { Reservation } from "@Types/Reservation";
import { Restaurant } from "@Types/Restaurant";
import { useContext, useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import styles from "./styles.module.css";

function ReservationDetails() {
  const { id: restaurantId } = useParams<{id: string}>();
  const { mode } = useContext(AppContext);
  const navigate = useNavigate();
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
  const [reservations, setReservations] = useState<Reservation[]>([]);


  const fetchReservations = async (id: number) => {
    try {
      const response = await getAllReservations(id);
      
      if (response.status === 200) {
        setReservations(response.data);
      } else {
        const errorMessage = typeof response.data === 'object' && response.data !== null && 'message' in response.data 
          ? response.data.message 
          : "Erro desconhecido";
        setError("Erro ao carregar reservas: " + errorMessage);
      }
    } catch (err) {
      setError("Erro ao carregar reservas: " + (err instanceof Error ? err.message : "Erro desconhecido"));
    }
  }

  const fetchRestaurant = async () => {
    if (!restaurantId) {
      setError("ID do restaurante não fornecido");
      setLoading(false);
      return;
    }

    try {
      const response = await getRestaurantById(restaurantId);
      
      if (response.status === 200) {
        setRestaurant(response.data);
        if (response.data && response.data.id) {
          await fetchReservations(response.data.id);
        }
      } else {
        const errorMessage = typeof response.data === 'object' && response.data !== null && 'message' in response.data 
          ? response.data.message 
          : "Erro desconhecido";
        setError("Erro ao carregar restaurante: " + errorMessage);
      }
    } catch (err) {
      console.error("Error fetching restaurant:", err);
      setError("Erro ao carregar restaurante: " + (err instanceof Error ? err.message : "Erro desconhecido"));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    setLoading(true);
    fetchRestaurant();
  }, [restaurantId]);

  const handleCancel = async (reservationCode: string) => {
    try {
      await cancelReservation(reservationCode);
      await fetchReservations(restaurant.id);
    } catch (err) {
      console.error("Error canceling reservation:", err);
    }
  };

  const handleCheckin = async (reservationCode: string) => {
    try {
      await checkinReservation(reservationCode);
      await fetchReservations(restaurant.id);
    } catch (err) {
      console.error("Error checking in reservation:", err);
    }
  };
  
  if (mode === "User") {
    return <div>Você não tem permissão para ver as reservas</div>;
  }

  if (loading) {
    return (
      <div className={styles.loading}>
        <div className={styles.loadingSpinner}></div>
      </div>
    );
  }
  
  if (error) {
    return (
      <div className={styles.emptyState}>
        <div className={styles.emptyStateIcon}>⚠️</div>
        <div className={styles.emptyStateText}>Erro</div>
        <div className={styles.emptyStateSubtext}>{error}</div>
      </div>
    );
  }

  if (!restaurant) {
    return (
      <div className={styles.emptyState}>
        <div className={styles.emptyStateIcon}>❓</div>
        <div className={styles.emptyStateText}>Restaurante não encontrado</div>
        <div className={styles.emptyStateSubtext}>
          Não foi possível encontrar os dados do restaurante selecionado.
        </div>
      </div>
    );
  }


 return (
    <div className={`${styles.container}`}>
      <header className={styles.header}>
        <div className={styles.restaurantInfo}>
          <h1 className={styles.restaurantName}>{restaurant.name}</h1>
          <div className={styles.restaurantLocation}>
            {restaurant.location}
          </div>
        </div>
      </header>
      
      <main>
        {reservations.length > 0 ? (
          <div className={styles.reservationsList}>
            {reservations.map((reservation) => (
              <div key={reservation.code} className={styles.reservationItem}>
                <div>Código: <span className={styles.reservationCode}>{reservation.code}</span></div>
                <div>Status: <span className={styles.reservationStatus}>{reservation.status}</span></div>
                <div className={styles.buttonsContainer}>
                  <button className={styles.cancelButton + " " + styles.bigButton} onClick={() => handleCancel(reservation.code)}>Cancelar</button>
                  <button className={styles.confirmButton + " " + styles.bigButton} onClick={() => handleCheckin(reservation.code)}>Check-in</button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className={styles.emptyState}>
            <div className={styles.emptyStateIcon}>📅</div>
            <div className={styles.emptyStateText}>Nenhuma reserva encontrada</div>
            <div className={styles.emptyStateSubtext}>
              Não há reservas para este restaurante no momento.
            </div>
          </div>
        )}
      </main>
    </div>
  );
}

export default ReservationDetails;