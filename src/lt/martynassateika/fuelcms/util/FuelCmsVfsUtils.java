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

package lt.martynassateika.fuelcms.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FuelCmsVfsUtils {

    public static Optional<VirtualFile> getFuelFolder(Project project) {
        return Optional.ofNullable(project.getBaseDir().findChild("fuel"));
    }

    public static Optional<VirtualFile> getApplicationFolder(Project project) {
        return getFuelFolder(project).map(vf -> vf.findChild("application"));
    }

    public static Optional<VirtualFile> getApplicationConfigFolder(Project project) {
        return getApplicationFolder(project).map(vf -> vf.findChild("config"));
    }

    public static Optional<VirtualFile> getDatabaseSettingsFile(Project project) {
        return getApplicationConfigFolder(project).map(vf -> vf.findChild("database.php"));
    }

    public static Optional<VirtualFile> getModulesFolder(Project project) {
        return getFuelFolder(project).map(vf -> vf.findChild("modules"));
    }

    public static List<VirtualFile> getAdvancedModules(Project project) {
        return FuelCmsVfsUtils.getModuleFolders(project).stream()
                .filter(VirtualFile::isDirectory)
                .collect(Collectors.toList());
    }

    public static boolean advancedModuleExists(Project project, String name) {
        return getAdvancedModules(project).stream()
                .map(VirtualFile::getName)
                .anyMatch(fileName -> fileName.equals(name));
    }

    public static List<VirtualFile> getModuleFolders(Project project) {
        return getModulesFolder(project).map(vf -> Stream.of(vf.getChildren())
                .filter(VirtualFile::isDirectory)
                .collect(Collectors.toList())
        ).orElse(Collections.emptyList());
    }

    public static Optional<VirtualFile> getModuleFolder(Project project, @NotNull String moduleName) {
        return getModulesFolder(project).map(vf -> vf.findChild(moduleName));
    }

    public static Optional<VirtualFile> getModuleConfigFolder(Project project, @NotNull String moduleName) {
        return getModuleFolder(project, moduleName).map(vf -> vf.findChild("config"));
    }

    public static Optional<VirtualFile> getConstantsFileOfModule(Project project, @NotNull String moduleName) {
        return getModuleConfigFolder(project, moduleName).map(vf -> vf.findChild(moduleName + "_constants.php"));
    }

    public static List<VirtualFile> getBlocks(Project project) {
        return getBlocksFolder(project)
                .map(VfsUtil::collectChildrenRecursively)
                .orElse(Collections.emptyList())
                .stream()
                .filter(vf -> "php".equalsIgnoreCase(vf.getExtension()))
                .collect(Collectors.toList());
    }

    public static Optional<VirtualFile> getBlocksFolder(Project project) {
        return getApplicationFolder(project)
                .map(vf -> vf.findChild("views"))
                .map(vf -> vf.findChild("_blocks"));
    }

}
