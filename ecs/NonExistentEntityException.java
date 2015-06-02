package ecs;

public class NonExistentEntityException extends Exception {
    public NonExistentEntityException() {
        super();
    }

    public NonExistentEntityException(String message) {
        super(message);
    }

    public NonExistentEntityException(Throwable cause) {
        super(cause);
    }

    public NonExistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
