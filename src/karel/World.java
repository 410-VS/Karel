package karel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;
/**
 * Karel the Robot
 * Current version has 1 predefined map
 * @author Heather,Noel,Sam,Amber,Josh,MacsR4Luzrs
 */

public class World extends JPanel
{
    private final int OFFSET = 0;
    private final int SPACE = 31;
    private ArrayList walls = new ArrayList();//walls in world
    private ArrayList gems = new ArrayList(); //gems in world
    private ArrayList areas = new ArrayList(); //floors
    private boolean isRunning = true; //game ending bool
    Wall home = new Wall(0,0); // home space
    protected Player karel; //object for karel 
    private int w = 18;
    private int h = 14;
    
    //Map
    private String level =
              "###################\n"
            + "#                 #\n"
            + "#                 #\n"
            + "#                 #\n"
            + "#                 #\n"
            + "#                 #\n"
            + "#        $        #\n"
            + "#       $#$       #\n"
            + "#      $###$      #\n"
            + "#     $#####$     #\n"
            + "#    $#######$    #\n"
            + "#   $#########$   #\n"
            + "# ^ ###########   #\n"
            + "###################\n";
    
    //Constructor - Set up world
    public World()
    {
        initWorld();
        setFocusable(true);
    }
    
    public int getBoardWidth() 
    {
        return this.w;
    }

    public int getBoardHeight() 
    {
        return this.h;
    }

    
    //Reads the map and adds all objects and their coordinates to arraylists
    public final void initWorld()
    {
        //create wall and gem objects
        Wall wall;
        Gem gem;
        Area a;
        
        //variables used to keep track of coordinates during loop
        int x = 0;
        int y = 0;
        
        for (int i = 0; i < level.length(); i++)
        {
            //Grab the item in string at i
            char item = level.charAt(i); 
            
             a = new Area(x, y);
                areas.add(a);

            //Adjust X,Y value based on what character is at i
            //and create an item in the array list if needed
            if (item == '\n') 
            {
                y += SPACE;
                if (this.w < x) 
                {
                    this.w = x;
                }
              x = OFFSET;
            } 
            else if (item == '#') 
            {
                wall = new Wall(x, y);
                walls.add(wall);
                x += SPACE;
            }
            else if (item == '$') 
            {
                gem = new Gem(x,y);
                gems.add(gem);
                x += SPACE;
            } 
            else if (item == '.') 
            {
                a = new Area(x, y);
                areas.add(a);
                x += SPACE;
            } else if (item == '^') 
            {
                karel = new Player(x,y);
                x += SPACE;
            } 
            else if (item == ' ') 
            {
                x += SPACE;
            }

            h = y;
        }
        
   }
    
        public void buildWorld(Graphics g) {

        g.setColor(new Color(250, 240, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList world = new ArrayList();
        world.addAll(walls);
        world.addAll(areas);
        world.addAll(gems);
        world.add(karel);
        
        for (int i = 0; i < areas.size(); i++)
        {
            Entity item = (Entity) areas.get(i);
            g.drawImage(item.getImage(), item.GetX(), item.GetY(), this);
        }

        for (int i = 0; i < world.size(); i++) {

            Entity item = (Entity) world.get(i);

            if ((item instanceof Player)|| (item instanceof Gem)) 
            {
                g.drawImage(item.getImage(), item.GetX(), item.GetY(), this);
            } 
            else if(item instanceof Wall) 
            {
                g.drawImage(item.getImage(), item.GetX(), item.GetY(), this);
            }

    /*        if (completed) {
                g.setColor(new Color(0, 0, 0));
                g.drawString("Completed", 25, 20);
            }
*/
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        buildWorld(g);
    }
    
    public void choiceMade(String choice)
    {    
        //Get karels current direction
        char direction = karel.GetDirection();

        if ("go".equals(choice)) //Attempt to move the player
        {                
            switch(direction)
            {
                case '^':
                    handleMove(0,-SPACE);
                    break;
                case 'v':
                    handleMove(0, SPACE);
                    break;
                case '>':
                    handleMove(SPACE,0);
                    break;
                case '<':
                    handleMove(-SPACE,0);
                    break;
            }

        }
        else if ("left".equals(choice)) //Turn the player left
        {                
            switch(direction)
            {
                case '^':
                    karel.SetDirection('<');
                    karel.ChangeImage("left");
                    break;
                case 'v':
                    karel.SetDirection('>');
                    karel.ChangeImage("right");
                    break;
                case '>':
                    karel.SetDirection('^');
                    karel.ChangeImage("up");
                    break;
                case '<':
                    karel.SetDirection('v');
                    karel.ChangeImage("down");
                    break;
            }
        }
        else if ("right".equals(choice))//turn the player right
        {                
            switch(direction)
            {
                case '^':
                    karel.SetDirection('>');
                    karel.ChangeImage("right");
                    break;
                case 'v':
                    karel.SetDirection('<');
                    karel.ChangeImage("left");
                    break;
                case '>':
                    karel.SetDirection('v');
                    karel.ChangeImage("down");
                    break;
                case '<':
                    karel.SetDirection('^');
                    karel.ChangeImage("up");
                    break;
            }
        }
        
       else if ("manual".equals(choice)) //Get multiple commands
       {
            //actions();
       }

        this.repaint();
    }
     
    public void handleMove(int x, int y)
    {
        //Get where karel wants to move
        int newX = x + karel.GetX();
        int newY = y + karel.GetY();

        if (karel.isWallCollision(newX, newY, walls))
        {
            //collided with wall - do not move karel
        }
        else if (karel.isHomeCollision(newX,newY,home))
        {
            //if karel is home and all gems are taken, move and end game
            if(gems.isEmpty())
            {
                karel.move(x,y);
                isRunning = false;
                System.out.println("You have won!");
            }
        }
        else if (karel.isGemCollision(newX, newY, gems) != -1)
        {
            //pick up the gem and move karel
            gems.remove(karel.isGemCollision(newX, newY, gems));
            karel.move(x, y);
        }
        else
        {
            //move karel
            karel.move(x, y);
        }
    }
    
}
    

    
