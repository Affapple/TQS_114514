import { useContext } from "react";
import styles from "./styles.module.css";
import { AppContext } from "@hooks/AppContext";

function Navbar() {
  const { mode, switchMode } = useContext(AppContext);

  return (
    <nav className={styles.navbar}>
      <span className={styles.appName}>Canteen</span>
      <div className={styles.modeSwitch}>
        {mode}-View <a onClick={switchMode}>Switch</a>
      </div>
    </nav>
  );
}

export default Navbar;
