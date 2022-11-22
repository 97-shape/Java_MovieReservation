/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package moviereservation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author 엄상욱
 */
public class PosterPanel extends javax.swing.JPanel {
    /**
     * Creates new form PosterPanel
     */
    
    private Image img;
    Dimension d = new Dimension(294, 420);
    
    public void nextImg(String name){
        try{
            this.img = ImageIO.read(MainFrame.class.getResource(String.format("/img/%s.jpg", name)));
            this.setSize(d);
            repaint();
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
	
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(img, 0, 0, d.width, d.height, null);
    }
    // public PosterPanel(Image img) {
    public PosterPanel(String name) {
        this.nextImg(name);
        this.setSize(d);
        
        setLayout(null);
        setVisible(true);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
