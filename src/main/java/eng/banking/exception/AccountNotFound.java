package eng.banking.exception;

public class AccountNotFound extends RuntimeException{

    public AccountNotFound(String message) {

        super(message);
    }
}
