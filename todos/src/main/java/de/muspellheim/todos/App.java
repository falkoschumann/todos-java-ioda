package de.muspellheim.todos;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.backend.messagehandlers.AddTodoCommandHandler;
import de.muspellheim.todos.backend.messagehandlers.SelectTodosQueryHandler;
import de.muspellheim.todos.backend.messagehandlers.ToggleTodoCommandHandler;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.frontend.TodosController;
import java.util.List;

public class App {
  public static void main(String[] args) {
    // Build
    var todosRepository = new MemoryTodosRepository();
    todosRepository.store(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)));
    var addTodoCommandHandler = new AddTodoCommandHandler(todosRepository);
    var toggleTodoCommandHandler = new ToggleTodoCommandHandler(todosRepository);
    var selectTodosQueryHandler = new SelectTodosQueryHandler(todosRepository);
    var todosController = new TodosController();

    // Bind
    todosController.onAddTodo =
        c -> {
          addTodoCommandHandler.handle(c);
          var r = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(r);
        };
    todosController.onToggleTodo =
        c -> {
          toggleTodoCommandHandler.handle(c);
          var r = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(r);
        };
    todosController.onSelectTodos =
        q -> {
          var r = selectTodosQueryHandler.handle(q);
          todosController.display(r);
        };

    // Run
    todosController.run();
  }
}
