package de.muspellheim.todos.frontend;

import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

class Header extends Box {
  Consumer<String> onAddTodo;

  Header() {
    super(BoxLayout.Y_AXIS);

    JTextField newTodo = new JTextField();
    newTodo.setToolTipText("What needs to be done?");
    newTodo.addActionListener(
        e -> {
          var title = newTodo.getText().trim();
          if (title.isEmpty()) {
            return;
          }
          onAddTodo.accept(title);
          newTodo.setText("");
        });
    add(newTodo);
  }
}
