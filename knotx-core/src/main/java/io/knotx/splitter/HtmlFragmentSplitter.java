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
package io.knotx.splitter;

import com.google.common.collect.Lists;
import io.knotx.dataobjects.Fragment;
import io.knotx.fragments.FragmentConstants;
import io.knotx.fragments.SnippetPatterns;
import io.knotx.options.SnippetOptions;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import org.apache.commons.lang3.StringUtils;

class HtmlFragmentSplitter implements FragmentSplitter {

  private final SnippetPatterns snippetPatterns;

  HtmlFragmentSplitter(SnippetOptions snippetOptions) {
    snippetPatterns = new SnippetPatterns(snippetOptions);
  }

  @Override
  public List<Fragment> split(String html) {
    if (StringUtils.isEmpty(html)) {
      throw new NoSuchElementException("html cannot be empty");
    }
    List<Fragment> fragments = Lists.newLinkedList();
    if (snippetPatterns.getAnySnippetPattern().matcher(html).matches()) {
      Matcher matcher = snippetPatterns.getSnippetPattern().matcher(html);
      int idx = 0;
      while (matcher.find()) {
        MatchResult matchResult = matcher.toMatchResult();
        if (idx < matchResult.start()) {
          processMarkup(fragments,html, idx, matchResult.start());
        }
        fragments.add(
            toSnippet(matchResult.group(1).intern()
                    .split(FragmentConstants.FRAGMENT_IDENTIFIERS_SEPARATOR), html,
                matchResult.start(), matchResult.end()));
        idx = matchResult.end();
      }
      if (idx < html.length()) {
        processMarkup(fragments, html, idx, html.length());
      }
    } else {
      processMarkup(fragments, html, 0, html.length());
    }
    return fragments;
  }

  private void processMarkup(List<Fragment> fragments, String data, int startIdx, int endIdx) {
    String html = data.substring(startIdx, endIdx);
    Matcher matcher = snippetPatterns.getFallbackPattern().matcher(html);
    int idx = 0;
    while (matcher.find()) {
      MatchResult matchResult = matcher.toMatchResult();
      if (idx < matchResult.start()) {
        fragments.add(Fragment.raw(html.substring(idx, matchResult.start())));
      }
      fragments.add(toFallback(html, matchResult.start(), matchResult.end()));
      idx = matchResult.end();
    }
    if (idx < html.length()) {
      fragments.add(Fragment.raw(html.substring(idx, html.length())));
    }
  }

  private Fragment toFallback(String html, int startIdx, int endIdx) {
    String snippet = html.substring(startIdx, endIdx);
    return Fragment.fallback(snippet);
  }

  private Fragment toSnippet(String[] ids, String html, int startIdx, int endIdx) {
    String snippet = html.substring(startIdx, endIdx);
    Matcher matcher = snippetPatterns.getSnippetWithFallbackPattern().matcher(snippet);
    String fallback = matcher.matches() ?  matcher.group(2) : null;
    return Fragment.snippet(Arrays.asList(ids), snippet, fallback);
  }

}
