#
# Sonar, entreprise quality control tool.
# Copyright (C) 2008-2012 SonarSource
# mailto:contact AT sonarsource DOT com
#
# Sonar is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 3 of the License, or (at your option) any later version.
#
# Sonar is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with Sonar; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
#

module Resourceable

  SCOPE_SET='PRJ'
  SCOPE_SPACE='DIR'
  SCOPE_ENTITY='FIL'
  SCOPES=[SCOPE_SET, SCOPE_SPACE, SCOPE_ENTITY]

  QUALIFIER_VIEW='VW'
  QUALIFIER_SUBVIEW='SVW'
  QUALIFIER_PROJECT='TRK'
  QUALIFIER_MODULE='BRC'
  QUALIFIER_DIRECTORY='DIR'
  QUALIFIER_FILE='FIL'
  QUALIFIER_PACKAGE='PAC'
  QUALIFIER_CLASS='CLA'
  QUALIFIER_UNIT_TEST_CLASS='UTS'
  QUALIFIER_LIB='LIB'
  QUALIFIERS=[QUALIFIER_VIEW, QUALIFIER_SUBVIEW, QUALIFIER_PROJECT, QUALIFIER_MODULE, QUALIFIER_DIRECTORY, QUALIFIER_PACKAGE, QUALIFIER_FILE, QUALIFIER_CLASS, QUALIFIER_UNIT_TEST_CLASS, QUALIFIER_LIB]
  QUALIFIER_NAMES={
    QUALIFIER_VIEW => 'view',
    QUALIFIER_SUBVIEW => 'sub_view',
    QUALIFIER_PROJECT => 'project',
    QUALIFIER_MODULE => 'sub_project',
    QUALIFIER_DIRECTORY => 'directory',
    QUALIFIER_PACKAGE => 'package',
    QUALIFIER_FILE => 'file',
    QUALIFIER_CLASS => 'class',
    QUALIFIER_UNIT_TEST_CLASS => 'unit_test',
    QUALIFIER_LIB => 'library'
  }

  def set?
    scope==SCOPE_SET
  end

  def space?
    scope==SCOPE_SPACE
  end

  def entity?
    scope==SCOPE_ENTITY
  end

  def library?
    qualifier==QUALIFIER_LIB
  end

  def view?
    qualifier==QUALIFIER_VIEW
  end

  def subview?
    qualifier==QUALIFIER_SUBVIEW
  end

  def project?
    qualifier==QUALIFIER_PROJECT
  end

  def module?
    qualifier==QUALIFIER_MODULE
  end

  def directory?
    qualifier==QUALIFIER_DIRECTORY
  end

  def file?
    qualifier==QUALIFIER_FILE
  end

  def source_code?
    java_definition.hasSourceCode()
  end

  def display_dashboard?
    !source_code?
  end

  def java_definition
    @java_definition ||=
      begin
        Java::OrgSonarServerUi::JRubyFacade.getInstance().getResourceDefinition(qualifier)
      end
  end

  def self.qualifier_name(qualifier)
    QUALIFIER_NAMES[qualifier]
  end
end
