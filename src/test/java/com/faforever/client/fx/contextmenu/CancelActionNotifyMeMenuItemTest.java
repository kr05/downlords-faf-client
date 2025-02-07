package com.faforever.client.fx.contextmenu;

import com.faforever.client.builders.GameBeanBuilder;
import com.faforever.client.domain.GameBean;
import com.faforever.client.i18n.I18n;
import com.faforever.client.replay.LiveReplayService;
import com.faforever.client.replay.TrackingLiveReplay;
import com.faforever.client.replay.TrackingLiveReplayAction;
import com.faforever.client.test.PlatformTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CancelActionNotifyMeMenuItemTest extends PlatformTest {

  @Mock
  private I18n i18n;
  @Mock
  private LiveReplayService liveReplayService;


  private CancelActionNotifyMeMenuItem instance;

  @BeforeEach
  public void setUp() throws Exception {
    instance = new CancelActionNotifyMeMenuItem(i18n, liveReplayService);
  }

  @Test
  public void testOnClickedCancelActionNotifyMe() {
    instance.onClicked();
    verify(liveReplayService).stopTrackingLiveReplay();
  }

  @Test
  public void testVisibleItem() {
    GameBean game = GameBeanBuilder.create().defaultValues().get();
    when(liveReplayService.getTrackingLiveReplay()).thenReturn(Optional.of(new TrackingLiveReplay(game.getId(), TrackingLiveReplayAction.NOTIFY_ME)));

    instance.setObject(game);
    assertTrue(instance.isVisible());
  }

  @Test
  public void testInvisibleItemIfNoGame() {
    instance.setObject(null);
    assertFalse(instance.isVisible());
  }

  @Test
  public void testInvisibleItemIfNoStartTimeGame() {
    GameBean game = GameBeanBuilder.create().defaultValues().startTime(null).get();
    instance.setObject(game);
    assertFalse(instance.isVisible());
  }

  @Test
  public void testInvisibleItemIfNoTrackingOwnReplay() {
    GameBean game = GameBeanBuilder.create().defaultValues().id(1).get();
    when(liveReplayService.getTrackingLiveReplay()).thenReturn(Optional.of(new TrackingLiveReplay(2, TrackingLiveReplayAction.NOTIFY_ME)));

    instance.setObject(game);
    assertFalse(instance.isVisible());
  }
}