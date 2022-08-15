package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Failure;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.messages.ToggleAllCommand;
import de.muspellheim.todos.contract.ports.TodosRepository;
import de.muspellheim.todos.contract.ports.TodosRepositoryException;
import java.util.List;

public class ToggleAllCommandHandler {
  private final TodosRepository todosRepository;

  public ToggleAllCommandHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public CommandStatus handle(ToggleAllCommand command) {
    try {
      var todos = todosRepository.load();
      todos = toggleAll(todos, command.checked());
      todosRepository.store(todos);
      return new Success();
    } catch (TodosRepositoryException e) {
      return Failure.of(
          "Could not set all todos as " + (command.checked() ? "completed" : "active") + ".", e);
    }
  }

  private List<Todo> toggleAll(List<Todo> todos, boolean checked) {
    return todos.stream().map(t -> new Todo(t.id(), t.title(), checked)).toList();
  }
}
