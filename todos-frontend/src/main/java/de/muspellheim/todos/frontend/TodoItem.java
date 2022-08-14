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
  private final JComponent edit;

  TodoItem(Todo todo) {
    setMaximumSize(new Dimension(Short.MAX_VALUE, 0));
    setOpaque(false);
    layout = new CardLayout();
    setLayout(layout);

    var view = createView(todo);
    add(view, "VIEW");

    edit = createEditor(todo);
    add(edit, "EDIT");
  }

  private JComponent createView(Todo todo) {
    //
    // Build
    //
    var container = Box.createHorizontalBox();
    container.setAlignmentX(Component.LEFT_ALIGNMENT);
    container.setPreferredSize(new Dimension(0, 32));

    var completed = new JCheckBox("", todo.completed());
    container.add(completed);

    var text = todo.title();
    if (todo.completed()) {
      text = "<html><strike>" + text + "</strike><html>";
    }
    var title = new JLabel(text);
    title.setMinimumSize(new Dimension(0, 32));
    title.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));
    container.add(title);

    var url = TodoItem.class.getResource("/images/destroy.png");
    assert url != null;
    var destroy = new JButton(new ImageIcon(url));
    destroy.setVisible(false);
    container.add(destroy);

    //
    // Bind
    //
    completed.addActionListener(e -> onToggle.run());
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
    title.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            destroy.setVisible(true);
          }

          @Override
          public void mouseExited(MouseEvent e) {
            destroy.setVisible(false);
          }
        });
    destroy.addActionListener(e -> onDestroy.run());
    destroy.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            destroy.setVisible(true);
          }

          @Override
          public void mouseExited(MouseEvent e) {
            destroy.setVisible(false);
          }
        });

    return container;
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
