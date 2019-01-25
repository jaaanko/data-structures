package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Game {
    TERenderer ter = new TERenderer();
    static final int WIDTH = 70;
    static final int HEIGHT = 40;
    static final int MIDHEIGHT = HEIGHT/2;
    static final int MIDWIDTH = WIDTH/2;
    TETile[][] world;
    boolean gameOver;
    int playerX;
    int playerY;


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        StdDraw.setCanvasSize(WIDTH*16, (HEIGHT+2)*16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT+2);

        drawMenu();
        char input = readMenuInput();
        if(input == 'N'){
            //Get seed and start game
            startGame(getSeed());

        }
        else if(input == 'L') {
            //Load saved game if it exists
        }

    }

    private void startGame(int seed){
        Map m = new Map(WIDTH,HEIGHT,seed);
        ter.initialize(WIDTH, HEIGHT+2);

        world = m.generate();
        playerX = m.playerX;
        playerY = m.playerY;

        ter.renderFrame(world);

        Font smallFont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(smallFont);

        double initialX = 0;
        double initialY = 0;
        double mouseX;
        double mouseY;

        while(!gameOver){
            mouseX = StdDraw.mouseX();
            mouseY = StdDraw.mouseY();
            if((mouseX < WIDTH && mouseY < HEIGHT) && (mouseX != initialX || mouseY != initialY)) {

                TETile t = world[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()];

                StdDraw.pause(50);
                ter.renderFrame(world);

                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.textRight(20, HEIGHT + 1, t.description());

                StdDraw.show();
                initialX = mouseX;
                initialY = mouseY;
            }
            if(StdDraw.hasNextKeyTyped()){
                parseInput(StdDraw.nextKeyTyped());
            }
        }
    }

    private void parseInput(char input){
        if(input == 'w'){
            if(!(world[playerX][playerY+1].equals(Tileset.WALL))){
                world[playerX][playerY+1] = Tileset.PLAYER;
                world[playerX][playerY] = Tileset.FLOOR;
                playerY ++;
            }
        }
        else if(input == 'a'){
            if(!(world[playerX-1][playerY].equals(Tileset.WALL))){
                world[playerX-1][playerY] = Tileset.PLAYER;
                world[playerX][playerY] = Tileset.FLOOR;
                playerX --;
            }
        }
        else if(input == 's'){
            if(!(world[playerX][playerY-1].equals(Tileset.WALL))){
                world[playerX][playerY-1] = Tileset.PLAYER;
                world[playerX][playerY] = Tileset.FLOOR;
                playerY --;
            }
        }
        else if(input == 'd'){
            if(!(world[playerX+1][playerY].equals(Tileset.WALL))){
                world[playerX+1][playerY] = Tileset.PLAYER;
                world[playerX][playerY] = Tileset.FLOOR;
                playerX ++;
            }
        }
        ter.renderFrame(world);
        StdDraw.show();
    }

    private void drawMenu(int x, int y, String s){
        StdDraw.clear(Color.BLACK);
        Font titleFont = new Font("Monaco", Font.BOLD, 30);
        Font smallFont = new Font("Monaco", Font.BOLD, 20);

        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(MIDWIDTH,MIDHEIGHT+10,"GAME (Temporary Name)");

        StdDraw.setFont(smallFont);
        StdDraw.text(MIDWIDTH,MIDHEIGHT,"New Game (N)");
        StdDraw.text(MIDWIDTH,MIDHEIGHT-3,"Load Game(L)");
        StdDraw.text(MIDWIDTH,MIDHEIGHT-6,"Quit (Q)");

        StdDraw.text(x,y,s);

        StdDraw.show();
    }

    private void drawMenu(){
        drawMenu(0,0,"");
    }

    private char readMenuInput(){
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (Character.toUpperCase(input) == 'N') {
                    drawMenu(MIDWIDTH, MIDHEIGHT - 10, "Enter Seed: ");
                    return 'N';
                } else if (Character.toUpperCase(input) == 'L') {
                    drawMenu(MIDWIDTH, MIDHEIGHT - 10, "Load ");
                    return 'L';
                } else if (Character.toUpperCase(input) == 'Q') {
                    System.exit(0);
                }
            }
        }
    }

    private int getSeed(){
        char c;
        StringBuilder sb = new StringBuilder();
        while(true){
            if(StdDraw.hasNextKeyTyped()){
                c = StdDraw.nextKeyTyped();
                if(Character.isDigit(c)){
                    sb.append(c);
                    drawMenu(MIDWIDTH, MIDHEIGHT - 10, "Enter Seed: " + sb);
                }
                else if(Character.toUpperCase(c) == 'S' && sb.length() > 0){
                    break;
                }
            }
        }
        System.out.println(sb);
        return Integer.parseInt(sb.toString());
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        if(Character.toUpperCase(input.charAt(0)) == 'N'){
            if(Character.isDigit(input.charAt(1))){
                if(getStopIndex(input) > 0) {
                    int seed = Integer.parseInt(input.substring(1, getStopIndex(input)));
                    Map m = new Map(WIDTH, HEIGHT, seed);
                    return m.generate();
                }
            }
        }
        else if(Character.toUpperCase(input.charAt(0)) == 'L'){
            System.out.println("LOAD");
        }
        else if(Character.toUpperCase(input.charAt(0)) == 'Q'){
            System.out.println("QUIT");
            System.exit(0);
        }
        return null;
    }

    private int getStopIndex(String input){
        int i;
        boolean found = false;
        for(i=0; i<input.length();i++){
            if(Character.toUpperCase(input.charAt(i)) == 'S'){
                found = true;
                break;
            }
        }
        if(!found){
            return -1;
        }
        return i;
    }

}