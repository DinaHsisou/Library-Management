package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Student;
import com.library.dao.BorrowDAO;
import com.library.model.Borrow;
import com.library.util.DbConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BorrowService {
    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(BorrowService.class);

    private BorrowDAO borrowDAO;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;
    private Connection connection;

    // Default constructor
    public BorrowService() {
        try {
            this.connection = DbConnection.getConnection();
            this.studentDAO = new StudentDAO(this.connection);
            this.bookDAO = new BookDAO(this.connection);
            this.borrowDAO = new BorrowDAO(this.connection);
            logger.info("BorrowService initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing BorrowService", e);
        }
    }

    // Constructor with dependency injection
    public BorrowService(BorrowDAO borrowDAO, BookDAO bookDAO, StudentDAO studentDAO) {
        this.borrowDAO = borrowDAO;
        this.bookDAO = bookDAO;
        this.studentDAO = studentDAO;
        logger.debug("BorrowService created with injected DAOs");
    }

    // Method to borrow a book
    public boolean borrowBook(int studentId, int bookId, Date borrowDate) {
        logger.info("Attempting to borrow book - StudentID: {}, BookID: {}", studentId, bookId);

        // Check if student exists
        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            logger.warn("Student not found - StudentID: {}", studentId);
            return false;
        }

        // Check if book exists
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            logger.warn("Book not found - BookID: {}", bookId);
            return false;
        }

        // Create and save borrow
        Borrow borrow = new Borrow(0, student, book, borrowDate, null);
        try {
            borrowDAO.save(borrow);
            logger.info("Book borrowed successfully - StudentID: {}, BookID: {}", studentId, bookId);
            return true;
        } catch (Exception e) {
            logger.error("Error borrowing book - StudentID: {}, BookID: {}", studentId, bookId, e);
            return false;
        }
    }

    // Get borrow by ID
    public Borrow getBorrow(int borrowId) {
        logger.debug("Retrieving borrow with ID: {}", borrowId);
        Borrow borrow = borrowDAO.getBorrowById(borrowId);
        if (borrow == null) {
            logger.warn("No borrow found with ID: {}", borrowId);
        }
        return borrow;
    }

    // Display borrows
    public void displayBorrows() {
        List<Borrow> borrows = borrowDAO.getAllBorrows();
        if (borrows.isEmpty()) {
            logger.info("No borrows recorded");
            return;
        }

        logger.info("Displaying {} borrows", borrows.size());
        for (Borrow borrow : borrows) {
            String borrowDate = new SimpleDateFormat("dd/MM/yyyy").format(borrow.getBorrowDate());
            String returnDate = borrow.getReturnDate() != null
                    ? new SimpleDateFormat("dd/MM/yyyy").format(borrow.getReturnDate())
                    : "Not returned";

            logger.info("Borrow Details - ID: {}, Student: {}, Book: {}, Borrowed: {}, Returned: {}",
                    borrow.getId(),
                    borrow.getStudent().getName(),
                    borrow.getBook().getTitle(),
                    borrowDate,
                    returnDate
            );
        }
    }

    // Get all borrows
    public List<Borrow> getAllBorrows() {
        try {
            logger.debug("Retrieving all borrows");
            return borrowDAO.getAllBorrows();
        } catch (Exception e) {
            logger.error("Error retrieving borrows", e);
            return null;
        }
    }

    // Close connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed successfully");
            } catch (Exception e) {
                logger.error("Error closing database connection", e);
            }
        }
    }

    // Check if borrow exists
    public boolean borrowExists(int borrowId) {
        boolean exists = borrowDAO.getBorrowById(borrowId) != null;
        logger.debug("Checking borrow existence - ID: {}, Exists: {}", borrowId, exists);
        return exists;
    }

    // Return book
    public void returnBook(int borrowId) {
        try {
            logger.info("Attempting to return book - BorrowID: {}", borrowId);
            borrowDAO.returnBook(borrowId);
            logger.info("Book returned successfully - BorrowID: {}", borrowId);
        } catch (Exception e) {
            logger.error("Error returning book - BorrowID: {}", borrowId, e);
        }
    }
}