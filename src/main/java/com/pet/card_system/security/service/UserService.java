package com.pet.card_system.security.service;

import com.pet.card_system.core.dto.UserCreateRequest;
import com.pet.card_system.core.dto.UserDTO;
import com.pet.card_system.core.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
    Page<UserDTO> getAllUsers(Pageable pageable);

    UserDTO getUserById(Long id);

    UserDTO createUser(UserCreateRequest request);

    UserDTO updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}

