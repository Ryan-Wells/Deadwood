import java.util.Arrays;
import java.util.Scanner;

//gameSystem - controls the game. Ends days, finds winner, keeps track of players, initializes the game.
public class gameSystem{

   static Scanner scan = new Scanner(System.in); //scanner for user input

   static int dayNumber;
   static int remainingScenes;
   static int numPlayers;
   static Player[] players;



   //initializes the game.
   static void startGame(){
      dayNumber = 1;
      remainingScenes = 8;
      shuffle(Board.deck);
      Board.populateBoard();
     
   
      //get number of players and create players
      String userInput;
      boolean validInput = false;
      while(validInput == false){
         System.out.println("how many players are playing? (2-3)");
         userInput = scan.nextLine();
      
         //only checking for 2 or 3 players options
         while(userInput.charAt(0) != '2' && userInput.charAt(0) != '3'){
            System.out.println("'2' for 2 players");
            System.out.println("'3' for 3 players");
            userInput = scan.nextLine();
         }
         validInput = true;
         numPlayers = Integer.parseInt(String.valueOf(userInput));
      }
   
      //initialize players
      players = new Player[numPlayers];
      for (int i = 0; i < numPlayers; i++){
         System.out.println("player " + (i+1) + ", What is your name?");
         userInput = scan.nextLine();
         players[i] = new Player();
         players[i].init(userInput);
      }
   
      if(numPlayers == 2){
         players[0].nextPlayer = players[1];
         players[1].nextPlayer = players[0];
      }
   
      if(numPlayers == 3){
         players[0].nextPlayer = players[1];
         players[1].nextPlayer = players[2];
         players[2].nextPlayer = players[0];
      }
   
      returnPlayersToTrailers();
      players[0].playTurn();
   }


   //used to find the player with the highest score at the end of the game.
   static Player findWinner(){
      int highScore = 0;
      Player winner = new Player();
      for(int i = 0; i < players.length; i++){
         players[i].score = (players[i].dollars + players[i].credits + (5 * players[i].rank));
         System.out.println(players[i].name + " finished with a score of " + players[i].score);
         if(players[i].score > highScore){
            winner = players[i];
            highScore = players[i].score;
         }
      }
      return winner;
   }


   //ends the current day. Returns players to trailers, repopulates board, finishes game if applicable.
   static void endDay(){
      returnPlayersToTrailers();
      dayNumber++;
      if(dayNumber == 4){
         System.out.println("\n\n  END OF THE THIRD DAY \n\n");
         Player victor = findWinner();
         System.out.println("\n\n\n----------------------------------------------------------\n   With a score of " + victor.score + ", " + victor.name + " is the winner!\n----------------------------------------------------------\n");
         System.exit(0);
      }
      else{
        System.out.println("\n\n\n\n\n   ENDING DAY " + (gameSystem.dayNumber - 1) + "  \n\n\n\n\n");
         remainingScenes = 8;
         Board.populateBoard();
         players[0].playTurn();
      }
   }


   //returns all players to the trailers to start the next day
   static void returnPlayersToTrailers(){
      int roomIndex = Board.roomIndex("trailer");
      for (int i = 0; i < numPlayers; i++){
         players[i].rehearsalTokens = 0;
         players[i].currentRoom = Board.currentBoard[roomIndex];
         players[i].isTurn = false;
         players[i].hasMoved = false;
         players[i].hasPart = false;
         players[i].currentPart = null;
      }
   }


   //shuffles the deck to avoid identical playthroughs
   static void shuffle(Object[] array) {
      for (int i = 0; i < 100; i++) {
         int r1 = (int)(Math.random()*array.length);
         int r2 = (int)(Math.random()*array.length);
         Object tmp = array[r1];
         array[r1] = array[r2];
         array[r2] = tmp;
      }
   }
   
}
