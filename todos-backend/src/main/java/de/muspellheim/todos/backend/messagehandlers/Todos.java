package de.muspellheim.todos.backend.messagehandlers;

class Todos {
  private Todos() {}

  static String normalizeTitle(String title) {
    return title.trim();
  }
}
