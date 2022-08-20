package de.muspellheim.todos.frontend.fx;

import de.muspellheim.todos.contract.data.Todo;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

class TodoCell extends ListCell<Todo> {
  Consumer<Integer> onDestroy;
  BiConsumer<Integer, String> onSave;
  Consumer<Integer> onToggle;

  @Override
  protected void updateItem(Todo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    } else {
      setGraphic(new View(item));
    }
  }

  @Override
  public void startEdit() {
    super.startEdit();
    setGraphic(new Edit(getItem()));
  }

  @Override
  public void commitEdit(Todo newValue) {
    super.commitEdit(newValue);
    setGraphic(new View(newValue));
  }

  @Override
  public void cancelEdit() {
    super.cancelEdit();
    setGraphic(new View(getItem()));
  }

  private class View extends HBox {
    View(Todo todo) {
      //
      // Build
      //
      setSpacing(8);
      setAlignment(Pos.BASELINE_LEFT);

      var completed = new CheckBox();
      completed.setSelected(todo.completed());
      getChildren().add(completed);

      var title = new Label(todo.title());
      title.setMaxWidth(Double.MAX_VALUE);
      if (todo.completed()) {
        Styles.add(title, "line-through");
      } else {
        Styles.remove(title, "line-through");
      }
      HBox.setHgrow(title, Priority.ALWAYS);
      getChildren().add(title);

      var url = TodoCell.class.getResource("/images/destroy.png");
      Objects.requireNonNull(url, "Resource not found: /images/destroy.png");
      var destroy = new Button("", new ImageView(new Image(url.toExternalForm())));
      destroy.setVisible(false);
      getChildren().add(destroy);

      //
      // Bind
      //
      setOnMouseEntered(e -> destroy.setVisible(true));
      setOnMouseExited(e -> destroy.setVisible(false));
      completed.setOnAction(e -> onToggle.accept(todo.id()));
      title.setOnMouseClicked(
          e -> {
            if (e.getClickCount() == 2) {
              startEdit();
            }
          });
      destroy.setOnAction(e -> onDestroy.accept(todo.id()));
    }
  }

  private class Edit extends HBox {
    private final TextField title;

    Edit(Todo todo) {
      //
      // Build
      //
      title = new TextField(todo.title());
      HBox.setHgrow(title, Priority.ALWAYS);
      getChildren().add(title);

      Platform.runLater(
          () -> {
            title.selectAll();
            title.requestFocus();
          });

      //
      // Bind
      //
      title.setOnKeyReleased(
          e -> {
            switch (e.getCode()) {
              case ENTER -> handleSubmit();
              case ESCAPE -> handleCancel();
            }
          });
    }

    private void handleSubmit() {
      onSave.accept(getItem().id(), title.getText());
      commitEdit(new Todo(getItem().id(), title.getText(), getItem().completed()));
    }

    private void handleCancel() {
      title.setText(getItem().title());
      cancelEdit();
    }
  }
}
