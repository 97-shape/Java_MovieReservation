/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package moviereservation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author umsang-uk
 */
public class SeatFrame extends javax.swing.JFrame {
    private static DBManager dbManager;
    //private static DBManager dbManager = DBManagerMySql.getinstance();
    
    String sql_countSeat = 
        "SELECT COUNT(s.Seat_ID)\n" +
        "FROM Reservation r, Seat s\n" +
        "WHERE r.Time_ID = %s\n" +
        "AND r.Reservation_ID = s.Reservation_ID\n" +
        "GROUP BY r.Time_ID;";
    
    String sql_reservedSeat = 
        "SELECT s.Seat_X, s.Seat_Y\n" + 
        "FROM Reservation r, Seat s\n" +
        "WHERE r.Time_ID = %s\n" +
        "AND r.Reservation_ID = s.Reservation_ID;";
    
    String sql_reserve = "INSERT INTO Reservation (User_ID, Time_ID, Adult, Teen, Other) VALUES ('%s', %s, %d, %d, %d);";
    String sql_seat = "INSERT INTO seat (seat_x, seat_y, reservation_id) VALUES (%s, %s, %s);";
    
    
    JButton[][] seatBtn;
    JLabel[] rowLabel;
    
    final int adultPrice = 14000;
    final int teenPrice = 10000;
    final int otherPrice = 5000;
    
    int adult = 0;
    int teen = 0;
    int other = 0;
    int seatCnt;
    int count = 0;
    int money = 0;
    
    String user_id;
    ArrayList<String[]> reservation_id;
    String[] data;
    
    ArrayList<Integer> seats = new ArrayList<>();
    DefaultListModel seatPosition = new DefaultListModel();
    // 디버그
    //0 : r.StartTime, 1 : r.EndTime, 2 : r.Movie_Name, 3 : r.Movie_Count, 4 : r.Room_No, 5 : r.Total_Seat, 6 : r.Time_ID, 7:r.Theart_Name, 8:r.Seat_Row, 9:r.seat_Col, 10:movie_Date, 11:Movie_Poster
    static String[] Debugdata = {"09:00:00", "11:41:00", "블랙 팬서: 와칸다 포에버", "3", "3", "25", "3", "영진 시네마", "5", "5", "2022-11-19", "블랙 팬서_와칸다 포에버"};
    /**
     * Creates new form testFrame
     */
    public SeatFrame(String[] data, DBManager dbManager, String user_id) {
        this.data = data;
        this.user_id = user_id;
        this.dbManager = dbManager;
        initComponents();
        Screen.setOpaque(true);
        Screen.setBackground(new Color(0,51,102));
        this.setSize(1200, 800);
        this.setVisible(true);
        
        this.posterPanel.setDimension(150, 180);
        
        // 예매 정보
        MovieNameLabel.setText(data[2]);
        TheaterLocationLabel.setText(data[7]);
        RoomLabel.setText(data[4]+"관");
        DateLabel.setText(data[10]);
        int x = Integer.parseInt(data[8]);
        int y = Integer.parseInt(data[9]);
        
        
        // 좌석
        seatBtn = new JButton[x][y+1];
        rowLabel = new JLabel[x];
        for (int i=0; i < x; i++){
            rowLabel[i] = new JLabel(Character.toString((char)(i+65)));
            rowLabel[i].setHorizontalAlignment(JLabel.CENTER);
            SeatPanel.add(rowLabel[i]);
            for (int j=0; j < y; j++){
                seatBtn[i][j] = new JButton(i + ", " + j);
                SeatPanel.add(seatBtn[i][j]);
                seatBtn[i][j].addActionListener(new CheckSeat());
            }
        }
        
        // 예약된 좌석 체크 및 비활성화
        ArrayList<String[]> reservedSeat = dbManager.executeQuery(String.format(sql_reservedSeat, data[6]));
        int rX, rY;
        for (int i=0; i < reservedSeat.size(); i++){
            rX = Integer.parseInt(reservedSeat.get(i)[0]);
            rY = Integer.parseInt(reservedSeat.get(i)[1]);
            seatBtn[rX][rY].setText("예약");
            seatBtn[rX][rY].setEnabled(false);
        }
        
        // 예약된 좌석 수 확인
        seatCnt = Integer.parseInt(data[5]);
        ArrayList<String[]> reservedSeatCnt = dbManager.executeQuery(String.format(sql_countSeat, data[6]));
        //System.out.println(totalSeat.get(0)[0]);
        if (reservedSeatCnt.isEmpty()){
            SeatInfoLabel.setText("0/" + data[5]);
        }else{
            SeatInfoLabel.setText(reservedSeatCnt.get(0)[0] + "/" + data[5]);
            seatCnt -= Integer.parseInt(reservedSeatCnt.get(0)[0]);
        }
                
        
        this.SeatPanel.setLayout(new GridLayout(x, y));
        this.SeatPanel.setPreferredSize(new Dimension(700, 400));
        //SeatPanel.setSize(1000, 1000);
        
        // 이벤트
        AdultM.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (adult <= 0) {adult = 0;}
                else {adult--;}
                AdultCnt.setText(Integer.toString(adult));
                money -= adultPrice;
                moneyLabel.setText(Integer.toString(money));
            }
        });
        
        AdultP.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (adult + teen + other >= seatCnt) {}
                else {adult++;}
                AdultCnt.setText(Integer.toString(adult));
                money += adultPrice;
                moneyLabel.setText(Integer.toString(money));
            }
        });
        TeenM.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (teen <= 0) {teen = 0;}
                else {teen--;}
                TeenCnt.setText(Integer.toString(teen));
                money -= teenPrice;
                moneyLabel.setText(Integer.toString(money));
            }
        });
        
        TeenP.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (adult + teen + other >= seatCnt) {}
                else {teen++;}
                TeenCnt.setText(Integer.toString(teen));
                money += teenPrice;
                moneyLabel.setText(Integer.toString(money));
            }
        });
        OtherM.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (other <= 0) {other = 0;}
                else {other--;}
                OtherCnt.setText(Integer.toString(other));
                money -= otherPrice;
                moneyLabel.setText(Integer.toString(money));
            }
        });
        
        OtherP.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (adult + teen + other >= seatCnt) {}
                else {other++;}
                OtherCnt.setText(Integer.toString(other));
                money += otherPrice;
                moneyLabel.setText(Integer.toString(money));
            }
        });
        
        resetButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                seatPosition.setSize(0);
                adult = 0;
                teen = 0;
                other = 0;
                money = 0;
                
                AdultCnt.setText(Integer.toString(adult));
                TeenCnt.setText(Integer.toString(teen));
                OtherCnt.setText(Integer.toString(other));
                personLabel.setText("0");
                moneyLabel.setText(Integer.toString(money));
                
                for (int i=0; i<seats.size(); i += 2){
                    //seatBtn[seats.get(i)][seats.get(i+1)].setText(String.format("%d, %d", seats.get(i), seats.get(i+1)));
                    seatBtn[seats.get(i)][seats.get(i+1)].setEnabled(true);
                }
                count = 0;
            }
        });
        
        try {
            posterPanel.DrawImg(ImageIO.read(SeatFrame.class.getResource(String.format("/img/%s.jpg", data[11]))));
        } catch (IOException ex) {
            posterPanel.DrawImg(null);
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

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        MovieNameLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        TheaterLocationLabel = new javax.swing.JLabel();
        RoomLabel = new javax.swing.JLabel();
        DateLabel = new javax.swing.JLabel();
        posterPanel = new moviereservation.PosterPanel();
        jPanel6 = new javax.swing.JPanel();
        Label1 = new javax.swing.JLabel();
        Label2 = new javax.swing.JLabel();
        Label3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        seatList = new javax.swing.JList<>();
        jPanel8 = new javax.swing.JPanel();
        Label4 = new javax.swing.JLabel();
        personLabel = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        Label5 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        moneyLabel = new javax.swing.JLabel();
        reserveBtn = new javax.swing.JToggleButton();
        prevBtn = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        SeatPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        SeatInfoLabel = new javax.swing.JLabel();
        Screen = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        AdultCnt = new javax.swing.JLabel();
        AdultM = new javax.swing.JButton();
        AdultP = new javax.swing.JButton();
        TeenM = new javax.swing.JButton();
        TeenCnt = new javax.swing.JLabel();
        TeenP = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        OtherM = new javax.swing.JButton();
        OtherCnt = new javax.swing.JLabel();
        OtherP = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setBounds(new java.awt.Rectangle(0, 0, 1200, 800));
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 1200, 800));
        setMaximumSize(new java.awt.Dimension(1200, 800));
        setPreferredSize(new java.awt.Dimension(1200, 800));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(455, 800));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        MovieNameLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 30)); // NOI18N
        MovieNameLabel.setText("양자경의 더 모든 날 모든 순간");
        jPanel1.add(MovieNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 47, -1, -1));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TheaterLocationLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        TheaterLocationLabel.setText("jLabel8");

        RoomLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        RoomLabel.setText("jLabel9");

        DateLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        DateLabel.setText("jLabel10");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TheaterLocationLabel)
                    .addComponent(RoomLabel)
                    .addComponent(DateLabel))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(TheaterLocationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(RoomLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21)
                .addComponent(DateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(36, 36, 36))
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, -1, -1));

        javax.swing.GroupLayout posterPanelLayout = new javax.swing.GroupLayout(posterPanel);
        posterPanel.setLayout(posterPanelLayout);
        posterPanelLayout.setHorizontalGroup(
            posterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        posterPanelLayout.setVerticalGroup(
            posterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        jPanel1.add(posterPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 150, 180));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Label1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        Label1.setText("선택");

        Label2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        Label2.setText("완료");

        Label3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        Label3.setText("일반");

        jButton1.setText("x, y");
        jButton1.setEnabled(false);

        jButton2.setText("예약");
        jButton2.setEnabled(false);

        jButton3.setText("x, y");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label3)
                    .addComponent(Label2)
                    .addComponent(Label1))
                .addGap(35, 35, 35))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(41, 41, 41))
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 150, -1));

        seatList.setModel(seatPosition);
        jScrollPane1.setViewportView(seatList);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 360, 170, 180));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Label4.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        Label4.setText("선택 인원 ");

        personLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        personLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        personLabel.setText("0");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label4)
                .addGap(32, 32, 32)
                .addComponent(personLabel)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(Label4)
                    .addComponent(personLabel))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 580, 160, 40));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Label5.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        Label5.setText("최종 결제 금액");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label5)
                .addContainerGap())
        );

        jPanel1.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 640, 160, 40));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        moneyLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        moneyLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        moneyLabel.setText("0");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(149, Short.MAX_VALUE)
                .addComponent(moneyLabel)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(moneyLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 640, 170, 40));

        reserveBtn.setFont(new java.awt.Font("Helvetica Neue", 0, 26)); // NOI18N
        reserveBtn.setText("예약");
        reserveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reserveBtnActionPerformed(evt);
            }
        });
        jPanel1.add(reserveBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 710, 120, 40));

        prevBtn.setFont(new java.awt.Font("Helvetica Neue", 0, 26)); // NOI18N
        prevBtn.setText("이전");
        prevBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevBtnActionPerformed(evt);
            }
        });
        jPanel1.add(prevBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 710, 120, 40));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        SeatPanel.setBackground(new java.awt.Color(255, 255, 255));
        SeatPanel.setPreferredSize(new java.awt.Dimension(700, 400));
        SeatPanel.setSize(new java.awt.Dimension(703, 543));

        javax.swing.GroupLayout SeatPanelLayout = new javax.swing.GroupLayout(SeatPanel);
        SeatPanel.setLayout(SeatPanelLayout);
        SeatPanelLayout.setHorizontalGroup(
            SeatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        SeatPanelLayout.setVerticalGroup(
            SeatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel7.setText("좌석현황");

        SeatInfoLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        SeatInfoLabel.setText("jLabel8");

        Screen.setBackground(new java.awt.Color(0, 51, 102));
        Screen.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        Screen.setForeground(new java.awt.Color(255, 255, 255));
        Screen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Screen.setText("SCREEN");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SeatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Screen, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(323, 323, 323)
                        .addComponent(jLabel7)
                        .addGap(41, 41, 41)
                        .addComponent(SeatInfoLabel)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(Screen)
                .addGap(48, 48, 48)
                .addComponent(SeatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SeatInfoLabel)
                    .addComponent(jLabel7))
                .addGap(70, 70, 70))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel1.setText("성인");

        AdultCnt.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        AdultCnt.setText(Integer.toString(adult));

        AdultM.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        AdultM.setText("<");
        AdultM.setBounds(new java.awt.Rectangle(0, 0, 25, 25));
        AdultM.setMargin(new java.awt.Insets(0, 0, 0, 0));

        AdultP.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        AdultP.setText(">");

        TeenM.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        TeenM.setText("<");

        TeenCnt.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        TeenCnt.setText(Integer.toString(teen));

        TeenP.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        TeenP.setText(">");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel2.setText("청소년");

        OtherM.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        OtherM.setText("<");

        OtherCnt.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        OtherCnt.setText(Integer.toString(other));

        OtherP.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        OtherP.setText(">");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel3.setText("우대");

        resetButton.setText("초기화");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AdultM, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AdultCnt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AdultP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TeenM, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TeenCnt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TeenP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OtherM, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(OtherCnt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(OtherP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(resetButton)
                .addGap(48, 48, 48))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(resetButton)
                    .addComponent(OtherP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel4Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OtherCnt, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(OtherM, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TeenP, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TeenCnt, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(TeenM, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AdultP, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AdultCnt, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(AdultM, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resetButtonActionPerformed

    private void reserveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reserveBtnActionPerformed
        // TODO add your handling code here:
        if (count == 0){
            JOptionPane.showMessageDialog(null, "예약할 인원의 수가 없습니다.", "예약 실패", JOptionPane.INFORMATION_MESSAGE);
        }else if (count == (adult + teen + other)){
            String sendData[] = new String[7];
            sendData[0] = MovieNameLabel.getText();
            sendData[1] = DateLabel.getText();
            sendData[2] = TheaterLocationLabel.getText();
            sendData[3] = String.format("%s ~ %s", data[0], data[1]); // 영화 시작 ~ 끝 시간
            sendData[4] = data[3]; // 영화 회차
            sendData[5] = String.format("성인 : %d, 청소년 : %d, 우대 : %d", adult, teen, other); // 관람객 수
            String seatInfo = "";
            String[] position = Arrays.copyOf(seatPosition.toArray(), seatPosition.toArray().length, String[].class);
            /*
            for(String position1 : position) {
                System.out.println(String.format("(%s) ", position1));
                seatInfo += String.format("(%s) ", position1);
            }
            */
            // System.out.println(seatInfo);
            dbManager.executeUpdate(String.format(sql_reserve, user_id, data[6], adult, teen, other));
            reservation_id = dbManager.executeQuery("SELECT last_insert_id();");
            for(String position1 : position) {
                //System.out.println(position1.split(", ")[0] + "|" +position1.split(", ")[1]);
                seatInfo += String.format("(%s) ", position1);
                dbManager.executeUpdate(String.format(sql_seat, position1.split(", ")[0], position1.split(", ")[1], reservation_id.get(0)[0]));
            }
            sendData[6] = seatInfo;
            new Complete_Page(sendData, dbManager, user_id).setVisible(true);
            dispose();
        }else{
            JOptionPane.showMessageDialog(null, "선택하신 인원 수 만큼의 좌석이 선택되지않았습니다.", "예약 실패", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_reserveBtnActionPerformed

    private void prevBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevBtnActionPerformed
        // TODO add your handling code here:
        new ReservationFrame(dbManager, user_id).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_prevBtnActionPerformed
    
    //좌석 선택 이벤트
    class CheckSeat implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //System.out.println(e.getSource());
            if (count < (adult + teen + other)){
                seatPosition.addElement(e.getActionCommand());
                int x = Integer.parseInt(e.getActionCommand().split(", ")[0]);
                int y = Integer.parseInt(e.getActionCommand().split(", ")[1]);
                seats.add(x);
                seats.add(y);
                personLabel.setText(Integer.toString(++count));
                //seatBtn[x][y].setText("x");
                seatBtn[x][y].setEnabled(false);
            }
        }
    }
    
    // 디버그
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SeatFrame(Debugdata, DBManagerMySql.getinstance(), "test").setVisible(true);
            }
        });
    }
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AdultCnt;
    private javax.swing.JButton AdultM;
    private javax.swing.JButton AdultP;
    private javax.swing.JLabel DateLabel;
    private javax.swing.JLabel Label1;
    private javax.swing.JLabel Label2;
    private javax.swing.JLabel Label3;
    private javax.swing.JLabel Label4;
    private javax.swing.JLabel Label5;
    private javax.swing.JLabel MovieNameLabel;
    private javax.swing.JLabel OtherCnt;
    private javax.swing.JButton OtherM;
    private javax.swing.JButton OtherP;
    private javax.swing.JLabel RoomLabel;
    private javax.swing.JLabel Screen;
    private javax.swing.JLabel SeatInfoLabel;
    private javax.swing.JPanel SeatPanel;
    private javax.swing.JLabel TeenCnt;
    private javax.swing.JButton TeenM;
    private javax.swing.JButton TeenP;
    private javax.swing.JLabel TheaterLocationLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel moneyLabel;
    private javax.swing.JLabel personLabel;
    private moviereservation.PosterPanel posterPanel;
    private javax.swing.JToggleButton prevBtn;
    private javax.swing.JToggleButton reserveBtn;
    private javax.swing.JButton resetButton;
    private javax.swing.JList<String> seatList;
    // End of variables declaration//GEN-END:variables
}
