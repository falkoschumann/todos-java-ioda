module de.muspellheim.todos.backend {
  exports de.muspellheim.todos.backend.adapters;
  exports de.muspellheim.todos.backend.messagehandlers;

  requires static lombok;
  requires transitive de.muspellheim.todos.contract;
  requires com.google.gson;
}
