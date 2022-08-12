package de.muspellheim.todos.contract.messages;

import java.util.Objects;

public record Failure(String errorMessage) implements CommandStatus {
  public Failure {
    Objects.requireNonNull(errorMessage, "errorMessage");
  }
}
