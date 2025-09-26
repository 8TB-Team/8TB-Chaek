package com.example.chackchack.domain.bookItem.entity;

import com.example.chackchack.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Random;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    Book book;

//    int volume;

    @Column(unique = true, updatable = false, nullable = false, length = 64)
    String serialNumber;

//    String isbn;

    // JPA persist 될 때 자동 실행
    @PrePersist
    public void generateValues() {
        if (this.serialNumber == null) {
            this.serialNumber = serialGenerator();
        }
//        if (this.isbn == null) {
//            this.isbn = ISBNGenerator();
//        }
    }

//    @Builder
//    public BookItem(int volume, Book book) {
//        this.volume = volume;
//        this.book = book;
//    }

    @Builder
    public BookItem(Book book) {
        this.book = book;
    }

    static String serialGenerator() {

        int length = 64;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

//    static String ISBNGenerator() {
//
//        int length = 17;
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        StringBuilder sb = new StringBuilder();
//        Random random = new Random();
//
//        for (int i = 0; i < length; i++) {
//            int index = random.nextInt(characters.length());
//            sb.append(characters.charAt(index));
//        }
//
//        return sb.toString();
//    }
}
