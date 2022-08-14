package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.ClearCompletedCommand;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.ports.TodosRepository;
import java.util.List;

public class ClearCompletedCommandHandler {
  private final TodosRepository todosRepository;

  public ClearCompletedCommandHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public CommandStatus handle(ClearCompletedCommand command) {
    var todos = todosRepository.load();
    todos = clearCompleted(todos);
    todosRepository.store(todos);
    return new Success();
  }

  private List<Todo> clearCompleted(List<Todo> todos) {
    return todos.stream().filter(t -> !t.completed()).toList();
  }
}
