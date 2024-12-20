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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BorrowServiceTest {
    private BorrowService borrowService;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;
    private BorrowDAO borrowDAO;
    private Connection connection;
    private Student testStudent;
    private Book testBook;

    @BeforeEach
    void setUp() {
        try {
            connection = DbConnection.getConnection();
            bookDAO = new BookDAO(connection);
            studentDAO = new StudentDAO(connection);
            borrowDAO = new BorrowDAO(connection);
            borrowService = new BorrowService(borrowDAO, bookDAO, studentDAO);

            connection.createStatement().execute("DELETE FROM borrows");
            connection.createStatement().execute("DELETE FROM books");
            connection.createStatement().execute("DELETE FROM students");

            // Créer l'étudiant test
            testStudent = new Student("Alice");
            studentDAO.addStudent(testStudent);
            // Récupérer l'étudiant pour avoir son ID généré
            List<Student> students = studentDAO.getAllStudents();
            testStudent = students.get(0);

            // Créer le livre test avec ISBN unique
            testBook = new Book(
                    "Java Programming",
                    "John Doe",
                    "O'Reilly",
                    2024,
                    "111"
            );
            bookDAO.add(testBook);
            // Récupérer le livre pour avoir son ID généré
            List<Book> books = bookDAO.getAllBooks();
            testBook = books.get(0);

        } catch (Exception e) {
            fail("Erreur lors de l'initialisation des tests : " + e.getMessage());
        }
    }

    @Test
    void testBorrowLifecycle() {
        // Debug: afficher les IDs
        System.out.println("Student ID: " + testStudent.getId());
        System.out.println("Book ID: " + testBook.getId());

        // 1. Emprunter un livre
        Date borrowDate = new Date();
        boolean result = borrowService.borrowBook(testStudent.getId(), testBook.getId(), borrowDate);
        assertTrue(result, "L'emprunt devrait réussir");

        // 2. Vérifier l'emprunt
        List<Borrow> borrows = borrowService.getAllBorrows();
        assertFalse(borrows.isEmpty(), "Des emprunts devraient exister");
        Borrow borrow = borrows.get(0);

        assertNotNull(borrow, "L'emprunt devrait exister");
        assertEquals(testStudent.getId(), borrow.getStudent().getId(), "ID étudiant incorrect");
        assertEquals(testBook.getId(), borrow.getBook().getId(), "ID livre incorrect");
        assertNull(borrow.getReturnDate(), "Date de retour devrait être null");

        // 3. Retourner le livre
        borrowService.returnBook(borrow.getId());

        // 4. Vérifier le retour
        Borrow returnedBorrow = borrowService.getBorrow(borrow.getId());
        assertNotNull(returnedBorrow, "L'emprunt devrait toujours exister");
        assertNotNull(returnedBorrow.getReturnDate(), "La date de retour devrait être définie");
    }

    @AfterEach
    void tearDown() {
        try {
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
}