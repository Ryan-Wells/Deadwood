import java.util.*;

//Player class. Has information about the player, and lots of methods.
public class Player {

   static Scanner scan = new Scanner(System.in);

   String name;

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

   Part currentPart;
   Player nextPlayer;
   Room currentRoom;


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
      this.currentPart = new Part();
      this.currentPart.name = "null";
      this.nextPlayer = null; 
      this.currentRoom = null; 
   
   
   }


   //removes a player's part
   void removePart(){
      Part nullPart = new Part();
      currentPart = nullPart;
      hasPart = false;
      justTookPart = false;
      currentPart.name = "null";
   }


   //used to play the game.
   void playTurn(){
      isTurn = true;
      hasMoved = false;
      justTookPart = false;
   
   
      System.out.println("  Starting " + name + "'s turn...");
      System.out.println("     current Room = " + currentRoom.name);
   
      boolean canUpgrade = false;
      boolean canMove = false;
      boolean canAct = false;
      boolean canRehearse = false;
      boolean canTakePart = false;
   
      String userInput = "error";
      boolean deciding = true;
   
      while(deciding){
         //printing out the player's options
         System.out.print("\nSo what would you like to do?");
         System.out.print("   here are your options... \n   [  info    end turn  ");
      
         if(currentRoom.name.equals("office")){
            System.out.print("  upgrade  ");
            canUpgrade = true;
         }
         if(hasMoved == false){
            if(hasPart == false){
               System.out.print("  move  ");
               canMove = true;
            }
         }
         if(hasPart == true){
            canTakePart = false;
            if(justTookPart == false){
               System.out.print("  act    rehearse  ");
               canAct = true;
               canRehearse = true;
            }
         }
         if(hasPart == false){
            if(!currentRoom.name.equals("office") && !currentRoom.name.equals("trailer") && !currentRoom.doneShooting){
               System.out.print("  take part  ");
               canTakePart = true;
            }
         }
         System.out.println("]");
      
         userInput = scan.nextLine();
      
      
         if(userInput.equals("move")){  //move
            if(canMove && !hasMoved){
               System.out.println("Let's try to move.\n");
               //decide where to move
               System.out.print("So... your options are [");
               for(int i = 0; i < currentRoom.neighbors.length; i++){
                  System.out.print("  " + currentRoom.neighbors[i] + "  ");
               }
               System.out.println("]");
               boolean decidingRoom = true;
            
               while(decidingRoom){
                  System.out.println("please enter your destination... ('cancel' to cancel)");
                  userInput = scan.nextLine();
                  if(userInput.equals("cancel")){
                     decidingRoom = false;
                  }
                  else if(moveTo(userInput) == 0){
                     decidingRoom = false;
                  }
               }
            }
            else{
               System.out.println("You have already moved this turn, please choose another action or end your turn.");
            }
         }
         
         else if(userInput.equals("info")){  //info
            printInfo();
         }
         
         
         else if(userInput.equals("upgrade")){  //upgrade
            if(canUpgrade){
               System.out.println("Let's try to get you an upgrade... ");
            
            
            
               System.out.println("   Here are your options...  (you are currently rank " + rank + ")");
               for(int i = 0; i < currentRoom.upgrades.length; i++){
                  System.out.println("   [" + i + "] " + currentRoom.upgrades[i]);
                  
               }
               boolean decidingUpgrade = true;
               
               while(decidingUpgrade){
                  System.out.println("please select an upgrade (0 - 9 or cancel)");
                  userInput = scan.nextLine();
                  if(userInput.equals("cancel")){
                     decidingUpgrade = false;
                  }
                  else{
                     int selectedUpgrade = Integer.parseInt(userInput);
                     if(selectedUpgrade > -1 && selectedUpgrade < 10){
                     
                        //decide upgrades
                        if(currentRoom.upgrades[selectedUpgrade].currency.equals("dollar")){
                           if(currentRoom.upgrades[selectedUpgrade].level == rank){
                              System.out.println("You are already rank " + rank);
                           }
                           else if(currentRoom.upgrades[selectedUpgrade].level < rank){
                              System.out.println("You currently have a rank higher than this upgrade!");
                           }
                           else if(dollars < currentRoom.upgrades[selectedUpgrade].amount){
                              System.out.println("Not enough dollars! You need " + (currentRoom.upgrades[selectedUpgrade].amount - dollars) + " more dollars.");
                           }
                           else{
                              System.out.println("You spent " + currentRoom.upgrades[selectedUpgrade].amount + " dollars to upgrade your rank to " + currentRoom.upgrades[selectedUpgrade].level + "!");
                              dollars = dollars - currentRoom.upgrades[selectedUpgrade].amount;
                              rank = currentRoom.upgrades[selectedUpgrade].level;
                              System.out.println("Your rank is now " + rank + ", and you have " + dollars + " dollars remaining.");
                              decidingUpgrade = false;
                           }
                        }
                        else{
                           if(credits < currentRoom.upgrades[selectedUpgrade].amount){
                              System.out.println("Not enough credits! You need " + (currentRoom.upgrades[selectedUpgrade].amount - credits) + " more credits.");
                           }
                           else{
                              System.out.println("You spent " + currentRoom.upgrades[selectedUpgrade].amount + " credits to upgrade your rank to " + currentRoom.upgrades[selectedUpgrade].level + "!");
                              credits = credits - currentRoom.upgrades[selectedUpgrade].amount;
                              rank = currentRoom.upgrades[selectedUpgrade].level;     
                              System.out.println("Your rank is now " + rank + ", and you have " + credits + " credits remaining.");
                              decidingUpgrade = false;
                           }
                        }
                     }
                  }
               }
            }
         
            else{
               System.out.println("You cannot upgrade because you are in " + currentRoom.name + ", please move to the office to upgrade.");
            }
         }
         
         //act
         else if(userInput.equals("act")){
            if(canAct){
               System.out.println("Let's try to act...");
               act();
               System.out.println("Now that you have acted, your turn is over.\n\n\n");
               nextPlayer.playTurn();
            }
            else{
               System.out.println("You cannot act.");
            }
         }
         
         //rehearse
         else if(userInput.equals("rehearse")){
            if(canRehearse){
               System.out.println("Let's try to rehearse...");
               rehearse();
            
               System.out.println("Now that you have rehearsed, your turn is over.\n\n");
               nextPlayer.playTurn();
            }
            else{
               System.out.println("you cannot rehearse.");
            }
         }
         
         //take part
         else if(userInput.equals("take part")){
            if(canTakePart && !currentRoom.doneShooting){
               System.out.println("Let's try to take a part");
               System.out.print("So... your options are [");
               System.out.print(" Possible off-card parts:");
               for(int i = 0; i < currentRoom.parts.length; i++){
                  System.out.print("   '" + currentRoom.parts[i].name + "', rank " + currentRoom.parts[i].level);
               }
               System.out.print("\n          Possible on-card parts:");
               for(int i = 0; i < currentRoom.roomCard.parts.length; i++){
                  System.out.print("   '" + currentRoom.roomCard.parts[i].name + "', rank " + currentRoom.roomCard.parts[i].level);
               }
               System.out.println("]");
               boolean decidingPart = true;
            
               while(decidingPart){
                  System.out.println("please enter your part... ('cancel' to cancel)");
                  userInput = scan.nextLine();
                  if(userInput.equals("cancel")){
                     decidingPart = false;
                  }
                  else if(takePart(userInput) == 0){
                     decidingPart = false;
                     hasPart = true;
                     System.out.println("Since you have just taken a new part, you cannot act or rehearse on it and must therefore end your turn.\n\n");
                     nextPlayer.playTurn();
                  }
               }
            
            }
            else{
               System.out.println("You cannot take a part.");
            }
         }
         
         //cheating / debugging
         else if(userInput.equals("cheat")){
            cheatMe();
         }
         
         //ends the entire game
         else if(userInput.equals("flip table")){
            gameSystem.dayNumber = 3;
            gameSystem.endDay();
         
         }
         
         //end turn
         else if(userInput.equals("end turn")){
            System.out.println("Ending your turn...");
            nextPlayer.playTurn();
         }
         
         //garbage
         else{
            System.out.println("please enter a valid command.");
         }
      }
   }

   //used to debug and give info
   public void printInfo(){
      System.out.println("Your name is " + name + ". Your rank is " + rank + ", you have " + dollars + " dollars and " + credits + " credits. You have " + rehearsalTokens + " rehearsal tokens. Your current room is '" + currentRoom.name + "'.");
   
      if(hasPart){
         System.out.print("You are playing the part of '" + currentPart.name);
         
         if(currentPart.onCard){
            System.out.print("', an on-the-card role.");
         }
         if(!currentPart.onCard){
            System.out.print("', an off-the-card role.");
         }
      }
      
      System.out.println("Other players...");
      for(int i = 0; i < gameSystem.players.length; i++){
         if(!gameSystem.players[i].name.equals(name)){
            System.out.println("  " + gameSystem.players[i].name + ", rank " + gameSystem.players[i].rank + ", $" + gameSystem.players[i].dollars + ", " + gameSystem.players[i].credits + "cr, " + gameSystem.players[i].currentRoom.name + ".");
         }
      }
   
      System.out.println();
   }


   //used to move to a neighboring room.
   int moveTo(String destination){
   
      //check if the player has already moved
      if(hasMoved){
         System.out.println("Can only move once per turn.");
         return -1;
      }
   
      //check to see if they have a part
      if(hasPart){
         System.out.println("You have a part (" + currentPart.name + ") so you cannot move. Please act or rehearse");
         return -1;
      }
   
      //find room with destination name
      Room tempRoom = new Room();
      for(int i = 0; i < Board.currentBoard.length; i++){
         if(destination.equals(Board.currentBoard[i].name)){
            tempRoom = Board.currentBoard[i];
         }
      }
   
      //check if it is a neighbor
      for(int i = 0; i < currentRoom.neighbors.length; i++){
         //if it is, move to the room.
         if(destination.equals(currentRoom.neighbors[i])){
            System.out.println("movement from " + currentRoom.name + " to " + destination + " approved!");
            currentRoom = tempRoom;
            //flip the room's card if it hasn't been already
            if(currentRoom.roomCard != null && currentRoom.roomCard.flipped == false){
               currentRoom.roomCard.flipped = true;
            }
            hasMoved = true;
            return 0;
         }
      }
   
      //if it's not a neighbor or some other error occurs, don't move.
      if(!hasMoved){
         System.out.println("could not move from " + currentRoom.name + " to " + destination);
         return -1;
      }
      return -1;
   }


   //acts on a movie.
   void act(){ 
      if(hasPart){
         //roll one dice and check its value + rehearse bonus
         rollOnce();
         System.out.println("You rolled a " + diceValue + " (+" + rehearsalTokens + ")");
         //on successful acting
         if ((diceValue + rehearsalTokens) >= currentRoom.roomCard.budget) {
            System.out.println("Successfully Acted!");
            System.out.println(name + ": '" + currentPart.line + "'\n");
            currentRoom.removeShotTokens(1);
            System.out.println(currentRoom.remainingTakes + " shot tokens remaining here!");
            //check if player is on or off card for payout
            if(currentPart.onCard){
               System.out.println("  +2 credits for on-the-card role!");
               credits += 2;
            }
            else{
               System.out.println("  +1 of each for off-the-card role!");
               dollars += 1;
               credits += 1;
            }
         }
         
         //on unsuccessful acting
         else{
            System.out.println("Failed to Act (less than budget of $" + currentRoom.roomCard.budget + "M)!");
            //check if player is not on card for payout
            if(!currentPart.onCard){
               System.out.println("   +1 dollar for off-the-card role.");
               dollars += 1;
               System.out.println(currentRoom.remainingTakes + " shot tokens remaining here!");
            }
         }
      
         //check if the scene has been completed
         if (currentRoom.remainingTakes <= 0){
            System.out.println("  The movie " + currentRoom.roomCard.name + " ($" + currentRoom.roomCard.budget + "M budget) has finished production! Let's pay the actors...");
            
            rollNTimes(currentRoom.roomCard.budget);
            currentRoom.wrapUp(multipleDiceValues);
         }
      }
   
   }
   

   //adds a rehearsal token if the player currently has a part
   void rehearse(){
      if(hasPart){
         System.out.println("Rehearsing. (current part is '" + currentPart.name + "') ...  Rehearsal tokens: " + rehearsalTokens + " -> " + (rehearsalTokens + 1));
         rehearsalTokens++;
      }
      else{
         System.out.println("You must have a part before you can rehearse!");
         return;
      }
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
   

   //upgrade a player's rank
   int upgradeTo(Upgrade upgrade){
      Scanner scan = new Scanner(System.in);
      if(currentRoom.name.equals("office")){
         //display possible upgrades
         System.out.println("Possible upgrades:");
         for(int i = 0; i < currentRoom.upgrades.length; i++){
            System.out.println(" " + i + "): " + currentRoom.upgrades[i]);
         }
      
         System.out.println("\n please select an upgrade... (1-" + currentRoom.upgrades.length + ")");
         int requestedUpgrade = Integer.parseInt(scan.nextLine());
      
         //requested an upgrade requiring credits
         if(currentRoom.upgrades[requestedUpgrade].currency.equals("credit")){
            if(credits < currentRoom.upgrades[requestedUpgrade].amount){
               System.out.println("Not enough credits!");
               return -1;
            }
            else{
               System.out.println("Sufficient credits. Rank given!");
               rank = currentRoom.upgrades[requestedUpgrade].level;
               return 0;
            }
         }
         //requested an upgrade requiring dollars
         else{
            if(dollars < currentRoom.upgrades[requestedUpgrade].amount){
               System.out.println("Not enough dollars!");
               return -1;
            }
            else{
               System.out.println("Sufficient dollars. Rank given!");
               rank = currentRoom.upgrades[requestedUpgrade].level;
               return 0;
            }
         }
      }
      
      else{
         System.out.println("You are not currently in the casting office.");
         return -1;
      }
   }


   //player takes a part in the current room/card
   int takePart(String partName){
   
      boolean partFound = false;
   
      System.out.println("Requesting part '" + partName + "'...");
   
      //check for off-card parts
      for(int i = 0; i < currentRoom.parts.length; i++){
         if(currentRoom.parts[i].name.equals(partName)){
            partFound = true;
            if(rank < currentRoom.parts[i].level){
               System.out.println("Your rank is too low for that part.");
               return -1;
            }
            else if(currentRoom.parts[i].isTaken){
               System.out.println("That part is already taken.");
               return -1;
            }
            else{
               currentRoom.parts[i].isTaken = true;
               currentPart = currentRoom.parts[i];
            
               System.out.println("\nYour new part is now:  " + currentPart.name);
               return 0;
            }
         }
      }
   
      //check for on-card parts
      for(int i = 0; i < currentRoom.roomCard.parts.length; i++){
         if(currentRoom.roomCard.parts[i].name.equals(partName)){
            partFound = true;
            if(rank < currentRoom.roomCard.parts[i].level){
               System.out.println("Your rank is too low for that part.");
               return -1;
            }
            else if(currentRoom.roomCard.parts[i].isTaken){
               System.out.println("That part is already taken.");
               return -1;
            }
            else{
               currentRoom.roomCard.parts[i].isTaken = true;
               currentPart = currentRoom.roomCard.parts[i];
            
               System.out.println("\nYour new part is now:  " + currentPart.name);
               return 0;
            }
         }
      }
   
      if(partFound == false){
         System.out.println("\nCould not find the part '" + partName + "' so no assignment took place.");
         return -1;
      }
      return 0;
   }


   //sets the player's rank, dollars, credits, and rehearsal tokens manually, and may set the remaining scenes to speed up the game.
   private void cheatMe(){
      Scanner scan = new Scanner(System.in);
   
      System.out.println("new rank?");
      String userInput = scan.nextLine();
      rank = Integer.parseInt(userInput);
   
      System.out.println("dollars?");
      userInput = scan.nextLine();
      dollars = Integer.parseInt(userInput);
   
      System.out.println("credits?");
      userInput = scan.nextLine();
      credits = Integer.parseInt(userInput);
   
      System.out.println("rehearsal tokens?");
      userInput = scan.nextLine();
      rehearsalTokens = Integer.parseInt(userInput);
      
      System.out.println("current room?");
      userInput = scan.nextLine();
      
      Room tempRoom = currentRoom;
      for(int i = 0; i < Board.currentBoard.length; i++){
         if(userInput.equals(Board.currentBoard[i].name)){
            tempRoom = Board.currentBoard[i];
         }
      }
      currentRoom = tempRoom;
      
      System.out.println("remaining Scenes?");
      userInput = scan.nextLine();
      gameSystem.remainingScenes = Integer.parseInt(userInput);
      
      
      System.out.println("finished. stats are |" + rank + "|" + dollars + "|" + credits + "|" + rehearsalTokens + "|" + currentRoom.name + "| with " + gameSystem.remainingScenes + " remaining.");
   }

}
