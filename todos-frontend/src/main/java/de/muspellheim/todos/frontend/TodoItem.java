package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.data.Todo;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

class TodoItem extends Box {
  Runnable onDestroy;
  Runnable onToggle;

  TodoItem(Todo todo) {
    super(BoxLayout.X_AXIS);
    setAlignmentX(Component.LEFT_ALIGNMENT);
    setMaximumSize(new Dimension(Short.MAX_VALUE, 0));

    var completed = new JCheckBox("", todo.completed());
    completed.addActionListener(e -> onToggle.run());
    add(completed);

    var title = new JLabel(todo.title());
    setMaximumSize(new Dimension(Short.MAX_VALUE, 0));
    add(title);

    add(Box.createHorizontalGlue());

    var url = TodoItem.class.getResource("/images/destroy.png");
    assert url != null;
    var destroy = new JButton(new ImageIcon(url));
    destroy.addActionListener(e -> onDestroy.run());
    add(destroy);
  }
}
