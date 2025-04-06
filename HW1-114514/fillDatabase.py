from datetime import datetime, timedelta
import random
import psycopg2
from dotenv import load_dotenv
import os

load_dotenv()


def fill_database():
    # Create a connection to the database
    conn = psycopg2.connect(
        host="localhost",
        database=os.getenv("DB_NAME"),
        user=os.getenv("DB_USERNAME"),
        password=os.getenv("DB_PASSWORD"),
    )
    cursor = conn.cursor()

    cursor.execute("SELECT * FROM restaurant")
    restaurants = cursor.fetchall()

    meatPlates = [
        "Bife de porco",
        "Vaca Grelhada",
        "Carne de Porco",
        "Frango Grelhado",
    ]
    fishPlates = [
        "Salmão",
        "Tilápia",
        "Peixe Grelhado",
        "Peixe Grelhado",
    ]
    soupPlates = [
        "Sopa de cebola",
        "Sopa de legumes",
        "Sopa de tomate",
        "Sopa de cebola",
    ]

    meals = {
        "MEAT": meatPlates,
        "FISH": fishPlates,
        "SOUP": soupPlates,
    }

    # For each restaurant to the next 10 days
    #   creates a menu for each day and time
    for restaurant in restaurants:
        for days in range(0, 10):
            for time in ["LUNCH", "DINNER"]:
                # Create the menu
                cursor.execute(
                    "INSERT INTO menu (restaurant_id, date, time, capacity) VALUES (%s, %s, %s, %s) RETURNING id",
                    (
                        restaurant[0],
                        datetime.now() + timedelta(days=days),
                        time,
                        100,
                    ),
                )

                menu = cursor.fetchone()
                # Add meals to the menu
                for mealType in ["MEAT", "FISH", "SOUP"]:
                    mealPlate = random.choice(meals[mealType])
                    cursor.execute(
                        "INSERT INTO meal (menu_id, description, type) VALUES (%s, %s, %s)",
                        (menu[0], mealPlate, mealType),
                    )

    conn.commit()
    cursor.close()
    conn.close()


fill_database()
