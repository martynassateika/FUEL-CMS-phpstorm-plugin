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

package lt.martynassateika.fuelcms.psi.util;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Various utility methods for navigating the PSI tree.
 *
 * @author martynas.sateika
 * @since 0.2.0
 */
public class MyPsiUtil {

  /**
   * @param psiElement element whose parent to inspect
   * @param parentClass wanted parent class
   * @param <T> parent class type
   * @return parent of <tt>psiElement</tt> if its class is <tt>parentClass</tt>, else null
   */
  @Nullable
  public static <T> T getParentOfType(@Nullable PsiElement psiElement,
      @NotNull Class<T> parentClass) {
    if (psiElement != null) {
      PsiElement parent = psiElement.getParent();
      if (parent != null) {
        if (parentClass.isInstance(parent)) {
          //noinspection unchecked
          return (T) parent;
        }
      }
    }
    return null;
  }

  /**
   * @param psiElement element to walk up
   * @param steps number of times to go up via {@link PsiElement#getParent()}
   * @return element {@code steps} parents up, or null
   */
  @Nullable
  public static PsiElement walkUp(@NotNull PsiElement psiElement, int steps) {
    PsiElement parent = psiElement;
    for (int i = 0; i < steps; i++) {
      parent = parent.getParent();
      if (parent == null) {
        return null;
      }
    }
    return parent;
  }

  /**
   * @param psiElement element to walk up
   * @param steps number of times to go up via {@link PsiElement#getParent()}
   * @return element {@code steps} parents up, if instance of {@code clazz}, or null
   */
  @Nullable
  public static <T> T walkUp(@NotNull PsiElement psiElement, int steps, Class<T> clazz) {
    PsiElement parent = walkUp(psiElement, steps);
    if (clazz.isInstance(parent)) {
      //noinspection unchecked
      return (T) parent;
    }
    return null;
  }

}
