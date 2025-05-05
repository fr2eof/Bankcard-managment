package com.pet.card_system.security.service.impl;

import com.pet.card_system.core.dto.UserCreateRequest;
import com.pet.card_system.core.dto.UserDTO;
import com.pet.card_system.core.dto.UserUpdateRequest;
import com.pet.card_system.core.exception.UserNotFoundException;
import com.pet.card_system.core.repository.entity.Role;
import com.pet.card_system.core.repository.entity.User;
import com.pet.card_system.core.repository.UserRepository;
import com.pet.card_system.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return mapToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO createUser(UserCreateRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(Role.valueOf(request.role()));

        user = userRepository.save(user);
        return mapToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        if (request.role() != null) {
            user.setRole(Role.valueOf(request.role()));
        }

        if (request.role() != null) {
            user.setRole(Role.valueOf(request.role()));
        }

        return mapToDTO(userRepository.save(user));
    }


    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
