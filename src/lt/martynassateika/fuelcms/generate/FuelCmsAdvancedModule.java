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

/**
 * Represents a FuelCMS advanced module class.
 */
public class FuelCmsAdvancedModule implements GenerateTarget {

  private final String name;

  /**
   * @param name advanced module name
   */
  public FuelCmsAdvancedModule(@NotNull String name) {
    this.name = name;
  }

  @Override
  public List<String> getCommand() {
    List<String> strings = new ArrayList<>(3);
    strings.add("index.php");
    strings.add("fuel/generate/advanced");
    strings.add(name);
    return strings;
  }

  @Override
  public String getName() {
    return name;
  }

}
