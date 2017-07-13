package com.thoennes.checkers;

import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    // media player used to play a clicking sound when you move
    MediaPlayer mp;

    TextView winText;

    // size of tile
    static float size = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // use the custom layout
        setContentView(R.layout.layout_game_board);

        // initialize the game
        Game game = Game.init(this);

        // grab the size of the screen and scale the size of
        // the tiles to the device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        size = width/8;

        // create the tiles and pieces
        generateAssests();

        // assign the links each tile has
        //setLinks();
        links();

        // assign the tile an ID (index in the array)
        index();

        // USED TO DEBUG FOR NOW
        // go through each tile and print the neighbors it links to
        for (int j = 0; j < game.getTiles().size(); j ++)
        {
            Tile t = game.getTiles().get(j);
            System.out.print(j + ":     ");
            for (int k = 0; k < t.getNeighbors().size(); k ++)
            {
                System.out.print(t.getNeighbors().get(k).getID()+",");
            }
            System.out.println("\n");
        }

        index();
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
        int count = 1;

        for (int i = 0; i < 32; i ++)
        {
            //Log.d("ZZZZZZZZZZZZZZZZZ", i+"\t"+multipleOfEighth);
            if (count == 4)
            {
                count = 0;
                multipleOfEighth = !multipleOfEighth;
            }
            /*if (i == 0 || i == 8 || i == 16 || i == 24)//i % 8 == 0)
            {
                multipleOfEighth = true;
            }
            else
            {
                multipleOfEighth = false;
            }*/

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

            count ++;
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
            else // 1, 2, 3
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
     * method that creates all the tiles and pieces
     * for both players
     */
    public void generateAssests()
    {
        Paint black = new Paint();
        black.setColor(Color.BLACK);

        Paint red = new Paint();
        red.setColor(Color.RED);

        // set the color of the opponent painter
        Paint opponent = new Paint();
        opponent.setColor(Color.rgb(0, 0, 500));

        // set the color of the player painter
        Paint player = new Paint();
        player.setColor(Color.rgb(255, 128, 0));

        Game game = Game.getGameInstance();
        int tileNum = 0;

        for (int row = 0; row < 8; row++)
        {
            //if ( (row % 2) != 0) tileNum =  tileNum+1;
            System.out.print("ROW: " + row + ", TILENUM: " + tileNum + "\n");
            for (int col = 0; col < 8; col++)
            {
                // bounds of the tiles
                float left = row * size;
                float top = col * size;
                float right = left + size;
                float bottom = top + size;

                if ((row + col) % 2 == 0)
                {
                    float circleX = left + size / 2;
                    float circleY = top + size / 2;

                    // you are adding the tiles vertically and not horizontally
                    // which is why the numbering is off. the y*W+x does work
                    // but you are still adding the tiles vertically
                    game.addTile(new Tile(left, top, right, bottom, tileNum, black));
                    System.out.print("*******TILENUM: " + tileNum + "\n");
                    tileNum++;

                    /*if (col < 7)
                    {
                        tileNum += 2;
                    } else {
                        tileNum ++;
                    }*/

                    // top 3 rows are where the opponent starts
                    if (col < 3)
                    {
                        game.addOpponent(new Piece(circleX, circleY, opponent));
                    } // bottom 3 rows are where the player starts
                    else if (col > 4)
                    {
                        game.addPlayer(new Piece(circleX, circleY, player));
                    }
                }
                else
                {
                    // invalid tiles that are just used for drawing
                    game.addInvalidTile(new Tile(left, top, right, bottom, -1, red));
                }
            }
        }
    }

    // T E M P O R A R Y
    public void links()
    {
        Game game = Game.getGameInstance();
        game.getTiles().get(0).addNeighbor(game.getTiles().get(4));

        game.getTiles().get(1).addNeighbor(game.getTiles().get(4));
        game.getTiles().get(1).addNeighbor(game.getTiles().get(5));

        game.getTiles().get(2).addNeighbor(game.getTiles().get(5));
        game.getTiles().get(2).addNeighbor(game.getTiles().get(6));

        game.getTiles().get(3).addNeighbor(game.getTiles().get(6));
        game.getTiles().get(3).addNeighbor(game.getTiles().get(7));

        game.getTiles().get(4).addNeighbor(game.getTiles().get(0));
        game.getTiles().get(4).addNeighbor(game.getTiles().get(1));
        game.getTiles().get(4).addNeighbor(game.getTiles().get(8));
        game.getTiles().get(4).addNeighbor(game.getTiles().get(9));

        game.getTiles().get(5).addNeighbor(game.getTiles().get(1));
        game.getTiles().get(5).addNeighbor(game.getTiles().get(2));
        game.getTiles().get(5).addNeighbor(game.getTiles().get(9));
        game.getTiles().get(5).addNeighbor(game.getTiles().get(10));

        game.getTiles().get(6).addNeighbor(game.getTiles().get(2));
        game.getTiles().get(6).addNeighbor(game.getTiles().get(3));
        game.getTiles().get(6).addNeighbor(game.getTiles().get(10));
        game.getTiles().get(6).addNeighbor(game.getTiles().get(11));

        game.getTiles().get(7).addNeighbor(game.getTiles().get(3));
        game.getTiles().get(7).addNeighbor(game.getTiles().get(11));

        game.getTiles().get(8).addNeighbor(game.getTiles().get(4));
        game.getTiles().get(8).addNeighbor(game.getTiles().get(12));

        game.getTiles().get(9).addNeighbor(game.getTiles().get(4));
        game.getTiles().get(9).addNeighbor(game.getTiles().get(5));
        game.getTiles().get(9).addNeighbor(game.getTiles().get(12));
        game.getTiles().get(9).addNeighbor(game.getTiles().get(13));

        game.getTiles().get(10).addNeighbor(game.getTiles().get(5));
        game.getTiles().get(10).addNeighbor(game.getTiles().get(6));
        game.getTiles().get(10).addNeighbor(game.getTiles().get(13));
        game.getTiles().get(10).addNeighbor(game.getTiles().get(14));

        game.getTiles().get(11).addNeighbor(game.getTiles().get(6));
        game.getTiles().get(11).addNeighbor(game.getTiles().get(7));
        game.getTiles().get(11).addNeighbor(game.getTiles().get(11));
        game.getTiles().get(11).addNeighbor(game.getTiles().get(15));

        game.getTiles().get(12).addNeighbor(game.getTiles().get(8));
        game.getTiles().get(12).addNeighbor(game.getTiles().get(9));
        game.getTiles().get(12).addNeighbor(game.getTiles().get(16));
        game.getTiles().get(12).addNeighbor(game.getTiles().get(17));

        game.getTiles().get(13).addNeighbor(game.getTiles().get(9));
        game.getTiles().get(13).addNeighbor(game.getTiles().get(10));
        game.getTiles().get(13).addNeighbor(game.getTiles().get(17));
        game.getTiles().get(13).addNeighbor(game.getTiles().get(18));

        game.getTiles().get(14).addNeighbor(game.getTiles().get(10));
        game.getTiles().get(14).addNeighbor(game.getTiles().get(11));
        game.getTiles().get(14).addNeighbor(game.getTiles().get(18));
        game.getTiles().get(14).addNeighbor(game.getTiles().get(19));

        game.getTiles().get(15).addNeighbor(game.getTiles().get(11));
        game.getTiles().get(15).addNeighbor(game.getTiles().get(19));

        game.getTiles().get(16).addNeighbor(game.getTiles().get(12));
        game.getTiles().get(16).addNeighbor(game.getTiles().get(20));

        game.getTiles().get(17).addNeighbor(game.getTiles().get(12));
        game.getTiles().get(17).addNeighbor(game.getTiles().get(13));
        game.getTiles().get(17).addNeighbor(game.getTiles().get(20));
        game.getTiles().get(17).addNeighbor(game.getTiles().get(21));

        game.getTiles().get(18).addNeighbor(game.getTiles().get(13));
        game.getTiles().get(18).addNeighbor(game.getTiles().get(14));
        game.getTiles().get(18).addNeighbor(game.getTiles().get(21));
        game.getTiles().get(18).addNeighbor(game.getTiles().get(22));

        game.getTiles().get(19).addNeighbor(game.getTiles().get(14));
        game.getTiles().get(19).addNeighbor(game.getTiles().get(15));
        game.getTiles().get(19).addNeighbor(game.getTiles().get(22));
        game.getTiles().get(19).addNeighbor(game.getTiles().get(23));

        game.getTiles().get(20).addNeighbor(game.getTiles().get(16));
        game.getTiles().get(20).addNeighbor(game.getTiles().get(17));
        game.getTiles().get(20).addNeighbor(game.getTiles().get(24));
        game.getTiles().get(20).addNeighbor(game.getTiles().get(25));

        game.getTiles().get(21).addNeighbor(game.getTiles().get(17));
        game.getTiles().get(21).addNeighbor(game.getTiles().get(18));
        game.getTiles().get(21).addNeighbor(game.getTiles().get(25));
        game.getTiles().get(21).addNeighbor(game.getTiles().get(26));

        game.getTiles().get(22).addNeighbor(game.getTiles().get(18));
        game.getTiles().get(22).addNeighbor(game.getTiles().get(19));
        game.getTiles().get(22).addNeighbor(game.getTiles().get(26));
        game.getTiles().get(22).addNeighbor(game.getTiles().get(27));

        game.getTiles().get(23).addNeighbor(game.getTiles().get(19));
        game.getTiles().get(23).addNeighbor(game.getTiles().get(27));

        game.getTiles().get(24).addNeighbor(game.getTiles().get(20));
        game.getTiles().get(24).addNeighbor(game.getTiles().get(28));

        game.getTiles().get(25).addNeighbor(game.getTiles().get(20));
        game.getTiles().get(25).addNeighbor(game.getTiles().get(21));
        game.getTiles().get(25).addNeighbor(game.getTiles().get(28));
        game.getTiles().get(25).addNeighbor(game.getTiles().get(29));

        game.getTiles().get(26).addNeighbor(game.getTiles().get(21));
        game.getTiles().get(26).addNeighbor(game.getTiles().get(22));
        game.getTiles().get(26).addNeighbor(game.getTiles().get(29));
        game.getTiles().get(26).addNeighbor(game.getTiles().get(30));

        game.getTiles().get(27).addNeighbor(game.getTiles().get(22));
         game.getTiles().get(27).addNeighbor(game.getTiles().get(23));
        game.getTiles().get(27).addNeighbor(game.getTiles().get(30));
        game.getTiles().get(27).addNeighbor(game.getTiles().get(31));

        game.getTiles().get(28).addNeighbor(game.getTiles().get(24));
        game.getTiles().get(28).addNeighbor(game.getTiles().get(25));

        game.getTiles().get(29).addNeighbor(game.getTiles().get(25));
        game.getTiles().get(29).addNeighbor(game.getTiles().get(26));

        game.getTiles().get(30).addNeighbor(game.getTiles().get(26));
        game.getTiles().get(30).addNeighbor(game.getTiles().get(27));

        game.getTiles().get(31).addNeighbor(game.getTiles().get(27));
    }

    public void index()
    {
        Game game = Game.getGameInstance();

        game.getTiles().get(8).setID(1);
        game.getTiles().get(16).setID(2);
        game.getTiles().get(24).setID(3);

        game.getTiles().get(4).setID(4);
        game.getTiles().get(12).setID(5);
        game.getTiles().get(20).setID(6);
        game.getTiles().get(28).setID(7);

        game.getTiles().get(1).setID(8);
        game.getTiles().get(9).setID(9);
        game.getTiles().get(17).setID(10);
        game.getTiles().get(25).setID(11);

        game.getTiles().get(5).setID(12);
        game.getTiles().get(13).setID(13);
        game.getTiles().get(21).setID(14);
        game.getTiles().get(29).setID(15);

        game.getTiles().get(2).setID(16);
        game.getTiles().get(10).setID(17);
        game.getTiles().get(18).setID(18);
        game.getTiles().get(26).setID(19);

        game.getTiles().get(6).setID(20);
        game.getTiles().get(14).setID(21);
        game.getTiles().get(22).setID(22);
        game.getTiles().get(30).setID(23);

        game.getTiles().get(3).setID(24);
        game.getTiles().get(11).setID(25);
        game.getTiles().get(19).setID(26);
        game.getTiles().get(27).setID(27);

        game.getTiles().get(7).setID(28);
        game.getTiles().get(15).setID(29);
        game.getTiles().get(23).setID(30);
        game.getTiles().get(31).setID(31);
    }
}