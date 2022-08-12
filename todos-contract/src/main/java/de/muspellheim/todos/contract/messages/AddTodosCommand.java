package de.muspellheim.todos.contract.messages;

import java.util.Objects;

public record AddTodosCommand(String title) {
  public AddTodosCommand {
    Objects.requireNonNull(title, "title");
  }
}
