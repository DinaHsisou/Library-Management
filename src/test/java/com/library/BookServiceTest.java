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
    void testAddBook() {
        // Créer un livre de test en utilisant le constructeur disponible
        Book book = new Book("Java Programming", "John Doe", "O'Reilly", 2024);
        book.setIsbn("123-456"); // Setter pour les champs additionnels
        book.setPublishedYear(2024);

        // Ajouter le livre
        bookService.addBook(book);

        // Vérifier que le livre a été ajouté
        Book foundBook = bookService.findBookById(book.getId());
        assertNotNull(foundBook, "Le livre devrait être trouvé");
        assertEquals("Java Programming", foundBook.getTitle(), "Le titre devrait correspondre");
        assertEquals("John Doe", foundBook.getAuthor(), "L'auteur devrait correspondre");
        assertEquals("O'Reilly", foundBook.getPublisher(), "L'éditeur devrait correspondre");
    }

    @Test
    void testFindBookById() {
        // Créer et ajouter un livre de test
        Book book = new Book("Java Programming", "John Doe", "O'Reilly", 2024);
        book.setIsbn("123-456");
        book.setPublishedYear(2024);
        bookService.addBook(book);

        // Rechercher le livre
        Book foundBook = bookService.findBookById(book.getId());

        // Vérifications
        assertNotNull(foundBook, "Le livre devrait être trouvé");
        assertEquals("Java Programming", foundBook.getTitle(), "Le titre devrait correspondre");
        assertEquals("John Doe", foundBook.getAuthor(), "L'auteur devrait correspondre");
    }

    @Test
    void testDeleteBook() {
        // Créer et ajouter un livre de test
        Book book = new Book("Java Programming", "John Doe", "O'Reilly", 2024);
        book.setIsbn("123-456");
        bookService.addBook(book);

        // Supprimer le livre
        bookService.deleteBook(book.getId());

        // Vérifier que le livre a été supprimé
        Book deletedBook = bookService.findBookById(book.getId());
        assertNull(deletedBook, "Le livre devrait être supprimé");
    }

    @Test
    void testUpdateBook() {
        // Créer et ajouter un livre de test
        Book book = new Book("Java Programming", "John Doe", "O'Reilly", 2024);
        book.setIsbn("123-456");
        bookService.addBook(book);

        // Modifier le livre
        book.setTitle("Advanced Java");
        book.setAuthor("Jane Doe");
        book.setPublisher("Manning");
        bookService.updateBook(book);

        // Vérifier les modifications
        Book updatedBook = bookService.findBookById(book.getId());
        assertNotNull(updatedBook, "Le livre devrait être trouvé");
        assertEquals("Advanced Java", updatedBook.getTitle(), "Le titre devrait être mis à jour");
        assertEquals("Jane Doe", updatedBook.getAuthor(), "L'auteur devrait être mis à jour");
        assertEquals("Manning", updatedBook.getPublisher(), "L'éditeur devrait être mis à jour");
    }
}