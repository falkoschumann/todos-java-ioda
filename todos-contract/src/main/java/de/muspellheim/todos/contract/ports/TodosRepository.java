package de.muspellheim.todos.contract.ports;

import de.muspellheim.todos.contract.data.Todo;
import java.util.List;

public interface TodosRepository {
  List<Todo> load() throws TodosRepositoryException;

  void store(List<Todo> todos) throws TodosRepositoryException;
}
