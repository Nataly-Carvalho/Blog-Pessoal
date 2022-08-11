package org.generation.blogPessoal.repository;

import java.util.List;

import org.generation.blogPessoal.model.TemaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TemaRepository extends JpaRepository< TemaModel, Long> {
	
	public List<TemaModel>findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);

}
