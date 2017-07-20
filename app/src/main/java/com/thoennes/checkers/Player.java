package com.thoennes.checkers;

import java.util.ArrayList;

import static com.thoennes.checkers.MainActivity.size;

/**
 * Created by Alex on 7/20/17.
 *
 * Player Class
 */

public class Player
{
    private ArrayList<Piece> pieces;

    private Tile start;
    private Tile end;

    public Player(ArrayList<Piece> pieces)
    {
        this.pieces = new ArrayList<>(pieces);
    }

    public void adjustPieces()
    {}

    /**
     * This method checks to see if you first tapped a valid tile.
     * If you did, then it checks to see if that tile has a piece on it
     * and if it does then it saves that tile as your first then waits
     * for you to tap a tile without a piece to be saved as the second tile
     *
     * @param touchX
     * @param touchY
     */
    public void attemptMove(float touchX, float touchY)
    {
        Game game = Game.getGameInstance();

        Tile t = game.findTappedTile(touchX, touchY);

        if (t != null && t.hasPiece(game.getPlayerPieces()) && start == null)
        {
            start = t;
            game.getMp().start();
        }
        else if (t != null && t.isEmpty(game.getPlayerPieces(), game.getOpponentPieces()) && start != null)
        {
            end = t;
            move();
            start = null;
            end = null;
        }
        else // if you tapped an invalid move space
        {
            end = null;
            start = null;
        }
    }

    public void move()
    {
        Game game = Game.getGameInstance();
        float x = end.getLeft() + (size/2);
        float y = end.getTop()+ (size/2);

        // if end is a neighbor of start
        if (start.isNeighbor(end))
        {
            // moving a king
            if (start.getPiece(pieces).isKing() && canMoveKing(start, end))
            {
                start.getPiece(pieces).setXY(x, y);

                // play the wooden click sound
                game.getMp().start();

                game.getAI().attemptMove();
            } // moving a non-king
            else if (canMoveNonKing(start, end))
            {
                start.getPiece(pieces).setXY(x, y);

                // play the wooden click sound
                game.getMp().start();

                game.getAI().attemptMove();
            }
        }
    }


    public void attemptJump(float touchX, float touchY)
    {
        Game game = Game.getGameInstance();

        Tile t = game.findTappedTile(touchX, touchY);
    }

    public boolean canMoveNonKing(Tile start, Tile end)
    {
        return (end.getLeft() < start.getLeft() && end.getTop() < start.getTop()) ||
                (end.getRight() > start.getRight() && end.getTop() < start.getTop());

    }

    public boolean canMoveKing(Tile start, Tile end)
    {
        return (end.getLeft() < start.getLeft() && end.getBottom() > start.getBottom()) ||
                (end.getRight() > start.getRight() && end.getBottom() > start.getBottom());
    }
}
