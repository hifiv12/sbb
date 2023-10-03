package com.mysite.sbb;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
  
  public String markdown(String markdown) {

    Parser parser = Parser.builder().build();
    // 마크 다운 문법 적용
    Node document = parser.parse(markdown);
    // html 텍스트로 렌더
    HtmlRenderer renderer = HtmlRenderer.builder().build();
    
    return renderer.render(document);

  }

}
