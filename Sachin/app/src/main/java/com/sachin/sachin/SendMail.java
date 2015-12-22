package com.sachin.sachin;

/**
 * Created by samurai on 21-Nov-15.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
    private static final String username = "sachinrohit.e@gmail.com";
    private static final String password = "samurai$*";
    public static Context myappContext;


    private MimeMessage createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
       // Message
/*
        WORKING CODE : PLAIN MESSAGE
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sachinrohit.e@gmail.com", "Sachin Rohit"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);//, "text/html" , "utf-8");
*/


        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sachinrohit.e@gmail.com", "Sachin Rohit"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody, "utf-8", "html");//, "text/html" , "utf-8");
        //message.setContent(messageBody, "text/html; charset=utf-8");

/*        Multipart _multipart = new MimeMultipart();
        // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart*//*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sachinrohit.e@gmail.com", "Le Pape Fashion Store"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setSentDate(new Date());
        // setup message body
        BodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setContent(messageBody, "text/html");
        _multipart.addBodyPart(messageBodyPart);

        // Put parts in message
        message.setContent(_multipart, "text/html");*/

        return message;
    }

    private Session createSessionObject() {


        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // https://java.net/projects/javamail/pages/OAuth2
/*
        Properties props = new Properties();
        props.put("mail.imap.ssl.enable", "true"); // required for Gmail
        props.put("mail.imap.sasl.enable", "true");
        props.put("mail.imap.sasl.mechanisms", "XOAUTH2");
        props.put("mail.imap.auth.login.disable", "true");
        props.put("mail.imap.auth.plain.disable", "true");
        props.setProperty("mail.store.protocol", "imap");
        Session session = Session.getInstance(props);
        Store store = null;
        try {
            store = (IMAPStore) session.getStore("imap");
            String username = "48.rohit@gmail.com";
            String oauth2_access_token = "969002628016-u2k88407lug0b898186ent4baeeqsvbc.apps.googleusercontent.com";
            store.connect("imap.gmail.com", username, oauth2_access_token);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

*/
    }

     public void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            MimeMessage message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private class SendMailTask extends AsyncTask<MimeMessage, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(myappContext, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(MimeMessage... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
