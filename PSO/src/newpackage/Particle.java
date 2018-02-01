/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

/**
 *
 * @author Riddhi
 */
public class Particle {
    
    
    private double[] position;
    private double[] velocity;
    private double cost;
 private Best partclebest;

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Best getPartclebest() {
        return partclebest;
    }

    public void setPartclebest(Best partclebest) {
        this.partclebest = partclebest;
    }

    
    
    
    
    
}
