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

package lt.martynassateika.fuelcms.reference;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.util.PathUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lt.martynassateika.fuelcms.psi.util.FuelViewPsiUtil;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Allows navigation to view files.
 */
public class ViewReferenceContributor extends PsiReferenceContributor {

  @Override
  public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(PlatformPatterns.psiElement(), new PsiReferenceProvider() {
      @NotNull
      @Override
      public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement,
          @NotNull ProcessingContext processingContext) {
        if (psiElement instanceof StringLiteralExpression) {
          Project project = psiElement.getProject();
          StringLiteralExpression literalExpression = (StringLiteralExpression) psiElement;
          if (FuelViewPsiUtil.isFuelBlockName(literalExpression)) {
            // Block references
            List<PsiReference> references = getReferences(project, literalExpression, "_blocks");
            return references.toArray(PsiReference.EMPTY_ARRAY);
          } else if (FuelViewPsiUtil.isFuelViewName(literalExpression)) {
            // Regular view references
            List<PsiReference> references = getReferences(project, literalExpression, null);
            return references.toArray(PsiReference.EMPTY_ARRAY);
          }
        }
        return PsiReference.EMPTY_ARRAY;
      }
    });
  }

  private static List<PsiReference> getReferences(Project project,
      StringLiteralExpression literalExpression, String viewSubFolder) {
    String blockFileName = PathUtil.makeFileName(literalExpression.getContents(), "php");
    PsiManager psiManager = PsiManager.getInstance(project);

    List<PsiReference> references = new ArrayList<>();

    // Check application folder
    Optional<VirtualFile> viewsFolder = FuelCmsVfsUtils
        .getApplicationViewsFolder(project)
        .map(vf -> viewSubFolder == null ? vf : vf.findChild(viewSubFolder));
    if (viewsFolder.isPresent()) {
      String viewPath = Paths.get(viewsFolder.get().getPath(), blockFileName).toString();
      String normalizedPath = FilenameUtils.separatorsToUnix(viewPath);
      FuelCmsVfsUtils.getApplicationViews(project, viewSubFolder)
          .stream()
          .filter(vf -> vf.getPath().startsWith(normalizedPath))
          .map(psiManager::findFile)
          .filter(Objects::nonNull)
          .map(file -> new MyPsiFileReference(file, literalExpression))
          .forEachOrdered(references::add);
    }

    // Check advanced modules
    List<VirtualFile> modules = FuelCmsVfsUtils.getAdvancedModules(project);
    for (VirtualFile module : modules) {
      String moduleName = module.getName();
      Optional<VirtualFile> moduleViewsFolder = FuelCmsVfsUtils
          .getModuleViewsFolder(project, moduleName);
      if (moduleViewsFolder.isPresent()) {
        String viewPath = Paths.get(moduleViewsFolder.get().getPath(), blockFileName)
            .toString();
        String normalizedPath = FilenameUtils.separatorsToUnix(viewPath);
        FuelCmsVfsUtils.getModuleViews(project, moduleName, viewSubFolder)
            .stream()
            .filter(vf -> vf.getPath().startsWith(normalizedPath))
            .map(psiManager::findFile)
            .filter(Objects::nonNull)
            .map(file -> new MyPsiFileReference(file, literalExpression))
            .forEachOrdered(references::add);
      }
    }
    return references;
  }

}
