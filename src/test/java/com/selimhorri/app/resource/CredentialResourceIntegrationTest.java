package com.selimhorri.app.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.selimhorri.app.AbstractIntegrationTest;
import com.selimhorri.app.domain.RoleBasedAuthority;
import com.selimhorri.app.dto.request.CredentialInfoUpdateRequestDto;
import com.selimhorri.app.dto.request.PasswordChangeRequestDto;
import com.selimhorri.app.dto.request.UserCreateRequestDto;
import com.selimhorri.app.dto.response.UserResponseDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CredentialResourceIntegrationTest extends AbstractIntegrationTest {

        private static final String URI_USERS = "/api/users";
        private static final String URI_CREDENTIALS = "/api/credentials";
        private static Integer createdUserId;
        private static Integer createdCredentialId;
        private static String createdUsername;

        @Test
        @Order(1)
        void createOwningUserForCredential() throws Exception {
                final UserCreateRequestDto userCreateDto = UserCreateRequestDto.builder()
                                .firstName("CredentialOwner")
                                .lastName("User")
                                .email("credential.owner@example.com")
                                .phone("444555666")
                                .username("credential_owner")
                                .password("password123")
                                .build();

                final MvcResult mvcResult = this.mockMvc.perform(post(URI_USERS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(userCreateDto)))
                                .andExpect(status().isCreated())
                                .andReturn();

                final UserResponseDto createdUser = this.objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                new TypeReference<UserResponseDto>() {
                                });
                createdUserId = createdUser.getUserId();
                assertThat(createdUser.getCredential()).isNotNull();
                createdCredentialId = createdUser.getCredential().getCredentialId();
                createdUsername = createdUser.getCredential().getUsername();

                assertThat(createdUserId).isNotNull();
                assertThat(createdCredentialId).isNotNull();
                assertThat(createdUsername).isNotNull();
        }

        @Test
        @Order(2)
        void testGetCredentialByIdAndUsername() throws Exception {
                assertThat(createdCredentialId).as("Credential ID must be set for get tests").isNotNull();
                assertThat(createdUsername).as("Username must be set for get tests").isNotNull();

                this.mockMvc.perform(get(URI_CREDENTIALS + "/" + createdCredentialId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.credentialId").value(createdCredentialId))
                                .andExpect(jsonPath("$.username").value(createdUsername));

                this.mockMvc.perform(get(URI_CREDENTIALS + "/username/" + createdUsername)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.credentialId").value(createdCredentialId));
        }

        @Test
        @Order(3)
        void testUpdateCredentialInfo() throws Exception {
                assertThat(createdCredentialId).as("Credential ID must be set for update info test").isNotNull();
                final CredentialInfoUpdateRequestDto updateInfoDto = CredentialInfoUpdateRequestDto.builder()
                                .roleBasedAuthority(RoleBasedAuthority.ROLE_ADMIN.name())
                                .isEnabled(false)
                                .isAccountNonExpired(false)
                                .isAccountNonLocked(false)
                                .isCredentialsNonExpired(false)
                                .build();

                this.mockMvc.perform(put(URI_CREDENTIALS + "/" + createdCredentialId + "/info")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(updateInfoDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.roleBasedAuthority").value(RoleBasedAuthority.ROLE_ADMIN.name()))
                                .andExpect(jsonPath("$.isEnabled").value(false));
        }

        @Test
        @Order(4)
        void testUpdateCredentialPassword() throws Exception {
                assertThat(createdCredentialId).as("Credential ID must be set for update password test").isNotNull();
                final PasswordChangeRequestDto passwordDto = PasswordChangeRequestDto.builder()
                                .password("newStrongPassword")
                                .build();

                this.mockMvc.perform(put(URI_CREDENTIALS + "/" + createdCredentialId + "/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(passwordDto)))
                                .andExpect(status().isOk());
        }

        @Test
        @Order(5)
        void testDisallowedCredentialOperations() throws Exception {
                // POST a /api/credentials devuelve 405 directamente en el controller
                this.mockMvc.perform(post(URI_CREDENTIALS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isMethodNotAllowed());

                // DELETE a /api/credentials/{id} ahora devuelve 405 directamente desde el
                // controller
                assertThat(createdCredentialId).as("Credential ID must be set for disallowed delete test").isNotNull();
                this.mockMvc.perform(delete(URI_CREDENTIALS + "/" + createdCredentialId))
                                .andExpect(status().isMethodNotAllowed());
        }
}