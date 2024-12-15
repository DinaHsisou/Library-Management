package com.library.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    //added isbn and publishedYear fields
    private String isbn;
    private int publishedYear;


    // Constructeur par défaut
    public Book() {
    }

    // Constructeur complet
    public Book(String title, String author, String publisher, int year) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
    }
    @Override
    public String toString() {
        return String.format("Livre ID: %d | Titre: %s | Auteur: %s | Éditeur: %s | Année: %d | ISBN: %s",
                id, title, author, publisher, publishedYear, isbn);
    }

    // Constructeur additionnel si nécessaire
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }
}
