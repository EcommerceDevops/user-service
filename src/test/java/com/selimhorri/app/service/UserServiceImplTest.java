package com.selimhorri.app.service;

import com.selimhorri.app.domain.Credential;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.dto.request.UserCreateRequestDto;
import com.selimhorri.app.dto.response.UserResponseDto;
import com.selimhorri.app.exception.wrapper.UserObjectNotFoundException;
import com.selimhorri.app.repository.UserRepository;
import com.selimhorri.app.service.impl.UserServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create_shouldCreateUserAndVerificationToken_whenGivenValidDto() {
        // Arrange: Preparamos los datos de entrada
        final var createDto = UserCreateRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .username("johndoe")
                .password("password123")
                .build();

        // Simulamos el comportamiento de los mocks
        when(this.passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Cuando se guarde el usuario, lo devolvemos con un ID simulado
        when(this.userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setUserId(1);
            userToSave.getCredential().setCredentialId(1);
            userToSave.getCredential().getVerificationTokens().iterator().next().setVerificationTokenId(1);
            return userToSave;
        });

        // Act: Ejecutamos el método a probar
        final UserResponseDto response = this.userService.create(createDto);

        // Assert: Verificamos los resultados
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1);
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getCredential()).isNotNull();
        assertThat(response.getCredential().getUsername()).isEqualTo("johndoe");

        // Verificamos que los métodos de los mocks fueron llamados
        verify(this.passwordEncoder, times(1)).encode("password123");
        verify(this.userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        // Arrange: Creamos objetos completos y válidos
        final Credential credential = new Credential();
        credential.setCredentialId(1);
        credential.setUsername("janedoe");
        // *** CAMPOS IMPORTANTES QUE FALTABAN ***
        credential.setRoleBasedAuthority(com.selimhorri.app.domain.RoleBasedAuthority.ROLE_USER);
        credential.setIsEnabled(true);
        credential.setIsAccountNonExpired(true);
        credential.setIsAccountNonLocked(true);
        credential.setIsCredentialsNonExpired(true);

        final User user = new User();
        user.setUserId(1);
        user.setFirstName("Jane");
        user.setCredential(credential);

        // Establecemos la referencia bidireccional
        credential.setUser(user);

        // Simulamos que el repositorio devuelve este usuario completo
        when(this.userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        final UserResponseDto response = this.userService.findById(1);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("Jane");
        assertThat(response.getCredential().getUsername()).isEqualTo("janedoe");
        assertThat(response.getCredential().getRoleBasedAuthority()).isEqualTo("USER");
    }

    @Test
    void findById_shouldThrowException_whenUserDoesNotExist() {
        // Arrange
        when(this.userRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> this.userService.findById(99))
                .isInstanceOf(UserObjectNotFoundException.class)
                .hasMessageContaining("User with id: 99 not found");
    }

}