package byog.Core;
import byog.TileEngine.*;

public class MapLauncher {
    public static final int WIDTH = 70;
    public static final int HEIGHT = 40;

    public static void main(String[] args){
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Map m = new Map(WIDTH,HEIGHT,8547);
        TETile[][] map = m.generate();
        ter.renderFrame(map);

    }

}
