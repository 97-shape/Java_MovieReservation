/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package moviereservation;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class MovieReservation extends JFrame implements ActionListener, Runnable
{
    //0~100을 세어주는 프로그래스바 생성
   private JProgressBar jpb= new JProgressBar (JProgressBar.HORIZONTAL, 0,100);  
   private JButton bt1= new JButton("시작");
   
   DBManager dbManager;
    //생성자 호출
   public MovieReservation(){
      super("TEST");
      Container con = getContentPane();
      con.setLayout(new BorderLayout());
      con.add("North", new JLabel("데이터 베이스 연결"));
      con.add("Center", jpb);
        //JProgressBar 셋팅
      jpb.setStringPainted(true);
      jpb.setString("0%");
        //화면에 올리기
      JPanel jp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      jp.add(bt1);
      bt1.addActionListener(this);

      con.add("South",jp);
      setSize(300,150);
      setVisible(true);
   }
   private boolean bb = true;
   private static int ii;

    //스레드 오버라이드 메소드
    @Override
   public void run(){
      if(ii==100){
         ii=0;
      }
      for(int i=ii;i<=100;i++){
         if(!bb) break;
         try{
            Thread.sleep(10);
         } catch(InterruptedException ee){}
            jpb.setValue(i);
            ii=i;
            jpb.setString(i+"%");
         
      }
      bt1.setEnabled(true);
      new HomeFrame(dbManager, null).setVisible(true);
      dispose();
   }
    //ActionListener 오버라이드 메소드
    @Override
   public void actionPerformed(ActionEvent e){
      if(e.getSource()==bt1){  //"시작" 버튼 클릭
         bb=true;
         dbManager = DBManagerMySql.getinstance();
         new Thread(this).start();
         bt1.setEnabled(false);
      }
   }
   public static void main(String[] args) 
   {
      new MovieReservation();
   }
}
