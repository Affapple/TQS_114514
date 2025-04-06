package tqs.homework.canteen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.homework.canteen.entities.Meal;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    boolean existsByMenu_idAndId(Long menuId, Long mealId);
}
