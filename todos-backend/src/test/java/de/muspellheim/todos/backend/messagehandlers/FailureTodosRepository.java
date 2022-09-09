package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.ports.TodosRepository;
import de.muspellheim.todos.contract.ports.TodosRepositoryException;
import java.util.List;

public class FailureTodosRepository implements TodosRepository {
  @Override
  public List<Todo> load() throws TodosRepositoryException {
    throw new TodosRepositoryException("Something is strange.");
  }

  @Override
  public void store(List<Todo> todos) throws TodosRepositoryException {
    throw new TodosRepositoryException("Something is strange.");
  }
}
