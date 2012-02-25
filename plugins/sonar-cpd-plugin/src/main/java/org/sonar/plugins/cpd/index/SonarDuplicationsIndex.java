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
package org.sonar.plugins.cpd.index;

import com.google.common.collect.Lists;
import org.sonar.api.resources.Resource;
import org.sonar.duplications.block.Block;
import org.sonar.duplications.block.ByteArray;
import org.sonar.duplications.index.AbstractCloneIndex;
import org.sonar.duplications.index.CloneIndex;
import org.sonar.duplications.index.PackedMemoryCloneIndex;

import java.util.Collection;
import java.util.List;

public class SonarDuplicationsIndex extends AbstractCloneIndex {

  private final CloneIndex mem = new PackedMemoryCloneIndex();
  private final DbDuplicationsIndex db;

  public SonarDuplicationsIndex() {
    this.db = null;
  }

  public SonarDuplicationsIndex(DbDuplicationsIndex db) {
    this.db = db;
  }

  public void insert(Resource resource, Collection<Block> blocks) {
    for (Block block : blocks) {
      mem.insert(block);
    }
    if (db != null) {
      db.insert(resource, blocks);
    }
  }

  public Collection<Block> getByResource(Resource resource, String resourceKey) {
    if (db != null) {
      db.prepareCache(resource);
    }
    return mem.getByResourceId(resourceKey);
  }

  public Collection<Block> getBySequenceHash(ByteArray hash) {
    if (db == null) {
      return mem.getBySequenceHash(hash);
    } else {
      List<Block> result = Lists.newArrayList(mem.getBySequenceHash(hash));
      result.addAll(db.getByHash(hash));
      return result;
    }
  }

  public Collection<Block> getByResourceId(String resourceId) {
    throw new UnsupportedOperationException();
  }

  public void insert(Block block) {
    throw new UnsupportedOperationException();
  }

}
