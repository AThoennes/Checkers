package com.thoennes.checkers;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

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
    ArrayList<Piece> pieces = new ArrayList<>();

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

    static float size = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_game_board);

        Game game = Game.init(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        size = width/8;

        generateAssests();


        //setContentView(new GameBoard(this));

        //tiles = GameBoard.tiles;
        //pieces = GameBoard.pieces;

        // reference button in the layout
        //resetButton = (Button) findViewById(btn);

        //mp = MediaPlayer.create(this, R.raw.move);

        //winText = (TextView) findViewById(R.id.winText);

        //setUp();

        // set up the click listeners for the tiles
        //onClick();
    }

    public void setLinks()
    {
        Game game = Game.getGameInstance();

        // take the tiles array from the game board class and store it in this tiles array so that
        // 1) GameBoard doesn't become the class that handles all the operations
        // 2) because the tiles array in the GameBoard class is being rewritten every time the scene
        //    is drawn, I need to store the tiles securely where it won't change

        // 0, 8, 16, 24 are the edge numbers that need to be dealt with specially
        boolean multipleOfEighth = true;

        for (int i = 0; i < 32; i ++)
        {
            if (i % 8 == 0)
            {
                multipleOfEighth = true;
            }
            else
            {
                multipleOfEighth = false;
            }

            if (multipleOfEighth)
            {
                if (!specialCase(i))
                {
                    // set the backward links
                    game.getTiles().get(i).addNeighbor(game.getTiles().get(i - 5));
                    game.getTiles().get(i).addNeighbor(game.getTiles().get(i - 4));

                    // set the forward link
                    game.getTiles().get(i).addNeighbor(game.getTiles().get(i + 3));
                    game.getTiles().get(i).addNeighbor(game.getTiles().get(i + 4));
                }
                else
                {
                    // if the number is 0-3, a multiple of 8, or 7,15,23,31
                    handleSpecialCase(i);
                }
            }
            else if (!multipleOfEighth)
            {
                if (!specialCase(i))
                {
                    game.getTiles().get(i).addNeighbor(game.getTiles().get(i - 4));
                    game.getTiles().get(i).addNeighbor(game.getTiles().get(i - 3));

                    game.getTiles().get(i).addNeighbor(game.getTiles().get(i + 4));
                    game.getTiles().get(i).addNeighbor(game.getTiles().get(i + 5));
                }
                else
                {
                    handleSpecialCase(i);
                }
            }
        }
    }

    /**
     * Method that determines if the current
     * tile in the checker board has special
     * links or not.
     *
     * @param index
     * @return
     */
    public boolean specialCase(int index)
    {
        // left side is divisible by 8 but right side isn't
        return (index < 4 || index % 8 == 0 || index == 7 || index == 15 || index == 23 || index == 31);
    }

    public void handleSpecialCase(int index)
    {
        Game game = Game.getGameInstance();

        if (index < 4) // 0, 1, 2, 3
        {
            if (index == 0) // 0
            {
                game.getTiles().get(index).addNeighbor(game.getTiles().get(index + 4));
            }
            else if (index != 0) // 1, 2, 3
            {
                game.getTiles().get(index).addNeighbor(game.getTiles().get(index + 3));
                game.getTiles().get(index).addNeighbor(game.getTiles().get(index + 4));
            }
        }
        else if (index % 8 == 0) // 8, 16, 24
        {
            game.getTiles().get(index).addNeighbor(game.getTiles().get(index - 4));

            game.getTiles().get(index).addNeighbor(game.getTiles().get(index + 4));
        }
        else if (index > 27 && index < 31) // 28, 29, 30
        {
            game.getTiles().get(index).addNeighbor(game.getTiles().get(index - 4));
            game.getTiles().get(index).addNeighbor(game.getTiles().get(index - 3));
        }
        else if (index == 31) // 31
        {
            game.getTiles().get(index).addNeighbor(game.getTiles().get(index - 4));
        }
        else // 7, 15, 23
        {
            game.getTiles().get(index).addNeighbor(game.getTiles().get(index - 4));

            game.getTiles().get(index).addNeighbor(game.getTiles().get(index + 4));
        }
    }


    /**
     * This method sets up all the
     * images and variable and arrays
     * that are needed for the game to
     * run properly
     */
    /*public void setUp()
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
//        setNeighbors();

        // set the jump connectivity
//        setJumps();

        // add all the tiles to an array list
        addToArray();

        addOpponents();
    }*/

    /**
     * This method handles the click listeners. To move, you click
     * the tile containing the piece you would like to move then
     * click the tile you would like to move to.
     */
    /*public void onClick()
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
    }*/

    public void generateAssests()
    {
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                // boundds of the tiles
                float left = row * size;
                float top = col * size;
                float right = left + size;
                float bottom = top + size;

                if ((row + col) % 2 == 0)
                {
                    float circleX = left + size / 2;
                    float circleY = top + size / 2;

                    Game.getGameInstance().addTile(new Tile(left, top, right, bottom));

                    if (col < 3)
                    {
                        Game.getGameInstance().addOpponent(new Piece(circleX, circleY));
                    }
                    else if (col > 4)
                    {
                        Game.getGameInstance().addPlayer(new Piece(circleX, circleY));
                    }
                }
            }
        }
    }

    /**
     * Restarts the game by resetting all the variables
     */
    /*public void restart()
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
    }*/

    /**
     * this method handles moving player pieces. The
     * player moves by tapping the piece they want to
     * move then the tile they want to move to
     */
    /*public void handleMoves()
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
    }*/

    /**
     * This method handles the jumping action
     */
    /*public void handleJump()
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

                // allow the computer to move
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
    }*/

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
    /*public void prepareForNextClick()
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
    }*/

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
    /*public void AI()
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
    }*/

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
}