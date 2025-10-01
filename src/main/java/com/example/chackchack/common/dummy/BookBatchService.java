package com.example.chackchack.common.dummy;

import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookBatchService {

    private final BookRepository bookRepository;

    @Transactional
    public void saveBatch(List<Book> books) {
        bookRepository.saveAll(books);
    }
}
