package ar.com.juani.melishows.exception;

import javax.persistence.PersistenceException;

public class NotAvailableSeatsLockException extends PersistenceException {

    public NotAvailableSeatsLockException(String message) {
        super(message);
    }
    
    public NotAvailableSeatsLockException(String message, Throwable th) {
    	super(message, th);
    }

    private static final long serialVersionUID = 917158393508889669L;

}
