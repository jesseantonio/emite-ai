package com.emiteai.emite_ai_backend.controller;

import com.emiteai.emite_ai_backend.configuration.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/process")
    public ResponseEntity<String> processReport() {
        try {

            rabbitTemplate.convertAndSend(RabbitConfig.REPORT_QUEUE, "GenerateReport");
            return ResponseEntity.ok("Relatório está sendo gerado em segundo plano.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar mensagem para fila: " + e.getMessage());
        }
    }
}
