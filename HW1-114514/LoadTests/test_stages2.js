import { check } from "k6";
import http from "k6/http";

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";

export const options = {
  stages: [
    // ramp up from 0 to 20 VUs over the next 30 seconds
    { duration: "30s", target: 120 },
    // run 120 VUs over the next 30 seconds
    { duration: "30s", target: 120 },
    // ramp down from 120 to 0 VUs over the next 30 seconds
    { duration: "30s", target: 0 },
  ],
  thresholds: {
    http_req_failed: ["rate<0.01"], // http errors should be less than 1%
    http_req_duration: ["p(95)<100"], // 95% of requests should be below 100ms
    checks: ["rate>0.98"], // 98% dos checks têm de passar
  },
};

// Test data
const RESTAURANT_ID = __ENV.RESTAURANT_ID || "1";
const RESERVATION_ID = __ENV.RESERVATION_ID || "ABC12345";

export default function () {
  // Restaurant endpoints
  const restaurantsRes = http.get(`${BASE_URL}/api/v1/restaurants`);
  check(restaurantsRes, {
    "restaurants status is 200": (r) => r.status === 200,
    "restaurants response time < 500ms": (r) => r.timings.duration < 500,
  });

  const restaurantRes = http.get(
    `${BASE_URL}/api/v1/restaurants/${RESTAURANT_ID}`,
  );
  check(restaurantRes, {
    "restaurant by id status is 200": (r) => r.status === 200,
    "restaurant by id response time < 500ms": (r) => r.timings.duration < 500,
  });

  const menusRes = http.get(
    `${BASE_URL}/api/v1/restaurants/${RESTAURANT_ID}/menus`,
  );
  check(menusRes, {
    "restaurant menus status is 200": (r) => r.status === 200,
    "restaurant menus response time < 500ms": (r) => r.timings.duration < 500,
  });

  // Reservation endpoints
  const reservationsRes = http.get(
    `${BASE_URL}/api/v1/reservations?restaurantId=${RESTAURANT_ID}`,
  );
  check(reservationsRes, {
    "reservations status is 200": (r) => r.status === 200,
    "reservations response time < 500ms": (r) => r.timings.duration < 500,
  });

  const reservationRes = http.get(
    `${BASE_URL}/api/v1/reservations/${RESERVATION_ID}`,
  );
  check(reservationRes, {
    "reservation by id status is 200": (r) => r.status === 200,
    "reservation by id response time < 500ms": (r) => r.timings.duration < 500,
  });

  // Weather endpoints
  const weatherRes = http.get(`${BASE_URL}/api/v1/weather/forecast`);
  check(weatherRes, {
    "weather forecast status is 200": (r) => r.status === 200,
    "weather forecast response time < 500ms": (r) => r.timings.duration < 500,
  });

  const cacheStatsRes = http.get(`${BASE_URL}/api/v1/weather/cache/stats`);
  check(cacheStatsRes, {
    "cache stats status is 200": (r) => r.status === 200,
    "cache stats response time < 500ms": (r) => r.timings.duration < 500,
  });
}
