package de.muspellheim.todos.contract.ports;

import de.muspellheim.todos.contract.data.Todo;
import java.util.List;

public interface TodosRepository {
  List<Todo> load();

  void store(List<Todo> todos);
}
