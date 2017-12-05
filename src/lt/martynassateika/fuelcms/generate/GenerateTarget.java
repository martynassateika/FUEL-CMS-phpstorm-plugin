/*
 * Copyright 2017 Martynas Sateika
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

import java.util.List;

/**
 * Represents the target of a FuelCMS generate command.
 * <p>
 * This includes models, simple modules and advanced modules.
 */
public interface GenerateTarget {

    /**
     * Returns the command to execute.
     * <p>
     * The executable is not included, the first element represents the 'index.php' file.
     *
     * @return the 'generate' command, separated at spaces
     */
    List<String> getCommand();

    /**
     * @return name of the target
     */
    String getName();

}
