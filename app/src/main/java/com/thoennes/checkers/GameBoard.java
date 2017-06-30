package com.thoennes.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alex on 1/20/17.
 *
 */

public class GameBoard extends View
{
    boolean initialized = false;

    // size of each square tile
    float size;

    // paint objects used to paint the tiles and pieces
    Paint red; // red tiles are just for show, you can only move on the black tiles
    Paint black; // black tiles are the playable ones and these contain pieces
    Paint opponentColor; // used to draw the opponent pieces which are blue
    Paint playerColor; // used to draw the player pieces which are orange

    private float radius;

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
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawGameBoard(canvas);
        //canvas.drawCircle(0,0,radius, black);
    }

    private void drawGameBoard(Canvas canvas)
    {
        if (!initialized)
        {
            // the size of each tile is the screen width divided
            // by 8 because this is an 8x8 board
            size = canvas.getWidth() / 8;

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
                    }
                }
            }
            initialized = true;
        }
        else // if the base game board has been drawn once you now draw the board with the updated piece positions
        {
            Game game = Game.getGameInstance();

            // draw valid tiles
            for (int i = 0; i < game.getTiles().size(); i ++)
            {
                Tile t = game.getTiles().get(i);
                canvas.drawRect(t.getLeft(), t.getTop(), t.getRight(), t.getBottom(), t.getColor());
            }

            // draw invalid tiles
            for (int i = 0; i < game.getInvalidTiles().size(); i ++)
            {
                Tile t = game.getInvalidTiles().get(i);
                canvas.drawRect(t.getLeft(), t.getTop(), t.getRight(), t.getBottom(), t.getColor());
            }

            // draw player pieces
            for (int i = 0; i < game.getPlayerPieces().size(); i ++)
            {
                Piece p = game.getPlayerPieces().get(i);
                canvas.drawCircle(p.getX(), p.getY(), radius, p.getColor());
            }

            for (int j = 0; j < Game.getGameInstance().getOpponentPieces().size(); j ++)
            {
                Piece p = game.getPlayerPieces().get(j);
                canvas.drawCircle(p.getX(), p.getY(), radius, p.getColor());
            }
        }
    }

    private void drawPiece(int col, float x, float y, float radius, Canvas canvas)
    {
        if (col < 3)
        {
            canvas.drawCircle(x, y, radius, opponentColor);
        }
        else if (col > 4)
        {
            canvas.drawCircle(x, y, radius, playerColor);
        }
    }
}
