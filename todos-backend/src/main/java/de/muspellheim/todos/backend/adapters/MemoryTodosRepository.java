package de.muspellheim.todos.backend.adapters;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.ports.TodosRepository;
import java.util.List;

public class MemoryTodosRepository implements TodosRepository {
  private List<Todo> todos;

  @Override
  public List<Todo> load() {
    return List.copyOf(todos);
  }

  @Override
  public void store(List<Todo> todos) {
    this.todos = todos;
  }
}
