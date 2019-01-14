//the Board class. Holds rooms and cards.
public class Board {

   public static Room[] currentBoard = new Room[12];
   public static Card[] deck = new Card[40];
   public static int currentBoardIndex = 0;
   public static int deckIndex = 0;

   //fills the board with cards.
   static void populateBoard(BoardLayersListener boardGUI){
   
      for(int i = 0; i < Board.currentBoardIndex; i++){
         if (currentBoard[i].name.equals("trailer") || currentBoard[i].name.equals("office")){
         //do nothing
         }
         else{
            //remove the cards before laying down new ones
            if(currentBoard[i].roomCard != null){
               BoardLayersListener.removeCard(currentBoard[i].roomCard);
               BoardLayersListener.flipCard(currentBoard[i].roomCard);
            }
         }
      }
   
      //iterate through rooms and assign a card to each room
      for(int i = 0; i < Board.currentBoardIndex; i++){
         //special handling for trailer and upgrade room (office)
         if (currentBoard[i].name.equals("trailer") || currentBoard[i].name.equals("office")){
            //do nothing
         }
         else{
            //assign a room a card, decrease the cardIndex as cards are used during the game
            currentBoard[i].roomCard = deck[deckIndex-1];
            currentBoard[i].roomCard.flipped = false;
            
            //set the cards and shots on the board GUI
            boardGUI.setCards(currentBoard[i].roomCard, currentBoard[i]);
            boardGUI.setCardBacks(currentBoard[i].roomCard, currentBoard[i]);
            boardGUI.setShots(currentBoard[i]);
           
            currentBoard[i].remainingTakes = currentBoard[i].maxTakes;
            for(int j = 0; j < currentBoard[i].roomCard.parts.length; j++){
               currentBoard[i].roomCard.parts[j].isTaken = false;
            }
            for(int k = 0; k < currentBoard[i].parts.length; k++){
               currentBoard[i].parts[k].isTaken = false;
            }
            currentBoard[i].doneShooting = false;
            deckIndex--;
         }
      }
   }

   //iterate through currentBoard and return index of a specific room.
   static int roomIndex(String roomName){
      for(int i = 0; i < Board.currentBoardIndex; i++){
         if (currentBoard[i].name.equals(roomName))
            return i;
      }
      return -1;
   }

}
