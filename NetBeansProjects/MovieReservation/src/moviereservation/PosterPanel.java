/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package moviereservation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author umsang-uk
 */
public class PosterPanel extends javax.swing.JPanel {
    Dimension d; // 패널 크기 받아오는 변수 Dimension(int x, inty);
    Image img; // 이미지 받아오는 변수
    /**
     * Creates new form PosterPanel
     */
    
    /* 패널에 이미지 그리기, 해당하는 이미지는 프레임에서 따로 받아온다.
     * 패널명.DrawImg(불러올 이미지 경로)
     * 예시)
     * posterPanel.DrawImg(ImageIO.read(SeatFrame.class.getResource(String.format("/img/%s.jpg", data[11]))));
     */
    public void DrawImg(Image img){
        try{
            this.img = img;
            //System.out.println(img);    
            repaint();
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    // 패널 크기 지정하기, 위와 마찬가지로 크기를 넣어주면 됨.
    // posterPanel.setDimension(150, 180);
    public void setDimension(int x, int y){
        d = new Dimension(x, y);
    }
    
    public void paintComponent(Graphics g){
        if (img == null){
            g.drawRect(0, 0, d.width, d.height);
        }else{
            g.drawImage(img, 0, 0, d.width, d.height, null);
        }
    }
    public PosterPanel() {
        d = new Dimension(224, 320);
        initComponents();
    }
    /*
    public PosterPanel(Dimension d, Image img) {
        this.d = d;
        this.img = img;
        initComponents();
        repaint();
    }
    */
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
            .addGap(0, 448, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    void setBounds(int i, int i0) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
