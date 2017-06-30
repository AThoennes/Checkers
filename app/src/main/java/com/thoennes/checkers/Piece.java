package com.thoennes.checkers;

import android.graphics.Paint;

/**
 * Created by Alex on 12/25/16.
 *
 * Piece Class
 */

class Piece
{
    // is this piece a king
    private boolean king = false;

    // coordinates (in float) of the
    // piece on the screen
    private float x;
    private float y;

    private Paint color;

    /**
     * Constructor for a basic piece
     *
     * @param x
     * @param y
     */
    public Piece(float x, float y, Paint color)
    {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * returns whether or not this piece is a king
     *
     * @return king
     */
    public boolean isKing()
    {
        return king;
    }

    /**
     * used to make this piece a king
     *
     * @param king
     */
    public void setKing(boolean king)
    {
        this.king = king;
    }

    public Paint getColor()
    {
        return color;
    }

    /**
     * returns the X coordinate of the piece
     *
     * @return
     */
    public float getX()
    {
        return x;
    }

    /**
     * returns the Y coordinate of the piece
     *
     * @return
     */
    public float getY()
    {
        return y;
    }

    /**
     * set the values for both X and Y
     *
     * @param x
     * @param y
     */
    public void setXY(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
}