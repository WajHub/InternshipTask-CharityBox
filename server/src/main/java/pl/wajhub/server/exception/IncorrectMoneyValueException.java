package pl.wajhub.server.exception;

public class IncorrectMoneyValueException extends RuntimeException {
    public IncorrectMoneyValueException(Double value) {

        super(value+" is incorrect value!");
    }
}
