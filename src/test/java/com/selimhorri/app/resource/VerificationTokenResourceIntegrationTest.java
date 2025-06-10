package com.selimhorri.app.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.selimhorri.app.AbstractIntegrationTest;
import com.selimhorri.app.dto.request.UserCreateRequestDto;
import com.selimhorri.app.dto.response.UserResponseDto;
import com.selimhorri.app.dto.response.VerificationTokenResponseDto;
import com.selimhorri.app.dto.response.collection.DtoCollectionResponse;
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
class VerificationTokenResourceIntegrationTest extends AbstractIntegrationTest {

        private static final String URI_USERS = "/api/users";
        private static final String URI_VERIFICATION_TOKENS = "/api/verificationTokens";
        private static Integer createdUserId;
        private static Integer createdVerificationTokenId;

        @Test
        @Order(1)
        void createOwningUserForVerificationToken() throws Exception {
                final UserCreateRequestDto userCreateDto = UserCreateRequestDto.builder()
                                .firstName("TokenOwner")
                                .lastName("User")
                                .email("token.owner@example.com")
                                .phone("777888999")
                                .username("token_owner")
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
                assertThat(createdUserId).isNotNull();

                final MvcResult tokenListResult = this.mockMvc.perform(get(URI_VERIFICATION_TOKENS)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                DtoCollectionResponse<VerificationTokenResponseDto> tokens = this.objectMapper.readValue(
                                tokenListResult.getResponse().getContentAsString(),
                                new TypeReference<DtoCollectionResponse<VerificationTokenResponseDto>>() {
                                });

                VerificationTokenResponseDto foundToken = tokens.getCollection().stream()
                                .filter(t -> t.getCredential() != null &&
                                                t.getCredential().getUser() != null &&
                                                t.getCredential().getUser().getUserId().equals(createdUserId))
                                .findFirst()
                                .orElseThrow(() -> new AssertionError(
                                                "Verification token for created user not found. User ID: "
                                                                + createdUserId));

                createdVerificationTokenId = foundToken.getVerificationTokenId();
                assertThat(createdVerificationTokenId).isNotNull();
        }

        @Test
        @Order(2)
        void testGetVerificationTokenById() throws Exception {
                assertThat(createdVerificationTokenId).as("Verification Token ID must be set for get test").isNotNull();
                assertThat(createdUserId).as("Created User ID must be set for verification").isNotNull();

                this.mockMvc.perform(get(URI_VERIFICATION_TOKENS + "/" + createdVerificationTokenId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.verificationTokenId").value(createdVerificationTokenId))
                                .andExpect(jsonPath("$.credential.user.userId").value(createdUserId));
        }

        @Test
        @Order(3)
        void testDisallowedVerificationTokenOperations() throws Exception {
                assertThat(createdVerificationTokenId).as("Verification Token ID must be set for disallowed ops test")
                                .isNotNull();

                // POST
                this.mockMvc.perform(post(URI_VERIFICATION_TOKENS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isMethodNotAllowed());

                // PUT /api/verificationTokens/{id}
                this.mockMvc.perform(put(URI_VERIFICATION_TOKENS + "/" + createdVerificationTokenId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isMethodNotAllowed());

                // PUT /api/verificationTokens
                this.mockMvc.perform(put(URI_VERIFICATION_TOKENS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isMethodNotAllowed());

                // DELETE
                this.mockMvc.perform(delete(URI_VERIFICATION_TOKENS + "/" + createdVerificationTokenId))
                                .andExpect(status().isMethodNotAllowed()); // Controller ahora devuelve 405 directamente
        }
}