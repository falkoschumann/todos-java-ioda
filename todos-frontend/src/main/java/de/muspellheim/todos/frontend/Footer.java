package de.muspellheim.todos.frontend;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Footer extends JPanel {
  Runnable onClearCompleted;
  Consumer<Filter> onFilterChanged;

  private final TodosModel model;
  private final JLabel activeCount;
  private final JComboBox<Filter> filter;
  private final JButton clearCompleted;

  Footer(TodosModel model) {
    //
    // Build
    //
    this.model = model;
    var layout = new GridBagLayout();
    setLayout(layout);

    activeCount = new JLabel("<html><strong>0</strong> items left</html>");
    var border = BorderFactory.createEmptyBorder(5, 8, 5, 8);
    activeCount.setBorder(border);
    add(activeCount);

    filter = new JComboBox<>();
    for (var f : Filter.values()) {
      filter.addItem(f);
    }
    var constraint = new GridBagConstraints();
    constraint.weightx = 1.0;
    layout.setConstraints(filter, constraint);
    add(filter);

    clearCompleted = new JButton("Clear completed");
    add(clearCompleted);

    //
    // Bind
    //
    model.addChangeListener(e -> handleStateChanged());
    filter.addActionListener(e -> changeFilter());
    clearCompleted.addActionListener(e -> onClearCompleted.run());
  }

  private void handleStateChanged() {
    setVisible(model.existsTodos());
    activeCount.setText(
        "<html><strong>"
            + model.activeCount()
            + "</strong> "
            + Strings.pluralize(model.activeCount(), "item")
            + " left</html>");
    clearCompleted.setVisible(model.existsCompleted());
  }

  private void changeFilter() {
    var f = filter.getItemAt(filter.getSelectedIndex());
    onFilterChanged.accept(f);
  }
}
