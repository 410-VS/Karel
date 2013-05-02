package karel;
import java.awt.Image;
import java.util.*;
//Primary class for all objects that will appear in world

public class Entity 
{
    //object coordinates
    //change x back to private and update collision
    protected int x;
    protected int y;
    private final int SPACE = 31;
    private Image image;
    
    //constructor sets object position
    public Entity(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public Image getImage() {
        return this.image;
    }

    public void setImage(Image img) {
        image = img;
    }
    
    // Get the x coordinate
    public int GetX()
    {
        return this.x;
    }
    
    // get the y coordinate 
    public int GetY()
    {
        return this.y;
    }
    
    // set x coordinate
    public void SetX(int x)
    {
        this.x = x; 
    }
    
    // set y coordinate
    public void SetY(int y)
    {
        this.y = y; 
    }
    
    //Handles all collisions that are not game ending
    public boolean isWallCollision(int x, int y, ArrayList walls)
    {
        Wall tempWall;
        
       for (int i = 0; i < walls.size(); i++)
       {
           tempWall = (Wall) walls.get(i);
           
            if(tempWall.x == x && tempWall.y == y)
            {
                return true;
            }
       }
       return false;
    }
    
    //Collision detection for home square at 0 gems left
    public int isGemCollision(int x, int y, ArrayList gems)
    {
        Gem tempGem;
        int number;
        
        for (int i = 0; i < gems.size(); i++)
        {
            tempGem = (Gem) gems.get(i);

             if(tempGem.x == x && tempGem.y == y)
             {
                 number = i;
                 return number;
             }
        }
     
        //No collision
        return -1;
    }
    
    public boolean isHomeCollision(int x, int y, Wall home)
    {   
       
        if((home.x == x && home.y == y))
        {
           return true;
        }
        
        return false;
    }
}
