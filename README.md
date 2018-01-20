# ticket-to-ride-game-analysis
Analysis of optimal routes in various versions of the Ticket to Ride board game

## Algorithm Design
This code computes a measure for determining optimal tracks to claim
on various maps of Ticket to Ride (TTR). It works as follows: each board is
modelled as a graph. Then for each destination ticket, the shortest paths are
computed. For each track on the board a counter of the number of shortest paths that
depend on said track is stored. So if a given track is referenced by multiple
shortest paths, it is of more importance for claiming. The reasoning is if a player
focuses on claiming the most referenced routes, the chances of drawing additional
destination tickets that already lay on, or very near, the player's pre-existing network
are maximized--thus enabling the player to score points more quickly and perhaps
win the game.

## Program Output
Each map outputs a CSV file listing various tracks on a given board and 
the number of times it is referenced in a shortest path corresponding to a
destination ticket. Note: not all tracks on the board will be included
in the program output. Such tracks are not refernced in any shortest paths.

For anyone wishing to analyze an expansion of TTR not listed here,
simply follow the steps below:

The Java code works for any map of TTR with a minimal of tweaking. To add another map
to the code base, simply create two CSV files: one of all the invidiual tracks on the
board and a second with all the destination tickets--see one of the pre-existing files for 
an example of how to label the headers of the file. Also note: follow the existing directory structure
as the Java program expects files to be located in the same general manner. 

## Creating the Routes File
The Java program reads this file to populate a graph object in preparation of analysis. Arbitrarily
number all the cities on your map of TTR--these will uniquely identify each city and is required due
to how the graph object is implemented. Then for each city on the board label eaching adjacent edge 
(track) with in the needed data: see the CSV file for TTR: Europe as an example. Be aware that each
track will appear twice in your file. For example, if a track exists from London to Edinburgh this 
will be labeled as follows: 

Origin | originId | Destination | destinationId | carCost 
------ | ------ | ------ | ------ | ------  
London | 1 | Edinburgh | 0 | 4 
... | ... | ... | ... | ... 
Edinburgh | 0 | London| 1 | 4 
 
Pro tip: I recommend using Excel for this task so that you can take advantage of VLOOKUP to simply 
this tedious data entry step, then export your results as a CSV.

## Creating the Destination Tickets File
Fill in all the details for each of the map's destination tickets. See one of the pre-existing examples
for how this is done.

## Modify the Main Java Program
If you followed the directory structure conventions, see the GraphBuilder.java file; it contains entries
detailing what code needs to be added to make the program work for your map. This Java program will write 
out a CSV file with a listing of the most congested tracks on the board; however, you need to create a 
blank CSV file or the program will complain. For the maps currently included in the code base, follow
the file/directory structure exactly to minimize errors when creating the files for your map. That's it!
Compile and run GraphBuilder.java, and you should get a CSV file populated with a measure of track congestion.
Note: not all tracks on the board will be listed in the output file, and that is intended.
