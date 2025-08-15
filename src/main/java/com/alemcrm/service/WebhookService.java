package com.alemcrm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alemcrm.model.BotConfig;
import com.alemcrm.model.Client;
import com.alemcrm.model.ConversaEstado;
import com.alemcrm.model.Intent;
import com.alemcrm.model.Question;
import com.alemcrm.repository.BotConfigRepository;
import com.alemcrm.repository.ClientRepository;
import com.alemcrm.repository.ConversaEstadoRepository;
import com.alemcrm.repository.IntentRepository;
import com.alemcrm.repository.QuestionRepository;

@Service
public class WebhookService {

    private final ClientRepository clienteRepository;
    private final ConversaEstadoRepository conversaEstadoRepository;
    private final WhatsappService whatsappService;
    private final IntentRepository intentRepository;
    private final BotConfigRepository botConfigRepository;
    private final QuestionRepository questionRepository;

    public WebhookService(ClientRepository clienteRepository,
                          ConversaEstadoRepository conversaEstadoRepository,
                          WhatsappService whatsappService,
                          IntentRepository intentRepository,
                          BotConfigRepository botConfigRepository,
                          QuestionRepository questionRepository) {
        this.clienteRepository = clienteRepository;
        this.conversaEstadoRepository = conversaEstadoRepository;
        this.whatsappService = whatsappService;
        this.intentRepository = intentRepository;
        this.botConfigRepository = botConfigRepository;
        this.questionRepository = questionRepository;
    }

    public void processarMensagemRecebida(String telefone, String mensagem) {
        Client cliente = clienteRepository.findByTelefone(telefone).orElse(null);
        BotConfig botConfig = botConfigRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("BotConfig não configurado"));

        // Carrega todas as intents do BotConfig
        List<Intent> intentsDoBot = intentRepository.findByBotConfigId(botConfig.getId());

        if (cliente == null) {
            cliente = criarCliente(telefone);
            criarConversaEstadoInicial(cliente);
            enviarMenuInicial(cliente, botConfig, intentsDoBot);
            return;
        }

        ConversaEstado estado = conversaEstadoRepository.findByCliente(cliente).orElse(null);
        if (estado == null) {
            criarConversaEstadoInicial(cliente);
            enviarMenuInicial(cliente, botConfig, intentsDoBot);
            return;
        }

        // Se ainda não escolheu intent, interpreta mensagem como número do menu
        if (estado.getIntent() == null) {
            try {
                int escolha = Integer.parseInt(mensagem.trim()) - 1;
                if (escolha >= 0 && escolha < intentsDoBot.size()) {
                    estado.setIntent(intentsDoBot.get(escolha));
                    estado.setIndicePerguntaAtual(0);
                    conversaEstadoRepository.save(estado);
                    enviarProximaPergunta(cliente);
                } else {
                    whatsappService.sendTextMessage(telefone, "Opção inválida. Digite um número do menu.");
                    enviarMenuInicial(cliente, botConfig, intentsDoBot);
                }
            } catch (NumberFormatException e) {
                whatsappService.sendTextMessage(telefone, "Digite apenas o número da opção desejada.");
                enviarMenuInicial(cliente, botConfig, intentsDoBot);
            }
            return;
        }

        // Continua fluxo de perguntas
        enviarProximaPergunta(cliente);
    }

    private void enviarMenuInicial(Client cliente, BotConfig botConfig, List<Intent> intents) {
        StringBuilder menu = new StringBuilder();

        // Saudação primeiro
        menu.append(botConfig.getGreetingMessage()).append("\n\n");
        menu.append("Escolha uma opção:\n");

        // Emojis para 1, 2, 3, ...
        String[] numerosEmoji = {"1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"};

        for (int i = 0; i < intents.size(); i++) {
            String emoji = (i < numerosEmoji.length) ? numerosEmoji[i] : (i + 1) + ".";
            menu.append(emoji).append(" ").append(intents.get(i).getName()).append("\n");
        }

        whatsappService.sendTextMessage(cliente.getTelefone(), menu.toString());
    }

    private void enviarProximaPergunta(Client cliente) {
        ConversaEstado estado = conversaEstadoRepository.findByCliente(cliente)
                .orElseThrow(() -> new RuntimeException("Estado de conversa não encontrado"));

        Intent intent = estado.getIntent();
        List<Question> perguntas = questionRepository.findByIntentId(intent.getId());
        int indice = estado.getIndicePerguntaAtual();

        if (indice < perguntas.size()) {
            Question pergunta = perguntas.get(indice);
            whatsappService.sendTextMessage(cliente.getTelefone(), pergunta.getText());

            estado.setIndicePerguntaAtual(indice + 1);
            conversaEstadoRepository.save(estado);
        } else {
            // Mensagem final de agradecimento
            whatsappService.sendTextMessage(cliente.getTelefone(),
                    "Obrigado por ter respondido todas as perguntas. Em breve alguém da nossa equipe entrará em contato com você.");

            // Reset do estado para nova escolha
            estado.setIntent(null);
            estado.setIndicePerguntaAtual(0);
            conversaEstadoRepository.save(estado);
        }
    }

    private Client criarCliente(String telefone) {
        Client cliente = new Client();
        cliente.setTelefone(telefone);
        clienteRepository.save(cliente);
        return cliente;
    }

    private ConversaEstado criarConversaEstadoInicial(Client cliente) {
        ConversaEstado estado = new ConversaEstado();
        estado.setCliente(cliente);
        estado.setIntent(null);
        estado.setIndicePerguntaAtual(0);
        return conversaEstadoRepository.save(estado);
    }
}
