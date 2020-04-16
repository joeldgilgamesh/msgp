package com.sprint.minfi.msgp.repository;

import com.sprint.minfi.msgp.domain.DetailVersementIntermediaire;
import com.sprint.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DetailVersementIntermediaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailVersementIntermediaireRepository extends JpaRepository<DetailVersementIntermediaire, Long> {

	public DetailVersementIntermediaireDTO findByNumeroVersment(String numeroVersment);
}
