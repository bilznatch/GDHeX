GDHeX is a hex tile utility set up that does most of the annoying hex grid math for you.
This project is extremely heavily based on the work done by redblobgames, but with an aim to build on that to provide a more general hexTile utility for LibGDX developers.

It will be gaining features as they're requested.
If a feature is missing that makes sense to have, you can add it yourself and send a PR, or you can make a feature request in issues and I'll add it as soon as I can.

To add to a project:
Copy the 3 classes HexTile, FractionalHexTile, and HexUtils into your LibGDX project.

To use:
Create an instance of HexUtils, and have it generate a grid using generateGrid(width,height).
Most of the other tools inside are self explanatory.
