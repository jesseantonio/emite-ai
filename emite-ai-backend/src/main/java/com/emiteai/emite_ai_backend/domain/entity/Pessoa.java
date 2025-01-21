package com.emiteai.emite_ai_backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pessoa")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modification_by")
    private String lastModificationBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_modification_at")
    private LocalDateTime lastModificationAt;

}

