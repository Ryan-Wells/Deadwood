import java.util.*;

//Room class. Holds info about the room, and has several methods involving movies and players.
public class Room {

   String name;

   String[] neighbors;
   Part[] parts;
   Upgrade[] upgrades;
   Player[] actors = new Player[3]; //up to 3 players
   Take[] takes;

   int maxTakes;
   int remainingTakes;

   //area
   int x;
   int y;
   int h;
   int w;

   Card roomCard;

   boolean doneShooting;



   //removes a shot token from the room
   void removeShotTokens(int shotsRemoved){
      remainingTakes = remainingTakes - shotsRemoved;
      //below is just in case for testing, not necessary
      if (remainingTakes < 0){
         System.out.println("Too many shots removed!");
      }
   }

   //removes all shot counters, removes the card from the board after everything is done.
   void wrapUp(int[] diceValues){
      doneShooting = true;
      giveRewards(diceValues);
   
      //reset player values related to roles and rehearsal
      for (int i = 0; i < gameSystem.numPlayers; i++){
         if (gameSystem.players[i].currentRoom.roomCard == roomCard){
            gameSystem.players[i].removePart();
            gameSystem.players[i].rehearsalTokens = 0;
         }
      }
   
      //remove card from board
      Card nullCard = new Card();
      nullCard.name = "null";
      nullCard.flipped = true;
      roomCard = nullCard;
      
      gameSystem.remainingScenes--;
      
      //if there is only 1 remaining scene left, end the day.
      if(gameSystem.remainingScenes == 1){

         gameSystem.endDay();
      }
   }


   //rewards each player acting on the movie
   void giveRewards(int[] diceValues) {
    
      boolean anyPlayersOnCard = false;
      Arrays.sort(diceValues);
   
      if(roomCard.parts.length == 3){
         if(roomCard.parts[2]!= null){
         //if there is an actor for role 3...
            for(int i = 0; i < gameSystem.players.length; i++){
            
               if(gameSystem.players[i].currentPart != null){
                  if(gameSystem.players[i].currentPart.name.equals(roomCard.parts[2].name)){
                  
                     anyPlayersOnCard = true;
                  
                    //give them money from dice 2 and 5 (biggest)
                     System.out.println("Bonus money: " + gameSystem.players[i].name + " receives compensation for their on-the-card role!");
                     System.out.println("  They get " + diceValues[5] + " and " + diceValues[2] + " dollars from the bonus dice.");
                     gameSystem.players[i].dollars += diceValues[5];
                     gameSystem.players[i].dollars += diceValues[2];
                  }
               }
            }
         }
      }
   
      if(roomCard.parts[1].name != null){
        //if there is an actor for role 2...
         for(int i = 0; i < gameSystem.players.length; i++){
            if(gameSystem.players[i].currentPart != null){
               if(gameSystem.players[i].currentPart.name.equals(roomCard.parts[1].name)){
               
                  anyPlayersOnCard = true;
               
                  //give them money from dice 1 and 4
                  System.out.println("Bonus money: " + gameSystem.players[i].name + " receives compensation for their on-the-card role!");
                  System.out.println("  They get " + diceValues[4] + " and " + diceValues[1] + " dollars from the bonus dice.");
                  gameSystem.players[i].dollars += diceValues[4];
                  gameSystem.players[i].dollars += diceValues[1];
               }
            }
         }
      }
      
      if(roomCard.parts[0].name != null){
        //if there is an actor for role 1...
         for(int i = 0; i < gameSystem.players.length; i++){
            if(gameSystem.players[i].currentPart != null){
               if(gameSystem.players[i].currentPart.name.equals(roomCard.parts[0].name)){
               
                  anyPlayersOnCard = true;
               
                  //give them money from dice 0 (smallest) and 3
                  System.out.println("Bonus money: " + gameSystem.players[i].name + " receives compensation for their on-the-card role!");
                  System.out.println("  They get " + diceValues[3] + " and " + diceValues[0] + " dollars from the bonus dice.");
                  gameSystem.players[i].dollars += diceValues[3];
                  gameSystem.players[i].dollars += diceValues[0];
               }
            }
         }
      }
    
   
      //payout to players not on the card
      if(anyPlayersOnCard) {
         for (int j = 0; j < gameSystem.numPlayers; j++){
            if(gameSystem.players[j].currentPart != null){
               if ((gameSystem.players[j].currentPart.onCard == false) &&
               (gameSystem.players[j].currentRoom.roomCard == roomCard)) {
                  System.out.println("Bonus money:  " + gameSystem.players[j].name + " had an off-the-card role of rank " + gameSystem.players[j].currentPart.level + ". They earn " + gameSystem.players[j].currentPart.level + " dollars.");
                  gameSystem.players[j].dollars += gameSystem.players[j].currentPart.level;
               }
            }
         }
      }
      else{
         System.out.println("  Nobody had an on-the-card role for this movie, so there is no bonus cash.");
      }
   }



   //prints all information the room has
   public void printRoomInfo(){
   
      System.out.println("\n\n  Room name = '" + name + "'");
   
      if(roomCard != null){
         System.out.println("\n  Room card = '" + roomCard.name + "'");
      }
   
      if(neighbors != null){
         System.out.print("  Neighbors =");
         for(int i = 0; i < neighbors.length; i++){
            System.out.print("  '" + neighbors[i] + "'");
         }
      }
   
      System.out.print("\n  Takes =");
      if(remainingTakes == 0){
         System.out.print("  0");
      }
      else{
         for(int i = 0; i < remainingTakes; i++){
            System.out.print("  " + (i+1));
         }
      }
   
      if(parts != null){
         System.out.println("\n  Parts:");
         for(int i = 0; i < parts.length; i++){
            System.out.println("    " + parts[i]);
         }
      }
   
      if(upgrades != null){
         System.out.println("\n  Upgrades:");
         for(int i = 0; i < upgrades.length; i++){
            System.out.println("    " + upgrades[i]);
         }
      }
   
      if(actors != null){
         System.out.print("\n Current actors: ");
         if(actors[0] != null){
            System.out.print(actors[0].name + ",  ");
         }
         if(actors[1] != null){
            System.out.print(actors[1].name + ", and ");
         }
         if(actors[2] != null){
            System.out.println(actors[2].name + ".");
         }
      
      }
      System.out.println();
   }

}
