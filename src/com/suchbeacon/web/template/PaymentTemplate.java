package com.suchbeacon.web.template;

import com.suchbeacon.web.Template;
import java.util.ArrayList;
import java.util.List;

public class PaymentTemplate extends Template{
  private String name;
  private String price;
  private String imageUrl;
  private String location;
  private String description;

  @Override
  public List<String> render(){
    System.out.println("Rendering PaymentTemplate");

    List<String> cards = new ArrayList<String>();
    cards.add(
        "<article>"
          +"<figure>"
            +"<img src=\""+imageUrl+"\" width=\"100%\" height=\"100%\">"
          +"</figure>"
          +"<section>"
            +"<table class=\"text-small slign-justify\">"
              +"<tbody>"
                +"<tr>"
                  +"<td>Item:</td>"
                  +"<td>"+name+"</td>"
                +"</tr>"
                +"<tr>"
                  +"<td>Location</td>"
                  +"<td>"
                    +location
                  +"</td>"
                +"</tr>"
              +"</tbody>"
            +"</table>"
          +"</section>"
        +"</article>"
        +"<article>"
          +"<h1 class=\"auto-paginate\">"
          +"<hr>"
          +"<p class=\"text-small\">"
            +description
          +"</p>"
        +"</article>"
    );
    return cards;
  }
}
