package com.example.chackchack.domain.book.entity;

import com.example.chackchack.common.entity.BaseEntity;
import com.example.chackchack.domain.book.dto.request.BookRequest;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long Id;

    @Size(max = 255, message = "255자 이하로 입력하세요")
    String title;

    @Size(max = 100, message = "100자 이하로 입력하세요")
    String author;

    @Size(max = 50, message = "50자 이하로 입력하세요")
    String category;

    public Book(String author, String category,String title) {
        this.author = author;
        this.category = category;
        this.title = title;
    }

    public static Book toEntityFrom(BookRequest bookRequest){
        return new Book(bookRequest.author(), bookRequest.category(), bookRequest.title());
    }

    public static Book toUpdateOf(Long bookId, BookRequest bookRequest){
        return new Book(bookRequest.author(), bookRequest.category(), bookRequest.title());

    }
}
