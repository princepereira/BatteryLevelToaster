import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tester {

  final static String URL = "http://192.168.225.1/";   
  public static void main(String[] args) {
      ToastMessage message = new ToastMessage();
      String perc = readBatteryLevel();
      while (perc != ""){
        try{
          int percentInt = Integer.parseInt(perc.split("%")[0]);
          String severity = "";
          int sleepTime = 10000;
          if(percentInt < 15){
            severity = "CRITICAL => ";
            sleepTime = 2000;
          }else if(percentInt < 20){
            severity = "WARNING => ";
            sleepTime = 5000;
          } else if(percentInt < 40){
            severity = "MAJOR => ";
          } else if(percentInt < 50){
            severity = "MINOR => ";
          } else{
            Thread.sleep(sleepTime);
            continue;
          }
          message.printMsg(severity+"Battery Level :"+perc);
          Thread.sleep(sleepTime);
          perc = readBatteryLevel();
        } catch (Exception ex){
        }
      }
   }

  public static String readBatteryLevel(){
    try{
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(URL).openConnection().getInputStream() ));
      StringBuilder sb = new StringBuilder();
      String line = null;
      while( ( line = bufferedReader.readLine() ) != null ) {
        sb.append( line ) ;
        sb.append( "\n");
      }
      bufferedReader.close();
      String val = sb.toString().split("<input id=\"batterylevel\"  value=\"")[1].split("\" type=\"hidden")[0];
      System.out.println(">>>>>>>>>>>>>>>"+val+"<<<<<<<<<<<<<<<<<<<<<<");
      return val;
    } catch(Exception ex){

    }
    return "";
  }
}

class ToastMessage extends JFrame {
   public void printMsg(final String message) {
      setUndecorated(true);
      setLayout(new GridBagLayout());
      setBackground(new Color(240,240,240,250));
      setLocationRelativeTo(null);
      setSize(300, 50);
      add(new JLabel(message)); 
       
      addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            setShape(new  RoundRectangle2D.Double(0,0,getWidth(),
            getHeight(), 20, 20));                      
         }
      });
      display(); 
   }

   public void display() {
      try {
         setOpacity(1);
         setVisible(true);
         Thread.sleep(2000);

         //hide the toast message in slow motion
         for (double d = 1.0; d > 0.2; d -= 0.1) {
            Thread.sleep(500);
            setOpacity((float)d);
         }

         // set the visibility to false
         setVisible(false);
      }catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }
}

