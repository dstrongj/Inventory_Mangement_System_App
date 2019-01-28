package Exceptions;

/**
 * exception handling class. This is used when a parts and products dont
 * meet the necessary requirement while being initalized or changed
 *
 * @author daniel strong <dstron7@wgu.edu>
 */
public class ValidationException extends Exception {
    /**
     * Constructor to pass message to handler
     * 
     */
    public ValidationException(String message) {
        super(message);
    }
}
