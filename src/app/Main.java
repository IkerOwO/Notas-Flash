import utils.Front; 
import java.awt.Color;
import javax.swing.JFrame;

public class Main{
    public static void main(String[] args){
        Front front = new Front();
        front.setTitle("Notas Flash");
        front.setSize(550,600);
        front.setVisible(true);
        front.getContentPane().setBackground(Color.WHITE);
        front.setResizable(false);
        front.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
