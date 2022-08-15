package de.muspellheim.todos.contract.messages;

import java.util.Objects;

public record Failure(String errorMessage) implements CommandStatus {
  public Failure {
    Objects.requireNonNull(errorMessage, "errorMessage");
  }

  public static Failure of(String errorMessage, Throwable cause) {
    if (cause == null) {
      return new Failure(errorMessage);
    }

    return of(errorMessage + "\n- " + cause.getMessage(), cause.getCause());
  }
}
