/*
 * Copyright 2018 Martynas Sateika
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lt.martynassateika.fuelcms.generate;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a FuelCMS simple module class.
 */
public class FuelCmsSimpleModule implements GenerateTarget {

  private final String name;

  @Nullable
  private String advancedModule;

  /**
   * @param name simple module name
   * @param advancedModule advanced module this simple module belongs to (null indicates this module
   * is not part of an advanced module, and instead resides in the 'application' directory)
   */
  public FuelCmsSimpleModule(@NotNull String name, @Nullable String advancedModule) {
    this.name = name;
    this.advancedModule = advancedModule;
  }

  @Override
  public List<String> getCommand() {
    List<String> strings = new ArrayList<>(4);
    strings.add("index.php");
    strings.add("fuel/generate/simple");
    strings.add(name);
    if (advancedModule != null) {
      strings.add(advancedModule);
    }
    return strings;
  }

  @Override
  public String getName() {
    return name;
  }

}
