# GDHex
GDHeX is a hex tile utility set up that does most of the annoying hex grid math for you.  
This project is extremely heavily based on the work done by redblobgames, but with an aim to build on that to provide a more general hexTile utility for LibGDX developers.

### Features
It will be gaining features as they're requested.

If a feature is missing that makes sense to have, there are two options.  
You can either add it yourself and send a PR, or you can make a feature request in issues and I'll add it as soon as I can.

Currently implemented major features:
- Hexgrid maps in 4 basic shapes: Rectangles, Hexagons, Triangles, and Rhombuses
- Built in methods to get radiuses and rings in relation to a hextile.
- Automatic scaling of the grid for "stretched" hexagons (textures with square dimensions like 32x32 instead of mathematically perfect hexagons)
- Automatic zsorting and culling of hextiles for stress-free rendering. If you want to use the culled version of the grid call HexUtils.getOnScreen(), otherwise you can use getFullGrid().
- Pixel to Hextile conversion, feed your mouse's position into pixelToHex() and get back a placeholder for the tile at that position. You can also retrieve the actual tile at that position using pixelToGridHex().
- A* pathfinding (each tile has a weight from 0-100, you can change this behavior if you want).

### Importing into a project
Copy the 3 classes HexTile, FractionalHexTile, and HexUtils into your LibGDX project.

### Usage
You can create an instance of HexUtils and generate a map as follows:
```java
public void initializeHexUtils(){
  /*HexUtils.pointy and HexUtils.flat describe whether the hex is pointy or flat on top.
    The Vector2 passed second is the distance to our corners.
    The last Vector2 is the origin in world coordinates.
    If you are using a square hexagon tile that is stretched,
    you can specify your stretch direction (Horizontal/Vertical) after the origin.*/
  hexUtils = new HexUtils(HexUtils.pointy,new Vector2(32,32),new Vector2(0,0));
  
  /*Now we can use our instance to generate a triangular grid.
    This grid has a max width of 10 and points upward.
    We can mirror it on the y axis using the boolean flipy at the end.*/
  hexUtils.generateTriangularGrid(10,false);
}
```
