package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.data.Todo;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class TodosController {
  public Consumer<AddTodoCommand> onAddTodo;
  public Consumer<ClearCompletedCommand> onClearCompleted;
  public Consumer<DestroyTodoCommand> onDestroyTodo;
  public Consumer<SaveTodoCommand> onSaveTodo;
  public Consumer<ToggleAllCommand> onToggleAll;
  public Consumer<ToggleTodoCommand> onToggleTodo;
  public Consumer<SelectTodosQuery> onSelectTodos;

  private final JFrame frame;
  private final Header header;
  private final TodoList todoList;
  private final Footer footer;

  private SelectTodosQueryResult selectedTodos;

  public TodosController() {
    //
    // Build
    //
    frame = new JFrame("Todos");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(480, 640);
    frame.setLocationByPlatform(true);
    // alternatively, center window
    // frame.setLocationRelativeTo(null);

    var container = new JPanel();
    container.setLayout(new BorderLayout());
    frame.add(container);

    header = new Header();
    container.add(header, BorderLayout.NORTH);

    todoList = new TodoList();
    container.add(new JScrollPane(todoList), BorderLayout.CENTER);

    footer = new Footer();
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
    footer.onFilterChanged = f -> update();
  }

  public void display(SelectTodosQueryResult result) {
    selectedTodos = result;
    update();
  }

  private void update() {
    var activeTodos = selectedTodos.todos().stream().filter(t -> !t.completed()).toList();
    var completedTodos = selectedTodos.todos().stream().filter(Todo::completed).toList();
    var allTodos = selectedTodos.todos();
    var todos =
        switch (footer.getFilter()) {
          case ACTIVE -> activeTodos;
          case COMPLETED -> completedTodos;
          default -> allTodos;
        };
    header.setActiveCount(activeTodos.size());
    todoList.setTodos(todos);
    footer.setActiveCount(activeTodos.size());
    footer.setCompletedCount(completedTodos.size());
  }

  public void run() {
    frame.setVisible(true);
    header.requestFocus();
    onSelectTodos.accept(new SelectTodosQuery());
  }
}
