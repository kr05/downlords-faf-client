package com.faforever.client.game;

import com.faforever.client.i18n.I18n;
import com.faforever.client.map.generator.GenerationType;
import com.faforever.client.map.generator.GeneratorOptions;
import com.faforever.client.map.generator.MapGeneratorService;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.preferences.GeneratorPrefs;
import com.faforever.client.test.PlatformTest;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GenerateMapControllerTest extends PlatformTest {


  @Mock
  private
  NotificationService notificationService;
  @Mock
  private MapGeneratorService mapGeneratorService;
  @Mock
  private I18n i18n;
  @Mock
  private CreateGameController createGameController;
  @Spy
  private GeneratorPrefs generatorPrefs;

  @InjectMocks
  private GenerateMapController instance;

  public void unbindProperties() {
    generatorPrefs.generationTypeProperty().unbind();
    generatorPrefs.spawnCountProperty().unbind();
    generatorPrefs.mapSizeInKmProperty().unbind();
    generatorPrefs.numTeamsProperty().unbind();
    generatorPrefs.waterRandomProperty().unbind();
    generatorPrefs.plateauRandomProperty().unbind();
    generatorPrefs.mountainRandomProperty().unbind();
    generatorPrefs.rampRandomProperty().unbind();
    generatorPrefs.mexRandomProperty().unbind();
    generatorPrefs.reclaimRandomProperty().unbind();
    generatorPrefs.waterDensityProperty().unbind();
    generatorPrefs.plateauDensityProperty().unbind();
    generatorPrefs.mountainDensityProperty().unbind();
    generatorPrefs.rampDensityProperty().unbind();
    generatorPrefs.mexDensityProperty().unbind();
    generatorPrefs.reclaimDensityProperty().unbind();
    generatorPrefs.commandLineArgsProperty().unbind();
  }

  @BeforeEach
  public void setUp() throws Exception {
    generatorPrefs.setSpawnCount(10);
    generatorPrefs.setMapSizeInKm(10.0);

    loadFxml("theme/play/generate_map.fxml", clazz -> instance);
    unbindProperties();
  }

  @Test
  public void testBadMapNameFails() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.previousMapName.setText("Bad");
    instance.onGenerateMap();

    verify(notificationService).addImmediateWarnNotification("mapGenerator.invalidName");
  }

  @Test
  public void testSetLastSpawnCount() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertEquals(10, instance.spawnCountSpinner.getValue().intValue());
  }

  @Test
  public void testSetLastNumTeams() {
    generatorPrefs.setNumTeams(5);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertEquals(instance.numTeamsSpinner.getValue().intValue(), 5);
  }

  @Test
  public void testSetLastMapSize() {

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertEquals(instance.mapSizeSpinner.getValue(), 10.0);
    assertEquals((int) instance.spawnCountSpinner.getValue(), 10);
  }

  @Test
  public void testSetLastMapStyle() {
    generatorPrefs.setMapStyle("TEST");

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    instance.setStyles(new ArrayList<>(List.of("TEST")));

    assertEquals(instance.mapStyleComboBox.getValue(), "TEST");
  }

  @Test
  public void testSetLastBiome() {
    generatorPrefs.setBiome("Test");

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    instance.setBiomes(new ArrayList<>(List.of("Test")));

    assertEquals(instance.biomeComboBox.getValue(), "Test");
  }

  @Test
  public void testSetLastWaterRandom() {
    generatorPrefs.setWaterRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.waterRandom.isSelected());
  }

  @Test
  public void testSetLastPlateauRandom() {
    generatorPrefs.setPlateauRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.plateauRandom.isSelected());
  }

  @Test
  public void testSetLastMountainRandom() {
    generatorPrefs.setMountainRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.mountainRandom.isSelected());
  }

  @Test
  public void testSetLastRampRandom() {
    generatorPrefs.setRampRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.rampRandom.isSelected());
  }

  @Test
  public void testSetLastMexRandom() {
    generatorPrefs.setMexRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.mexRandom.isSelected());
  }

  @Test
  public void testSetLastReclaimRandom() {
    generatorPrefs.setReclaimRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.reclaimRandom.isSelected());
  }

  @Test
  public void testSetLastWaterSlider() {
    generatorPrefs.setWaterDensity(71);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertEquals(instance.waterSlider.getValue(), 71, 0);
  }

  @Test
  public void testSetLastMountainSlider() {
    generatorPrefs.setMountainDensity(71);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertEquals(instance.mountainSlider.getValue(), 71, 0);
  }

  @Test
  public void testSetLastPlateauSlider() {
    generatorPrefs.setPlateauDensity(71);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertEquals(instance.plateauSlider.getValue(), 71, 0);
  }

  @Test
  public void testSetLastRampSlider() {
    generatorPrefs.setRampDensity(71);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertEquals(instance.rampSlider.getValue(), 71, 0);
  }

  @Test
  public void testSetLastMexSlider() {
    generatorPrefs.setMexDensity(71);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertEquals(instance.mexSlider.getValue(), 71, 0);
  }

  @Test
  public void testSetLastReclaimSlider() {
    generatorPrefs.setReclaimDensity(71);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertEquals(instance.reclaimSlider.getValue(), 71, 0);
  }

  @Test
  public void testSetLastCommandLineArgs() {
    generatorPrefs.setCommandLineArgs("--help");

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertEquals("--help", instance.commandLineArgsText.getText());
    assertTrue(instance.commandLineArgsText.isVisible());
    assertTrue(instance.commandLineLabel.isVisible());
  }

  @Test
  public void testCommandLineArgsNotVisibleWhenNotSetInitially() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.commandLineArgsText.isVisible());
    assertFalse(instance.commandLineLabel.isVisible());
  }

  @Test
  public void testToggleCommandLineArgs() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.commandLineArgsText.isVisible());
    assertFalse(instance.commandLineLabel.isVisible());

    WaitForAsyncUtils.asyncFx(() -> instance.toggleCommandlineInput());
    WaitForAsyncUtils.waitForFxEvents();

    assertTrue(instance.commandLineArgsText.isVisible());
    assertTrue(instance.commandLineLabel.isVisible());
  }

  @Test
  public void testStylesVisibleWhenPopulated() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    instance.setStyles(new ArrayList<>(List.of("TEST")));

    assertTrue(instance.mapStyleLabel.isVisible());
    assertTrue(instance.mapStyleComboBox.isVisible());
  }

  @Test
  public void testStylesNotVisibleWhenNotPopulated() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.mapStyleLabel.isVisible());
    assertFalse(instance.mapStyleComboBox.isVisible());
  }

  @Test
  public void testBiomesVisibleWhenPopulated() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    instance.setBiomes(new ArrayList<>(List.of("Test")));

    assertTrue(instance.biomeLabel.isVisible());
    assertTrue(instance.biomeComboBox.isVisible());
  }

  @Test
  public void testBiomesNotVisibleWhenNotPopulated() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.biomeLabel.isVisible());
    assertFalse(instance.biomeComboBox.isVisible());
  }

  @Test
  public void testWaterSliderVisibilityWhenRandom() {
    generatorPrefs.setWaterRandom(true);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.waterSliderBox.isVisible());
  }

  @Test
  public void testPlateauSliderVisibilityWhenRandom() {
    generatorPrefs.setPlateauRandom(true);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.plateauSliderBox.isVisible());
  }

  @Test
  public void testMountainSliderVisibilityWhenRandom() {
    generatorPrefs.setMountainRandom(true);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.mountainSliderBox.isVisible());
  }

  @Test
  public void testRampSliderVisibilityWhenRandom() {
    generatorPrefs.setRampRandom(true);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.rampSliderBox.isVisible());
  }

  @Test
  public void testMexSliderVisibilityWhenRandom() {
    generatorPrefs.setMexRandom(true);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.mexSliderBox.isVisible());
  }

  @Test
  public void testReclaimSliderVisibilityWhenRandom() {
    generatorPrefs.setReclaimRandom(true);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertFalse(instance.reclaimSliderBox.isVisible());
  }

  @Test
  public void testWaterSliderVisibilityWhenNotRandom() {
    generatorPrefs.setWaterRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertTrue(instance.waterSliderBox.isVisible());
  }

  @Test
  public void testPlateauSliderVisibilityWhenNotRandom() {
    generatorPrefs.setPlateauRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertTrue(instance.plateauSliderBox.isVisible());
  }

  @Test
  public void testMountainSliderVisibilityWhenNotRandom() {
    generatorPrefs.setMountainRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertTrue(instance.mountainSliderBox.isVisible());
  }

  @Test
  public void testRampSliderVisibilityWhenNotRandom() {
    generatorPrefs.setRampRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertTrue(instance.rampSliderBox.isVisible());
  }

  @Test
  public void testMexSliderVisibilityWhenNotRandom() {
    generatorPrefs.setMexRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertTrue(instance.mexSliderBox.isVisible());
  }

  @Test
  public void testReclaimSliderVisibilityWhenNotRandom() {
    generatorPrefs.setReclaimRandom(false);

    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();

    assertTrue(instance.reclaimSliderBox.isVisible());
  }

  @Test
  public void testOptionsNotDisabledWithoutMapName() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.previousMapName.setText("neroxis_map_generator");
    instance.previousMapName.setText("");

    assertFalse(instance.generationTypeComboBox.isDisabled());
    assertFalse(instance.rampRandomBox.isDisabled());
    assertFalse(instance.rampSliderBox.isDisabled());
    assertFalse(instance.waterRandomBox.isDisabled());
    assertFalse(instance.waterSliderBox.isDisabled());
    assertFalse(instance.plateauRandomBox.isDisabled());
    assertFalse(instance.plateauSliderBox.isDisabled());
    assertFalse(instance.mountainRandomBox.isDisabled());
    assertFalse(instance.mountainSliderBox.isDisabled());
    assertFalse(instance.reclaimRandomBox.isDisabled());
    assertFalse(instance.reclaimSliderBox.isDisabled());
    assertFalse(instance.mexRandomBox.isDisabled());
    assertFalse(instance.mexSliderBox.isDisabled());
    assertFalse(instance.mapStyleComboBox.isDisabled());
    assertFalse(instance.biomeComboBox.isDisabled());
  }

  @Test
  public void testOptionsDisabledWithMapName() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.previousMapName.setText("neroxis_map_generator");

    assertTrue(instance.commandLineArgsText.isDisabled());
    assertTrue(instance.generationTypeComboBox.isDisabled());
    assertTrue(instance.rampRandomBox.isDisabled());
    assertTrue(instance.rampSliderBox.isDisabled());
    assertTrue(instance.waterRandomBox.isDisabled());
    assertTrue(instance.waterSliderBox.isDisabled());
    assertTrue(instance.plateauRandomBox.isDisabled());
    assertTrue(instance.plateauSliderBox.isDisabled());
    assertTrue(instance.mountainRandomBox.isDisabled());
    assertTrue(instance.mountainSliderBox.isDisabled());
    assertTrue(instance.reclaimRandomBox.isDisabled());
    assertTrue(instance.reclaimSliderBox.isDisabled());
    assertTrue(instance.mexRandomBox.isDisabled());
    assertTrue(instance.mexSliderBox.isDisabled());
    assertTrue(instance.mapStyleComboBox.isDisabled());
    assertTrue(instance.biomeComboBox.isDisabled());
  }

  @Test
  public void testOptionsDisabledWithCommandLine() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.commandLineArgsText.setText("--help");

    assertTrue(instance.generationTypeComboBox.isDisabled());
    assertTrue(instance.rampRandomBox.isDisabled());
    assertTrue(instance.rampSliderBox.isDisabled());
    assertTrue(instance.waterRandomBox.isDisabled());
    assertTrue(instance.waterSliderBox.isDisabled());
    assertTrue(instance.plateauRandomBox.isDisabled());
    assertTrue(instance.plateauSliderBox.isDisabled());
    assertTrue(instance.mountainRandomBox.isDisabled());
    assertTrue(instance.mountainSliderBox.isDisabled());
    assertTrue(instance.reclaimRandomBox.isDisabled());
    assertTrue(instance.reclaimSliderBox.isDisabled());
    assertTrue(instance.mexRandomBox.isDisabled());
    assertTrue(instance.mexSliderBox.isDisabled());
    assertTrue(instance.mapStyleComboBox.isDisabled());
    assertTrue(instance.biomeComboBox.isDisabled());
  }

  @Test
  public void testOptionsDisabledWithStyle() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.mapStyleComboBox.setItems(FXCollections.observableList(List.of("TEST")));
    instance.mapStyleComboBox.getSelectionModel().selectFirst();

    assertTrue(instance.rampRandomBox.isDisabled());
    assertTrue(instance.rampSliderBox.isDisabled());
    assertTrue(instance.waterRandomBox.isDisabled());
    assertTrue(instance.waterSliderBox.isDisabled());
    assertTrue(instance.plateauRandomBox.isDisabled());
    assertTrue(instance.plateauSliderBox.isDisabled());
    assertTrue(instance.mountainRandomBox.isDisabled());
    assertTrue(instance.mountainSliderBox.isDisabled());
    assertTrue(instance.reclaimRandomBox.isDisabled());
    assertTrue(instance.reclaimSliderBox.isDisabled());
    assertTrue(instance.mexRandomBox.isDisabled());
    assertTrue(instance.mexSliderBox.isDisabled());
    assertTrue(instance.biomeComboBox.isDisabled());
  }

  @Test
  public void testOptionsNotDisabledWithNoStyle() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.mapStyleComboBox.getSelectionModel().clearSelection();

    assertFalse(instance.rampRandomBox.isDisabled());
    assertFalse(instance.rampSliderBox.isDisabled());
    assertFalse(instance.waterRandomBox.isDisabled());
    assertFalse(instance.waterSliderBox.isDisabled());
    assertFalse(instance.plateauRandomBox.isDisabled());
    assertFalse(instance.plateauSliderBox.isDisabled());
    assertFalse(instance.mountainRandomBox.isDisabled());
    assertFalse(instance.mountainSliderBox.isDisabled());
    assertFalse(instance.reclaimRandomBox.isDisabled());
    assertFalse(instance.reclaimSliderBox.isDisabled());
    assertFalse(instance.mexRandomBox.isDisabled());
    assertFalse(instance.mexSliderBox.isDisabled());
    assertFalse(instance.biomeComboBox.isDisabled());
  }

  @Test
  public void testOptionsNotDisabledWithRandomStyle() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.mapStyleComboBox.setItems(FXCollections.observableList(List.of(MapGeneratorService.GENERATOR_RANDOM_STYLE)));
    instance.mapStyleComboBox.getSelectionModel().selectFirst();

    assertFalse(instance.rampRandomBox.isDisabled());
    assertFalse(instance.rampSliderBox.isDisabled());
    assertFalse(instance.waterRandomBox.isDisabled());
    assertFalse(instance.waterSliderBox.isDisabled());
    assertFalse(instance.plateauRandomBox.isDisabled());
    assertFalse(instance.plateauSliderBox.isDisabled());
    assertFalse(instance.mountainRandomBox.isDisabled());
    assertFalse(instance.mountainSliderBox.isDisabled());
    assertFalse(instance.reclaimRandomBox.isDisabled());
    assertFalse(instance.reclaimSliderBox.isDisabled());
    assertFalse(instance.mexRandomBox.isDisabled());
    assertFalse(instance.mexSliderBox.isDisabled());
    assertFalse(instance.biomeComboBox.isDisabled());
  }

  @Test
  public void testStyleDisabledWithBiome() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.biomeComboBox.setItems(FXCollections.observableList(List.of("Test")));
    instance.biomeComboBox.getSelectionModel().selectFirst();

    assertFalse(instance.rampRandomBox.isDisabled());
    assertFalse(instance.rampSliderBox.isDisabled());
    assertFalse(instance.waterRandomBox.isDisabled());
    assertFalse(instance.waterSliderBox.isDisabled());
    assertFalse(instance.plateauRandomBox.isDisabled());
    assertFalse(instance.plateauSliderBox.isDisabled());
    assertFalse(instance.mountainRandomBox.isDisabled());
    assertFalse(instance.mountainSliderBox.isDisabled());
    assertFalse(instance.reclaimRandomBox.isDisabled());
    assertFalse(instance.reclaimSliderBox.isDisabled());
    assertFalse(instance.mexRandomBox.isDisabled());
    assertFalse(instance.mexSliderBox.isDisabled());

    assertTrue(instance.mapStyleComboBox.isDisabled());
  }

  @Test
  public void testOptionsNotDisabledWithNoBiome() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.biomeComboBox.getSelectionModel().clearSelection();

    assertFalse(instance.rampRandomBox.isDisabled());
    assertFalse(instance.rampSliderBox.isDisabled());
    assertFalse(instance.waterRandomBox.isDisabled());
    assertFalse(instance.waterSliderBox.isDisabled());
    assertFalse(instance.plateauRandomBox.isDisabled());
    assertFalse(instance.plateauSliderBox.isDisabled());
    assertFalse(instance.mountainRandomBox.isDisabled());
    assertFalse(instance.mountainSliderBox.isDisabled());
    assertFalse(instance.reclaimRandomBox.isDisabled());
    assertFalse(instance.reclaimSliderBox.isDisabled());
    assertFalse(instance.mexRandomBox.isDisabled());
    assertFalse(instance.mexSliderBox.isDisabled());
    assertFalse(instance.mapStyleComboBox.isDisabled());
  }

  @Test
  public void testOptionsNotDisabledWithRandomBiome() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.biomeComboBox.setItems(FXCollections.observableList(List.of(MapGeneratorService.GENERATOR_RANDOM_BIOME)));
    instance.biomeComboBox.getSelectionModel().selectFirst();

    assertFalse(instance.rampRandomBox.isDisabled());
    assertFalse(instance.rampSliderBox.isDisabled());
    assertFalse(instance.waterRandomBox.isDisabled());
    assertFalse(instance.waterSliderBox.isDisabled());
    assertFalse(instance.plateauRandomBox.isDisabled());
    assertFalse(instance.plateauSliderBox.isDisabled());
    assertFalse(instance.mountainRandomBox.isDisabled());
    assertFalse(instance.mountainSliderBox.isDisabled());
    assertFalse(instance.reclaimRandomBox.isDisabled());
    assertFalse(instance.reclaimSliderBox.isDisabled());
    assertFalse(instance.mexRandomBox.isDisabled());
    assertFalse(instance.mexSliderBox.isDisabled());
    assertFalse(instance.mapStyleComboBox.isDisabled());
  }

  @Test
  public void testOptionsNotDisabledWithCasual() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.generationTypeComboBox.setValue(GenerationType.TOURNAMENT);
    instance.generationTypeComboBox.setValue(GenerationType.CASUAL);

    assertFalse(instance.rampRandomBox.isDisabled());
    assertFalse(instance.rampSliderBox.isDisabled());
    assertFalse(instance.waterRandomBox.isDisabled());
    assertFalse(instance.waterSliderBox.isDisabled());
    assertFalse(instance.plateauRandomBox.isDisabled());
    assertFalse(instance.plateauSliderBox.isDisabled());
    assertFalse(instance.mountainRandomBox.isDisabled());
    assertFalse(instance.mountainSliderBox.isDisabled());
    assertFalse(instance.reclaimRandomBox.isDisabled());
    assertFalse(instance.reclaimSliderBox.isDisabled());
    assertFalse(instance.mexRandomBox.isDisabled());
    assertFalse(instance.mexSliderBox.isDisabled());
    assertFalse(instance.mapStyleComboBox.isDisabled());
    assertFalse(instance.biomeComboBox.isDisabled());
  }

  @Test
  public void testOptionsDisabledWithTournament() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.generationTypeComboBox.setValue(GenerationType.TOURNAMENT);

    assertFalse(instance.generationTypeComboBox.isDisabled());
    assertTrue(instance.rampRandomBox.isDisabled());
    assertTrue(instance.rampSliderBox.isDisabled());
    assertTrue(instance.waterRandomBox.isDisabled());
    assertTrue(instance.waterSliderBox.isDisabled());
    assertTrue(instance.plateauRandomBox.isDisabled());
    assertTrue(instance.plateauSliderBox.isDisabled());
    assertTrue(instance.mountainRandomBox.isDisabled());
    assertTrue(instance.mountainSliderBox.isDisabled());
    assertTrue(instance.reclaimRandomBox.isDisabled());
    assertTrue(instance.reclaimSliderBox.isDisabled());
    assertTrue(instance.mexRandomBox.isDisabled());
    assertTrue(instance.mexSliderBox.isDisabled());
    assertTrue(instance.mapStyleComboBox.isDisabled());
    assertTrue(instance.biomeComboBox.isDisabled());
  }

  @Test
  public void testOptionsDisabledWithBlind() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.generationTypeComboBox.setValue(GenerationType.BLIND);

    assertFalse(instance.generationTypeComboBox.isDisabled());
    assertTrue(instance.rampRandomBox.isDisabled());
    assertTrue(instance.rampSliderBox.isDisabled());
    assertTrue(instance.waterRandomBox.isDisabled());
    assertTrue(instance.waterSliderBox.isDisabled());
    assertTrue(instance.plateauRandomBox.isDisabled());
    assertTrue(instance.plateauSliderBox.isDisabled());
    assertTrue(instance.mountainRandomBox.isDisabled());
    assertTrue(instance.mountainSliderBox.isDisabled());
    assertTrue(instance.reclaimRandomBox.isDisabled());
    assertTrue(instance.reclaimSliderBox.isDisabled());
    assertTrue(instance.mexRandomBox.isDisabled());
    assertTrue(instance.mexSliderBox.isDisabled());
    assertTrue(instance.mapStyleComboBox.isDisabled());
    assertTrue(instance.biomeComboBox.isDisabled());
  }

  @Test
  public void testOptionsDisabledWithUnexplored() {
    WaitForAsyncUtils.asyncFx(() -> reinitialize(instance));
    WaitForAsyncUtils.waitForFxEvents();
    instance.generationTypeComboBox.setValue(GenerationType.UNEXPLORED);

    assertFalse(instance.generationTypeComboBox.isDisabled());
    assertTrue(instance.rampRandomBox.isDisabled());
    assertTrue(instance.rampSliderBox.isDisabled());
    assertTrue(instance.waterRandomBox.isDisabled());
    assertTrue(instance.waterSliderBox.isDisabled());
    assertTrue(instance.plateauRandomBox.isDisabled());
    assertTrue(instance.plateauSliderBox.isDisabled());
    assertTrue(instance.mountainRandomBox.isDisabled());
    assertTrue(instance.mountainSliderBox.isDisabled());
    assertTrue(instance.reclaimRandomBox.isDisabled());
    assertTrue(instance.reclaimSliderBox.isDisabled());
    assertTrue(instance.mexRandomBox.isDisabled());
    assertTrue(instance.mexSliderBox.isDisabled());
    assertTrue(instance.mapStyleComboBox.isDisabled());
    assertTrue(instance.biomeComboBox.isDisabled());
  }

  @Test
  public void testGetGenerateMapWithName() {
    runOnFxThreadAndWait(() -> reinitialize(instance));
    instance.previousMapName.setText("neroxis_map_generator_0.0.0_12345");
    instance.setOnCloseButtonClickedListener(() -> {});
    when(mapGeneratorService.isGeneratedMap("neroxis_map_generator_0.0.0_12345")).thenReturn(true);
    when(mapGeneratorService.generateMap(anyString())).thenReturn(CompletableFuture.completedFuture(null));

    runOnFxThreadAndWait(() -> instance.onGenerateMap());

    verify(mapGeneratorService).generateMap("neroxis_map_generator_0.0.0_12345");
    verify(mapGeneratorService, never()).generateMap(any(GeneratorOptions.class));
  }

  @Test
  public void testGetGenerateMapNoNameNoRandom() {
    generatorPrefs.setWaterRandom(false);
    generatorPrefs.setMountainRandom(false);
    generatorPrefs.setPlateauRandom(false);
    generatorPrefs.setRampRandom(false);
    generatorPrefs.setMexRandom(false);
    generatorPrefs.setReclaimRandom(false);
    generatorPrefs.setWaterDensity(1);
    generatorPrefs.setPlateauDensity(2);
    generatorPrefs.setMountainDensity(3);
    generatorPrefs.setRampDensity(4);
    generatorPrefs.setMexDensity(5);
    generatorPrefs.setReclaimDensity(6);
    generatorPrefs.setSpawnCount(2);
    generatorPrefs.setNumTeams(2);
    generatorPrefs.setMapSizeInKm(10.0);
    generatorPrefs.setGenerationType(GenerationType.CASUAL);

    instance.mapStyleComboBox.setItems(FXCollections.observableList(List.of("TEST")));
    instance.mapStyleComboBox.getSelectionModel().selectFirst();
    instance.biomeComboBox.setItems(FXCollections.observableList(List.of("Test")));
    instance.biomeComboBox.getSelectionModel().selectFirst();

    runOnFxThreadAndWait(() -> reinitialize(instance));

    ArgumentCaptor<GeneratorOptions> captor = ArgumentCaptor.forClass(GeneratorOptions.class);

    runOnFxThreadAndWait(() -> instance.onGenerateMap());

    verify(mapGeneratorService).generateMap(captor.capture());

    GeneratorOptions result = captor.getValue();

    assertEquals(1 - 1 / 127f, result.landDensity(), 0);
    assertEquals(2 / 127f, result.plateauDensity(), 0);
    assertEquals(3 / 127f, result.mountainDensity(), 0);
    assertEquals(4 / 127f, result.rampDensity(), 0);
    assertEquals(5 / 127f, result.mexDensity(), 0);
    assertEquals(6 / 127f, result.reclaimDensity(), 0);
    assertEquals(2, result.spawnCount());
    assertEquals(512, result.mapSize());
    assertEquals(2, result.numTeams());
    assertEquals(GenerationType.CASUAL, result.generationType());
    assertNull(result.commandLineArgs());
    assertEquals("TEST", result.style());
    assertEquals("Test", result.biome());
  }

  @Test
  public void testGetGenerateMapWithCommandLineArgs() {
    generatorPrefs.setCommandLineArgs("--test");

    runOnFxThreadAndWait(() -> reinitialize(instance));

    ArgumentCaptor<GeneratorOptions> captor = ArgumentCaptor.forClass(GeneratorOptions.class);

    runOnFxThreadAndWait(() -> instance.onGenerateMap());

    verify(mapGeneratorService).generateMap(captor.capture());

    GeneratorOptions result = captor.getValue();

    assertEquals(result.commandLineArgs(), "--test");
  }

  @Test
  public void testGetGenerateMapNoNameRandom() {
    generatorPrefs.setWaterRandom(true);
    generatorPrefs.setMountainRandom(true);
    generatorPrefs.setPlateauRandom(true);
    generatorPrefs.setRampRandom(true);
    generatorPrefs.setMexRandom(true);
    generatorPrefs.setReclaimRandom(true);

    runOnFxThreadAndWait(() -> reinitialize(instance));

    ArgumentCaptor<GeneratorOptions> captor = ArgumentCaptor.forClass(GeneratorOptions.class);

    runOnFxThreadAndWait(() -> instance.onGenerateMap());

    verify(mapGeneratorService).generateMap(captor.capture());

    GeneratorOptions result = captor.getValue();

    assertNull(result.landDensity());
    assertNull(result.plateauDensity());
    assertNull(result.mountainDensity());
    assertNull(result.rampDensity());
    assertNull(result.mexDensity());
    assertNull(result.reclaimDensity());
  }
}

