package tqs.lab6_1;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;

    String title;
    String author;
    LocalDate published;
    public Book(String name, String author, LocalDate published) {
        this.title = name;
        this.author = author;
        this.published = published;
    }


}
