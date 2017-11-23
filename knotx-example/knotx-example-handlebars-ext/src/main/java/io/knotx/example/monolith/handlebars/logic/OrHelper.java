package io.knotx.example.monolith.handlebars.logic;

import com.github.jknack.handlebars.Options;
import io.knotx.example.monolith.handlebars.ConditionalHelper;

import java.util.Arrays;
import java.util.Objects;

/**
 * Checks if any value is not empty.<br>
 * Usage:
 * <pre>
 *     {{#or value1 value2 ... }}
 *         some value not empty
 *     {{else}}
 *         all empty
 *     {{/or}}
 * </pre>
 */
public class OrHelper extends ConditionalHelper {

  @Override
  public String getName() {
    return "or";
  }

  @Override
  protected Boolean checkCondition(Object firstParam, Options options) {
    return !options.isFalsy(firstParam) || Arrays.stream(options.params)
        .filter(Objects::nonNull)
        .anyMatch(param -> !options.isFalsy(param));
  }
}
