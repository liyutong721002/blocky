package com.gamewerks.blocky.engine;

import java.util.Random;

public class RandomBlock {
    private PieceKind[] pieces;
    private int currentI;
    
    public RandomBlock(){
        pieces = PieceKind.ALL; 
        shuffle(); 
    }
            
    public void shuffle(){
        for (int i=0; i < 6; i++){
            Random random = new Random();
            int j = random.nextInt(7 - i) + i;
            PieceKind temp = pieces[i];
            pieces[i] = pieces[j];
            pieces[j] = temp;
        }
        currentI = 0;         
    }
    
    public PieceKind next(){
       if (currentI >= 6) {
           shuffle();
       } 
       currentI++;
       return pieces[currentI];
    }
}
