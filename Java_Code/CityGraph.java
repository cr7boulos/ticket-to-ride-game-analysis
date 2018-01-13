import java.util.LinkedList;

public class CityGraph{
  public Vertex[] cities;
  public Object[] adjacencyList; // actually contains LinkedList<Vertex>[]
  
  public CityGraph(int numOfCities){
    cities = new Vertex[numOfCities];
    adjacencyList = new Object[numOfCities];
  }
}