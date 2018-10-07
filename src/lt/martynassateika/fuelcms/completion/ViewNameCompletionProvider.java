package lt.martynassateika.fuelcms.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import java.util.List;
import lt.martynassateika.fuelcms.psi.util.FuelViewPsiUtil;
import lt.martynassateika.fuelcms.psi.util.MyPsiUtil;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Aids in completing view names.
 *
 * @author martynas.sateika
 */
public class ViewNameCompletionProvider extends CompletionProvider<CompletionParameters> {

  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
      @NotNull CompletionResultSet resultSet) {
    PsiElement originalPosition = parameters.getOriginalPosition();
    if (originalPosition != null) {
      StringLiteralExpression literalExpression = MyPsiUtil
          .getParentOfType(originalPosition, StringLiteralExpression.class);
      if (literalExpression != null) {
        if (FuelViewPsiUtil.isFuelBlockName(literalExpression)) {
          addViewNameCompletions(originalPosition, "_blocks", resultSet);
        } else if (FuelViewPsiUtil.isFuelViewName(literalExpression)) {
          addViewNameCompletions(originalPosition, null, resultSet);
        }
      }
    }
  }

  private void addViewNameCompletions(PsiElement originalPosition, @Nullable String subFolder,
      @NotNull CompletionResultSet resultSet) {
    Project project = originalPosition.getProject();

    // Check application folder
    FuelCmsVfsUtils.getApplicationViewsFolder(project)
        .map(vf -> subFolder == null ? vf : vf.findChild(subFolder))
        .ifPresent(folder -> FuelCmsVfsUtils.getApplicationViews(project, subFolder)
            .stream()
            .map(view -> new ViewLookupElement(folder, view, "application"))
            .forEach(resultSet::addElement));

    // Check advanced modules
    List<VirtualFile> modules = FuelCmsVfsUtils.getAdvancedModules(project);
    for (VirtualFile module : modules) {
      String moduleName = module.getName();
      FuelCmsVfsUtils.getModuleViewsFolder(project, moduleName)
          .map(vf -> subFolder == null ? vf : vf.findChild(subFolder))
          .ifPresent(folder -> FuelCmsVfsUtils.getModuleViews(project, moduleName, subFolder)
              .stream()
              .map(view -> new ViewLookupElement(folder, view, moduleName))
              .forEach(resultSet::addElement));
    }
  }

}
