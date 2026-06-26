package com.bbts.automation.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas_automacao")
public class TarefaAutomacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String descricaoOriginal;

    @Column(columnDefinition = "TEXT")
    private String jsonEstruturadoIa;

    private String status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    private Long tempoEstimadoManualMinutos;
    private Long tempoExecucaoRoboSegundos;

    // Construtor Padrão exigido pelo Hibernate
    public TarefaAutomacao() {
    }

    // Getters e Settersmanuais (Sem Lombok)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricaoOriginal() {
        return descricaoOriginal;
    }

    public void setDescricaoOriginal(String descricaoOriginal) {
        this.descricaoOriginal = descricaoOriginal;
    }

    public String getJsonEstruturadoIa() {
        return jsonEstruturadoIa;
    }

    public void setJsonEstruturadoIa(String jsonEstruturadoIa) {
        this.jsonEstruturadoIa = jsonEstruturadoIa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public Long getTempoEstimadoManualMinutos() {
        return tempoEstimadoManualMinutos;
    }

    public void setTempoEstimadoManualMinutos(Long tempoEstimadoManualMinutos) {
        this.tempoEstimadoManualMinutos = tempoEstimadoManualMinutos;
    }

    public Long getTempoExecucaoRoboSegundos() {
        return tempoExecucaoRoboSegundos;
    }

    public void setTempoExecucaoRoboSegundos(Long tempoExecucaoRoboSegundos) {
        this.tempoExecucaoRoboSegundos = tempoExecucaoRoboSegundos;
    }
}