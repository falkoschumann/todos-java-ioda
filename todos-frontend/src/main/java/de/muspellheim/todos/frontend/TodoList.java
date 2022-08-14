package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.data.Todo;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;

class TodoList extends Box {
  Consumer<Integer> onDestroy;
  BiConsumer<Integer, String> onSave;
  Consumer<Integer> onToggle;

  TodoList() {
    super(BoxLayout.Y_AXIS);
  }

  void setTodos(List<Todo> todos) {
    removeAll();
    for (var t : todos) {
      var item = new TodoItem(t);
      item.onDestroy = () -> onDestroy.accept(t.id());
      item.onSave = (title) -> onSave.accept(t.id(), title);
      item.onToggle = () -> onToggle.accept(t.id());
      add(item);
    }
    add(Box.createVerticalGlue());
    revalidate();
    repaint();
  }
}
