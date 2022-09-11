package de.muspellheim.todos.fx;

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
import de.muspellheim.todos.frontend.fx.TodosController;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
  private AddTodoCommandHandler addTodoCommandHandler;
  private ClearCompletedCommandHandler clearCompletedCommandHandler;
  private DestroyTodoCommandHandler destroyTodoCommandHandler;
  private SaveTodoCommandHandler saveTodoCommandHandler;
  private ToggleAllCommandHandler toggleAllCommandHandler;
  private ToggleTodoCommandHandler toggleTodoCommandHandler;
  private SelectTodosQueryHandler selectTodosQueryHandler;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void init() {
    //
    // Build
    //
    var todosRepository =
        new JsonTodosRepository(Paths.get(System.getProperty("user.home"), "todos.json"));
    addTodoCommandHandler = new AddTodoCommandHandler(todosRepository);
    clearCompletedCommandHandler = new ClearCompletedCommandHandler(todosRepository);
    destroyTodoCommandHandler = new DestroyTodoCommandHandler(todosRepository);
    saveTodoCommandHandler = new SaveTodoCommandHandler(todosRepository);
    toggleAllCommandHandler = new ToggleAllCommandHandler(todosRepository);
    toggleTodoCommandHandler = new ToggleTodoCommandHandler(todosRepository);
    selectTodosQueryHandler = new SelectTodosQueryHandler(todosRepository);
  }

  @Override
  public void start(Stage primaryStage) {
    var todosController = TodosController.create(primaryStage);

    //
    // Bind
    //
    todosController.onAddTodoCommand =
        command -> {
          var status = addTodoCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onClearCompletedCommand =
        command -> {
          var status = clearCompletedCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onDestroyTodoCommand =
        command -> {
          var status = destroyTodoCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onSaveTodoCommand =
        command -> {
          var status = saveTodoCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onToggleAllCommand =
        command -> {
          var status = toggleAllCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onToggleTodoCommand =
        command -> {
          var status = toggleTodoCommandHandler.handle(command);
          if (status instanceof Failure) {
            todosController.showError(((Failure) status).errorMessage());
            return;
          }

          var result = selectTodosQueryHandler.handle(new SelectTodosQuery());
          todosController.display(result);
        };
    todosController.onSelectTodosQuery =
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
