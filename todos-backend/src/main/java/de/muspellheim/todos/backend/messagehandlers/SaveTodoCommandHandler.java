package de.muspellheim.todos.backend.messagehandlers;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Failure;
import de.muspellheim.todos.contract.messages.SaveTodoCommand;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.ports.TodosRepository;
import de.muspellheim.todos.contract.ports.TodosRepositoryException;
import java.util.ArrayList;
import java.util.List;

public class SaveTodoCommandHandler {
  private final TodosRepository todosRepository;

  public SaveTodoCommandHandler(TodosRepository todosRepository) {
    this.todosRepository = todosRepository;
  }

  public CommandStatus handle(SaveTodoCommand command) {
    try {
      var todos = todosRepository.load();
      var title = Todos.normalizeTitle(command.title());
      todos = saveTodo(todos, command.id(), title);
      todosRepository.store(todos);
      return new Success();
    } catch (TodosRepositoryException e) {
      return Failure.of(
          "Todo \"" + command.title() + "\" (" + command.id() + ") could not be saved.", e);
    }
  }

  private static List<Todo> saveTodo(List<Todo> todos, int id, String title) {
    var newTodos = new ArrayList<Todo>();
    for (var t : todos) {
      if (t.id() != id) {
        newTodos.add(t);
        continue;
      }

      if (title.isEmpty()) {
        continue;
      }

      newTodos.add(new Todo(t.id(), title, t.completed()));
    }
    return newTodos;
  }
}
