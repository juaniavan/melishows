package ar.com.juani.melishows.exception;

import javax.persistence.PersistenceException;

public class NotAvailableSeatsException extends PersistenceException {

    public NotAvailableSeatsException(String message) {
        super(message);
    }
    
    public NotAvailableSeatsException(String message, Throwable th) {
    	super(message, th);
    }

    private static final long serialVersionUID = 917158393508889669L;

}
