import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.io.FileNotFoundException;
public class GraphBuilder
{
  public static void main (String [] args)
  {
    final int NEDERLAND_CITY_COUNT = 30;
    final int EUROPE_CITY_COUNT = 47;
    
    HashMap routesInCars = new HashMap<String, Integer>(200);
    HashMap routesInCoins = new HashMap<String, Integer>(200);
    HashMap paths = new HashMap<String, LinkedList<String>>(100);
    HashMap pathDependencies = new HashMap<String, Integer>(100);
    
    CityGraph gameMap = null;
    
    String fileName = "";
    String routeFileName = "";
    
    Scanner inputStream = null;
    
    System.out.println("This program creates CSV files \n" + 
                                    "mapping the number of dependencies between \n" +
                                    "routes in various game boards of Ticket to Ride.\n" +
                                    "Enter the number corresponding to the map you \n" +
                                    "would like to generate a file for: \n" +
                                     "1. Ticket to Ride: Europe \n" +
                                     "2. Ticket to Ride: Nederlands");
    int choice = 0;
    boolean valid = false;
      do{
      try{
        Scanner keyboard = new Scanner(System.in);
        choice = Integer.parseInt(keyboard.next("[1-2]"));
        valid = true;
      }
      catch(NoSuchElementException e){
        System.out.println("Please enter a valid number.");
      }
    } while(!valid);
    
    if(choice == 1){
     fileName = "..\\Europe_Data_Files\\Europe_Game_Graph.csv";
     routeFileName = "..\\Europe_Data_Files\\Europe_Route_Cards.csv";
     gameMap = new CityGraph(EUROPE_CITY_COUNT);
    }
    
    else if(choice == 2){
     fileName = "..\\Nederland_Data_Files\\Nederland_Game_Graph.csv";
     routeFileName = "..\\Nederland_Data_Files\\Nederland_Route_Cards.csv";
     gameMap = new CityGraph(NEDERLAND_CITY_COUNT);
    }
    
    try
    {
      inputStream = new Scanner(new File(fileName));
    }
    catch (FileNotFoundException e)
    {
      System.out.println("Error reading the file " + fileName);
      System.exit(0);
    }
    
    if(inputStream.hasNextLine()){
      inputStream.nextLine(); // discard the headers in the CSV file
    }
    while(inputStream.hasNextLine()){
      String line = inputStream.nextLine();
      
      String [] contents = line.split(",");
      // index: 0 - Origin; 1 - originId; 2 - Destination; 3 - destinationId;
      // 4 - carCost; 5 - coinCost (if applicable)
      
      int originId = Integer.parseInt(contents[1]);
      int destId = Integer.parseInt(contents[3]);
      
      // set up the graph's array of verticies (cities)
      // Note: need to scan through the file and set
      // up the verticies before creating the adjacency list
      if(gameMap.cities[originId] == null){
        gameMap.cities[originId] = new Vertex(originId, contents[0]);
      }
    }
    try
    {
      inputStream = new Scanner(new File(fileName));
    }
    catch (FileNotFoundException e)
    {
      System.out.println("Error reading the file " + fileName);
      System.exit(0);
    }
    if(inputStream.hasNextLine()){
      inputStream.nextLine(); // discard the headers in the CSV file
    }
    // scan through the file again to set up the adjacency list
    while (inputStream.hasNextLine())
    {
      String line = inputStream.nextLine();
      String [] contents = line.split(",");
      // index: 0 - Origin; 1 - originId; 2 - Destination; 3 - destinationId;
      // 4 - carCost; 5 - coinCost
      
      int originId = Integer.parseInt(contents[1]);
      int destId = Integer.parseInt(contents[3]);
      
      // Build up the adjacency list
      if(gameMap.adjacencyList[originId] == null){
        gameMap.adjacencyList[originId] = new LinkedList<Vertex>();
        ((LinkedList<Vertex>)gameMap.adjacencyList[originId]).add(gameMap.cities[destId]);
      }
      else {
        ((LinkedList<Vertex>)gameMap.adjacencyList[originId]).add(gameMap.cities[destId]);
      }
      

      // set up weight functions
      routesInCars.put(contents[1] + ":" + contents[3], new Integer(contents[4]));
      //routesInCoins.put(contents[1] + ":" + contents[3], new Integer(contents[5]));
    }

    try
    {
      inputStream = new Scanner(new File(routeFileName));
    }
    catch (FileNotFoundException e)
    {
      System.out.println("Error reading the file: " + routeFileName);
      System.exit(0);
    }
    if(inputStream.hasNextLine()){
      inputStream.nextLine(); // discard the headers in the CSV file
    }

    while(inputStream.hasNextLine()){
      String [] contents = inputStream.nextLine().split(",");
      // index: 0 - Origin; 1 - originId; 2 - Destination; 3 - destinationId;
      // 4 - pointValue
      int originId = Integer.parseInt(contents[1]);
      int destId  = Integer.parseInt(contents[3]);
      paths.put(contents[0] + ":" + contents[2], new LinkedList<String>());
      GraphUtils.DijkstraCar(gameMap, routesInCars, gameMap.cities[originId]);
      
      GraphUtils.printPath(gameMap, 
                                          gameMap.cities[originId], 
                                          gameMap.cities[destId],
                                          (LinkedList<String>)paths.get(contents[0] + ":" + contents[2]));
      LinkedList<String> shortestPath = (LinkedList<String>)paths.get(contents[0] + ":" + contents[2]);
      for(int i = 0, j = 1; j < shortestPath.size(); i++, j++){
        String origin = shortestPath.get(i);
        String dest = shortestPath.get(j);
        if(!pathDependencies.containsKey(origin + ":" + dest) && 
           !pathDependencies.containsKey(dest + ":" + origin) ){
         pathDependencies.put(origin + ":" + dest, 1);
        }
        else if(pathDependencies.containsKey(origin + ":" + dest)){
          pathDependencies.put(origin + ":" + dest,
                  (Integer)pathDependencies.get(origin + ":" + dest) + 1);
        }
        else{
          // pathDependencies contains the key -> dest + ":" + origin
          pathDependencies.put(dest + ":" + origin,
                  (Integer)pathDependencies.get(dest + ":" + origin) + 1);
        }
      }
    }
    System.out.println(pathDependencies);
    System.out.println(pathDependencies.size());
    PrintWriter outputStream = null;
    try
    {
      outputStream = new PrintWriter(new File("..\\Europe_Data_Files\\Output\\dependencies.csv"));
    }
    catch(FileNotFoundException e)
    {
      System.out.println("Error opening the file: deps.csv");
      System.exit(0);
    }
    // create file headers
    outputStream.println("Origin,Destination,Dependencies");
    for(Map.Entry<String,Integer> map : (Set<Map.Entry<String,Integer>>)pathDependencies.entrySet()){
     String[] res = map.getKey().split(":");
     outputStream.print(res[0] + "," + res[1] + ",");
     outputStream.println(map.getValue());
    }
    
    // be sure to call the close method:
    // this deactivates the lock on the 
    // file and flushes--writes-- the output to the file
    outputStream.close();
  }
}