package com.speedme.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import com.speedme.entity.VehicleEntity;
import com.speedme.entity.WorldEntity;
import com.speedme.util.GameDef;

//First Person Camera Controller
public class SpeedMeGame3d
{

	private VehicleEntity vehicle;
	private List<WorldEntity> worldObjects;

	
	//3d vector to store the camera's position in
  private Vector3f    position    = null;
  //the rotation around the Y axis of the camera
  private float       yaw         = 0.0f;
  //the rotation around the X axis of the camera
  private float       pitch       = 0.0f;
  //Constructor that takes the starting x, y, z location of the camera
  public SpeedMeGame3d(float x, float y, float z)
  {
      //instantiate position Vector3f to the x y z params.
      position = new Vector3f(x, y, z);
      
      
  }
  public SpeedMeGame3d() { // This is made so the line FPCameraController app = new FPCameraController(); will work
              // TODO Auto-generated constructor stub
      }
      //increment the camera's current yaw rotation
  public void yaw(float amount)
  {
      //increment the yaw by the amount param
      yaw += amount;
  }

  //increment the camera's current yaw rotation
  public void pitch(float amount)
  {
      //increment the pitch by the amount param
      pitch += amount;
  }
  //moves the camera forward relitive to its current rotation (yaw)
  public void walkForward(float distance)
  {
              position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
              position.z += distance * (float)Math.cos(Math.toRadians(yaw));
  }

  //moves the camera backward relitive to its current rotation (yaw)
  public void walkBackwards(float distance)
  {          
              position.x += distance * (float)Math.sin(Math.toRadians(yaw));
              position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
  }

  //strafes the camera left relitive to its current rotation (yaw)
  public void strafeLeft(float distance)
  {
              position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
              position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
  }

  //strafes the camera right relitive to its current rotation (yaw)
  public void strafeRight(float distance)
  {
              position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
              position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));
  }

  //translates and rotate the matrix so that it looks through the camera
  //this dose basic what gluLookAt() does
  public void lookThrough()
  {
      //roatate the pitch around the X axis
      GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
      //roatate the yaw around the Y axis
      GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
      //translate to the position vector's location
      GL11.glTranslatef(position.x, position.y, position.z);
  }
  private static boolean gameRunning=true;
  private static int targetWidth = 800;
  private static int targetHeight = 600;

  private void init(){
      //load textures here and other things
  }

  private float xrot=0.1f;
  private float yrot=0.1f;
  private float zrot=0.1f;

  /** The texture that’s been loaded */

  private static void initDisplay(boolean fullscreen){

      DisplayMode chosenMode = null;

      try {
              DisplayMode[] modes = Display.getAvailableDisplayModes();

              for (int i=0;i<modes.length;i++) {
                  if ((modes[i].getWidth() == targetWidth) && (modes[i].getHeight() == targetHeight)) {
                      chosenMode = modes[i];
                      break;
                  }
              }
          } catch (LWJGLException e) {
      Sys.alert("Error", "Unable to determine display modes.");
      System.exit(0);
  }

      // at this point if we have no mode there was no appropriate, let the user know
  // and give up
      if (chosenMode == null) {
          Sys.alert("Error", "Unable to find appropriate display mode.");
          System.exit(0);
      }

      try {
          Display.setDisplayMode(chosenMode);
          Display.setFullscreen(fullscreen);
          Display.setTitle("Secret Title");
          Display.create();

      }
      catch (LWJGLException e) {
          Sys.alert("Error","Unable to create display.");
          System.exit(0);
      }

}

  private static boolean initGL(){
      GL11.glMatrixMode(GL11.GL_PROJECTION);
      GL11.glLoadIdentity();

//      Calculate the aspect ratio of the window
      GLU.gluPerspective(45.0f,((float)targetWidth)/((float)targetHeight),0.1f,100.0f);
      GL11.glMatrixMode(GL11.GL_MODELVIEW);
      GL11.glLoadIdentity();

      GL11.glEnable(GL11.GL_TEXTURE_2D);                                    // Enable Texture Mapping ( NEW )
      GL11.glShadeModel(GL11.GL_SMOOTH);
      GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
      GL11.glClearDepth(1.0f);
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glDepthFunc(GL11.GL_LEQUAL);
      GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
      return true;
  }
      public boolean isKeyPressed(int keyCode) {
              // apparently, someone at decided not to use standard

              // keycode, so we have to map them over:

              switch(keyCode) {
              case KeyEvent.VK_SPACE:
                      keyCode = Keyboard.KEY_SPACE;
                      break;
              case KeyEvent.VK_ESCAPE:
                      keyCode = Keyboard.KEY_ESCAPE;
                      break;
              case KeyEvent.VK_W:
                      keyCode = Keyboard.KEY_W;
                      break;
              case KeyEvent.VK_A:
                      keyCode = Keyboard.KEY_A;
                      break;
              case KeyEvent.VK_S:
                      keyCode = Keyboard.KEY_S;
                      break;
              case KeyEvent.VK_D:
                      keyCode = Keyboard.KEY_D;
                      break;
              }

              return org.lwjgl.input.Keyboard.isKeyDown(keyCode);
      }

  private void run(){
    SpeedMeGame3d camera = new SpeedMeGame3d(0, 0, 0);

          float dx        = 0.0f;
          float dy        = 0.0f;
          float dt        = 0.0f; //length of frame
          float lastTime  = 0.0f; // when the last frame was
          float time      = 0.0f;

          float mouseSensitivity = 0.15f;
          float movementSpeed = 10.0f; //move 10 units per second

     	  initEntities();
          //hide the mouse
          Mouse.setGrabbed(true);
      while(gameRunning){
          update();
          render();
          Display.update();

           //keep looping till the display window is closed the ESC key is down
          /*
          while (!Display.isCloseRequested() ||
           !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
              {
              */
              time = Sys.getTime();
             
              //here is your movement speed, which can be changed to anything
              dt = 0.0005f;
             
              lastTime = time;


              //distance in mouse movement from the last getDX() call.
              dx = Mouse.getDX();
              //distance in mouse movement from the last getDY() call.
              dy = Mouse.getDY();

              //control camera yaw from x movement from the mouse
              camera.yaw(dx * mouseSensitivity);
              //control camera pitch from y movement from the mouse
              camera.pitch(-dy * mouseSensitivity);


              //when passing in the distrance to move
              //we times the movementSpeed with dt this is a time scale
              //so if its a slow frame u move more then a fast frame
              //so on a slow computer you move just as fast as on a fast computer
             
              //OVER HERE! What do I do to make the boolean canWalk actually work the right way?
             
              if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
              {
                  camera.walkForward(movementSpeed*dt);
              }
              if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
              {
                  camera.walkBackwards(movementSpeed*dt);
              }
              if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left
              {
                  camera.strafeLeft(movementSpeed*dt);
              }
              if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right
              {
                  camera.strafeRight(movementSpeed*dt);
              }

              //set the modelview matrix back to the identity
              GL11.glLoadIdentity();
              //look through the camera before you draw anything
              camera.lookThrough();
              //you would draw your scene here.

              //draw the buffer to the screen
              //Display.update();
          //}

          // finally check if the user has requested that the display be
          // shutdown
          if (Display.isCloseRequested()) {
                  gameRunning = false;
                  Display.destroy();
                  System.exit(0);
              }
          if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
          {
              Sys.alert("Close","To continue, press ESCAPE on your keyboard or OK on the screen.");
              System.exit(0);
             
          }
      }
  }

  private void update(){
      xrot+=0.1f;
      yrot+=0.1f;
      zrot+=0.1f;
  }

  private void render(){
	  /*
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
      //GL11.glLoadIdentity();

      GL11.glTranslatef(0.0f,0.0f,-5.0f);                              // Move Into The Screen 5 Units

      GL11.glBegin(GL11.GL_QUADS);
              //HERE IS WHERE YOU BIND A TEXTURE
              // Front Face
              GL11.glTexCoord2f(0.0f, 0.0f);
              GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
              GL11.glTexCoord2f(1.0f, 0.0f);
              GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
              GL11.glTexCoord2f(1.0f, 1.0f);
              GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
              GL11.glTexCoord2f(0.0f, 1.0f);
              GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad

              // Back Face
              GL11.glTexCoord2f(1.0f, 0.0f);
              GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad
              GL11.glTexCoord2f(1.0f, 1.0f);
              GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
              GL11.glTexCoord2f(0.0f, 1.0f);
              GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
              GL11.glTexCoord2f(0.0f, 0.0f);
              GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad

              // Top Face
              GL11.glTexCoord2f(0.0f, 1.0f);
              GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
              GL11.glTexCoord2f(0.0f, 0.0f);
              GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
              GL11.glTexCoord2f(1.0f, 0.0f);
              GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
              GL11.glTexCoord2f(1.0f, 1.0f);
              GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad

              // Bottom Face
              GL11.glTexCoord2f(1.0f, 1.0f);
              GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Top Right Of The Texture and Quad
              GL11.glTexCoord2f(0.0f, 1.0f);
              GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Top Left Of The Texture and Quad
              GL11.glTexCoord2f(0.0f, 0.0f);
              GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
              GL11.glTexCoord2f(1.0f, 0.0f);
              GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad

              // Right face
              GL11.glTexCoord2f(1.0f, 0.0f);
              GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad
              GL11.glTexCoord2f(1.0f, 1.0f);
              GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
              GL11.glTexCoord2f(0.0f, 1.0f);
              GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
              GL11.glTexCoord2f(0.0f, 0.0f);
              GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad

              // Left Face
              GL11.glTexCoord2f(0.0f, 0.0f);
              GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad
              GL11.glTexCoord2f(1.0f, 0.0f);
              GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
              GL11.glTexCoord2f(1.0f, 1.0f);
              GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
              GL11.glTexCoord2f(0.0f, 1.0f);
              GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
      GL11.glEnd();*/
	  
		//Desenha objetos do mundo
		for (WorldEntity worldE : worldObjects) {
			worldE.draw();
		}

		//Desenha veículo
		vehicle.draw();
	  
  }

  //Criar entities - só desenha no draw
	private void initEntities() {
		initWorld();
		vehicle = new VehicleEntity(500,350,80,47, 1);
	}

	//COnfigura o mundo
	private void initWorld() {

		worldObjects = new ArrayList<WorldEntity>();
		worldObjects.add(new WorldEntity(100,0,80,200, GameDef.TEXTURE_WOOD));
		worldObjects.add(new WorldEntity(300,0,80,200, GameDef.TEXTURE_WOOD));
		
	}

  public static void runGame(String[] args)
{
      SpeedMeGame3d main = new SpeedMeGame3d();
      initDisplay(false);
              initGL();
              main.init();
              main.run();
      }
}