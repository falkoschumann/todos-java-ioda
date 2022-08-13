package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.AddTodoCommand;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.ports.TodosRepository;
import java.util.ArrayList;
import java.util.List;

public class AddTodoCommandHandler {
  private final TodosRepository todosRepository;

  public AddTodoCommandHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public CommandStatus handle(AddTodoCommand command) {
    var title = command.title().trim();
    if (title.isEmpty()) {
      return new Success();
    }

    var todos = todosRepository.load();
    var id = getNextId(todos);
    todos = addTodo(todos, id, title);
    todosRepository.store(todos);
    return new Success();
  }

  private static int getNextId(List<Todo> todos) {
    var id = todos.stream().mapToInt(Todo::id).max().orElse(0);
    id++;
    return id;
  }

  private static List<Todo> addTodo(List<Todo> todos, int id, String title) {
    var newTodos = new ArrayList<>(todos);
    newTodos.add(new Todo(id, title, false));
    return newTodos;
  }
}
