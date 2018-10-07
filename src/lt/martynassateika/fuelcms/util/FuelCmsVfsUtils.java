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

package lt.martynassateika.fuelcms.util;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.jetbrains.php.lang.psi.PhpFile;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lt.martynassateika.fuelcms.FuelCmsProjectSettings;
import org.jetbrains.annotations.NotNull;

public class FuelCmsVfsUtils {

  public static Optional<VirtualFile> getFuelFolder(Project project) {
    FuelCmsProjectSettings projectSettings = ServiceManager
        .getService(project, FuelCmsProjectSettings.class);
    return Optional.ofNullable(projectSettings.getFuelFolder());
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

  public static Optional<VirtualFile> getModuleConfigFolder(Project project,
      @NotNull String moduleName) {
    return getModuleFolder(project, moduleName).map(vf -> vf.findChild("config"));
  }

  public static Optional<PhpFile> getConstantsFileOfModule(Project project,
      @NotNull String moduleName) {
    PsiManager psiManager = PsiManager.getInstance(project);
    return getModuleConfigFolder(project, moduleName)
        .map(vf -> vf.findChild(moduleName + "_constants.php"))
        .map(psiManager::findFile)
        .filter(PhpFile.class::isInstance)
        .map(PhpFile.class::cast);
  }

  public static List<VirtualFile> getApplicationBlocks(Project project) {
    return getApplicationBlocksFolder(project)
        .map(VfsUtil::collectChildrenRecursively)
        .orElse(Collections.emptyList())
        .stream()
        .filter(vf -> "php".equalsIgnoreCase(vf.getExtension()))
        .collect(Collectors.toList());
  }

  public static Optional<VirtualFile> getApplicationBlocksFolder(Project project) {
    return getApplicationFolder(project)
        .map(vf -> vf.findChild("views"))
        .map(vf -> vf.findChild("_blocks"));
  }

  public static Optional<VirtualFile> getModuleViewsFolder(Project project,
      @NotNull String moduleName) {
    return getModuleFolder(project, moduleName)
        .map(vf -> vf.findChild("views"));
  }

  public static Optional<VirtualFile> getModuleBlocksFolder(Project project,
      @NotNull String moduleName) {
    return getModuleViewsFolder(project, moduleName)
        .map(vf -> vf.findChild("_blocks"));
  }

  public static List<VirtualFile> getModuleBlocks(Project project, @NotNull String moduleName) {
    return getModuleBlocksFolder(project, moduleName)
        .map(VfsUtil::collectChildrenRecursively)
        .orElse(Collections.emptyList())
        .stream()
        .filter(vf -> "php".equalsIgnoreCase(vf.getExtension()))
        .collect(Collectors.toList());
  }

  public static Optional<VirtualFile> getModuleDocumentationFolder(Project project,
      @NotNull String moduleName) {
    return getModuleViewsFolder(project, moduleName)
        .map(vf -> vf.findChild("_docs"));
  }

}
