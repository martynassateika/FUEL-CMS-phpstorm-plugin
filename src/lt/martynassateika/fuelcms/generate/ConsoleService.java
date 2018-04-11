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

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

/**
 * Allows easy access to the {@link ConsoleView} associated with the FuelCMS Log {@link ToolWindow}.
 */
public class ConsoleService {

    private final ConsoleView consoleView;

    public ConsoleService(Project project) {
        this.consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project)
                .getConsole();
    }

    public ConsoleView getConsoleView() {
        return consoleView;
    }

}
