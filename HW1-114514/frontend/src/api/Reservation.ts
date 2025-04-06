import apiClient from "@api/ApiClient";
import { MenuTime } from "@Types/MenuTime";
import { ReservationRequestDTO } from "@Types/ReservationRequestDTO";

function getAllReservations(restaurantId: number, date?: Date, time?: MenuTime) {
  const response = apiClient.get("/reservations", {
    params: {
      restaurantId,
      date,
      time,
    },
  });
  return response;
}

function createReservation(reservationRequest: ReservationRequestDTO) {
  const response = apiClient.post("/reservations", reservationRequest);
  return response;
}

function getReservation(reservationId: number) {
  const response = apiClient.get(`/reservations/${reservationId}`);
  return response;
}

function cancelReservation(reservationId: number) {
  const response = apiClient.delete(`/reservations/${reservationId}`);
  return response;
}

function checkinReservation(reservationId: number) {
  const response = apiClient.put(`/reservations/${reservationId}`);
  return response;
}

export { getAllReservations, createReservation, getReservation, cancelReservation, checkinReservation };
