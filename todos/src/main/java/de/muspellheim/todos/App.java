package de.muspellheim.todos;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.backend.messagehandlers.SelectTodosQueryHandlerImpl;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.frontend.TodosController;
import java.util.List;

public class App {
  public static void main(String[] args) {
    // Build
    var todosRepository = new MemoryTodosRepository();
    todosRepository.store(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)));
    var selectTodosQueryHandler = new SelectTodosQueryHandlerImpl(todosRepository);
    var todosController = new TodosController();

    // Bind
    todosController.onSelectTodos =
        q -> {
          var r = selectTodosQueryHandler.handle(q);
          todosController.display(r);
        };

    // Run
    todosController.run();
  }
}
