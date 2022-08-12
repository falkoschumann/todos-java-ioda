package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.contract.messages.SelectTodosQueryResult;
import de.muspellheim.todos.contract.ports.TodosRepository;

public class SelectTodosQueryHandler {
  private final TodosRepository todosRepository;

  public SelectTodosQueryHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public SelectTodosQueryResult handle(SelectTodosQuery query) {
    var todos = todosRepository.load();
    return new SelectTodosQueryResult(todos);
  }
}
