package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.messages.ToggleTodoCommand;
import de.muspellheim.todos.contract.ports.TodosRepository;
import java.util.List;

public class ToggleTodoCommandHandler {
  private final TodosRepository todosRepository;

  public ToggleTodoCommandHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public CommandStatus handle(ToggleTodoCommand command) {
    var todos = todosRepository.load();
    todos = toggleTodo(todos, command.id());
    todosRepository.store(todos);
    return new Success();
  }

  private static List<Todo> toggleTodo(List<Todo> todos, int id) {
    return todos.stream()
        .map(
            t -> {
              if (t.id() != id) {
                return t;
              }

              return new Todo(t.id(), t.title(), !t.completed());
            })
        .toList();
  }
}
