package oldsynsim;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Simulation
{
  public static void main(String[] args)
  {
    JFrame window = new JFrame();
    window.setDefaultCloseOperation(3);
    Dimension windowSize = new Dimension(500, 500);
    int scale = 1;
    SimulationPanel s = new SimulationPanel(windowSize, scale);
    window.getContentPane().add(s);
    window.setSize(windowSize);
    window.setVisible(true);
    int i = 0;
    while (true) {
      s.update();
      s.savePaint(i++);
      window.repaint();
    }
  }
}