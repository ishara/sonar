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
package org.sonar.server.platform;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.sonar.jpa.test.AbstractDbUnitTestCase;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;

public class ServerImplTest extends AbstractDbUnitTestCase {

  @Test
  public void alwaysReturnTheSameValues() {
    ServerImpl server = new ServerImpl(getSessionFactory());
    server.start();

    assertNotNull(server.getId());
    assertEquals(server.getId(), server.getId());

    assertNotNull(server.getVersion());
    assertEquals(server.getVersion(), server.getVersion());

    assertNotNull(server.getStartedAt());
    assertEquals(server.getStartedAt(), server.getStartedAt());
  }

  @Test
  public void getVersionFromFile() throws IOException {
    assertEquals("1.0", new ServerImpl(getSessionFactory()).loadVersionFromManifest("/org/sonar/server/platform/ServerImplTest/pom-with-version.properties"));
  }

  @Test
  public void testFileWithNoVersion() throws IOException {
    assertEquals("", new ServerImpl(getSessionFactory()).loadVersionFromManifest("/org/sonar/server/platform/ServerImplTest/pom-without-version.properties"));
  }

  @Test
  public void testFileWithEmptyVersionParameter() throws IOException {
    assertEquals("", new ServerImpl(getSessionFactory()).loadVersionFromManifest("/org/sonar/server/platform/ServerImplTest/pom-with-empty-version.properties"));
  }

  @Test
  public void shouldNotFailIfFileNotFound() throws IOException {
    assertEquals("", new ServerImpl(getSessionFactory()).loadVersionFromManifest("/org/sonar/server/platform/ServerImplTest/unknown-file.properties"));
  }

  @Test
  public void shouldLoadServerIdFromDatabase() {
    setupData("shouldLoadServerIdFromDatabase");

    ServerImpl server = new ServerImpl(getSessionFactory(), new Date());
    server.start();

    assertThat(server.getPermanentServerId(), Is.is("abcde"));
  }
}
