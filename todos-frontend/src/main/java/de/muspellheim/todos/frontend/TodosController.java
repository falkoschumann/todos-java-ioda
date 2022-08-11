package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.contract.messages.SelectTodosQueryResult;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class TodosController {
  private final JFrame frame;
  private final TodoList todoList;

  public Delegate<SelectTodosQuery> onSelectTodos;

  public TodosController() {
    frame = new JFrame("Todos");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(320, 640);

    todoList = new TodoList();
    frame.add(new JScrollPane(todoList));
  }

  public void display(SelectTodosQueryResult result) {
    todoList.setTodos(result.todos());
  }

  public void run() {
    SwingUtilities.invokeLater(
        () -> {
          frame.setVisible(true);
          onSelectTodos.delegate(new SelectTodosQuery());
        });
  }
}
