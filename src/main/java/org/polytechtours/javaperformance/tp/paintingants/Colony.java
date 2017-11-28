package org.polytechtours.javaperformance.tp.paintingants;

/*
 * Colony.java
 *
 * Created on 11 avril 2007, 16:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
import java.util.Vector;

public class Colony implements Runnable {

  private Boolean mustContinue = Boolean.TRUE;
  private Vector<Ant> colonies;
  private PaintingAnts mApplis;

  /** Creates a new instance of Colony */
  public Colony(Vector<Ant> pColonie, PaintingAnts pApplis) {
    colonies = pColonie;
    mApplis = pApplis;
  }

  public void pleaseStop() {
    mustContinue = false;
  }

  @Override
  public void run() {

    while (mustContinue == true) {
      if (!mApplis.getPause()) {
        for (int i = 0; i < colonies.size(); i++) {
          colonies.get(i).deplacer();
          mApplis.compteur();
        }
      } else {
        /*
         * try { Thread.sleep(100); } catch (InterruptedException e) { break; }
         */

      }
    }
  }

}
