package com.thoennes.checkers;

import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by Alex on 12/28/16.
 *
 * Tile Class
 */

public class Tile
{
    public ArrayList<Tile> Neighbors = new ArrayList<>();

    // these are the four points that are used to draw the tile
    private float left, top, right, bottom;

    // the id is the index in the array
    private int id;

    // color of the tile (black or red)
    private Paint color;

    private float [] center;

    /**
     * Constructor for the game Tiles
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public Tile(float left, float top, float right, float bottom, int id, Paint color) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.id = id;
        this.color = color;
    }

    public Tile(float left, float top, float right, float bottom, float [] center, Paint color) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.center = center;
        this.color = color;
    }

    @Override
    public boolean equals(Object obj)
    {
        super.equals(obj);

        // if the object is compared with itself
        return obj == this;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public Paint getColor()
    {
        return color;
    }

    /**
     * adds a neighbor to the
     * neighbor array
     *
     * @param tile
     */
    public void addNeighbor(Tile tile)
    {
        Neighbors.add(tile);
    }

    public ArrayList<Tile> getNeighbors()
    {
        return Neighbors;
    }

    public boolean hasPiece(ArrayList<Piece> pieces)
    {
        return getPiece(pieces) != null;
    }

    public Piece getPiece(ArrayList<Piece> pieces)
    {
        RectF r = new RectF(this.left, this.top, this.right, this.bottom);

        for (int i = 0; i < pieces.size(); i ++)
        {
            if (r.contains(pieces.get(i).getX(), pieces.get(i).getY()))
            {
                return pieces.get(i);
            }
        }

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
        for (int i = 0; i < getNeighbors().size(); i ++)
        {
            if (end.equals(getNeighbors().get(i)))//end.getID() == getNeighbors().get(i).getID())
            {
                return true;
            }
        }

        return false;
    }

    public boolean isEmpty(ArrayList<Piece> player, ArrayList<Piece> opponent)
    {
        return !this.hasPiece(player) && !this.hasPiece(opponent);
    }

    public int getID()
    {
        return this.id;
    }

    public void setID(int id)
    {
        this.id = id;
    }
}
