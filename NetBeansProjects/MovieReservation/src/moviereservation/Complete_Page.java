package moviereservation;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Complete_Page extends JFrame{
    
    DBManager dbManager;
    String user_id;

   JLabel MovieNameLabel;
   JLabel ScreeningDateLabel;
   JLabel ScreeningDateTimeLabel;
   JLabel ScreeningTimeLabel;
   JLabel TheaterLocationLabel;
   JLabel TheaterNumberLabel;
   JLabel PersonnelInfoLabel;
   JLabel SeatLowColumnLabel;
   /**
    * Launch the application.
    */
   /*
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
             String data[] = {"와칸다", "2022-11-20", "영진", "19:00", "4", "성인 2, 청소년 1, 기타 0", "(1,1), (2,1), (3,2)"};
            try {
               Complete_Page window = new Complete_Page(data);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }
   */

   /**
    * Create the application.
    */
   public Complete_Page(String[] data, DBManager dbManager, String user_id) {
      initialize();
      this.dbManager = dbManager;
      this.user_id = user_id;
      this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) { 
                    new HomeFrame(dbManager, user_id).setVisible(true);
                    dispose();
            }
        });
      MovieNameLabel.setText(data[0]);
      ScreeningDateLabel.setText(data[1]);
      ScreeningTimeLabel.setText(data[3]);
      TheaterLocationLabel.setText(data[2]);
      TheaterNumberLabel.setText(data[4]);
      PersonnelInfoLabel.setText(data[5]);
      SeatLowColumnLabel.setText(data[6]);
   }
   
   public Complete_Page(String[] data) {
      initialize();
      this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      MovieNameLabel.setText(data[0]);
      ScreeningDateLabel.setText(data[1]);
      ScreeningTimeLabel.setText(data[3]);
      TheaterLocationLabel.setText(data[2]);
      TheaterNumberLabel.setText(data[4]);
      PersonnelInfoLabel.setText(data[5]);
      SeatLowColumnLabel.setText(data[6]);
   }
   

   /**
    * Initialize the contents of the frame.
    */
   private void initialize() {
      this.setBounds(100, 100, 450, 500);
      this.getContentPane().setLayout(new GridLayout(5, 0, 0, 0));
      
      JPanel panel_1 = new JPanel();
      this.getContentPane().add(panel_1);
      panel_1.setLayout(null);
      
      JLabel CompleteLabel = new JLabel("결제 완료");
      CompleteLabel.setForeground(new Color(128, 0, 255));
      CompleteLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 24));
      CompleteLabel.setBounds(30, 20, 100, 48);
      panel_1.add(CompleteLabel);
      
      JPanel panel_2 = new JPanel();
      this.getContentPane().add(panel_2);
      panel_2.setLayout(null);
      
      MovieNameLabel = new JLabel("Movie_Name");
      MovieNameLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 18));
      MovieNameLabel.setBounds(30, 5, 300, 40);
      panel_2.add(MovieNameLabel);
      
      JPanel panel_3 = new JPanel();
      this.getContentPane().add(panel_3);
      panel_3.setLayout(null);
      
      ScreeningDateLabel = new JLabel("Screening_Date");
      ScreeningDateLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 14));
      ScreeningDateLabel.setBounds(30, 40, 130, 20);
      panel_3.add(ScreeningDateLabel);
      
      ScreeningDateTimeLabel = new JLabel("상영 일시");
      ScreeningDateTimeLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 16));
      ScreeningDateTimeLabel.setForeground(new Color(128, 0, 255));
      ScreeningDateTimeLabel.setBounds(30, 5, 130, 30);
      panel_3.add(ScreeningDateTimeLabel);
      
      ScreeningTimeLabel = new JLabel("Screening_Time");
      ScreeningTimeLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 14));
      ScreeningTimeLabel.setBounds(30, 62, 200, 20);
      panel_3.add(ScreeningTimeLabel);
      
      JLabel TheaterLabel = new JLabel("상영관");
      TheaterLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 16));
      TheaterLabel.setForeground(new Color(128, 0, 255));
      TheaterLabel.setBounds(250, 5, 130, 30);
      panel_3.add(TheaterLabel);
      
      TheaterLocationLabel = new JLabel("Theater_Location");
      TheaterLocationLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 14));
      TheaterLocationLabel.setBounds(250, 40, 130, 20);
      panel_3.add(TheaterLocationLabel);
      
      TheaterNumberLabel = new JLabel("Theater_Number");
      TheaterNumberLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 14));
      TheaterNumberLabel.setBounds(250, 62, 130, 20);
      panel_3.add(TheaterNumberLabel);
      
      JPanel panel_4 = new JPanel();
      this.getContentPane().add(panel_4);
      panel_4.setLayout(null);
      
      JLabel SeatInfoLabel = new JLabel("좌석정보");
      SeatInfoLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 16));
      SeatInfoLabel.setForeground(new Color(128, 0, 255));
      SeatInfoLabel.setBackground(new Color(240, 240, 240));
      SeatInfoLabel.setBounds(30, 5, 130, 30);
      panel_4.add(SeatInfoLabel);
      
      PersonnelInfoLabel = new JLabel("Personnel_Information");
      PersonnelInfoLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 14));
      PersonnelInfoLabel.setBounds(30, 40, 190, 20);
      panel_4.add(PersonnelInfoLabel);
      
      SeatLowColumnLabel = new JLabel("Seat_Low_Column");
      SeatLowColumnLabel.setFont(new Font("함초롬바탕", Font.PLAIN, 14));
      SeatLowColumnLabel.setBounds(30, 62, 190, 20);
      panel_4.add(SeatLowColumnLabel);
      
      JPanel panel_5 = new JPanel();
      this.getContentPane().add(panel_5);
      panel_5.setLayout(null);
      
      JLabel TheaterNameLabel = new JLabel("                        Cinema");
      TheaterNameLabel.setFont(new Font("Impact", Font.PLAIN, 30));
      TheaterNameLabel.setForeground(new Color(128, 0, 255));
      TheaterNameLabel.setBackground(new Color(240, 240, 240));
      TheaterNameLabel.setBounds(50, 10, 334, 77);
      panel_5.add(TheaterNameLabel);
   }
}