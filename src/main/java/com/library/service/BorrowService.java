
package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Student;
import com.library.dao.BorrowDAO;
import com.library.model.Borrow;
import com.library.util.DbConnection;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BorrowService {

    private BorrowDAO borrowDAO;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;
    private Connection connection;

    // Constructeur par défaut
    public BorrowService() {
        try {
            this.connection = DbConnection.getConnection();
            this.studentDAO = new StudentDAO(this.connection);
            this.bookDAO = new BookDAO(this.connection);
            this.borrowDAO = new BorrowDAO(this.connection);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du service d'emprunts : " + e.getMessage());
        }
    }

    // Constructeur avec injection des DAOs nécessaires
    public BorrowService(BorrowDAO borrowDAO, BookDAO bookDAO, StudentDAO studentDAO) {
        this.borrowDAO = borrowDAO;
        this.bookDAO = bookDAO;
        this.studentDAO = studentDAO;
    }

    // Méthode pour emprunter un livre
    // Emprunter un livre
    public boolean borrowBook(int studentId, int bookId, Date borrowDate) {
        // Vérifier que l'étudiant existe
        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            System.out.println("Étudiant non trouvé");
            return false;
        }

        // Vérifier que le livre existe
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            System.out.println("Livre non trouvé");
            return false;
        }

        // Créer et sauvegarder l'emprunt
        Borrow borrow = new Borrow(0, student, book, borrowDate, null);
        try {
            borrowDAO.save(borrow);
            System.out.println("Livre emprunté avec succès !");
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'emprunt du livre : " + e.getMessage());
            return false;
        }
    }

    // Obtenir un emprunt par son ID
    public Borrow getBorrow(int borrowId) {
        return borrowDAO.getBorrowById(borrowId);
    }



    // Afficher les emprunts (méthode fictive, à adapter)
    public void displayBorrows() {
        List<Borrow> borrows = borrowDAO.getAllBorrows();
        if (borrows.isEmpty()) {
            System.out.println("Aucun emprunt enregistré");
            return;
        }

        System.out.println("\n=== Liste des emprunts ===");
        System.out.println("------------------------");
        for (Borrow borrow : borrows) {
            // Formater la date d'emprunt
            String borrowDate = new SimpleDateFormat("dd/MM/yyyy").format(borrow.getBorrowDate());

            // Gérer la date de retour
            String returnDate = "Non retourné";
            if (borrow.getReturnDate() != null) {
                returnDate = new SimpleDateFormat("dd/MM/yyyy").format(borrow.getReturnDate());
            }

            System.out.printf("ID: %d | Étudiant: %s | Livre: %s | Emprunté le: %s | Retourné le: %s%n",
                    borrow.getId(),
                    borrow.getStudent().getName(),
                    borrow.getBook().getTitle(),
                    borrowDate,
                    returnDate
            );
        }
        System.out.println("------------------------");
        System.out.println("Total: " + borrows.size() + " emprunt(s)\n");
    }
    // Obtenir la liste de tous les emprunts
    public List<Borrow> getAllBorrows() {
        try {
            return borrowDAO.getAllBorrows();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des emprunts : " + e.getMessage());
            return null;
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

    // Vérifier si un emprunt existe
    public boolean borrowExists(int borrowId) {
        return borrowDAO.getBorrowById(borrowId) != null;
    }

    public void returnBook(int borrowId) {
        try {
            borrowDAO.returnBook(borrowId);
        } catch (Exception e) {
            System.err.println("Erreur lors du retour du livre : " + e.getMessage());
        }
    }
}
