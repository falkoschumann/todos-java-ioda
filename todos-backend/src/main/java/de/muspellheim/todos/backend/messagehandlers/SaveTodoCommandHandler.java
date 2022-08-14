package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.SaveTodoCommand;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.ports.TodosRepository;
import java.util.ArrayList;
import java.util.List;

public class SaveTodoCommandHandler {
  private final TodosRepository todosRepository;

  public SaveTodoCommandHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public CommandStatus handle(SaveTodoCommand command) {
    var todos = todosRepository.load();
    todos = saveTodo(todos, command.id(), command.title());
    todosRepository.store(todos);
    return new Success();
  }

  private static List<Todo> saveTodo(List<Todo> todos, int id, String title) {
    var newTodos = new ArrayList<Todo>();
    for (var t : todos) {
      if (t.id() != id) {
        newTodos.add(t);
        continue;
      }

      var newTitle = title.trim();
      if (newTitle.isEmpty()) {
        continue;
      }

      newTodos.add(new Todo(t.id(), newTitle, t.completed()));
    }
    return newTodos;
  }
}
