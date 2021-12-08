// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package osmedile.intellij.stringmanip;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public final class StringManipulationBundle extends DynamicBundle {
  @NonNls
  public static final String BUNDLE = "messages.StringManipulationBundle";
  private static final StringManipulationBundle INSTANCE = new StringManipulationBundle();

  private StringManipulationBundle() {
    super(BUNDLE);
  }

  @NotNull
  public static @Nls
  String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
    return INSTANCE.getMessage(key, params);
  }

  public static @Nls
  String partialMessage(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                        int unassignedParams,
                        Object... params) {
    return INSTANCE.getPartialMessage(key, unassignedParams, params);
  }

  @NotNull
  public static Supplier<String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
    return INSTANCE.getLazyMessage(key, params);
  }
}
