package com.varukha.onlinebookstore;

import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTitle("The Lord of the Rings");
                book.setAuthor("J.R.R.Tolkien");
                book.setIsbn("9780261103252");
                book.setPrice(BigDecimal.valueOf(800));
                book.setDescription("The title refers to the story's "
                        + "main antagonist, the Dark Lord Sauron, "
                        + "who, in an earlier age, created the One "
                        + "Ring to rule the other Rings of Power given "
                        + "to Men, Dwarves, and Elves, in his campaign "
                        + "to conquer all of Middle-earth");

                book.setCoverImage("J.R.R. Tolkien’s Personal Book Cover Designs "
                        + "for The Lord of the Rings Trilogy");
                bookService.save(book);
                System.out.println(bookService.findAll());
            }
        };
    }
}
