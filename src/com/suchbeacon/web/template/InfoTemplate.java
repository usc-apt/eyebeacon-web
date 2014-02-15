package com.suchbeacon.web.template;

import com.suchbeacon.web.Template;
import java.util.ArrayList;
import java.util.List;

public class InfoTemplate extends Template{
  private String name;
  private String imageUrl;
  private String location;
  private Info[] infos;
  
  @Override
  public List<String> render(){
    System.out.println("Rendering InfoTemplate");

    List<String> cards = new ArrayList<String>();
    String html=
          "<article class=\"cover-only\">"
            +"<figure>"
              +"<img src=\"" + imageUrl + "\" width=\"100%\" height=\"100%\">"
            +"</figure>"
            +"<section>"
              +"<h1 class=\"text-large\">"
                +name
              +"</h1>"
              +"<hr>"
              +"<p class=\"text-small\">"
                +"Located In:"
              +"</p>"
              +"<p class=\"text-minor\">"
                +location
              +"</p>"
            +"</section>"
          +"</article"
    ;

    for(Info i : infos){
     html+=
          "<article class=\"auto-paignate\">"
            +"<h1 class=\"text-large\">"
              +i.section
            +"</h1>"
            +"<p class=\"text-small\">"
              +i.desc
            +"</p>"
          +"</article>"
      ;
    }
    cards.add(html);

    return cards;
  }

 static class Info{
   private String section;
   private String desc;

   public Info(){
   }
 }
}
