package com.gamewerks.blocky.engine;

import com.gamewerks.blocky.util.Constants;
import com.gamewerks.blocky.util.Position;
import java.util.Random;

public class BlockyGame {
    private static final int LOCK_DELAY_LIMIT = 30;
    
    private Board board;
    private Piece activePiece;
    private Direction movement;
    private RandomBlock rand;
    
    private int lockCounter;
    
    public BlockyGame() {
        board = new Board();
        movement = Direction.NONE;
        lockCounter = 0;
        rand = new RandomBlock();
        trySpawnBlock();
    }
    
    private void trySpawnBlock() {
        if (activePiece == null) {
            PieceKind newPiece = rand.next();
            System.out.println("Piece: " + newPiece);
            activePiece = new Piece(newPiece, new Position(Constants.BOARD_HEIGHT - 1, Constants.BOARD_WIDTH / 2 - 2));
            if (board.collides(activePiece)) {
                System.exit(0);
            }
        }
    }
    
    private void processMovement(Direction movement) {
        Position nextPos;
        switch(movement) {
        case NONE:
            nextPos = activePiece.getPosition();
            break;
        case LEFT:
            nextPos = activePiece.getPosition().add(0, -1);
            break;
        case RIGHT:
            nextPos = activePiece.getPosition().add(0, 1);
            break;
        default:
            throw new IllegalStateException("Unrecognized direction: " + movement.name());
        }
        if (!board.collides(activePiece.getLayout(), nextPos)) {
            activePiece.moveTo(nextPos);
        }
    }
    
    private void processGravity() {
        Position nextPos = activePiece.getPosition().add(-1, 0);
        //activePiece.moveTo(nextPos);
        System.out.println("Trying to move to: " + nextPos.row);
        if (!board.collides(activePiece.getLayout(), nextPos)) {
            System.out.println("Piece moving to: " + nextPos.row);
            lockCounter = 0;
            activePiece.moveTo(nextPos);
        } else {
            System.out.println("Piece stopped at: " + activePiece.getPosition().row);
            if (lockCounter < LOCK_DELAY_LIMIT) {
                lockCounter += 1;
            } else {
                System.out.println("Piece locked at: " + activePiece.getPosition().row);
                board.addToWell(activePiece);
                lockCounter = 0;
                activePiece = null;
            }
        }
    }
    
    private void processClearedLines() {
        board.deleteRows(board.getCompletedRows());
    }
    
    public void step() {
        trySpawnBlock();
        processGravity();
        processClearedLines();
    }
    
    public boolean[][] getWell() {
        return board.getWell();
    }
    
    public Piece getActivePiece() { return activePiece; }
    public void setDirection(Direction movement) {
        this.movement = movement;
        processMovement(movement);
    }
    public void rotatePiece(boolean dir) { activePiece.rotate(dir, board); }
}