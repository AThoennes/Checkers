package com.thoennes.checkers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

public class MainActivity extends AppCompatActivity
{
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
        game.generateAssets();

        // assign the links each tile has
        game.asignNeighbors();
    }
}