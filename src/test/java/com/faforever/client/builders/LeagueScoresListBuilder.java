package com.faforever.client.builders;

import com.faforever.client.domain.LeagueScoreJournalBean;

import java.util.ArrayList;
import java.util.List;

public class LeagueScoresListBuilder {
  private final List<LeagueScoreJournalBean> leagueScores = new ArrayList<>();

  public static LeagueScoresListBuilder create() {
    return new LeagueScoresListBuilder();
  }

  public LeagueScoresListBuilder defaultValues() {
    append(LeagueScoreJournalBeanBuilder.create().defaultValues().get());
    return this;
  }

  public LeagueScoresListBuilder append(LeagueScoreJournalBean leagueScore) {
    leagueScores.add(leagueScore);
    return this;
  }

  public LeagueScoresListBuilder replace(List<LeagueScoreJournalBean> leagueScore) {
    this.leagueScores.clear();
    this.leagueScores.addAll(leagueScore);
    return this;
  }

  public List<LeagueScoreJournalBean> get() {
    return leagueScores;
  }
}
