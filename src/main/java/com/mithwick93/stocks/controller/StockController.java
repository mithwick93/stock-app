package com.mithwick93.stocks.controller;

import com.mithwick93.stocks.Constants;
import com.mithwick93.stocks.controller.dto.StockDto;
import com.mithwick93.stocks.controller.mapper.StockMapper;
import com.mithwick93.stocks.exception.StockNotFoundException;
import com.mithwick93.stocks.modal.Stock;
import com.mithwick93.stocks.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Stocks REST controller
 *
 * @author mithwick93
 */
@RestController
@RequestMapping(value = "/api/v1/stocks")
public class StockController {
    private StockService stockService;
    private StockMapper stockMapper;

    @Autowired
    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    @Autowired
    public void setStockMapper(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    /**
     * Returns list of all {@link Stock}s.
     *
     * @param page page number. Default is 0.
     * @param size size per page. Default is 10.
     * @return list of all {@link Stock}s.
     */
    @Operation(summary = "Get all stocks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the Stocks",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))}// TODO: type of object
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = @Content
            )
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<StockDto>> getStocks(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int size
    ) {
        //TODO: Add page info to the response
        Page<Stock> stocksPages = stockService.findAllStocks(page, size);

        List<Stock> stocks = stocksPages.getContent();
        List<StockDto> stocksResponse = stocks
                .stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(stocksResponse);
    }

    /**
     * Returns {@link Stock} by its id.
     *
     * @param id id of stock to lookup.
     * @return {@link Stock} by its id.
     * @throws StockNotFoundException when there is no stock with such id.
     */
    @Operation(summary = "Get a stock by its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the Stock",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = @Content
            )
    })
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<StockDto> getStockById(
            @Parameter(description = "id of stock to be searched") @PathVariable Long id
    ) {
        Stock stock = stockService.findStockById(id);
        StockDto stockResponse = stockMapper.toDto(stock);

        return ResponseEntity.ok(stockResponse);
    }

    /**
     * Create new stock by request.
     *
     * @param stockDto {@link StockDto} of new stock to add.
     * @return newly created {@link Stock}.
     */
    @Operation(summary = "Create stock")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created new Stock",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad stock data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = @Content
            )
    })
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<StockDto> createStock(
            @Parameter(description = "new stock request") @Valid @RequestBody StockDto stockDto
    ) {
        Stock newStockRequest = stockMapper.toModal(stockDto);
        System.out.println(newStockRequest);
        Stock stock = stockService.createStock(newStockRequest);
        StockDto stockResponse = stockMapper.toDto(stock);

        return ResponseEntity
                .created(linkTo(methodOn(StockController.class).getStockById(stockResponse.getId())).toUri())
                .body(stockResponse);
    }

    /**
     * Updates given stock.
     *
     * @param id       id of stock to update.
     * @param stockDto {@link StockDto} of stock to update from.
     * @return updated {@link Stock}.
     */
    @Operation(summary = "Update stock")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated Stock",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad stock data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = @Content
            )
    })
    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<StockDto> updateStock(
            @Parameter(description = "id of stock to be updated") @PathVariable Long id,
            @Parameter(description = "stock information to be updated") @Valid @RequestBody StockDto stockDto
    ) {
        Stock updateStockRequest = stockMapper.toModal(stockDto);
        Stock updatedStock = stockService.updateStock(id, updateStockRequest);
        StockDto stockResponse = stockMapper.toDto(updatedStock);

        return ResponseEntity.ok(stockResponse);
    }

    /**
     * Delete a stock by id.
     *
     * @param id id of stock to delete.
     * @return Void
     */
    @Operation(summary = "Delete stock")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Deleted Stock",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = @Content
            )
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteStock(
            @Parameter(description = "id of stock to be deleted") @PathVariable Long id
    ) {
        stockService.deleteStock(id);

        return ResponseEntity.noContent().build();
    }
}

