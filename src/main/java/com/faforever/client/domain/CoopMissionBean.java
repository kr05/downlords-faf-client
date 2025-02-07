package com.faforever.client.domain;

import com.faforever.client.coop.CoopCategory;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class CoopMissionBean {
  @EqualsAndHashCode.Include
  private final ObjectProperty<Integer> id = new SimpleObjectProperty<>();
  @ToString.Include
  private final StringProperty name = new SimpleStringProperty();
  private final StringProperty description = new SimpleStringProperty();
  @ToString.Include
  private final IntegerProperty version = new SimpleIntegerProperty();
  private final ObjectProperty<CoopCategory> category = new SimpleObjectProperty<>();
  private final StringProperty downloadUrl = new SimpleStringProperty();
  private final StringProperty thumbnailUrlSmall = new SimpleStringProperty();
  private final StringProperty thumbnailUrlLarge = new SimpleStringProperty();
  private final StringProperty mapFolderName = new SimpleStringProperty();

  public String getDescription() {
    return description.get();
  }

  public void setDescription(String description) {
    this.description.set(description);
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  public String getName() {
    return name.get();
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public StringProperty nameProperty() {
    return name;
  }

  public int getVersion() {
    return version.get();
  }

  public void setVersion(int version) {
    this.version.set(version);
  }

  public IntegerProperty versionProperty() {
    return version;
  }

  public CoopCategory getCategory() {
    return category.get();
  }

  public void setCategory(CoopCategory category) {
    this.category.set(category);
  }

  public ObjectProperty<CoopCategory> categoryProperty() {
    return category;
  }

  public String getDownloadUrl() {
    return downloadUrl.get();
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl.set(downloadUrl);
  }

  public StringProperty downloadUrlProperty() {
    return downloadUrl;
  }

  public String getThumbnailUrlSmall() {
    return thumbnailUrlSmall.get();
  }

  public void setThumbnailUrlSmall(String thumbnailUrlSmall) {
    this.thumbnailUrlSmall.set(thumbnailUrlSmall);
  }

  public StringProperty thumbnailUrlSmallProperty() {
    return thumbnailUrlSmall;
  }

  public String getThumbnailUrlLarge() {
    return thumbnailUrlLarge.get();
  }

  public void setThumbnailUrlLarge(String thumbnailUrlLarge) {
    this.thumbnailUrlLarge.set(thumbnailUrlLarge);
  }

  public StringProperty thumbnailUrlLargeProperty() {
    return thumbnailUrlLarge;
  }

  public String getMapFolderName() {
    return mapFolderName.get();
  }

  public void setMapFolderName(String mapFolderName) {
    this.mapFolderName.set(mapFolderName);
  }

  public StringProperty mapFolderNameProperty() {
    return mapFolderName;
  }

  public Integer getId() {
    return id.get();
  }

  public void setId(Integer id) {
    this.id.set(id);
  }

  public ObjectProperty<Integer> idProperty() {
    return id;
  }
}
