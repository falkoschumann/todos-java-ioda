module de.muspellheim.todos.fx {
  requires de.muspellheim.todos.backend;
  requires de.muspellheim.todos.frontend.fx;
  requires javafx.controls;

  opens de.muspellheim.todos.fx to
      javafx.graphics;
}
