import { getRestaurantById, getRestaurantMenus } from "@api/Restaurant";
import { createMenu } from "@api/Menu";
import { AppContext } from "@hooks/AppContext";
import { Menu } from "@Types/Menu";
import { Restaurant } from "@Types/Restaurant";
import { MenuRequestDTO } from "@Types/MenuRequestDTO";
import { MenuTime } from "@Types/MenuTime";
import { MealType } from "@Types/MealType";
import {
  Dispatch,
  SetStateAction,
  useContext,
  useEffect,
  useState,
} from "react";
import styles from "./styles.module.css";
import { format } from "date-fns";
import { pt } from "date-fns/locale";
import { MealDTO } from "@Types/MealDTO";
import { createReservation } from "@api/Reservation";
import { ReservationRequestDTO } from "@Types/ReservationRequestDTO";

interface MenusProps {
  selectedRestaurant: number;
  setTitle: Dispatch<SetStateAction<string>>;
}

export default function Menus({ selectedRestaurant, setTitle }: MenusProps) {
  const { mode } = useContext(AppContext);
  const [restaurant, setRestaurant] = useState<Restaurant>();
  const [menus, setMenus] = useState<Menu[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showAddMenu, setShowAddMenu] = useState(false);
  const [newMenu, setNewMenu] = useState<MenuRequestDTO>({
    restaurantId: selectedRestaurant,
    date: new Date(),
    options: [],
    time: MenuTime.LUNCH,
  });
  const [mealOptions, setMealOptions] = useState<Array<MealDTO>>([
    {
      description: "sopa de pato",
      type: MealType.SOUP,
      menuId: -1,
    },
    {
      description: "carne de vaca",
      type: MealType.MEAT,
      menuId: -1,
    },
    {
      description: "arroz de marisco",
      type: MealType.FISH,
      menuId: -1,
    },
  ]);

  useEffect(() => {
    const fetchRestaurant = async () => {
      try {
        setLoading(true);
        const response = await getRestaurantById(selectedRestaurant);

        if (response.status === 200) {
          setRestaurant(response.data);
          setTitle(
            `Ementas de ${response.data.name} (${response.data.location})`
          );
        } else {
          setError("Não foi possível carregar os dados do restaurante.");
        }
      } catch (err) {
        setError("Ocorreu um erro ao carregar os dados do restaurante.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurant();
  }, [selectedRestaurant, setTitle]);

  useEffect(() => {
    if (restaurant) {
      const fetchMenus = async () => {
        try {
          setLoading(true);
          const response = await getRestaurantMenus(restaurant.id);

          if (response.status === 200) {
            setMenus(
              response.data.sort((a, b) => {
                if (a.date === b.date) {
                  return a.time === MenuTime.LUNCH ? -1 : 1;
                }
                return a.date < b.date ? -1 : 1;
              })
            );
          } else {
            setError("Não foi possível carregar as ementas.");
          }
        } catch (err) {
          setError("Ocorreu um erro ao carregar as ementas.");
          console.error(err);
        } finally {
          setLoading(false);
        }
      };

      fetchMenus();
    }
  }, [restaurant]);

  const getMealTypeClass = (type: string) => {
    switch (type) {
      case "SOUP":
        return styles.mealTypeSoup;
      case "MEAT":
        return styles.mealTypeMeat;
      case "FISH":
        return styles.mealTypeFish;
      default:
        return "";
    }
  };

  const formatDate = (dateString: string) => {
    try {
      const date = new Date(dateString);
      return format(date, "EEEE, d 'de' MMMM", { locale: pt });
    } catch {
      return dateString;
    }
  };

  const handleAddMenu = async () => {
    try {
      setLoading(true);
      const menuRequest: MenuRequestDTO = { ...newMenu, options: mealOptions };
      const menuResponse = await createMenu(menuRequest);
      if (menuResponse.status === 201) {
        // Refresh menus after adding
        const updatedMenus = await getRestaurantMenus(selectedRestaurant);
        if (updatedMenus.status === 200) {
          setMenus(updatedMenus.data);
        }
        setShowAddMenu(false);
        setMealOptions([]);
      }
    } catch (err) {
      setError("Erro ao adicionar menu");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleMealChange = (
    index: number,
    field: "description" | "type",
    value: string
  ) => {
    const updatedMeals = [...mealOptions];
    if (field === "type") {
      updatedMeals[index] = { ...updatedMeals[index], type: value as MealType };
    } else {
      updatedMeals[index] = { ...updatedMeals[index], description: value };
    }
    setMealOptions(updatedMeals);
  };

  const addMealOption = () => {
    setMealOptions([
      ...mealOptions,
      { description: "", type: MealType.SOUP, menuId: -1 },
    ]);
  };

  const removeMealOption = (index: number) => {
    const updatedMeals = mealOptions.filter((_, i) => i !== index);
    setMealOptions(updatedMeals);
  };


  const reserveMeal = async (menuId: number) => {
    const reservationRequest: ReservationRequestDTO = {
      mealId: mealId
    };

    const response = await createReservation(reservationRequest);
    if (response.status === 201) {
      console.log("Reserva criada com sucesso");
    } else {
      console.error("Erro ao criar reserva");
    }
  };

  if (loading) {
    return (
      <div className={styles.loading}>
        <div className={styles.loadingSpinner}></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.emptyState}>
        <div className={styles.emptyStateIcon}>⚠️</div>
        <div className={styles.emptyStateText}>Erro</div>
        <div className={styles.emptyStateSubtext}>{error}</div>
      </div>
    );
  }

  if (!restaurant) {
    return (
      <div className={styles.emptyState}>
        <div className={styles.emptyStateIcon}>❓</div>
        <div className={styles.emptyStateText}>Restaurante não encontrado</div>
        <div className={styles.emptyStateSubtext}>
          Não foi possível encontrar os dados do restaurante selecionado.
        </div>
      </div>
    );
  }
  return (
    <div className={`${styles.container}`}>
      <div className={styles.header}>
        <div className={styles.restaurantInfo}>
          <div>
            <h1 className={styles.restaurantName}>{restaurant.name}</h1>
            <div className={styles.restaurantLocation}>
              {restaurant.location}
            </div>
          </div>
        </div>
        {mode === "Worker" && (
          <button
            className={styles.addMenuButton}
            onClick={() => setShowAddMenu(true)}
          >
            Adicionar Menu
          </button>
        )}
      </div>

      {showAddMenu && (
        <div className={styles.addMenuForm}>
          <h2>Adicionar Novo Menu</h2>
          <div className={styles.formGroup}>
            <label>Data:</label>
            <input
              type="date"
              value={newMenu.date.toISOString().split("T")[0]}
              onChange={(e) =>
                setNewMenu({ ...newMenu, date: new Date(e.target.value) })
              }
            />
          </div>
          <div className={styles.formGroup}>
            <label>Horário:</label>
            <select
              value={newMenu.time}
              onChange={(e) =>
                setNewMenu({ ...newMenu, time: e.target.value as MenuTime })
              }
            >
              <option value={MenuTime.LUNCH}>Almoço</option>
              <option value={MenuTime.DINNER}>Jantar</option>
            </select>
          </div>

          <div className={styles.mealOptionsSection}>
            <h3>Opções de Refeição</h3>
            {mealOptions.map((meal, index) => (
              <div key={index} className={styles.mealOption}>
                <div className={styles.formGroup}>
                  <label>Tipo:</label>
                  <select
                    value={meal.type}
                    onChange={(e) =>
                      handleMealChange(index, "type", e.target.value)
                    }
                  >
                    <option value={MealType.SOUP}>Sopa</option>
                    <option value={MealType.MEAT}>Carne</option>
                    <option value={MealType.FISH}>Peixe</option>
                  </select>
                </div>
                <div className={styles.formGroup}>
                  <label>Descrição:</label>
                  <input
                    type="text"
                    value={meal.description}
                    onChange={(e) =>
                      handleMealChange(index, "description", e.target.value)
                    }
                    placeholder="Descreva a refeição..."
                  />
                </div>
                <button
                  className={styles.removeMealButton}
                  onClick={() => removeMealOption(index)}
                >
                  Remover
                </button>
              </div>
            ))}
            <button className={styles.addMealButton} onClick={addMealOption}>
              Adicionar Opção de Refeição
            </button>
          </div>

          <div className={styles.formActions}>
            <button onClick={handleAddMenu}>Confirmar</button>
            <button onClick={() => setShowAddMenu(false)}>Cancelar</button>
          </div>
        </div>
      )}

      {menus.length === 0 ? (
        <div className={styles.emptyState}>
          <div className={styles.emptyStateIcon}>🍽️</div>
          <div className={styles.emptyStateText}>Nenhuma ementa disponível</div>
          <div className={styles.emptyStateSubtext}>
            Não há ementas cadastradas para este restaurante no momento.
          </div>
        </div>
      ) : (
        <div className={styles.menusContainer}>
          <div className={styles.menusGrid}>
            {menus.map((menu) => (
              <>
                <div key={menu.id} className={styles.menuCard}>
                  <div className={styles.menuHeader}>
                    <div className={styles.menuDate}>
                      {formatDate(menu.date)}
                    </div>
                    <div className={styles.menuTime}>
                      {menu.time === MenuTime.LUNCH ? "Almoço" : "Jantar"}
                    </div>
                  </div>
                  <div className={styles.menuContent}>
                    <div className={styles.menuCapacity}>
                      <span className={styles.capacityIcon}>👥</span>
                      <span>Capacidade: {menu.capacity} pessoas</span>
                    </div>
                    <div className={styles.mealList}>
                      {menu.options.map((meal) => (
                        <>
                          <div key={meal.id} className={styles.mealItem}>
                            <div
                              className={`${styles.mealType} ${getMealTypeClass(
                                meal.type
                              )}`}
                            >
                              {meal.type === "SOUP"
                                ? "Sopa"
                                : meal.type === "MEAT"
                                ? "Carne"
                                : "Peixe"}
                            </div>
                            <div className={styles.mealDescription}>
                              {meal.description}
                            </div>
                          </div>
                        </>
                      ))}
                    </div>
                  </div>
                <button className={styles.addMealButton} onClick={reserveMeal}>
                  Reservar refeição
                </button>
                </div>
              </>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
