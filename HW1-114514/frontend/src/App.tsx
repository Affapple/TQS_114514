import { BrowserRouter, Route, Routes } from "react-router-dom";
import { AppContext, Mode } from "@hooks/AppContext";
import MainPage from "@pages/MainPage";
import { useState } from "react";

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
        <Routes>
          <Route path="/" element={<MainPage />} />
        </Routes>
      </BrowserRouter>
    </AppContext.Provider>
  );
}

export default App;
