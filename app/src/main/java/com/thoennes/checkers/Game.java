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

    // array list of all the player pieces
    private ArrayList<Piece> playerPieces = new ArrayList<>();

    // array list of all the opponent pieces
    private ArrayList<Piece> opponentPiececs = new ArrayList<>();

    // array of the coords of the spot you are coming from
    private float [] prevCoords = new float [4];

    // array of the coords of the spot you want to go to
    private float [] newCoords = new float [4];

    private Piece activePiece;

    private Tile start;
    private Tile end;

    //private constructor to avoid client applications to use constructor
    private Game(Context context)
    {
        this.context = context;
    }

    public static Game init(Context context)
    {
        if (instance == null)
        {
            instance = new Game(context);
            Game.getGameInstance().prevCoords[0] = -1;
        }

        return instance;
    }

    public static Game getGameInstance()
    {
        return instance;
    }

    public void addTile(Tile t)
    {

        if (tiles.size() < 32)
        {
            tiles.add(t);
        }
    }

    public void addOpponent(Piece p)
    {
        opponentPiececs.add(p);
    }

    public void addPlayer(Piece p)
    {
        playerPieces.add(p);
    }

    public Tile getTile(int index)
    {
        return tiles.get(index);
    }

    public ArrayList<Tile> getTiles()
    {
        return tiles;
    }

    public void move(float touchX, float touchY)
    {
        // go through all the tiles
        for (int i = 0; i < tiles.size(); i ++)
        {
            // create a temp tile to reference
            // the current tile in the array list
            Tile t = tiles.get(i);

            // turn the current tile into a float rectangle
            RectF r = new RectF(t.getLeft(), t.getTop(), t.getRight(), t.getBottom());

            // if this is the tile you tapped
            if (r.contains(touchX, touchY))
            {
                // you then wan to check if a player piece resides on that tile
                for (int j = 0; j < playerPieces.size(); j++)
                {
                    // create a temp player piece
                    Piece p = playerPieces.get(j);

                    // if there is a piece on this tile and you haven't chosen a piece to move yet
                    if (r.contains(p.getX(), p.getY()) && activePiece == null)
                    {
                        activePiece = p;
                        /*prevCoords[0] = t.getLeft();   // left
                        prevCoords[1] = t.getTop();    // top
                        prevCoords[2] = t.getRight();  // right
                        prevCoords[3] = t.getBottom(); // bottom*/
                        start = t;

                        break;
                    }
                    else if (!r.contains(p.getX(), p.getY()) && activePiece != null)
                    {
                        /*newCoords[0] = t.getLeft();   // left
                        newCoords[1] = t.getTop();    // top
                        newCoords[2] = t.getRight();  // right
                        newCoords[3] = t.getBottom(); // bottom*/
                        end = t;

                        attemptMove();

                        activePiece = null;
                        start = null;
                        end = null;

                        break;
                    }
                }
            }
        }
    }

    public void attemptMove()
    {
        // N O T      W O R K I N G!!!!!!
        for (int i = 0; i < start.getNeighbors().size(); i ++)
        {
            // if valid neighbor
            if (end.getLeft() == start.getNeighbors().get(i).getLeft() &&
                    end.getTop() == start.getNeighbors().get(i).getTop() &&
                    end.getRight() == start.getNeighbors().get(i).getRight() &&
                    end.getBottom() == start.getNeighbors().get(i).getBottom())
            {
                Log.d("XXXXXXXXXXXXXXXXXXXXXXX", "POOP");
            }
        }
    }

    public boolean isNeighbor(Tile t)
    {
        float x = end.getLeft() - start.getLeft();
        float y = end.getBottom() - start.getTop();

        if (x < 0)
        {
            x *= -1;
        }

        if (y < 0)
        {
            y *= -1;
        }

        return false;
    }
}