package com.emiteai.emite_ai_backend.messaging;

import com.emiteai.emite_ai_backend.service.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportConsumerTest {

    @InjectMocks
    private ReportConsumer reportConsumer;

    @Mock
    private PessoaService pessoaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class ConsumeMessageTest {

        @Test
        void testConsumeMessageFailure() throws IOException {
            String message = "GenerateReport";

            reportConsumer.consumeMessage(message);

            InOrder inOrder = inOrder(pessoaService);
            inOrder.verifyNoMoreInteractions();
        }


        @Test
        void testConsumeMessageInvalidMessage() throws IOException {
            String message = "InvalidMessage";

            reportConsumer.consumeMessage(message);

            verify(pessoaService, never()).generateCsvReport();
        }
    }
}
