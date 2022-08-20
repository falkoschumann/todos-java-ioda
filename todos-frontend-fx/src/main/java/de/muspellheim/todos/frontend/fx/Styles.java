package de.muspellheim.todos.frontend.fx;

import javafx.scene.Node;

class Styles {
  private Styles() {}

  static void add(Node node, String style) {
    if (!node.getStyleClass().contains("line-through")) {
      node.getStyleClass().add("line-through");
    }
  }

  static void remove(Node node, String style) {
    node.getStyleClass().remove("line-through");
  }
}
