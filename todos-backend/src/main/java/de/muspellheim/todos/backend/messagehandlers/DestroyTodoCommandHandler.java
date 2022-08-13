package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.DestroyTodoCommand;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.ports.TodosRepository;
import java.util.List;

public class DestroyTodoCommandHandler {
  private final TodosRepository todosRepository;

  public DestroyTodoCommandHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public CommandStatus handle(DestroyTodoCommand command) {
    var todos = todosRepository.load();
    todos = destroyTodo(todos, command.id());
    todosRepository.store(todos);
    return new Success();
  }

  private static List<Todo> destroyTodo(List<Todo> todos, int id) {
    return todos.stream().filter(t -> t.id() != id).toList();
  }
}
