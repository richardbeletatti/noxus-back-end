package com.noxus.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.noxus.model.BotConfig;
import com.noxus.model.Client;
import com.noxus.model.ConversaEstado;
import com.noxus.model.Intent;
import com.noxus.model.Question;
import com.noxus.model.Resposta;
import com.noxus.repository.BotConfigRepository;
import com.noxus.repository.ClientRepository;
import com.noxus.repository.ConversaEstadoRepository;
import com.noxus.repository.IntentRepository;
import com.noxus.repository.QuestionRepository;
import com.noxus.repository.RespostaRepository;

@Service
public class WebhookService {

    private final ClientRepository clienteRepository;
    private final ConversaEstadoRepository conversaEstadoRepository;
    private final WhatsappService whatsappService;
    private final BotInteractionService botInteractionService;
    private final IntentRepository intentRepository;
    private final BotConfigRepository botConfigRepository;
    private final QuestionRepository questionRepository;
    private final RespostaRepository respostaRepository;

    public WebhookService(ClientRepository clienteRepository,
                          ConversaEstadoRepository conversaEstadoRepository,
                          WhatsappService whatsappService,
                          BotInteractionService botInteractionService,
                          IntentRepository intentRepository,
                          BotConfigRepository botConfigRepository,
                          QuestionRepository questionRepository,
                          RespostaRepository respostaRepository) {
        this.clienteRepository = clienteRepository;
        this.conversaEstadoRepository = conversaEstadoRepository;
        this.whatsappService = whatsappService;
        this.botInteractionService = botInteractionService;
        this.intentRepository = intentRepository;
        this.botConfigRepository = botConfigRepository;
        this.questionRepository = questionRepository;
        this.respostaRepository = respostaRepository;
    }

    public void processarMensagemRecebida(String telefone, String mensagem) {
        Client cliente = clienteRepository.findByTelefone(telefone).orElse(null);
        BotConfig botConfig = botConfigRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("BotConfig não configurado"));

        List<Intent> intentsDoBot = intentRepository.findByBotConfigId(botConfig.getId());

        // 1️⃣ Cliente novo → cria e envia menu inicial
        if (cliente == null) {
            cliente = criarCliente(telefone);
            criarConversaEstadoInicial(cliente);
            enviarMenuInicial(cliente, botConfig, intentsDoBot);
            return;
        }

        // 2️⃣ Estado de conversa novo → cria e envia menu
        ConversaEstado estado = conversaEstadoRepository.findByCliente(cliente).orElse(null);
        if (estado == null) {
            criarConversaEstadoInicial(cliente);
            enviarMenuInicial(cliente, botConfig, intentsDoBot);
            return;
        }

        // 3️⃣ Se ainda não escolheu intent → trata escolha de menu
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

        // 4️⃣ Fluxo de perguntas e respostas
        List<Question> perguntas = questionRepository.findByIntentId(estado.getIntent().getId());
        int indiceAtual = estado.getIndicePerguntaAtual();

        // Salva resposta da pergunta anterior
        if (indiceAtual > 0 && indiceAtual <= perguntas.size()) {
            Question perguntaAnterior = perguntas.get(indiceAtual - 1);
            Resposta resp = new Resposta();
            resp.setConversaEstado(estado);
            resp.setQuestion(perguntaAnterior);
            resp.setResposta(mensagem.trim());
            respostaRepository.save(resp);
        }

        // Se ainda há perguntas
        if (indiceAtual < perguntas.size()) {
            Question proximaPergunta = perguntas.get(indiceAtual);
            whatsappService.sendTextMessage(cliente.getTelefone(), proximaPergunta.getText());
            estado.setIndicePerguntaAtual(indiceAtual + 1);
            conversaEstadoRepository.save(estado);
            return;
        }

        // 5️⃣ Final do fluxo
        finalizarFluxo(cliente, estado, botConfig);
    }

    private void finalizarFluxo(Client cliente, ConversaEstado estado, BotConfig botConfig) {
        List<Resposta> respostas = respostaRepository.findByConversaEstado(estado);

        StringBuilder descricao = new StringBuilder();
        for (Resposta r : respostas) {
            descricao.append(r.getQuestion().getText())
                     .append(": ")
                     .append(r.getResposta())
                     .append("\n");
        }
        descricao.append("\nTelefone: ").append(cliente.getTelefone());

        String nomeUsuario = cliente.getNome() != null ? cliente.getNome() : "Cliente sem nome";
        String intencaoEscolhida = estado.getIntent().getName();
        Long userId = botConfig.getUserId();

        botInteractionService.salvarRespostasEAdicionarCard(
            cliente.getTelefone(),
            nomeUsuario,
            intencaoEscolhida,
            descricao.toString(),
            userId
        );

        whatsappService.sendTextMessage(cliente.getTelefone(),
            "Obrigado por responder todas as perguntas. Em breve nossa equipe entrará em contato.");

        respostaRepository.deleteAll(respostas);
        estado.setIntent(null);
        estado.setIndicePerguntaAtual(0);
        conversaEstadoRepository.save(estado);
    }

    private void enviarMenuInicial(Client cliente, BotConfig botConfig, List<Intent> intents) {
        StringBuilder menu = new StringBuilder();
        menu.append(botConfig.getGreetingMessage()).append("\n\n");
        menu.append("Escolha uma opção:\n");

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
            finalizarFluxo(cliente, estado,
                botConfigRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new RuntimeException("BotConfig não configurado")));
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
