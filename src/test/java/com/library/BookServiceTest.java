package com.library;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.service.BookService;
import com.library.util.DbConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookService bookService;
    private BookDAO bookDAO;
    private Connection connection;

    @BeforeEach
    void setUp() {
        try {
            connection = DbConnection.getConnection();
            bookDAO = new BookDAO(connection);
            bookService = new BookService(bookDAO);
        } catch (Exception e) {
            fail("Erreur lors de l'initialisation des tests : " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            if (connection != null) {
                connection.createStatement().execute("DELETE FROM books");
                connection.close();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du nettoyage : " + e.getMessage());
        }
    }

    @Test
    void testBookLifecycle() {
        // 1. Créer et ajouter un livre
        Book book = new Book(
                "Java Programming",
                "John Doe",
                "O'Reilly",
                2024,
                "123-456"
        );
        bookService.addBook(book);
        assertNotEquals(0, book.getId(), "L'ID devrait être généré après l'ajout");

        // 2. Rechercher le livre par ID
        Book foundBook = bookService.findBookById(book.getId());
        assertNotNull(foundBook, "Le livre devrait être trouvé après l'ajout");
        assertEquals("Java Programming", foundBook.getTitle(), "Le titre devrait correspondre");
        assertEquals("John Doe", foundBook.getAuthor(), "L'auteur devrait correspondre");

        // 3. Mettre à jour le livre
        book.setTitle("Advanced Java");
        book.setAuthor("Jane Doe");
        bookService.updateBook(book);

        // 4. Vérifier la mise à jour
        Book updatedBook = bookService.findBookById(book.getId());
        assertNotNull(updatedBook, "Le livre devrait être trouvé après la mise à jour");
        assertEquals("Advanced Java", updatedBook.getTitle(), "Le titre devrait être mis à jour");
        assertEquals("Jane Doe", updatedBook.getAuthor(), "L'auteur devrait être mis à jour");

        // 5. Supprimer le livre
        bookService.deleteBook(book.getId());

        // 6. Vérifier la suppression
        Book deletedBook = bookService.findBookById(book.getId());
        assertNull(deletedBook, "Le livre devrait être supprimé");
    }
}