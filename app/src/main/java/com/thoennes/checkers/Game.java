package com.thoennes.checkers;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Alex on 4/28/17.
 */
public class Game
{
    // Static Singleton instance of the Game engine
    private static Game instance = null;

    private Context context;

    // array list of all possible tiles you can move to
    static private ArrayList<Tile> tiles = new ArrayList<>();

    // invalid tiles are only used when drawing the board
    static private ArrayList<Tile> invalidTiles = new ArrayList<>();

    // array list of all the player pieces
    private ArrayList<Piece> playerPieces = new ArrayList<>();

    // array list of all the opponent pieces
    private ArrayList<Piece> opponentPiececs = new ArrayList<>();

    // this is the tile you want to move from
    private Tile start;

    // this is the tile you want to move to
    private Tile end;

    //private constructor to avoid client applications to use constructor
    private Game(Context context)
    {
        this.context = context;
    }

    /**
     * initialization method for the game
     *
     * @param context
     * @return
     */
    public static Game init(Context context)
    {
        if (instance == null)
        {
            instance = new Game(context);
        }

        return instance;
    }

    /**
     * method that is used to get access tot he instance of the game
     *
     * @return
     */
    public static Game getGameInstance()
    {
        return instance;
    }

    /**
     * used to add tiles to the tile array
     * as long as the tile array size does
     * not exceed 32
     *
     * @param t
     */
    public void addTile(Tile t)
    {
        if (tiles.size() < 32)
        {
            tiles.add(t);
        }
    }

    /**
     * Adds an invalid tile (one you can not move to)
     * to the array list
     *
     * @param t
     */
    public void addInvalidTile(Tile t)
    {
        if (invalidTiles.size() < 32)
        {
            invalidTiles.add(t);
        }
    }

    /**
     * used to add opponents to the array
     *
     * @param p
     */
    public void addOpponent(Piece p)
    {
        opponentPiececs.add(p);
    }

    /**
     * method that is used when adding player pieces
     * to the array list
     *
     * @param p
     */
    public void addPlayer(Piece p)
    {
        playerPieces.add(p);
    }

    /**
     * used to obtain access to the tiles array
     *
     * @return
     */
    public ArrayList<Tile> getTiles()
    {
        return tiles;
    }

    /**
     * returns the array list of the invalid tiles
     *
     * @return
     */
    public ArrayList<Tile> getInvalidTiles()
    {
        return invalidTiles;
    }

    /**
     * Returns the array list of the player's pieces
     *
     * @return
     */
    public ArrayList<Piece> getPlayerPieces()
    {
        return playerPieces;
    }

    /**
     * return the array list of the opposing pieces
     *
     * @return
     */
    public ArrayList<Piece> getOpponentPieces()
    {
        return opponentPiececs;
    }

    /**
     * method that returns the tile tapped on
     * if it is a valid tile
     *
     * @param x
     * @param y
     * @return
     */
    public Tile findTappedTile(float x, float y)
    {
        Tile t;
        for (int i = 0; i < tiles.size(); i ++)
        {
            t = tiles.get(i);
            RectF r = new RectF(t.getLeft(), t.getTop(), t.getRight(), t.getBottom());

            if (r.contains(x, y))
            {
                System.out.println("VALID TILE");
                return t;
            }
        }

        return null;
    }

    /**
     * This method checks to see if you first tapped a valid tile.
     * If you did, then it checks to see if that tile has a piece on it
     * and if it does then it saves that tile as your first then waits
     * for you to tap a tile without a piece to be saved as the second tile
     *
     * @param touchX
     * @param touchY
     */
    public void move(float touchX, float touchY)
    {
        Tile t = findTappedTile(touchX, touchY);

        if (t != null && t.hasPiece(playerPieces) && start == null)
        {
            start = t;
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
        else if (t != null && !t.hasPiece(playerPieces) && start != null)
        {
            end = t;
            System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYY");
            attemptMove();
            start = null;
            end = null;
        }
    }

    public void attemptMove()
    {
        float x = end.getLeft() + (MainActivity.size/2);
        float y = end.getTop()+ (MainActivity.size/2);

        // if end is a neighbor of start
        if (start.isNeighbor(end))
        {
            if (end.getID() > start.getID() && start.getPiece(playerPieces).isKing())
            {
                Log.d("MMMMMMMMMMMMMMMMMMMM", "MOVED");
                start.getPiece(playerPieces).setXY(x, y);
            }
            else if (end.getID() < start.getID())
            {
                Log.d("MMMMMMMMMMMMMMMMMMMM", "MOVED");
                start.getPiece(playerPieces).setXY(x, y);
            }
        }
    }
}