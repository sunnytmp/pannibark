package com.riverlog.pig.ui.server;

import java.io.File;


import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

public class EmailPigScripts {
    Logger logger =Logger.getLogger("SendEmail.class");
    public String to = "nothing";
    public String from = "nothing";
    public  String subject = "Candidate Profile";
    public String usernameProp = null;
    public String pwd = null;
    public void SendEmail(){

    }
//public static void main (String args[]) throws Exception {
    public  void  main1 (String[] filenamesofcandidates, String toEmail, String cc,String textMessage,String subjectparam,String username) throws Exception {
   try{

       subject = subjectparam.toString().trim();
        logger.info("Begin executing main1....");
        String host = "";
        String protocol = "";
        String port = "";

       // PropertyInitialization propinit = new PropertyInitialization();
     try{
       // Properties prop =  propinit.readProperties();
        host = "mail.wingrunt.com" ; // propinit.getEmailHost(prop).trim();
        protocol = "smtp" ; //propinit.getEmailProtocol(prop).trim();
        port = "25" ; //propinit.getPort(prop).trim();
        from = "info@riverlog.com"; //propinit.getFromEmail(prop);
        from = from.toString().trim();
       
        usernameProp = "info@riverlog.com";//propinit.getEmailUserName(prop);
        usernameProp = usernameProp.toString().trim();
      
        pwd = "i^F0ZoGq?Mhm" ; //propinit.getEmailPassword(prop);
        pwd = pwd.toString().trim();
        logger.info("Initialized userid pwd correctly");

        //  if (!hexmacname.equals(propinit.getReValidate(prop))){
        //      return false;
        //  }
        System.out.println("User Name = " + usernameProp);
        System.out.println("User PWD  = " + pwd);
    }catch(Exception et){
        logger.debug("Exception in initialization procedure - user id pwd" + et.getMessage());
    }

        boolean sessionDebug = true;

        Properties props = System.getProperties();
        //props.put("mail.host", host);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
       // props.put("mail.smtp.starttls.enable","true");
       // props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", port);
     //  props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.transport.protocol", "smtp");
        
        props.put("mail.debug", "true");
        logger.info("Property initialization producedure ends..");

        Authenticator auth = new SMTPAuthenticator();
        javax.mail.Session mailSession = javax.mail.Session.getInstance(props,auth);

        mailSession.setDebug(sessionDebug);

        Message msg = new MimeMessage(mailSession);

        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = {new InternetAddress(toEmail)};
        msg.setRecipients(Message.RecipientType.TO, address);
        if (cc !=null){
         InternetAddress[] addressCC = {new InternetAddress(cc)};
         msg.setRecipients(Message.RecipientType.CC, addressCC);
        }
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        Transport tr = mailSession.getTransport(protocol);
        tr.connect (host, new Integer(port).intValue(), usernameProp,pwd);
        logger.info("Retrieved Protocol..");
        logger.info("Connected Succesfully..");
        
    
           Message message = attachFile(tr,props,mailSession,filenamesofcandidates,toEmail, cc,textMessage,username);
        

        //tr.send(message);
        tr.sendMessage(message, msg.getAllRecipients());
        tr.close();
        
        logger.info("Closed open Transport - tr..");
   }catch(Exception ex){
       ex.printStackTrace();
   }
}


public Message attachFile(Transport tr, Properties props, javax.mail.Session session,String[] candidateids,String toEmail, String cc,String textOfEmail,String username) {
    Message message = new MimeMessage(session);
    try{
    logger.info("Begin attachFile method..");
    message.setFrom(new InternetAddress(from));
    message.addRecipient(Message.RecipientType.TO,
       new InternetAddress(toEmail));
    if (cc!= null){
        message.addRecipient(Message.RecipientType.CC,
        new InternetAddress(cc));
    }
        message.setSubject(subject);
        logger.info("Set Message to,from,cc and subject");
        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Fill the message - Remove comment from below and make messageText HTML by including html tags
      //  messageBodyPart.setContent(messageText, "text/html");
        messageBodyPart.setText(textOfEmail.trim());
        logger.info("Set body part");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        File[] attachments = null;  
      if (candidateids != null && candidateids.length > 0){
      
       for (int files = 0; files <= candidateids.length-1; files++){
                attachments = new File[files+1];
                attachments[files]  =  new File( new File("").getAbsoluteFile() + "/" + username + "/"+candidateids[files]);
       }
      } //no files to attach
    	  
      
        logger.info("Got attachement files - ." );
        // Part two is attachment
        if (attachments != null){
        for (int i = 0 ; i< attachments.length;i++){
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachments[i]);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(candidateids[i]);
        multipart.addBodyPart(messageBodyPart);
       }
      }
        logger.info("Put various multipart 2 - file attachments");
        // Put parts in message
        message.setContent(multipart);

        // Send the message
        //tr.send(message);
    }catch(Exception e){
        logger.debug("Exception during method execution of attachFile" + e.getMessage());
    e.printStackTrace();
 }
return message;

}
class SMTPAuthenticator extends javax.mail.Authenticator {

      public PasswordAuthentication getPasswordAuthentication() {
            String username = usernameProp;
            String password = pwd;
            return new PasswordAuthentication(username, password);
      }
}
}