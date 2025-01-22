package com.emiteai.emite_ai_backend.service;

import com.emiteai.emite_ai_backend.domain.entity.Pessoa;
import com.emiteai.emite_ai_backend.dto.PessoaDto;
import com.emiteai.emite_ai_backend.repository.PessoaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

    LocalDateTime now;
    Pessoa pessoa;
    PessoaDto pessoaDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        now = LocalDateTime.now();
        pessoa = createEntity();
        pessoaDto = createDto();
    }

    private Pessoa createEntity() {
        return Pessoa.builder()
                .id(UUID.randomUUID())
                .nome("Jessé Antônio")
                .cpf("123.456.789-00")
                .telefone("(47) 98765-4321")
                .numero("123")
                .complemento("Apto 45")
                .cep("12345-678")
                .bairro("Centro")
                .municipio("Blumenau")
                .estado("SC")
                .createdBy("Admin")
                .lastModificationBy("Admin")
                .createdAt(now)
                .lastModificationAt(now)
                .build();
    }

    private PessoaDto createDto() {
        return PessoaDto.builder()
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
                .build();
    }

    @Nested
    class SaveOrUpdateTest {

        @Spy
        @InjectMocks
        PessoaService pessoaService;

        @Mock
        PessoaRepository pessoaRepository;

        @Captor
        ArgumentCaptor<Pessoa> pessoaCaptor;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }


        @Test
        void testSave() {
            Pessoa pessoa = createEntity();
            pessoa.setId(null);

            PessoaDto pessoaDto = createDto();
            pessoaDto.setId(null);

            Mockito.when(pessoaRepository.save(pessoaCaptor.capture())).thenReturn(pessoa);

            Pessoa result = pessoaService.saveOrUpdate(pessoaDto);

            Assertions.assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(pessoa);

            InOrder inOrder = Mockito.inOrder(pessoaService, pessoaRepository);
            inOrder.verify(pessoaService).saveOrUpdate(pessoaDto);
            inOrder.verify(pessoaRepository).save(pessoaCaptor.capture());
            inOrder.verifyNoMoreInteractions();
        }
        @Test
        void testUpdate() {
            Pessoa pessoa = createEntity();
            PessoaDto pessoaDto = createDto();

            Mockito.when(pessoaRepository.save(pessoaCaptor.capture())).thenReturn(pessoa);

            Pessoa result = pessoaService.saveOrUpdate(pessoaDto);

            Assertions.assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(pessoa);

            InOrder inOrder = Mockito.inOrder(pessoaService, pessoaRepository);
            inOrder.verify(pessoaService).saveOrUpdate(pessoaDto);
            inOrder.verify(pessoaRepository).save(pessoaCaptor.capture());
            inOrder.verifyNoMoreInteractions();
        }

    }

    @Nested
    class DeleteByIdTest {

        @Spy
        @InjectMocks
        PessoaService pessoaService;

        @Mock
        PessoaRepository pessoaRepository;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testDeleteById() {
            UUID uuid = UUID.randomUUID();

            pessoaService.deleteById(uuid);

            InOrder inOrder = Mockito.inOrder(pessoaService, pessoaRepository);
            inOrder.verify(pessoaService).deleteById(uuid);
            inOrder.verify(pessoaRepository).deleteById(uuid);
            inOrder.verifyNoMoreInteractions();
        }

    }

    @Nested
    class ListTest {

        @Spy
        @InjectMocks
        PessoaService pessoaService;

        @Mock
        PessoaRepository pessoaRepository;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testListWithResults() {
            int page = 0;
            int size = 10;
            String[] sort = {"nome"};

            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

            Pessoa pessoa = createEntity();

            Page<Pessoa> pessoaPage = new PageImpl<>(List.of(pessoa));

            Mockito.when(pessoaRepository.findAll(pageable)).thenReturn(pessoaPage);

            Page<PessoaDto> result = pessoaService.list(page, size, sort);

            Assertions.assertThat(result.getContent()).hasSize(1);
            PessoaDto pessoaDto = result.getContent().get(0);
            Assertions.assertThat(pessoaDto.getNome()).isEqualTo(pessoa.getNome());
            Assertions.assertThat(pessoaDto.getCpf()).isEqualTo(pessoa.getCpf());

            InOrder inOrder = Mockito.inOrder(pessoaService, pessoaRepository);
            inOrder.verify(pessoaService).list(page, size, sort);
            inOrder.verify(pessoaRepository).findAll(pageable);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        void testListWithoutResults() {
            int page = 0;
            int size = 10;
            String[] sort = {"nome"};

            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

            Mockito.when(pessoaRepository.findAll(pageable)).thenReturn(Page.empty());

            Page<PessoaDto> result = pessoaService.list(page, size, sort);

            Assertions.assertThat(result.getContent()).isEmpty();

            InOrder inOrder = Mockito.inOrder(pessoaService, pessoaRepository);
            inOrder.verify(pessoaService).list(page, size, sort);
            inOrder.verify(pessoaRepository).findAll(pageable);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Nested
    class FindByIdTest {

        @Spy
        @InjectMocks
        PessoaService pessoaService;

        @Mock
        PessoaRepository pessoaRepository;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }


        @Test
        void testFindById() {
            UUID uuid = UUID.randomUUID();
            Pessoa pessoa = new Pessoa();

            Mockito.when(pessoaRepository.findById(uuid)).thenReturn(Optional.of(pessoa));
            Optional<Pessoa> result = pessoaService.findById(uuid);

            Assertions.assertThat(result).isEqualTo(Optional.of(pessoa));

            InOrder inOrder = Mockito.inOrder(pessoaService, pessoaRepository);
            inOrder.verify(pessoaService).findById(uuid);
            inOrder.verify(pessoaRepository).findById(uuid);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        void testNotFindById() {
            UUID uuid = UUID.randomUUID();

            Mockito.when(pessoaRepository.findById(uuid)).thenReturn(Optional.empty());
            Optional<Pessoa> result = pessoaService.findById(uuid);

            Assertions.assertThat(result).isEqualTo(Optional.empty());

            InOrder inOrder = Mockito.inOrder(pessoaService, pessoaRepository);
            inOrder.verify(pessoaService).findById(uuid);
            inOrder.verify(pessoaRepository).findById(uuid);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Nested
    class GenerateCsvReportTest {

        @Spy
        @InjectMocks
        PessoaService pessoaService;

        @Mock
        PessoaRepository pessoaRepository;

        @TempDir
        Path tempDir;

        private static final Path DIRECTORY_PATH = Paths.get("src/main/resources/relatorios");


        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @AfterEach
        public void cleanUp() throws IOException {
            Files.walk(DIRECTORY_PATH)
                    .filter(Files::isRegularFile)
                    .forEach(this::deleteFile);
        }

        @Test
        void testGenerateCsvReportWithData() throws Exception {
            Pessoa pessoa = createEntity();

            Mockito.when(pessoaRepository.findAll()).thenReturn(List.of(pessoa));

            Path directoryPath = Paths.get("" + DIRECTORY_PATH);
            CompletableFuture<Void> future = pessoaService.generateCsvReport();
            future.join();

            Assertions.assertThat(hasFilesInDirectory(directoryPath)).isTrue();

            InOrder inOrder = Mockito.inOrder(pessoaService, pessoaRepository);
            inOrder.verify(pessoaService).generateCsvReport();
            inOrder.verify(pessoaRepository).findAll();
            inOrder.verifyNoMoreInteractions();
        }

        public static boolean hasFilesInDirectory(Path directory) throws IOException {
            try (Stream<Path> paths = Files.walk(directory)) {
                return paths.anyMatch(Files::isRegularFile);
            }
        }

        private void deleteFile(Path file) {
            try {
                Files.delete(file);
                System.out.println("Arquivo deletado: " + file);
            } catch (IOException e) {
                System.err.println("Erro ao deletar arquivo: " + file);
                e.printStackTrace();
            }
        }
    }
}
