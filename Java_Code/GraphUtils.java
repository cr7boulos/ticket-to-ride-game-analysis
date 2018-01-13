import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Comparator;
import java.util.PriorityQueue;

public class GraphUtils{
  public static void BFS(CityGraph g, Vertex v){
    for(int i = 0; i < g.cities.length; i++){
      if(v.id != i){
        g.cities[i].color = "white";
        g.cities[i].coinCost = Integer.MAX_VALUE;
        g.cities[i].parent = null;
      }
      v.color = "gray";
      v.coinCost = 0;
      v.parent = null;
      LinkedList<Vertex> queue = new LinkedList<>();
      queue.add(v);
      while( queue.size() > 0){
        Vertex current = queue.remove();
        LinkedList<Vertex> adj = (LinkedList<Vertex>)g.adjacencyList[current.id];
        for(Vertex vtx : adj) {
          if(vtx.color.equals("white")){
            vtx.color = "gray";
            vtx.coinCost = current.coinCost + 1;
            vtx.parent = current;
            queue.add(vtx);
          }
        }
        current.color = "black";
      }
    }
  }
  
  // helper methods
  private static void initSingleSource(CityGraph g, Vertex src){
    for(Vertex v: g.cities){
      v.carCost = Integer.MAX_VALUE;
      v.coinCost = Integer.MAX_VALUE;
      v.parent = null;
    }
    src.coinCost = 0;
    src.carCost = 0;
  }
  
  private static void relaxCoin(Vertex child, Vertex parent, HashMap<String,Integer> costMap){
    if(child.coinCost > parent.coinCost + costMap.get(child.id + ":" + parent.id)){
      child.coinCost = parent.coinCost + costMap.get(child.id + ":" + parent.id);
      child.parent = parent;
    }
  }
  
  private static void relaxCar(Vertex child, Vertex parent, HashMap<String,Integer> costMap, PriorityQueue<Vertex> pq){
    if(child.carCost > parent.carCost + costMap.get(child.id + ":" + parent.id)){
      child.carCost = parent.carCost + costMap.get(child.id + ":" + parent.id);
      child.parent = parent;
      // force resort of priority queue
      // see this SO post for more details:
      // https://stackoverflow.com/questions/6952660/java-priority-queue-reordering-when-editing-elements
      pq.remove(child);
      pq.add(child);
    }
  }
  
  // Computes route costs in terms of cars
  public static void DijkstraCar(CityGraph g, HashMap<String, Integer> costMap, Vertex src){
    initSingleSource(g,src);
    HashSet vertexSet = new HashSet<Vertex>(45);

    PriorityQueue que = new PriorityQueue<Vertex>(45, (v1, v2 ) -> { 
      return Integer.compare(v1.carCost, v2.carCost);
    });
    for(Vertex v : g.cities){
     que.add(v);
    }
    
    while(que.size() > 0){
     Vertex vtx = (Vertex)que.poll(); // gets vertex with lowest cost
     vertexSet.add(vtx);
     LinkedList<Vertex> list = (LinkedList<Vertex>)g.adjacencyList[vtx.id];
      for(Vertex v : list){
       relaxCar(v, vtx, costMap, que);
      }
    }
  }
  
  
  // Note: BFS or Dijkstra's method needs to be called on the CityGraph param, g, 
  // before calling this method to ensure the 
  // shortest-path pointers have been set up correctly
  public static void printPath(CityGraph g, Vertex orig, Vertex dest, LinkedList<String> shortRoutes ){
    System.out.println("Origin: " + orig.cityName + " - Dest: " + dest.cityName);
    printPathHelper(g, orig, dest, shortRoutes);
    System.out.println("\n--------------");
  }
  private static void printPathHelper(CityGraph g, Vertex orig, Vertex dest, LinkedList<String> shortRoutes){
    if( orig.id == dest.id){
      System.out.print(orig.cityName + " ");
      shortRoutes.add(orig.cityName);
    } else if(dest.parent == null){
      System.out.println("No path from origin to destination exists");
    } else{
      printPathHelper(g, orig, dest.parent, shortRoutes);
      System.out.print(dest.cityName + " ");
      shortRoutes.add(dest.cityName);
    }
  }
  
}