package de.muspellheim.todos.frontend;

import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Footer extends JPanel {
  Runnable onClearCompleted;
  Consumer<Filter> onFilterChanged;

  private final JLabel activeCount;
  private final JComboBox<Filter> filter;
  private final JButton clearCompleted;

  Footer() {
    //
    // Build
    //
    setLayout(new GridLayout());

    activeCount = new JLabel();
    setActiveCount(0);
    var border = BorderFactory.createEmptyBorder(5, 8, 5, 8);
    activeCount.setBorder(border);
    add(activeCount);

    filter = new JComboBox<>();
    for (var f : Filter.values()) {
      filter.addItem(f);
    }
    add(filter);

    clearCompleted = new JButton("Clear completed");
    clearCompleted.addActionListener(e -> onClearCompleted.run());
    add(clearCompleted);

    //
    // Bind
    //
    filter.addActionListener(e -> onFilterChanged.accept(getFilter()));
  }

  Filter getFilter() {
    return filter.getItemAt(filter.getSelectedIndex());
  }

  void setActiveCount(long c) {
    activeCount.setText(
        "<html><strong>" + c + "</strong> " + Strings.pluralize(c, "item") + " left</html>");
  }

  void setCompletedCount(long c) {
    clearCompleted.setVisible(c > 0);
  }
}
