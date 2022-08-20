package de.muspellheim.todos.frontend.fx;

class Strings {
  private Strings() {}

  static String pluralize(long count, String word) {
    return count == 1 ? word : word + "s";
  }
}
