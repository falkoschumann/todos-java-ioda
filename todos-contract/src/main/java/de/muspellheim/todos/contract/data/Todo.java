package de.muspellheim.todos.contract.data;

import java.util.Objects;

public record Todo(int id, String title, boolean completed) {
  public Todo {
    Objects.requireNonNull(title, "title must not be null");
  }
}
