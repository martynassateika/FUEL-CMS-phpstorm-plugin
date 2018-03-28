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

package lt.martynassateika.fuelcms.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.refactoring.PhpNameUtil;
import lt.martynassateika.fuelcms.psi.util.FuelBlockPsiUtil;
import lt.martynassateika.fuelcms.psi.util.MyPsiUtil;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Aids in completing Fuel Block names.
 */
public class BlockNameCompletionContributor extends CompletionContributor {

    public BlockNameCompletionContributor() {
        extend(CompletionType.BASIC, getPlace(),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters,
                                                  ProcessingContext context, @NotNull
                                                          CompletionResultSet resultSet) {
                        PsiElement originalPosition = parameters.getOriginalPosition();
                        if (originalPosition != null) {
                            StringLiteralExpression literalExpression = MyPsiUtil.getParentOfType(originalPosition, StringLiteralExpression.class);
                            if (literalExpression != null && FuelBlockPsiUtil.isFuelBlockName(literalExpression)) {
                                Project project = originalPosition.getProject();
                                FuelCmsVfsUtils.getBlocksFolder(project).ifPresent(folder -> {
                                    FuelCmsVfsUtils.getBlocks(project)
                                            .stream()
                                            .map(block -> new SimpleLookupElement(folder, block))
                                            .forEach(resultSet::addElement);
                                });
                            }
                        }
                    }
                });
    }

    @NotNull
    private PsiElementPattern.Capture<LeafPsiElement> getPlace() {
        return PlatformPatterns
                .psiElement(LeafPsiElement.class)
                .withLanguage(PhpLanguage.INSTANCE);
    }

    private static class SimpleLookupElement extends LookupElement {
        private final VirtualFile file;
        private final String relativePath;

        private SimpleLookupElement(VirtualFile blocksFolder, VirtualFile file) {
            this.file = file;
            this.relativePath = getRelativePathToBlockFile(blocksFolder);
        }

        private String getRelativePathToBlockFile(VirtualFile blocksFolder) {
            Path filePath = Paths.get(PhpNameUtil.getNameWithoutExtension(file.getPath()));
            Path blocksFolderPath = Paths.get(blocksFolder.getPath());
            Path relativePath = blocksFolderPath.relativize(filePath);
            return FilenameUtils.separatorsToUnix(relativePath.toString());
        }

        @NotNull
        @Override
        public String getLookupString() {
            return relativePath;
        }

        @Override
        public void renderElement(LookupElementPresentation presentation) {
            super.renderElement(presentation);
            presentation.setIcon(IconLoader.findIcon("/icons/php-icon.png"));
        }
    }

}
