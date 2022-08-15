package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.messages.ToggleAllCommand;
import de.muspellheim.todos.contract.ports.TodosRepository;
import java.util.List;

public class ToggleAllCommandHandler {
  private final TodosRepository todosRepository;

  public ToggleAllCommandHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public CommandStatus handle(ToggleAllCommand command) {
    var todos = todosRepository.load();
    todos = toggleAll(todos, command.checked());
    todosRepository.store(todos);
    return new Success();
  }

  private List<Todo> toggleAll(List<Todo> todos, boolean checked) {
    return todos.stream().map(t -> new Todo(t.id(), t.title(), checked)).toList();
  }
}
