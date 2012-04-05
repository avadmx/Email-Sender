/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emsender;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
/**
 *
 * @author root
 */
public class AdsMail {
    private Properties conn_props;
    private Properties sess_props;
    private Properties mess_props;
    private HashMap recipients;
    private Properties attachs;
    private Properties content;
    public List<String> errors= new ArrayList<String>();
    private boolean error;
    public static void send(){
        String host = "mail.adsourcing.com.mx";
		int port = 25;
		String username = "alfonso.valdez@adsourcing.com.mx";
		String password = "avd2005600";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
 
		Session session = Session.getInstance(props);
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("noreplay@adsourcing.com.mx"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("alfonso.valdez@adsourcing.com.mx"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler," +
					"\n\n No spam to my email, please!");
 
			Transport transport = session.getTransport("smtp");
			transport.connect(host, port, username, password);
 
			transport.sendMessage(message, message.getAllRecipients());
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    }
    public boolean go(){
      boolean eval=false;
      String host = this.conn_props.getProperty("host");
      int port = Integer.parseInt(this.conn_props.getProperty("send_port"));
      String username = this.conn_props.getProperty("username");
      String password = this.conn_props.getProperty("password");
      Session session = Session.getInstance(this.sess_props);
      try {
        Message message = new MimeMessage(session);
	message.setFrom(new InternetAddress(this.mess_props.getProperty("from")));        
        Set<String> keys=this.recipients.keySet();
        for(String key:keys){
            Message.RecipientType rt=null;
            String type=(String)this.recipients.get(key);
            if(type.equals("to")){
                rt=Message.RecipientType.TO;
                
            }else if(type.equals("cc")){
                rt=Message.RecipientType.CC;
            }else if(type.equals("bcc")){
                rt=Message.RecipientType.BCC;
            }
            message.setRecipients(rt,InternetAddress.parse(key));
        }
	message.setSubject(this.content.getProperty("subject"));
	message.setContent(this.content.getProperty("body"), this.content.getProperty("type")); 
	Transport transport = session.getTransport("smtp");
	transport.connect(host, port, username, password);
 	transport.sendMessage(message, message.getAllRecipients());
        eval=true;
       }catch (MessagingException e) {
	  throw new RuntimeException(e);
       }
       return eval;
    }
    public AdsMail(String string_xml_mail){
        Document docXML=null;
        try{
            docXML= DocumentHelper.parseText(string_xml_mail);
        }catch(DocumentException e){
          this.errors.add(e.toString());
        }
        this.init(docXML);
    }
    public AdsMail(Document xml_mail){
        this.init(xml_mail);
    }
    public void init(Document xml_mail){
        this.error=false;
        this.conn_props= new Properties();
        this.sess_props= new Properties();
        this.mess_props= new Properties();
        this.recipients= new HashMap();
        this.attachs= new Properties();
        this.content=new Properties();
        try{
          this.init_conn_props(xml_mail);
          this.init_sess_props(xml_mail);
          this.init_message(xml_mail);
        }catch(NullPointerException e){
            this.set_erros(e.toString());
        }
    }
    private void init_message(Document xml_mail){
        Element xml_mess_props=xml_mail.getRootElement().element("message");
        List<Attribute> atts = xml_mess_props.attributes();
        for(Attribute iAtt:atts){
          this.mess_props.put(iAtt.getName(), iAtt.getData());
        }
        this.init_recipients(xml_mail);
        this.init_attachs(xml_mail);
        this.init_content(xml_mail);
    }
    private void init_recipients(Document xml_mail){
        Element xml_mess_props=xml_mail.getRootElement().element("message").element("recipients");
        List<Element> recipients = xml_mess_props.elements();
        for(Element recip:recipients){
          this.recipients.put(recip.attribute("address").getData(),recip.attribute("type").getData());
        }
    }
    private void init_attachs(Document xml_mail){
        Element xml_mess_props=xml_mail.getRootElement().element("message").element("attachments");
        List<Element> attachments = xml_mess_props.elements();
        for(Element attach:attachments){
          this.attachs.put(attach.attribute("name").getData(),attach.element("b64_element").getData());
        }
    }
    private void init_content(Document xml_mail){
        Element xml_mess_props=xml_mail.getRootElement().element("message").element("content");
        List<Element> contents = xml_mess_props.elements();
        for(Element iEle:contents){
          this.content.put(iEle.getName(), iEle.getData());
        }
    }
    private void init_conn_props(Document xml_mail){
        Element xml_conn_props=xml_mail.getRootElement().element("conn_props");
        List<Element> elements=xml_conn_props.elements();
        for(Element iEle:elements){
          this.conn_props.put(iEle.getName(), iEle.getData());
        }
    }
    private void init_sess_props(Document xml_mail){
        Element xml_sess_props=xml_mail.getRootElement().element("sess_props");
        List<Element> elements=xml_sess_props.elements();
        for(Element iEle:elements){
            this.sess_props.put(iEle.attribute("name").getData(), iEle.attribute("value").getData());
        }
    }
    private void set_erros(String error_message)
    {
        this.error=true;
        this.errors.add(error_message);
    }
    public boolean has_errors(){
        return this.error;
    }
}
