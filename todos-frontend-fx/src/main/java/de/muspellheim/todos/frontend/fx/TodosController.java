package de.muspellheim.todos.frontend.fx;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.AddTodoCommand;
import de.muspellheim.todos.contract.messages.ClearCompletedCommand;
import de.muspellheim.todos.contract.messages.DestroyTodoCommand;
import de.muspellheim.todos.contract.messages.SaveTodoCommand;
import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.contract.messages.SelectTodosQueryResult;
import de.muspellheim.todos.contract.messages.ToggleAllCommand;
import de.muspellheim.todos.contract.messages.ToggleTodoCommand;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TodosController {
  public Consumer<AddTodoCommand> onAddTodo;
  public Consumer<ClearCompletedCommand> onClearCompleted;
  public Consumer<DestroyTodoCommand> onDestroyTodo;
  public Consumer<SaveTodoCommand> onSaveTodo;
  public Consumer<ToggleAllCommand> onToggleAll;
  public Consumer<ToggleTodoCommand> onToggleTodo;
  public Consumer<SelectTodosQuery> onSelectTodos;

  @FXML private Stage stage;
  @FXML private CheckBox toggleAll;
  @FXML private TextField newTodo;
  @FXML private ListView<Todo> todoList;
  @FXML private TextFlow itemsLeft;
  @FXML private ChoiceBox<Filter> filter;

  public static TodosController create(Stage stage) {
    var viewPath = "/fxml/todos-view.fxml";
    try {
      var url = TodosController.class.getResource(viewPath);
      Objects.requireNonNull(url, "Resource not found: " + viewPath);
      var loader = new FXMLLoader(url);
      loader.setRoot(stage);
      loader.load();
      return loader.getController();
    } catch (IOException e) {
      throw new RuntimeException("Could not load view from: " + viewPath, e);
    }
  }

  @FXML
  private void initialize() {
    filter.getItems().addAll(Filter.values());
    filter.setValue(Filter.ALL);

    newTodo.requestFocus();
  }

  public void run() {
    stage.show();
  }

  public void display(SelectTodosQueryResult result) {
    // TODO Implement me
  }

  public void showError(String message) {
    var alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Unexpected error");
    alert.setHeaderText("Unexpected error");
    alert.setContentText(message);
    alert.show();
  }
}
