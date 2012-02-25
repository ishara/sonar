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
package org.sonar.core.persistence;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;

public class BatchSessionTest {
  @Test
  public void shouldCommitWhenReachingBatchSize() {
    SqlSession mybatisSession = mock(SqlSession.class);
    BatchSession session = new BatchSession(mybatisSession, 10);

    for (int i = 0; i < 9; i++) {
      session.insert("id" + i);
      verify(mybatisSession).insert("id" + i);
      verify(mybatisSession, never()).commit();
      verify(mybatisSession, never()).commit(anyBoolean());
    }
    session.insert("id9");
    verify(mybatisSession).commit();
  }

  @Test
    public void shouldResetCounterAfterCommit() {
      SqlSession mybatisSession = mock(SqlSession.class);
      BatchSession session = new BatchSession(mybatisSession, 10);

      for (int i = 0; i < 35; i++) {
        session.insert("id" + i);
      }
      verify(mybatisSession, times(3)).commit();
    }
}
