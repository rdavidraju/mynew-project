package com.nspl.app.repository;

import com.nspl.app.domain.JournalBatches;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JournalBatches entity.
 */
@SuppressWarnings("unused")
public interface JournalBatchesRepository extends JpaRepository<JournalBatches,Long> {

}
