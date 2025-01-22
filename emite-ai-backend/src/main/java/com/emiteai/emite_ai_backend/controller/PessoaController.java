package com.emiteai.emite_ai_backend.controller;

import com.emiteai.emite_ai_backend.dto.PessoaDto;
import com.emiteai.emite_ai_backend.dto.ReportDto;
import com.emiteai.emite_ai_backend.service.PessoaService;
import com.emiteai.emite_ai_backend.domain.entity.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/pessoa")
@CrossOrigin(origins = "http://localhost:3000")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @PostMapping
    public Pessoa saveOrUpdate(@RequestBody PessoaDto pessoa) {
        return pessoaService.saveOrUpdate(pessoa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        pessoaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PessoaDto>> list(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                @RequestParam(value = "sort", defaultValue = "nome", required = false) String[] sort) {
        return new ResponseEntity<>(pessoaService.list(page, size, sort), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Pessoa>> findById(@PathVariable UUID id) {
        return new ResponseEntity<>(pessoaService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/process")
    public ResponseEntity<String> processReport() {
        try {
            CompletableFuture<Void> future = pessoaService.generateCsvReport();
            return ResponseEntity.ok("Relatório está sendo gerado em segundo plano.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao gerar relatório: " + e.getMessage());
        }
    }

}
