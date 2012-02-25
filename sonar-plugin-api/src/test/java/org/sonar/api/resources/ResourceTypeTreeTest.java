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
package org.sonar.api.resources;

import org.junit.Test;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.sonar.api.resources.ResourceTypesTest.qualifiers;

public class ResourceTypeTreeTest {
  private final ResourceTypeTree tree = ResourceTypeTree.builder()
      .addType(ResourceType.builder("TRK").build())
      .addType(ResourceType.builder("DIR").build())
      .addType(ResourceType.builder("FIL").build())
      .addType(ResourceType.builder("UTS").build())
      .addRelations("TRK", "DIR")
      .addRelations("DIR", "FIL")
      .addRelations("DIR", "UTS")
      .build();


  @Test
  public void getTypes() {
    assertThat(tree.getTypes().size(), is(4));
    assertThat(qualifiers(tree.getTypes()), hasItems("TRK", "DIR", "FIL", "UTS"));
  }

  @Test
  public void getChildren() {
    assertThat(tree.getChildren("TRK").size(), is(1));
    assertThat(tree.getChildren("TRK"), hasItems("DIR"));

    assertThat(tree.getChildren("DIR").size(), is(2));
    assertThat(tree.getChildren("DIR"), hasItems("FIL", "UTS"));

    assertThat(tree.getChildren("FIL").size(), is(0));
  }

  @Test
  public void getLeaves() {
    assertThat(tree.getLeaves().size(), is(2));
    assertThat(tree.getLeaves(), hasItems("FIL", "UTS"));
  }

}
