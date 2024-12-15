package com.library;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.dao.BorrowDAO;
import com.library.model.Book;
import com.library.model.Student;
import com.library.model.Borrow;
import com.library.service.BorrowService;
import com.library.util.DbConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BorrowServiceTest {
    private BorrowService borrowService;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;
    private BorrowDAO borrowDAO;
    private Connection connection;

    @BeforeEach
    void setUp() {
        try {
            // Initialiser la connexion
            connection = DbConnection.getConnection();

            // Initialiser les DAOs avec la même connexion
            bookDAO = new BookDAO(connection);
            studentDAO = new StudentDAO(connection);
            borrowDAO = new BorrowDAO(connection);

            // Initialiser le service avec les DAOs
            borrowService = new BorrowService(borrowDAO, bookDAO, studentDAO);

            // Ajouter des données de test
            Student student1 = new Student("Alice");
            student1.setId(1);
            studentDAO.addStudent(student1);

            Student student2 = new Student("Bob");
            student2.setId(2);
            studentDAO.addStudent(student2);

            Book book1 = new Book("Java Programming", "John Doe", "Publisher", 2024);
            book1.setId(1);
            bookDAO.add(book1);

            Book book2 = new Book("Advanced Java", "Jane Doe", "Publisher", 2024);
            book2.setId(2);
            bookDAO.add(book2);

        } catch (Exception e) {
            fail("Erreur lors de l'initialisation des tests : " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            // Nettoyer la base de données
            if (connection != null) {
                connection.createStatement().execute("DELETE FROM borrows");
                connection.createStatement().execute("DELETE FROM books");
                connection.createStatement().execute("DELETE FROM students");
                connection.close();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du nettoyage : " + e.getMessage());
        }
    }

    @Test
    void testBorrowBook() {
        // Test d'emprunt de livre
        Date borrowDate = new Date();
        assertTrue(borrowService.borrowBook(1, 1, borrowDate));

        // Vérifier que l'emprunt a été créé
        Borrow borrow = borrowService.getBorrow(1);
        assertNotNull(borrow);
        assertEquals(1, borrow.getStudent().getId());
        assertEquals(1, borrow.getBook().getId());
        assertNull(borrow.getReturnDate());
    }

    @Test
    void testBorrowBookStudentNotFound() {
        // Test avec un ID d'étudiant inexistant
        Date borrowDate = new Date();
        assertFalse(borrowService.borrowBook(999, 1, borrowDate));
    }

    @Test
    void testBorrowBookNotFound() {
        // Test avec un ID de livre inexistant
        Date borrowDate = new Date();
        assertFalse(borrowService.borrowBook(1, 999, borrowDate));
    }

    @Test
    void testGetAllBorrows() {
        // Créer quelques emprunts
        Date borrowDate = new Date();
        borrowService.borrowBook(1, 1, borrowDate);
        borrowService.borrowBook(2, 2, borrowDate);

        // Vérifier la liste des emprunts
        assertNotNull(borrowService.getAllBorrows());
        assertTrue(borrowService.getAllBorrows().size() >= 2);
    }

    @Test
    void testBorrowExists() {
        // Créer un emprunt
        Date borrowDate = new Date();
        borrowService.borrowBook(1, 1, borrowDate);

        // Vérifier l'existence de l'emprunt
        assertTrue(borrowService.borrowExists(1));
        assertFalse(borrowService.borrowExists(999));
    }
}