package com.thoennes.checkers;

import java.util.ArrayList;
import java.util.Random;

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

    public AI()
    {
        // code
    }

    public void adjustPieces(ArrayList<Piece> pieces)
    {
        this.pieces.addAll(pieces);
    }

    /**
     *
     * Moves a piece that the opponent controls
     *
     */
    public void move()
    {
        Game game = Game.getGameInstance();

        Random rand = new Random();
        int pieceNum = rand.nextInt(pieces.size());

        Piece p = pieces.get(pieceNum);

        Tile start = game.findTappedTile(p.getX(), p.getY());
//        int tileNum = rand.nextInt(start.getNeighbors().size());
//        Tile end = start.getNeighbors().get(tileNum);


        //while (end.is)
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
