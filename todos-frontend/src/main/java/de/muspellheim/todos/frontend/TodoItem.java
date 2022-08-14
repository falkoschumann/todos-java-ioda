package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.data.Todo;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class TodoItem extends JPanel {
  Runnable onDestroy;
  Consumer<String> onSave;
  Runnable onToggle;

  private final CardLayout layout;
  private final JComponent view;
  private final JComponent edit;

  TodoItem(Todo todo) {
    setOpaque(false);
    layout = new CardLayout();
    setLayout(layout);

    view = createView(todo);
    add(view, "VIEW");

    edit = createEditor(todo);
    add(edit, "EDIT");
  }

  private JComponent createView(Todo todo) {
    var box = Box.createHorizontalBox();
    box.setAlignmentX(Component.LEFT_ALIGNMENT);
    box.setMaximumSize(new Dimension(Short.MAX_VALUE, 0));

    var completed = new JCheckBox("", todo.completed());
    completed.addActionListener(e -> onToggle.run());
    box.add(completed);

    var title = new JLabel(todo.title());
    setMaximumSize(new Dimension(Short.MAX_VALUE, 0));
    title.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
              layout.show(TodoItem.this, "EDIT");
              edit.requestFocus();
            }
          }
        });
    box.add(title);

    box.add(Box.createHorizontalGlue());

    var url = TodoItem.class.getResource("/images/destroy.png");
    assert url != null;
    var destroy = new JButton(new ImageIcon(url));
    destroy.addActionListener(e -> onDestroy.run());
    box.add(destroy);

    return box;
  }

  private JComponent createEditor(Todo todo) {
    JTextField textField = new JTextField(todo.title());
    textField.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              onSave.accept(textField.getText());
              layout.show(TodoItem.this, "VIEW");
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
              textField.setText(todo.title());
              layout.show(TodoItem.this, "VIEW");
            }
          }
        });
    return textField;
  }
}
