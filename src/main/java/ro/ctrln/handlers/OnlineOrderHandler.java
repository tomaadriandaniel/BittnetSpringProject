package ro.ctrln.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.ctrln.exceptions.*;

@ControllerAdvice
public class OnlineOrderHandler {

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<String> handleInvalidProductIdException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product ID is not valid!");
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<String> handleInvalidQuantityException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity must be positive!");
    }

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<String> handleNotEnoughStockException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough stock!");
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<String> handleInvalidProductException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The order has no products!");
    }

    @ExceptionHandler(InvalidOrderIdException.class)
    public ResponseEntity<String> handleInvalidOrderIdException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No order ID is present!");
    }

    @ExceptionHandler(OrderCancelledException.class)
    public ResponseEntity<String> handleOrderCancelledException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This order has been cancelled!");
    }

    @ExceptionHandler(OrderDeliveredException.class)
    public ResponseEntity<String> handleOrderDeliveredException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This order has been delivered!");
    }

    @ExceptionHandler(OrderNotYetDeliveredException.class)
    public ResponseEntity<String> handleOrderNotYetDeliveredException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This order has not been delivered yet!");
    }

}
