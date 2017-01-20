package com.thoennes.checkers;

import android.widget.ImageView;

/**
 * Created by Alex on 12/28/16.
 *
 * Tile Class
 */

public class Tile
{
    // image of the tile
    private ImageView image;

    // row number of the tile
    private int row;

    // x and y coordinates (in pixels)
    // of the tile on the screen
    private float x, y;

    //used to reference the current piece on the tile
    private Piece piece;

    // name of the tile
    // ex. A1 or H8 or C7
    private String name;

    // boolean used to determine if the tile is taken
    // by a piece or not
    private boolean taken;

    // array of tiles that are next to this tile (normal moves)
    private Tile [] neighbors;

    // array of tiles this tile can jump to (jumps)
    private Tile [] jumps;

    /**
     * Default constructor for a tile
     *
     * @param image
     * @param row
     * @param piece
     * @param name
     * @param taken
     * @param x
     * @param y
     * @param neighborsSize
     * @param jumpSize
     */
    public Tile(ImageView image, int row, Piece piece, String name, boolean taken, float x, float y, int neighborsSize, int jumpSize)
    {
        this.image = image;
        this.row = row;
        this.piece = piece;
        this.name = name;
        this.taken = taken;
        this.x = x;
        this.y = y;
        this.neighbors = new Tile [neighborsSize];
        this.jumps = new Tile [jumpSize];
    }

    /**
     * returns the image of the tile
     *
     * @return image
     */
    public ImageView getImage()
    {
        return image;
    }

    /**
     * returns the row number the tile is in
     *
     * @return row
     */
    public int getRow()
    {
        return row;
    }

    /**
     * returns taken
     *
     * @return taken
     */
    public boolean hasPiece()
    {
        return taken;
    }

    /**
     * This method is used to set the
     * variable taken to true of false.
     *
     * @param taken
     */
    public void setTaken(boolean taken)
    {
        this.taken = taken;
    }

    /**
     * use this method to set the piece being moved
     * onto the tile you are moving to
     *
     * @param piece
     */
    public void setPiece(Piece piece)
    {
        this.piece = piece;
    }

    /**
     * This method should only be used when hasPiece() is true
     * otherwise you risk the possibility of an error. This method
     * returns the piece associated with this tile.
     *
     * @return piece
     */
    public Piece getPiece()
    {
        return piece;
    }

    /**
     * returns the string name of the tile.
     *
     * ex.  A6 or H1 or D3
     *
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * returns the y value of the tile on the screen
     *
     * @return y
     */
    public float getY()
    {
        return y;
    }

    /**
     * returns the x value of the tile on the screen
     *
     * @return x
     */
    public float getX()
    {
        return x;
    }

    /**
     * adds a neighbor at the specified index in the array
     *
     * @param location
     * @param tile
     */
    public void addNeighbor(int location, Tile tile)
    {
        neighbors[location] = tile;
    }

    /**
     * adds a tile you can jump to at the specified index
     *
     * @param location
     * @param tile
     */
    public void addJump(int location, Tile tile)
    {
        jumps[location] = tile;
    }

    /**
     * tests the connectivity of two tiles. You pass
     * in the tile you want to move TO and if it is
     * in the neighbor array of the tile you are moving
     * FROM then the two tiles connect.
     *
     * @param tile
     * @return result
     */
    public boolean connectsTo(Tile tile)
    {
        // for each tile in this tile's neighbor array
        for (Tile t : neighbors)
        {
            // use the name as the id because it is unique to each tile
            if (t.getName().equals(tile.getName()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * tests the connectivity of two tiles jump-wise. You pass
     * in the tile you want to start at and if the tile you want
     * to end on is in the array then this is a valid jump.
     *
     * @param tile
     * @return
     */
    public boolean jumpsTo(Tile tile)
    {
        // for each tile in this tile's neighbor array
        for (int i = 0; i < jumps.length; i ++)
        {
            // use the name as the id because it is unique to each tile
            if (jumps[i].getName().equals(tile.getName()))
            {
                return true;
            }
        }

        return false;
    }
}
