package eng.banking.exception;

public class NoTransactionsFoundException extends RuntimeException{


    public NoTransactionsFoundException(String message) {

        super(message);
    }
}
