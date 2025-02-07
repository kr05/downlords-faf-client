package com.faforever.client.map.generator;

import com.faforever.client.config.ClientProperties;
import com.faforever.client.fx.PlatformService;
import com.faforever.client.i18n.I18n;
import com.faforever.client.task.CompletableTask;
import com.faforever.client.task.ResourceLocks;
import com.faforever.commons.io.ByteCopier;
import com.google.common.annotations.VisibleForTesting;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class DownloadMapGeneratorTask extends CompletableTask<Void> {

  private final MapGeneratorService mapGeneratorService;
  private final ClientProperties clientProperties;
  private final I18n i18n;
  private final PlatformService platformService;

  @Setter
  @Getter
  @VisibleForTesting
  private ComparableVersion version;

  @Autowired
  public DownloadMapGeneratorTask(MapGeneratorService mapGeneratorService, ClientProperties clientProperties, I18n i18n, PlatformService platformService) {
    super(Priority.HIGH);

    this.mapGeneratorService = mapGeneratorService;
    this.clientProperties = clientProperties;
    this.i18n = i18n;
    this.platformService = platformService;
  }

  @Override
  protected Void call() throws Exception {
    Objects.requireNonNull(version, "Version hasn't been set.");

    updateTitle(i18n.get("game.mapGeneration.downloadGenerator.title", version));

    URL url = new URL(String.format(clientProperties.getMapGenerator().getDownloadUrlFormat(), version));

    URLConnection urlConnection = url.openConnection();

    Path targetFile = mapGeneratorService.getGeneratorExecutablePath(version);
    Files.createDirectories(targetFile.getParent());
    Path tempFile = Files.createTempFile(targetFile.getParent(), "generator", null);

    ResourceLocks.acquireDownloadLock();
    try (InputStream inputStream = url.openStream(); OutputStream outputStream = Files.newOutputStream(tempFile)) {
      ByteCopier.from(inputStream)
          .to(outputStream)
          .totalBytes(urlConnection.getContentLength())
          .listener(this::updateProgress)
          .copy();

      Files.move(tempFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
    } finally {
      ResourceLocks.freeDownloadLock();
      try {
        Files.deleteIfExists(tempFile);
      } catch (IOException e) {
        log.warn("Could not delete temporary file: `{}`", tempFile, e);
      }
    }

    platformService.setUnixExecutableAndWritableBits(targetFile);

    return null;
  }
}