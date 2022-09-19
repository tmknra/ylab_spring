package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.Entity;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.Storage;
import com.edu.ulab.app.storage.StorageService;
import com.edu.ulab.app.storage.StorageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private static final StorageService bookStorage = new StorageServiceImpl("books");
    private final BookMapper bookMapper;

    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);
        BookEntity save = (BookEntity) bookStorage.save(bookEntity);
        return bookMapper.bookEntityToBookDto(save);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        BookEntity byId = (BookEntity) bookStorage.findById(bookDto.getId());

        byId.setUserId(Objects.requireNonNull(bookDto.getUserId()));
        byId.setAuthor(Objects.requireNonNull(bookDto.getAuthor()));
        byId.setTitle(Objects.requireNonNull(bookDto.getTitle()));
        byId.setPageCount(Objects.requireNonNull(bookDto.getPageCount()));

        BookEntity save = (BookEntity) bookStorage.save(byId);
        return bookMapper.bookEntityToBookDto(save);
    }

    @Override
    public BookDto getBookById(Long id) {
        BookEntity byId = (BookEntity) bookStorage.findById(id);
        return bookMapper.bookEntityToBookDto(byId);
    }

    @Override
    public void deleteBookById(Long id) {
        bookStorage.deleteById(id);
    }

    @Override
    public List<BookDto> getAll() {
        return bookStorage.getAll()
                .stream()
                .map(entity -> bookMapper.bookEntityToBookDto((BookEntity) entity))
                .toList();
    }
}
