package com.thoennes.checkers;

/**
 * Created by Alex on 12/25/16.
 *
 * Piece Class
 */

class Piece
{
    // is this a player or opponent
    private String type;

    // is this piece a king
    private boolean king = false;

    // coordinates (in float) of the
    // piece on the screen
    private float x;
    private float y;

    /**
     * Constructor for a basic piece
     *
     * @param x
     * @param y
     */
    public Piece(float x, float y)
    {
        this.x = x;
        this.y = y;
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

    /**
     * returns the type of this piece (player or opponent)
     *
     * @return type
     */
    public String getType()
    {
        return type;
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
     * changes the X coordinate of the piece
     *
     * @param x
     */
    public void setX(float x)
    {
        this.x = x;
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
     * changes the Y coordinate of the piece
     *
     * @param y
     */
    public void setY(float y)
    {
        this.y = y;
    }
}