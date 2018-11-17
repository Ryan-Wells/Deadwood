import java.util.*;

//Card class. Holds information regarding movies.
public class Card {

   int number;
   int budget;
   int scene;

   String name;
   String description;
   String image;

   boolean flipped;

   Part[] parts;


   //used to print out all of the card's information
   public void printCardInfo(){

      if(name != null){
         System.out.println("Card name = '" + name + "'");
      }

      if(image != null){
         System.out.println("img = " + image);
      }

      if(budget != 0){
         System.out.println("Budget = " + budget);
      }

      if(scene != 0){
         System.out.println("Scene " + scene);
      }

      if(description != null){
         System.out.println("Description = " + description);
      }

      if(parts != null){
         System.out.println("Parts: ");
         for(int i = 0; i < parts.length; i++){
            System.out.println("    " + parts[i]);
         }
      }
      System.out.println("\n");

   }


}
