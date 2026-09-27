package br.senac.tads.dsw.exemplo4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senac.tads.dsw.exemplo4.model.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

}
