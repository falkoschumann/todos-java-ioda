package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.contract.messages.SelectTodosQueryHandler;
import de.muspellheim.todos.contract.messages.SelectTodosQueryResult;
import de.muspellheim.todos.contract.ports.TodosRepository;

public class SelectTodosQueryHandlerImpl implements SelectTodosQueryHandler {
  private final TodosRepository todosRepository;

  public SelectTodosQueryHandlerImpl(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  @Override
  public SelectTodosQueryResult handle(SelectTodosQuery query) {
    var todos = todosRepository.load();
    return new SelectTodosQueryResult(todos);
  }
}
