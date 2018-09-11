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
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
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
     * Fuel Blocks can be rendered by calling the 'render' method in the 'Fuel_blocks' class, and
     * FUEL CMS additionally supplies a helper function 'fuel_block'.
     *
     * Regardless of which one is used, the name of the block is either the first string parameter,
     * or under the 'view' key in the first array parameter, e.g.:
     *
     * fuel_block('foo');
     * fuel_block(array('view' => 'foo'));
     * $this->fuel->blocks->render('foo');
     * $this->fuel->blocks->render(array('view' => 'foo'));
     *
     * 'blocks' is actually a magic property in the 'Fuel' master object, however it is not defined
     * in the class's PHPDoc (yet) and so we cannot have PhpStorm follow the references! Instead,
     * we short-circuit: if the method is called 'render' and the class reference is named 'blocks',
     * we say 'good enough' and contribute the reference to the view name.
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
                return isFuelBlockFunctionReference(parameterList) || isRenderMethod(parameterList);
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
                      return isFuelBlockFunctionReference(parameterList) || isRenderMethod(parameterList);
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
     * @param parameterList function or method parameter list
     * @return {@code true} if the parameter list is that associated with the 'fuel_block' function
     */
    private static boolean isFuelBlockFunctionReference(ParameterList parameterList) {
      FunctionReference functionReference = getParentOfType(parameterList, FunctionReference.class);
      return functionReference != null && "fuel_block".equals(functionReference.getName());
    }

    /**
     * @param parameterList function or method parameter list
     * @return {@code true} if the parameter list is associated with a method called 'render', with
     * resolving class reference named 'blocks'
     */
    private static boolean isRenderMethod(ParameterList parameterList) {
      MethodReference methodReference = getParentOfType(parameterList, MethodReference.class);
      if (methodReference != null && "render".equals(methodReference.getName())) {
        PhpExpression classReference = methodReference.getClassReference();
        if (classReference != null) {
          String className = classReference.getName();
          return "blocks".equals(className);
        }
      }
      return false;
    }

}
