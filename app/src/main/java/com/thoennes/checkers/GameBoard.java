package com.thoennes.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Alex on 1/20/17.
 *
 */

public class GameBoard extends View
{
    // size of each square tile
    float size;

    // paint objects used to paint the tiles and pieces
    Paint red; // red tiles are just for show, you can only move on the black tiles
    Paint black; // black tiles are the playable ones and these contain pieces
    Paint opponentColor; // used to draw the opponent pieces which are blue
    Paint playerColor; // used to draw the player pieces which are orange

    private float radius;

    ArrayList<Piece> pieces = new ArrayList<>();
    ArrayList<Tile> tiles = new ArrayList<>();

    float [] old = new float [4];
    float [] New = new float [2];

    boolean firstClick = true;
    boolean moving = false;

    Tile t;

    // used
    public GameBoard(Context context)
    {
        super(context);
        init(context);
    }

    public GameBoard(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public GameBoard(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context)
    {

        // set the color of the red tile painter
        red = new Paint();
        red.setColor(Color.RED);

        // set the color of the black tile painter
        black = new Paint();
        black.setColor(Color.BLACK);

        // set the color of the opponent painter
        opponentColor = new Paint();
        opponentColor.setColor(Color.rgb(0, 0, 500));

        // set the color of the player painter
        playerColor = new Paint();
        playerColor.setColor(Color.rgb(255, 128, 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Game.getGameInstance().move(event.getX(), event.getY());
        float clickX = event.getX();
        float clickY = event.getY();

        if (wasClicked(clickX, clickY))
        {
            Log.d("CLICKED", "CLICKEDDD");

            if (firstClick)
            {
                if (tileHasPiece(t))
                {
                    old = getClickedTile(clickX, clickY, firstClick);
                    firstClick = false;
                    Log.d("FIRST", "SAVED FIRST COORDS");
                }
            }
            else
            {
                if (!tileHasPiece(t))
                {
                    New = getClickedTile(clickX, clickY, firstClick);
                    Log.d("SECOND", "SAVED 2nd COORDS");
                    firstClick = true;
                    moving = true;
                    invalidate();
                }
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        pieces.clear();
        tiles.clear();


        if (!moving)
        {
            drawGameBoard(canvas);
        }
        else
        {
            drawGameBoard(canvas);

            float oldX = old[0] + (size / 2);
            float oldY = old[1] + (size / 2);

            float newX = New[0] + (size / 2);
            float newY = New[1] + (size / 2);

            //              left    top     right   bottom  color
            canvas.drawRect(old[0], old[1], old[2], old[3], black);
            canvas.drawCircle(newX, newY, size/2, playerColor);


            for (int i = 0; i < pieces.size(); i ++)
            {
                if (pieces.get(i).getX() == oldX && pieces.get(i).getY() == oldY)
                {
                    pieces.get(i).setX(newX);
                    pieces.get(i).setY(newY);
                    break;
                }
            }
        }
    }

    private void drawGameBoard(Canvas canvas)
    {
        // the size of each tile is the screen width divided
        // by 8 because this is an 8x8 board
        size = canvas.getWidth() / 8;

        int c = 0;

        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                // these are variables used in drawing the tiles
                float left = row * size;
                float top = col * size;
                float right = left + size;
                float bottom = top + size;

                if ((row + col) % 2 == 0)
                {
                    float circleX = left + size / 2;
                    float circleY = top + size / 2;
                    radius = size / 2;
                    canvas.drawRect(left, top, right, bottom, black);
                    drawPiece(col, circleX, circleY, radius, canvas);
                }
                else
                {
                    canvas.drawRect(left, top, right, bottom, red);
                }c++;
            }
        }
    }

    private void drawPiece(int col, float x, float y, float radius, Canvas canvas)
    {
        if (col < 3)
        {
            pieces.add(new Piece(x, y));
            canvas.drawCircle(x, y, radius, opponentColor);
        }
        else if (col > 4)
        {
            pieces.add(new Piece(x, y));
            canvas.drawCircle(x, y, radius, playerColor);
        }
    }

    private boolean wasClicked(float x, float y)
    {
        for (int i = 0; i < tiles.size(); i ++)
        {
             t = tiles.get(i);
            if (validTile(t, x, y))
            {
                return true;
            }
        }

        return false;
    }

    private boolean validTile(Tile t, float x, float y)
    {
        if (x < t.getRight() && x > t.getLeft() && y < t.getBottom() && y > t.getTop())
        {
            return true;
        }

        return false;
    }

    private boolean tileHasPiece(Tile t)
    {
        for (int i = 0; i < pieces.size(); i ++)
        {
            float tileX = t.getLeft() + (size / 2);
            float tileY = t.getTop() + (size / 2);

            if (tileX == pieces.get(i).getX() && tileY == pieces.get(i).getY())
            {
                return true;
            }
        }

        return false;
    }

    private float [] getClickedTile(float x, float y, boolean firstClick)
    {
        float temp[];

        if (firstClick)
        {
            temp = new float[4];

            for (int i = 0; i < tiles.size(); i++)
            {
                Tile t = tiles.get(i);
                if (validTile(t, x, y))
                {
                    getValues(temp, firstClick, t);
                }
            }
        }
        else
        {
            temp = new float[2];

            for (int i = 0; i < tiles.size(); i++)
            {
                Tile t = tiles.get(i);

                if (validTile(t, x, y))
                {
                    getValues(temp, firstClick, t);
                }
            }
        }

        return temp;
    }

    private void getValues(float temp[], boolean firstClick, Tile t)
    {
        if (firstClick)
        {
            temp[0] = t.getLeft();
            temp[1] = t.getTop();
            temp[2] = t.getRight();
            temp[3] = t.getBottom();
        }
        else
        {
            temp[0] = t.getLeft();
            temp[1] = t.getTop();
        }
    }

    /*public float getSize()
    {
        return this.size;
    }*/
}
