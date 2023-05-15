/* autogenerated by Processing revision 1292 on 2023-05-15 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Game extends PApplet {

public void setup(){
/* size commented out by preprocessor */;
}
public void draw(){
    fill(500, 200, 500);
    circle(400, 300, 600);
    triangle(0, 0, 203, 42, 63, 423);
}
// Revised from Daniel Shiffman's p5js Animated Sprite tutorial
// Expects .json spritesheet from TexturePack software

// https://editor.p5js.org/codingtrain/sketches/vhnFx1mml
// http://youtube.com/thecodingtrain
// https://thecodingtrain.com/CodingChallenges/111-animated-sprite.html

// Example Horse Spritesheet from
// https://opengameart.org/content/2d-platformer-art-assets-from-horse-of-spring

// Example Animated Sprite
// https://youtu.be/3noMeuufLZY

public class AnimatedSprite extends Sprite{
  
    private ArrayList<PImage> animation;
    private int w;
    private int h;
    private int len;
    private int index;

    JSONObject spriteData;
    PImage spriteSheet;


  // Constructor for AnimatedSprite with Spritesheet (Must use the TexturePacker to make the JSON)
  // https://www.codeandweb.com/texturepacker
  public AnimatedSprite(String png, float x, float y, String json) {
    super("none", x, y, 1.0f, true);

    this.animation = new ArrayList<PImage>();
 
    spriteData = loadJSONObject(json);
    spriteSheet = loadImage(png);
    JSONArray frames = spriteData.getJSONArray("frames");
    
    for(int i=0; i<frames.size(); i++){

      JSONObject frame = frames.getJSONObject(i);
      //System.out.println(i + ": " + frame + "\n");
      JSONObject fr = frame.getJSONObject("frame");
      //System.out.println("ss: " + fr + "\n");

      int sX = fr.getInt("x");
      int sY = fr.getInt("y");
      int sW = fr.getInt("w");
      int sH = fr.getInt("h");
      System.out.println(i + ":\t sX:" + sX + ":\t sY:" + sY + ":\t sW:" + sW + ":\t sH:" + sH);
      PImage img = spriteSheet.get(sX, sY, sW, sH);
      animation.add(img);

      this.w = this.animation.get(0).width;
      this.h = this.animation.get(0).height;
      this.len = this.animation.size();
      this.index = 0;
    }
  }


  //Overriden method: Displays the correct frame of the Sprite image on the screen
  public void show() {
    int index = (int) Math.floor(Math.abs(this.index)) % this.len;
    image(animation.get(index), super.getX(), super.getY());
    //System.out.println("Pos: "+ super.getX() +"," + super.getY());
  } 

  //Method to cycle through the images of the animated sprite
  public void animate(float animationSpeed){
    index += (int) (animationSpeed * 10);
    show();
  }

  //animated method that makes the Sprite move to the right-left
  public void animateHorizontal(float horizontalSpeed, float animationSpeed, boolean wraparound) {

    //adjust speed & frames
    animate(animationSpeed);
    super.move( (int) (horizontalSpeed * 10), 0 );
  
    //wraparound sprite if goes off the right or left
    if(wraparound){
      wraparoundHorizontal();
    }

  }

  //animated method that makes the Sprite move down-up
  public void animateVertical(float verticalSpeed, float animationSpeed, boolean wraparound) {

    //adjust speed & frames
    animate(animationSpeed);
    super.move( 0, (int) (verticalSpeed * 10));
  
    //wraparound sprite if goes off the bottom or top
    if(wraparound){
      wraparoundVertical();
    }

  }


  //wraparound sprite if goes off the right-left
  private void wraparoundHorizontal(){
    if ( super.getX() > width ) {
      super.setX( -this.w );
    } else if ( super.getX() < -width ){
      super.setX( width );
    }
  }

  //wraparound sprite if goes off the top-bottom
  private void wraparoundVertical(){
    if ( super.getY() > height ) {
      super.setY( -this.h );
    } else if ( super.getY() < -height ){
      super.setY( height );
    }

  }



}
public class Grid{
  
  private int rows;
  private int cols;
  private GridTile[][] board;
  

  //Grid constructor that will create a Grid with the specified number of rows and cols
  public Grid(int rows, int cols){
    this.rows = rows;
    this.cols = cols;
    board = new GridTile[rows][cols];
    
    for(int r=0; r<rows; r++){
      for(int c=0; c<cols; c++){
         board[r][c] = new GridTile();
      }
    }
  }

  // Default Grid constructor that creates a 3x3 Grid  
  public Grid(){
     this(3,3);
  }

  // Method that Assigns a String mark to a location in the Grid.  
  // This mark is not necessarily visible, but can help in tracking
  // what you want recorded at each GridLocation.
  public void setMark(String mark, GridLocation loc){
    board[loc.getR()][loc.getC()].setNewMark(mark);
    printGrid();
  } 

  // Method that Assigns a String mark to a location in the Grid.  
  // This mark is not necessarily visible, but can help in tracking
  // what you want recorded at each GridLocation.  
  // Returns true if mark is correctly set (no previous mark) or false if not
  public boolean setNewMark(String mark, GridLocation loc){
    int row = loc.getR();
    int col = loc.getC();
    boolean isGoodClick = board[row][col].setNewMark(mark);
    printGrid();
    return isGoodClick;
  } 
  
  //Method that prints out the marks in the Grid to the console
  public void printGrid(){
   
    for(int r = 0; r<rows; r++){
      for(int c = 0; c<cols; c++){
         System.out.print(board[r][c]);
      }
      System.out.println();
    } 
  }
  
  //Method that returns the GridLocation of where the mouse is currently hovering over
  public GridLocation getGridLocation(){
      
    int row = mouseY/(pixelHeight/this.rows);
    int col = mouseX/(pixelWidth/this.cols);

    return new GridLocation(row, col);
  } 

  //Accessor method that provide the x-pixel value given a GridLocation loc
  public int getX(GridLocation loc){
    
    int widthOfOneTile = pixelWidth/this.cols;
 
    //calculate the center of the grid GridLocation
    int pixelX = (widthOfOneTile/2) + (widthOfOneTile * loc.getC()); 
    
    return pixelX;
  } 
  
  //Accessor method that provide the y-pixel value given a GridLocation loc
  public int getY(GridLocation loc){
    
    int heightOfOneTile = pixelHeight/this.rows;
 
    //calculate the center of the grid GridLocation
    int pixelY = (heightOfOneTile/2) + (heightOfOneTile * loc.getR()); 
    
    return pixelY;
  } 
  
  //Accessor method that returns the number of rows in the Grid
  public int getRows(){
    return rows;
  }
  
  //Accessor method that returns the number of cols in the Grid
  public int getCols(){
    return cols;
  }

  //Returns the GridTile object stored at a specified GridLocation
  public GridTile getTile(GridLocation loc){
    return board[loc.getR()][loc.getC()];
  }

  //Returns the GridTile object stored at a specified row and column
  public GridTile getTile(int r, int c){
    return board[r][c];
  }
  
}
public class GridLocation{
 
  int row;
  int col;
  
  public GridLocation(int row, int col){
    this.row = row;
    this.col = col;
  }
  
  public int getR(){
    return row;
  }
  
  public int getC(){
    return col;
  }
  
  public String toString(){
    return row + "," + col;
  }
  
}
public class GridTile{
  
  private PImage pi;
  private String mark;
  final private static String noMark = " ";

  //Default GridTile constructor which puts an empty String mark in the GridTile
  public GridTile(){
    this(noMark);
  }

  //GridTile constructor which adds the specified String mark
  public GridTile(String mark){
    this.mark = mark;

  }
  
  // Accessor method that gets the mark in the GridTile
  public String getMark(){
    return mark;
  }
  
  // Mutator method that automatically changes the mark
  public void setMark(String mark){
    this.mark = mark;
  }

  // Mutator method sets a new mark in the GridTile 
  // if it does not already have a mark, 
  // returns true or false if successful
  public boolean setNewMark(String mark){
    if(this.mark.equals(noMark)){
      this.mark = mark;
      System.out.println("Successfully changed mark");
      return true;
    } else {
      System.out.println("That GridTile is already taken!");
      return false;
    }
  }
  
  // Mutator method that sets an new PImage in the GridTile
  public void setImage(PImage pi){
    this.pi = pi;
  }

  //Accessor method that returns the PImage stored in the GridTile
  public PImage getImage(){
    return pi;
  }
  
  public String toString(){
    return mark;
  }


}
public class Platform {//extends Sprite {

	//Platform defined by it's center-x and top-Y positions
	public Platform(float posXCenter, float posYTop, float platWidth, float platHeight, int clr) {

		//pass along the center-x and center-y to Sprite super
		//super(posXCenter, posYTop + (platHeight/2), clr);
		//System.out.println("PlatTopY: " + posYTop + "\tPlatCenterY: " + (posYTop + (platHeight/2)));
		// setWidth(platWidth);
		// setHeight(platHeight);
		//setColor(Color.black);
	}

	public Platform(float posXCenter, float posYTop, float platWidth, float platHeight) {
		//pass along the center-x and center-y to Sprite super
		this(posXCenter, posYTop, platWidth, platHeight, color(0,0,0));
	}

}
// Inspired by Daniel Shiffman's p5js Animated Sprite tutorial

public class Sprite {
  
    PImage spriteImg;
    private float center_x;
    private float center_y;
    private float speed_x;
    private float speed_y;
    private float w;
    private float h;
    private boolean isAnimated;


  // Main Constructor
  public Sprite(String spriteImgPath, float scale, float x, float y, boolean isAnimated) {
    this.center_x = x;
    this.center_y = y;
    this.speed_x = 0;
    this.speed_y = 0;
    this.isAnimated = isAnimated;
    if(!isAnimated){
      this.spriteImg = loadImage(spriteImgPath);
      w = spriteImg.width * scale;
      h = spriteImg.height * scale;
    }
  }

  // Simpler Constructor for Non-Animated Sprite
  public Sprite(String spriteImg, float x, float y) {
    this(spriteImg, 1.0f, x, y, false);
  }


  // method to display the Sprite image on the screen
  public void show() {
      image(spriteImg, this.center_x, this.center_y, w, h);
  }

  // method to move Sprite image on the screen to a specific coordinate
  public void moveTo(float x, float y){
    this.center_x = x;
    this.center_y = y;
  }

  // method to move Sprite image on the screen relative to current position
  public void move(float change_x, float change_y){
    this.center_x += change_x;
    this.center_y += change_y;
  }

  // method that automatically moves the Sprite based on its velocity
  public void update(){
    move(speed_x, speed_y);
  }


  // method to rotate Sprite image on the screen
  public void rotate(float degrees){

  }


  /*-- ACCESSOR METHODS --*/
  public float getX(){
    return center_x;
  }
  public float getY(){
    return center_y;
  }
  public PImage getImg(){
    return spriteImg;
  }
  public boolean getIsAnimated(){
    return isAnimated;
  }
  
  
  /*-- MUTATOR METHODS --*/
  public void setX(float x){
    this.center_x = x;
  }
  public void setY(float y){
    this.center_y=y;
  }
  public void setImg(PImage img){
    this.spriteImg = img;
  }
  public void setIsAnimated(boolean a){
    isAnimated = a;
  }


  /*-- SPRITE BOUNDARY METHODS --
    -- Used from Long Bao Nguyen
    -- https://longbaonguyen.github.io/courses/platformer/platformer.html
  */
  public void setLeft(float left){
    center_x = left + w/2;
  }
  public float getLeft(){
    return center_x - w/2;
  }
  public void setRight(float right){
    center_x = right - w/2;
  }
  public float getRight(){
    return center_x + w/2;
  }
  public void setTop(float top){
    center_y = top + h/2;
  }
  public float getTop(){
    return center_y - h/2;
  }
  public void setBottom(float bottom){
    center_y = bottom - h/2;
  }
  public float getBottom(){
    return center_y + h/2;
  }
  

}


  public void settings() { size(800, 800); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
