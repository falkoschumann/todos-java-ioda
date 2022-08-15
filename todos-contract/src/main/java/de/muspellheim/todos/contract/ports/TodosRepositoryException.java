package de.muspellheim.todos.contract.ports;

public class TodosRepositoryException extends RuntimeException {
  public TodosRepositoryException(String message) {
    super(message);
  }

  public TodosRepositoryException(String message, Throwable cause) {
    super(message, cause);
  }
}
