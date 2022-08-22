package de.muspellheim.todos.frontend.fx;

import javafx.scene.Node;

class Styles {
  private Styles() {}

  static void add(Node node, String style) {
    if (!node.getStyleClass().contains(style)) {
      node.getStyleClass().add(style);
    }
  }

  static void remove(Node node, String style) {
    node.getStyleClass().remove(style);
  }
}
