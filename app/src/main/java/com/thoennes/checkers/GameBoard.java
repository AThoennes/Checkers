package com.thoennes.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alex on 1/20/17.
 *
 */

public class GameBoard extends View
{
    // set to true when the board has been drawn the first time
    boolean initialized = false;

    // size of each square tile
    private float size;

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

    private void init(Context context) {}

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
                        canvas.drawRect(left, top, right, bottom, Game.getGameInstance().getBlack());
                        drawPiece(col, circleX, circleY, radius, canvas);
                    }
                    else
                    {
                        canvas.drawRect(left, top, right, bottom, Game.getGameInstance().getRed());
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

            // draw opponent pieces
            for (int j = 0; j < Game.getGameInstance().getOpponentPieces().size(); j ++)
            {
                Piece p = game.getOpponentPieces().get(j);
                canvas.drawCircle(p.getX(), p.getY(), radius, p.getColor());
            }
        }
    }

    private void drawPiece(int col, float x, float y, float radius, Canvas canvas)
    {
        if (col < 3)
        {
            canvas.drawCircle(x, y, radius, Game.getGameInstance().getOpponent());
        }
        else if (col > 4)
        {
            canvas.drawCircle(x, y, radius, Game.getGameInstance().getPlayerColor());
        }
    }
}
