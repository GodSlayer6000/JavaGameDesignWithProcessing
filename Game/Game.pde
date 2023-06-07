/* Game Class Starter File
 * Last Edit: 5/23/2023
 * Authors: Oscar Ahuatl & Saifur Rahman
 */

//import processing.sound.*;
import java.util.*;

//GAME VARIABLES
private int msElapsed = 0;
String titleText = "Farming Simulater 2k23";
String extraText = "Start Farming!";

//Screens
Screen currentScreen;
World currentWorld;
Grid currentGrid;

//Splash Screen Variables
Screen splashScreen;
String splashBgFile = "images/background.jpg";
PImage splashBg;

//Main Screen Variables
Grid mainGrid;
//Grid farmLand; //6x6??
String mainBgFile = "images/farm.png";
PImage mainBg;

PImage player1;
String player1File = "images/newsteve.png";
int player1Row = 2;
int player1Col = 2;
int health = 100;
int money = 0;

PImage enemy;
String enemyFile = "images/zombie.png";

//EndScreen variables
World endScreen;
PImage endBg;
String endBgFile = "images/youwin.png";

//Example Variables
//SoundFile song;
AnimatedSprite exampleSprite;
boolean doAnimation;


//Required Processing method that gets run once
void setup() {

  //Match the screen size to the background image size
  size(800, 600);

  //Set the title on the title bar
  surface.setTitle(titleText);

  //Load BG images used
  splashBg = loadImage(splashBgFile);
  splashBg.resize(800,600);
  mainBg = loadImage(mainBgFile);
  mainBg.resize(800,600);
  endBg = loadImage(endBgFile);
  endBg.resize(800,600);

  //setup the screens/worlds/grids in the Game
  splashScreen = new Screen("splash", splashBg);
  mainGrid = new Grid("farm", mainBg, 15,15);
  endScreen = new World("end", endBg);
  currentScreen = splashScreen;
  currentGrid= mainGrid;

  //setup the sprites
  player1 = loadImage(player1File);
  //player1.resize(mainGrid.getTileWidthPixels(),mainGrid.getTileHeightPixels());
  player1.resize(75,75);
  enemy = loadImage(enemyFile);
  enemy.resize(50,50);
  

  //Other Setup
  // Load a soundfile from the /data folder of the sketch and play it back
  // song = new SoundFile(this, "sounds/Lenny_Kravitz_Fly_Away.mp3");
  // song.play();

  //Animation & Sprite setup
  exampleAnimationSetup();

  imageMode(CORNER);    //Set Images to read coordinates at corners
  //fullScreen();   //only use if not using a specfic bg image
  println("Game started...");
  
}

//Required Processing method that automatically loops
//(Anything drawn on the screen should be called from here)
void draw() {

  updateTitleBar();

  if (msElapsed % 500 == 0) {
    populateSprites();
    moveSprites();
  }

  updateScreen();
  
  if(isGameOver()){
    endGame();
  }

  checkExampleAnimation();
  
  msElapsed +=100;
  currentScreen.pause(100);

}

//Known Processing method that automatically will run whenever a key is pressed
void keyPressed(){

  //check what key was pressed
  System.out.println("Key pressed: " + keyCode); //keyCode gives you an integer for the key

  //What to do when a key is pressed?
  
  //set [W] key to move the player1 up & avoid Out-of-Bounds errors
  if(keyCode == 87 && player1Row != 0){
    
    //Store old GridLocation
    GridLocation oldLoc = new GridLocation(player1Row, player1Col);
    
    //Erase image from previous location
    currentGrid.clearTileImage(oldLoc);

    //change the field for player1Row
    player1Row--;

  }

  if(keyCode == 65 && player1Col != 0){
    //check case where out of bounds
    

    //shift the player1 picture up in the 2D array
    GridLocation oldLoc = new GridLocation(player1Row, player1Col);
    

    //eliminate the picture from the old location
    currentGrid.clearTileImage(oldLoc);
    //change the field for player1Row
    player1Col--;

  }
if(keyCode == 83 && player1Row != currentGrid.getNumRows()-1 ){
    //check case where out of bounds
    
    
    //shift the player1 picture up in the 2D array
    GridLocation oldLoc = new GridLocation(player1Row, player1Col);

    //eliminate the picture from the old location
    currentGrid.clearTileImage(oldLoc);
//change the field for player1Row
    player1Row++;


  }
  if(keyCode == 68 && player1Col != currentGrid.getNumCols()-1){
    //check case where out of bounds
    


    //shift the player1 picture up in the 2D array
    GridLocation oldLoc = new GridLocation(player1Row, player1Col);

    //eliminate the picture from the old location
    currentGrid.clearTileImage(oldLoc);
    //change the field for player1Row
    player1Col++;
  }
}
  //Known Processing method that automatically will run when a mouse click triggers it
  void mouseClicked(){
  
  //check if click was successful
  System.out.println("Mouse was clicked at (" + mouseX + "," + mouseY + ")");
  if(currentGrid != null){
    System.out.println("Grid location: " + currentGrid.getGridLocation());
  }

    //what to do if clicked?
    GridLocation clickedLoc = currentGrid.getGridLocation();
    GridLocation player1Loc = new GridLocation(player1Row, player1Col);
    
    if(clickedLoc.equals(player1Loc)){
      player1Col--;
    }


  //Toggle the animation on & off
  doAnimation = !doAnimation;
  System.out.println("doAnimation: " + doAnimation);
  if(currentGrid != null){
    currentGrid.setMark("X",currentGrid.getGridLocation());
  }
    
  }





//------------------ CUSTOM  METHODS --------------------//

//method to update the Title Bar of the Game
public void updateTitleBar(){

  if(!isGameOver()) {
    //set the title each loop
    surface.setTitle(titleText + "    " + extraText + "Health bar: " + health);

    //adjust the extra text as desired
  
  }

}

//method to update what is drawn on the screen each frame
public void updateScreen(){

  //Update the Background
  background(currentScreen.getBg());

  //splashScreen update
  if(splashScreen.getScreenTime() > 3000 && splashScreen.getScreenTime() < 5000){
    currentScreen = mainGrid;
  }

  //skyGrid Screen Updates
  if(currentScreen == mainGrid){
    currentGrid = mainGrid;

    //Display the Player1 image
    GridLocation player1Loc = new GridLocation(player1Row,player1Col);
    //GridLocation player1Loc = new GridLocation(5,5);
    currentGrid.setTileImage(player1Loc, player1);

    //Update other screen elements
    currentGrid.showImages();
    currentGrid.showSprites();
    currentGrid.showGridSprites();
      
  }

  //Update other screens?




}

//Method to populate enemies or other sprites on the screen
public void populateSprites(){

  //What is the index for the last column?
  int intCol = currentGrid.getNumCols()-1;

  //Loop through all the rows in the last column
  for(int r = 0; r < currentGrid.getNumRows(); r++){

    //Generate a random number
    double rando = Math.random() * 10 ;
    int ranrow = (int) (Math.random()*2) +13;

    //10% of the time, decide to add an enemy image to a Tile
   if(rando < 0.1){
     currentGrid.setTileImage(new GridLocation(r, ranrow), enemy);

   }
  }
  
}

//Method to move around the enemies/sprites on the screen
public void moveSprites(){

//Loop through all of the rows & cols in the grid
  for(int r = 0; r < currentGrid.getNumRows(); r++){
    for(int c = 0; c < currentGrid.getNumCols(); c++){

      GridLocation loc = new GridLocation(r,c);

      if(c ==0){
        currentGrid.clearTileImage(loc);
      }
      else if(c != 0){
        GridLocation newLoc;
        if (player1Row < r){
          newLoc = new GridLocation(r-1,c);
        } else if (player1Row > r){
          newLoc = new GridLocation(r+1,c);
        } else if (player1Col > c){
          newLoc = new GridLocation(r,c+1);
        } else {
          newLoc = new GridLocation(r,c-1);
        }

        
        checkCollision(loc, newLoc);

        if(currentGrid.hasTileImage(loc)   && !loc.equals( new GridLocation(player1Row, player1Col))    ){
          currentGrid.setTileImage(newLoc, currentGrid.getTileImage(loc));
          currentGrid.clearTileImage(loc);

        }

      }
     
      // What does this section do???
      // if(r != 0 && r < currentGrid.getNumRows()){
      //   GridLocation newLoc = new GridLocation(r-1 ,c);
        
      //   checkCollision(loc, newLoc);

      //   if(currentGrid.hasTileImage(loc)   && !loc.equals( new GridLocation(player1Row, player1Col))    ){
      //     currentGrid.setTileImage(newLoc, currentGrid.getTileImage(loc));
      //     currentGrid.clearTileImage(loc);

      //   }

      // }
    
  
     

    }
  }
}
      //Store the 2 tile locations to move

      //Check if the current tile has an image that is not player1      


        //Get image/sprite from current location


        //CASE 1: Collision with player1


        //CASE 2: Move enemy over to new location

        
        //Erase image/sprite from old location
        
        //System.out.println(loc + " " + currentGrid.hasTileImage(loc));


      //CASE 3: Enemy leaves screen at first column



//Method to handle the collisions between Sprites on the Screen
public boolean checkCollision(GridLocation loc, GridLocation nextLoc){

//check current location first
PImage image = currentGrid.getTileImage(loc);
AnimatedSprite sprite = currentGrid.getTileSprite(loc);

if(image == null && sprite == null){
  return false;
}

//check next location
PImage nextImage = currentGrid.getTileImage(nextLoc);
AnimatedSprite nextSprite = currentGrid.getTileSprite(nextLoc);

//if(nextImage == null && nextSprite == null){
//  return false;
//}

//check if mob hit the player
//if(enemy.equals(image) && player1.equals(nextImage)){
if(player1.equals(nextImage) && enemy.equals(image)){
  System.out.println("you been hit");

  //clear out enemy if it hits
  currentGrid.clearTileSprite(loc);
  int cash = (int)(Math.random() * 10) + 5;

  money+=cash;
  System.out.println("Cash: " + money);
    
  //lose hp
  System.out.println("lost health: " + health);

  health--;
  System.out.print("lost health: " + health);
  

}



return true;


}
        
      

public void handleCollisions(){


}

//method to indicate when the main game is over
public boolean isGameOver(){
  return false; //by default, the game is never over
}

//method to describe what happens after the game is over
public void endGame(){
    System.out.println("Game Over!");

    //Update the title bar

    //Show any end imagery
    currentScreen = endScreen;
    //image(endBg, 100,100);

}

//example method that creates 5 horses along the screen
public void exampleAnimationSetup(){  
  int i = 4;
  exampleSprite = new AnimatedSprite("sprites/texture.png", 50.0, i*75.0, "sprites/texture.json");
}

//example method that animates the horse Sprites
public void checkExampleAnimation(){
  if(doAnimation){
    exampleSprite.resize(200,200);
    exampleSprite.animateHorizontal(5.0, 1.0, true);
  }
}