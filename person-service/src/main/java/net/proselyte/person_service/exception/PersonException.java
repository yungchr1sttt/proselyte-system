package net.proselyte.person_service.exception;

public class PersonException extends RuntimeException{

    public PersonException(String message) {
        super(message);
    }
    public PersonException(String message, Object ... args) {
        super(String.format(message, args));
    }

}
