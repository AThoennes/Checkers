package com.thoennes.checkers;

import java.util.ArrayList;
import java.util.Random;

import static com.thoennes.checkers.MainActivity.size;

/**
 * Created by Alex on 7/14/17.
 *
 * AI Class
 */

public class AI
{
    // list of all pieces that the AI can use
    ArrayList<Piece> pieces;

    /**
     * Constructor to create an AI opponent
     *
     * @param pieces
     */
    public AI(ArrayList<Piece> pieces)
    {
        this.pieces = new ArrayList<Piece>(pieces);
    }

    public void adjustPieces(ArrayList<Piece> pieces)
    {
        this.pieces.addAll(pieces);
    }

    /**
     *
     * Moves a piece that the ai controls
     *
     */
    public void attemptMove()
    {
        Game game = Game.getGameInstance();

        Random rand = new Random();
        int pieceNum = rand.nextInt(pieces.size());
        System.out.println("PIECE NUM: " + pieceNum);

        Piece p = pieces.get(pieceNum);

        Tile start = game.findTappedTile(p.getX(), p.getY());

        // to avoid java.lang.IllegalArgumentException: n <= 0: 0
        int tileNum = 0;

        if (start.getNeighbors().size() > 1)
        {
            tileNum = rand.nextInt(start.getNeighbors().size());
        }
        System.out.println(tileNum);
        Tile end = start.getNeighbors().get(tileNum);

        if (end.isEmpty(game.getPlayerPieces(), game.getOpponentPieces()))
        {
            if (canMoveNonKing(start, end))
            {
                move(start, end);
            }
        }
        else
        {
            findMoveablePiece(start);
        }
    }

    /**
     * If a piece can not be moved from the first random generation,
     * you want to continually redraw a random number for a piece to
     * move until you find a valid answer
     *
     * @param start
     */
    private void findMoveablePiece(Tile start)
    {
        Random rand = new Random();

        Game game = Game.getGameInstance();

        // to avoid java.lang.IllegalArgumentException: n <= 0: 0
        int tileNum = 0;

        if (start.getNeighbors().size() > 1)
        {
            tileNum = rand.nextInt(start.getNeighbors().size());
        }
        System.out.println(tileNum);
        Tile end = start.getNeighbors().get(tileNum);

        if (end.isEmpty(game.getPlayerPieces(), game.getOpponentPieces()))
        {
            if (canMoveNonKing(start, end))
            {
                move(start, end);
            }
        }
        else
        {
            int pieceNum = rand.nextInt(pieces.size());
            System.out.println("PIECE NUM: " + pieceNum);

            Piece p = pieces.get(pieceNum);

            start = Game.getGameInstance().findTappedTile(p.getX(), p.getY());

            findMoveablePiece(start);
        }
    }

    /**
     * Moves the opponent piece to the
     * randomly selected tile
     *
     * @param start
     * @param end
     */
    private void move(Tile start, Tile end)
    {
        Game game = Game.getGameInstance();

        float x = end.getLeft() + (size/2);
        float y = end.getTop()+ (size/2);
        start.getPiece(game.getOpponentPieces()).setXY(x, y);
    }

    public boolean canMoveNonKing(Tile start, Tile end)
    {
        return (end.getLeft() < start.getLeft() && end.getBottom() > start.getBottom()) ||
                (end.getRight() > start.getRight() && end.getBottom() > start.getBottom());
    }

    /**
     * Returns the current pieces in play that the AI can use
     *
     * @return
     */
    public ArrayList<Piece> getPieces()
    {
        return pieces;
    }
}
