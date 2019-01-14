import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.event.*;
import javax.swing.JOptionPane;

public class BoardLayersListener extends JFrame {

   //JLabels
   static JLabel boardlabel;
   static JLabel cardlabel;
   static JLabel backlabel;
   static JLabel playerlabel;
   static JLabel player1label;
   static JLabel player2label;
   static JLabel player3label;
   static JLabel mLabel;
   static JLabel shotlabel;
  
   //JButtons
   static JButton bAct;
   static JButton bRehearse;
   static JButton bMove;
   static JButton bTakePart;
   static JButton bUpgrade;
   static JButton bEndTurn;
   static JButton bCancel;
  
   //JLayered Pane
   static JLayeredPane bPane;
  
   //JTextArea
   static JTextArea playerInfo;
   static JTextArea textBox;
   
  
   //Constructor
   public BoardLayersListener() {
      
      //Set the title of the JFrame
      super("Deadwood");
      //Set the exit option for the JFrame
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      
      //Create the JLayeredPane to hold the display, cards, dice and buttons
      bPane = getLayeredPane();
    
      //Create the deadwood board
      boardlabel = new JLabel();
      ImageIcon icon =  new ImageIcon("images/board.jpg");
      boardlabel.setIcon(icon); 
      boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
      
      //Add the board to the lowest layer
      bPane.add(boardlabel, new Integer(0));
      
      //Set the size of the GUI
      setSize(icon.getIconWidth()+200,icon.getIconHeight());
       
      //create buttons
      createMenu(icon);     
   }
  
   //creating buttons for the GUI
   public void createMenu(ImageIcon icon){
      bMove = new JButton("Move");
      bMove.setBackground(Color.white);
      bMove.setBounds(icon.getIconWidth()+10, 30, 110, 20);
      bMove.addMouseListener(new boardMouseListener());
     
      bAct = new JButton("Act");
      bAct.setBackground(Color.white);
      bAct.setBounds(icon.getIconWidth()+10, 60, 110, 20);
      bAct.addMouseListener(new boardMouseListener());
     
      bRehearse = new JButton("Rehearse");
      bRehearse.setBackground(Color.white);
      bRehearse.setBounds(icon.getIconWidth()+10, 90, 110, 20);
      bRehearse.addMouseListener(new boardMouseListener());
      
      bTakePart = new JButton("Take part");
      bTakePart.setBackground(Color.white);
      bTakePart.setBounds(icon.getIconWidth()+10, 120, 110, 20);
      bTakePart.addMouseListener(new boardMouseListener());
      
      bUpgrade = new JButton("Upgrade");
      bUpgrade.setBackground(Color.white);
      bUpgrade.setBounds(icon.getIconWidth()+10, 150, 110, 20);
      bUpgrade.addMouseListener(new boardMouseListener());
      
      bEndTurn = new JButton("End Turn");
      bEndTurn.setBackground(Color.white);
      bEndTurn.setBounds(icon.getIconWidth()+10, 180, 110, 20);
      bEndTurn.addMouseListener(new boardMouseListener());
     
      playerInfo = new JTextArea ();
      playerInfo.setWrapStyleWord(true);
      playerInfo.setLineWrap(true);
      playerInfo.setBounds(1205, 210, 190, 115);
     
      textBox = new JTextArea ();
      textBox.setText("Initializing game");
      textBox.setWrapStyleWord(true);
      textBox.setLineWrap(true);
      textBox.setBounds(1205, 380, 190, 515);
      
      //Place the action buttons and text boxes in the top layer
      bPane.add(bAct, new Integer(2));
      bPane.add(bRehearse, new Integer(2));
      bPane.add(bMove, new Integer(2));
      bPane.add(bTakePart, new Integer(2));
      bPane.add(bUpgrade, new Integer(2));
      bPane.add(bEndTurn, new Integer(2));
      
      bPane.add(playerInfo, new Integer (2));
      bPane.add(textBox, new Integer (2));
   }
   
   //class to redraw the player stat box
   public static void drawStats(Player player){
      playerInfo.setText("Name: " + player.name +
         "\nRank: " + player.rank + "\nDollars: " +
         player.dollars + "\nCredits: " +
         player.credits + "\nRehearsal Tokens: " +
         player.rehearsalTokens + "\nPart: " +
         player.partName + "\nRoom: " +
         player.currentRoom.name + "\n");
   }
  
  
   //class to set cards in the gui
   public static void setCards(Card card, Room room){
      cardlabel = new JLabel();
      card.cardlabel = cardlabel;
      ImageIcon cIcon =  new ImageIcon("images/" + card.image);
      cardlabel.setIcon(cIcon); 
      cardlabel.setBounds(room.x, room.y, cIcon.getIconWidth()+2, cIcon.getIconHeight());
      cardlabel.setOpaque(true);
      
      //Add the card to the lower layer
      bPane.add(cardlabel, new Integer(1));
   }
  
   //puts the card backs onto the board to simulate unflipped cards
   public static void setCardBacks(Card card, Room room){
      backlabel = new JLabel();
      card.backlabel = backlabel;
      ImageIcon bIcon =  new ImageIcon("images/backOfCard.png");
      backlabel.setIcon(bIcon); 
      backlabel.setBounds(room.x, room.y, bIcon.getIconWidth()+2, bIcon.getIconHeight());
      backlabel.setOpaque(true);
      
      //Add the card to the lower layer og the GUI
      bPane.add(backlabel, new Integer(2));
   }
  
   //sets the shot counters onto the board
   public static void setShots(Room room){
      for (int i = 0; i < room.maxTakes; i++){
         shotlabel = new JLabel();
         ImageIcon sIcon = new ImageIcon("images/shot.png");
         shotlabel.setIcon(sIcon);
         if(room.takes[i].takelabel == null){
            room.takes[i].takelabel = shotlabel;
            shotlabel.setBounds(room.takes[i].x, room.takes[i].y, sIcon.getIconWidth(), sIcon.getIconHeight());
            shotlabel.setOpaque(true);
         }
         bPane.add(shotlabel, new Integer(3));
      }
   }
  
   //removes a shot token from a room
   public static void removeAShot(Room room){
      bPane.remove(room.takes[room.remainingTakes].takelabel); 
      room.takes[room.remainingTakes].takelabel = null;
      bPane.revalidate();
      bPane.repaint();
   }
  
  
   //ask for how many players are playing
   public static int getPlayers() {
      int numPLayers = 0;
      //asks for number of players
      String[] options = new String[] {"2", "3",};
      int option =  JOptionPane.showOptionDialog(bPane, "How many players are playing?", "Players",
         JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
      return (Integer.parseInt(options[option]));
   }
   
   //reset players to the trailer area for the first time!!!!! //THIS can probably be cleaned up
   public static void initPlayers(int numPlayers, Player[] players){
      for(int i = 0; i < numPlayers; i++){
         playerlabel = new JLabel();
         String newIcon = "images/" + players[i].color + "1.png";
         ImageIcon pIcon = new ImageIcon(newIcon);
         playerlabel.setIcon(pIcon);
      
         playerlabel.setBounds(998 + (i * pIcon.getIconWidth()), 383, 
                                      pIcon.getIconWidth(), pIcon.getIconHeight());
         playerlabel.setOpaque(true);
         players[i].playerlabel = playerlabel;
         
         // Add the player to the lower layer
         bPane.add(playerlabel, new Integer(3));
      }
   }
   
   //set players in the trailer after the day ends
   public static void returnPlayersToTrailers(){
      Room tempRoom = new Room();
      Icon pIcon;
      int trailerKey = Board.roomIndex("trailer");
      tempRoom = Board.currentBoard[trailerKey];
      for(int i = 0; i < gameSystem.numPlayers; i++){
         pIcon = gameSystem.players[i].playerlabel.getIcon();
         gameSystem.players[i].currentRoom.playersInRoom--;
         tempRoom.playersInRoom++;
         gameSystem.players[i].currentRoom = tempRoom;
         gameSystem.players[i].playerlabel.setBounds(tempRoom.x + (i * gameSystem.players[i].playerlabel.getIcon().getIconWidth()), 
                     tempRoom.y + 120, gameSystem.players[i].playerlabel.getIcon().getIconWidth(), gameSystem.players[i].playerlabel.getIcon().getIconHeight());
         bPane.add(gameSystem.players[i].playerlabel, new Integer(3));
      }
   }
   
   //"flips" a card 
   public static void flipCard(Card card){
      if(card.backlabel != null){
         bPane.remove(card.backlabel);
         bPane.revalidate();
         bPane.repaint();
         card.flipped = true;  
      }
   }
   
   //removes a card from the board's GUI
   public static void removeCard(Card card){
      if(card.cardlabel != null){
         bPane.remove(card.cardlabel);
         bPane.revalidate();
         bPane.repaint();
      }
   }
   
   //upgrades a player's rank using the GUI
   public static int playerUpgrade(Player player){
      if(!player.currentRoom.name.equals("office")){
         return -2;
      }
      int upgradeKey = JOptionPane.showOptionDialog(null, "Which upgrade would you like?", "Message",
                 JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                 null, player.currentRoom.upgrades, player.currentRoom.upgrades[0]);
                 
      //handle if the player closes the option box           
      if(upgradeKey == -1){
         return -1;
      }
                 
      //if they try to get an upgrade that doesn't raise their level
      if(player.rank >= player.currentRoom.upgrades[upgradeKey].level){
         textBox.append("\nYou are already rank " + player.rank);
         return -1;
      }
     
      //requested an upgrade requiring credits
      if(player.currentRoom.upgrades[upgradeKey].currency.equals("credit")){
         if(player.credits < player.currentRoom.upgrades[upgradeKey].amount){
            textBox.append("\nNot enough credits!");
            return -1;
         }
         else{
            player.credits -= player.currentRoom.upgrades[upgradeKey].amount;
            player.rank = player.currentRoom.upgrades[upgradeKey].level;
         }
      }
      //requested an upgrade requiring dollars
      else{
         if(player.dollars < player.currentRoom.upgrades[upgradeKey].amount){
            textBox.append("\nNot enough dollars!");
            return -1;
         }
         else{
            player.dollars -= player.currentRoom.upgrades[upgradeKey].amount;
            player.rank = player.currentRoom.upgrades[upgradeKey].level;
         }
      }
   
      textBox.append("\nUpgraded to rank " + player.rank);
      drawStats(player);  
      String newIcon = "images/" + player.color + player.rank + ".png";
      ImageIcon pIcon = new ImageIcon(newIcon);
      player.playerlabel.setIcon(pIcon);
      playerlabel.setOpaque(true);
      bPane.revalidate();
      bPane.repaint();
      return 0;
   }
   
   //moves a player from one spot to another
   public static void playerMove(Player player){
      if(!player.canIMove()){
         return;
      }
      
      BoardLayersListener.bTakePart.setBackground(Color.black);
      BoardLayersListener.bTakePart.setEnabled(false);
      int roomKey;
      String[] neighbors = player.currentRoom.neighbors;
      Room room = player.currentRoom;
      
      roomKey = JOptionPane.showOptionDialog(null, "Where would you like to move?", "Message",
                 JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                 null, neighbors, neighbors[0]);
      //handle close option of the dialog box
      if(roomKey == -1){
         return;
      }
      player.canTakePart = true;
      Icon pIcon = player.playerlabel.getIcon();
      
      Room tempRoom = new Room();
      for(int i = 0; i < Board.currentBoard.length; i++){
         if(neighbors[roomKey].equals(Board.currentBoard[i].name)){
            tempRoom = Board.currentBoard[i];
         }
      }
      textBox.append("\nMoved from " + player.currentRoom.name + " to " + tempRoom.name);
      player.currentRoom.playersInRoom--;
      tempRoom.playersInRoom++;
      if(tempRoom.name.equals("office")){
         bTakePart.setBackground(Color.black);
         bTakePart.setEnabled(false);
         bUpgrade.setBackground(Color.white);
         bUpgrade.setEnabled(true);
      }
      else{
         bUpgrade.setBackground(Color.black);
         bUpgrade.setEnabled(false);
      }
      if(tempRoom.name.equals("trailer")){
         bTakePart.setBackground(Color.black);
         bTakePart.setEnabled(false);
      }
      if(!tempRoom.name.equals("office") && !tempRoom.name.equals("trailer")){
         flipCard(tempRoom.roomCard);
      }
      bMove.setBackground(Color.black);
      bMove.setEnabled(false);
      player.currentRoom = tempRoom;
      player.playerlabel.setBounds(tempRoom.x + ((tempRoom.playersInRoom - 1) * pIcon.getIconWidth()), 
                                   tempRoom.y + 120, pIcon.getIconWidth(), pIcon.getIconHeight());
      bPane.add(player.playerlabel, new Integer(3));
      player.hasMoved = true;
      if(!player.currentRoom.doneShooting){
         bTakePart.setBackground(Color.white);
         bTakePart.setEnabled(true);
      }
      else{
         bTakePart.setBackground(Color.black);
         bTakePart.setEnabled(false);
      }
      drawStats(player);   
   }
   
   //used to take a part using the GUI
   public static void playerTakePart(Player player){
   
      if(!player.canTakePart){
         return;
      }
      if(bTakePart.isEnabled() == false){
         return;
      }
      int partKey;
      Part[] roomParts = player.currentRoom.parts;
      Part[] cardParts = player.currentRoom.roomCard.parts;
      Part[] parts = new Part[roomParts.length + cardParts.length]; 
      
      //merge all parts into one array
      for(int i = 0; i < cardParts.length; i++) {
         parts[i] = cardParts[i];
      }
      for(int i = 0; i < roomParts.length; i++) {
         parts[i + cardParts.length] = roomParts[i];    
      } 
      
      partKey = JOptionPane.showOptionDialog(null, "Which part would you like?", "Message", 
                 JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, 
                 null, parts, parts[0]);
                                          
      Icon pIcon = player.playerlabel.getIcon();
   
      for(int i = 0; i < parts.length; i++){
      
         //handle return value from closing the dialog box
         if(partKey == -1){
            return;
         }
      
         if(parts[partKey].equals(parts[i])){
         
            if(player.rank < parts[i].level){
               textBox.append("\nYou need to be rank " + parts[i].level + " or higher to take this part");
               return;
            }
            if(parts[i].isTaken){
               textBox.append("\nThis part is already taken");
               return;
            }
            player.currentPart = parts[i];
            player.partName = parts[i].name;
            player.currentPart.isTaken = true;
            drawStats(player);
         }
      }
      
      if (player.currentPart.onCard){
         player.playerlabel.setBounds(player.currentPart.x  + player.currentRoom.x, player.currentPart.y  + player.currentRoom.y, pIcon.getIconWidth(), pIcon.getIconHeight());
      }
      else{
         player.playerlabel.setBounds(player.currentPart.x + 3, player.currentPart.y + 4, pIcon.getIconWidth(), pIcon.getIconHeight());
      }
      System.out.println(player.currentPart.x+3 + " " + player.currentPart.y+4);
      bPane.add(player.playerlabel, new Integer(3));
      player.justTookPart = true;
      player.hasPart = true;
      bAct.setBackground(Color.black);
      bAct.setEnabled(false);
      bMove.setBackground(Color.black);
      bMove.setEnabled(false);
      bRehearse.setBackground(Color.black);
      bRehearse.setEnabled(false);
      bTakePart.setBackground(Color.black);
      bTakePart.setEnabled(false);
   }
   
   //method to end player turns
   public static void endTurn(Player player){
      player.isTurn = false;
      player.hasMoved = false;
      player.nextPlayer.playTurn();
   }

  // This class implements Mouse Events
   class boardMouseListener implements MouseListener{
   
      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {
         if(e.getSource()== bMove){
            System.out.println("Move is Selected\n");
            if(gameSystem.currentPlayer.canIMove()){
               playerMove(gameSystem.currentPlayer);
            }
         }   
         else if(e.getSource()== bAct){
            System.out.println("Acting is Selected\n");
            if(gameSystem.currentPlayer.canIAct()){
               gameSystem.currentPlayer.act();
            }
         }
         else if(e.getSource()== bRehearse){
            System.out.println("Rehearse is Selected\n");
            if(gameSystem.currentPlayer.hasPart){
               if(!gameSystem.currentPlayer.hasRehearsed){
                  if(!gameSystem.currentPlayer.justTookPart){
                     if(!gameSystem.currentPlayer.hasActed){
                        gameSystem.currentPlayer.rehearsalTokens++;
                        textBox.append("\nRehearsal tokens: " + (gameSystem.currentPlayer.rehearsalTokens - 1) + " > " + gameSystem.currentPlayer.rehearsalTokens);
                        drawStats(gameSystem.currentPlayer);
                        bAct.setBackground(Color.black);
                        bAct.setEnabled(false);
                        bRehearse.setBackground(Color.black);
                        bRehearse.setEnabled(false);
                        gameSystem.currentPlayer.hasRehearsed = true;
                     }
                  }
               }
            }
         }
         else if(e.getSource() == bTakePart){
            System.out.println("TakePart is selected\n");
            playerTakePart(gameSystem.currentPlayer);
         }
         else if(e.getSource() == bUpgrade){
            System.out.println("Upgrade is selected\n");
            playerUpgrade(gameSystem.currentPlayer);
         }
         else if(e.getSource() == bEndTurn){
            System.out.println("EndTurn is selected\n");
            endTurn(gameSystem.currentPlayer);
         }
    
      }
      public void mousePressed(MouseEvent e) {
      }
      public void mouseReleased(MouseEvent e) {
      }
      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }
   }
}
