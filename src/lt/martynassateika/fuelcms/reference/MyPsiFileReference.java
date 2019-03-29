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

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PsiReference implementation used to resolve string literals to files.
 *
 * @author martynas.sateika
 * @since 0.2.0
 */
class MyPsiFileReference implements PsiReference {

  private final PsiFile file;

  private final StringLiteralExpression literalExpression;

  MyPsiFileReference(PsiFile file, StringLiteralExpression literalExpression) {
    this.file = file;
    this.literalExpression = literalExpression;
  }

  @NotNull
  @Override
  public PsiElement getElement() {
    return literalExpression;
  }

  @NotNull
  @Override
  public TextRange getRangeInElement() {
    int start = literalExpression.getTextRange().getStartOffset();
    return literalExpression.getTextRange().shiftRight(-start);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    return file;
  }

  @NotNull
  @Override
  public String getCanonicalText() {
    return "";
  }

  @Override
  public PsiElement handleElementRename(@NotNull String newElementName)
      throws IncorrectOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isReferenceTo(@NotNull PsiElement element) {
    return resolve() == element;
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    return ArrayUtil.EMPTY_OBJECT_ARRAY;
  }

  @Override
  public boolean isSoft() {
    return true;
  }

}
