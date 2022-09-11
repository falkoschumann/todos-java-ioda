package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.AddTodoCommand;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Failure;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.ports.TodosRepository;
import de.muspellheim.todos.contract.ports.TodosRepositoryException;
import java.util.ArrayList;
import java.util.List;

public class AddTodoCommandHandler {
  private final TodosRepository todosRepository;

  public AddTodoCommandHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public CommandStatus handle(AddTodoCommand command) {
    try {
      var title = Todos.normalizeTitle(command.title());
      if (title.isEmpty()) {
        return new Success();
      }

      var todos = todosRepository.load();
      todos = addTodo(todos, title);
      todosRepository.store(todos);
      return new Success();
    } catch (TodosRepositoryException e) {
      return Failure.of("Todo \"" + command.title() + "\" could not be added.", e);
    }
  }

  private static List<Todo> addTodo(List<Todo> todos, String title) {
    var id = todos.stream().mapToInt(Todo::id).max().orElse(0);
    id++;
    var newTodos = new ArrayList<>(todos);
    newTodos.add(new Todo(id, title, false));
    return newTodos;
  }
}
