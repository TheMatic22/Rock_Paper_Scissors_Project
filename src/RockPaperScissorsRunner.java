import javax.swing.JFrame;
public class RockPaperScissorsRunner {
    public static void main(String[] args){
        RockPaperScissorsFrame frame = new RockPaperScissorsFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650,500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
