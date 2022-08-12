package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.messages.AddTodosCommand;
import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.contract.messages.SelectTodosQueryResult;
import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class TodosController {
  private final JFrame frame;
  private final TodoList todoList;

  public Consumer<AddTodosCommand> onAddTodo;
  public Consumer<SelectTodosQuery> onSelectTodos;

  public TodosController() {
    frame = new JFrame("Todos");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setLocationByPlatform(true);
    frame.setSize(320, 640);

    var container = new JPanel();
    container.setLayout(new BorderLayout());
    frame.add(container);

    var header = new Header();
    header.onAddTodo = t -> onAddTodo.accept(new AddTodosCommand(t));
    container.add(header, BorderLayout.NORTH);

    todoList = new TodoList();
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
