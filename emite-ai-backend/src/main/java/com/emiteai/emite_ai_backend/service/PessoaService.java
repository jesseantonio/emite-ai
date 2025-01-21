package com.emiteai.emite_ai_backend.service;

import com.emiteai.emite_ai_backend.domain.entity.Pessoa;
import com.emiteai.emite_ai_backend.dto.PessoaDto;
import com.emiteai.emite_ai_backend.dto.ReportDto;
import com.emiteai.emite_ai_backend.repository.PessoaRepository;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa saveOrUpdate(PessoaDto pessoaDTO) {
        Pessoa pessoa = Pessoa.builder()
                .id(pessoaDTO.getId())
                .nome(pessoaDTO.getNome())
                .cpf(pessoaDTO.getCpf())
                .telefone(pessoaDTO.getTelefone())
                .numero(pessoaDTO.getNumero())
                .cep(pessoaDTO.getCep())
                .bairro(pessoaDTO.getBairro())
                .municipio(pessoaDTO.getMunicipio())
                .estado(pessoaDTO.getEstado())
                .createdBy(pessoaDTO.getCreatedBy())
                .createdAt(pessoaDTO.getCreatedAt())
                .lastModificationBy(pessoaDTO.getLastModificationBy())
                .lastModificationAt(LocalDateTime.now())
                .build();

        return pessoaRepository.save(pessoa);
    }

    public void deleteById(UUID id) {
        pessoaRepository.deleteById(id);
    }

    public Page<PessoaDto> list(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return pessoaRepository.findAll(pageable).map(pessoa -> PessoaDto.builder()
                .id(pessoa.getId())
                .nome(pessoa.getNome())
                .cpf(pessoa.getCpf())
                .telefone(pessoa.getTelefone())
                .numero(pessoa.getNumero())
                .complemento(pessoa.getComplemento())
                .cep(pessoa.getCep())
                .bairro(pessoa.getBairro())
                .municipio(pessoa.getMunicipio())
                .estado(pessoa.getEstado())
                .createdBy(pessoa.getCreatedBy())
                .lastModificationBy(pessoa.getLastModificationBy())
                .createdAt(pessoa.getCreatedAt())
                .lastModificationAt(pessoa.getLastModificationAt())
                .build());
    }

    public Optional<Pessoa> findById(UUID id) {
        return pessoaRepository.findById(id);
    }

    @Async
    public CompletableFuture<Void> generateCsvReport(ReportDto reportDto) throws IOException {
        List<Pessoa> pessoas = pessoaRepository.findAll();

        try (CSVWriter writer = new CSVWriter(new FileWriter(reportDto.getFilePath()))) {
            writer.writeNext(new String[]{
                    "ID", "Nome", "CPF", "Telefone", "Número", "Complemento",
                    "CEP", "Bairro", "Município", "Estado", "Criado Por",
                    "Última Modificação Por", "Criado Em", "Última Modificação Em"
            });

            for (Pessoa pessoa : pessoas) {
                writer.writeNext(new String[]{
                        pessoa.getId().toString(),
                        pessoa.getNome(),
                        pessoa.getCpf(),
                        pessoa.getTelefone(),
                        pessoa.getNumero(),
                        pessoa.getComplemento(),
                        pessoa.getCep(),
                        pessoa.getBairro(),
                        pessoa.getMunicipio(),
                        pessoa.getEstado(),
                        pessoa.getCreatedBy(),
                        pessoa.getLastModificationBy(),
                        pessoa.getCreatedAt().toString(),
                        pessoa.getLastModificationAt().toString()
                });
            }
        }

        return CompletableFuture.completedFuture(null);
    }

}
