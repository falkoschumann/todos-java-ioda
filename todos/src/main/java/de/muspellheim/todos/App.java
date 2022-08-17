package de.muspellheim.todos;

import de.muspellheim.todos.backend.adapters.JsonTodosRepository;
import de.muspellheim.todos.backend.messagehandlers.AddTodoCommandHandler;
import de.muspellheim.todos.backend.messagehandlers.ClearCompletedCommandHandler;
import de.muspellheim.todos.backend.messagehandlers.DestroyTodoCommandHandler;
import de.muspellheim.todos.backend.messagehandlers.SaveTodoCommandHandler;
import de.muspellheim.todos.backend.messagehandlers.SelectTodosQueryHandler;
import de.muspellheim.todos.backend.messagehandlers.ToggleAllCommandHandler;
import de.muspellheim.todos.backend.messagehandlers.ToggleTodoCommandHandler;
import de.muspellheim.todos.contract.messages.Failure;
import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.frontend.TodosController;
import java.nio.file.Paths;

public class App {
  // TODO Extract request handler into backend for reuse in App FX and App SWT

  public static void main(String[] args) {
    //
    // Build
    //
    var todosRepository = new JsonTodosRepository(Paths.get("todos.json"));
    var addTodoCommandHandler = new AddTodoCommandHandler(todosRepository);
    var clearCompletedCommandHandler = new ClearCompletedCommandHandler(todosRepository);
    var destroyTodoCommandHandler = new DestroyTodoCommandHandler(todosRepository);
    var saveTodoCommandHandler = new SaveTodoCommandHandler(todosRepository);
    var toggleAllCommandHandler = new ToggleAllCommandHandler(todosRepository);
    var toggleTodoCommandHandler = new ToggleTodoCommandHandler(todosRepository);
    var selectTodosQueryHandler = new SelectTodosQueryHandler(todosRepository);
    var todosController = new TodosController();

    //
    // Bind
    //
    todosController.onAddTodo =
        command -> {
          var status = addTodoCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onClearCompleted =
        command -> {
          var status = clearCompletedCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onDestroyTodo =
        command -> {
          var status = destroyTodoCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onSaveTodo =
        command -> {
          var status = saveTodoCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onToggleAll =
        command -> {
          var status = toggleAllCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onToggleTodo =
        command -> {
          var status = toggleTodoCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onSelectTodos =
        query -> {
          var result = selectTodosQueryHandler.handle(query);
          todosController.display(result);
        };

    //
    // Run
    //
    todosController.run();
  }
}
