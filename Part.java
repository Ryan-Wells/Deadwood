//Part class. Holds information such as name and level.
public class Part {

   //xml data
   String name = "null";
   String line;

   int level;
   int x;
   int y;
   int h;
   int w;

   boolean isTaken;
   boolean onCard;

   Take[] takes;

   //used to print out information regarding the part.
   public String toString(){
      return name;
   }


}
