import { BrowserRouter, Route, Routes } from "react-router-dom";
import { AppContext, Mode } from "@hooks/AppContext";
import { useState } from "react";
import Navbar from "@components/Navbar";
import Menus from "@components/Menus";
import Restaurants from "@components/Restaurants";
import styles from "./styles.module.css";
import Reservations from "@components/Reservations";
import Weather from "@components/Weather";

function NotFound() {
  return <div>404 Not Found</div>;
}

function App() {
  const [mode, setMode] = useState<Mode>(Mode.User);

  const switchMode = () => {
    setMode(() => (mode === Mode.User ? Mode.Worker : Mode.User));
  };

  return (
    <AppContext.Provider
      value={{
        mode: mode,
        switchMode: switchMode,
      }}
    >
      <BrowserRouter>
        <Navbar />
        <Weather />
        <main>
          <div className={styles.mainContent}>
            <Routes>
              <Route path="/" element={<Restaurants />} />
              <Route path="/restaurant/:id" element={<Menus />} />
              <Route
                path="/restaurant/:id/reservations"
                element={<Reservations />}
              />
              <Route path="*" element={<NotFound />} />
            </Routes>
          </div>
        </main>
      </BrowserRouter>
    </AppContext.Provider>
  );
}

export default App;
