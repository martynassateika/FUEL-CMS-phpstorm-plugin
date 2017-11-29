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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.config.PhpProjectConfigurationFacade;
import com.jetbrains.php.config.interpreters.PhpInterpreter;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper functions for interacting with the FuelCMS CLI.
 */
public class FuelCli {

    private static final Logger LOGGER = Logger.getInstance(FuelCli.class);

    /**
     * Executes a FuelCMS "generate" command, producing <tt>target</tt>.
     *
     * @param project current IDE project
     * @param target  target of the "generate" command
     * @throws ExecutionException if an {@link OSProcessHandler} cannot be set up
     */
    public static void executeGenerateCommand(@NotNull Project project,
                                              @NotNull GenerateTarget target) throws ExecutionException {
        PhpInterpreter phpInterpreter = PhpProjectConfigurationFacade
                .getInstance(project)
                .getInterpreter();
        if (phpInterpreter != null) {
            // Set up FUEL CMS generate command
            List<String> commands = new ArrayList<>();
            commands.add(phpInterpreter.getPathToPhpExecutable());
            commands.addAll(target.getCommand());

            GeneralCommandLine commandLine = new GeneralCommandLine(commands)
                    .withCharset(StandardCharsets.UTF_8)
                    .withWorkDirectory(project.getBasePath());
            ProcessHandler processHandler = new OSProcessHandler(commandLine);
            processHandler.addProcessListener(new DefaultProcessListener(project));
            processHandler.startNotify();
        } else {
            LOGGER.debug("Not running generate command, reason: PHP interpreter is not set up.");
        }
    }

}
