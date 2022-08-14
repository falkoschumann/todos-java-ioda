package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.AddTodoCommand;
import de.muspellheim.todos.contract.messages.DestroyTodoCommand;
import de.muspellheim.todos.contract.messages.SaveTodoCommand;
import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.contract.messages.SelectTodosQueryResult;
import de.muspellheim.todos.contract.messages.ToggleTodoCommand;
import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class TodosController {
  public Consumer<AddTodoCommand> onAddTodo;
  public Consumer<DestroyTodoCommand> onDestroyTodo;
  public Consumer<SaveTodoCommand> onSaveTodo;
  public Consumer<ToggleTodoCommand> onToggleTodo;
  public Consumer<SelectTodosQuery> onSelectTodos;

  private final JFrame frame;
  private final TodoList todoList;
  private final Footer footer;

  private SelectTodosQueryResult selectedTodos;

  public TodosController() {
    frame = new JFrame("Todos");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(320, 640);
    frame.setLocationByPlatform(true);
    // alternatively, center window
    // frame.setLocationRelativeTo(null);

    var container = new JPanel();
    container.setLayout(new BorderLayout());
    frame.add(container);

    var header = new Header();
    header.onAddTodo = t -> onAddTodo.accept(new AddTodoCommand(t));
    container.add(header, BorderLayout.NORTH);

    todoList = new TodoList();
    todoList.onDestroy = id -> onDestroyTodo.accept(new DestroyTodoCommand(id));
    todoList.onToggle = id -> onToggleTodo.accept(new ToggleTodoCommand(id));
    container.add(new JScrollPane(todoList), BorderLayout.CENTER);

    footer = new Footer();
    footer.onFilterChanged = f -> updateTodoList();
    container.add(footer, BorderLayout.SOUTH);
  }

  public void display(SelectTodosQueryResult result) {
    selectedTodos = result;
    updateTodoList();
  }

  private void updateTodoList() {
    var activeTodos = selectedTodos.todos().stream().filter(t -> !t.completed()).toList();
    var completedTodos = selectedTodos.todos().stream().filter(Todo::completed).toList();
    var allTodos = selectedTodos.todos();
    var todos =
        switch (footer.getFilter()) {
          case ACTIVE -> activeTodos;
          case COMPLETED -> completedTodos;
          default -> allTodos;
        };
    todoList.setTodos(todos);
    footer.setActiveCount(activeTodos.size());
  }

  public void run() {
    frame.setVisible(true);
    onSelectTodos.accept(new SelectTodosQuery());
  }
}
