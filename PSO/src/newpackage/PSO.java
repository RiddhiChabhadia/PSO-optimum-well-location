/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.applet.Applet;
import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;

/**
 *
 * @author Riddhi
 */
public class PSO extends Applet implements Runnable {
  
    Color[] colors = new Color[]{BLUE}; // array holding available colors

    static int width, height; // variables for applet dimensions

    int particleCount; 
    static int nVar=2;
    static double[]  varSize=new double[nVar];
    static int varMin=-10;
    static int varMax=10;
    static int maxIt=100;
    static int nPop=50;
   static double w=1;
   static double c1=2;
  static  double c2=2;
    public static Fitness f=new Fitness();
    public static Best gbest=new Best();
  static   double wdamp = 0.99;  
      static Best bp = new Best();
   static  ParticleList partiList=new ParticleList();
   
       public void init() {
        initialize();
        start();
        width = 10;
        height = 10;
    }

    public void start() {
        for (Particle p : partiList.getParticleList()) {
            Thread t;
            t = new Thread(this);
            t.start();
        } // end for
    }
    public static void initialize(){
          gbest.setCost(Double.POSITIVE_INFINITY);
           
    double[] initialVelocity = {0, 0, 0, 0, 0};
    for(int i=0;i<nPop;i++){
        Particle p = new Particle();
            double[] position = new double[nVar];
            double[] velocity = new double[nVar];
            p.setPosition(position);
            p.setVelocity(velocity);
            p.setPartclebest(bp);
            p.getPartclebest().setPostion(position);
            partiList.getParticleList().add(p);}

    
  // p.getPartclebest().setPostion(p.getPosition());
    //p.getPartclebest().setCost(p.getCost());
    
   
     //double[] initialVelocity = {0, 0, 0, 0, 0};

        for (int i = 1; i < nPop; i++) {
            //Generate Random Position
            double[] pstn = doubles(nVar, varMin, varMax);
            //
            partiList.getParticleList().get(i).setPosition(pstn);

            //Evaluate Cost
            partiList.getParticleList().get(i).setCost(f.cost(partiList.getParticleList().get(i)));

            //Initialize Velocity
            partiList.getParticleList().get(i).setVelocity(initialVelocity);

            //Update the Initial Personal Best
            bp.setPostion(partiList.getParticleList().get(i).getPosition());
            bp.setCost(partiList.getParticleList().get(i).getCost());
            partiList.getParticleList().get(i).setPartclebest(bp);
            //Update Global Best
            if (partiList.getParticleList().get(i).getCost() < gbest.getCost()) {
                gbest.setCost(partiList.getParticleList().get(i).getCost());
                gbest.setPostion(partiList.getParticleList().get(i).getPosition());      //Check if this is needed later on
            }
        }

    
  
    
    
     
    
  
    //particle array printing 
    for(Particle p:partiList.getParticleList()){
        System.out.println(p.getCost()+"Cost evaluation");
          double[] arr = p.getPosition();
            System.out.println("");
            for (int i = 0; i < arr.length; i++) {
                System.out.print("" + arr[i]);
                System.out.print("\t");
            }
    }
    
    //PSO function
    
    
    
    }
    
    
//    public static void main(String args[]){
//    initialize();
//    
//    }
    public static double[] updateParticleVelocity(int particlenum, ParticleList pl, double w, double c1, double c2, Best gBest, double[] VarSize, int swarmsize) {
        double[] vel = new double[nVar];
        Random generator = new Random();
        double[] sub1 = new double[nVar];
        double[] sub2 = new double[nVar];
        Arrays.setAll(VarSize, x -> {
            return generator.nextDouble();
        });
        double[] result2 = DoubleStream.of(VarSize).map(d1 -> d1 * c1).toArray();
        double[] result3 = DoubleStream.of(VarSize).map(d1 -> d1 * c2).toArray();
        double[] result1 = DoubleStream.of(pl.getParticleList().get(particlenum).getVelocity()).map(d -> d * w).toArray();

        for (int j = 0; j < nVar; j++) {
            sub1[j] = ((pl.getParticleList().get(particlenum).getPartclebest().getPostion())[j]
                    - (pl.getParticleList().get(particlenum).getPosition())[j]);
            sub2[j] = (gbest.getPostion())[j] - (pl.getParticleList().get(j).getPosition())[j];
        }

        for (int j = 0; j < nVar; j++) {
            result2[j] = result2[j] * sub1[j];
            result3[j] = result3[j] * sub2[j];
        }

        for (int j = 0; j < nVar; j++) {
            vel[j] = result1[j] + result2[j] + result3[j];
        }

        return vel;
    }
  
   
        public static double[] doubles(int noOfVariables, int VarMin, int VarMax) {
        Random r = new Random();
        return r.doubles(noOfVariables, VarMin, VarMax).toArray();
    }
    
          private static double[] updateParticlePosition(int particle, ParticleList pl) {
        double[] pos = new double[nVar];
        for (int i = 0; i < nVar; i++) {
            pos[i] = (pl.getParticleList().get(particle).getPosition())[i] + (pl.getParticleList().get(particle).getVelocity())[i];
        }
        return pos;
    }


    public void run() {
          double[] BestCosts = new double[maxIt];
        Arrays.fill(BestCosts, 0);
        
        
         for(int i=0;i<maxIt;i++){
        System.out.println("Iteration"+maxIt);
        
        for(int j=0;j<nPop;j++){
        
          double[] vel = updateParticleVelocity(j, partiList, w, c1, c2, gbest, varSize, nPop);
                partiList.getParticleList().get(j).setVelocity(vel);

                //Update Position of particle in Global Best Direction
                double[] pos = updateParticlePosition(j, partiList);
                partiList.getParticleList().get(j).setPosition(pos);

                //Evaluate Cost for the particles
                partiList.getParticleList().get(j).setCost(f.cost(partiList.getParticleList().get(j)));

                //Update Personal Best 
                if (partiList.getParticleList().get(j).getCost() < partiList.getParticleList().get(j).getPartclebest().getCost()) {
                    partiList.getParticleList().get(j).getPartclebest().setPostion(partiList.getParticleList().get(j).getPosition());
                    partiList.getParticleList().get(j).getPartclebest().setCost(partiList.getParticleList().get(j).getCost());

                    //Update Global Best
                    if (partiList.getParticleList().get(j).getCost() < gbest.getCost()) {
                        gbest.setCost(partiList.getParticleList().get(j).getCost());
                        gbest.setPostion(partiList.getParticleList().get(j).getPosition());   //Check if this is needed later on
                    }
                }
        
        
        
        }
    
    BestCosts[i] = gbest.getCost();
            System.out.println("Iteration:\t"+i+"\tCost:\t"+BestCosts[i]);
            
            //Update Interia Coefficient
            w = w * wdamp;
            try {
                Thread.sleep(600); // wait 600 msec before continuing
            } catch (InterruptedException e) {
                return;
            }
    repaint();
    
    }
    
    //To change body of generated methods, choose Tools | Templates.
    }
      public void paint(Graphics g) {
        resize(1000, 1000);
        setBackground(BLACK);
        super.paint(g);
        int gbestx = (int) gbest.getPostion()[0];
        int gbesty = (int) gbest.getPostion()[1];
        g.setColor(YELLOW);
        g.fillOval(gbestx, gbesty, 10, 10);
        g.setColor(GREEN);
        g.fillRect(0, 0, 30, 30);
        int i = 0;
        for (Particle p : partiList.getParticleList()) {

            // set current color
            // draw filled oval using current x and y coordinates and diameter
            double[] loc = p.getPosition();

            int x = Integer.valueOf((int) Math.round(loc[0]));
            int y = Integer.valueOf((int) Math.round(loc[0]));
            System.out.println("X Coords:\t" + x);
            System.out.println("Y Coords:\t" + y);
            g.setColor(WHITE);
//            int pbestx = (int) pBestLocation.get(i).getLoc()[0];
//            int pbesty = (int) pBestLocation.get(i).getLoc()[1];
//            g.drawLine(pbestx, pbesty, gbestx, gbesty);
//            g.setColor(RED);
//            g.drawLine(x, y, pbestx, pbesty);
//            g.setColor(PINK);
//            g.drawLine(x, y, gbestx, gbesty);
//            g.setColor(BLUE);
            g.fillOval(x, y, 20, 20);
            i++;

        }
    }
   
    
}
