/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2011 SonarSource
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
package org.sonar.markdown;

import org.sonar.channel.RegexChannel;

/**
 * Markdown treats double simple quotes ('') as indicators of code. Text wrapped with two '' will be wrapped with an HTML <code> tag.
 * 
 * E.g., the input ''printf()'' will produce <code>printf()</code>
 */
class HtmlCodeChannel extends RegexChannel<MarkdownOutput> {

  public HtmlCodeChannel() {
    super("''.+?''");
  }

  @Override
  protected void consume(CharSequence token, MarkdownOutput output) {
    output.append("<code>");
    output.append(token.subSequence(2, token.length() - 2));
    output.append("</code>");
  }
}