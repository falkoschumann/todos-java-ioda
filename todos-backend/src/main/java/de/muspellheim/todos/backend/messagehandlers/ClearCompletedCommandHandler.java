package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.ClearCompletedCommand;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Failure;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.ports.TodosRepository;
import de.muspellheim.todos.contract.ports.TodosRepositoryException;
import java.util.List;

public class ClearCompletedCommandHandler {
  private final TodosRepository todosRepository;

  public ClearCompletedCommandHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public CommandStatus handle(ClearCompletedCommand command) {
    try {
      var todos = todosRepository.load();
      todos = clearCompleted(todos);
      todosRepository.store(todos);
      return new Success();
    } catch (TodosRepositoryException e) {
      return Failure.of("Could not clear completed todos.", e);
    }
  }

  private List<Todo> clearCompleted(List<Todo> todos) {
    return todos.stream().filter(t -> !t.completed()).toList();
  }
}
