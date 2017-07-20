package com.thoennes.checkers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;

import java.util.ArrayList;

import static com.thoennes.checkers.MainActivity.size;

/**
 * Created by Alex on 4/28/17.
 *
 * Game Class
 */
public class Game
{
    // Static Singleton instance of the Game engine
    private static Game instance = null;

    private Context context;

    // array list of all possible tiles you can move to
    private ArrayList<Tile> tiles = new ArrayList<>();

    // invalid tiles are only used when drawing the board
    private ArrayList<Tile> invalidTiles = new ArrayList<>();

    // array list of all the player pieces
    private ArrayList<Piece> playerPieces = new ArrayList<>();

    // array list of all the opponent pieces
    private ArrayList<Piece> opponentPieces = new ArrayList<>();

    // this is the tile you want to move from
    private Tile start;

    // this is the tile you want to move to
    private Tile end;

    private static Paint black = new Paint();

    private static Paint red = new Paint();

    // set the color of the opponent painter
    private static Paint opponent = new Paint();

    // set the color of the player painter
    private static Paint playerColor = new Paint();

    // the opponent in the game
    private AI ai;

    private Player player;

    // media player used to play a clicking sound when you move
    static MediaPlayer mp;

    //private constructor to avoid client applications to use constructor
    public Game(Context context)
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
            black.setColor(Color.BLACK);
            red.setColor(Color.RED);
            opponent.setColor(Color.rgb(0, 0, 500));
            playerColor.setColor(Color.rgb(255, 165, 0));
            mp = MediaPlayer.create(context, R.raw.move);
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
                return t;
            }
        }

        return null;
    }

    /**
     * Called when you tap the screen. This handles moving
     * the player and the opponent
     *
     * @param touchX
     * @param touchY
     */
    public void move(float touchX, float touchY)
    {
        player.attemptMove(touchX, touchY);
    }

    /**
     * method that creates all the tiles and pieces
     * that will be used in the game
     */
    public void generateAssets()
    {
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                // bounds of the tiles
                float left = row * size; // left side of the tile
                float top = col * size; // top of the tile
                float right = left + size; // right side of the tile
                float bottom = top + size; // bottom of the tile

                if ((row + col) % 2 == 0)
                {
                    float circleX = left + size / 2;
                    float circleY = top + size / 2;

                    // add the tile to the list of playable tiles (colored black)
                    addTile(new Tile(left, top, right, bottom, black));

                    // top 3 rows are where the opponent starts
                    if (col < 3)
                    {
                        // add the opponent piece to the list
                        addOpponent(new Piece(circleX, circleY, opponent));
                    } // bottom 3 rows are where the player starts
                    else if (col > 4)
                    {
                        // add the player piece to the list
                        addPlayer(new Piece(circleX, circleY, playerColor));
                    }
                }
                else
                {
                    // invalid tiles that are just used for drawing
                    addInvalidTile(new Tile(left, top, right, bottom, red));
                }
            }
        }

        // create a new opponent
        ai = new AI(opponentPieces);

        // create a new player
        player = new Player(playerPieces);
    }

    /**
     * This method assigns the proper neighbors
     * to each tile so that you can move legally
     */
    public void asignNeighbors()
    {
        // boolean to tell which number set to use
        /*boolean shiftedDown = false;

        tiles.get(0).addNeighbor(tiles.get(3));

        for (int i = 1; i < tiles.size(); i ++)
        {
            if (!shiftedDown)
            {
                if (i % 4 == 0)
                {
                    shiftedDown = true;

                    if (i == 8 || i == 16 || i == 24)
                    {
                        tiles.get(i).addNeighbor(tiles.get(i - 4));
                        tiles.get(i).addNeighbor(tiles.get(i + 4));
                    }

                    continue;
                }

                if (i - 5 >= 0)
                {
                    tiles.get(i).addNeighbor(tiles.get(i - 5));
                }

                if (i - 4 >= 0)
                {
                    tiles.get(i).addNeighbor(tiles.get(i - 4));
                }

                if (i + 3 < tiles.size())
                {
                    tiles.get(i).addNeighbor(tiles.get(i + 3));
                }

                if (i + 4 < tiles.size())
                {
                    tiles.get(i).addNeighbor(tiles.get(i + 4));
                }
            }
            else if (shiftedDown)
            {
                if (i % 4 == 0)
                {
                    shiftedDown = false;
                }

                if (i - 4 >= 0)
                {
                    tiles.get(i).addNeighbor(tiles.get(i - 4));
                }

                if (i - 3 >= 0)
                {
                    tiles.get(i).addNeighbor(tiles.get(i-3));
                }

                if (i + 4 < tiles.size())
                {
                    tiles.get(i).addNeighbor(tiles.get(i + 4));
                }

                if (i + 5 < tiles.size())
                {
                    tiles.get(i).addNeighbor(tiles.get(i + 5));
                }
            }
            System.out.println(shiftedDown);
        }*/

        getTiles().get(0).addNeighbor(getTiles().get(4));

        getTiles().get(1).addNeighbor(getTiles().get(4));
        getTiles().get(1).addNeighbor(getTiles().get(5));

        getTiles().get(2).addNeighbor(getTiles().get(5));
        getTiles().get(2).addNeighbor(getTiles().get(6));

        getTiles().get(3).addNeighbor(getTiles().get(6));
        getTiles().get(3).addNeighbor(getTiles().get(7));

        getTiles().get(4).addNeighbor(getTiles().get(0));
        getTiles().get(4).addNeighbor(getTiles().get(1));
        getTiles().get(4).addNeighbor(getTiles().get(8));
        getTiles().get(4).addNeighbor(getTiles().get(9));

        getTiles().get(5).addNeighbor(getTiles().get(1));
        getTiles().get(5).addNeighbor(getTiles().get(2));
        getTiles().get(5).addNeighbor(getTiles().get(9));
        getTiles().get(5).addNeighbor(getTiles().get(10));

        getTiles().get(6).addNeighbor(getTiles().get(2));
        getTiles().get(6).addNeighbor(getTiles().get(3));
        getTiles().get(6).addNeighbor(getTiles().get(1));
        getTiles().get(6).addNeighbor(getTiles().get(11));

        getTiles().get(7).addNeighbor(getTiles().get(3));
        getTiles().get(7).addNeighbor(getTiles().get(11));

        getTiles().get(8).addNeighbor(getTiles().get(4));
        getTiles().get(8).addNeighbor(getTiles().get(12));

        getTiles().get(9).addNeighbor(getTiles().get(4));
        getTiles().get(9).addNeighbor(getTiles().get(5));
        getTiles().get(9).addNeighbor(getTiles().get(12));
        getTiles().get(9).addNeighbor(getTiles().get(13));

        getTiles().get(10).addNeighbor(getTiles().get(5));
        getTiles().get(10).addNeighbor(getTiles().get(6));
        getTiles().get(10).addNeighbor(getTiles().get(13));
        getTiles().get(10).addNeighbor(getTiles().get(14));

        getTiles().get(11).addNeighbor(getTiles().get(6));
        getTiles().get(11).addNeighbor(getTiles().get(7));
        getTiles().get(11).addNeighbor(getTiles().get(11));
        getTiles().get(11).addNeighbor(getTiles().get(15));

        getTiles().get(12).addNeighbor(getTiles().get(8));
        getTiles().get(12).addNeighbor(getTiles().get(9));
        getTiles().get(12).addNeighbor(getTiles().get(16));
        getTiles().get(12).addNeighbor(getTiles().get(17));

        getTiles().get(13).addNeighbor(getTiles().get(9));
        getTiles().get(13).addNeighbor(getTiles().get(10));
        getTiles().get(13).addNeighbor(getTiles().get(17));
        getTiles().get(13).addNeighbor(getTiles().get(18));

        getTiles().get(14).addNeighbor(getTiles().get(10));
        getTiles().get(14).addNeighbor(getTiles().get(11));
        getTiles().get(14).addNeighbor(getTiles().get(18));
        getTiles().get(14).addNeighbor(getTiles().get(19));

        getTiles().get(15).addNeighbor(getTiles().get(11));
        getTiles().get(15).addNeighbor(getTiles().get(19));

        getTiles().get(16).addNeighbor(getTiles().get(12));
        getTiles().get(16).addNeighbor(getTiles().get(20));

        getTiles().get(17).addNeighbor(getTiles().get(12));
        getTiles().get(17).addNeighbor(getTiles().get(13));
        getTiles().get(17).addNeighbor(getTiles().get(20));
        getTiles().get(17).addNeighbor(getTiles().get(21));

        getTiles().get(18).addNeighbor(getTiles().get(13));
        getTiles().get(18).addNeighbor(getTiles().get(14));
        getTiles().get(18).addNeighbor(getTiles().get(21));
        getTiles().get(18).addNeighbor(getTiles().get(22));

        getTiles().get(19).addNeighbor(getTiles().get(14));
        getTiles().get(19).addNeighbor(getTiles().get(15));
        getTiles().get(19).addNeighbor(getTiles().get(22));
        getTiles().get(19).addNeighbor(getTiles().get(23));

        getTiles().get(20).addNeighbor(getTiles().get(16));
        getTiles().get(20).addNeighbor(getTiles().get(17));
        getTiles().get(20).addNeighbor(getTiles().get(24));
        getTiles().get(20).addNeighbor(getTiles().get(25));

        getTiles().get(21).addNeighbor(getTiles().get(17));
        getTiles().get(21).addNeighbor(getTiles().get(18));
        getTiles().get(21).addNeighbor(getTiles().get(25));
        getTiles().get(21).addNeighbor(getTiles().get(26));

        getTiles().get(22).addNeighbor(getTiles().get(18));
        getTiles().get(22).addNeighbor(getTiles().get(19));
        getTiles().get(22).addNeighbor(getTiles().get(26));
        getTiles().get(22).addNeighbor(getTiles().get(27));

        getTiles().get(23).addNeighbor(getTiles().get(19));
        getTiles().get(23).addNeighbor(getTiles().get(27));

        getTiles().get(24).addNeighbor(getTiles().get(20));
        getTiles().get(24).addNeighbor(getTiles().get(28));

        getTiles().get(25).addNeighbor(getTiles().get(20));
        getTiles().get(25).addNeighbor(getTiles().get(21));
        getTiles().get(25).addNeighbor(getTiles().get(28));
        getTiles().get(25).addNeighbor(getTiles().get(29));

        getTiles().get(26).addNeighbor(getTiles().get(21));
        getTiles().get(26).addNeighbor(getTiles().get(22));
        getTiles().get(26).addNeighbor(getTiles().get(29));
        getTiles().get(26).addNeighbor(getTiles().get(30));

        getTiles().get(27).addNeighbor(getTiles().get(22));
        getTiles().get(27).addNeighbor(getTiles().get(23));
        getTiles().get(27).addNeighbor(getTiles().get(30));
        getTiles().get(27).addNeighbor(getTiles().get(31));

        getTiles().get(28).addNeighbor(getTiles().get(24));
        getTiles().get(28).addNeighbor(getTiles().get(25));

        getTiles().get(29).addNeighbor(getTiles().get(25));
        getTiles().get(29).addNeighbor(getTiles().get(26));

        getTiles().get(30).addNeighbor(getTiles().get(26));
        getTiles().get(30).addNeighbor(getTiles().get(27));

        getTiles().get(31).addNeighbor(getTiles().get(27));
    }

    /**
     * Assigns the jumpable tiles for every tile in the game
     *
     */
    public void assignJumps()
    {
        for (int i = 0; i < tiles.size(); i ++)
        {
            if (i - 9 >= 0)
            {
                tiles.get(i).addJump(tiles.get(i - 9));
            }

            if (i - 7 >= 0)
            {
                tiles.get(i).addJump(tiles.get(i - 7));
            }

            if (i + 7 < tiles.size())
            {
                tiles.get(i).addJump(tiles.get(i + 7));
            }

            if (i + 9 < tiles.size())
            {
                tiles.get(i).addJump(tiles.get(i + 9));
            }
        }
    }

    /**
     * used to add opponents to the array
     *
     * @param p
     */
    public void addOpponent(Piece p)
    {
        opponentPieces.add(p);
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
        return opponentPieces;
    }

    /**
     * Returns the opponent of the player
     *
     * @return
     */
    public AI getAI()
    {
        return ai;
    }

    /**
     * Returns the player object in this game
     *
     * @return
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Returns the black paint element
     *
     * @return
     */
    public static Paint getBlack()
    {
        return black;
    }

    /**
     * returns the red paint element
     *
     * @return
     */
    public static Paint getRed()
    {
        return red;
    }

    /**
     * Returns the opponent (blue)
     * paint element
     *
     * @return
     */
    public static Paint getOpponent()
    {
        return opponent;
    }

    /**
     * Returns the player (orange)
     * paint element
     *
     * @return
     */
    public static Paint getPlayerColor()
    {
        return playerColor;
    }
}