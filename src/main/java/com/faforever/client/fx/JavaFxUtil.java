package com.faforever.client.fx;

import com.google.common.base.Strings;
import com.sun.javafx.stage.PopupWindowHelper;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.controlsfx.control.RangeSlider;
import org.springframework.util.Assert;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.nio.file.Files.createDirectories;
import static javax.imageio.ImageIO.write;

/**
 * Utility class to fix some annoying JavaFX shortcomings.
 */
@Slf4j
public final class JavaFxUtil {

  public static final StringConverter<Path> PATH_STRING_CONVERTER = new StringConverter<>() {
    @Override
    public String toString(Path object) {
      if (object == null) {
        return null;
      }
      return object.toAbsolutePath().toString();
    }

    @Override
    public Path fromString(String string) {
      if (Strings.isNullOrEmpty(string)) {
        return null;
      }
      return Path.of(string);
    }
  };

  private JavaFxUtil() {
    throw new AssertionError("Not instantiatable");
  }

  public static void makeNumericTextField(TextField textField, int maxLength, boolean allowNegative) {
    JavaFxUtil.addListener(textField.textProperty(), (observable, oldValue, newValue) -> {
      boolean isNegative = newValue.startsWith("-");
      String value = newValue;
      int maxLengthActual = isNegative && allowNegative ? maxLength + 1 : maxLength;

      if (allowNegative) {
        if (!value.matches("-?\\d*")) {
          value = newValue.replaceAll("[^-\\d]", "");
        }
      } else {
        if (!value.matches("\\d*")) {
          value = newValue.replaceAll("[^\\d]", "");
        }
      }

      if (maxLengthActual > 0 && value.length() > maxLengthActual) {
        value = value.substring(0, maxLengthActual);
      }

      textField.setText(value);
      if (textField.getCaretPosition() > textField.getLength()) {
        textField.positionCaret(textField.getLength());
      }
    });
  }

  /**
   * Better version of {@link Tooltip#setGraphic(Node)} that does not add unnecessary space. Javadoc of
   * {@link Tooltip#setGraphic(Node)} explains that this method is meant for adding icons.
   *
   * @param content - The content of the tooltip.
   * @return New Tooltip with added content.
   */
  public static Tooltip createCustomTooltip(Node content) {
    Tooltip tooltip = new Tooltip();
    PopupWindowHelper.getContent(tooltip).setAll(content);
    return tooltip;
  }

  /**
   * Centers a window FOR REAL. https://javafx-jira.kenai.com/browse/RT-40368
   */
  public static void centerOnScreen(Stage stage) {
    double width = stage.getWidth();
    double height = stage.getHeight();

    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    stage.setX((screenBounds.getMaxX() - screenBounds.getMinX() - width) / 2);
    stage.setY((screenBounds.getMaxY() - screenBounds.getMinY() - height) / 2);
  }

  public static void assertApplicationThread() {
    Assert.state(Platform.isFxApplicationThread(), "Must run in FX Application thread");
  }

  public static void assertBackgroundThread() {
    Assert.state(!Platform.isFxApplicationThread(), "Must not run in FX Application thread");
  }

  public static boolean isVisibleRecursively(Node node) {
    if (!node.isVisible()) {
      return false;
    }

    Parent parent = node.getParent();
    return parent == null ? node.getScene() != null : isVisibleRecursively(parent);
  }

  public static String toRgbCode(Color color) {
    return String.format("#%02X%02X%02X",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
  }

  /**
   * Returns an unmodifiable observable list from the specified list that mirrors any changes made to the specified
   * map.
   */
  public static <K, V> ObservableList<K> attachListToMapKeys(ObservableList<K> list, ObservableMap<K, V> map) {
    addListener(map, (MapChangeListener<K, V>) change -> {
      if (change.wasRemoved()) {
        list.remove(change.getKey());
      }
      if (change.wasAdded()) {
        list.add(change.getKey());
      }
    });
    return FXCollections.unmodifiableObservableList(list);
  }

  /**
   * Returns an unmodifiable observable list from the specified list that mirrors any changes made to the specified
   * map.
   */
  public static <K, V> ObservableList<V> attachListToMap(ObservableList<V> list, ObservableMap<K, V> map) {
    addListener(map, (MapChangeListener<K, V>) change -> {
      if (change.wasRemoved()) {
        list.remove(change.getValueRemoved());
      }
      if (change.wasAdded()) {
        list.add(change.getValueAdded());
      }
    });
    return FXCollections.unmodifiableObservableList(list);
  }

  /**
   * Returns an unmodifiable observable list from the specified list that mirrors any changes made to the specified
   * map.
   */
  public static <K, V> ObservableSet<V> attachSetToMap(ObservableSet<V> set, ObservableMap<K, V> map) {
    addListener(map, (MapChangeListener<K, V>) change -> {
      if (change.wasRemoved()) {
        set.remove(change.getValueRemoved());
      }
      if (change.wasAdded()) {
        set.add(change.getValueAdded());
      }
    });
    return FXCollections.unmodifiableObservableSet(set);
  }

  public static void persistImage(Image image, Path path, String format) {
    if (image == null) {
      return;
    }

    JavaFxUtil.addAndTriggerListener(image.progressProperty(), new ChangeListener<>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (newValue.intValue() >= 1) {
          writeImageLater(image, path, format);
          observable.removeListener(this);
        }
      }
    });
  }

  private static void writeImageLater(Image image, Path path, String format) {
    CompletableFuture.runAsync(() -> {
      try {
        if (image == null) {
          return;
        }
        if (path.getParent() != null) {
          createDirectories(path.getParent());
        }
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        if (bufferedImage == null) {
          log.warn("Could not read image from {} for {}", image.getUrl(), path);
          return;
        }
        write(bufferedImage, format, path.toFile());
        log.trace("Image written to {}", path);
      } catch (IOException e) {
        log.warn("Could not write image to {}", path, e);
      }
    });
  }

  public static void setAnchors(Node node, double value) {
    AnchorPane.setBottomAnchor(node, value);
    AnchorPane.setLeftAnchor(node, value);
    AnchorPane.setRightAnchor(node, value);
    AnchorPane.setTopAnchor(node, value);
  }

  public static void fixScrollSpeed(ScrollPane scrollPane) {
    Node content = scrollPane.getContent();
    content.setOnScroll(event -> {
      double deltaY = event.getDeltaY() * 3;
      double height = scrollPane.getContent().getBoundsInLocal().getHeight();
      double vvalue = scrollPane.getVvalue();
      // deltaY/height to make the scrolling equally fast regardless of the actual height of the component
      scrollPane.setVvalue(vvalue + -deltaY / height);
    });
  }

  /**
   * Since the JavaFX properties API is not thread safe, adding listeners must be synchronized on the property - which
   * is what this method does. Also manually triggers listener after adding.
   */
  public static <T> void addAndTriggerListener(ObservableValue<T> observableValue, ChangeListener<? super T> listener) {
    synchronized (observableValue) {
      observableValue.addListener(listener);
      listener.changed(observableValue, null, observableValue.getValue());
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, adding listeners must be synchronized on the property - which
   * is what this method does. Also manually triggers listener after adding.
   */
  public static void addAndTriggerListener(Observable observable, InvalidationListener listener) {
    synchronized (observable) {
      observable.addListener(listener);
      listener.invalidated(observable);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, adding listeners must be synchronized on the property - which
   * is what this method does.
   */
  public static <T> void addListener(ObservableValue<T> observableValue, ChangeListener<? super T> listener) {
    synchronized (observableValue) {
      observableValue.addListener(listener);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, adding listeners must be synchronized on the property - which
   * is what this method does.
   */
  public static void addListener(Observable observable, InvalidationListener listener) {
    synchronized (observable) {
      observable.addListener(listener);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, adding listeners must be synchronized on the property - which
   * is what this method does.
   */
  public static <K, V> void addListener(ObservableMap<K, V> observable, MapChangeListener<K, V> listener) {
    synchronized (observable) {
      observable.addListener(listener);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, adding listeners must be synchronized on the property - which
   * is what this method does.
   */
  public static <T> void addListener(ObservableList<T> observable, ListChangeListener<T> listener) {
    synchronized (observable) {
      observable.addListener(listener);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, adding listeners must be synchronized on the property - which
   * is what this method does.
   */
  public static <K, V> void addListener(MapProperty<K, V> mapProperty,
                                        MapChangeListener<? super K, ? super V> listener) {
    synchronized (mapProperty) {
      mapProperty.addListener(listener);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, adding listeners must be synchronized on the property - which
   * is what this method does.
   */
  public static <T> void addListener(ObservableSet<T> set, SetChangeListener<T> listener) {
    synchronized (set) {
      set.addListener(listener);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, removing listeners must be synchronized on the property - which
   * is what this method does.
   */
  public static <T> void removeListener(ObservableValue<T> observableValue, ChangeListener<? super T> listener) {
    synchronized (observableValue) {
      observableValue.removeListener(listener);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, removing listeners must be synchronized on the property - which
   * is what this method does.
   */
  public static <T> void removeListener(ObservableList<T> observableList, ListChangeListener<? super T> listener) {
    synchronized (observableList) {
      observableList.removeListener(listener);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, removing listeners must be synchronized on the property - which
   * is what this method does.
   */
  public static void removeListener(Observable observable, InvalidationListener listener) {
    synchronized (observable) {
      observable.removeListener(listener);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, removing listeners must be synchronized on the property - which
   * is what this method does.
   */
  public static <T, U> void removeListener(ObservableMap<T, U> observable, MapChangeListener<T, U> listener) {
    synchronized (observable) {
      observable.removeListener(listener);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, binding a property must be synchronized on the property - which
   * is what this method does.
   */
  public static <T> void bind(Property<T> property, ObservableValue<? extends T> observable) {
    synchronized (property) {
      property.bind(observable);
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, unbinding a property must be synchronized on the property -
   * which is what this method does.
   */
  public static <T> void unbind(Property<T> property) {
    synchronized (property) {
      property.unbind();
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, binding properties must be synchronized on the properties -
   * which is what this method does. Since synchronization happens on both property in order
   * {@code property1, property2}, this is prone to deadlocks. To avoid this, pass the property with the lower
   * visibility (e.g. method- or controller-only) as first and the property with higher visibility (e.g. a property from
   * a shared object or service) as second parameter.
   */
  public static void bindBidirectional(StringProperty stringProperty, IntegerProperty integerProperty,
                                       NumberStringConverter numberStringConverter) {
    synchronized (stringProperty) {
      synchronized (integerProperty) {
        stringProperty.bindBidirectional(integerProperty, numberStringConverter);
      }
    }
  }

  /**
   * Since the JavaFX properties API is not thread safe, binding properties must be synchronized on the properties -
   * which is what this method does. Since synchronization happens on both property in order
   * {@code property1, property2}, this is prone to deadlocks. To avoid this, pass the property with the lower
   * visibility (e.g. method- or controller-only) as first and the property with higher visibility (e.g. a property from
   * a shared object or service) as second parameter.
   */
  public static <T> void bindBidirectional(Property<T> property1, Property<T> property2) {
    synchronized (property1) {
      synchronized (property2) {
        property1.bindBidirectional(property2);
      }
    }
  }

  public static Pointer getNativeWindow() {

    return User32.INSTANCE.GetActiveWindow().getPointer();
  }

  public static void bindManagedToVisible(Node... nodes) {
    Arrays.stream(nodes).forEach(node -> node.managedProperty().bind(node.visibleProperty()));
  }

  public static void bindTextFieldAndRangeSlider(TextField lowValueTextField, TextField highValueTextField,
                                                 RangeSlider rangeSlider) {
    DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getInstance();
    numberFormat.setMaximumFractionDigits(0);
    bindTextFieldAndRangeSlider(lowValueTextField, highValueTextField, rangeSlider, numberFormat);
  }

  public static void bindTextFieldAndRangeSlider(TextField lowValueTextField, TextField highValueTextField,
                                                 RangeSlider rangeSlider, DecimalFormat format) {
    Map.of(
        lowValueTextField, Pair.of(rangeSlider.lowValueProperty(), rangeSlider.getMin()),
        highValueTextField, Pair.of(rangeSlider.highValueProperty(), rangeSlider.getMax())
    ).forEach((textField, pair) -> {
      DoubleProperty valueProperty = pair.getLeft();
      Double value = pair.getRight();
      textField.textProperty().bindBidirectional(valueProperty, new StringConverter<>() {
        @Override
        public String toString(Number number) {
          if (!number.equals(value)) {
            return format.format(number);
          } else {
            return "";
          }
        }

        @Override
        public Number fromString(String string) {
          String decimalSeparator = Character.toString(format.getDecimalFormatSymbols().getDecimalSeparator());
          try {
            Number number = format.parse(string);
            String decimalSeparatorSuffix = string.endsWith(decimalSeparator) && format.getMaximumFractionDigits() > 0 ? decimalSeparator : "";
            textField.setText(format.format(number) + decimalSeparatorSuffix);
            return number;
          } catch (ParseException e) {
            if (!string.equals("-") && !string.equals(decimalSeparator)) {
              textField.setText("");
            }
            return value;
          }
        }
      });
    });
  }
}
