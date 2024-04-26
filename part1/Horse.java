
public class Horse
{
    private String name;
    private char Icon;
    private int distanceTravelled;
    private boolean fallen;
    private double confidenceRating;
      
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        setSymbol(horseSymbol);
        this.name = horseName;
        setConfidence(horseConfidence); 
    }
    
    public double getConfidence()
    {
        return Math.round(this.confidenceRating * 10.0) / 10.0;
    }
    
    public int getDistanceTravelled()
    {
        return this.distanceTravelled;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public char getSymbol()
    {
        return this.Icon;
    }
    
    public void goBackToStart()
{
    this.distanceTravelled=0;
}

public boolean hasFallen()
{
    return this.fallen;
}

public void moveForward()
{
    this.distanceTravelled++;
}

public void setConfidence(double newConfidence)
{
    this.confidenceRating=newConfidence;
    if(this.confidenceRating<0.0){
        this.confidenceRating=0.0;
    }
    else if(this.confidenceRating>1.0){
        this.confidenceRating=1.0;
    }
}

public void setSymbol(char newSymbol)
{
    this.Icon=newSymbol;
}

public void fall()
{
    this.fallen=true;
    this.confidenceRating-=0.1;
    if(this.confidenceRating<0){
        this.confidenceRating=0;
    }
}
    
}
