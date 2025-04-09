import { getAllRestaurants } from "@api/Restaurant";
import { Restaurant } from "@Types/Restaurant";
import { useEffect, useState } from "react";
import styles from "./styles.module.css";
import { useNavigate } from "react-router";
import UserReservation from "../UserReservation";
import { getReservation } from "@api/Reservation";
import { Reservation } from "@Types/Reservation";

export default function Restaurants() {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const [reservationCode, setReservationCode] = useState("");
  const [reservation, setReservation] = useState<Reservation | null>(null);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchRestaurants = async () => {
      const response = await getAllRestaurants();

      if (response.status === 200) {
        setRestaurants(response.data);
      }
    };

    fetchRestaurants();
  }, []);

  const openModal = () => {
    setReservationCode("");
    setReservation(null);
    setError("");
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const handleSubmit = () => {
    if (reservationCode.length === 0) {
      setError("Código da reserva inválido");
      return;
    }
    setError("");

    const fetchReservation = async () => {
      const response = await getReservation(reservationCode);

      if (response.status === 200) {
        setReservation(response.data);
      }
    };

    fetchReservation();
  };

  return (
    <div className={styles.container}>
      <h1>Restaurantes</h1>
      <button onClick={() => openModal()} id="search-reservations">
        Procurar uma reserva
      </button>
      <div className={styles.restaurants}>
        {restaurants.length > 0 ? (
          restaurants.map((restaurant) => (
            <div
              key={restaurant.id}
              className={styles.restaurant}
              onClick={() => {
                navigate(`/restaurant/${restaurant.id}`);
              }}
            >
              <h3>{restaurant.name}</h3>
              <p className={styles.restaurantInfo}>
                <span>{restaurant.location}</span>
                <span>{restaurant.capacity} lugares</span>
              </p>
            </div>
          ))
        ) : (
          <div className={styles.noRestaurants}>
            <span>Nenhuma cantina encontrada</span>
          </div>
        )}
      </div>
      {showModal && (
        <div className={styles.modalContainer}>
          <div className={styles.modal}>
            <div className={styles.modalContent}>
              {reservation && !error ? (
                <>
                  <UserReservation
                    reservation={reservation}
                    setReservation={setReservation}
                  />
                </>
              ) : (
                <>
                  <h2>Procurar uma reserva</h2>
                  <div className={styles.modalInput}>
                    <input
                      id="reservation-code-input"
                      type="text"
                      placeholder="Código da reserva"
                      value={reservationCode}
                      onChange={(e) => setReservationCode(e.target.value)}
                    />
                    {error && <span className={styles.error}>{error}</span>}
                    <div className={styles.modalButtons}>
                      <button
                        id="search"
                        onClick={() => handleSubmit()}
                        className={
                          styles.confirmButton + " " + styles.bigButton
                        }
                      >
                        Procurar
                      </button>
                    </div>
                  </div>
                </>
              )}
              <button
                id="close"
                onClick={() => closeModal()}
                className={styles.bigButton}
              >
                Fechar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
