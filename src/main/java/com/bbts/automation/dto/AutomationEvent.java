package com.bbts.automation.dto;

import org.springframework.context.ApplicationEvent;

public class AutomationEvent extends ApplicationEvent {
    private final Long tarefaId;
    private final String payload;

    public AutomationEvent(Object source, Long tarefaId, String payload) {
        super(source);
        this.tarefaId = tarefaId;
        this.payload = payload;
    }

    public Long getTarefaId() {
        return tarefaId;
    }

    public String getPayload() {
        return payload;
    }
}