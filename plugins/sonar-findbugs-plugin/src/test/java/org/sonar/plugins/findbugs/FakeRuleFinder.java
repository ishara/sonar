/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
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

import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.rules.XMLRuleParser;

import java.util.Collection;
import java.util.List;

public class FakeRuleFinder implements RuleFinder {

  private final List<Rule> findbugsRules;

  public FakeRuleFinder() {
    FindbugsRuleRepository repo = new FindbugsRuleRepository(new XMLRuleParser());
    findbugsRules = repo.createRules();
    for (Rule rule : findbugsRules) {
      rule.setRepositoryKey(FindbugsConstants.REPOSITORY_KEY);
    }
  }

  public Rule findById(int ruleId) {
    throw new UnsupportedOperationException();
  }

  public Rule findByKey(String repositoryKey, String key) {
    for (Rule rule : findbugsRules) {
      if (rule.getKey().equals(key)) {
        return rule;
      }
    }
    return null;
  }

  public Rule find(RuleQuery query) {
    throw new UnsupportedOperationException();
  }

  public Collection<Rule> findAll(RuleQuery query) {
    return findbugsRules;
  }
}