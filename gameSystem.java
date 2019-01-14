import java.util.Arrays;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;

//gameSystem - controls the game. Ends days, finds winner, keeps track of players, initializes the game.
public class gameSystem{

   static Scanner scan = new Scanner(System.in); //scanner for user input

   static int dayNumber;
   static int remainingScenes;
   static int numPlayers;
   static Player[] players;
   static BoardLayersListener boardGUI;
   static Player currentPlayer;

   //initializes the game.
   static void startGame(){
      dayNumber = 1;
      remainingScenes = 10;
      shuffle(Board.deck);
      boardGUI = new BoardLayersListener();
      Board.populateBoard(boardGUI);
      
      //testing methods that implement gui from bourslayoutlistener
      boardGUI.setVisible(true);
   
      //get number of players and create players
      String userInput;
      boolean validInput = false;
          
      numPlayers = boardGUI.getPlayers();
         
      //initialize players
      players = new Player[numPlayers];
      for (int i = 0; i < numPlayers; i++){   
         players[i] = new Player();
         players[i].init("Player " + (i+1)); 
      }
   
      if(numPlayers == 2){
         players[0].nextPlayer = players[1];
         players[0].color = "r";
         players[1].nextPlayer = players[0];
         players[1].color = "g";
      }
   
      if(numPlayers == 3){
         players[0].nextPlayer = players[1];
         players[0].color = "r";
         players[1].nextPlayer = players[2];
         players[1].color = "g";
         players[2].nextPlayer = players[0];
         players[2].color = "b";
      }
   
      boardGUI.initPlayers(numPlayers, players);
      
      returnPlayersToTrailers();
      players[0].playTurn();
   }


   //used to find the player with the highest score at the end of the game.
   static void findWinner(){
      BoardLayersListener.textBox.setText("");
      int highScore = 0;
      String[] cont = new String[] {"Continue"};
      boolean anyTies = false;
      Player winner = new Player();
      for(int i = 0; i < players.length; i++){
         players[i].score = (players[i].dollars + players[i].credits + (5 * players[i].rank));
         BoardLayersListener.textBox.append(players[i].name + " scored " + players[i].score + " points\n");
         if(players[i].score > highScore){
            winner = players[i];
            highScore = players[i].score;
         }
      }
      //check for ties
      for(int i = 0; i < players.length; i++){
         if(players[i].score == winner.score && !players[i].name.equals(winner.name)){
            anyTies = true;
         }
      }
      if(!anyTies){
      
         int finish = JOptionPane.showOptionDialog(null, "With a score of " + winner.score + ", " + winner.name + " is the winner!", "Message",
                 JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                 null, cont, cont[0]);
      }
      else{
         int finish = JOptionPane.showOptionDialog(null, "We have a tie! Congratulations to the winners.", "Message",
                 JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                 null, cont, cont[0]);
      }
   }


   //ends the current day. Returns players to trailers, repopulates board, finishes game if applicable.
   static void endDay(){
      String[] cont = new String[] {"Continue"};
      returnPlayersToTrailers();
   
      int yes = JOptionPane.showOptionDialog(null, "Ending day " + dayNumber++, "Message",
                 JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                 null, cont, cont[0]);
                 
      if(dayNumber == 4){
         findWinner();
         System.exit(0);
      }
      else{
         remainingScenes = 8;
         Board.populateBoard(boardGUI);
         players[0].playTurn();
      }
   }


   //returns all players to the trailers to start the next day
   static void returnPlayersToTrailers(){
      int roomIndex = Board.roomIndex("trailer");
      BoardLayersListener.bMove.setBackground(Color.white);
      BoardLayersListener.bMove.setEnabled(true);
      BoardLayersListener.bAct.setBackground(Color.black);
      BoardLayersListener.bAct.setEnabled(false);
      BoardLayersListener.bRehearse.setBackground(Color.black);
      BoardLayersListener.bRehearse.setEnabled(false);
      BoardLayersListener.bTakePart.setBackground(Color.black);
      BoardLayersListener.bTakePart.setEnabled(false);
      BoardLayersListener.bUpgrade.setBackground(Color.black);
      BoardLayersListener.bUpgrade.setEnabled(false);
      BoardLayersListener.bEndTurn.setBackground(Color.white);
      BoardLayersListener.bEndTurn.setEnabled(true);
      
      for (int i = 0; i < numPlayers; i++){
         players[i].rehearsalTokens = 0;
         if(players[i].currentRoom != null){
            players[i].currentRoom.playersInRoom -= 1;
         }
         players[i].currentRoom = Board.currentBoard[roomIndex];
         players[i].currentRoom.playersInRoom += 1;
         players[i].isTurn = false;
         players[i].hasMoved = false;
         players[i].hasPart = false;
         players[i].currentPart = null;
         players[i].partName = "none";
         players[i].playerlabel.setBounds(players[i].currentRoom.x + (i * players[i].playerlabel.getIcon().getIconWidth()), 
                                   players[i].currentRoom.y + 120, players[i].playerlabel.getIcon().getIconWidth(), players[i].playerlabel.getIcon().getIconHeight());
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
