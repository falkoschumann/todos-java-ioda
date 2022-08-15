package de.muspellheim.todos.frontend;

import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

class Header extends Box {
  Consumer<String> onAddTodo;
  Consumer<Boolean> onToggleAll;

  private final JCheckBox toggleAll;
  private final JTextField newTodo;

  Header() {
    //
    // Build
    //
    super(BoxLayout.X_AXIS);

    // We can not place component outside clipping rect without custom clipping,
    // so we move toggle all from main to header.
    toggleAll = new JCheckBox("");
    toggleAll.setToolTipText("Mark all as complete");
    add(toggleAll);

    newTodo = new JTextField();
    // Swing has no placeholders, so we use a tooltip.
    newTodo.setToolTipText("What needs to be done?");
    add(newTodo);

    //
    // Bind
    //
    toggleAll.addActionListener(e -> onToggleAll.accept(toggleAll.isSelected()));
    newTodo.addActionListener(
        e -> {
          var title = newTodo.getText().trim();
          if (title.isEmpty()) {
            return;
          }

          onAddTodo.accept(title);
          newTodo.setText("");
        });
  }

  void setAllTodosCount(int c) {
    toggleAll.setVisible(c > 0);
  }

  void setActiveCount(int c) {
    toggleAll.setSelected(c == 0);
  }

  @Override
  public void requestFocus() {
    newTodo.requestFocus();
  }
}
