package com.library.service;
import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.util.DbConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private BookDAO bookDAO;
    private Connection connection;

    public BookService() {
        try {
            this.connection = DbConnection.getConnection();
            if (this.connection == null) {
                throw new SQLException("Impossible d'établir la connexion à la base de données");
            }
            this.bookDAO = new BookDAO(this.connection);
            if (this.bookDAO == null) {
                throw new IllegalStateException("BookDAO n'a pas été initialisé correctement");
            }
        } catch (Exception e) {
            logger.error("Erreur critique lors de l'initialisation du service de livres : {}", e.getMessage());
            throw new RuntimeException("Impossible d'initialiser le service de livres", e);
        }
    }

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void addBook(Book book) {
        bookDAO.add(book);
    }

    public void displayBooks() {
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            logger.info("Book: {}", book);
        }
    }

    public Book findBookById(int id) {
        return bookDAO.getBookById(id);
    }

    public void deleteBook(int id) {
        bookDAO.delete(id);
    }

    public void updateBook(Book book) {
        bookDAO.update(book);
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                logger.error("Erreur lors de la fermeture de la connexion : {}", e.getMessage());
            }
        }
    }
}