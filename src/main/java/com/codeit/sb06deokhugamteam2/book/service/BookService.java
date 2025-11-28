package com.codeit.sb06deokhugamteam2.book.service;

import com.codeit.sb06deokhugamteam2.book.client.NaverSearchClient;
import com.codeit.sb06deokhugamteam2.book.dto.response.CursorPageResponsePopularBookDto;
import com.codeit.sb06deokhugamteam2.book.dto.data.PopularBookDto;
import com.codeit.sb06deokhugamteam2.book.dto.data.BookDto;
import com.codeit.sb06deokhugamteam2.book.dto.request.BookCreateRequest;
import com.codeit.sb06deokhugamteam2.book.dto.request.BookImageCreateRequest;
import com.codeit.sb06deokhugamteam2.book.dto.response.NaverBookDto;
import com.codeit.sb06deokhugamteam2.book.entity.Book;
import com.codeit.sb06deokhugamteam2.book.mapper.BookMapper;
import com.codeit.sb06deokhugamteam2.book.repository.BookRepository;
import com.codeit.sb06deokhugamteam2.book.storage.S3Storage;
import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import com.codeit.sb06deokhugamteam2.dashboard.entity.DashBoard;
import com.codeit.sb06deokhugamteam2.dashboard.repository.DashBoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final DashBoardRepository dashBoardRepository;
    private final S3Storage s3Storage;
    private final BookMapper bookMapper;
    private final NaverSearchClient naverSearchClient;

    public BookDto create(BookCreateRequest bookCreateRequest, Optional<BookImageCreateRequest> optionalBookImageCreateRequest) {
        Book book = Book.builder()
                .isbn(bookCreateRequest.getIsbn())
                .title(bookCreateRequest.getTitle())
                .author(bookCreateRequest.getAuthor())
                .description(bookCreateRequest.getDescription())
                .publisher(bookCreateRequest.getPublisher())
                .publishedDate(bookCreateRequest.getPublishedDate())
                .build();

        Book savedBook = bookRepository.save(book);
        String thumbnailUrl = optionalBookImageCreateRequest.map(bookImageCreateRequest -> {
                    String key = savedBook.getId().toString() + "-" + bookImageCreateRequest.getOriginalFilename();
                    s3Storage.putThumbnail(key, bookImageCreateRequest.getBytes(), bookImageCreateRequest.getContentType());
                    return s3Storage.getThumbnail(key);
                }
        ).orElse(null);

        savedBook.update(thumbnailUrl);

        return bookMapper.toDto(savedBook);
    }

    public NaverBookDto info(String isbn) {
        return naverSearchClient.bookSearchByIsbn(isbn);
    }

    public CursorPageResponsePopularBookDto getPopularBooks(PeriodType period, String cursor, Instant after, Sort.Direction direction, Integer limit) {

        List<DashBoard> bookDashboard = dashBoardRepository.findRankedListByCursor(RankingType.BOOK, period, cursor, after, direction, limit);

        List<PopularBookDto> popularBookDtoList = new ArrayList<>();

        bookDashboard.forEach(dashBoard -> {
            Book book = bookRepository.findById(dashBoard.getEntityId())
                    .orElseThrow(() -> new EntityNotFoundException("도서를 찾을 수 없습니다: " + dashBoard.getEntityId()));
            popularBookDtoList.add(
                    bookMapper.toDto(dashBoard, book, period)
            );
        });

        CursorPageResponsePopularBookDto response = bookMapper.toCursorBookDto(popularBookDtoList, limit);

        return response;
    }

    public void deleteSoft(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("도서를 찾을 수 없습니다: " + bookId));

//        book.getReviews().forEach(review -> {
//            review.deleted();
//            review.getComments().forEach(Comment::softDelete);
//        });

        book.setDeletedAsTrue();
        bookRepository.save(book);
        log.info("도서 논리 삭제 완료: {}", bookId);
    }

    public void deleteHard(UUID bookId) {
        bookRepository.deleteById(bookId);
        log.info("도서 물리 삭제 완료: {}", bookId);
    }
}
