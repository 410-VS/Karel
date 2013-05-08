package karel;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Player extends Entity
{
    char direction; //karels icon ^ > < v
    
    private ArrayList Bag = new ArrayList();
    Gem tempGem;
    private int StepCounter;
    
    public Player(int x, int y) 
    {
        super(x, y);
        StepCounter = 0;
        direction ='^';

        URL loc = this.getClass().getResource("up.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }

    //updates karels position
    public void move(int x, int y) 
    {
        int newX = this.GetX() + x;
        int newY = this.GetY() + y;
        this.SetX(newX);
        this.SetY(newY);
    }
    
    public int getSteps()
    {
        return this.StepCounter;
    }
    
    public void addStep()
    {
        StepCounter++;
    }
    
    public void addGem(Gem gem)
    {
        gem.SetX(0);
        gem.SetY(0);
        this.Bag.add(gem);
    }
    
    public Gem removeGem()
    {
        tempGem = (Gem) Bag.get(0);
        this.Bag.remove(0);
        return tempGem;
    }
    public int getGemCount()
    {
        return this.Bag.size();
    }
    
    
    public char GetDirection()
    {
        return this.direction;
    }
    
    public void SetDirection(char newDirection)
    {
        this.direction = newDirection;
    }
    
    public void ChangeImage(String NewDirection)
    {   
        URL loc = this.getClass().getResource(NewDirection +".png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }
}