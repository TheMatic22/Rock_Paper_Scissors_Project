import java.util.Random;
public class RandomStrat implements Strategy{
    private final Random rand = new Random();

    public String getMove(String playerMove){
        String[] moves = {"R","P","S"};
        return moves[rand.nextInt(3)];
    }
}
