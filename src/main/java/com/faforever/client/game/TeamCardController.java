package com.faforever.client.game;


import com.faforever.client.domain.GameBean;
import com.faforever.client.domain.GamePlayerStatsBean;
import com.faforever.client.domain.GameOutcome;
import com.faforever.client.domain.PlayerBean;
import com.faforever.client.domain.SubdivisionBean;
import com.faforever.client.fx.FxApplicationThreadExecutor;
import com.faforever.client.fx.JavaFxUtil;
import com.faforever.client.fx.NodeController;
import com.faforever.client.fx.SimpleChangeListener;
import com.faforever.client.i18n.I18n;
import com.faforever.client.player.PlayerService;
import com.faforever.client.theme.UiService;
import com.faforever.client.util.RatingUtil;
import com.faforever.commons.api.dto.Faction;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class TeamCardController extends NodeController<Node> {

  private static final PseudoClass VICTORY = PseudoClass.getPseudoClass("victory");
  private static final PseudoClass DEFEAT = PseudoClass.getPseudoClass("defeat");
  private static final PseudoClass DRAW = PseudoClass.getPseudoClass("draw");
  private static final PseudoClass UNKNOWN = PseudoClass.getPseudoClass("unknown");
  private static final int UNSET = -1000000;

  private final I18n i18n;
  private final PlayerService playerService;
  private final FxApplicationThreadExecutor fxApplicationThreadExecutor;
  private final UiService uiService;

  public Pane teamPaneRoot;
  public VBox teamPane;
  public Label teamNameLabel;
  public Label gameResultLabel;

  private final ObjectProperty<List<Integer>> playerIds = new SimpleObjectProperty<>(List.of());
  private final ObjectProperty<List<PlayerBean>> players = new SimpleObjectProperty<>(List.of());
  private final ObjectProperty<Function<PlayerBean, Integer>> ratingProvider = new SimpleObjectProperty<>();
  private final ObjectProperty<Function<PlayerBean, SubdivisionBean>> divisionProvider = new SimpleObjectProperty<>();
  private final ObjectProperty<Function<PlayerBean, Faction>> factionProvider = new SimpleObjectProperty<>();
  private final ObjectProperty<RatingPrecision> ratingPrecision = new SimpleObjectProperty<>();
  private final ObjectProperty<GameOutcome> teamOutcome = new SimpleObjectProperty<>();
  private final IntegerProperty teamId = new SimpleIntegerProperty();
  private final SimpleChangeListener<List<PlayerBean>> playersListener = this::populateTeamContainer;
  private final ObservableValue<Integer> teamRating = ratingProvider.flatMap(provider -> ratingPrecision.flatMap(precision -> players.map(playerBeans -> playerBeans.stream()
      .map(provider)
      .filter(Objects::nonNull)
      .map(rating -> precision == RatingPrecision.ROUNDED ? RatingUtil.getRoundedRating(rating) : rating)
      .reduce(Integer::sum).orElse(UNSET))));

  private final Map<PlayerBean, PlayerCardController> playerCardControllersMap = new HashMap<>();

  @Override
  protected void onInitialize() {
    teamNameLabel.textProperty()
        .bind(teamRating.flatMap(teamRating -> teamId.map(id -> switch (id.intValue()) {
          case 0, GameBean.NO_TEAM -> i18n.get("game.tooltip.teamTitleNoTeam");
          case GameBean.OBSERVERS_TEAM -> i18n.get("game.tooltip.observers");
          default -> {
            try {
              if (teamRating == UNSET) {
                yield i18n.get("game.tooltip.teamTitleNoRating", id.intValue() - 1);
              } else {
                yield i18n.get("game.tooltip.teamTitle", id.intValue() - 1, teamRating);
              }
            } catch (NumberFormatException e) {
              yield "";
            }
          }
        })));
    JavaFxUtil.bindManagedToVisible(gameResultLabel);
    gameResultLabel.visibleProperty().bind(gameResultLabel.textProperty().isEmpty().not());
    players.addListener(playersListener);
  }

  private void populateTeamContainer(List<PlayerBean> newValue) {
    CompletableFuture.supplyAsync(() -> createPlayerCardControllers(newValue))
        .thenAcceptAsync(controllers -> teamPane.getChildren()
            .setAll(controllers.stream().map(PlayerCardController::getRoot).toList()), fxApplicationThreadExecutor);
  }

  private List<PlayerCardController> createPlayerCardControllers(List<PlayerBean> players) {
    playerCardControllersMap.clear();
    return players.stream().map(player -> {
      PlayerCardController controller = uiService.loadFxml("theme/player_card.fxml");

      controller.ratingProperty()
          .bind(ratingProvider.map(ratingFunction -> ratingFunction.apply(player))
              .flatMap(rating -> ratingPrecision.map(precision -> precision == RatingPrecision.ROUNDED ? RatingUtil.getRoundedRating(rating) : rating)));
      controller.divisionProperty()
              .bind(divisionProvider.map(divisionFunction -> divisionFunction.apply(player)));
      controller.factionProperty()
          .bind(factionProvider.map(factionFunction -> factionFunction.apply(player)));
      controller.setPlayer(player);

      playerCardControllersMap.put(player, controller);

      return controller;
    }).toList();
  }
  
  public void showGameResult() {
    switch (teamOutcome.get()) {
      case VICTORY -> {
        gameResultLabel.setText(i18n.get("game.resultVictory"));
        gameResultLabel.pseudoClassStateChanged(VICTORY, true);
      }
      case DEFEAT -> {
        gameResultLabel.setText(i18n.get("game.resultDefeat"));
        gameResultLabel.pseudoClassStateChanged(DEFEAT, true);
      }
      case DRAW, MUTUAL_DRAW -> {
        gameResultLabel.setText(i18n.get("game.resultDraw"));
        gameResultLabel.pseudoClassStateChanged(DRAW, true);
      }
      default -> {
        gameResultLabel.setText(i18n.get("game.resultUnknown"));
        gameResultLabel.pseudoClassStateChanged(UNKNOWN, true);
      }
    }
  }

  public void bindPlayersToPlayerIds() {
    players.bind(playerIds.map(ids -> ids.stream()
        .map(playerService::getPlayerByIdIfOnline)
        .flatMap(Optional::stream)
        .collect(Collectors.toCollection(FXCollections::observableArrayList))));
  }

  public void setRatingProvider(Function<PlayerBean, Integer> ratingProvider) {
    this.ratingProvider.set(ratingProvider);
  }

  public void setDivisionProvider(Function<PlayerBean, SubdivisionBean> divisionProvider) {
    this.divisionProvider.set(divisionProvider);
  }

  public void setFactionProvider(Function<PlayerBean, Faction> factionProvider) {
    this.factionProvider.set(factionProvider);
  }

  public void setRatingPrecision(RatingPrecision ratingPrecision) {
    this.ratingPrecision.set(ratingPrecision);
  }

  public void setTeamId(int teamId) {
    this.teamId.set(teamId);
  }

  public int getTeamId() {
    return teamId.get();
  }

  public void setPlayerIds(Collection<Integer> playerIds) {
    this.playerIds.set(List.copyOf(playerIds));
  }

  public void setPlayers(Collection<PlayerBean> players) {
    this.players.set(List.copyOf(players));
  }

  public ObjectProperty<List<Integer>> playerIdsProperty() {
    return playerIds;
  }

  public IntegerProperty teamIdProperty() {
    return teamId;
  }

  public ObjectProperty<Function<PlayerBean, Integer>> ratingProviderProperty() {
    return ratingProvider;
  }

  public void setTeamOutcome(GameOutcome teamOutcome) {
    this.teamOutcome.set(teamOutcome);
  }

  public GameOutcome getTeamOutcome() {
    return teamOutcome.get();
  }

  public ObjectProperty<GameOutcome> teamResult() {
    return teamOutcome;
  }

  public void setStats(List<GamePlayerStatsBean> teamPlayerStats) {
    for (GamePlayerStatsBean playerStats : teamPlayerStats) {
      PlayerCardController controller = playerCardControllersMap.get(playerStats.getPlayer());
      if (controller != null) {
        controller.setPlayerStats(playerStats);
      }
    }
  }

  @Override
  public Node getRoot() {
    return teamPaneRoot;
  }
}

