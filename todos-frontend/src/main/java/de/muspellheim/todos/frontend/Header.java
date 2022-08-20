package de.muspellheim.todos.frontend;

import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

class Header extends Box {
  Consumer<String> onAddTodo;
  Consumer<Boolean> onToggleAll;

  private final TodosModel model;
  private final JCheckBox toggleAll;
  private final JTextField newTodo;

  Header(TodosModel model) {
    //
    // Build
    //
    super(BoxLayout.X_AXIS);
    this.model = model;

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
    model.addChangeListener(e -> handleStateChanged());
    toggleAll.addActionListener(e -> handleToggleAll());
    newTodo.addActionListener(e -> handleNewTodo());
  }

  private void handleStateChanged() {
    toggleAll.setVisible(model.isExistsTodos());
    toggleAll.setSelected(model.isAllCompleted());
  }

  private void handleToggleAll() {
    onToggleAll.accept(toggleAll.isSelected());
  }

  private void handleNewTodo() {
    var title = newTodo.getText().trim();
    if (title.isEmpty()) {
      return;
    }

    onAddTodo.accept(title);
    newTodo.setText("");
  }

  @Override
  public void requestFocus() {
    newTodo.requestFocus();
  }
}
