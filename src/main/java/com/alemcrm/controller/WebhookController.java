package com.alemcrm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alemcrm.dto.WhatsappWebhookDTO;

@RestController
@RequestMapping("webhook")
public class WebhookController {

	@PostMapping
	public ResponseEntity<Void> webhook(@RequestBody WhatsappWebhookDTO payload){
	if (payload.entry != null) {
	    for (var e : payload.entry) {
	        for (var c : e.changes) {
	            var value = c.value;
	            if (value.messages != null) {
	                for (var msg : value.messages) {
	                    String nome = value.contacts.get(0).profile.name;
	                    String telefone = value.contacts.get(0).wa_id;
	                    String texto = msg.text != null ? msg.text.body : "";
	                    System.out.println("Mensagem recebida de " + nome + ": " + texto);
	                    // aqui você salva no banco se quiser
	                }
	            }
	        }
	    }
	  }
	return null;
	}

	@GetMapping
	public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
			@RequestParam("hub.verify_token") String verifyToken, @RequestParam("hub.challenge") String challenge) {

		if ("subscribe".equals(mode) && "meu-token-secreto".equals(verifyToken)) {
			return ResponseEntity.ok(challenge);
		} else {
			return ResponseEntity.status(403).build();
		}
	}

}
