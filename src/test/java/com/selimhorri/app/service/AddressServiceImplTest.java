package com.selimhorri.app.service;

import com.selimhorri.app.domain.Address;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.dto.request.AddressCreateRequestDto;
import com.selimhorri.app.dto.request.AddressUpdateRequestDto;
import com.selimhorri.app.dto.response.AddressResponseDto;
import com.selimhorri.app.exception.wrapper.UserObjectNotFoundException;
import com.selimhorri.app.repository.AddressRepository;
import com.selimhorri.app.repository.UserRepository;
import com.selimhorri.app.service.impl.AddressServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void create_shouldCreateAddress_whenUserExists() {
        // Arrange
        // Crear un User y Credential completos para la simulación
        final com.selimhorri.app.domain.Credential userCredential = new com.selimhorri.app.domain.Credential();
        userCredential.setCredentialId(1);
        userCredential.setUsername("address_owner");
        userCredential.setRoleBasedAuthority(com.selimhorri.app.domain.RoleBasedAuthority.ROLE_USER);
        userCredential.setIsEnabled(true); // y otros booleanos
        userCredential.setIsAccountNonExpired(true);
        userCredential.setIsAccountNonLocked(true);
        userCredential.setIsCredentialsNonExpired(true);

        final User user = new User();
        user.setUserId(1);
        user.setFirstName("AddressOwner");
        user.setCredential(userCredential); // Asignar la credencial completa
        userCredential.setUser(user); // Mantener la bidireccionalidad

        final AddressCreateRequestDto createDto = AddressCreateRequestDto.builder()
                .fullAddress("123 Main St")
                .postalCode("12345")
                .city("Anytown")
                .userId(1)
                .build();

        when(this.userRepository.findById(1)).thenReturn(Optional.of(user));
        when(this.addressRepository.save(any(Address.class))).thenAnswer(invocation -> {
            Address addressToSave = invocation.getArgument(0);
            addressToSave.setAddressId(1);
            // Para que el mapper funcione, la dirección debe tener el usuario completo
            addressToSave.setUser(user);
            return addressToSave;
        });

        // Act
        final AddressResponseDto response = this.addressService.create(createDto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAddressId()).isEqualTo(1);
        assertThat(response.getFullAddress()).isEqualTo("123 Main St");
        assertThat(response.getUser()).isNotNull(); // Verificar que el usuario se mapeó
        assertThat(response.getUser().getFirstName()).isEqualTo("AddressOwner");
        verify(this.userRepository, times(1)).findById(1);
        verify(this.addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void create_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        // Arrange
        final AddressCreateRequestDto createDto = AddressCreateRequestDto.builder().userId(99).build();
        when(this.userRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> this.addressService.create(createDto))
                .isInstanceOf(UserObjectNotFoundException.class)
                .hasMessageContaining("Cannot create address, user with id: 99 not found");
    }

    @Test
    void update_shouldUpdateAddress_whenAddressExists() {
        // Arrange
        // Crear un User y Credential completos para la simulación
        final com.selimhorri.app.domain.Credential userCredential = new com.selimhorri.app.domain.Credential();
        userCredential.setCredentialId(1);
        userCredential.setUsername("address_owner_update");
        userCredential.setRoleBasedAuthority(com.selimhorri.app.domain.RoleBasedAuthority.ROLE_USER);
        userCredential.setIsEnabled(true);
        userCredential.setIsAccountNonExpired(true);
        userCredential.setIsAccountNonLocked(true);
        userCredential.setIsCredentialsNonExpired(true);

        final User user = new User();
        user.setUserId(1);
        user.setFirstName("AddressOwnerUpdate");
        user.setCredential(userCredential);
        userCredential.setUser(user);

        final Address existingAddress = new Address();
        existingAddress.setAddressId(1);
        existingAddress.setFullAddress("Old Address");
        existingAddress.setUser(user); // ASIGNAR EL USUARIO COMPLETO
        existingAddress.setUpdatedAt(Instant.now().minusSeconds(100));

        final AddressUpdateRequestDto updateDto = AddressUpdateRequestDto.builder()
                .fullAddress("New Address")
                .postalCode("54321")
                .city("Newcity")
                .build();

        when(this.addressRepository.findById(1)).thenReturn(Optional.of(existingAddress));
        when(this.addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        final AddressResponseDto response = this.addressService.update(1, updateDto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getFullAddress()).isEqualTo("New Address");
        assertThat(response.getPostalCode()).isEqualTo("54321");
        assertThat(existingAddress.getUpdatedAt()).isNotNull();
        verify(this.addressRepository, times(1)).save(existingAddress);
    }
}