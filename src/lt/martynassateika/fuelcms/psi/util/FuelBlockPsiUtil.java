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
import com.jetbrains.php.lang.psi.elements.ArrayCreationExpression;
import com.jetbrains.php.lang.psi.elements.ArrayHashElement;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

import static lt.martynassateika.fuelcms.psi.util.MyPsiUtil.getParentOfType;

/**
 * PSI utility methods specific to Fuel Blocks.
 */
public class FuelBlockPsiUtil {

    /**
     * Returns {@code true} if {@code element} represents the name of a Fuel Block.
     * <p>
     * Fuel Blocks can be named in two different ways. Inside the function parameter list, the name
     * can either be the first string parameter (e.g. "fuel_block('foo')"), or under the
     * 'view' key in the first array parameter (e.g. "fuel_block(array('view' => 'foo')").
     *
     * @param element a PSI element
     * @return {@code true} if {@code element} represents the name of a Fuel Block
     */
    public static boolean isFuelBlockName(@NotNull StringLiteralExpression element) {
        // Check if string is first parameter
        ParameterList parameterList = getParentOfType(element, ParameterList.class);
        if (parameterList != null) {
            PsiElement[] parameters = parameterList.getParameters();
            if (parameters[0] == element) {
                FunctionReference functionReference = getParentOfType(parameterList, FunctionReference.class);
                return isFuelBlockFunctionReference(functionReference);
            }
        } else {
          // Check if string is value in array
          ArrayHashElement arrayHashElement = MyPsiUtil.walkUp(element, 2, ArrayHashElement.class);
          if (arrayHashElement != null) {
            PhpPsiElement key = arrayHashElement.getKey();
            if (key instanceof StringLiteralExpression) {
              String contents = ((StringLiteralExpression) key).getContents();
              if (contents.equals("view")) {
                ArrayCreationExpression arrayCreationExpression = getParentOfType(arrayHashElement, ArrayCreationExpression.class);
                if (arrayCreationExpression != null) {
                  parameterList = getParentOfType(arrayCreationExpression, ParameterList.class);
                  if (parameterList != null) {
                    PsiElement[] parameters = parameterList.getParameters();
                    // The array must be the first parameter of 'fuel_block'
                    if (parameters[0] == arrayCreationExpression) {
                      FunctionReference functionReference = getParentOfType(parameterList, FunctionReference.class);
                      return isFuelBlockFunctionReference(functionReference);
                    }
                  }
                }
              }
            }
          }
        }
        return false;
    }

    /**
     * @param functionReference a function reference
     * @return {@code true} if {@code functionReference} represents the 'fuel_block' function
     */
    private static boolean isFuelBlockFunctionReference(FunctionReference functionReference) {
        return functionReference != null && "fuel_block".equals(functionReference.getName());
    }

}
