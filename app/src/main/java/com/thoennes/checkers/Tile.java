package com.thoennes.checkers;

import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by Alex on 12/28/16.
 *
 * Tile Class
 *
 * A tile is defined as a space you are allowed to move
 * onto whether you are moving or jumping. Every tile
 * knows who its neighbors are as well as jump neighbors.
 * Each tile also contains a left, top, right, and bottom
 * coordinate which is used when determining where to draw
 * the tile and if it contains a piece.
 */

public class Tile
{
    // list of all the neighbors for this tile
    public ArrayList<Tile> neighbors = new ArrayList<>();

    // list of all jumps this tile can perform
    public ArrayList<Tile> jumps = new ArrayList<>();

    // these are the four points that are used to draw the tile
    private float left, top, right, bottom;

    // color of the tile (black or red)
    private Paint color;

    /**
     * Constructor for the game Tiles
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public Tile(float left, float top, float right, float bottom, Paint color) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.color = color;
    }

    @Override
    public boolean equals(Object obj)
    {
        super.equals(obj);

        // if the object is compared with itself
        return obj == this;
    }

    /**
     * Determines if this tile has a piece. This
     * is done by checking to see if getPiece is
     * null or not
     *
     * @param pieces
     * @return
     */
    public boolean hasPiece(ArrayList<Piece> pieces)
    {
        return getPiece(pieces) != null;
    }

    /**
     * Returns the piece that this tile holds
     *
     * @param pieces
     * @return
     */
    public Piece getPiece(ArrayList<Piece> pieces)
    {
        // create a temp rectF with this tiles coords
        RectF r = new RectF(this.left, this.top, this.right, this.bottom);

        // then go through and find  which piece resides on this tile
        for (int i = 0; i < pieces.size(); i ++)
        {
            if (r.contains(pieces.get(i).getX(), pieces.get(i).getY()))
            {
                // return that piece
                return pieces.get(i);
            }
        }

        // no piece was found
        return null;
    }

    /**
     * checks to see if end is a neighbor of start
     *
     * @param end
     * @return
     */
    public boolean isNeighbor(Tile end)
    {
        // go through all the available neighbors
        for (int i = 0; i < getNeighbors().size(); i ++)
        {
            // use the overridden equals method
            if (end.equals(getNeighbors().get(i)))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether this tile is empty (no piece of any kind) or not
     *
     * @param player
     * @param opponent
     * @return
     */
    public boolean isEmpty(ArrayList<Piece> player, ArrayList<Piece> opponent)
    {
        return !this.hasPiece(player) && !this.hasPiece(opponent);
    }

    /**
     * Returns the left coordinate of the tile
     *
     * @return left
     */
    public float getLeft() {
        return left;
    }

    /**
     * Returns the top coordinate of the tile
     *
     * @return top
     */
    public float getTop() {
        return top;
    }

    /**
     * Returns the right coordinate of the tile
     *
     * @return right
     */
    public float getRight() {
        return right;
    }

    /**
     * Returns the bottom coordinate of the tile
     *
     * @return bottom
     */
    public float getBottom() {
        return bottom;
    }

    /**
     * Returns the paint element associated with this tile.
     * This is used when invalidating the view to
     * redraw everything
     *
     * @return color
     */
    public Paint getColor()
    {
        return color;
    }

    /**
     * adds a neighbor to the neighbor array
     *
     * @param tile
     */
    public void addNeighbor(Tile tile)
    {
        neighbors.add(tile);
    }

    /**
     * Returns the neighbors of this tile
     *
     * @return neighbors
     */
    public ArrayList<Tile> getNeighbors()
    {
        return neighbors;
    }

    /**
     * Adds a neighbor this tile can jump to
     *
     * @param tile
     */
    public void addJump(Tile tile)
    {
        jumps.add(tile);
    }

    /**
     * Returns the array of performable jumps
     *
     * @return jumps
     */
    public ArrayList<Tile> getJumps()
    {
        return jumps;
    }
}
