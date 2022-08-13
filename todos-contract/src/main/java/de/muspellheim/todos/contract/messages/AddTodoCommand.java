package de.muspellheim.todos.contract.messages;

import java.util.Objects;

public record AddTodoCommand(String title) {
  public AddTodoCommand {
    Objects.requireNonNull(title, "title");
  }
}
