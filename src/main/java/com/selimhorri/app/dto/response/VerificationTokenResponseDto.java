package com.selimhorri.app.dto.response;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.selimhorri.app.constant.AppConstant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationTokenResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer verificationTokenId;
    private String token;
    @JsonFormat(pattern = AppConstant.LOCAL_DATE_FORMAT, shape = Shape.STRING)
    @DateTimeFormat(pattern = AppConstant.LOCAL_DATE_FORMAT)
    private LocalDate expireDate;
    private CredentialResponseDto credential; // Contexto de la credencial propietaria

}