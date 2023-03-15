/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package moviereservation;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author umsang-uk
 */
public class HomeFrame extends javax.swing.JFrame {
    DBManager dbManager;
    Image logo;
    
    boolean isLogin = false;
    
    int idx = 0;
    int posterX = 100;
    String user_id;
    // id, name, describe, rank
    String sql_Ranking = 
        "SELECT md.Movie_ID, md.Movie_Name, md.Movie_Describe, md.Movie_Poster, r.rate\n" +
        "FROM reviewview r , Movie_Detail AS md LEFT OUTER JOIN (\n" +
        "SELECT w.Movie_ID, sum(w.Reserved) AS total\n" +
        "FROM watchedview w\n" +
        "GROUP BY w.Movie_ID\n" +
        ") AS c\n" +
        "ON md.Movie_ID = c.Movie_ID\n" +
        "where md.Movie_ID = r.Movie_ID\n" +
        "ORDER BY total DESC;";
    
    ArrayList<String[]> movieRank;
    PosterPanel[] posters = new PosterPanel[4];
    DetailPanel[] details = new DetailPanel[4];
    String[] ID = new String[4];
    /**
     * Creates new form HomeFrame
     */
    
    public HomeFrame(DBManager dbManager, String user_id) {
        this.user_id = user_id;
        this.dbManager = dbManager;
        movieRank = dbManager.executeQuery(sql_Ranking);
        
        initComponents();
        myPageBtn.setVisible(false);
        myPageBtn.setEnabled(true);
        
        try {
            logoPanel.DrawImg(ImageIO.read(HomeFrame.class.getResource("/img/logo.png")));
            logoPanel.d = new Dimension(300, 100);
        } catch (IOException ex) {
            Logger.getLogger(HomeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Thread posterT = new Thread(new display());
        
        
        if (user_id != null){
            isLogin = true;
            logBtn.setText("로그아웃");
            myPageBtn.setVisible(true);
            joinBtn.setVisible(false);
        }
        
        for (int i=0; i<4; i++){
            posters[i] = new PosterPanel();
            details[i] = new DetailPanel(movieRank.get(idx)[2], movieRank.get(idx)[4]);
            posters[i].setDimension(224, 320);
            ID[i] = movieRank.get(idx)[0];
            posters[i].setBounds(posterX, 100, 224, 320);
            details[i].setBounds(posterX, 100, 224, 320);
            //System.out.println(movieRank.get(idx)[2]);
            /*
            try {
                posters[i].DrawImg(ImageIO.read(HomeFrame.class.getResource(String.format("/img/%s.jpg", movieRank.get((idx+i)%movieRank.size())[3]))));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            */
            posters[i].addMouseListener(new PosterEnter());
            details[i].setVisible(false);
            jMainPanel.add(details[i]);
            jMainPanel.add(posters[i]);
            posterX += 256;
            idx++;
        }
        idx = 0;
        reservationBtn.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 if (isLogin){
                     new ReservationFrame(dbManager, user_id).setVisible(true);
                     dispose();
                 }
             }
        });
        posterT.start();
        
    }
    
    public void posterUpdate(){
        int idx2 = idx++;
        for (int i=0; i<4; i++){
            try {
                System.out.println(movieRank.get(idx2)[3]);
                //BufferedImage img = ImageIO.read(HomeFrame.class.getResourceAsStream(String.format("/img/%s.jpg", movieRank.get(idx2)[3])));
                //posters[i].DrawImg(img);
                //Image img = new ImageIcon(getClass().getClassLoader().getResource(String.format("/img/%s.jpg", movieRank.get(idx2)[3]))).getImage();
                //posters[i].DrawImg(img);
                posters[i].DrawImg(ImageIO.read(HomeFrame.class.getResourceAsStream(String.format("/img/%s.jpg", movieRank.get(idx2)[3]))));
            } catch (Exception ex) {
            }
            //posters[i].DrawImg(ImageIO.read(HomeFrame.class.getResource(String.format("/img/%s.jpg", movieRank.get(idx2)[3]))));
            //posters[i].DrawImg(Toolkit.getDefaultToolkit().getImage(getClass().getResource(String.format("/img/%s.jpg", movieRank.get(idx2)[3]))));
            ID[i] = movieRank.get(idx2)[0];
            details[i].UpdatePanel(movieRank.get(idx2)[2], movieRank.get(idx2++)[4]);
            details[i].setVisible(false);
            if (idx2 >= movieRank.size()){
                idx2 = 0;
            }
        } 
        if (idx >= movieRank.size()){
            idx = 0;
        }
    }
    
    public class display implements Runnable{

        @Override
        public void run() {
            try{
                while (true){
                    posterUpdate();
                    Thread.sleep(10000);
                }
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLogoPanel = new javax.swing.JPanel();
        logBtn = new javax.swing.JButton();
        joinBtn = new javax.swing.JButton();
        logoPanel = new moviereservation.PosterPanel();
        myPageBtn = new javax.swing.JButton();
        jMainPanel = new javax.swing.JPanel();
        reservationBtn = new javax.swing.JToggleButton();
        movieListBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 800));
        setResizable(false);

        jLogoPanel.setBackground(new java.awt.Color(255, 255, 255));

        logBtn.setText("로그인");
        logBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logBtnActionPerformed(evt);
            }
        });

        joinBtn.setText("회원가입");
        joinBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout logoPanelLayout = new javax.swing.GroupLayout(logoPanel);
        logoPanel.setLayout(logoPanelLayout);
        logoPanelLayout.setHorizontalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        logoPanelLayout.setVerticalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        myPageBtn.setText("마이페이지");
        myPageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myPageBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jLogoPanelLayout = new javax.swing.GroupLayout(jLogoPanel);
        jLogoPanel.setLayout(jLogoPanelLayout);
        jLogoPanelLayout.setHorizontalGroup(
            jLogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLogoPanelLayout.createSequentialGroup()
                .addContainerGap(447, Short.MAX_VALUE)
                .addComponent(logoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(180, 180, 180)
                .addComponent(myPageBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(joinBtn)
                .addGap(30, 30, 30))
        );
        jLogoPanelLayout.setVerticalGroup(
            jLogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLogoPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jLogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(logBtn)
                        .addComponent(joinBtn)
                        .addComponent(myPageBtn))
                    .addComponent(logoPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jLogoPanel, java.awt.BorderLayout.PAGE_START);

        jMainPanel.setBackground(new java.awt.Color(255, 255, 255));

        reservationBtn.setBackground(new java.awt.Color(204, 204, 255));
        reservationBtn.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        reservationBtn.setText("예약하기");
        reservationBtn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        reservationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reservationBtnActionPerformed(evt);
            }
        });

        movieListBtn.setText("전체 영화");
        movieListBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                movieListBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jMainPanelLayout = new javax.swing.GroupLayout(jMainPanel);
        jMainPanel.setLayout(jMainPanelLayout);
        jMainPanelLayout.setHorizontalGroup(
            jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMainPanelLayout.createSequentialGroup()
                .addGap(518, 518, 518)
                .addComponent(reservationBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(526, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jMainPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(movieListBtn)
                .addGap(107, 107, 107))
        );
        jMainPanelLayout.setVerticalGroup(
            jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jMainPanelLayout.createSequentialGroup()
                .addContainerGap(432, Short.MAX_VALUE)
                .addComponent(movieListBtn)
                .addGap(30, 30, 30)
                .addComponent(reservationBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(185, 185, 185))
        );

        getContentPane().add(jMainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void reservationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reservationBtnActionPerformed
        // TODO add your handling code here:
        if (!isLogin){
            LoginDlg loginDlg = new LoginDlg(this, true, dbManager);
            loginDlg.setVisible(true);
            if (loginDlg.isLoginSuccess == true) {
                logBtn.setText("로그아웃");
                isLogin = true;
                user_id = loginDlg.id;
                //System.out.println(user_id);
            }
        }
        if (isLogin){
            new ReservationFrame(dbManager, user_id).setVisible(true);
            dispose();
        }
        
    }//GEN-LAST:event_reservationBtnActionPerformed

    private void logBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logBtnActionPerformed
        // TODO add your handling code here:
        System.out.println(evt.getActionCommand());
        if (evt.getActionCommand().equals("로그인")){
            LoginDlg loginDlg = new LoginDlg(this, true, dbManager);
            loginDlg.setVisible(true);
            if (loginDlg.isLoginSuccess == true) {
                logBtn.setText("로그아웃");
                joinBtn.setVisible(false);
                myPageBtn.setVisible(true);
                isLogin = true;
                user_id = loginDlg.id;
                System.out.println(user_id);
            }
        }else if (evt.getActionCommand().equals("로그아웃")){
            logBtn.setText("로그인");
            joinBtn.setVisible(true);
            myPageBtn.setVisible(false);
            user_id = null;
            isLogin = false;
        }
    }//GEN-LAST:event_logBtnActionPerformed

    private void joinBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinBtnActionPerformed
        // TODO add your handling code here:
        JoinDlg joinDlg = new JoinDlg(true, dbManager);
        joinDlg.setVisible(true);
    }//GEN-LAST:event_joinBtnActionPerformed

    private void movieListBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_movieListBtnActionPerformed
        // TODO add your handling code here:
        new MovieListFrame(movieRank, dbManager, isLogin, user_id).setVisible(true);
        dispose();
    }//GEN-LAST:event_movieListBtnActionPerformed

    private void myPageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myPageBtnActionPerformed
        // TODO add your handling code here:
        new MyPageFrame(dbManager, user_id).setVisible(true);
    }//GEN-LAST:event_myPageBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeFrame(DBManagerMySql.getinstance(), null).setVisible(true);
            }
        });
    }
    
    class PosterEnter implements MouseListener{
        int index=0;
        JPanel panel;
        
        @Override
        public void mouseExited(MouseEvent e) {
            //System.out.println(e.getXOnScreen());
            panel = (JPanel)e.getSource();
            //System.out.println(panel.getX());
            index = panel.getX();
            //System.out.println(prev + " " + idx/256);
            details[index/256].setVisible(false);
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            //System.out.println(e.getSource());
            panel = (JPanel)e.getSource();
            index = panel.getX();
            //System.out.println(prev + " " + idx/256);
            details[index/256].setVisible(true);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            panel = (JPanel)e.getSource();
            index = panel.getX()/256;
            System.out.println(index);
            new MovieDetailFrame(dbManager, ID[index], isLogin, "test").setVisible(true);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {   
        }
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jLogoPanel;
    private javax.swing.JPanel jMainPanel;
    private javax.swing.JButton joinBtn;
    private javax.swing.JButton logBtn;
    private moviereservation.PosterPanel logoPanel;
    private javax.swing.JButton movieListBtn;
    private javax.swing.JButton myPageBtn;
    private javax.swing.JToggleButton reservationBtn;
    // End of variables declaration//GEN-END:variables
}
