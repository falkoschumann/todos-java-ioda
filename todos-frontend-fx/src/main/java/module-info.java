module de.muspellheim.todos.frontend.fx {
  exports de.muspellheim.todos.frontend.fx;

  requires javafx.controls;
  requires javafx.fxml;
  requires transitive de.muspellheim.todos.contract;

  opens de.muspellheim.todos.frontend.fx to
      javafx.fxml;
}
