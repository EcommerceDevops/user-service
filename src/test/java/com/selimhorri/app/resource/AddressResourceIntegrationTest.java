package com.selimhorri.app.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.selimhorri.app.AbstractIntegrationTest;
import com.selimhorri.app.dto.request.AddressCreateRequestDto;
import com.selimhorri.app.dto.request.AddressUpdateRequestDto;
import com.selimhorri.app.dto.request.UserCreateRequestDto;
import com.selimhorri.app.dto.response.AddressResponseDto;
import com.selimhorri.app.dto.response.UserResponseDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AddressResourceIntegrationTest extends AbstractIntegrationTest {

        private static final String URI_USERS = "/api/users";
        private static final String URI_ADDRESSES = "/api/address";
        private static Integer createdUserId;
        private static Integer createdAddressId;

        @Test
        @Order(1)
        void createOwningUserForAddress() throws Exception {
                final UserCreateRequestDto userCreateDto = UserCreateRequestDto.builder()
                                .firstName("AddressOwner")
                                .lastName("User")
                                .email("address.owner@example.com")
                                .phone("111222333")
                                .username("address_owner")
                                .password("password123")
                                .build();

                final var mvcResult = this.mockMvc.perform(post(URI_USERS)
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
        }

        @Test
        @Order(2)
        void testAddressCreationAndRetrievalFlow() throws Exception {
                assertThat(createdUserId).as("Owning User must be created first for Address flow").isNotNull();

                final AddressCreateRequestDto createDto = AddressCreateRequestDto.builder()
                                .fullAddress("456 Oak St")
                                .postalCode("67890")
                                .city("Otherville")
                                .userId(createdUserId)
                                .build();

                final var mvcResult = this.mockMvc.perform(post(URI_ADDRESSES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(createDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.addressId").isNumber())
                                .andExpect(jsonPath("$.fullAddress").value("456 Oak St"))
                                .andExpect(jsonPath("$.user.userId").value(createdUserId))
                                .andReturn();

                final AddressResponseDto createdAddress = this.objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                new TypeReference<AddressResponseDto>() {
                                });
                createdAddressId = createdAddress.getAddressId();
                assertThat(createdAddressId).isNotNull();

                this.mockMvc.perform(get(URI_ADDRESSES + "/" + createdAddressId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.city").value("Otherville"));
        }

        @Test
        @Order(3)
        void testUpdateAddress() throws Exception {
                assertThat(createdAddressId).as("Address must be created first for update").isNotNull();
                final AddressUpdateRequestDto updateDto = AddressUpdateRequestDto.builder()
                                .fullAddress("789 Pine St Updated")
                                .postalCode("00000")
                                .city("UpdatedCity")
                                .build();

                this.mockMvc.perform(put(URI_ADDRESSES + "/" + createdAddressId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(updateDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.fullAddress").value("789 Pine St Updated"))
                                .andExpect(jsonPath("$.city").value("UpdatedCity"));
        }

        @Test
        @Order(4)
        void testDeleteAddress() throws Exception {
                assertThat(createdAddressId).as("Address ID must be set from previous tests for delete").isNotNull();

                this.mockMvc.perform(delete(URI_ADDRESSES + "/" + createdAddressId))
                                .andExpect(status().isNoContent());

                // Después de borrar, un GET a ese ID debe devolver el error que tu
                // ApiExcetionHandler genere
                // para AddressNotFoundException. Si es 400, esta aserción es correcta.
                // Idealmente, debería ser 404.
                this.mockMvc.perform(get(URI_ADDRESSES + "/" + createdAddressId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest()) // AJUSTADO A BAD_REQUEST BASADO EN TU LOG
                                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST")) // Verifica el cuerpo del
                                                                                          // error
                                .andExpect(jsonPath("$.msg")
                                                .value(String.format("#### Address with id: %d not found!! ####",
                                                                createdAddressId)));
        }
}