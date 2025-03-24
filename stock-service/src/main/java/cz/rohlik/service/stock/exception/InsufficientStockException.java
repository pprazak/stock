package cz.rohlik.service.stock.exception;

public class InsufficientStockException extends IllegalStateException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
