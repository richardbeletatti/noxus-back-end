package com.noxus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.noxus.dto.WhatsappWebhookDTO;
import com.noxus.service.WebhookService;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping
    public ResponseEntity<Void> webhook(@RequestBody WhatsappWebhookDTO payload) {
        if (payload.entry != null) {
            for (var e : payload.entry) {
                for (var c : e.changes) {
                    var value = c.value;
                    if (value.messages != null) {
                        for (var msg : value.messages) {
                            String telefone = value.contacts.get(0).wa_id;
                            String texto = msg.text != null ? msg.text.body : "";
                            webhookService.processarMensagemRecebida(telefone, texto);
                        }
                    }
                }
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
                                                @RequestParam("hub.verify_token") String verifyToken,
                                                @RequestParam("hub.challenge") String challenge) {

        if ("subscribe".equals(mode) && "meu-token-secreto".equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
