package com.bbts.automation.repository;

import com.bbts.automation.domain.TarefaAutomacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Diz ao Spring que este é o componente de acesso ao banco
public interface TarefaAutomacaoRepository extends JpaRepository<TarefaAutomacao, Long> {
    // Ao estender JpaRepository, os métodos save() e findById() são injetados automaticamente
}