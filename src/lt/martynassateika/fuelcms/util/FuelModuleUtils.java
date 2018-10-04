package lt.martynassateika.fuelcms.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.SmartList;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.Constant;
import com.jetbrains.php.lang.psi.elements.PhpDefine;
import com.jetbrains.php.lang.psi.visitors.PhpRecursiveElementVisitor;
import java.util.List;
import java.util.Optional;

/**
 * Utility methods related to FUEL CMS modules.
 *
 * @author martynas.sateika
 * @since 0.4.0
 */
public final class FuelModuleUtils {

  /**
   * Returns the version number of a module from its constants file.
   *
   * If the constants file does not exist, or if it does but the constant is of the wrong format,
   * then an empty Optional is returned.
   *
   * A well-formed constant is the upper-case of the module's name with a '_VERSION' suffix, e.g. if
   * the module is 'blog', then the version should be defined in 'BLOG_VERSION'.
   *
   * @param project current project
   * @param moduleName module name (e.g. 'fuel' or 'blog')
   * @return version number of module, if present
   */
  public static Optional<String> getAdvancedModuleVersion(Project project, String moduleName) {
    Optional<PhpFile> constantsFile = FuelCmsVfsUtils.getConstantsFileOfModule(project, moduleName);
    if (constantsFile.isPresent()) {
      PhpFile phpFile = constantsFile.get();
      String expectedConstantName = getModuleVersionConstant(moduleName);
      List<Constant> constants = getConstantsByName(phpFile, expectedConstantName);
      if (!constants.isEmpty()) {
        // Only care about the first match. Multiple defines with the same name
        // are un-usual and PhpStorm warns about this with an inspection anyway
        Constant firstMatch = constants.get(0);
        PsiElement firstMatchValue = firstMatch.getValue();
        return Optional
            .ofNullable(firstMatchValue)
            .map(PsiElement::getText)
            .map(StringUtil::unquoteString);
      }
    }
    return Optional.empty();
  }

  private static String getModuleVersionConstant(String moduleName) {
    return moduleName.toUpperCase() + "_VERSION";
  }

  @SuppressWarnings("deprecation")
  private static List<Constant> getConstantsByName(PhpFile file, final String name) {
    final List<Constant> result = new SmartList<>();
    file.accept(new PhpRecursiveElementVisitor() {
      @Override
      public void visitPhpConstant(Constant constant) {
        super.visitPhpConstant(constant);
        if (constant instanceof PhpDefine) {
          if (constant.getName().equals(name)) {
            result.add(constant);
          }
        }
      }
    });
    return result;
  }

}
