package de.muspellheim.todos.backend.adapters;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.ports.TodosRepository;
import java.util.List;

public class MemoryTodosRepository implements TodosRepository {
  private List<Todo> todos;

  public MemoryTodosRepository() {
    this(List.of());
  }

  public MemoryTodosRepository(List<Todo> todos) {
    store(todos);
  }

  @Override
  public final List<Todo> load() {
    return todos;
  }

  @Override
  public final void store(List<Todo> todos) {
    this.todos = List.copyOf(todos);
  }
}
