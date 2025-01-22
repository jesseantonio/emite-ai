package com.emiteai.emite_ai_backend.controller;

import com.emiteai.emite_ai_backend.dto.PessoaDto;
import com.emiteai.emite_ai_backend.dto.ReportDto;
import com.emiteai.emite_ai_backend.service.PessoaService;
import com.emiteai.emite_ai_backend.domain.entity.Pessoa;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaControllerTest {

    @InjectMocks
    private PessoaController pessoaController;

    @Mock
    private PessoaService pessoaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class SaveOrUpdateTest {

        @Test
        void testSaveOrUpdate() {
            PessoaDto pessoaDto = new PessoaDto();
            Pessoa pessoa = new Pessoa();

            when(pessoaService.saveOrUpdate(pessoaDto)).thenReturn(pessoa);

            Pessoa result = pessoaController.saveOrUpdate(pessoaDto);

            Assertions.assertThat(result).isEqualTo(pessoa);

            InOrder inOrder = inOrder(pessoaService);
            inOrder.verify(pessoaService).saveOrUpdate(pessoaDto);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void testDelete() {
            UUID id = UUID.randomUUID();

            ResponseEntity<Void> response = pessoaController.delete(id);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

            InOrder inOrder = inOrder(pessoaService);
            inOrder.verify(pessoaService).deleteById(id);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Nested
    class ListTest {

        @Test
        void testList() {
            Page<PessoaDto> pageMock = mock(Page.class);

            when(pessoaService.list(0, 10, new String[]{"nome"})).thenReturn(pageMock);

            ResponseEntity<Page<PessoaDto>> response = pessoaController.list(0, 10, new String[]{"nome"});

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isEqualTo(pageMock);

            InOrder inOrder = inOrder(pessoaService);
            inOrder.verify(pessoaService).list(0, 10, new String[]{"nome"});
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Nested
    class FindByIdTest {

        @Test
        void testFindById() {
            UUID id = UUID.randomUUID();
            Pessoa pessoa = new Pessoa();

            when(pessoaService.findById(id)).thenReturn(Optional.of(pessoa));

            ResponseEntity<Optional<Pessoa>> response = pessoaController.findById(id);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isEqualTo(Optional.of(pessoa));

            InOrder inOrder = inOrder(pessoaService);
            inOrder.verify(pessoaService).findById(id);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Nested
    class ProcessReportTest {

        @Test
        void testProcessReportSuccess() throws IOException {
            String filePath = "src/";
            ReportDto reportDto = new ReportDto(filePath);

            when(pessoaService.generateCsvReport()).thenReturn(CompletableFuture.completedFuture(null));

            ResponseEntity<String> response = pessoaController.processReport();

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isEqualTo("Relatório está sendo gerado em segundo plano.");

            InOrder inOrder = inOrder(pessoaService);
            inOrder.verify(pessoaService).generateCsvReport();
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        void testProcessReportFailure() throws IOException {
            String filePath = "src/";
            ReportDto reportDto = new ReportDto(filePath);

            when(pessoaService.generateCsvReport()).thenThrow(new IOException("Erro ao gerar arquivo"));

            ResponseEntity<String> response = pessoaController.processReport();

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            Assertions.assertThat(response.getBody()).isEqualTo("Erro ao gerar relatório: Erro ao gerar arquivo");

            InOrder inOrder = inOrder(pessoaService);
            inOrder.verify(pessoaService).generateCsvReport();
            inOrder.verifyNoMoreInteractions();
        }
    }
}
