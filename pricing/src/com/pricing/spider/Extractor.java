package com.pricing.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Extractor {
    private HashSet<String> links;
    private List<String> articles;
    private String[] cssClass = {"a-price-whole", "_1vC4OE _2rQ-NK"}; 

    public Extractor() {
        links = new HashSet<>();
        articles = new ArrayList<>();
    }

    //Find all URLs that start with "http://www.mkyong.com/page/" and add them to the HashSet
    public void getPageLinks(String URL) {
        if (!links.contains(URL)) {
            try {

                Document document = Jsoup.connect(URL).get();
                //Elements body = document.body().select("href");
                Elements body = document.body().getAllElements();
               // Element otherLinks = document.body().getElementById("url");

                for (Element page : body) {
                    if (links.add(URL)) {
                        //Remove the comment from the line below if you want to see it running on your editor
                        System.out.println(URL);
                    }
                    //getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    //Connect to each link saved in the article and find all the articles in the page
    public void getArticles() {
        links.forEach(x -> {
            Document document;
            try {
                document = Jsoup.connect(x).get();
                
                for(int i = 0; i < cssClass.length; i++) {
                    Elements articleLinks = document.getElementsByClass(cssClass[i]);
                    for (Element article : articleLinks) {
                    	articles.add(article.text());
                    }
                }
                
       //         Elements articleLinks = document.getElementsByClass("a-price-whole");
         //       for (Element article : articleLinks) {
                    //Only retrieve the titles of the articles that contain Java 8
                    //if (article.text().matches("^.*?(Java 8|java 8|JAVA 8).*$")) {
                        //Remove the comment from the line below if you want to see it running on your editor, 
                        //or wait for the File at the end of the execution
                        //System.out.println(article.attr("abs:href"));

                        /*ArrayList<String> temporary = new ArrayList<>();
                        temporary.add(article.text()); //The title of the article
                        temporary.add(article.attr("abs:href")); //The URL of the article
*/         //               articles.add(article.text());
                    //}
             //   }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    public void writeToFile(String filename) {
        FileWriter writer;
        try {
            writer = new FileWriter(filename);
            articles.forEach(a -> {
                try {
                    String temp = "- Price: " + a;
                    //display to console
                    System.out.println(temp);
                    //save to file
                    writer.write(temp);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Extractor bwc = new Extractor();
        bwc.getPageLinks("https://www.flipkart.com/search?q=Redmi%20Note%207%20Pro%20%28Neptune%20Blue%2C%2064%20GB%29%20%20%284%20GB%20RAM%29&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off");
        bwc.getPageLinks("https://www.amazon.in/s/ref=nb_sb_ss_sc_1_7?url=search-alias%3Daps&field-keywords=adidas+running+shoes+men");
        bwc.getArticles();
        bwc.writeToFile("Java 8 Articles");
    }
}