import { createContext } from "react";

enum Mode {
  User = "User",
  Worker = "Worker",
}
interface Context {
  mode: Mode;
  switchMode: () => void;
}
const AppContext = createContext<Context>({
  mode: Mode.User,
  switchMode: () => {},
} as Context);

export { Mode, AppContext };
