package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.data.Todo;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.JScrollPane;

class TodoList extends JScrollPane {
  Consumer<Integer> onDestroy;
  BiConsumer<Integer, String> onSave;
  Consumer<Integer> onToggle;

  private final Box view;

  TodoList() {
    view = Box.createVerticalBox();
    setViewportView(view);
  }

  void setTodos(List<Todo> todos) {
    view.removeAll();
    for (var t : todos) {
      var item = new TodoItem(t);
      item.onDestroy = () -> onDestroy.accept(t.id());
      item.onSave = (title) -> onSave.accept(t.id(), title);
      item.onToggle = () -> onToggle.accept(t.id());
      view.add(item);
    }
    view.add(Box.createVerticalGlue());
    view.revalidate();
    view.repaint();
  }
}
