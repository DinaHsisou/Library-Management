package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private StudentDAO studentDAO;
    private Connection connection;

    public StudentService() {
        try {
            this.connection = DbConnection.getConnection();
            this.studentDAO = new StudentDAO(this.connection);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du service : " + e.getMessage());
        }
    }

    // Constructeur
    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    

    // Ajouter un étudiant
    public void addStudent(Student student) {
        try {
            studentDAO.addStudent(student);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de l'étudiant : " + e.getMessage());
        }
    }

    // Afficher tous les étudiants
    public void displayStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            if (students.isEmpty()) {
                System.out.println("Aucun étudiant trouvé.");
                return;
            }

            System.out.println("Liste des étudiants :");
            for (Student student : students) {
                System.out.println("ID: " + student.getId() + " | Nom: " + student.getName());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des étudiants : " + e.getMessage());
        }
    }

    // Trouver un étudiant par ID
    // Trouver un étudiant par ID
    public Student findStudentById(int id) {
        try {
            Student student = studentDAO.getStudentById(id);
            if (student == null) {
                System.out.println("Aucun étudiant trouvé avec l'ID : " + id);
            }
            return student;
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche de l'étudiant : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour fermer la connexion si nécessaire
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
