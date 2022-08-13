package de.muspellheim.todos.frontend;

public class Texts {
  private Texts() {}

  static String pluralize(long count, String word) {
    return count == 1 ? word : word + "s";
  }
}
