//A class used by the casting office to deal with players who want to upgrade their rank
public class Upgrade{

    int level;
    String currency;
    int amount;
    int x;
    int y;
    int h;
    int w;

   //used to print out the upgrade's information
   public String toString(){
      if(currency.equals("dollar")){
         return "rank " + level + ", $" +  amount;
      }
      else{
         return "rank " + level + ", " +  amount + "cr";
      }
   }

}
