package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.data.Todo;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

class TodoItem extends Box {
  Runnable onToggle;

  TodoItem(Todo todo) {
    super(BoxLayout.X_AXIS);
    setAlignmentX(Component.LEFT_ALIGNMENT);
    setMaximumSize(new Dimension(Short.MAX_VALUE, 0));

    JCheckBox completed = new JCheckBox("", todo.completed());
    completed.addActionListener(e -> onToggle.run());
    add(completed);

    JLabel title = new JLabel(todo.title());
    setMaximumSize(new Dimension(Short.MAX_VALUE, 0));
    add(title);
  }
}
