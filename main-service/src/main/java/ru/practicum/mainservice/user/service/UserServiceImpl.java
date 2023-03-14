package ru.practicum.mainservice.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.user.dto.UserDtoIn;
import ru.practicum.mainservice.user.dto.UserDtoOut;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDtoOut add(UserDtoIn userDtoIn) {
        log.debug("UserServiceImpl: add user with name= {}", userDtoIn.getName());
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDtoIn)));
    }

    @Override
    public List<UserDtoOut> getAll(List<Long> ids, Integer from, Integer size) {
        List<User> users = userRepository.getAllByIdIsIn(ids, PageRequest.of(from, size));
        return users.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("UserServiceImpl: user with id=" +
                                id + " was not found."));
        log.debug("UserServiceImpl: delete user with id={}", id);
        userRepository.delete(user);
    }
}
