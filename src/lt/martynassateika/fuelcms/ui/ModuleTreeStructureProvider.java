package lt.martynassateika.fuelcms.ui;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Updates the presentation of certain FUEL-CMS files and folders in the tree structure.
 *
 * @author martynas.sateika
 * @since 0.4.0
 */
public class ModuleTreeStructureProvider implements TreeStructureProvider {

  @NotNull
  @Override
  public Collection<AbstractTreeNode> modify(@NotNull AbstractTreeNode abstractTreeNode,
      @NotNull Collection<AbstractTreeNode> children, ViewSettings viewSettings) {
    List<VirtualFile> moduleFolders = null;

    List<AbstractTreeNode> nodes = new ArrayList<>(children.size());
    for (AbstractTreeNode node : children) {
      Project project = node.getProject();
      if (project != null) {
        if (moduleFolders == null) {
          moduleFolders = FuelCmsVfsUtils.getModuleFolders(project);
        }

        if (node.getValue() instanceof PsiDirectory) {
          PsiDirectory directory = (PsiDirectory) node.getValue();
          VirtualFile virtualFile = directory.getVirtualFile();
          if (moduleFolders.contains(virtualFile)) {
            nodes.add(new FuelModuleNode(project, directory, viewSettings));
            continue;
          }
        }
      }
      nodes.add(node);
    }
    return nodes;
  }

  @Nullable
  @Override
  public Object getData(Collection<AbstractTreeNode> collection, String s) {
    return null; // default method overridden for 2016.1 support
  }

}
