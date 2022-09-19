package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.StorageService;
import com.edu.ulab.app.storage.StorageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final StorageService userStorage = new StorageServiceImpl("users");
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity user = userMapper.userDtoToEserEntity(userDto);
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id
        return userMapper.userEntityToUserDto((UserEntity) userStorage.save(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserEntity byId = (UserEntity) userStorage.findById(userDto.getId());
        if (userDto.getAge()!=null)
        byId.setAge(userDto.getAge());

        if (userDto.getFullName()!=null)
        byId.setFullName(userDto.getFullName());

        if (userDto.getTitle()!=null)
        byId.setTitle(userDto.getTitle());

        if (userDto.getBooksIdList()!=null)
        byId.setBooksIdList(userDto.getBooksIdList());

        UserEntity save = (UserEntity) userStorage.save(byId);
        return userMapper.userEntityToUserDto(save);
    }

    @Override
    public UserDto getUserById(Long id) {
        UserEntity byId = (UserEntity) userStorage.findById(id);
        return userMapper.userEntityToUserDto(byId);
    }

    @Override
    public UserDto deleteUserById(Long id) {
        return userMapper.userEntityToUserDto((UserEntity) userStorage.deleteById(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userStorage.getAll()
                .stream()
                .map(entity -> userMapper.userEntityToUserDto((UserEntity) entity))
                .toList();
    }
}
