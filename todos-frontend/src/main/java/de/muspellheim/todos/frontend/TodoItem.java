package de.muspellheim.todos.frontend;

import de.muspellheim.todos.contract.data.Todo;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class TodoItem extends JPanel {
  Runnable onDestroy;
  Consumer<String> onSave;
  Runnable onToggle;

  private static final String CARD_VIEW = "VIEW";
  private static final String CARD_EDIT = "EDIT";

  private final CardLayout layout;
  private final View view;
  private final Edit edit;
  private Todo todo;

  TodoItem(Todo todo) {
    setName(todo.title() + " (" + todo.id() + ")");
    setMaximumSize(new Dimension(Short.MAX_VALUE, 0));
    setOpaque(false);
    layout = new CardLayout();
    setLayout(layout);

    view = new View();
    add(view, CARD_VIEW);

    edit = new Edit();
    add(edit, CARD_EDIT);

    setTodo(todo);
  }

  final Todo getTodo() {
    return todo;
  }

  final void setTodo(Todo value) {
    todo = value;
    view.updateState();
    edit.updateState();
  }

  private class View extends Box {
    private final JCheckBox completed;
    private final JLabel title;
    private final JButton destroy;

    View() {
      //
      // Build
      //
      super(BoxLayout.X_AXIS);
      setAlignmentX(Component.LEFT_ALIGNMENT);
      setPreferredSize(new Dimension(0, 28));

      completed = new JCheckBox("");
      add(completed);

      title = new JLabel();
      title.setMinimumSize(new Dimension(0, 28));
      title.setMaximumSize(new Dimension(Short.MAX_VALUE, 28));
      add(title);

      var url = TodoItem.class.getResource("/images/destroy.png");
      assert url != null;
      destroy = new JButton(new ImageIcon(url));
      destroy.setVisible(false);
      add(destroy);

      //
      // Bind
      //
      completed.addActionListener(e -> onToggle.run());
      title.addMouseListener(new MouseClickListener());
      title.addMouseListener(new MouseHoverListener());
      destroy.addActionListener(e -> onDestroy.run());
      destroy.addMouseListener(new MouseHoverListener());
    }

    void updateState() {
      completed.setSelected(todo.completed());
      var text = todo.title();
      if (todo.completed()) {
        text = "<html><strike>" + text + "</strike><html>";
      }
      title.setText(text);

      revalidate();
      repaint();
    }

    private class MouseClickListener extends MouseAdapter {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          layout.show(TodoItem.this, CARD_EDIT);
          edit.requestFocus();
        }
      }
    }

    private class MouseHoverListener extends MouseAdapter {
      @Override
      public void mouseEntered(MouseEvent e) {
        destroy.setVisible(true);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        destroy.setVisible(false);
      }
    }
  }

  private class Edit extends Box {
    private final JTextField title;

    Edit() {
      //
      // Build
      //
      super(BoxLayout.X_AXIS);
      title = new JTextField();
      add(title);

      //
      // Bind
      //
      title.addKeyListener(new KeyPressedListener());
      title.addFocusListener(new FocusLostListener());
    }

    void updateState() {
      title.setText(todo.title());
      revalidate();
      repaint();
    }

    @Override
    public void requestFocus() {
      title.requestFocus();
    }

    private void handleSubmit() {
      onSave.accept(title.getText());
      layout.show(TodoItem.this, CARD_VIEW);
    }

    private void handleCancel() {
      title.setText(todo.title());
      layout.show(TodoItem.this, CARD_VIEW);
    }

    private class KeyPressedListener extends KeyAdapter {
      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_ENTER -> handleSubmit();
          case KeyEvent.VK_ESCAPE -> handleCancel();
        }
      }
    }

    private class FocusLostListener extends FocusAdapter {
      @Override
      public void focusLost(FocusEvent e) {
        handleSubmit();
      }
    }
  }
}
