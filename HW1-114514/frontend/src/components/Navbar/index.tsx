import { useContext } from "react";
import styles from "./styles.module.css";
import { AppContext } from "@hooks/AppContext";
import { useNavigate } from "react-router-dom";

function Navbar() {
  const { mode, switchMode } = useContext(AppContext);
  const navigate = useNavigate();

  return (
    <nav className={styles.navbar}>
      <span className={styles.appName} 
        onClick={() => {
          navigate("/");
        }}
      >Canteen</span>
      <div className={styles.modeSwitch}>
        {mode}-View <a onClick={switchMode}>Switch</a>
      </div>
    </nav>
  );
}

export default Navbar;
