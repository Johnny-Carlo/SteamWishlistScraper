/*
 
 *Tim Haskins
 *March 25, 2016

 */
import java.io.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
// org.jsoup does not come standard in the Java library
// It can be found here as a zip file: (unzips to reveal a jar file)
// http://jsoup.org/download
// Last checked March 16, 2016

/*

 *Expected arguments:
 *args[0]: The name of the text file that this program will print to.
 *args[1]: The ID or profile of the steam user in question.
            Example: 76561198099289598, 
 */

public class SteamWishlistScraper
{
   static Document doc;
   
   public static void main (String [] args)
   {
      boolean itWorked = check(args[1]);
      if(!itWorked){return;}
      
      String out = parseThrough();
      writeOut(out, args[0]);
      System.out.print("Done.\n");
   }
   
   public static boolean check(String id)
   {
      //this method checks to see if the users argument leads to a legitimate 
      // wishlist. If it does, this method saves the webpage to the doc variable
      // and returns true.
      // Otherwise, it returns false.
      String url = "http://steamcommunity.com/profiles/" + id + "/wishlist";
      String url2 = "http://steamcommunity.com/id/" + id + "/wishlist";
      System.out.print("Trying " + url + "\n");
      
      try
      {
         doc = Jsoup.connect(url).get();
         if( doc.title( ).equals( "Steam Community :: Error" ) )
         {
            System.out.print("Didn't work. Trying " + url2 + "\n");
            doc = Jsoup.connect(url2).get();
            if( doc.title( ).equals( "Steam Community :: Error" ) )
            {
               System.out.print("Neither worked. Exiting...\n");
               return false;
            }
         }
         //System.out.print(doc.title());
         if( doc.title().indexOf("::") == doc.title().lastIndexOf("::") )
         {
            System.out.print( "Looks like the account is private. Exiting...\n" );
            return false;
         }
         System.out.print("Got it!\n");
         
      }
      catch(IOException e){}
      return true;
   }
   
   public static String parseThrough()
   {
      Element thing = doc.getElementById("wishlist_items");
      Elements items = thing.getElementsByClass("wishlistRowItem");
      String give = "";
      
      for(Element item : items)
      {
         give += item.getElementsByClass("ellipsis").first().text() + " ... ";
         
         if(item.getElementsByClass("price").first()        == null && 
            item.getElementsByClass("discount_pct").first() == null)
         {
            give += "Free\n";
         }
         else if(item.getElementsByClass("price").first() == null)
         {
            give += 
                 item.getElementsByClass("discount_pct").first().text()            + " off " 
               + item.getElementsByClass("discount_original_price").first().text() + " = " 
               + item.getElementsByClass("discount_final_price").first().text()    + "\n";
         }
         else
         {
            give += item.getElementsByClass("price").first().text() + "\n";
         }
      }
      //System.out.println(give);
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