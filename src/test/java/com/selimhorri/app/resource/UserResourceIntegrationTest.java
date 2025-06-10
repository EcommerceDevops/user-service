package com.selimhorri.app.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.selimhorri.app.AbstractIntegrationTest;
import com.selimhorri.app.dto.request.UserCreateRequestDto;
import com.selimhorri.app.dto.response.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserResourceIntegrationTest extends AbstractIntegrationTest {

        private static final String URI_USERS = "/api/users";

        @Test
        void testUserCreationAndRetrievalFlow() throws Exception {
                // Arrange: Preparamos el DTO de creación
                final var createDto = UserCreateRequestDto.builder()
                                .firstName("Integration")
                                .lastName("Test")
                                .email("integration.test@example.com")
                                .phone("987654321")
                                .username("integration_test")
                                .password("securePassword")
                                .build();

                // 1. Act & Assert: CREAR un nuevo usuario vía POST
                final var mvcResult = this.mockMvc.perform(post(URI_USERS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(createDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.userId").isNumber())
                                .andExpect(jsonPath("$.firstName").value("Integration"))
                                .andExpect(jsonPath("$.credential.username").value("integration_test"))
                                .andReturn();

                // Extraemos la respuesta para obtener el ID del usuario creado
                final var responseAsString = mvcResult.getResponse().getContentAsString();
                final var createdUser = this.objectMapper.readValue(responseAsString,
                                new TypeReference<UserResponseDto>() {
                                });
                final Integer createdUserId = createdUser.getUserId();

                assertThat(createdUserId).isNotNull();

                // 2. Act & Assert: RECUPERAR el usuario recién creado vía GET
                this.mockMvc.perform(get(URI_USERS + "/" + createdUserId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.userId").value(createdUserId))
                                .andExpect(jsonPath("$.email").value("integration.test@example.com"));
        }

}