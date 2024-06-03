//Tetris Clone - inspired by https://gist.github.com/DataWraith/5236083 


/*
 * Author: anushka dole
 * date: 6/2
 * purpose: tetris clone that (mostly :DDDD) works
 * 
 */


import javax.swing.*; // JFrame, JPanel, JButton, JComponent
import java.awt.*; // Graphics
import java.awt.event.*; // ActionListener, ActionEvent
import javax.imageio.ImageIO;
import java.io.*; // File, IOException
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class Tetris extends JPanel implements ActionListener, KeyListener{
    private int score;
    private final int rows = 20;
    private final int cols = 10;
    private final int cellSize = 30;
    private int verticalShift;
    private int pause;
    private int horizontalShift;
    private boolean isGameOver = false;
    private boolean[][] boardState;
    boolean currentPieceOver;
    private int CRI;
    private Color currentColor;
    private final Color[] colors = {Color.BLUE, Color.CYAN, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.RED, new Color(128, 0, 128), Color.YELLOW};
    private Color[][] board; 
    Timer timer;
    // I, O, T, S, Z, J, and L
    public enum tetrominoe {
        I_SHAPE,
        O_SHAPE,
        L_SHAPE,
        T_SHAPE,
        S_SHAPE,
        Z_SHAPE,
        J_SHAPE
    }

    public enum rotation {
        DEG90,
        DEG180,
        DEG270,
        DEG360
    }

    private rotation currentRotation;
    private final rotation[] rotationOptions = {rotation.DEG90, rotation.DEG180, rotation.DEG270, rotation.DEG360};
    private final tetrominoe[] options = {tetrominoe.I_SHAPE, tetrominoe.O_SHAPE, tetrominoe.L_SHAPE, tetrominoe.T_SHAPE, tetrominoe.S_SHAPE, tetrominoe.Z_SHAPE, tetrominoe.J_SHAPE};
    private tetrominoe currentPiece;

    public Tetris() {
        boardState = new boolean[20][10];
        //true: occupied. false: empty

        this.board = new Color[20][10];
        int random = (int) (Math.random() * 7);
        currentPiece = options[random];
        CRI = (int) (Math.random() * 4);
        currentRotation = rotationOptions[CRI];

        for(int i = 0; i < boardState.length; i++) {
            for(int j = 0; j< boardState[i].length; j++) {
                boardState[i][j] = false;
            }
        }

        setBackground(Color.BLACK);
        JTextArea bruh = new JTextArea(1,1);
        add(bruh);
        bruh.addKeyListener(this);
        pause = 100; // how many milliseconds after this code runs should the timer first run actionPerformed?
        int speed = 0; // how many milliseconds after the previous actionPerformed should the next one run?
        currentColor = Color.GREEN;
        timer = new Timer(speed, this);
        timer.setInitialDelay(pause);
        timer.start();
    }


    private void drawPieces(tetrominoe t, Graphics g) {
        g.setColor(currentColor);
        try {
            switch(t) {
                case T_SHAPE:
                    switch(currentRotation) {
                        case DEG90: 
                            if(horizontalShift>5) horizontalShift =5;
                            if(horizontalShift<-4) horizontalShift =-4;                       
                            if(nextPosAvailable(0, 4) && nextPosAvailable(1, 3) && nextPosAvailable(1, 4) && nextPosAvailable(2, 4)) {
                                g.fillRect((4 + horizontalShift) * cellSize, (0 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((3 + horizontalShift) * cellSize, (1 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (1 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (2 + verticalShift) * cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>17) verticalShift = 18;
                                board[0 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][3 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][4 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                        case DEG180:
                            if(horizontalShift>4) horizontalShift =4;
                            if(horizontalShift<-3) horizontalShift =-3;                       
                            if(nextPosAvailable(1, 3) && nextPosAvailable(1, 4) && nextPosAvailable(1, 5) && nextPosAvailable(0, 4)) {
                                g.fillRect((3 + horizontalShift) * cellSize, (1 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (1 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((5 + horizontalShift) * cellSize, (1 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (0 + verticalShift) * cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>18) verticalShift = 19;
                                board[1 + verticalShift-1][3 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][4 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                        case DEG360:
                            if(horizontalShift>4) horizontalShift =4;
                            if(horizontalShift<-5) horizontalShift =-5;                           
                            if(nextPosAvailable(0, 3) && nextPosAvailable(0, 4) && nextPosAvailable(0, 5) && nextPosAvailable(1, 4)) {
                                g.fillRect((3 + horizontalShift) * cellSize, (0 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (0 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((5 + horizontalShift) * cellSize, (0 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (1 + verticalShift) * cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>18) verticalShift = 19;
                                board[0 + verticalShift-1][3 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                        case DEG270:
                            if(horizontalShift>4) horizontalShift =4;
                            if(horizontalShift<-4) horizontalShift =-4;  
                            if(nextPosAvailable(0, 4) && nextPosAvailable(1, 5) && nextPosAvailable(1, 4) && nextPosAvailable(2, 4)) {
                                g.fillRect((4 + horizontalShift) * cellSize, (0 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((5 + horizontalShift) * cellSize, (1 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (1 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (2 + verticalShift) * cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>17) verticalShift = 18;
                                board[0 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][4 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                        }
                    break;
    
                case I_SHAPE:
                    switch(currentRotation) {
                        case DEG360:
                        case DEG180:
                            if(horizontalShift>3) horizontalShift=3;
                            if(horizontalShift<-3) horizontalShift =-3;  
                            if(nextPosAvailable(0, 3) && nextPosAvailable(0, 4) && nextPosAvailable(0, 5) && nextPosAvailable(0, 6)) {
                                g.fillRect((3+horizontalShift) * cellSize, (0+verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (0 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((5 + horizontalShift) * cellSize, (0 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((6 + horizontalShift) * cellSize, (0 + verticalShift) * cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>19) verticalShift =20;
                                board[0 + verticalShift-1][3 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][6 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                        case DEG90:
                        case DEG270:
                            if(horizontalShift>5) horizontalShift=5;
                            if(horizontalShift<-4) horizontalShift =-4;  
                            if(nextPosAvailable(0, 4) && nextPosAvailable(1, 4) && nextPosAvailable(2, 4) && nextPosAvailable(3, 4)) {
                                g.fillRect((4+horizontalShift) * cellSize, (0+verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (1 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (2 + verticalShift) * cellSize, cellSize, cellSize);
                                g.fillRect((4 + horizontalShift) * cellSize, (3 + verticalShift) * cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>16) verticalShift = 17;
                                board[0 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[3 + verticalShift-1][4 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                    }
                    break;
    
                case O_SHAPE:
                    switch(currentRotation) {
                        case DEG90:
                        case DEG180:
                        case DEG270:
                        case DEG360:
                            if(horizontalShift>4) horizontalShift=4;
                            if(horizontalShift<-4) horizontalShift =-4; 
                            if(nextPosAvailable(0, 4) && nextPosAvailable(1, 4) && nextPosAvailable(0, 5) && nextPosAvailable(1, 5)) {
                                g.fillRect((4+horizontalShift) *cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((4+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>18) verticalShift = 19;
                                board[0 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                    }
                    break;
                
                case J_SHAPE:
                    switch(currentRotation) {
                        case DEG90:
                            if(horizontalShift>3) horizontalShift=3;
                            if(horizontalShift<-4) horizontalShift =-4;  
                            if(nextPosAvailable(1, 4) && nextPosAvailable(1, 5) && nextPosAvailable(1, 6) && nextPosAvailable(2, 6)) {
                                g.fillRect((4+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>18) verticalShift = 19;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][6 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][6 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                        
                        case DEG180:
                            if(horizontalShift>3) horizontalShift =3;
                            if(horizontalShift<-4) horizontalShift =-4; 
                            if(nextPosAvailable(0, 5) && nextPosAvailable(1, 5) && nextPosAvailable(2, 5) && nextPosAvailable(0, 6)) {
                                g.fillRect((5+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>17) verticalShift =18;
                                board[0 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][6 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;

                        case DEG270:
                        if(horizontalShift>3) horizontalShift =3;
                        if(horizontalShift<-4) horizontalShift =-4; 
                            if(nextPosAvailable(1, 4) && nextPosAvailable(2, 4) && nextPosAvailable(2, 5) && nextPosAvailable(2, 6)) {
                                g.fillRect((4+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((4+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>18) verticalShift = 19;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][6 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;

                        case DEG360:
                            if(horizontalShift>4) horizontalShift =4;
                            if(horizontalShift<-4) horizontalShift =-4; 
                            if(nextPosAvailable(0, 5) && nextPosAvailable(1, 5) && nextPosAvailable(2, 5) && nextPosAvailable(2, 4)) {
                                g.fillRect((5+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((4+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>17) verticalShift =18;
                                board[0 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][4 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                    }
                    break;
                
                case L_SHAPE:
                    switch(currentRotation) {
                        case DEG90:
                        if(horizontalShift>3) horizontalShift =3;
                        if(horizontalShift<-4) horizontalShift =-4; 
                            if(nextPosAvailable(2, 4) && nextPosAvailable(2, 5) && nextPosAvailable(2, 6) && nextPosAvailable(1, 6)) {
                                g.fillRect((4+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>18) verticalShift = 19;
                                board[2 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][6 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][6 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                        case DEG180:
                        if(horizontalShift>3) horizontalShift =3;
                        if(horizontalShift<-4) horizontalShift =-4; 
                            if(nextPosAvailable(0, 5) && nextPosAvailable(0, 6) && nextPosAvailable(1, 6) && nextPosAvailable(2, 6)) {
                                g.fillRect((5+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>17) verticalShift = 18;
                                board[0 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][6 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][6 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][6 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                        case DEG270:
                        if(horizontalShift>3) horizontalShift =3;
                        if(horizontalShift<-4) horizontalShift =-4; 
                            if(nextPosAvailable(1, 4) && nextPosAvailable(1, 5) && nextPosAvailable(1, 6) && nextPosAvailable(2, 4)) {
                                g.fillRect((4+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((4+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>18) verticalShift = 19;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][6 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][4 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                        case DEG360:
                        if(horizontalShift>3) horizontalShift =3;
                        if(horizontalShift<-4) horizontalShift =-4; 
                            if(nextPosAvailable(0, 5) && nextPosAvailable(1, 5) && nextPosAvailable(2, 5) && nextPosAvailable(2, 6)) {
                                g.fillRect((5+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>17) verticalShift = 18;
                                board[0 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][6 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                    }
                    break;
                
                case S_SHAPE:
                    switch(currentRotation) {
                        case DEG90:
                        case DEG270:
                        if(horizontalShift>3) horizontalShift =3;
                        if(horizontalShift<-5) horizontalShift =-5; 
                            if(nextPosAvailable(0, 5) && nextPosAvailable(1, 5) && nextPosAvailable(1, 6) && nextPosAvailable(2, 6)) {
                                g.fillRect((5+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
            
                            drawGridLines(g);
                            } else {
                                if(verticalShift>17) verticalShift =18;
                                board[0 + verticalShift -1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][6 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][6 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                        case DEG180:
                        case DEG360:
                        if(horizontalShift>3) horizontalShift =3;
                        if(horizontalShift<-3) horizontalShift =-3; 
                            if(nextPosAvailable(0, 5) && nextPosAvailable(0, 6) && nextPosAvailable(1, 4) && nextPosAvailable(1, 5)) {
                                g.fillRect((5+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((4+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
            
                            drawGridLines(g);
                            } else {
                                if(verticalShift>18) verticalShift =19;
                                board[0 + verticalShift -1][5 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][6 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                    }
                    break;
    
                case Z_SHAPE:

                    switch(currentRotation) {
                        case DEG90:
                        case DEG270:
                        if(horizontalShift>4) horizontalShift =4;
                        if(horizontalShift<-4) horizontalShift =-4; 
                            if(nextPosAvailable(0, 5) && nextPosAvailable(1, 5) && nextPosAvailable(1, 4) && nextPosAvailable(2, 4)) {
                                g.fillRect((5+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((4+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((4+horizontalShift)*cellSize, (2+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>17) verticalShift =18;
                                board[0 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[2 + verticalShift-1][4 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;

                        case DEG180:
                        case DEG360:
                        if(horizontalShift>5) horizontalShift =5;
                        if(horizontalShift<-4) horizontalShift =-4; 
                            if(nextPosAvailable(0, 4) && nextPosAvailable(0, 5) && nextPosAvailable(1, 5) && nextPosAvailable(1, 6)) {
                                g.fillRect((4+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (0+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((5+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                g.fillRect((6+horizontalShift)*cellSize, (1+verticalShift)*cellSize, cellSize, cellSize);
                                drawGridLines(g);
                            } else {
                                if(verticalShift>18) verticalShift =19;
                                board[0 + verticalShift-1][4 + horizontalShift] = currentColor;
                                board[0 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][5 + horizontalShift] = currentColor;
                                board[1 + verticalShift-1][6 + horizontalShift] = currentColor;
                                currentPieceOver = true;
                            }
                            break;
                    }
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            isGameOver = true;
        }
    }

    private void generateNewPiece() {
        score += 5;
        fixPieceToBoard();
        int random = (int) (Math.random() * 7);
        currentColor = colors[random];
        currentPiece = options[random];
        verticalShift = 0;
        horizontalShift = 0;
        currentPieceOver = false;
    }

        public void clearLines() {
            int height = boardState.length;
            int width = boardState[0].length;
            
            ArrayList<Integer> fullRows = new ArrayList<>();
            
            for (int i = 0; i < height; i++) {
                boolean isFull = true;
                for (int j = 0; j < width; j++) {
                    if (!boardState[i][j]) {
                        isFull = false;
                        break;
                    }
                }
                if (isFull) {
                    fullRows.add(i);
                }
            }
            
            for (int row : fullRows) {
                for (int i = row; i > 0; i--) {
                    for (int j = 0; j < width; j++) {
                        boardState[i][j] = boardState[i-1][j];
                        board[i][j] = board[i-1][j];
                    }
                }
                score+=100;

                for (int j = 0; j < width; j++) {
                    boardState[0][j] = false;
                }
            }
    }

    private void fixPieceToBoard() {
        for(int i = 0; i < boardState.length; i++) {
            for(int j = 0; j < boardState[i].length; j++) {
                if(board[i][j] != null && !board[i][j].equals(Color.BLACK)) 
                    boardState[i][j] = true;
            }
        }
    }

    private boolean nextPosAvailable(int initialVertical, int initialHorizontal) {
        try {
            if(boardState[initialVertical+verticalShift][initialHorizontal+horizontalShift] == false)
                return true;
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private void drawBoard(Graphics g) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] != null && boardState[i][j]==true) {
                    g.setColor(board[i][j]);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    private void drawGridLines(Graphics g) {
        g.setColor(Color.GRAY);
                for (int i = 0; i <= rows; i++) {
                    g.drawLine(0, i * cellSize, cols * cellSize, i * cellSize);
                }
                for (int i = 0; i <= cols; i++) {
                    g.drawLine(i * cellSize, 0, i * cellSize, rows * cellSize);
                }
    }

      public void keyTyped(KeyEvent e) {
        displayInfo(e, "KEY TYPED: ");
      }

    public void keyPressed(KeyEvent e) {
        displayInfo(e, "KEY PRESSED: ");
        int keyCode = e.getKeyCode();
        String key = KeyEvent.getKeyText(keyCode);
        if(key.equalsIgnoreCase("left")) horizontalShift--;
        else if(key.equalsIgnoreCase("right")) horizontalShift++;
        else if(key.equalsIgnoreCase("up")) {
            CRI = CRI+1==4 ? 0 : CRI+1;
            currentRotation = rotationOptions[CRI];
        } else if(key.equalsIgnoreCase("down")) {
            timer.setDelay(pause/4);
        }
    }

    public void keyReleased(KeyEvent e) {
        displayInfo(e, "KEY RELEASED: ");
        int keyCode = e.getKeyCode();
        String key = KeyEvent.getKeyText(keyCode);

        if(key.equalsIgnoreCase("down")) {
            timer.setDelay(pause);
        }
    }

    private void displayInfo(KeyEvent e, String keyStatus){
        //You should only rely on the key char if the event
        //is a key typed event.
        int id = e.getID();
        String keyString;
        if (id == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            keyString = "key character = '" + c + "'";
        } else {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")";
            //System.out.println(keyString);
        }
 
        int modifiersEx = e.getModifiersEx();
        String modString = "extended modifiers = " + modifiersEx;
        String tmpString = KeyEvent.getModifiersExText(modifiersEx);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no extended modifiers)";
        }
 
        String actionString = "action key? ";
        if (e.isActionKey()) {
            actionString += "YES";
        } else {
            actionString += "NO";
        }

        String locationString = "key location: ";
        int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "standard";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "left";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "right";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }
 

        //Display information about the KeyEvent...
       // System.out.println(keyString + "\n\t" + modString + "\n\t" + actionString + "\n\t" + locationString);
    }

    public Dimension getPreferredSize() {
        return new Dimension(cols * cellSize, rows * cellSize);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(isGameOver) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER. Score: " + score, getWidth()/2,getHeight()/2);
        } else {
            drawBoard(g);
            drawGridLines(g);
            drawPieces(currentPiece, g);
            clearLines();
            verticalShift++;
        }
    }

     public void actionPerformed(ActionEvent e) {
        if(currentPieceOver)
            generateNewPiece();
        repaint();
        timer.restart(); 
    }

       public static void createAndShowGUI() {
            JFrame frame = new JFrame("Tetris World");
            Tetris tetrisPanel = new Tetris(); 
            frame.add(tetrisPanel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
     }

       public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(
            new Runnable(){
               public void run() {
                  createAndShowGUI();
               }
            });
       }
}
