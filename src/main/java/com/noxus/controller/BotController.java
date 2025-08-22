package com.noxus.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.noxus.dto.BotConfigDTO;
import com.noxus.dto.IntentDTO;
import com.noxus.model.BotConfig;
import com.noxus.model.Intent;
import com.noxus.model.Question;
import com.noxus.repository.BotConfigRepository;
import com.noxus.repository.IntentRepository;

@RestController
@RequestMapping("/api/bot")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BotController {

    @Autowired
    private IntentRepository intentRepository;

    @Autowired
    private BotConfigRepository botConfigRepository;

    private String normalizePhoneNumber(String number) {
        if (number == null) return null;
        String cleaned = number.replaceAll("[^\\d+]", "");
        if (!cleaned.matches("^\\+\\d{10,15}$")) {
            throw new IllegalArgumentException("Número de telefone inválido: " + number);
        }
        return cleaned;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveBotConfig(@RequestBody BotConfigDTO botConfigDTO) {
        try {
            String normalizedNumber = normalizePhoneNumber(botConfigDTO.getWhatsappNumber());
            botConfigDTO.setWhatsappNumber(normalizedNumber);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        List<Intent> savedIntents = new ArrayList<>();

        for (IntentDTO dto : botConfigDTO.getIntents()) {
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

        Optional<BotConfig> optionalConfig = botConfigRepository.findAll().stream().findFirst();

        BotConfig config = optionalConfig.orElse(new BotConfig());
        config.setWhatsappNumber(botConfigDTO.getWhatsappNumber());
        config.setGreetingMessage(botConfigDTO.getGreetingMessage());

        botConfigRepository.save(config);

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

        Optional<BotConfig> optionalConfig = botConfigRepository.findAll().stream().findFirst();

        BotConfig config = optionalConfig.orElse(new BotConfig());

        BotConfigDTO dto = new BotConfigDTO();
        dto.setIntents(intentDTOs);
        dto.setWhatsappNumber(config.getWhatsappNumber());
        dto.setGreetingMessage(config.getGreetingMessage());

        return dto;
    }
}
