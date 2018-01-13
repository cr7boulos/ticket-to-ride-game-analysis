public class Vertex{
  public int id;
  public String cityName;
  public String color; 
  public int coinCost;
  public int carCost;
  public Vertex parent;
  
  
  public Vertex(){
    
  }
  
  public Vertex(int id){
    this.id = id;
  }
  
  public Vertex(int id, String cityName){
    this.id = id;
    this.cityName = cityName;
  }
  
  public Vertex(int id, int coinCost, int carCost, Vertex parent){
    this.id = id;
    
    this.color = "";
    this.coinCost = coinCost;
    this.carCost = carCost;
    this.parent = parent;
  }
  
  @Override
  public String toString(){
    return "Vertex id: " + this.id +";Vertex car cost: " + this.carCost +  "; Vertex parent id: " + (this.parent != null ? this.parent.id : " null");
  }
}
