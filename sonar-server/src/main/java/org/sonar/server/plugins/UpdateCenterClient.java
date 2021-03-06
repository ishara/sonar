/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2012 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.server.plugins;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.ServerComponent;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.HttpDownloader;
import org.sonar.api.utils.Logs;
import org.sonar.updatecenter.common.UpdateCenter;
import org.sonar.updatecenter.common.UpdateCenterDeserializer;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;


/**
 * HTTP client to load data from the remote update center hosted at http://update.sonarsource.org.
 *
 * @since 2.4
 */
@Properties({
    @Property(
        key = "sonar.updatecenter.activate",
        defaultValue = "true",
        name = "Enable Update Center",
        project = false,
        global = false, // hidden from UI
        category = "Update Center"),
    @Property(
        key = UpdateCenterClient.URL_PROPERTY,
        defaultValue = "http://update.sonarsource.org/update-center.properties",
        name = "Update Center URL",
        project = false,
        global = false, // hidden from UI
        category = "Update Center")
})
public class UpdateCenterClient implements ServerComponent {

  public static final String URL_PROPERTY = "sonar.updatecenter.url";
  public static final int PERIOD_IN_MILLISECONDS = 60 * 60 * 1000;

  private URI uri;
  private UpdateCenter center = null;
  private long lastRefreshDate = 0;
  private HttpDownloader downloader;

  /**
   * for unit tests
   */
  UpdateCenterClient(HttpDownloader downloader, URI uri) {
    this.downloader = downloader;
    this.uri = uri;
    Logs.INFO.info("Update center: " + uri + " (" + downloader.getProxySynthesis(uri) + ")");
  }

  public UpdateCenterClient(HttpDownloader downloader, Settings configuration) throws URISyntaxException {
    this(downloader, new URI(configuration.getString(URL_PROPERTY)));
  }

  public UpdateCenter getCenter() {
    return getCenter(false);
  }

  public UpdateCenter getCenter(boolean forceRefresh) {
    if (center == null || forceRefresh || needsRefresh()) {
      center = download();
      lastRefreshDate = System.currentTimeMillis();
    }
    return center;
  }

  public Date getLastRefreshDate() {
    return lastRefreshDate > 0 ? new Date(lastRefreshDate) : null;
  }

  private boolean needsRefresh() {
    return lastRefreshDate + PERIOD_IN_MILLISECONDS < System.currentTimeMillis();
  }

  private UpdateCenter download() {
    InputStream input = null;
    try {
      input = downloader.openStream(uri);
      if (input != null) {
        java.util.Properties properties = new java.util.Properties();
        properties.load(input);
        return UpdateCenterDeserializer.fromProperties(properties);
      }

    } catch (Exception e) {
      LoggerFactory.getLogger(getClass()).error("Fail to download data from update center", e);

    } finally {
      IOUtils.closeQuietly(input);
    }
    return null;
  }
}
