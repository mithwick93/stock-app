package com.mithwick93.stocks.service;

import com.mithwick93.stocks.dal.repository.StockRepository;
import com.mithwick93.stocks.exception.StockNotFoundException;
import com.mithwick93.stocks.modal.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service to perform business logic on {@link Stock} entities.
 *
 * @author mithwick93
 */
@Service
public class StockService {
    private StockRepository stockRepository;

    @Autowired
    public void setRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * Returns list of all {@link Stock}s.
     *
     * @return list of all {@link Stock}s.
     */
    public Page<Stock> findAllStocks(int page, int size) {
        //TODO: Revisit paging and return, add sorting by name
        Pageable pageable = PageRequest.of(page, size);

        return stockRepository.findAll(pageable);
    }

    /**
     * Returns {@link Stock} by its id.
     *
     * @param id id of stock to lookup.
     * @return {@link Stock} by its id.
     * @throws StockNotFoundException when there is no stock with such id.
     */
    public Stock findStockById(long id) {
        return stockRepository
                .findById(id)
                .orElseThrow(
                        StockNotFoundException.supplier(id)
                );
    }

    /**
     * Create new stock.
     *
     * @param stock {@link Stock} new stock to add.
     * @return created {@link Stock}.
     */
    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    /**
     * Update {@link Stock} by its id based on the request received.
     *
     * @param id    id of stock to delete.
     * @param stock new {@link Stock} information.
     * @return updated {@link Stock}.
     */
    public Stock updateStock(long id, Stock stock) {
        Stock existingStock = findStockById(id);

        existingStock.setName(stock.getName());
        existingStock.setCurrentPrice(stock.getCurrentPrice());

        return stockRepository.save(existingStock);
    }

    /**
     * Delete {@link Stock} by its id.
     *
     * @param id id of stock to delete.
     */
    public void deleteStock(long id) {
        Stock existingStock = findStockById(id);

        stockRepository.deleteById(existingStock.getId());
    }

}
