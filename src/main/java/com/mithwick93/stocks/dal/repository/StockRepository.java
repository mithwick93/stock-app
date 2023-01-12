package com.mithwick93.stocks.dal.repository;

import com.mithwick93.stocks.modal.Stock;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository for {@link Stock} entities to perform CRUD operations.
 * @author mithwick93
 */
public interface StockRepository extends PagingAndSortingRepository<Stock, Long> {
}
