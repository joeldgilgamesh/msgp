package com.sprint.minfi.msgp.repository;

import com.sprint.minfi.msgp.domain.Transaction;
import com.sprint.minfi.msgp.service.dto.TransactionDTO;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	TransactionDTO findByCodeTransaction(String codeTransaction);

}
