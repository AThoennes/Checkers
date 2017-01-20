package com.thoennes.checkers;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
// this is a comment

import java.util.ArrayList;
import java.util.Random;

import static com.thoennes.checkers.R.id.btn;

public class MainActivity extends AppCompatActivity
{
    // all the pieces the player has
    Piece p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12;

    // all the pieces the opponent has
    Piece o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12;

    // used to reference the piece the player wants to move
    Piece currentPiece = null;

    // reset button
    Button resetButton;

    // all images for the player pieces
    ImageView playerImage1, playerImage2 ,playerImage3, playerImage4,
            playerImage5, playerImage6, playerImage7, playerImage8, playerImage9,
            playerImage10, playerImage11, playerImage12;

    // all images for the opponent pieces
    ImageView opponentImage1, opponentImage2 ,opponentImage3, opponentImage4,
            opponentImage5, opponentImage6, opponentImage7, opponentImage8, opponentImage9,
            opponentImage10, opponentImage11, opponentImage12;

    // all images for the tiles
    ImageView A1_Image, C1_Image, E1_Image, G1_Image,
            B2_Image, D2_Image, F2_Image, H2_Image,
            A3_Image, C3_Image, E3_Image, G3_Image,
            B4_Image, D4_Image, F4_Image, H4_Image,
            A5_Image, C5_Image, E5_Image, G5_Image,
            B6_Image, D6_Image, F6_Image, H6_Image,
            A7_Image, C7_Image, E7_Image, G7_Image,
            B8_Image, D8_Image, F8_Image, H8_Image;

    // all tiles that can be moved to
    Tile A1, C1, E1, G1,
            B2, D2, F2, H2,
            A3, C3, E3, G3,
            B4, D4, F4, H4,
            A5, C5, E5, G5,
            B6, D6, F6, H6,
            A7, C7, E7, G7,
            B8, D8, F8, H8;

    // used in determining where you are moving
    // from and where you are moving to
    Tile previousPos = null;
    Tile newPos = null;

    // used to determine if a player has chosen
    // which piece they would like to move
    boolean clicked = false;

    // array list of all tiles that is used to add a listener to each tile
    ArrayList<Tile> tiles = new ArrayList<>();

    ArrayList<Tile> opponentTiles = new ArrayList<>();

    // media player used to play a clicking sound when you move
    MediaPlayer mp;

    // used to reference the position you are jumping from
    String tempPrev = "";
    int tempX, tempY;

    // used to reference the position you are jumping to
    String tempNew = "";
    int newX, newY;

    TextView winText;

    final String PLAYER = "Player";
    final String OPPONENT = "Opponent";

    Tile AI_prev, AI_new;

    ArrayList<Tile> availableMoves = new ArrayList<>();

    // String matrix representation of the entire game board
    String [][] board = new String [][] {
            {"A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1"},
            {"A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2"},
            {"A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3"},
            {"A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4"},
            {"A5", "B5", "C5", "D5", "E5", "F5", "G5", "H5"},
            {"A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6"},
            {"A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7"},
            {"A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference button in the layout
        resetButton = (Button) findViewById(btn);

        mp = MediaPlayer.create(this, R.raw.move);

        winText = (TextView) findViewById(R.id.winText);

        setUp();

        // set up the click listeners for the tiles
        onClick();
    }

    /**
     * This method sets up all the
     * images and variable and arrays
     * that are needed for the game to
     * run properly
     */
    public void setUp()
    {
        // assign all images to objects
        assignImages();

        // set up the tiles
        initializeTiles();

        // set up the player pieces
        initializePlayers();

        // set up the opponent pieces
        initializeOpponents();

        // call this method again to finish setting up the tiles
        initializeTiles();

        // set the connectivity of each tile
        setNeighbors();

        // set the jump connectivity
        setJumps();

        // add all the tiles to an array list
        addToArray();

        addOpponents();
    }

    /**
     * This method handles the click listeners. To move, you click
     * the tile containing the piece you would like to move then
     * click the tile you would like to move to.
     */
    public void onClick()
    {
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                restart();
            }
        });

        // go through the tile array and an action listener to each image
        for (int i = 0; i < tiles.size(); i ++)
        {
            // declare a constant to be used in the inner class
            final Tile tile = tiles.get(i);

            tile.getImage().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // if this is the first tile clicked and it has a piece
                    if (!clicked && tile.hasPiece())
                    {
                        // set the current piece to the piece on the first tile
                        currentPiece = tile.getPiece();
                        mp.start();

                        // set the previous position to the first tile
                        previousPos = tile;

                        tempPrev = previousPos.getName();

                        // and then mark that you have chosen a tile
                        clicked = true;

                        Log.d("Tile clicked", "here");
                    } // if you have chosen a tile and the tile you just clicked has NO piece
                    else if (clicked && !tile.hasPiece())
                    {
                        // make the tile you just clicked the next tile
                        newPos = tile;
                        tempNew = newPos.getName();

                        // if the move you are trying to make is valid
                        if (previousPos.connectsTo(newPos))
                        {
                            handleMoves();
                        } // check for jump
                        else if (previousPos.jumpsTo(newPos) && jumpingEnemyPiece(previousPos, newPos))
                        {
                            handleJump();
                        }
                        else
                        {
                            nothingHappens();
                        }
                    }
                    else
                    {
                        nothingHappens();
                    }
                }
            });
        }
    }

    /**
     * Restarts the game by resetting all the variables
     */
    public void restart()
    {
        nothingHappens();

        // set up the tiles
        initializeTiles();

        // set up the player pieces
        initializePlayers();

        // set up the opponent pieces
        initializeOpponents();

        // call this method again to finish setting up the tiles
        initializeTiles();
        addOpponents();

        for (int i = 0; i < tiles.size(); i ++)
        {
            if (tiles.get(i).hasPiece())
            {
                tiles.get(i).getPiece().getImage().setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * this method handles moving player pieces. The
     * player moves by tapping the piece they want to
     * move then the tile they want to move to
     */
    public void handleMoves()
    {
        // check if the piece is a king
        if (currentPiece.isKing())
        {
            // if it is then draw the piece onto the new tile
            currentPiece.draw(newPos);

            // and prepare for the next move
            prepareForNextClick();

            addOpponents();
            AI();
            addOpponents();

        } // otherwise if a player is moving
        else if (currentPiece.getType().equals(PLAYER))
        {
            // make sure the player's non-king pieces are moving up
            if (newPos.getRow() < previousPos.getRow())
            {
                // then draw and reset variables used for moving
                currentPiece.draw(newPos);
                mp.start();

                prepareForNextClick();

                //SystemClock.sleep(5000);

                addOpponents();
                AI();
                addOpponents();
            }
            else
            {
                nothingHappens();
            }
        }

        win();
    }

    /**
     * This method handles the jumping action
     */
    public void handleJump()
    {
        // check if the piece is a king
        if (currentPiece.isKing())
        {
            // if it is then draw the piece onto the new tile
            currentPiece.draw(newPos);
            mp.start();

            removeJumpedPiece(previousPos, newPos);

            // and prepare for the next move
            prepareForNextClick();

            // allow the computer to move
            addOpponents();
            AI();
            addOpponents();

        } // otherwise if a non-king is jumoing
        else if (currentPiece.getType().equals(PLAYER))
        {
            // make sure the player's non-king pieces are moving up
            if (newPos.getRow() < previousPos.getRow())
            {
                // then draw and reset variables used for moving
                currentPiece.draw(newPos);
                mp.start();

                removeJumpedPiece(previousPos, newPos);
                prepareForNextClick();

                // allow the copmuter to move
                addOpponents();
                AI();
                addOpponents();
            }
            else
            {
                // used when a non king piece tries to go backwards
                nothingHappens();
            }
        }

        // check for a win after every move
        win();
    }

    /**
     * Finds the middle tile between the
     * two passed in tiles
     *
     * @param prev
     * @param next
     * @return
     */
    public Tile findMiddle(Tile prev, Tile next)
    {
        // string used to store the name found in the board array
        String temp = "";
        Tile middle = null;

        // x and y of the first tile
        int tempX = 0;
        int tempY = 0;

        // x and y of the second tile
        int newX = 0;
        int newY = 0;

        // find the coordinates of the old and new tile names
        for (int i = 0; i < board.length; i++)
        {

            for (int j = 0; j < board.length; j++)
            {
                if (board[i][j].equals(prev.getName()))
                {
                    tempX = i;
                    tempY = j;
                }

                if (board[i][j].equals(next.getName()))
                {
                    newX = i;
                    newY = j;
                }
            }
        }

        // bottom right
        if (tempX < newX && tempY < newY)
        {
            temp = board[tempX + 1][tempY + 1];
        } // top right
        else if (tempX < newX && tempY > newY)
        {
            temp = board[tempX + 1][tempY - 1];
        } // top left
        else if (tempX > newX && tempY > newY)
        {
            temp = board[tempX - 1][tempY - 1];
        } // bottom left
        else if (tempX > newX && tempY < newY)
        {
            temp = board[tempX - 1][tempY + 1];
        }

        for (int i = 0; i < tiles.size(); i++)
        {
            if (tiles.get(i).getName().equals(temp))
            {
                middle = tiles.get(i);
            }
        }

        return middle;
    }

    /**
     * This method removes the jumped piece
     * (the piece that was on the middle tile)
     *
     * @param prev
     * @param next
     */
    public void removeJumpedPiece(Tile prev, Tile next)
    {
        if (findMiddle(prev, next).hasPiece())
        {
            findMiddle(prev, next).getPiece().getImage().setVisibility(View.INVISIBLE);
            findMiddle(prev, next).setPiece(null);
            findMiddle(prev, next).setTaken(false);
        }
    }

    /**
     * Used by both the AI and the player. If this returns true,
     * then you are jumping an opponent piece
     *
     * @param prev
     * @param next
     * @return
     */
    public boolean jumpingEnemyPiece(Tile prev, Tile next)
    {
        return findMiddle(prev, next).hasPiece() && findMiddle(prev, next).getPiece().getType().equals(OPPONENT);

    }

    /**
     * This method resets all the values used for moving
     * pieces and telling the tiles which piece they
     * have,if any
     */
    public void prepareForNextClick()
    {
        // reset clicked
        clicked = false;

        // update which tiles have pieces
        previousPos.setTaken(false);
        newPos.setTaken(true);

        // tell the tiles which piece they
        // have if any
        previousPos.setPiece(null);
        newPos.setPiece(currentPiece);

        makeKing();

        // reset previous and new position
        previousPos = null;
        newPos = null;

        tempPrev = "";
        tempX = -1;
        tempY = -1;

        tempNew = "";
        newX = -1;
        newY = -1;

        currentPiece = null;

        addOpponents();
    }

    /**
     * This method is used to reset all the move
     * variables that are associated with choosing
     * a piece. This method is only used when a
     * non-king piece tries to go backwards
     */
    public void nothingHappens()
    {
        // reset clicked
        clicked = false;

        // reset previous and new position
        previousPos = null;
        newPos = null;

        //reset the current piece
        currentPiece = null;

        tempPrev = "";
        tempX = -1;
        tempY = -1;

        tempNew = "";
        newX = -1;
        newY = -1;

        AI_prev = null;
        AI_new = null;
    }

    /**
     * this method checks to see if a player piece can be made a king
     */
    public void makeKing()
    {
        if (newPos.getRow() == 1 && newPos.getPiece().getType().equals(PLAYER))
        {
            newPos.getPiece().getImage().setImageResource(R.drawable.player_king);
            newPos.getPiece().setKing(true);
        }
    }

    /**
     * This method checks to see if an AI piece can be make a king
     */
    public void makeAIKing()
    {
        if (AI_new.getRow() == 8 && AI_new.getPiece().getType().equals(OPPONENT))
        {
            AI_new.getPiece().getImage().setImageResource(R.drawable.opponent_king);
            AI_new.getPiece().setKing(true);
        }
    }

    /**
     * This method handles all the AI movement
     */
    public void AI()
    {
        // if the ai controls at least 1 piece
        if (opponentTiles.size() > 0)
        {
            // randomly choose from those pieces
            Random rand = new Random();
            int index = rand.nextInt(opponentTiles.size());
            AI_prev = opponentTiles.get(index);

            boolean jumped = false;

            // prioritize jumps over regular movement
            while (!canJump() && opponentTiles.size() > 0)
            {
                // if you can't jump then remove
                // the tile you tried to jump from
                opponentTiles.remove(index);

                if (opponentTiles.size() > 0)
                {
                    index = rand.nextInt(opponentTiles.size());
                    AI_prev = opponentTiles.get(index);

                    if (canJump())
                    {
                        jumped = true;
                    }
                }
            }

            // if the AI can jump then it jumps
            if (jumped)
            {
                jump();

                // reset all the AI movement variables
                resetAI();
            }
            else
            {
                // otherwise it performs the same actions but for regular movement
                addOpponents();

                while (!canMove() && opponentTiles.size() > 0)
                {
                    opponentTiles.remove(index);

                    if (opponentTiles.size() > 0)
                    {
                        index = rand.nextInt(opponentTiles.size());
                        AI_prev = opponentTiles.get(index);
                    }
                }

                move();

                // reset all the AI movement variables
                resetAI();
            }
        }
    }

    /**
     * Determines if the AI can jump
     * an opposing piece or not
     *
     * @return result
     */
    public boolean canJump()
    {
        // coordinates of tile in board
        int x = findPreviousX();
        int y = findPreviousY();

        // returned value
        boolean result = false;

        String topLeftName = findInBoard(x, y, 2, 2, "TopLeft");
        String topRightName = findInBoard(x, y, 2, 2, "TopRight");
        String bottomRightName = findInBoard(x, y, 2, 2, "BottomRight");
        String bottomLeftName = findInBoard(x, y, 2, 2, "BottomLeft");

        Tile topLeft = find(topLeftName);
        Tile topRight = find(topRightName);
        Tile bottomRight = find(bottomRightName);
        Tile bottomLeft = find(bottomLeftName);

        if (available(topLeft) && AI_prev.getPiece().isKing() && findMiddle(AI_prev, topLeft).hasPiece() &&
                findMiddle(AI_prev, topLeft).getPiece().getType().equals(PLAYER))
        {
            availableMoves.add(topLeft);
        }

        if (available(topRight) && AI_prev.getPiece().isKing() &&
                findMiddle(AI_prev, topRight).hasPiece() && findMiddle(AI_prev, topRight).getPiece().getType().equals(PLAYER))
        {
            availableMoves.add(topRight);
        }

        if (available(bottomRight) && findMiddle(AI_prev, bottomRight).hasPiece() &&
                findMiddle(AI_prev, bottomRight).getPiece().getType().equals(PLAYER))
        {
            availableMoves.add(bottomRight);
        }

        if (available(bottomLeft) && findMiddle(AI_prev, bottomLeft).hasPiece() &&
                findMiddle(AI_prev, bottomLeft).getPiece().getType().equals(PLAYER))
        {
            availableMoves.add(bottomLeft);
        }

        if (availableMoves.size() > 0)
        {
            result = true;
            pickNewPos();
        }

        availableMoves.clear();

        return result;
    }

    /**
     * Picks a new position at random for the AI to move to
     */
    public void pickNewPos()
    {
        Random rand = new Random();
        AI_new = availableMoves.get(rand.nextInt(availableMoves.size()));
    }

    /**
     * Performs the jump action for the AI
     */
    public void jump()
    {
        // check if the piece is a king
        if (AI_prev.getPiece().isKing())
        {
            // if it is then draw the piece onto the new tile
            AI_prev.getPiece().draw(AI_new);
            mp.start();

            removeJumpedPiece(AI_prev, AI_new);

        }
        else if (AI_prev.getPiece().getType().equals(OPPONENT))
        {
            // make sure the player's non-king pieces are moving up
            if (AI_new.getRow() > AI_prev.getRow())
            {
                // then draw and reset variables used for moving
                AI_prev.getPiece().draw(AI_new);
                mp.start();

                removeJumpedPiece(AI_prev, AI_new);
            }
        }
    }

    /**
     * This method determines if the AI can move. If it can,
     * then a random tile is chosen to move to
     *
     * @return
     */
    public boolean canMove()
    {
        // coordinates of tile in board
        int x = findPreviousX();
        int y = findPreviousY();

        // returned value
        boolean result = false;

        String topLeftName = findInBoard(x, y, 1, 1, "TopLeft");
        String topRightName = findInBoard(x, y, 1, 1, "TopRight");
        String bottomRightName = findInBoard(x, y, 1, 1, "BottomRight");
        String bottomLeftName = findInBoard(x, y, 1, 1, "BottomLeft");

        Tile topLeft = find(topLeftName);
        Tile topRight = find(topRightName);
        Tile bottomRight = find(bottomRightName);
        Tile bottomLeft = find(bottomLeftName);

        if (available(topLeft) && AI_prev.getPiece().isKing())
        {
            availableMoves.add(topLeft);
        }

        if (available(topRight) && AI_prev.getPiece().isKing())
        {
            availableMoves.add(topRight);
        }

        if (available(bottomRight))
        {
            availableMoves.add(bottomRight);
        }

        if (available(bottomLeft))
        {
            availableMoves.add(bottomLeft);
        }

        if (availableMoves.size() > 0)
        {
            result = true;
            pickNewPos();
        }

        availableMoves.clear();

        return result;
    }

    /**
     * Moves the selected AI piece
     */
    public void move()
    {
        // check if the piece is a king
        if (AI_prev.getPiece().isKing())
        {
            // if it is then draw the piece onto the new tile
            AI_prev.getPiece().draw(AI_new);

        } // otherwise if a non-king piece is moving
        else if (AI_new.getRow() > AI_prev.getRow())
        {
            // then draw and reset variables used for moving
            AI_prev.getPiece().draw(AI_new);
            mp.start();
        }
    }

    /**
     * Finds the X coordinate of the
     * previously chosen tile for the
     * AI movement
     *
     * @return
     */
    public int findPreviousX()
    {
        for (int i = 0; i < board.length; i ++)
        {
            for (int j = 0; j < board.length; j ++)
            {
                if (AI_prev.getName().equals(board[i][j]))
                {
                    return i;
                }
            }
        }

        return 0;
    }

    /**
     * Finds the Y coordinate of the
     * previously chosen tile for the
     * AI movement
     *
     * @return
     */
    public int findPreviousY()
    {
        for (int i = 0; i < board.length; i ++)
        {
            for (int j = 0; j < board.length; j ++)
            {
                if (AI_prev.getName().equals(board[i][j]))
                {
                    return j;
                }
            }
        }

        return 0;
    }

    /**
     * This method takes in the row and column position
     * of the name you are at in the game board. It then
     * uses the name to determine which corner you are
     * looking for. when that is decided, the method then
     * looks for the desired name using the change values
     * passed in.
     *
     * @param row
     * @param column
     * @param rowChange
     * @param columnChange
     * @param name
     * @return
     */
    public String findInBoard(int row, int column, int rowChange, int columnChange, String name)
    {
        if (name.equals("TopLeft") && row - rowChange >= 0 && column - columnChange >= 0)
        {
            return board[row - rowChange][column - columnChange];
        }
        else if (name.equals("TopRight") && row - rowChange >= 0 && column + columnChange < board.length)
        {
            return board[row - rowChange][column + columnChange];
        }
        else if (name.equals("BottomRight") && row + rowChange < board.length && column + columnChange < board.length)
        {
            return board[row + rowChange][column + columnChange];
        }
        else if (name.equals("BottomLeft") && row + rowChange < board.length && column - columnChange >= 0)
        {
            return board[row + rowChange][column - columnChange];
        }

        return "";
    }

    /**
     * Checks to see if the tile you are
     * looking at has room for a piece
     *
     * @param t
     * @return
     */
    public boolean available(Tile t)
    {
        return (t != null && !t.hasPiece());
    }

    /**
     * Finds the tile with the specified name
     * in the tile array
     *
     * @param name
     * @return
     */
    public Tile find(String name)
    {
        for (int i = 0; i < tiles.size(); i ++)
        {
            if (tiles.get(i).getName().equals(name))
            {
                return tiles.get(i);
            }
        }

        return null;
    }

    /**
     * When this method is called, all the AI (blue) variable used
     * for jumping and moving are reset. If a blue piece is able to
     * be kinged then this method will also king that piece
     */
    public void resetAI()
    {
        AI_prev.setTaken(false);
        AI_new.setTaken(true);

        AI_new.setPiece(AI_prev.getPiece());
        AI_prev.setPiece(null);

        makeAIKing();

        AI_prev = null;
        AI_new = null;
    }

    /**
     * This method checks to see if one player has zero tiles.
     * If a player does, then the end condition for the game is
     * satisfied and the game is over.
     */
    public void win()
    {
        int opponentCount = getCount(OPPONENT);
        int playerCount = getCount(PLAYER);

        if (opponentCount == 0)
        {
            winText.setText(getString(R.string.orange_win));
            winText.setVisibility(View.VISIBLE);

            removeListeners();
        }
        else if (playerCount == 0)
        {
            winText.setText(R.string.blue_win);
            winText.setVisibility(View.VISIBLE);

            removeListeners();
        }
    }

    /**
     * This method is used to count each player's tiles
     *
     * @param type
     * @return count
     */
    public int getCount(String type)
    {
        int count = 0;

        for (int i = 0; i < tiles.size(); i ++)
        {
            if (tiles.get(i).hasPiece() && tiles.get(i).getPiece().getType().equals(type))
            {
                count ++;
            }
        }

        return count;
    }

    /**
     * This method is used to set all the action listeners on every tile to null.
     * This is done when the game has been won by a player.
     */
    public void removeListeners()
    {
        for (int i = 0; i < tiles.size(); i ++)
        {
            tiles.get(i).getImage().setOnClickListener(null);
        }
    }

    /**
     * Assign all images to all objects
     */
    private void assignImages()
    {
        playerImage1 = (ImageView) findViewById(R.id.Player1);
        playerImage2 = (ImageView) findViewById(R.id.Player2);
        playerImage3 = (ImageView) findViewById(R.id.Player3);
        playerImage4 = (ImageView) findViewById(R.id.Player4);
        playerImage5 = (ImageView) findViewById(R.id.Player5);
        playerImage6 = (ImageView) findViewById(R.id.Player6);
        playerImage7 = (ImageView) findViewById(R.id.Player7);
        playerImage8 = (ImageView) findViewById(R.id.Player8);
        playerImage9 = (ImageView) findViewById(R.id.Player9);
        playerImage10 = (ImageView) findViewById(R.id.Player10);
        playerImage11 = (ImageView) findViewById(R.id.Player11);
        playerImage12 = (ImageView) findViewById(R.id.Player12);

        opponentImage1 = (ImageView) findViewById(R.id.Opponent1);
        opponentImage2 = (ImageView) findViewById(R.id.Opponent2);
        opponentImage3 = (ImageView) findViewById(R.id.Opponent3);
        opponentImage4 = (ImageView) findViewById(R.id.Opponent4);
        opponentImage5 = (ImageView) findViewById(R.id.Opponent5);
        opponentImage6 = (ImageView) findViewById(R.id.Opponent6);
        opponentImage7 = (ImageView) findViewById(R.id.Opponent7);
        opponentImage8 = (ImageView) findViewById(R.id.Opponent8);
        opponentImage9 = (ImageView) findViewById(R.id.Opponent9);
        opponentImage10 = (ImageView) findViewById(R.id.Opponent10);
        opponentImage11 = (ImageView) findViewById(R.id.Opponent11);
        opponentImage12 = (ImageView) findViewById(R.id.Opponent12);

        A1_Image = (ImageView) findViewById(R.id.A1);
        C1_Image = (ImageView) findViewById(R.id.C1);
        E1_Image = (ImageView) findViewById(R.id.E1);
        G1_Image = (ImageView) findViewById(R.id.G1);

        B2_Image = (ImageView) findViewById(R.id.B2);
        D2_Image = (ImageView) findViewById(R.id.D2);
        F2_Image = (ImageView) findViewById(R.id.F2);
        H2_Image = (ImageView) findViewById(R.id.H2);

        A3_Image = (ImageView) findViewById(R.id.A3);
        C3_Image = (ImageView) findViewById(R.id.C3);
        E3_Image = (ImageView) findViewById(R.id.E3);
        G3_Image = (ImageView) findViewById(R.id.G3);

        B4_Image = (ImageView) findViewById(R.id.B4);
        D4_Image = (ImageView) findViewById(R.id.D4);
        F4_Image = (ImageView) findViewById(R.id.F4);
        H4_Image = (ImageView) findViewById(R.id.H4);

        A5_Image = (ImageView) findViewById(R.id.A5);
        C5_Image = (ImageView) findViewById(R.id.C5);
        E5_Image = (ImageView) findViewById(R.id.E5);
        G5_Image = (ImageView) findViewById(R.id.G5);

        B6_Image = (ImageView) findViewById(R.id.B6);
        D6_Image = (ImageView) findViewById(R.id.D6);
        F6_Image = (ImageView) findViewById(R.id.F6);
        H6_Image = (ImageView) findViewById(R.id.H6);

        A7_Image = (ImageView) findViewById(R.id.A7);
        C7_Image = (ImageView) findViewById(R.id.C7);
        E7_Image = (ImageView) findViewById(R.id.E7);
        G7_Image = (ImageView) findViewById(R.id.G7);

        B8_Image = (ImageView) findViewById(R.id.B8);
        D8_Image = (ImageView) findViewById(R.id.D8);
        F8_Image = (ImageView) findViewById(R.id.F8);
        H8_Image = (ImageView) findViewById(R.id.H8);
    }

    /**
     * set up the player's pieces
     */
    private void initializePlayers()
    {
        // 12 pieces go to the player
        //            (image, type, king, start)
        p1 = new Piece(playerImage1, PLAYER, false, H8);
        p2 = new Piece(playerImage2, PLAYER, false, F8);
        p3 = new Piece(playerImage3, PLAYER, false, D8);
        p4 = new Piece(playerImage4, PLAYER, false, B8);
        p5 = new Piece(playerImage5, PLAYER, false, G7);
        p6 = new Piece(playerImage6, PLAYER, false, E7);
        p7 = new Piece(playerImage7, PLAYER, false, C7);
        p8 = new Piece(playerImage8, PLAYER, false, A7);
        p9 = new Piece(playerImage9, PLAYER, false, H6);
        p10 = new Piece(playerImage10, PLAYER, false, F6);
        p11 = new Piece(playerImage11, PLAYER, false, D6);
        p12 = new Piece(playerImage12, PLAYER, false, B6);
    }

    /**
     * set up the opponent's pieces
     */
    private void initializeOpponents()
    {
        // 12 pieces go to the opponent
        //            (image, type, king?)
        o1 = new Piece(opponentImage1, OPPONENT, false, A1);
        o2 = new Piece(opponentImage2, OPPONENT, false, C1);
        o3 = new Piece(opponentImage3, OPPONENT, false, E1);
        o4 = new Piece(opponentImage4, OPPONENT, false, G1);
        o5 = new Piece(opponentImage5, OPPONENT, false, B2);
        o6 = new Piece(opponentImage6, OPPONENT, false, D2);
        o7 = new Piece(opponentImage7, OPPONENT, false, F2);
        o8 = new Piece(opponentImage8, OPPONENT, false, H2);
        o9 = new Piece(opponentImage9, OPPONENT, false, A3);
        o10 = new Piece(opponentImage10, OPPONENT, false, C3);
        o11 = new Piece(opponentImage11, OPPONENT, false, E3);
        o12 = new Piece(opponentImage12, OPPONENT, false, G3);
    }

    /**
     * initialize all tiles
     */
    private void initializeTiles()
    {
        // first row
        //           (image, row, piece, name, x, y, neighbor count, jumps count)
        A1 = new Tile(A1_Image, 1, o1, "A1", true, 0, 0, 1, 1);
        C1 = new Tile(C1_Image, 1, o2, "C1", true, 280, 0, 2, 2);
        E1 = new Tile(E1_Image, 1, o3, "E1", true, 560, 0, 2, 2);
        G1 = new Tile(G1_Image, 1, o4, "G1", true, 840, 0, 2,  1);

        // second row
        B2 = new Tile(B2_Image, 2, o5, "B2", true, 140, 140, 4, 1);
        D2 = new Tile(D2_Image, 2, o6, "D2", true, 420, 140, 4, 2);
        F2 = new Tile(F2_Image, 2, o7, "F2", true, 700, 140, 4, 2);
        H2 = new Tile(H2_Image, 2, o8, "H2", true, 980, 140, 2, 1);

        // third row
        A3 = new Tile(A3_Image, 3, o9, "A3", true, 0, 280, 2, 2);
        C3 = new Tile(C3_Image, 3, o10, "C3", true, 280, 280, 4, 4);
        E3 = new Tile(E3_Image, 3, o11, "E3", true, 560, 280, 4, 4);
        G3 = new Tile(G3_Image, 3, o12, "G3", true, 840, 280, 4, 2);

        // fourth row
        B4 = new Tile(B4_Image, 4, null, "B4", false, 140, 420, 4, 2);
        D4 = new Tile(D4_Image, 4, null, "D4", false, 420, 420, 4, 4);
        F4 = new Tile(F4_Image, 4, null, "F4", false, 700, 420, 4, 4);
        H4 = new Tile(H4_Image, 4, null, "H4", false, 980, 420, 2, 2);

        // fifth row
        A5 = new Tile(A5_Image, 5, null, "A5", false, 0, 560, 2, 2);
        C5 = new Tile(C5_Image, 5, null, "C5", false, 280, 560, 4, 4);
        E5 = new Tile(E5_Image, 5, null, "E5", false, 560, 560, 4, 4);
        G5 = new Tile(G5_Image, 5, null, "G5", false, 840, 560, 4, 2);

        // sixth row
        B6 = new Tile(B6_Image, 6, p12, "B6", true, 140, 700, 4, 2);
        D6 = new Tile(D6_Image, 6, p11, "D6", true, 420, 700, 4, 4);
        F6 = new Tile(F6_Image, 6, p10, "F6", true, 700, 700, 4, 4);
        H6 = new Tile(H6_Image, 6, p9, "H6", true, 980, 700, 2, 2);

        // seventh row
        A7 = new Tile(A7_Image, 7, p8, "A7", true, 0, 840, 2, 1);
        C7 = new Tile(C7_Image, 7, p7, "C7", true, 280, 840, 4, 2);
        E7 = new Tile(E7_Image, 7, p6, "E7", true, 560, 840, 4, 2);
        G7 = new Tile(G7_Image, 7, p5, "G7", true, 840, 840, 4, 1);

        // sixth row
        B8 = new Tile(B8_Image, 8, p4, "B8", true, 140, 980, 2, 1);
        D8 = new Tile(D8_Image, 8, p3, "D8", true, 420, 980, 2, 2);
        F8 = new Tile(F8_Image, 8, p2, "F8", true, 700, 980, 2, 2);
        H8 = new Tile(H8_Image, 8, p1, "H8", true, 980, 980, 1, 1);
    }

    public void addToArray()
    {
        tiles.add(A1);
        tiles.add(C1);
        tiles.add(E1);
        tiles.add(G1);
        tiles.add(B2);
        tiles.add(D2);
        tiles.add(F2);
        tiles.add(H2);
        tiles.add(A3);
        tiles.add(C3);
        tiles.add(E3);
        tiles.add(G3);
        tiles.add(B4);
        tiles.add(D4);
        tiles.add(F4);
        tiles.add(H4);
        tiles.add(A5);
        tiles.add(C5);
        tiles.add(E5);
        tiles.add(G5);
        tiles.add(B6);
        tiles.add(D6);
        tiles.add(F6);
        tiles.add(H6);
        tiles.add(A7);
        tiles.add(C7);
        tiles.add(E7);
        tiles.add(G7);
        tiles.add(B8);
        tiles.add(D8);
        tiles.add(F8);
        tiles.add(H8);
    }

    /**
     * This method sets up the connectivity of the tiles
     */
    public void setNeighbors()
    {
        // ROw ONE
        A1.addNeighbor(0, B2);

        C1.addNeighbor(0, B2);
        C1.addNeighbor(1, D2);

        E1.addNeighbor(0, D2);
        E1.addNeighbor(1, F2);

        G1.addNeighbor(0, F2);
        G1.addNeighbor(1, H2);

        // ROW TWO
        B2.addNeighbor(0, A1);
        B2.addNeighbor(1, C1);
        B2.addNeighbor(2, A3);
        B2.addNeighbor(3, C3);

        D2.addNeighbor(0, C1);
        D2.addNeighbor(1, E1);
        D2.addNeighbor(2, C3);
        D2.addNeighbor(3, E3);

        F2.addNeighbor(0, E1);
        F2.addNeighbor(1, G1);
        F2.addNeighbor(2, E3);
        F2.addNeighbor(3, G3);

        H2.addNeighbor(0, G1);
        H2.addNeighbor(1, G3);

        // ROW THREE
        A3.addNeighbor(0, B2);
        A3.addNeighbor(1, B4);

        C3.addNeighbor(0, B2);
        C3.addNeighbor(1, D2);
        C3.addNeighbor(2, B4);
        C3.addNeighbor(3, D4);

        E3.addNeighbor(0, D2);
        E3.addNeighbor(1, F2);
        E3.addNeighbor(2, F4);
        E3.addNeighbor(3, D4);

        G3.addNeighbor(0, F2);
        G3.addNeighbor(1, H2);
        G3.addNeighbor(2, F4);
        G3.addNeighbor(3, H4);

        // ROW FOUR
        B4.addNeighbor(0, A3);
        B4.addNeighbor(1, C3);
        B4.addNeighbor(2, A5);
        B4.addNeighbor(3, C5);

        D4.addNeighbor(0, C3);
        D4.addNeighbor(1, E3);
        D4.addNeighbor(2, C5);
        D4.addNeighbor(3, E5);

        F4.addNeighbor(0, E3);
        F4.addNeighbor(1, G3);
        F4.addNeighbor(2, E5);
        F4.addNeighbor(3, G5);

        H4.addNeighbor(0, G3);
        H4.addNeighbor(1, G5);

        // ROW FIVE
        A5.addNeighbor(0, B4);
        A5.addNeighbor(1, B6);

        C5.addNeighbor(0, B4);
        C5.addNeighbor(1, D4);
        C5.addNeighbor(2, B6);
        C5.addNeighbor(3, D6);

        E5.addNeighbor(0, D4);
        E5.addNeighbor(1, F4);
        E5.addNeighbor(2, D6);
        E5.addNeighbor(3, F6);

        G5.addNeighbor(0, F4);
        G5.addNeighbor(1, H4);
        G5.addNeighbor(2, F6);
        G5.addNeighbor(3, H6);

        // ROW SIX
        B6.addNeighbor(0, A5);
        B6.addNeighbor(1, C5);
        B6.addNeighbor(2, A7);
        B6.addNeighbor(3, C7);

        D6.addNeighbor(0, C5);
        D6.addNeighbor(1, E5);
        D6.addNeighbor(2, C7);
        D6.addNeighbor(3, E7);

        F6.addNeighbor(0, E5);
        F6.addNeighbor(1, G5);
        F6.addNeighbor(2, E7);
        F6.addNeighbor(3, G7);

        H6.addNeighbor(0, G5);
        H6.addNeighbor(1, G7);

        // ROW SEVEN
        A7.addNeighbor(0, B6);
        A7.addNeighbor(1, B8);

        C7.addNeighbor(0, B6);
        C7.addNeighbor(1, D6);
        C7.addNeighbor(2, B8);
        C7.addNeighbor(3, D8);

        E7.addNeighbor(0, D6);
        E7.addNeighbor(1, F6);
        E7.addNeighbor(2, D8);
        E7.addNeighbor(3, F8);

        G7.addNeighbor(0, F6);
        G7.addNeighbor(1, H6);
        G7.addNeighbor(2, F8);
        G7.addNeighbor(3, H8);

        // ROW EIGHT
        B8.addNeighbor(0, A7);
        B8.addNeighbor(1, C7);

        D8.addNeighbor(0, C7);
        D8.addNeighbor(1, E7);

        F8.addNeighbor(0, E7);
        F8.addNeighbor(1, G7);

        H8.addNeighbor(0, G7);
    }

    public void setJumps()
    {
        // ROW ONE
        A1.addJump(0,C3);

        C1.addJump(0, A3);
        C1.addJump(1, E3);

        E1.addJump(0, C3);
        E1.addJump(1, G3);

        G1.addJump(0, E3);

        // ROW TWO
        B2.addJump(0, D4);

        D2.addJump(0, B4);
        D2.addJump(1, F4);

        F2.addJump(0, D4);
        F2.addJump(1, H4);

        H2.addJump(0, F4);

        // ROW THREE
        A3.addJump(0, C1);
        A3.addJump(1, C5);

        C3.addJump(0, A1);
        C3.addJump(1, E1);
        C3.addJump(2, A5);
        C3.addJump(3, E5);

        E3.addJump(0, C1);
        E3.addJump(1, G1);
        E3.addJump(2, C5);
        E3.addJump(3, G5);

        G3.addJump(0, E1);
        G3.addJump(1, E5);

        // ROW FOUR
        B4.addJump(0, D2);
        B4.addJump(1, D6);

        D4.addJump(0, B2);
        D4.addJump(1, F2);
        D4.addJump(2, B6);
        D4.addJump(3, F6);

        F4.addJump(0, D2);
        F4.addJump(1, H2);
        F4.addJump(2, D6);
        F4.addJump(3, H6);

        H4.addJump(0, F2);
        H4.addJump(1, F6);

        // ROW FIVE
        A5.addJump(0, C3);
        A5.addJump(1, C7);

        C5.addJump(0, A3);
        C5.addJump(1, E3);
        C5.addJump(2, A7);
        C5.addJump(3, E7);

        E5.addJump(0, C3);
        E5.addJump(1, G3);
        E5.addJump(2, C7);
        E5.addJump(3, G7);

        G5.addJump(0, E3);
        G5.addJump(1, E7);

        // ROW SIX
        B6.addJump(0, D4);
        B6.addJump(1, D8);

        D6.addJump(0, B4);
        D6.addJump(1, F4);
        D6.addJump(2, B8);
        D6.addJump(3, F8);

        F6.addJump(0, D4);
        F6.addJump(1, H4);
        F6.addJump(2, D8);
        F6.addJump(3, H8);

        H6.addJump(0, F4);
        H6.addJump(1, F8);

        // ROW SEVEN
        A7.addJump(0, C5);

        C7.addJump(0, A5);
        C7.addJump(0, E5);

        E7.addJump(0, C5);
        E7.addJump(1, G5);

        G7.addJump(0, E5);

        // ROW EIGHT
        B8.addJump(0, D6);

        D8.addJump(0, B6);
        D8.addJump(1, F6);

        F8.addJump(0, D6);
        F8.addJump(1, H6);

        H8.addJump(0, F6);
    }

    public void addOpponents()
    {
        opponentTiles.clear();
        for (int i = 0; i < tiles.size(); i ++)
        {
            if (tiles.get(i).hasPiece())
            {
                if (tiles.get(i).getPiece().getType().equals(OPPONENT))
                {
                    opponentTiles.add(tiles.get(i));
                }
            }
        }
    }
}