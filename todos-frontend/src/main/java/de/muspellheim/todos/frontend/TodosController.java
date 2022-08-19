package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.messages.AddTodoCommand;
import de.muspellheim.todos.contract.messages.ClearCompletedCommand;
import de.muspellheim.todos.contract.messages.DestroyTodoCommand;
import de.muspellheim.todos.contract.messages.SaveTodoCommand;
import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.contract.messages.SelectTodosQueryResult;
import de.muspellheim.todos.contract.messages.ToggleAllCommand;
import de.muspellheim.todos.contract.messages.ToggleTodoCommand;
import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class TodosController {
  public Consumer<AddTodoCommand> onAddTodo;
  public Consumer<ClearCompletedCommand> onClearCompleted;
  public Consumer<DestroyTodoCommand> onDestroyTodo;
  public Consumer<SaveTodoCommand> onSaveTodo;
  public Consumer<ToggleAllCommand> onToggleAll;
  public Consumer<ToggleTodoCommand> onToggleTodo;
  public Consumer<SelectTodosQuery> onSelectTodos;

  private final TodosModel model;
  private final JFrame frame;
  private final Header header;

  static {
    try {
      var className = UIManager.getSystemLookAndFeelClassName();
      UIManager.setLookAndFeel(className);
    } catch (Exception ignore) {
    }
  }

  public TodosController() {
    //
    // Build
    //
    model = new TodosModel();
    frame = new JFrame("Todos");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(400, 600);
    frame.setLocationByPlatform(true);
    // alternatively, center window
    // frame.setLocationRelativeTo(null);

    var container = new JPanel();
    container.setLayout(new BorderLayout());
    frame.add(container);

    header = new Header(model);
    container.add(header, BorderLayout.NORTH);

    TodoList todoList = new TodoList(model);
    container.add(todoList, BorderLayout.CENTER);

    Footer footer = new Footer(model);
    container.add(footer, BorderLayout.SOUTH);

    //
    // Bind
    //
    header.onAddTodo = t -> onAddTodo.accept(new AddTodoCommand(t));
    header.onToggleAll = c -> onToggleAll.accept(new ToggleAllCommand(c));
    todoList.onDestroy = id -> onDestroyTodo.accept(new DestroyTodoCommand(id));
    todoList.onSave = (id, title) -> onSaveTodo.accept(new SaveTodoCommand(id, title));
    todoList.onToggle = id -> onToggleTodo.accept(new ToggleTodoCommand(id));
    footer.onClearCompleted = () -> onClearCompleted.accept(new ClearCompletedCommand());
    footer.onFilterChanged = model::setFilter;
  }

  public void run() {
    frame.setVisible(true);
    header.requestFocus();
    onSelectTodos.accept(new SelectTodosQuery());
  }

  public void display(SelectTodosQueryResult result) {
    model.setTodos(result.todos());
  }

  public void showError(String message) {
    JOptionPane.showMessageDialog(frame, message, "Unexpected error", JOptionPane.ERROR_MESSAGE);
  }
}
