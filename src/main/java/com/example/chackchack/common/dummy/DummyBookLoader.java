package com.example.chackchack.common.dummy;

import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DummyBookLoader implements CommandLineRunner {
    private static final List<String> categories = List.of(
            "과학", "철학", "경제", "에세이", "소설", "시집", "자기계발", "역사", "IT", "여행"
    );
    private final BookRepository bookRepository;
    private final BookBatchService bookBatchService;
    private final Random random = new Random();

    private String randomTitle() {
        String[] keywords1 = {"행복", "바람", "꿈", "사랑", "죽음", "시간", "청춘", "별", "바다", "강아지", "포화", "세상", "기억"};
        String[] keywords2 = {"의 비밀", " 속으로", "에 관하여", "을 찾아서", "의 이야기", "이다", "의 역사", "의 끝"};
        return keywords1[random.nextInt(keywords1.length)]
                + keywords2[random.nextInt(keywords2.length)];
    }

    private String randomAuthor() {
        String[] lastNames = {"김", "이", "박", "최", "정", "조", "한", "서", "윤", "장"};
        String[] firstNames = {"우주", "지훈", "서윤", "도아", "정민", "지민", "예진", "수빈", "현진", "다은"};
        return lastNames[random.nextInt(lastNames.length)]
                + firstNames[random.nextInt(firstNames.length)];
    }

    private String randomCategory() {
        return categories.get(random.nextInt(categories.size()));
    }

    @Override
    public void run(String... args) {

        int total = 50000;
        int batchSize = 1000;

        if (bookRepository.count() > total) {
            System.out.println("이미 데이터가 존재합니다. 스킵합니다.");
            return;
        }

        System.out.println("더미 데이터 삽입 시작");

        for (int i = 0; i < total; i += batchSize) {
            List<Book> books = IntStream.range(0, batchSize)
                    .mapToObj(n -> new Book(
                            randomAuthor(),
                            randomCategory(),
                            randomTitle()
                    ))
                    .toList();

            bookBatchService.saveBatch(books);
        }

        System.out.println("Dummy data insert 완료");
    }
}