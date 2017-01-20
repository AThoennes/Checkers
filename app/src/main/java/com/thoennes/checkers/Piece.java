package com.thoennes.checkers;

import android.widget.ImageView;

/**
 * Created by Alex on 12/25/16.
 *
 * Piece Class
 */

class Piece
{
    // image of the piece
    private ImageView image;

    // is this a player or opponent
    private String type;

    // is this piece a king
    private boolean king = false;

    /**
     * default constructor for a piece
     *
     * @param image
     * @param type
     * @param king
     * @param start
     */
    public Piece(ImageView image, String type, boolean king, Tile start)
    {
        this.image = image;
        this.type = type;
        this.king = king;

        // draw the tiles at their
        // starting positions
        draw(start);
    }

    /**
     * This method draws the piece at the desired tile
     *
     * @param a
     */
    public void draw(Tile a)
    {
        image.animate().translationX(a.getX());
        image.animate().translationY(a.getY());
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
     * returns the image of this piece
     *
     * @return image
     */
    public ImageView getImage()
    {
        return image;
    }
}