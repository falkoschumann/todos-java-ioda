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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TodosController {
  public Consumer<AddTodoCommand> onAddTodoCommand;
  public Consumer<ClearCompletedCommand> onClearCompletedCommand;
  public Consumer<DestroyTodoCommand> onDestroyTodoCommand;
  public Consumer<SaveTodoCommand> onSaveTodoCommand;
  public Consumer<ToggleAllCommand> onToggleAllCommand;
  public Consumer<ToggleTodoCommand> onToggleTodoCommand;
  public Consumer<SelectTodosQuery> onSelectTodosQuery;

  private final TodosModel model = new TodosModel();

  @FXML private Stage stage;
  @FXML private CheckBox toggleAll;
  @FXML private TextField newTodo;
  @FXML private ListView<Todo> todoList;
  @FXML private Pane footer;
  @FXML private TextFlow itemsLeft;
  @FXML private ChoiceBox<Filter> filter;
  @FXML private Button clearCompleted;

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
      throw new RuntimeException("Could not load view from " + viewPath + ".", e);
    }
  }

  @FXML
  private void initialize() {
    //
    // Build
    //
    todoList.setCellFactory(v -> createTodoCell());

    filter.getItems().addAll(Filter.values());
    filter.setValue(Filter.ALL);

    //
    // Bind
    //
    toggleAll.visibleProperty().bind(model.existsTodosProperty());
    model.allCompletedProperty().addListener(e -> toggleAll.setSelected(model.isAllCompleted()));
    todoList.visibleProperty().bind(model.existsTodosProperty());
    todoList.setItems(model.getShownTodos());
    footer.visibleProperty().bind(model.existsTodosProperty());
    model
        .activeCountProperty()
        .addListener(
            e -> {
              var text1 = (Text) itemsLeft.getChildren().get(0);
              text1.setText(String.valueOf(model.getActiveCount()));
              var text2 = (Text) itemsLeft.getChildren().get(1);
              text2.setText(Strings.pluralize(model.getActiveCount(), " item") + " left");
            });
    filter.setOnAction(e -> model.setFilter(filter.getValue()));
    clearCompleted.visibleProperty().bind(model.existsCompletedProperty());
  }

  private ListCell<Todo> createTodoCell() {
    TodoCell cell = new TodoCell();
    cell.onDestroy = id -> onDestroyTodoCommand.accept(new DestroyTodoCommand(id));
    cell.onSave = (id, title) -> onSaveTodoCommand.accept(new SaveTodoCommand(id, title));
    cell.onToggle = id -> onToggleTodoCommand.accept(new ToggleTodoCommand(id));
    return cell;
  }

  public void run() {
    stage.show();
    newTodo.requestFocus();
    onSelectTodosQuery.accept(new SelectTodosQuery());
  }

  public void display(SelectTodosQueryResult result) {
    model.setTodos(result.todos());
  }

  public void showError(String message) {
    var alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Unexpected error");
    alert.setHeaderText("Unexpected error");
    alert.setContentText(message);
    alert.show();
  }

  @FXML
  private void handleToggleAll() {
    onToggleAllCommand.accept(new ToggleAllCommand(toggleAll.isSelected()));
  }

  @FXML
  private void handleNewTodo() {
    var title = newTodo.getText().trim();
    if (title.isEmpty()) {
      return;
    }

    onAddTodoCommand.accept(new AddTodoCommand(newTodo.getText()));
    newTodo.setText("");
  }

  @FXML
  private void handleClearCompleted() {
    onClearCompletedCommand.accept(new ClearCompletedCommand());
  }
}
