package com.alemcrm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alemcrm.dto.BotConfigDTO;
import com.alemcrm.dto.IntentDTO;
import com.alemcrm.model.Intent;
import com.alemcrm.model.Question;
import com.alemcrm.repository.IntentRepository;

@RestController
@RequestMapping("/api/bot")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // ajuste conforme necessário
public class BotController {

    @Autowired
    private IntentRepository intentRepository;

    @PostMapping("/save")
    public ResponseEntity<?> saveBotConfig(@RequestBody BotConfigDTO botConfig) {
        List<Intent> savedIntents = new ArrayList<>();

        for (IntentDTO dto : botConfig.getIntents()) {
            Intent intent = new Intent();
            intent.setName(dto.getName());

            List<Question> questions = dto.getQuestions().stream()
                .map(text -> {
                    Question q = new Question();
                    q.setText(text);
                    q.setIntent(intent);
                    return q;
                })
                .collect(Collectors.toList());

            intent.setQuestions(questions);

            savedIntents.add(intentRepository.save(intent));
        }

        // Se quiser salvar também o número do WhatsApp e mensagem, você pode criar uma tabela pra isso
        // ou persistir em outro local. Aqui estamos só imprimindo por enquanto.
        System.out.println("Número do WhatsApp: " + botConfig.getWhatsappNumber());
        System.out.println("Mensagem de Saudação: " + botConfig.getGreetingMessage());

        return ResponseEntity.ok("Configurações salvas com sucesso!");
    }

    @GetMapping("/load")
    public BotConfigDTO loadBotConfig() {
        List<Intent> intents = intentRepository.findAll();

        List<IntentDTO> intentDTOs = intents.stream().map(intent -> {
            IntentDTO dto = new IntentDTO();
            dto.setName(intent.getName());
            dto.setQuestions(
                intent.getQuestions().stream()
                    .map(Question::getText)
                    .collect(Collectors.toList())
            );
            return dto;
        }).collect(Collectors.toList());

        BotConfigDTO config = new BotConfigDTO();
        config.setIntents(intentDTOs);
        config.setWhatsappNumber("+5511999999999"); // 🔧 Substituir com valor real (se estiver em outro lugar)
        config.setGreetingMessage("Olá! Como posso te ajudar hoje?"); // 🔧 Substituir com valor real

        return config;
    }
}