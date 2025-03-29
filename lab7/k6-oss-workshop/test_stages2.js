import { check } from "k6";
import http from "k6/http";

const BASE_URL = __ENV.BASE_URL || "http://localhost:3333";

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
    http_req_duration: ["p(95)<1100"], // 95% of requests should be below 1100ms
    checks: ["rate>0.98"], // 98% dos checks têm de passar
  },
};

export default function() {
  let restrictions = {
    maxCaloriesPerSlice: 500,
    mustBeVegetarian: false,
    excludedIngredients: ["pepperoni"],
    excludedTools: ["knife"],
    maxNumberOfToppings: 6,
    minNumberOfToppings: 2,
  };
  let res = http.post(`${BASE_URL}/api/pizza`, JSON.stringify(restrictions), {
    headers: {
      "Content-Type": "application/json",
      "X-User-ID": 23423,
      Authorization: "token abcdef0123456789",
    },
  });

  check(res, {
    "is status 200": (r) => r.status === 200,
    "body size is lower than 1k": (r) => r.body.length <= 1000,
  });
}
