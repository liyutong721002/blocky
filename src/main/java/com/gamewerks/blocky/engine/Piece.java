package com.gamewerks.blocky.engine;

import java.io.IOException;
import java.util.HashMap;

import com.gamewerks.blocky.util.Loader;
import com.gamewerks.blocky.util.Position;

public class Piece {
    
    private static HashMap ROTATION_DATA = null;
    
    static {
        try {
            ROTATION_DATA = Loader.loadAllRotationData();
        } catch (IOException ex) {
            System.out.println("Exception occurred loading rotation data");
            System.exit(-1);
        }
    }
    
    private PieceKind kind;
    private int orientation;
    private Position pos;
    
    public Piece(PieceKind kind, Position pos) {
        this.kind = kind;
        orientation = 0;
        this.pos = pos;
    }
    
     public PieceKind getKind() {
        return kind;
    }
     
    public Position getPosition() {
        return pos;
    }
    
    public void moveTo(Position p) {
        pos = p;
    }
    
    public boolean[][] getLayout() {
        return ((boolean[][][]) ROTATION_DATA.get(kind))[orientation];
    }
    
    public void rotate(boolean dir, Board b) {
        int possible = this.orientation;
        if (dir) {
            possible = (possible + 1) % 4;
        } else {
            int k = possible - 1;
            possible = k < 0 ? 3 : k;
        }
        Piece temp = new Piece(kind, pos);
        temp.orientation = possible; 
        if (b.collides(temp)) {

        }else {
                this.orientation = possible;
            }
    }
}
