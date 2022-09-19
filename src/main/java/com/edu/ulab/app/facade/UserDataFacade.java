package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);
        List<Long> bookIdList = collectBooksIdsFromRequest(userBookRequest, createdUser);
        log.info("Collected book ids: {}", bookIdList);

        createdUser.setBooksIdList(bookIdList);
        userService.updateUser(createdUser);
        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(createdUser.getBooksIdList())
                .build();
    }


    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long userId) {
        log.info("Got user 'id:{}' book update request: {}", userId, userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        userDto.setId(userId);
        log.info("Mapped user request: {}", userDto);

        UserDto updatedUser = userService.updateUser(userDto);
        List<Long> bookIdList = collectBooksIdsFromRequest(userBookRequest, updatedUser);
        log.info("Update user: {}", updatedUser);


        log.info("Collected book ids: {}", bookIdList);
        return UserBookResponse.builder()
                .userId(updatedUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        log.info("Got user 'id:{}' GET request", userId);
        UserDto userById = userService.getUserById(userId);

        log.info("Got user {}", userById);
        return UserBookResponse.builder()
                .userId(userById.getId())
                .booksIdList(userById.getBooksIdList())
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        log.info("Got user 'id:{}' delete request", userId);
        UserDto userById = userService.getUserById(userId);
        log.info("Got user {}", userById);
        userService.deleteUserById(userId);
        List<Long> booksIdList = userById.getBooksIdList();
        booksIdList.stream()
                .peek(bookService::deleteBookById);
        log.info("Deleted user {} and his books with 'ids:{}'", userById, booksIdList);
    }

    public List<UserDto> getAll() {
        log.info("Got all users GET request");
        return userService.getAll();
    }


    private List<Long> collectBooksIdsFromRequest(UserBookRequest userBookRequest, UserDto userDto) {
        return userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(userDto.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
    }
}
