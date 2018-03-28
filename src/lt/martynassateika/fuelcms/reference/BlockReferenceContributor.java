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

package lt.martynassateika.fuelcms.reference;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.PathUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import lt.martynassateika.fuelcms.psi.util.FuelBlockPsiUtil;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

/**
 * Allows navigation to block files from 'fuel_block' calls.
 */
public class BlockReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(), new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
                if (psiElement instanceof StringLiteralExpression) {
                    StringLiteralExpression literalExpression = (StringLiteralExpression) psiElement;
                    if (FuelBlockPsiUtil.isFuelBlockName(literalExpression)) {
                        String blockFileName = PathUtil.makeFileName(literalExpression.getContents(), "php");
                        PsiManager psiManager = PsiManager.getInstance(psiElement.getProject());
                        Optional<VirtualFile> blocksFolder = FuelCmsVfsUtils.getBlocksFolder(psiElement.getProject());
                        if (blocksFolder.isPresent()) {
                            String blockPath = Paths.get(blocksFolder.get().getPath(), blockFileName).toString();
                            String normalizedBlockPath = FilenameUtils.separatorsToUnix(blockPath);
                            return FuelCmsVfsUtils.getBlocks(psiElement.getProject())
                                    .stream()
                                    .filter(vf -> vf.getPath().startsWith(normalizedBlockPath))
                                    .map(psiManager::findFile)
                                    .filter(Objects::nonNull)
                                    .map(file -> new MyPsiFileReference(file, literalExpression))
                                    .toArray(PsiReference[]::new);
                        }
                    }
                }
                return PsiReference.EMPTY_ARRAY;
            }
        });
    }

}
