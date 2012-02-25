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
package org.sonar.plugins.dbcleaner.period;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.DateUtils;
import org.sonar.core.purge.PurgeDao;
import org.sonar.core.purge.PurgeSnapshotQuery;
import org.sonar.core.purge.PurgeableSnapshotDto;
import org.sonar.plugins.dbcleaner.api.PeriodCleaner;

import java.util.List;

public class DefaultPeriodCleaner implements PeriodCleaner {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultPeriodCleaner.class);
  private PurgeDao purgeDao;
  private Settings settings;

  public DefaultPeriodCleaner(PurgeDao purgeDao, Settings settings) {
    this.purgeDao = purgeDao;
    this.settings = settings;
  }

  public void purge(Project project, int projectSnapshotId) {
    purge((long) project.getId());
  }

  public void purge(long projectId) {
    doPurge(projectId, new Filters(settings).getFilters());
  }

  @VisibleForTesting
  void doPurge(long projectId, List<Filter> filters) {
    List<PurgeableSnapshotDto> history = selectProjectSnapshots(projectId);
    for (Filter filter : filters) {
      filter.log();
      delete(filter.filter(history));
    }
  }

  private void delete(List<PurgeableSnapshotDto> snapshots) {
    for (PurgeableSnapshotDto snapshot : snapshots) {
      LOG.info("<- Delete snapshot: " + DateUtils.formatDateTime(snapshot.getDate()) + " [" + snapshot.getSnapshotId() + "]");
      purgeDao.deleteSnapshots(PurgeSnapshotQuery.create().setRootSnapshotId(snapshot.getSnapshotId()));
    }
  }

  private List<PurgeableSnapshotDto> selectProjectSnapshots(long resourceId) {
    return purgeDao.selectPurgeableSnapshots(resourceId);
  }
}
