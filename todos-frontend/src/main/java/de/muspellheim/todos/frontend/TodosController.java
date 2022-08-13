package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.messages.AddTodoCommand;
import de.muspellheim.todos.contract.messages.DestroyTodoCommand;
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
  private final JFrame frame;
  private final TodoList todoList;

  public Consumer<AddTodoCommand> onAddTodo;
  public Consumer<DestroyTodoCommand> onDestroyTodo;
  public Consumer<ToggleTodoCommand> onToggleTodo;
  public Consumer<SelectTodosQuery> onSelectTodos;

  public TodosController() {
    frame = new JFrame("Todos");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(320, 640);
    frame.setLocationByPlatform(true);
    // frame.setLocationRelativeTo(null); // alternatively, center window

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
  }

  public void display(SelectTodosQueryResult result) {
    todoList.setTodos(result.todos());
  }

  public void run() {
    frame.setVisible(true);
    onSelectTodos.accept(new SelectTodosQuery());
  }
}
