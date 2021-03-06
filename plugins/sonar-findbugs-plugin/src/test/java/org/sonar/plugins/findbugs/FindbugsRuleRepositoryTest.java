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
package org.sonar.plugins.findbugs;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;
import org.sonar.api.platform.ServerFileSystem;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.XMLRuleParser;

public class FindbugsRuleRepositoryTest {

  @Test
  public void testLoadRepositoryFromXml() {
    ServerFileSystem fileSystem = mock(ServerFileSystem.class);
    FindbugsRuleRepository repository = new FindbugsRuleRepository(fileSystem, new XMLRuleParser());
    List<Rule> rules = repository.createRules();
    assertThat(rules.size(), greaterThan(300));
    for (Rule rule : rules) {
      assertNotNull(rule.getKey());
      assertNotNull(rule.getConfigKey());
      assertNotNull(rule.getName());
    }
  }
}
