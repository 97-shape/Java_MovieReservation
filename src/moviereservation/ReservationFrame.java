/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package moviereservation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author umsang-uk
 */
public class ReservationFrame extends javax.swing.JFrame {
    
    private static DBManager dbManager = DBManagerMySql.getinstance();
    String sql=
        "SELECT md.Movie_Name, s.StartDate, s.EndDate, md.Movie_ID\n" +
        "FROM Movie_Detail as md, Screen as s, Theater as t\n" +
        "WHERE s.Movie_ID = md.Movie_ID \n" +
        "AND s.Theater_ID = t.Theater_ID\n" +
        "AND '%s' BETWEEN s.StartDate AND s.EndDate;";
    String sql_theater = 
        "SELECT t.Theater_Region, t.Theater_Name, t.Theater_ID\n" +
        "FROM Screen as s, Theater as t\n" +
        "WHERE s.Movie_ID = %d \n" +
        "AND s.Theater_ID = t.Theater_ID AND\n" +
        "'2022-11-26' BETWEEN s.StartDate AND s.EndDate;";
    String sql_screen = 
        //0 : r.StartTime, 1 : r.EndTime, 2 : r.Movie_Name, 3 : r.Movie_Count, 4 : r.Room_No, 5 : r.Total_Seat, 6 : r.Time_ID, 7:r.Theart_Name, 8:r.Seat_Row
        "SELECT r.StartTime, r.EndTime, r.Movie_Name, r.Movie_Count, r.Room_No, r.Total_Seat, r.Time_ID, r.Theater_Name, r.Seat_Row, r.seat_Col, r.Movie_Date\n" +
        "FROM reservationview r\n" +
        "WHERE r.Theater_Id = %d\n" +
        "AND r.Movie_ID = %d\n" +
        "AND r.Movie_Date = '%s'\n" +
        "AND r.StartTime >= '%d:00:00';";
    String sql_countSeat = 
        "SELECT COUNT(s.Seat_ID)\n" +
        "FROM Reservation r, Seat s\n" +
        "WHERE r.Time_ID = %s\n" +
        "AND r.Reservation_ID = s.Reservation_ID\n" +
        "GROUP BY r.Time_ID;";
    
    
    ArrayList<String[]> movie;
    ArrayList<String[]> theater;
    ArrayList<String[]> ScreenTime;
    ArrayList<String[]> ScreenTimeInfo;
    
    DefaultTableModel modelTable;
    //리스트
    DefaultListModel modelMovie;
    DefaultListModel modelRegion;
    DefaultListModel modelTheater;
    
    int Movie_id;
    int Theater_id;
    //현재 날짜로 기본 설정;
    int year, month, day, day_end;
    LocalDate date = LocalDate.now();
    LocalDate dateS = LocalDate.now();
    LocalTime curr_time = LocalTime.now();
    int yearC = date.getYear();
    int monthC = date.getMonthValue();
    int dayC = date.getDayOfMonth();
    int hour = curr_time.getHour();
    int time = curr_time.getHour();
    JButton[] dateBtn;
    JButton[] timeBtn;
    
    // 영화 목록
    public void movieTable(){
        modelMovie.clear();
        modelRegion.clear();
        modelTheater.clear();
        System.out.println(date);
        movie = dbManager.executeQuery(String.format(sql, dateS));
        for (int i=0; i<movie.size(); i++){
            System.out.println(movie.get(i)[0]);
            if (!modelMovie.contains(movie.get(i)[0]))
                modelMovie.addElement(movie.get(i)[0]);
        }
    }
    public void RegionTable(){
        modelRegion.clear();
        modelTheater.clear();
        theater = dbManager.executeQuery(String.format(sql_theater, Movie_id));
        for (int i=0; i<theater.size(); i++){
            if (!modelRegion.contains(theater.get(i)[0])){
                modelRegion.addElement(theater.get(i)[0]);
            }
        }
    }
    public void TheaterTable(String region){
        modelTheater.clear();
        for (int i=0; i<theater.size(); i++){
            //System.out.println(theater.get(i)[0]);
            //System.out.println(region);
            if (theater.get(i)[0].equals(region))
                modelTheater.addElement(theater.get(i)[1]);
        }
    }
    public void TableUpdate(){
        int seat;
        modelTable.setNumRows(0);
        ScreenTime = dbManager.executeQuery(String.format(sql_screen, Theater_id, Movie_id, dateS, time));
        //System.out.println(String.format(sql_screen, Theater_id, Movie_id, dateS, time) + " " + ScreenTime.size());
        //0 : r.StartTime, 1 : r.EndTime, 2 : r.Movie_Name, 3 : r.Movie_Count, 4 : r.Room_No, 5 : r.Total_Seat, 6 : r.Time_ID, 7:r.Theart_Name, 8:r.Seat_Row
        for (int i=0; i < ScreenTime.size(); i++){
            try{
                ScreenTimeInfo = dbManager.executeQuery(String.format(sql_countSeat, ScreenTime.get(i)[6]));
                seat = Integer.parseInt(ScreenTimeInfo.get(0)[0]);
            }catch(Exception e){
                seat = 0;
            }
            //System.out.println( + " " + ScreenTime.get(i)[2]+ " " + ScreenTime.get(i)[3]+ " " + ScreenTime.get(i)[4]+ " " + ScreenTime.get(i)[5]+ " " + ScreenTime.get(i)[6]);
            modelTable.addRow(new Object[]{ScreenTime.get(i)[0].substring(0, 5) + "~" + ScreenTime.get(i)[1].substring(0, 5), ScreenTime.get(i)[2], ScreenTime.get(i)[4]+ "관\t" + seat+ "/" + ScreenTime.get(i)[5]});
            //ScreenTable.updateUI();
        }
    }
    
    // 날짜
    public void DatePanel(){
        //System.out.print("ne" + day);
        //System.out.println(year + "," + month + "," +  day_end);
        for (int i=0; i < 10; i++){
            if (day > day_end){
                month++;
                day = 1;
            }
            if (month > 12){
                month = 1;
                year += 1;
                day = 1;
            }
            date = LocalDate.of(year, month, day);
            day_end = date.lengthOfMonth();
            if (year != yearC){
                dateBtn[i].setText(String.format("%d/%d/%d", year%100, month, day++));
            }else{
                dateBtn[i].setText(String.format("%d/%d", month, day++));
            }
            
        }
    }
    
    public void DatePanelReverse(){
        day -= 20;
        int tmp = 0;
        if (day < 1){
            month--;
            tmp = day;
        }
        if (month < 1){
            month = 12;
            year -= 1;
        }
        if (month != date.getMonthValue()){
            date = LocalDate.of(year, month, 1);
            day = date.lengthOfMonth()+tmp;
            day_end = date.lengthOfMonth();
        }
        DatePanel();
    }
    // 시간
    
    public void HourPanel(){
        for (int i=0; i<6; i++){
            if (hour > 23){
                timeBtn[i].setEnabled(false);
                timeBtn[i].setText("");
            }else{
                timeBtn[i].setText(Integer.toString(hour++));
                timeBtn[i].setEnabled(true);
            }
        }
    }
    public void HourPanelReverse(){
        //System.out.print("2 " + Integer.parseInt(timeBtn[0].getText()));
        hour = Integer.parseInt(timeBtn[0].getText())-6;
        if (hour < 0){
            hour = 0;
        }
        HourPanel();
    }

    /**
     * Creates new form ReservationFrame
     */
    public ReservationFrame() {
        initComponents();
        
        //리스트
        modelMovie = new DefaultListModel();
        modelRegion = new DefaultListModel();
        modelTheater = new DefaultListModel();
        this.MovieList.setModel(modelMovie);
        this.RegionList.setModel(modelRegion);
        this.TheaterList.setModel(modelTheater);
        System.out.println(String.format(sql, date));
        movieTable();
        
        //날짜 패널
        JButton prev_calender = new JButton("<");
        dayPanel.add(prev_calender);
        // cal.set(year, month, day);
        year = date.getYear();
        month = date.getMonthValue();
        day = date.getDayOfMonth();
        day_end = date.lengthOfMonth();
        dateBtn = new JButton[10];
        for (int i=0; i < 10; i++){
            dateBtn[i] = new JButton();
            dateBtn[i].addActionListener(new getBtnDate());
            dayPanel.add(dateBtn[i]);
        }
        DatePanel();
        JButton next_calender = new JButton(">");
        JButton calender = new JButton();
        dayPanel.add(next_calender);
        dayPanel.add(calender);
        
        //시간
        JButton prev_time = new JButton("<");
        TimePanel.add(prev_time);
        timeBtn = new JButton[6];
        for (int i=0; i < 6; i++){
            timeBtn[i] = new JButton();
            timeBtn[i].addActionListener(new getBtnTime());
            TimePanel.add(timeBtn[i]);
        }
        JButton next_time = new JButton(">");
        TimePanel.add(next_time);
        HourPanel();
        
        String colNames[] = {"시간", "영화명", "정보"};
        modelTable = new DefaultTableModel(colNames, 0);
        this.ScreenTable.setModel(modelTable);
        this.ScreenTable.getColumn("시간").setPreferredWidth(10);
        this.ScreenTable.getColumn("영화명").setPreferredWidth(80);
        this.ScreenTable.getColumn("정보").setPreferredWidth(10);
        
        
        // 이벤트
        // 영화 리스트 클릭 시
        MovieList.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Movie_id = Integer.parseInt(movie.get(MovieList.getAnchorSelectionIndex())[3]);
                RegionTable();
            }
        });
        // 지역 선택
        RegionList.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TheaterTable(theater.get(RegionList.getAnchorSelectionIndex())[0]);
            }
        });
        
        TheaterList.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Theater_id = Integer.parseInt(theater.get(TheaterList.getAnchorSelectionIndex())[2]);
                System.out.println(theater.get(TheaterList.getAnchorSelectionIndex())[1] + " " +Theater_id);
                TableUpdate();
            }
        });
        
        ScreenTable.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int idx = ScreenTable.getSelectedRow();
                System.out.println(ScreenTime.get(idx)[2]);
                // 다음 프레임으로 값 전송
                new SeatFrame(ScreenTime.get(idx));
            }
        });
        // 날짜 변경
        prev_calender.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (!dateBtn[0].getText().equals(String.format("%d/%d", monthC, dayC))){
                    DatePanelReverse();
                }
            }
        });
        
        next_calender.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                DatePanel();
            }
        });
        
        // 시간 변경
        prev_time.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //System.out.println(Integer.parseInt(timeBtn[0].getText()) + " " + curr_time.getHour());
                if (LocalDate.of(yearC, monthC, dayC).equals(dateS)){
                    if (Integer.parseInt(timeBtn[0].getText()) <= curr_time.getHour()){
                        //System.out.println("c "+curr_time.getHour());
                        hour = curr_time.getHour();
                        HourPanel();
                    }
                    else{
                        HourPanelReverse();
                    }
                } else{
                    System.out.println(hour);
                    HourPanelReverse();
                }
            }
        });
        
        next_time.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (hour < 24){
                    HourPanel();
                }
            }
        });
    }
    
    class getBtnDate implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            String[] text = e.getActionCommand().split("/");
            if (text.length == 3){
                dateS = LocalDate.of(2000+Integer.parseInt(text[0]), Integer.parseInt(text[1]), Integer.parseInt(text[2]));
                //System.out.println(dateS);
            }else{
                dateS = LocalDate.of(yearC, Integer.parseInt(text[0]), Integer.parseInt(text[1]));
                if (!dateS.equals(LocalDate.of(yearC, monthC, dayC))){
                    time = 0;
                }
                //System.out.println(dateS);
            }
            modelTable.setNumRows(0);
            movieTable();
        }
    }
    
    class getBtnTime implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.out.println(Integer.parseInt(e.getActionCommand()));
            time = Integer.parseInt(e.getActionCommand());
            TableUpdate();
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

        jButton1 = new javax.swing.JButton();
        dayPanel = new javax.swing.JPanel();
        ReservationPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        MovieList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        RegionList = new javax.swing.JList<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        TheaterList = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ScreenTable = new javax.swing.JTable();
        TimePanel = new javax.swing.JPanel();

        jButton1.setText("jButton1");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 25, 1200, 800));
        setMaximumSize(new java.awt.Dimension(1200, 800));
        setMinimumSize(new java.awt.Dimension(1200, 800));
        setPreferredSize(new java.awt.Dimension(1200, 800));

        dayPanel.setBackground(new java.awt.Color(255, 255, 255));
        dayPanel.setSize(new java.awt.Dimension(0, 30));
        dayPanel.setLayout(new java.awt.GridLayout(1, 11));
        getContentPane().add(dayPanel, java.awt.BorderLayout.PAGE_START);

        ReservationPanel.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        MovieList.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        MovieList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MovieListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(MovieList);

        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 28)); // NOI18N
        jLabel1.setText(" 영화");
        jPanel2.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        ReservationPanel.add(jPanel2);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 28)); // NOI18N
        jLabel2.setText(" 극장");
        jPanel4.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane4.setToolTipText("");

        RegionList.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jScrollPane4.setViewportView(RegionList);

        jPanel1.add(jScrollPane4);

        TheaterList.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jScrollPane5.setViewportView(TheaterList);

        jPanel1.add(jScrollPane5);

        jPanel4.add(jPanel1, java.awt.BorderLayout.CENTER);

        ReservationPanel.add(jPanel4);

        ScreenTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane1.setViewportView(ScreenTable);

        TimePanel.setLayout(new java.awt.GridLayout(1, 10));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TimePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(TimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 909, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        ReservationPanel.add(jPanel3);

        getContentPane().add(ReservationPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        movie = dbManager.executeQuery(String.format(sql, date));
        movieTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void MovieListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MovieListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_MovieListMouseClicked

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
            java.util.logging.Logger.getLogger(ReservationFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReservationFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReservationFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReservationFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReservationFrame().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> MovieList;
    private javax.swing.JList<String> RegionList;
    private javax.swing.JPanel ReservationPanel;
    private javax.swing.JTable ScreenTable;
    private javax.swing.JList<String> TheaterList;
    private javax.swing.JPanel TimePanel;
    private javax.swing.JPanel dayPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    // End of variables declaration//GEN-END:variables
}
