package com.bbts.automation.service;

import com.bbts.automation.domain.TarefaAutomacao;
import com.bbts.automation.dto.AutomationEvent;
import com.bbts.automation.repository.TarefaAutomacaoRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskConsumer {

    private final TarefaAutomacaoRepository repository;
    private final ChatClient chatClient;

    public TaskConsumer(TarefaAutomacaoRepository repository, ChatClient.Builder chatClientBuilder) {
        this.repository = repository;
        this.chatClient = chatClientBuilder.build();
    }

    @Async
    @EventListener // Captura o evento internamente sem precisar de filas do RabbitMQ
    public void processarTarefaInterna(AutomationEvent event) {
        System.out.println("[EVENTO INTERNO] Capturado evento para o ID de tarefa: " + event.getTarefaId());

        TarefaAutomacao tarefa = repository.findById(event.getTarefaId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada."));

        String promptAgente = """
                Você é um agente de IA especialista em automação e triagem de processos na BBTS.
                Sua tarefa é ler a solicitação de um funcionário e extrair os dados necessários para o robô de RPA executar a ação.
                
                Retorne ESTRITAMENTE um objeto JSON válido (sem blocos de código markdown como ```json).
                O JSON deve seguir exatamente esta estrutura:
                {
                  "sistemaDestino": "Nome do sistema/plataforma mencionado",
                  "acao": "CRIAR, ALTERAR, EXCLUIR ou CONSULTAR",
                  "parametros": "Dados extraídos como nomes, CPFs, códigos ou detalhes chaves da tarefa"
                }
                
                Solicitação original: %s
                """.formatted(event.getPayload());

        long inicioToken = System.currentTimeMillis();

        // Chamada à IA local via Ollama
        String respostaIARaw = this.chatClient.prompt()
                .user(promptAgente)
                .call()
                .content();

        long fimToken = System.currentTimeMillis();
        long tempoExecucaoRobo = (fimToken - inicioToken) / 1000;

        tarefa.setJsonEstruturadoIa(respostaIARaw);
        tarefa.setStatus("PROCESSADO_IA");
        tarefa.setDataConclusao(LocalDateTime.now());
        tarefa.setTempoExecucaoRoboSegundos(tempoExecucaoRobo);

        repository.save(tarefa);
        System.out.println("[IA LOCAL] Resposta estruturada gravada no H2 para o ID: " + tarefa.getId());
        System.out.println("[RESULTADO JSON]:\n" + respostaIARaw);
    }
}