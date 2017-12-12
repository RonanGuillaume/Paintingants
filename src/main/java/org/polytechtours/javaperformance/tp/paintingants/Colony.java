package org.polytechtours.javaperformance.tp.paintingants;
import java.util.Vector;

public class Colony{

  private Vector<Ant> ants;
  private PaintingAnts paintingAnts;

  public Colony(Vector<Ant> ants, PaintingAnts paintingAnts) {
    this.ants = ants;
    this.paintingAnts = paintingAnts;
  }

  public void moveAllMyAnts() {
      if (!paintingAnts.getPause()) {
        for (int i = 0; i < ants.size(); i++) {
          ants.get(i).move();
          paintingAnts.incrementCounter();
        }
      }
  }

}
