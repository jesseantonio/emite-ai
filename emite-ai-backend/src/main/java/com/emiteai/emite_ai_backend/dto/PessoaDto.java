package com.emiteai.emite_ai_backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class PessoaDto {

    private UUID id;
    private String nome;
    private String cpf;
    private String telefone;
    private String numero;
    private String complemento;
    private String cep;
    private String bairro;
    private String municipio;
    private String estado;
    private String createdBy;
    private String lastModificationBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastModificationAt;
}