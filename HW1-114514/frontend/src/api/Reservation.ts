import apiClient from "@api/ApiClient";
import { MenuTime } from "@Types/MenuTime";
import { ReservationRequestDTO } from "@Types/ReservationRequestDTO";

async function getAllReservations(restaurantId: number, date?: Date, time?: MenuTime) {
  const response = apiClient.get("/reservations", {
    params: {
      restaurantId,
      date,
      time,
    },
  });
  return response;
}

async function createReservation(reservationRequest: ReservationRequestDTO) {
  const response = apiClient.post("/reservations", reservationRequest);
  return response;
}

async function getReservation(reservationId: string) {
  const response = apiClient.get(`/reservations/${reservationId}`);
  return response;
}

async function cancelReservation(reservationId: string) {
  const response = apiClient.delete(`/reservations/${reservationId}`);
  return response;
}

async function checkinReservation(reservationId: string) {
  const response = apiClient.put(`/reservations/${reservationId}`);
  return response;
}

export { getAllReservations, createReservation, getReservation, cancelReservation, checkinReservation };
