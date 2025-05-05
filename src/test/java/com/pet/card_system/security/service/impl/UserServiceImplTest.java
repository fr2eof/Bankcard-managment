package com.pet.card_system.security.service.impl;

import com.pet.card_system.core.dto.UserCreateRequest;
import com.pet.card_system.core.dto.UserDTO;
import com.pet.card_system.core.dto.UserUpdateRequest;
import com.pet.card_system.core.exception.UserNotFoundException;
import com.pet.card_system.core.repository.UserRepository;
import com.pet.card_system.core.repository.entity.Role;
import com.pet.card_system.core.repository.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("john");
        testUser.setEmail("john@example.com");
        testUser.setRole(Role.USER);
        testUser.setPassword("pass123");
    }

    @Test
    void testGetAllUsers() {
        Page<User> page = new PageImpl<>(List.of(testUser));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<UserDTO> result = userService.getAllUsers(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("john", result.getContent().get(0).username());
    }

    @Test
    void testGetUserById_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserDTO result = userService.getUserById(1L);

        assertEquals("john", result.username());
    }

    @Test
    void testGetUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testCreateUser() {
        UserCreateRequest request = new UserCreateRequest("john", "john@example.com", "pass123", "USER");

        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserDTO result = userService.createUser(request);

        assertEquals("john", result.username());
        assertEquals("john@example.com", result.email());
    }

    @Test
    void testUpdateUser_partialUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserUpdateRequest update = new UserUpdateRequest("john@example.com", null, null, "ADMIN");

        UserDTO result = userService.updateUser(1L, update);

        assertEquals("ADMIN", String.valueOf(result.role()));
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
