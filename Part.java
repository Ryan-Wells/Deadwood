//Part class. Holds information such as name and level.
public class Part {

   //xml data
   String name = "null";
   int level;
   String line;

   boolean isTaken;
   boolean onCard;


   //used to print out information regarding the part.
   public String toString(){
      return "Part name = " + name + "\n    Part Level = " + level + "\n    Line = '" + line + "'   isTaken = " + isTaken + "  onCard = " + onCard + "\n";
   }


}
