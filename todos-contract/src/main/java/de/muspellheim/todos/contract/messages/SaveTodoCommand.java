package de.muspellheim.todos.contract.messages;

import java.util.Objects;

public record SaveTodoCommand(int id, String title) {
  public SaveTodoCommand {
    Objects.requireNonNull(title, "title");
  }
}
