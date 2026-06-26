package com.bbts.automation.controller;

import com.bbts.automation.domain.TarefaAutomacao;
import com.bbts.automation.dto.AutomationEvent;
import com.bbts.automation.repository.TarefaAutomacaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/automations")
@Tag(name = "Orquestrador de Automação Corporativa", description = "Endpoints para envio e triagem cognitiva de processos operacionais")
public class AutomationController {

    private final ApplicationEventPublisher eventPublisher;
    private final TarefaAutomacaoRepository repository;

    public AutomationController(ApplicationEventPublisher eventPublisher, TarefaAutomacaoRepository repository) {
        this.eventPublisher = eventPublisher;
        this.repository = repository;
    }

    @PostMapping(value = "/enviar", consumes = "text/plain", produces = "text/plain")
    @Operation(
            summary = "Submeter solicitação operacional em texto livre",
            description = "Recebe uma demanda textual não estruturada, persiste o registro inicial e dispara o processamento cognitivo assíncrono via IA local."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Requisição aceita com sucesso. O processamento foi delegado para background."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ou falha na orquestração do barramento de eventos.")
    })
    public ResponseEntity<String> receberTarefaManual(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Texto livre descrevendo a tarefa (Ex: solicitações de cadastro, acessos, manutenção)",
                    required = true
            )
            @RequestBody String descricaoTarefa
    ) {
        TarefaAutomacao tarefa = new TarefaAutomacao();
        tarefa.setDescricaoOriginal(descricaoTarefa);
        tarefa.setStatus("PENDENTE");
        tarefa.setDataCriacao(LocalDateTime.now());
        tarefa.setTempoEstimadoManualMinutos(15L);

        tarefa = repository.save(tarefa);

        // Dispara o evento interno de forma assíncrona respeitando o seu DTO
        AutomationEvent event = new AutomationEvent(this, tarefa.getId(), tarefa.getDescricaoOriginal());
        eventPublisher.publishEvent(event);

        return ResponseEntity.accepted().body("Evento gerado internamente. ID: " + tarefa.getId() + " em processamento.");
    }
}