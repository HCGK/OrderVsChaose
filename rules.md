#Order Vs Chaos

A game for 2 players   

##The Rules

###Set up

we have a 6 by 6 grid of tiles   
we have 2 player   
we have a source of red and blue tokens (e.g. we have a pile of 36 of each)   

independently we map the players to the pairs {first player, second player}, {Order, Chaos}   

Play is alternate starting with the fist player   

###Turn

The player places a red or blue token on a tile that dose not have a token on   
That tile is then said to have a token of the colour on for the rest of the game   
Note: the player chose the colour (from {red, blue}) and the tile (from the tiles that did not have a token on)   

###Victory

Order wins if (and when) there is a line*(h,v,d) of 5 tiles who each have a token of the same colour on them   
Chaos wins if order is prevented from wining*   
We assert that the winer will be clear at or befor the end of the 38th turn (18 each)

###Victory more formal

A neighbour operator is a function that takes a tile and outputs a different tile or null. We consider:   
The horizontal neighbour operator: (x,y) maps to (x+1,y) [or null if that would leave the grid]   
The vertical neighbour operator: (x,y) maps to (x,y+1) [or null if that would leave the grid]   
The diagonal neighbour operator: (x,y) maps to (x+1,y+1) [or null if that would leave the grid]   
The reverse_diagonal neighbour operator: (x,y) maps to (x+1,y-1) [or null if that would leave the grid]   

The Line operator extends this to produce a list/set of tiles or null is a follows:   
Take a neighbour operator O, a number N and a tile T   
Produce a list (array) of length N set the zeroth item as T   
set each other item as the neighbour of it predecessor   
if at any point null is encountered return null other wise return the list of N (non null) tiles   

A line a non null output of the line operator   
Order wins if and when there is a non-null output L of the line operator given one of the 4 considered neighbour, 5,   
and a tile, such that all the tiles in L have a token of the same colour on them   

Chaos shall be said to win when each line of 5 has both a red and blue token on one of its members   
