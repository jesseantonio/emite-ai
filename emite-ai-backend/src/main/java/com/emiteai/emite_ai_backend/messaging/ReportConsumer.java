package com.emiteai.emite_ai_backend.messaging;

import com.emiteai.emite_ai_backend.configuration.RabbitConfig;
import com.emiteai.emite_ai_backend.service.PessoaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReportConsumer {

    private final PessoaService pessoaService;

    public ReportConsumer(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @RabbitListener(queues = RabbitConfig.REPORT_QUEUE)
    public void consumeMessage(String message) {
        try {
            if ("GenerateReport".equals(message)) {
                pessoaService.generateCsvReport().join();
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
        }
    }
}
