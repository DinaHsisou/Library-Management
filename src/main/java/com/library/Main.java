package com.library;

import com.library.service.BorrowService;
import com.library.service.BookService;
import com.library.service.StudentService;
import com.library.model.Book;
import com.library.model.Student;
import com.library.model.Borrow;
import com.library.util.DbConnection;
import java.util.Date;
import java.util.Scanner;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Création des services
        BookService bookService = new BookService();
        StudentService studentService = new StudentService();
        BorrowService borrowService = new BorrowService();

        boolean running = true;

        while (running) {
            System.out.println("\n===== Menu =====");
            System.out.println("1. Ajouter un livre");
            System.out.println("2. Afficher les livres");
            System.out.println("3. Ajouter un étudiant");
            System.out.println("4. Afficher les étudiants");
            System.out.println("5. Emprunter un livre");
            System.out.println("6. Retourner un livre");
            System.out.println("7. Afficher les emprunts");
            System.out.println("8. Quitter");

            System.out.print("Choisir une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consommer la ligne restante après l'entier

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Entrez le titre du livre: ");
                        String title = scanner.nextLine();
                        System.out.print("Entrez l'auteur du livre: ");
                        String author = scanner.nextLine();
                        System.out.print("Entrez l'éditeur du livre: ");
                        String publisher = scanner.nextLine();
                        System.out.print("Entrez l'année de publication: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Entrez l'ISBN du livre: ");
                        String isbn = scanner.nextLine();

                        Book newBook = new Book(title, author, publisher, year);
                        newBook.setIsbn(isbn);
                        newBook.setPublishedYear(year);
                        bookService.addBook(newBook);
                        break;

                    case 2:
                        bookService.displayBooks();
                        break;

                    case 3:
                        System.out.print("Entrez le nom de l'étudiant: ");
                        String studentName = scanner.nextLine();
                        Student newStudent = new Student(studentName);
                        studentService.addStudent(newStudent);
                        break;

                    case 4:
                        studentService.displayStudents();
                        break;

                    case 5:
                        System.out.print("Entrez l'ID de l'étudiant: ");
                        int studentId = scanner.nextInt();
                        System.out.print("Entrez l'ID du livre: ");
                        int bookId = scanner.nextInt();

                        // Créer l'emprunt
                        borrowService.borrowBook(studentId, bookId, new Date());
                        break;

                    case 6:
                        System.out.print("Entrez l'ID de l'emprunt à retourner : ");
                        int borrowId = scanner.nextInt();
                        borrowService.returnBook(borrowId);
                        break;


                    case 7:
                        borrowService.displayBorrows();
                        break;


                    case 8:
                        running = false;
                        System.out.println("Au revoir!");
                        // Fermer les connexions avant de quitter
                        bookService.closeConnection();
                        studentService.closeConnection();
                        borrowService.closeConnection();
                        break;

                    default:
                        System.out.println("Option invalide.");
                }
            } catch (Exception e) {
                System.err.println("Une erreur est survenue : " + e.getMessage());
            }
        }

        scanner.close();
    }
}