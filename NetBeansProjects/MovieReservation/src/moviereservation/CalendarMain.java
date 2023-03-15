package moviereservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class CalendarMain extends JFrame implements ActionListener{
   Calendar cal; 
   int year, month, date;
   JPanel pane = new JPanel();
   public String tempDate; 
   Date SD = new Date();
   Date ED = new Date();
   static String start_date = null;
   
   DBManager dbManager;
   String user_id;
   
     
      JButton btn1 = new JButton("◀");
      JButton btn2 = new JButton("▶");

      JLabel yearlb = new JLabel("년");
      JLabel monthlb = new JLabel("월");

      JComboBox<Integer> yearCombo = new JComboBox<Integer>();
      DefaultComboBoxModel<Integer> yearModel = new DefaultComboBoxModel<Integer>();
      JComboBox<Integer> monthCombo = new JComboBox<Integer>();
      DefaultComboBoxModel<Integer> monthModel = new DefaultComboBoxModel<Integer>();

      JPanel pane2 = new JPanel(new BorderLayout());
         JPanel title = new JPanel(new GridLayout(1, 7));
            String titleStr[] = {"일", "월", "화", "수", "목", "금", "토"};
         JPanel datePane = new JPanel(new GridLayout(0, 7));
         private final JPanel panel = new JPanel();
         private final JTextField start_c = new JTextField();
         private final JTextField end_c = new JTextField();
         private final JButton reset = new JButton("재설정");
         private final JButton confirm_button = new JButton("확인");
         private final JLabel label = new JLabel("");
         private final JLabel label_1 = new JLabel("");
         private final JLabel label_2 = new JLabel("");

   public CalendarMain(DBManager dbManager, String user_id) {
       this.dbManager = dbManager;
       this.user_id = user_id;
     setBounds(0, 0, 1000, 1000);
        start_c.setColumns(10);
      cal = Calendar.getInstance(); //현재날짜
      year = cal.get(Calendar.YEAR);
      month = cal.get(Calendar.MONTH)+1;
      date = cal.get(Calendar.DATE);

      for(int i=year-100; i<=year+50; i++){
         yearModel.addElement(i);
      }
      yearCombo.setModel(yearModel);
      yearCombo.setSelectedItem(year);
      
      for(int i=1; i<=12; i++) {
         monthModel.addElement(i);
      }
      monthCombo.setModel(monthModel);
      monthCombo.setSelectedItem(month);

      for(int i=0; i<titleStr.length; i++){
         JLabel lbl = new JLabel(titleStr[i], JLabel.CENTER);
         if(i == 0){
            lbl.setForeground(Color.red);
         }else if(i == 6){
            lbl.setForeground(Color.blue);
         }
         title.add(lbl);
      }

      day(year, month);

      setTitle("달력");
      pane.add(btn1);
      pane.add(yearCombo);
      pane.add(yearlb);
      pane.add(monthCombo);
      pane.add(monthlb);
      pane.add(btn2);
      pane.setBackground(Color.LIGHT_GRAY);
      getContentPane().add(BorderLayout.NORTH, pane);
      pane2.add(title,"North");
      pane2.add(datePane);
      getContentPane().add(BorderLayout.CENTER, pane2);
      panel.setBorder(new LineBorder(new Color(0, 0, 0), 0, true));
      
      getContentPane().add(panel, BorderLayout.SOUTH);
      panel.setLayout(new GridLayout(0, 4, 0, 0));
      
      panel.add(start_c);
      reset.addMouseListener(new MouseAdapter() {
         public void mouseReleased(MouseEvent e){
            start_c.setText("");

         }
      });
      panel.add(reset);
      panel.add(confirm_button);
      confirm_button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
          
            if(!start_c.getText().isBlank()) {
                String[] date = start_c.getText().split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);
                
                ReservationFrame rf = new ReservationFrame(dbManager, user_id);
                rf.updateDate(LocalDate.of(year, month, day));
                rf.setVisible(true);
                dispose();
            }
         }
      });
      
      
      panel.add(label);
      
      panel.add(label_1);
      
      panel.add(label_2);

        setVisible(true);
        setSize(400,396);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btn1.addActionListener(this);
        btn2.addActionListener(this);
        yearCombo.addActionListener(this);
        monthCombo.addActionListener(this);
   }

   public void actionPerformed(ActionEvent e) {
      Object eventObj = e.getSource();
      if(eventObj instanceof JComboBox) {
         datePane.setVisible(false);
         datePane.removeAll();
         day((Integer)yearCombo.getSelectedItem(), (Integer)monthCombo.getSelectedItem());
         datePane.setVisible(true);
      }else if(eventObj instanceof JButton) {
         JButton eventBtn = (JButton) eventObj;
         int yy = (Integer)yearCombo.getSelectedItem();
         int mm = (Integer)monthCombo.getSelectedItem();
         if(eventBtn.equals(btn1)){
            if(mm==1){
               yy--; mm=12;
            }else{
               mm--;
            }            
         }else if(eventBtn.equals(btn2)){
            if(mm==12){
               yy++; mm=1;
            }else{
               mm++;
            }
         }
         yearCombo.setSelectedItem(yy);
         monthCombo.setSelectedItem(mm);
      }   
   }

   public void day(int year, int month) {
      Calendar date = Calendar.getInstance();//오늘날짜 + 시간
      date.set(year, month-1, 1);
      int week = date.get(Calendar.DAY_OF_WEEK);
      int lastDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);

      for(int space=1; space<week; space++) {
         datePane.add(new JLabel("\t"));
      }

      for (int day = 1; day <= lastDay; day++) {
         JButton btt = new JButton(String.valueOf(day));
         cal.set(year, month-1, day);
         int Week = cal.get(Calendar.DAY_OF_WEEK);
         if(Week==1){
            btt.setForeground(Color.red);            
         }else if(Week==7){
            btt.setForeground(Color.BLUE);
         }
         datePane.add(btt);
        
         btt.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
               
               System.out.println(year + "-" + month + "-" + btt.getText());
               String tempDate = year +"-"+month+"-"+btt.getText();
                start_c.setText(tempDate);
                start_date = tempDate;
            }
         });
      }
   }
   
   public static void main(String[] args) {
      //new CalendarMain();   
   }
}