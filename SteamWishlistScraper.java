import java.io.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class SteamWishlistScraper
{
   public static void main (String [] args)
   {
      String id = args[1];
      String url = "http://steamcommunity.com/profiles/" + id + "/wishlist";
      System.out.print(url + "\n");
      
      Document doc = Jsoup.parse("<html></html>");
      try
      {
         doc = Jsoup.connect(url).get();
      }
      catch(IOException e){}
      
      //System.out.print(doc.title() + "\n");
      Element thing = doc.getElementById("wishlist_items");
      //System.out.print(thing.text() + "\n");
      String out = parseThrough(thing);
      writeOut(out, args[0]);
   }
   
   public static String parseThrough(Element in)
   {
      Elements items = in.getElementsByClass("wishlistRowItem");
      String give = "";
      
      for(Element item : items)
      {
         //System.out.print(item.getElementsByClass("ellipsis").first().text() + " ... ");
         give += item.getElementsByClass("ellipsis").first().text() + " ... ";
         if(item.getElementsByClass("price").first() == null)
         {
            //System.out.print(item.getElementsByClass("discount_pct").first().text() + " off " 
            //   + item.getElementsByClass("discount_original_price").first().text() + " = " 
            //   + item.getElementsByClass("discount_final_price").first().text() + "\n");
            give += 
                 item.getElementsByClass("discount_pct").first().text()            + " off " 
               + item.getElementsByClass("discount_original_price").first().text() + " = " 
               + item.getElementsByClass("discount_final_price").first().text()    + "\n";
         }
         else
         {
            //System.out.print(item.getElementsByClass("price").first().text() + "\n");
            give += item.getElementsByClass("price").first().text() + "\n";
         }
      }
      System.out.println(give);
      return give;
   }
   
   public static void writeOut(String in, String file)
   {// write the info to a text file
      File outFile = new File(file);
      try
      {
         PrintStream out = new PrintStream(outFile);
         out.print(in);
         out.close();
      }
      catch(Exception FileNotFoundException)
      {
         System.out.print("File not found.");
      }
   }
}

/**/