package de.muspellheim.todos.frontend;

import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Footer extends JPanel {
  Consumer<Filter> onFilterChanged;

  private final JLabel activeCount;
  private final JComboBox<Filter> filter;

  Footer() {
    setLayout(new GridLayout(0, 3));

    activeCount = new JLabel();
    setActiveCount(0);
    var border = BorderFactory.createEmptyBorder(5, 8, 5, 8);
    activeCount.setBorder(border);
    add(activeCount);

    filter = new JComboBox<>();
    for (var f : Filter.values()) {
      filter.addItem(f);
    }
    filter.addActionListener(e -> onFilterChanged.accept(getFilter()));
    add(filter);
  }

  Filter getFilter() {
    return filter.getItemAt(filter.getSelectedIndex());
  }

  void setActiveCount(long c) {
    activeCount.setText(
        "<html><strong>" + c + "</strong> " + Strings.pluralize(c, "item") + " left</html>");
  }
}
