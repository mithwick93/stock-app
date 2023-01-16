package com.mithwick93.stocks.controller;

import com.mithwick93.stocks.Constants;
import com.mithwick93.stocks.controller.dto.StockRequestDto;
import com.mithwick93.stocks.controller.dto.StockResponseDto;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Stocks REST controller
 *
 * @author mithwick93
 */
@Tag(name = "Stocks", description = "CRUD operations")
@RestController
@RequestMapping(value = "/api/v1/stocks")
public class StockController {
    private final StockService stockService;
    private final StockMapper stockMapper;
    private final PagedResourcesAssembler<Stock> stockPagedResourcesAssembler;

    @Autowired
    public StockController(StockService stockService, StockMapper stockMapper, PagedResourcesAssembler<Stock> stockPagedResourcesAssembler) {
        this.stockService = stockService;
        this.stockMapper = stockMapper;
        this.stockPagedResourcesAssembler = stockPagedResourcesAssembler;
    }

    /**
     * Returns list of all {@link StockResponseDto}s.
     *
     * @param page Page number. Default is 0.
     * @param size Size per page. Default is 10.
     * @return List of all {@link StockResponseDto}s.
     */
    @Operation(summary = "Get all stocks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the Stocks",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            )
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<PagedModel<StockResponseDto>> getStocks(
            @Parameter(description = "0-index page number. Default is 0") @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @Parameter(description = "Size of a page. Default is 10") @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int size
    ) {
        Page<Stock> stocksPages = stockService.findAllStocks(page, size);
        PagedModel<StockResponseDto> stockResponseDtos = stockPagedResourcesAssembler.toModel(stocksPages, stockMapper);

        return ResponseEntity.ok(stockResponseDtos);
    }

    /**
     * Returns {@link StockResponseDto} by its id.
     *
     * @param id Id of stock to lookup.
     * @return {@link StockResponseDto} by its id.
     * @throws StockNotFoundException When there is no stock with such id.
     */
    @Operation(summary = "Get a stock by its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the Stock",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StockResponseDto.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            )
    })
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<StockResponseDto> getStockById(
            @Parameter(description = "Id of stock to be searched") @PathVariable Long id
    ) {
        Stock stock = stockService.findStockById(id);
        StockResponseDto stockResponseDto = stockMapper.toModel(stock);

        return ResponseEntity.ok(stockResponseDto);
    }

    /**
     * Create new stock by request.
     *
     * @param stockRequestDto {@link StockRequestDto} of new stock to add.
     * @return Newly created {@link Stock}.
     */
    @Operation(summary = "Create stock")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created new Stock",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StockResponseDto.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad stock data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            )
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<StockResponseDto> createStock(
            @Parameter(description = "New stock request") @Valid @RequestBody StockRequestDto stockRequestDto
    ) {
        Stock newStockRequest = stockMapper.toEntity(stockRequestDto);
        Stock createdStock = stockService.createStock(newStockRequest);
        StockResponseDto stockResponseDto = stockMapper.toModel(createdStock);

        return ResponseEntity
                .created(linkTo(methodOn(StockController.class).getStockById(stockResponseDto.getId())).toUri())
                .body(stockResponseDto);
    }

    /**
     * Updates given stock.
     *
     * @param id              Id of stock to update.
     * @param stockRequestDto {@link StockRequestDto} of stock to update from.
     * @return Updated {@link Stock}.
     */
    @Operation(summary = "Update stock")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated Stock",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StockResponseDto.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad stock data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            )
    })
    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<StockResponseDto> updateStock(
            @Parameter(description = "Id of stock to be updated") @PathVariable Long id,
            @Parameter(description = "Stock information to be updated") @Valid @RequestBody StockRequestDto stockRequestDto
    ) {
        Stock updateStockRequest = stockMapper.toEntity(stockRequestDto);
        Stock updatedStock = stockService.updateStock(id, updateStockRequest);
        StockResponseDto stockResponseDto = stockMapper.toModel(updatedStock);

        return ResponseEntity.ok(stockResponseDto);
    }

    /**
     * Delete a stock by id.
     *
     * @param id Id of stock to delete.
     * @return HTTP 204 response code with no body.
     */
    @Operation(summary = "Delete stock")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Deleted Stock",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
            )
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteStock(
            @Parameter(description = "Id of stock to be deleted") @PathVariable Long id
    ) {
        stockService.deleteStock(id);

        return ResponseEntity.noContent().build();
    }
}

