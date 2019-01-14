import java.util.*;
import org.w3c.dom.Document;

//main class for the game
public class Deadwood {

   public static BoardLayersListener boardListener;
   
   public static void main(String args[]){

      extractXMLInfo();
      
      //Runs the game.
      gameSystem.startGame();
   }
   
   //extract XML information to store room and card information//
   private static void extractXMLInfo(){
      Document doc = null;
      ParseXML parsing = new ParseXML();
      try{
         doc = parsing.getDocFromFile("cards.xml");
         parsing.readCardData(doc);
      
         doc = parsing.getDocFromFile("board.xml");
         parsing.readBoardData(doc);
      }
      catch (Exception e){
         System.out.println("Error = "+e);
      }
   }
}
