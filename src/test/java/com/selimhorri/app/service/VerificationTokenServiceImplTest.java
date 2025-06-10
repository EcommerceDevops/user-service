package com.selimhorri.app.service;

import com.selimhorri.app.domain.Credential;
import com.selimhorri.app.domain.RoleBasedAuthority;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.domain.VerificationToken;
import com.selimhorri.app.dto.response.VerificationTokenResponseDto;
import com.selimhorri.app.repository.VerificationTokenRepository;
import com.selimhorri.app.service.impl.VerificationTokenServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationTokenServiceImplTest {

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private VerificationTokenServiceImpl verificationTokenService;

    private VerificationToken createTestVerificationToken() {
        User user = new User();
        user.setUserId(1);
        user.setFirstName("Test");
        Credential credential = new Credential();
        credential.setCredentialId(1);
        credential.setUsername("testuser");
        credential.setRoleBasedAuthority(RoleBasedAuthority.ROLE_USER);
        credential.setUser(user);
        user.setCredential(credential);

        VerificationToken token = new VerificationToken();
        token.setVerificationTokenId(1);
        token.setToken("test-token");
        token.setExpireDate(LocalDate.now().plusDays(1));
        token.setCredential(credential);
        return token;
    }

    @Test
    void findById_shouldReturnToken_whenExists() {
        // Arrange
        final VerificationToken token = createTestVerificationToken();
        when(this.verificationTokenRepository.findById(1)).thenReturn(Optional.of(token));

        // Act
        final VerificationTokenResponseDto response = this.verificationTokenService.findById(1);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("test-token");
        assertThat(response.getCredential().getUsername()).isEqualTo("testuser");
    }

    @Test
    void create_shouldThrowUnsupportedOperationException() {
        assertThatThrownBy(() -> this.verificationTokenService.create())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void update_shouldThrowUnsupportedOperationException() {
        assertThatThrownBy(() -> this.verificationTokenService.update())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void deleteById_shouldThrowUnsupportedOperationException() {
        assertThatThrownBy(() -> this.verificationTokenService.deleteById(1))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}