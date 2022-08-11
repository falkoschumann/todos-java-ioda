package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.data.Todo;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

class TodoList extends Box {
  TodoList() {
    super(BoxLayout.Y_AXIS);
  }

  void setTodos(List<Todo> todos) {
    removeAll();
    for (var t : todos) {
      add(new JLabel(t.title()));
    }
    add(Box.createVerticalGlue());
  }
}
