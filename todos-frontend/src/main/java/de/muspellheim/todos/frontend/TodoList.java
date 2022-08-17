package de.muspellheim.todos.frontend;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.JScrollPane;

class TodoList extends JScrollPane {
  Consumer<Integer> onDestroy;
  BiConsumer<Integer, String> onSave;
  Consumer<Integer> onToggle;

  private final TodosModel model;
  private final Box container;

  TodoList(TodosModel model) {
    //
    // Build
    //
    this.model = model;
    container = Box.createVerticalBox();
    setViewportView(container);

    //
    // Bind
    //
    model.addChangeListener(e -> handleStateChanged());
  }

  private void handleStateChanged() {
    setVisible(model.existsTodos());

    // TODO update existing items if possible
    container.removeAll();
    for (var t : model.shownTodos()) {
      var item = new TodoItem(t);
      item.onDestroy = () -> onDestroy.accept(t.id());
      item.onSave = (title) -> onSave.accept(t.id(), title);
      item.onToggle = () -> onToggle.accept(t.id());
      container.add(item);
    }
    container.add(Box.createVerticalGlue());
    container.revalidate();
    container.repaint();
  }
}
