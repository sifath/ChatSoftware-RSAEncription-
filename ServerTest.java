
import javax.swing.JFrame;


public class ServerTest {
    public static void main(String[] args) {
        Server serverob=new Server();
        serverob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverob.startRunning();
    }
}
