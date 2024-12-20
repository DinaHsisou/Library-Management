package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.util.DbConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private StudentDAO studentDAO;
    private Connection connection;

    public StudentService() {
        try {
            this.connection = DbConnection.getConnection();
            this.studentDAO = new StudentDAO(this.connection);
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation du service : {}", e.getMessage());
            throw new ServiceException("Erreur d'initialisation du service", e);
        }
    }

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public void addStudent(Student student) {
        try {
            studentDAO.addStudent(student);
            logger.info("Étudiant ajouté avec succès : {}", student.getName());
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de l'étudiant : {}", e.getMessage());
            throw new ServiceException("Erreur lors de l'ajout de l'étudiant", e);
        }
    }

    public void displayStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            logger.info("Liste des étudiants :");
            for (Student student : students) {
                logger.info("ID: {} | Nom: {}", student.getId(), student.getName());
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'affichage des étudiants : {}", e.getMessage());
            throw new ServiceException("Erreur lors de l'affichage des étudiants", e);
        }
    }

    public Student findStudentById(int id) {
        try {
            Student student = studentDAO.getStudentById(id);
            if (student == null) {
                logger.info("Aucun étudiant trouvé avec l'ID : {}", id);
            }
            return student;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de l'étudiant avec l'ID {} : {}", id, e.getMessage());
            throw new ServiceException("Erreur lors de la recherche de l'étudiant", e);
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Connexion fermée avec succès");
            } catch (Exception e) {
                logger.error("Erreur lors de la fermeture de la connexion : {}", e.getMessage());
                throw new ServiceException("Erreur lors de la fermeture de la connexion", e);
            }
        }
    }

    // Exception interne pour la gestion des erreurs
    private static class ServiceException extends RuntimeException {
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}