package com.alemcrm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsappService {

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;  // ex: https://graph.facebook.com/v16.0/NUMBER_ID/messages

    @Value("${whatsapp.api.token}")
    private String whatsappApiToken;

    private final WebClient webClient;

    public WhatsappService() {
        this.webClient = WebClient.builder().build();
    }

    public void sendTextMessage(String toPhoneNumber, String messageText) {
        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", toPhoneNumber);
        body.put("type", "text");
        Map<String, String> textObj = new HashMap<>();
        textObj.put("body", messageText);
        body.put("text", textObj);

        webClient.post()
                .uri(whatsappApiUrl)
                .header("Authorization", "Bearer " + whatsappApiToken)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(err -> System.err.println("Erro ao enviar WhatsApp: " + err.getMessage()))
                .subscribe(response -> System.out.println("Mensagem enviada com sucesso: " + response));
    }
}

