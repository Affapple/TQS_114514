package tqs.lab6_1;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class V2_Lab61ApplicationTests {
    @Autowired
    private BookRepository repo;

    @Test
    void whenAddBook_thenBookIsAdded() {
        String title = "Harry potter e os talismas da morte parte 2";
        Book harryPotterBook = new Book(title, "JK. Rowling", LocalDate.of(2007, 12, 10));
        repo.save(harryPotterBook);

        assertEquals(repo.findByTitle(title).size(), 1);
        assertTrue(
            repo.findByTitle(title)
            .stream()
            .anyMatch((book)-> book.getTitle().equals(title))
        );
    }
 
    @Test
    void whenRetrieveBookById_thenCorrectBookIsReturned() {
        List<Book> books = repo.findAll();
        System.out.println("\n\n" + books.size());
        Book lastBook = books.getLast();
        Optional<Book> retrievedBook = repo.findById(lastBook.getId());

        assertTrue(retrievedBook.isPresent());
    }

    @Test
    void whenUpdateBook_thenRetrieveUpdatedBook() {
        List<Book> searchResult = repo.findByTitle("Book 3");
        Book book3 = searchResult.getFirst();

        String newTitle = "Harry Potter e a Câmara Secreta";
        book3.setTitle(newTitle);
        repo.save(book3);

        Book updatedBook = repo.findById(book3.getId()).orElseThrow();
        assertEquals(newTitle, updatedBook.getTitle());
    }

    @Test
    void whenDeleteBook_thenBookIsRemoved() {
        List<Book> searchResult = repo.findByTitle("Book 2");
        Book book3 = searchResult.getFirst();

        repo.deleteById(book3.getId());
        Optional<Book> deletedBook = repo.findById(book3.getId());
        assertFalse(deletedBook.isPresent());
    }
}
