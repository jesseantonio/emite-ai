package com.emiteai.emite_ai_backend.controller;

import com.emiteai.emite_ai_backend.configuration.RabbitConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @InjectMocks
    private ReportController reportController;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class ProcessReportTest {

        @Test
        void testProcessReportSuccess() {
            doNothing().when(rabbitTemplate).convertAndSend(RabbitConfig.REPORT_QUEUE, "GenerateReport");
            ResponseEntity<String> response = reportController.processReport();

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isEqualTo("Relatório está sendo gerado em segundo plano.");

            InOrder inOrder = inOrder(rabbitTemplate);
            inOrder.verify(rabbitTemplate).convertAndSend(RabbitConfig.REPORT_QUEUE, "GenerateReport");
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        void testProcessReportFailure() {
            doThrow(new RuntimeException("Erro ao enviar mensagem")).when(rabbitTemplate).convertAndSend(RabbitConfig.REPORT_QUEUE, "GenerateReport");
            ResponseEntity<String> response = reportController.processReport();

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            Assertions.assertThat(response.getBody()).isEqualTo("Erro ao enviar mensagem para fila: Erro ao enviar mensagem");

            InOrder inOrder = inOrder(rabbitTemplate);
            inOrder.verify(rabbitTemplate).convertAndSend(RabbitConfig.REPORT_QUEUE, "GenerateReport");
            inOrder.verifyNoMoreInteractions();
        }
    }
}
