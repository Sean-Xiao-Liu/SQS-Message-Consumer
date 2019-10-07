package com.xiao.soccerproject.sqsservice;



import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;


public class EmailService {

    private static Properties properties;
    private static Session session;
    private static MimeMessage mailMessage;




    public void sendEmail(Email email){
        properties = new Properties();

        properties.put("mail.smtp.host",System.getProperty("mail.smtp.host"));
        properties.put("mail.smtp.auth",System.getProperty("mail.smtp.auth"));
        properties.put("mail.smtp.starttle.enable",System.getProperty("mail.smtp.starttle.enable"));
        properties.put("mail.smtp.port",System.getProperty("mail.smtp.port"));
        properties.put("mail.user.name",System.getProperty("mail.user.name"));
        properties.put("mail.user.password",System.getProperty("mail.user.password"));

        System.out.println(">>>>>>>>>>>>>>>>" + properties);

        String mailServer = properties.getProperty("mail.smtp.host");
        String mailServerPort = properties.getProperty("mail.smtp.port");
        String mailUserName = properties.getProperty("mail.user.name");
        String mailUserPassword = properties.getProperty("mail.user.password");

        System.out.println("mail server is: " + mailServer + " ,mail server port is: " + mailServerPort + " , mail user name is:  " + mailUserName);

        if(mailServerPort.equalsIgnoreCase("465")) properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        session = Session.getInstance(properties,null);

        try{
            mailMessage = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(email.getFrom());
            mailMessage.setFrom(addressFrom);
            mailMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(email.getReplyTo()));// where to put recipient list?
            mailMessage.setSubject(email.getSubject());
            String emailBody = "Test to send email via SendGrid" + email.getText() + email.getSendTime();
            mailMessage.setContent(emailBody, "text/html");

            Transport transport = session.getTransport("smtp");
            transport.connect(mailServer,mailUserName,mailUserPassword);
            transport.sendMessage(mailMessage,mailMessage.getAllRecipients());
            transport.close();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
}

class Email{
    private String subject;
    private String from;
    private String replyTo;
    private String text;
    private LocalDateTime sendTime = LocalDateTime.now();

    public String getSubject() {
        return subject;
    }

    public String getFrom() {
        return from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getSendTime(){
        return sendTime;
    }

    public Email(String subject, String from, String replyTo, String text){ // Setter of Email
        this.from = from;
        this.subject = subject;
        this.replyTo = replyTo;
        this.text = text;


    }


}