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

  private final TodosModel model;
  private final Box container;

  TodoList(TodosModel model) {
    //
    // Build
    //
    this.model = model;
    container = Box.createVerticalBox();
    container.add(Box.createVerticalGlue());
    setViewportView(container);

    //
    // Bind
    //
    model.addChangeListener(e -> handleStateChanged());
  }

  private void handleStateChanged() {
    setVisible(model.existsTodos());

    List<Todo> todos = model.shownTodos();
    for (var i = 0; i < todos.size(); i++) {
      var todo = todos.get(i);
      var found = false;
      // Component count - 1 preserve glue as last component.
      for (var j = i; j < container.getComponentCount() - 1; j++) {
        var item = (TodoItem) container.getComponent(j);
        if (todo.id() == item.getTodo().id()) {
          found = true;
          item.setTodo(todo);
          if (i != j) {
            // Item's index has been changed, so swap items.
            container.add(container.getComponent(i), j);
          } // Else item's index has not changed.
          break;
        }
      }
      if (!found) {
        // Add Item.
        var item = new TodoItem(todo);
        item.onDestroy = () -> onDestroy.accept(todo.id());
        item.onSave = (title) -> onSave.accept(todo.id(), title);
        item.onToggle = () -> onToggle.accept(todo.id());
        container.add(item, i);
      }
    }

    // Remove unused items, but preserve glue as last component.
    while (container.getComponentCount() - 1 > todos.size()) {
      container.remove(container.getComponentCount() - 2);
    }

    container.revalidate();
    container.repaint();
  }
}
