/*
 * Copyright (C) 2016 Cognifide Limited
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
package io.knotx.knot.assembler;

import io.knotx.fragments.FragmentConstants;
import io.knotx.knot.assembler.impl.UnprocessedFragmentStrategy;
import io.vertx.core.json.JsonObject;

public class FragmentAssemblerConfiguration {

  private final String address;
  private final String snippetTagName;
  private final UnprocessedFragmentStrategy assemblyStrategy;

  public FragmentAssemblerConfiguration(JsonObject config) {
    address = config.getString("address");
    snippetTagName = config.getString("snippetTagName", FragmentConstants.DEFAULT_SNIPPET_TAG_NAME);
    assemblyStrategy = UnprocessedFragmentStrategy
        .valueOf(config.getString("unprocessedStrategy", UnprocessedFragmentStrategy.UNWRAP.name())
            .toUpperCase());
  }

  public String address() {
    return address;
  }

  public String getSnippetTagName() {
    return snippetTagName;
  }

  public UnprocessedFragmentStrategy unprocessedFragmentStrategy() {
    return assemblyStrategy;
  }
}
