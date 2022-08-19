package de.muspellheim.todos.frontend.fx;

enum Filter {
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
