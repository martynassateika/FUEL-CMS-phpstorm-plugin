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
import lt.martynassateika.fuelcms.psi.util.FuelBlockPsiUtil;
import lt.martynassateika.fuelcms.psi.util.MyPsiUtil;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Aids in completing Fuel Block names.
 *
 * @author martynas.sateika
 */
public class BlockNameCompletionProvider extends CompletionProvider<CompletionParameters> {

  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
      @NotNull CompletionResultSet resultSet) {
    PsiElement originalPosition = parameters.getOriginalPosition();
    if (originalPosition != null) {
      StringLiteralExpression literalExpression = MyPsiUtil
          .getParentOfType(originalPosition, StringLiteralExpression.class);
      if (literalExpression != null && FuelBlockPsiUtil.isFuelBlockName(literalExpression)) {
        Project project = originalPosition.getProject();

        // Check application folder
        FuelCmsVfsUtils.getApplicationBlocksFolder(project)
            .ifPresent(folder -> FuelCmsVfsUtils.getApplicationBlocks(project)
                .stream()
                .map(block -> new SimpleLookupElement(folder, block, "application"))
                .forEachOrdered(resultSet::addElement));

        // Check advanced modules
        List<VirtualFile> advancedModules = FuelCmsVfsUtils.getAdvancedModules(project);
        for (VirtualFile advancedModule : advancedModules) {
          String moduleName = advancedModule.getName();
          FuelCmsVfsUtils.getModuleBlocksFolder(project, moduleName)
              .ifPresent(folder -> FuelCmsVfsUtils.getModuleBlocks(project, moduleName)
                  .stream()
                  .map(block -> new SimpleLookupElement(folder, block, moduleName))
                  .forEachOrdered(resultSet::addElement));
        }
      }
    }
  }

}
