import java.util.*;
import javax.swing.*;
import java.awt.*;

//Player class. Has information about the player, and lots of methods.
public class Player {

   static Scanner scan = new Scanner(System.in);

   String name;
   String color;
   String partName;

   int dollars;
   int credits;
   int rank;
   int rehearsalTokens;
   int score;
   int diceValue;
   int[] multipleDiceValues = new int[6];

   boolean isTurn;
   boolean hasMoved;
   boolean hasPart;
   boolean justTookPart;
   boolean canTakePart;
   boolean hasRehearsed;
   boolean hasActed;

   Part currentPart;
   Player nextPlayer;
   Room currentRoom;
   
   JLabel playerlabel;

   //set name, set things to zero and false
   void init(String name){  
      this.name = name;
      this.dollars = 0;
      this.credits = 0;
      this.rank = 1;
      this.rehearsalTokens = 0;
      this.score = 0;
      this.diceValue = 0;
      this.isTurn = false;
      this.hasMoved = false;
      this.hasPart = false;
      this.justTookPart = false;
      this.canTakePart = true;
      this.currentPart = new Part();
      this.currentPart.name = "none";
      this.partName = "none";
      this.nextPlayer = null; 
      this.currentRoom = null; 
   }


   //removes a player's part
   void removePart(){
      Part nullPart = new Part();
      currentPart = nullPart;
      hasPart = false;
      justTookPart = false;
      currentPart.name = "none";
      partName = "none";
      BoardLayersListener.drawStats(this);
   }


   //used to play the game.
   void playTurn(){
   
      isTurn = true;
      gameSystem.currentPlayer = this;
      hasMoved = false;
      justTookPart = false;
      hasRehearsed = false;
      hasActed = false;
   
      BoardLayersListener.textBox.setText("Starting " + name + "'s turn...");
     
      boolean canUpgrade = false;
      boolean canMove = false;
      boolean canAct = false;
      boolean canRehearse = false;
      boolean deciding = true;
   
      String userInput = "error";
   
      if(hasPart){
         BoardLayersListener.bMove.setBackground(Color.black);
         BoardLayersListener.bMove.setEnabled(false);
      }
   
      while(deciding){
      
         BoardLayersListener.drawStats(this);
      
         if(currentRoom.name.equals("office")){
            canUpgrade = true;
            BoardLayersListener.bUpgrade.setBackground(Color.white);
            BoardLayersListener.bUpgrade.setEnabled(true);
         }
         else{
            BoardLayersListener.bUpgrade.setBackground(Color.black);
            BoardLayersListener.bUpgrade.setEnabled(false);
         }
         if(hasMoved == false){
            if(hasPart == false){
               canMove = true;
               BoardLayersListener.bMove.setBackground(Color.white);
               BoardLayersListener.bMove.setEnabled(true);
            }
         }
         else{
            BoardLayersListener.bMove.setBackground(Color.black);
            BoardLayersListener.bMove.setEnabled(false);
         }
         if(hasPart == true){
            canTakePart = false;
            if(justTookPart == false){
               canAct = true;
               canRehearse = true;
               BoardLayersListener.bAct.setBackground(Color.white);
               BoardLayersListener.bRehearse.setBackground(Color.white);
               BoardLayersListener.bAct.setEnabled(true);
               BoardLayersListener.bRehearse.setEnabled(true);
            }
         }
         else{
            BoardLayersListener.bAct.setBackground(Color.black);
            BoardLayersListener.bRehearse.setBackground(Color.black);
            BoardLayersListener.bAct.setEnabled(false);
            BoardLayersListener.bRehearse.setEnabled(false);
         }
         if(hasPart == false){
            if(!currentRoom.name.equals("office") && !currentRoom.name.equals("trailer")){
               canTakePart = true;
               BoardLayersListener.bTakePart.setBackground(Color.white);
               BoardLayersListener.bTakePart.setEnabled(true);
            }
            if(!currentRoom.doneShooting){
               canTakePart = true;
               BoardLayersListener.bTakePart.setBackground(Color.white);
               BoardLayersListener.bTakePart.setEnabled(true);
            }
            else{
               BoardLayersListener.bTakePart.setBackground(Color.black);
               BoardLayersListener.bTakePart.setEnabled(false);
            }
         }
         else{
            BoardLayersListener.bTakePart.setBackground(Color.black);
            BoardLayersListener.bTakePart.setEnabled(false);
         }
 
         BoardLayersListener.drawStats(this);      
         break;
      }
   }


   //acts on a movie.
   void act(){ 
      if(hasPart){
         //roll one dice and check its value + rehearse bonus
         rollOnce();
         BoardLayersListener.textBox.append("\nActing...\n You rolled a " + diceValue + " (+" + rehearsalTokens + ")");
         //on successful acting
         if((diceValue + rehearsalTokens) >= currentRoom.roomCard.budget) {
            currentRoom.removeShotTokens(1);
            BoardLayersListener.removeAShot(currentRoom);      
            BoardLayersListener.textBox.append("\nSuccessfully Acted! " + currentRoom.remainingTakes + " shot tokens remaining here!");
            BoardLayersListener.textBox.append("\n" + name + ": '" + currentPart.line + "'\n");
         
            //check if player is on or off card for payout
            if(currentPart.onCard){
               BoardLayersListener.textBox.append("\n  +2 credits for on-the-card role!");
               credits += 2;
               BoardLayersListener.drawStats(this);
            }
            else{
               BoardLayersListener.textBox.append("\n  +1 of each for off-the-card role!");
               dollars += 1;
               credits += 1;
               BoardLayersListener.drawStats(this);
            }
         }
         
         //on unsuccessful acting
         else{
            BoardLayersListener.textBox.append("\n Failed to act (less than budget of $" + currentRoom.roomCard.budget + "M)!");
         
            //check if player is not on card for payout
            if(!currentPart.onCard){
               BoardLayersListener.textBox.append("\n  +1 dollar for off-the-card role.");
               dollars += 1;
               BoardLayersListener.drawStats(this);
            }
            BoardLayersListener.textBox.append("\nThere are still " + currentRoom.remainingTakes + " shot tokens remaining here!");
         }
      
         //check if the scene has been completed
         if(currentRoom.remainingTakes <= 0){
            BoardLayersListener.textBox.append("\n\n  The movie " + currentRoom.roomCard.name + " ($" + currentRoom.roomCard.budget + "M budget) has finished production! Let's pay the actors...");
            rollNTimes(currentRoom.roomCard.budget);
            currentRoom.wrapUp(multipleDiceValues);
         }
         BoardLayersListener.bAct.setBackground(Color.black);
         BoardLayersListener.bMove.setBackground(Color.black);
         BoardLayersListener.bRehearse.setBackground(Color.black);
         BoardLayersListener.bTakePart.setBackground(Color.black);
         BoardLayersListener.bAct.setEnabled(false);
         BoardLayersListener.bMove.setEnabled(false);
         BoardLayersListener.bRehearse.setEnabled(false);
         BoardLayersListener.bTakePart.setEnabled(false);
      }
      hasActed = true;
   }
   

   //rolls one die.
   void rollOnce(){
      Random rand = new Random();
      diceValue = rand.nextInt(6) + 1;
   }
   

   //rolls n dice
   void rollNTimes(int n){
      int i, k;
      Random rand = new Random();
      for(i = 0; i < n; i++){
         multipleDiceValues[i] = rand.nextInt(6) + 1;
      }
      for(k = i; k < 6-n; k++){ //set remaining values to 0 to avoid errors
         multipleDiceValues[k] = 0;
      }
   }


   //calculates a player's score
   int calcScore(){
      score = dollars + credits + (5 * rank);
      return 0;
   }
   
   
   //used to test whether or not a player can move
   public boolean canIMove(){
      if(hasMoved){
         return false;
      }
      else if(hasPart){
         return false;
      }
      else{
         return true;
      }
   }
   
   
   //used to test whether or not a player can rehearse
   public boolean canIAct(){
      if(hasPart){
         if(!hasMoved){
            if(!hasActed){
               if(!hasRehearsed){
                  return true;
               }
            }
         }
      }
      return false;
   }
}
