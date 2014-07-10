package utils;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
//import javax.servlet.http.HttpServletResponse;
import javax.activation.*;

import DataEntity.*;
import java.util.*;
import utils.*;



class SocialAuth extends Authenticator{
	protected PasswordAuthentication getPasswordAuthentication(){
		return new PasswordAuthentication("dungkhmt@gmail.com","dungninh");
	}
}

public class SendMail {

	/**
	 * @param args
	 */
	public SendMail(){
		System.out.println("SendMail::SendMail()");
	}
	public  void processSendMail(){
		
		String[] to = {"dungpq@soict.hut.edu.vn","dungpq@coccoc.vn"};
	      String from = "dungkhmt@gmail.com";

	      // Assuming you are sending email from localhost
	      String host = "localhost";

	      // Get system properties
	      Properties properties = new Properties();//System.getProperties();
	      properties.put("mail.smtp.host", "smtp.gmail.com");
	      properties.put("mail.smtp.auth", "true");
	      properties.put("mail.debug", "false");
	      properties.put("mail.smtp.ssl.enable", "true");
	      System.out.println("processSendMail");
	      
	      Session session = Session.getDefaultInstance(properties, new SocialAuth());
	      
	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         InternetAddress[] toAddr = new InternetAddress[to.length];
	         for(int i = 0; i < to.length; i++)
	        	 toAddr[i] = new InternetAddress(to[i]);
	         message.setRecipients(Message.RecipientType.TO,toAddr);

	         // Set Subject: header field
	         message.setSubject("Thong tin hoi dong bao ve");

	         // Now set the actual message
	         String content = "<html>" + "    <meta charset=\"utf-8\"> " + 
	        		 "<table border=" + '"' + "1" + '"' + ">" +
	        		 "<tr>" + 
	        		 "<td>pham</td><td>quang</td>"+
	        		 "</tr>" +
	        		 "<tr>" + 
	        		 "<td>dung</td><td>ninh</td>"+
	        		 "</tr>" +	        		 
	         		"</html>";
	         
	         Vector<JuryInfo> listJury = Utility.getListJuryInfo("Room-Slot","All");
	         String schedule = "<html>";
	         schedule += "<head>" + 
	         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" + 
	         "</head>";
	         
	         schedule += "<table border=\"1\">";
	         for(int i = 0; i < listJury.size(); i++){
	        	 JuryInfo jr = listJury.get(i);
	        	 try{
	        	 schedule += "<tr>";
	        	 schedule += "<td>" + java.net.URLDecoder.decode(jr.getStudentName(),"UTF-8") + "</td>";
	        	 schedule += "<td>" + java.net.URLDecoder.decode(jr.getTitle(),"UTF-8") + "</td>";
	        	 schedule += "<td>" + java.net.URLDecoder.decode(jr.getExaminerName1(),"UTF-8") + "</td>";
	        	 schedule += "<td>" + java.net.URLDecoder.decode(jr.getExaminerName2(),"UTF-8") + "</td>";
	        	 schedule += "<td>" + java.net.URLDecoder.decode(jr.getPresidentName(),"UTF-8") + "</td>";
	        	 schedule += "<td>" + java.net.URLDecoder.decode(jr.getSecretaryName(),"UTF-8") + "</td>";
	        	 schedule += "<td>" + java.net.URLDecoder.decode(jr.getAdditionalMemberName(),"UTF-8") + "</td>";
	        	 schedule += "<td>" + java.net.URLDecoder.decode(jr.getSlotDescription(),"UTF-8") + "</td>";
	        	 schedule += "<td>" + java.net.URLDecoder.decode(jr.getRoomName(),"UTF-8") + "</td>";
	        	 schedule += "</tr>";
	        	 }catch(Exception ex){
	        		 ex.printStackTrace();
	        	 }
	         }
	         schedule += "</table>";
	         /*
	         schedule += "<table border=\"1\">";
	         for(int i = 0; i < listJury.size(); i++){
	        	 JuryInfo jr = listJury.get(i);
	        	 try{
	        	 schedule += "<tr>";
	        	 schedule += "<td>" + MimeUtility.decodeText(jr.getStudentName()) + "</td>";
	        	 schedule += "<td>" + MimeUtility.decodeText(jr.getTitle()) + "</td>";
	        	 schedule += "<td>" + MimeUtility.decodeText(jr.getExaminerName1()) + "</td>";
	        	 schedule += "<td>" + MimeUtility.decodeText(jr.getExaminerName2()) + "</td>";
	        	 schedule += "<td>" + MimeUtility.decodeText(jr.getPresidentName()) + "</td>";
	        	 schedule += "<td>" + MimeUtility.decodeText(jr.getSecretaryName()) + "</td>";
	        	 schedule += "<td>" + MimeUtility.decodeText(jr.getAdditionalMemberName()) + "</td>";
	        	 schedule += "<td>" + MimeUtility.decodeText(jr.getSlotDescription()) + "</td>";
	        	 schedule += "<td>" + MimeUtility.decodeText(jr.getRoomName()) + "</td>";
	        	 schedule += "</tr>";
	        	 }catch(Exception ex){
	        		 ex.printStackTrace();
	        	 }
	         }
	         schedule += "</table>";
	         */
	         schedule += "</html>";
	         
	         //schedule = "ABCDEG";
	         message.setContent(schedule,"text/html");
	         //message.setContent(schedule,"charset=UTF-8");
	         //message.setText("This is actual message");

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	         System.out.println(schedule);
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SendMail m = new SendMail();
		m.processSendMail();
	}

}
