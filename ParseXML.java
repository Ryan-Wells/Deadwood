// Code for parsing Deadwood's XML files
// Ryan Wells, Fall 2018

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.*;

public class ParseXML{

   // building a document from the XML file
   // returns a Document object after loading the book.xml file.
   public Document getDocFromFile(String filename)
        throws ParserConfigurationException{
      {

         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = dbf.newDocumentBuilder();
         Document doc = null;
      
         try{
            doc = db.parse(filename);
         }
         catch (Exception ex){
            System.out.println("XML parse failure");
            ex.printStackTrace();
         }
         return doc;
      } // exception handling
   }


   //reads in data from the board.xml file
   public void readBoardData(Document d){
   
      Element root = d.getDocumentElement();
   
      NodeList setList = root.getElementsByTagName("set");
      NodeList office = root.getElementsByTagName("office");
      NodeList trailer = root.getElementsByTagName("trailer");
      
   
      //special handling for office:
   
      //reads data from the node
      Node officeNode = office.item(0);
      Element officeElement = (Element) officeNode;
      String officeName = "office";
   
      Room officeTemp = new Room();
      officeTemp.name = officeName;
   
      //area
      //implement later
   
      //neighbors
      NodeList officeNeighbors = officeElement.getElementsByTagName("neighbor");
   
      String[] officeNeighborList = new String[officeNeighbors.getLength()];
   
      for(int i = 0; i < officeNeighbors.getLength(); i++){
         Element n = (Element) officeNeighbors.item(i);
         String tempNeighborName = n.getAttribute("name");
         officeNeighborList[i] = tempNeighborName;
      }
      officeTemp.neighbors = officeNeighborList;
   
      //upgrades
      NodeList officeUpgrades = officeElement.getElementsByTagName("upgrade");
   
      Upgrade[] upgradeList = new Upgrade[officeUpgrades.getLength()];
      for(int k = 0; k < upgradeList.length; k++){
         upgradeList[k] = new Upgrade();
      }
   
      //put all upgradse into upgradeList
      for(int i = 0; i < officeUpgrades.getLength(); i++){
         Element n = (Element) officeUpgrades.item(i);
         upgradeList[i].level = Integer.parseInt(n.getAttribute("level"));
         upgradeList[i].currency = n.getAttribute("currency");
         upgradeList[i].amount = Integer.parseInt(n.getAttribute("amt"));
      
      }
   
      officeTemp.upgrades = upgradeList;
      Board.currentBoard[Board.currentBoardIndex++] = officeTemp;
 
      //and special handling for trailer
      //reads data from the node
      Node trailerNode = trailer.item(0);
   
      Element trailerElement = (Element) trailerNode;
      String trailerName = "trailer";
   
      Room trailerTemp = new Room();
      trailerTemp.name = trailerName;
   
      //area
      //implement later
   
      //neighbors
      NodeList trailerNeighbors = trailerElement.getElementsByTagName("neighbor");
   
      String[] trailerNeighborList = new String[trailerNeighbors.getLength()];
   
      for(int i = 0; i < trailerNeighbors.getLength(); i++){
         Element n = (Element) trailerNeighbors.item(i);
         String tempNeighborName = n.getAttribute("name");
         trailerNeighborList[i] = tempNeighborName;
      }
      trailerTemp.neighbors = trailerNeighborList;
   
      Board.currentBoard[Board.currentBoardIndex++] = trailerTemp;
   
   
      //handles all the sets
      for(int i = 0; i < setList.getLength(); i++){
      
         Room tempRoom = new Room();
      
         //reads data from the nodes
         Node setNode = setList.item(i);
      
         //set name
         String setName = setNode.getAttributes().getNamedItem("name").getNodeValue();
         tempRoom.name = setName;
      
         Element set = (Element) setNode;
         NodeList neighbors = set.getElementsByTagName("neighbor");
         NodeList takes = set.getElementsByTagName("take");
         NodeList parts = set.getElementsByTagName("part");
      
         //area
         //implement later
      
         //neighbors
         String[] tempNeighbors = new String[neighbors.getLength()];
      
         for(int j = 0; j < neighbors.getLength(); j++){
         
            Element n = (Element) neighbors.item(j);
            String tempName = n.getAttribute("name");
            tempNeighbors[j] = tempName;
         }
      
         tempRoom.neighbors = tempNeighbors;
      
      
         //takes
         for(int k = 0; k < takes.getLength(); k++){
         
            Element t = (Element) takes.item(k);
            int num = Integer.parseInt(t.getAttribute("number"));
         
            //area
            //implement later
         }
         tempRoom.maxTakes = takes.getLength();
      
         //parts
         Part[] tempParts = new Part[parts.getLength()];
         for(int j = 0; j < parts.getLength(); j++) {
            Part tempPart = new Part();
            Element n = (Element) parts.item(j);
         
            tempPart.name = n.getAttribute("name");
            tempPart.level = Integer.parseInt(n.getAttribute("level"));
            tempPart.line = n.getElementsByTagName("line").item(0).getTextContent();
            tempPart.onCard = false;
            tempPart.isTaken = false;
         
         
            tempParts[j] = tempPart;
         
            //area
            //implement later
         
         }
         tempRoom.parts = tempParts;
         Board.currentBoard[Board.currentBoardIndex++] = tempRoom;
      }
   }


   //reads in data from the cards.xml file
   public void readCardData(Document d){
   
      Card[] tempDeck = new Card[40];
      int firstOne = 1;
   
      Element root = d.getDocumentElement();
   
      NodeList deck = root.getElementsByTagName("card");
   
   
      for(int i = 0; i < deck.getLength(); i++){
      
         Card tempCard = new Card();
      
         //reads data from the nodes
         Node card = deck.item(i);
      
         //card name
         tempCard.name = card.getAttributes().getNamedItem("name").getNodeValue();
      
         //card image
         tempCard.image = card.getAttributes().getNamedItem("img").getNodeValue();
      
         //card budget
         tempCard.budget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());
         
         
         NodeList partCount = card.getChildNodes();//check edge cases for cards with less than 3 parts
         int counter = 0;
         for (int p = 0; p < partCount.getLength(); p++){
            if("part".equals(partCount.item(p).getNodeName())){
               counter++;
            }
         }
         Part[] tempParts = new Part[counter];
         int partsIndex = 0;
         for(int a = 0; a < counter; a++){
            tempParts[a] = new Part();
         }
      
         NodeList children = card.getChildNodes(); 
      
         for(int j = 0; j < children.getLength(); j++){

            Node sub = children.item(j);
         
         
            //scenes
            if("scene".equals(sub.getNodeName())){
               //scene number
               tempCard.scene = Integer.parseInt(sub.getAttributes().getNamedItem("number").getNodeValue());
            
               //scene description
               tempCard.description = sub.getTextContent();
            }
            
            
            //parts
            else if("part".equals(sub.getNodeName())){
            
               //part name
               tempParts[partsIndex].name = sub.getAttributes().getNamedItem("name").getNodeValue();
            
               //part level
               tempParts[partsIndex].level = Integer.parseInt(sub.getAttributes().getNamedItem("level").getNodeValue());
            
               //part area
               //implement later
            
               //part line
               Element n = (Element) children.item(j);
               tempParts[partsIndex].line = n.getElementsByTagName("line").item(0).getTextContent();
            
               tempParts[partsIndex].onCard = true;
               tempParts[partsIndex].isTaken = false;
            
               tempCard.parts = tempParts;
               partsIndex++;
            
            }
         }
      
         Board.deck[Board.deckIndex++] = tempCard;

      }
   }
}
