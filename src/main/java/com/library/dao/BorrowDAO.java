
package com.library.dao;

import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    private final Connection connection;
    private final StudentDAO studentDAO;
    private final BookDAO bookDAO;

    public BorrowDAO(Connection connection) {
        this.connection = connection;
        this.studentDAO = new StudentDAO(connection); // même connexion
        this.bookDAO = new BookDAO(connection);       // même connexion
    }

    public List<Borrow> getAllBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM borrows";

        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Récupérer les objets Student et Book associés
                Student student = studentDAO.getStudentById(rs.getInt("student_id"));
                Book book = bookDAO.getBookById(rs.getInt("book_id"));

                Borrow borrow = new Borrow(
                        rs.getInt("id"),
                        student,
                        book,
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date")
                );
                borrows.add(borrow);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des emprunts : " + e.getMessage());
        }
        return borrows;
    }


    public void save(Borrow borrow) {
        String query = "INSERT INTO borrows (student_id, book_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());
            stmt.setDate(3, new java.sql.Date(borrow.getBorrowDate().getTime()));
            stmt.setDate(4, borrow.getReturnDate() != null ?
                    new java.sql.Date(borrow.getReturnDate().getTime()) : null);

            stmt.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    borrow.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement de l'emprunt : " + e.getMessage());
        }
    }


    // Méthode utile pour récupérer un emprunt spécifique
    public Borrow getBorrowById(int id) {
        String query = "SELECT * FROM borrows WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Student student = studentDAO.getStudentById(rs.getInt("student_id"));
                Book book = bookDAO.getBookById(rs.getInt("book_id"));

                return new Borrow(
                        rs.getInt("id"),
                        student,
                        book,
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'emprunt : " + e.getMessage());
        }
        return null;
    }
    public void returnBook(int borrowId) {
        String sql = "UPDATE borrows SET return_date = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Création de la date actuelle et conversion en java.sql.Date
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            statement.setDate(1, sqlDate);
            statement.setInt(2, borrowId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Livre retourné avec succès !");
            } else {
                System.out.println("Emprunt non trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du retour du livre : " + e.getMessage());
        }
    }


}
