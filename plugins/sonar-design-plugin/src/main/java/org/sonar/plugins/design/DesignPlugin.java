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
package org.sonar.plugins.design;

import com.google.common.collect.Lists;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.design.batch.*;
import org.sonar.plugins.design.ui.dependencies.GwtDependenciesTab;
import org.sonar.plugins.design.ui.libraries.GwtLibrariesPage;
import org.sonar.plugins.design.ui.page.GwtDesignPage;
import org.sonar.plugins.design.ui.widgets.ChidamberKemererWidget;
import org.sonar.plugins.design.ui.widgets.FileDesignWidget;
import org.sonar.plugins.design.ui.widgets.PackageDesignWidget;

import java.util.List;

public class DesignPlugin extends SonarPlugin {

  public List getExtensions() {
    List extensions = Lists.newArrayList();

    // Batch
    extensions.add(MavenDependenciesSensor.class);
    extensions.add(ProjectDsmDecorator.class);
    extensions.add(PackageTangleIndexDecorator.class);
    extensions.add(FileTangleIndexDecorator.class);
    extensions.add(SuspectLcom4DensityDecorator.class);
    extensions.add(GwtLibrariesPage.class);

    // UI
    extensions.add(GwtDesignPage.class);
    extensions.add(GwtDependenciesTab.class);
    extensions.add(FileDesignWidget.class);
    extensions.add(PackageDesignWidget.class);
    extensions.add(ChidamberKemererWidget.class);

    return extensions;
  }
}
