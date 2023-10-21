package com.faforever.client.domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Value
public class LeagueScoreJournalBean extends AbstractEntityBean<LeagueScoreJournalBean> {
  IntegerProperty gameId = new SimpleIntegerProperty();
  IntegerProperty loginId = new SimpleIntegerProperty();
  IntegerProperty gameCount = new SimpleIntegerProperty();
  IntegerProperty scoreBefore = new SimpleIntegerProperty();
  IntegerProperty scoreAfter = new SimpleIntegerProperty();
  ObjectProperty<LeagueSeasonBean> season = new SimpleObjectProperty<>();
  ObjectProperty<SubdivisionBean> divisionBefore = new SimpleObjectProperty<>();
  ObjectProperty<SubdivisionBean> divisionAfter = new SimpleObjectProperty<>();
  
  public int getGameId() {
    return gameId.get();
  }

  public void setGameId(int gameId) {
    this.gameId.set(gameId);
  }

  public IntegerProperty gameIdProperty() {
    return gameId;
  }
  
  public int getLoginId() {
    return loginId.get();
  }

  public void setLoginId(int loginId) {
    this.loginId.set(loginId);
  }

  public IntegerProperty loginIdProperty() {
    return loginId;
  }
  
  public int getGameCount() {
    return gameCount.get();
  }

  public void setGameCount(int gameCount) {
    this.gameCount.set(gameCount);
  }

  public IntegerProperty gameCountProperty() {
    return gameCount;
  }
  
  public int getScoreBefore() {
    return scoreBefore.get();
  }

  public void setScoreBefore(int scoreBefore) {
    this.scoreBefore.set(scoreBefore);
  }

  public IntegerProperty scoreBeforeProperty() {
    return scoreBefore;
  }
  
  public int getScoreAfter() {
    return scoreAfter.get();
  }

  public void setScoreAfter(int scoreAfter) {
    this.scoreAfter.set(scoreAfter);
  }

  public IntegerProperty scoreAfterProperty() {
    return scoreAfter;
  }
  
  public LeagueSeasonBean getSeason() {
    return season.get();
  }

  public void setSeason(LeagueSeasonBean season) {
    this.season.set(season);
  }

  public ObjectProperty<LeagueSeasonBean> seasonProperty() {
    return season;
  }
  
  public SubdivisionBean getDivisionBefore() {
    return divisionBefore.get();
  }

  public void setDivisionBefore(SubdivisionBean divisionBefore) {
    this.divisionBefore.set(divisionBefore);
  }

  public ObjectProperty<SubdivisionBean> divisionBeforeProperty() {
    return divisionBefore;
  }
  
  public SubdivisionBean getDivisionAfter() {
    return divisionAfter.get();
  }

  public void setDivisionAfter(SubdivisionBean divisionAfter) {
    this.divisionAfter.set(divisionAfter);
  }

  public ObjectProperty<SubdivisionBean> divisionAfterProperty() {
    return divisionAfter;
  }

}
