package com.mithwick93.stocks.service;

import com.mithwick93.stocks.dal.repository.StockRepository;
import com.mithwick93.stocks.exception.StockNotFoundException;
import com.mithwick93.stocks.modal.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Stock> findAllStocks(int pageNo, int pageSize) {
        //TODO: Revisit paging and return
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return stockRepository.findAll(pageable).getContent();
    }

    /**
     * Returns {@link Stock} by its id.
     *
     * @param id id of stock to lookup.
     * @return {@link Stock} by its id.
     * @throws StockNotFoundException when there is no stock with such id.
     */
    private Stock findStockById(long id) {
        //TODO: revisit
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
    public Stock createStock(final Stock stock) {
        return stockRepository.save(stock);
    }

    /**
     * Update {@link Stock} by its id based on the request received.
     *
     * @param id    id of stock to delete.
     * @param stock new {@link Stock} information.
     * @return updated {@link Stock}.
     */
    public Stock updateStock(long id, final Stock stock) { //TODO: revisit
        final Stock existingStock = findStockById(id);

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
        stockRepository.deleteById(id);
    }

}
