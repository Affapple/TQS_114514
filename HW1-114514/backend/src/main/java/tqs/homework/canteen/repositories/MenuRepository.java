package tqs.homework.canteen.repositories;

import tqs.homework.canteen.EnumTypes.MenuTime;
import tqs.homework.canteen.EnumTypes.MealType;
import tqs.homework.canteen.entities.Menu;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    public List<Menu> findByRestaurant_idAndDateBetween(Long restaurantId, LocalDate from, LocalDate to);
    public List<Menu> findByRestaurant_id(Long restaurantId);
    
    @Query("""
        SELECT EXISTS (
            SELECT 1
            FROM Menu as m JOIN m.options as meals 
            WHERE m.restaurant.id = :restaurantId 
            AND   m.date = :date 
            AND   m.time = :time 
            AND   meals.type = :type
        )
    """)
    public boolean existsMealOfThisType(Long restaurantId, LocalDate date, MenuTime time, MealType type);

    @Query("""
        SELECT EXISTS (
            SELECT 1
            FROM Menu as m
            WHERE m.restaurant.id = :restaurantId 
            AND   m.date = :date 
            AND   m.time = :time 
        )
    """)
    public boolean existsMealOfThisType(Long restaurantId, LocalDate date, MenuTime time);

    @Query("""
        SELECT EXISTS (
            SELECT 1
            FROM Menu as m
            WHERE m.restaurant.id = :restaurantId 
            AND   m.date >= :date
        )
    """)
    public boolean existsByRestaurant_idAndDateFrom(Long restaurantId, LocalDate date);
}
