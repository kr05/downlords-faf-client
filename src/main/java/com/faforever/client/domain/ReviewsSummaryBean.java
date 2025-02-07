package com.faforever.client.domain;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public abstract class ReviewsSummaryBean {
  @EqualsAndHashCode.Include
  private final ObjectProperty<Integer> id = new SimpleObjectProperty<>();
  private final FloatProperty positive = new SimpleFloatProperty();
  private final FloatProperty negative = new SimpleFloatProperty();
  private final FloatProperty score = new SimpleFloatProperty();
  private final FloatProperty averageScore = new SimpleFloatProperty();
  private final IntegerProperty numReviews = new SimpleIntegerProperty();
  private final FloatProperty lowerBound = new SimpleFloatProperty();

  public Integer getId() {
    return id.get();
  }

  public void setId(Integer id) {
    this.id.set(id);
  }

  public ObjectProperty<Integer> idProperty() {
    return id;
  }

  public float getPositive() {
    return positive.get();
  }

  public void setPositive(float positive) {
    this.positive.set(positive);
  }

  public FloatProperty positiveProperty() {
    return positive;
  }

  public float getNegative() {
    return negative.get();
  }

  public void setNegative(float negative) {
    this.negative.set(negative);
  }

  public FloatProperty negativeProperty() {
    return negative;
  }

  public float getScore() {
    return score.get();
  }

  public void setScore(float score) {
    this.score.set(score);
  }

  public FloatProperty averageScoreProperty() {
    return averageScore;
  }

  public float getAverageScore() {
    return averageScore.get();
  }

  public void setAverageScore(float averageScore) {
    this.averageScore.set(averageScore);
  }

  public FloatProperty scoreProperty() {
    return score;
  }

  public int getNumReviews() {
    return numReviews.get();
  }

  public void setNumReviews(int numReviews) {
    this.numReviews.set(numReviews);
  }

  public IntegerProperty numReviewsProperty() {
    return numReviews;
  }

  public float getLowerBound() {
    return lowerBound.get();
  }

  public void setLowerBound(float lowerBound) {
    this.lowerBound.set(lowerBound);
  }

  public FloatProperty lowerBoundProperty() {
    return lowerBound;
  }
}
