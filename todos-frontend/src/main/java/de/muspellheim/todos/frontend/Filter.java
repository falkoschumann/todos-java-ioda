package de.muspellheim.todos.frontend;

public enum Filter {
  ALL("All"),
  ACTIVE("Active"),
  COMPLETED("Completed");

  private final String name;

  Filter(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
