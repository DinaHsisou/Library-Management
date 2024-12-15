package com.library;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.service.StudentService;
import com.library.util.DbConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    private StudentService studentService;
    private StudentDAO studentDAO;
    private Connection connection;

    @BeforeEach
    void setUp() {
        try {
            // Initialiser la connexion
            connection = DbConnection.getConnection();
            // Créer le DAO avec la connexion
            studentDAO = new StudentDAO(connection);
            // Créer le service avec le DAO
            studentService = new StudentService(studentDAO);
        } catch (Exception e) {
            fail("Erreur lors de l'initialisation des tests : " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            // Nettoyer la base de données après chaque test
            if (connection != null) {
                connection.createStatement().execute("DELETE FROM students");
                connection.close();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du nettoyage : " + e.getMessage());
        }
    }

    @Test
    void testAddStudent() {
        // Créer un étudiant test
        Student student = new Student("Alice");
        student.setId(1);

        // Ajouter l'étudiant
        studentService.addStudent(student);

        // Vérifier que l'étudiant a été ajouté
        Student foundStudent = studentService.findStudentById(1);
        assertNotNull(foundStudent, "L'étudiant devrait être trouvé");
        assertEquals("Alice", foundStudent.getName(), "Le nom devrait correspondre");
    }

    @Test
    void testFindStudentById() {
        // Créer et ajouter un étudiant test
        Student student = new Student("Alice");
        student.setId(1);
        studentService.addStudent(student);

        // Rechercher l'étudiant
        Student foundStudent = studentService.findStudentById(1);

        // Vérifications
        assertNotNull(foundStudent, "L'étudiant devrait être trouvé");
        assertEquals(1, foundStudent.getId(), "L'ID devrait correspondre");
        assertEquals("Alice", foundStudent.getName(), "Le nom devrait correspondre");
    }

    @Test
    void testFindNonExistentStudent() {
        // Rechercher un étudiant qui n'existe pas
        Student foundStudent = studentService.findStudentById(999);
        assertNull(foundStudent, "Aucun étudiant ne devrait être trouvé");
    }

    @Test
    void testDisplayStudents() {
        // Ajouter plusieurs étudiants
        Student student1 = new Student("Alice");
        student1.setId(1);
        Student student2 = new Student("Bob");
        student2.setId(2);

        studentService.addStudent(student1);
        studentService.addStudent(student2);

        // Vérifier que la méthode displayStudents ne lance pas d'exception
        assertDoesNotThrow(() -> studentService.displayStudents());
    }

    @Test
    void testCloseConnection() {
        // Vérifier que la fermeture de la connexion ne lance pas d'exception
        assertDoesNotThrow(() -> studentService.closeConnection());
    }
}