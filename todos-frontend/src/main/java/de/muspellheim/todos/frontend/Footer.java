package de.muspellheim.todos.frontend;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

class Footer extends Box {
  private final JLabel activeCount;

  Footer() {
    super(BoxLayout.X_AXIS);

    activeCount = new JLabel();
    setActiveCount(0);
    var border = BorderFactory.createEmptyBorder(5, 8, 5, 8);
    activeCount.setBorder(border);
    add(activeCount);
  }

  void setActiveCount(long n) {
    activeCount.setText(
        "<html><strong>" + n + "</strong> " + Texts.pluralize(n, "item") + " left</html>");
  }
}
