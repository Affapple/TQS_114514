import Navbar from "@components/Navbar";
import Weather from "@components/Weather";
import styles from "./styles.module.css";
import { useState } from "react";
import Content from "@components/Content";
import Restaurants from "@components/Restaurants";
import Menus from "@components/Menus";

function MainPage() {
  const [selectedRestaurant, setSelectedRestaurant] = useState<number>(-1);
  const [title, setTitle] = useState<string>("");
  return (
    <>
      <Navbar />
      <Weather />
      <main>
        <div className={styles.mainContent}>
          <Content 
            title={title}
            showingRestaurants={selectedRestaurant === -1}
            clearSelection={() => setSelectedRestaurant(-1)}
          >
            {selectedRestaurant === -1 ? (
              <Restaurants
                setTitle={setTitle}
                setSelectedRestaurant={setSelectedRestaurant}
              />
            ) : (
              <Menus
                selectedRestaurant={selectedRestaurant}
                setTitle={setTitle}
              />
            )}
          </Content>
        </div>
      </main>
    </>
  );
}

export default MainPage;
