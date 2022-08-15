package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.contract.messages.SelectTodosQueryResult;
import de.muspellheim.todos.contract.ports.TodosRepository;
import de.muspellheim.todos.contract.ports.TodosRepositoryException;
import java.util.List;

public class SelectTodosQueryHandler {
  private final TodosRepository todosRepository;

  public SelectTodosQueryHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public SelectTodosQueryResult handle(SelectTodosQuery query) {
    try {
      var todos = todosRepository.load();
      return new SelectTodosQueryResult(todos);
    } catch (TodosRepositoryException e) {
      return new SelectTodosQueryResult(List.of());
    }
  }
}
