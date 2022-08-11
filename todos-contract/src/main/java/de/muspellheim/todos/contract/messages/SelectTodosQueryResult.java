package de.muspellheim.todos.contract.messages;

import de.muspellheim.todos.contract.data.Todo;
import java.util.List;
import java.util.Objects;

public record SelectTodosQueryResult(List<Todo> todos) {
  public SelectTodosQueryResult {
    Objects.requireNonNull(todos, "todos");
  }
}
