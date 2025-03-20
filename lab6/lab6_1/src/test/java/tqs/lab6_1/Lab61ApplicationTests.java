package tqs.lab6_1;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;


@Import(TestcontainersConfiguration.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Lab61ApplicationTests {
    @Autowired
    private BookRepository repo;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }


	@Test
	void contextLoads() {
        Book book = new Book("Harry Potter e a pedra filosofal", "TheySeeMe Rowling", LocalDate.of(2002, 12, 10));
        repo.save(book);
	}

    @Test
    void whenAddBook_thenBookIsAdded() {
        String title = "Harry Potter e a pedra filosofal";
        Book harryPotterBook = new Book(title, "TheySeeMe Rowling", LocalDate.of(2002, 12, 10));
        repo.save(harryPotterBook);

        assertEquals(repo.findByTitle(title).size(), 1);
        assertTrue(
            repo.findByTitle(title)
            .stream()
            .anyMatch((book)-> book.getTitle().equals(title))
        );
    }
}
