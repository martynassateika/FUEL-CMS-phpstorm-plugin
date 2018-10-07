package lt.martynassateika.fuelcms;

import com.intellij.CommonBundle;
import java.util.ResourceBundle;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author martynas.sateika
 * @since 0.4.0
 */
public class FuelCmsBundle {

  private static final String BUNDLE_NAME = "messages.FuelCmsBundle";

  private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  /**
   * @param key message key
   * @param params optional parameters
   * @return message from the resource bundle with parameters substituted
   */
  public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) String key,
      Object... params) {
    return CommonBundle.message(BUNDLE, key, params);
  }

}
