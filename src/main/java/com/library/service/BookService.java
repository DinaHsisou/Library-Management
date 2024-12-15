package com.library.service;
import com.library.dao.BookDAO; // Importation de BookDAO
import com.library.model.Book;   // Importation de Book
import com.library.util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class BookService {
    private BookDAO bookDAO;
    private Connection connection;

    // Constructeur par défaut avec création de connexion
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
            System.err.println("Erreur critique lors de l'initialisation du service de livres : " + e.getMessage());
            // Vous pourriez vouloir relancer l'exception ici
            throw new RuntimeException("Impossible d'initialiser le service de livres", e);
        }
    }
    // Constructeur avec DAO pour les tests
    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    // Ajouter un livre
    public void addBook(Book book) {
        try {
            bookDAO.add(book);
            System.out.println("Livre ajouté avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }
    // Afficher tous les livres
    public void displayBooks() {
        try {
            List<Book> books = bookDAO.getAllBooks();
            if (books.isEmpty()) {
                System.out.println("Aucun livre trouvé.");
                return;
            }

            System.out.println("\n=== Liste des Livres ===");
            System.out.println("------------------------");
            for (Book book : books) {
                System.out.println(book);
            }
            System.out.println("------------------------");
            System.out.println("Total: " + books.size() + " livre(s)\n");

        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des livres : " + e.getMessage());
        }
    }

    // Trouver un livre par ID
    public Book findBookById(int id) {
        try {
            Book book = bookDAO.getBookById(id);
            if (book == null) {
                System.out.println("Aucun livre trouvé avec l'ID : " + id);
            }
            return book;
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche du livre : " + e.getMessage());
            return null;
        }
    }
    // Supprimer un livre par ID

    public void deleteBook(int id) {
        try {
            bookDAO.delete(id);
            System.out.println("Livre supprimé avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du livre : " + e.getMessage());
        }
    }
    // Mise à jour des informations d'un livre
    public void updateBook(Book book) {
        try {
            bookDAO.update(book);
            System.out.println("Livre mis à jour avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du livre : " + e.getMessage());
        }
    }
    // Fermer la connexion
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}
