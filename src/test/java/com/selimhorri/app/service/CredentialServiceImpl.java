package com.selimhorri.app.service;

import com.selimhorri.app.domain.Credential;
import com.selimhorri.app.domain.RoleBasedAuthority;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.dto.request.CredentialInfoUpdateRequestDto;
import com.selimhorri.app.dto.request.PasswordChangeRequestDto;
import com.selimhorri.app.dto.response.CredentialResponseDto;
import com.selimhorri.app.repository.CredentialRepository;
import com.selimhorri.app.service.impl.CredentialServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CredentialServiceImplTest {

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CredentialServiceImpl credentialService;

    private Credential createTestCredential() {
        User user = new User();
        user.setUserId(1);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        // Crear una Credential para el User (simulando que UserMapper la necesita)
        Credential nestedUserCredential = new Credential(); // Puede parecer redundante pero es para el mapper
        nestedUserCredential.setCredentialId(2); // ID diferente
        nestedUserCredential.setUsername("testuser_nested");
        nestedUserCredential.setRoleBasedAuthority(RoleBasedAuthority.ROLE_USER);
        nestedUserCredential.setIsEnabled(true);
        nestedUserCredential.setIsAccountNonExpired(true);
        nestedUserCredential.setIsAccountNonLocked(true);
        nestedUserCredential.setIsCredentialsNonExpired(true);
        user.setCredential(nestedUserCredential); // Asignar al usuario
        nestedUserCredential.setUser(user);

        Credential credential = new Credential();
        credential.setCredentialId(1);
        credential.setUsername("testuser");
        credential.setPassword("oldEncodedPassword");
        credential.setRoleBasedAuthority(RoleBasedAuthority.ROLE_USER);
        credential.setIsEnabled(true);
        credential.setIsAccountNonExpired(true);
        credential.setIsAccountNonLocked(true);
        credential.setIsCredentialsNonExpired(true);
        credential.setUser(user); // Asignar el User completo a esta Credential
        credential.setUpdatedAt(Instant.now().minusSeconds(100));
        return credential;
    }

    @Test
    void findById_shouldReturnCredential_whenExists() {
        // Arrange
        final Credential credential = createTestCredential();
        when(this.credentialRepository.findById(1)).thenReturn(Optional.of(credential));

        // Act
        final CredentialResponseDto response = this.credentialService.findById(1);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("testuser");
    }

    @Test
    void updateInfo_shouldUpdateCredentialInfo_whenExists() {
        // Arrange
        final Credential existingCredential = createTestCredential();
        final CredentialInfoUpdateRequestDto updateDto = CredentialInfoUpdateRequestDto.builder()
                .roleBasedAuthority(RoleBasedAuthority.ROLE_ADMIN.name())
                .isEnabled(false)
                .isAccountNonExpired(false)
                .isAccountNonLocked(false)
                .isCredentialsNonExpired(false)
                .build();

        when(this.credentialRepository.findById(1)).thenReturn(Optional.of(existingCredential));
        when(this.credentialRepository.save(any(Credential.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        final CredentialResponseDto response = this.credentialService.updateInfo(1, updateDto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getRoleBasedAuthority()).isEqualTo(RoleBasedAuthority.ROLE_ADMIN.name());
        assertThat(response.getIsEnabled()).isFalse();
        assertThat(existingCredential.getUpdatedAt()).isNotNull(); // Verificar que se tocó el updatedAt
        verify(this.credentialRepository, times(1)).save(existingCredential);
    }

    @Test
    void updatePassword_shouldUpdatePassword_whenExists() {
        // Arrange
        final Credential existingCredential = createTestCredential();
        final PasswordChangeRequestDto passwordDto = PasswordChangeRequestDto.builder()
                .password("newPlainPassword")
                .build();

        when(this.credentialRepository.findById(1)).thenReturn(Optional.of(existingCredential));
        when(this.passwordEncoder.encode("newPlainPassword")).thenReturn("newEncodedPassword");
        when(this.credentialRepository.save(any(Credential.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        final CredentialResponseDto response = this.credentialService.updatePassword(1, passwordDto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(existingCredential.getPassword()).isEqualTo("newEncodedPassword");
        assertThat(existingCredential.getUpdatedAt()).isNotNull(); // Verificar que se tocó el updatedAt
        verify(this.passwordEncoder, times(1)).encode("newPlainPassword");
        verify(this.credentialRepository, times(1)).save(existingCredential);
    }

    @Test
    void create_shouldThrowUnsupportedOperationException() {
        assertThatThrownBy(() -> this.credentialService.create())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void deleteById_shouldThrowUnsupportedOperationException() {
        assertThatThrownBy(() -> this.credentialService.deleteById(1))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}