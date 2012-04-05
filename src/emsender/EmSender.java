/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emsender;

import com.sun.org.apache.xpath.internal.operations.Equals;
import emCompiler.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import java.io.*;
import org.dom4j.*;


/**
 *
 * @author root
 */
public class EmSender {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String action = args.length>0?args[0]:new String();
        if(action.equals("help")){
           EmSender.helping();
        }else if(action.equals("stringify-file")){
           EmSender.stringify_file(args);
        }else if(action.equals("periodicity")){
           EmSender.periodicity(args);
        }else if(action.equals("bytegify-string")){
           EmSender.bytegify_string(args);
        }else if(action.equals("file-to-xml")){
           EmSender.file_to_xml(args); 
        }else if(action.equals("xml-to-file")){
           EmSender.xml_to_file(args);
        }else if(action.equals("send-email")){
           EmSender.send_email(args);
        }else if(action.equals("test")){
           EmSender.test(args);
        }
        else{
           EmSender.unkwon();
        }
    }
    private static void unkwon(){
        System.out.println("Nothing to do, try option 'help'");
    }
    private static void helping(){
         System.out.println("Usage:");
         System.out.println("  [action] [args]* [options]* ");
         System.out.println("");
         System.out.println("Actions:");
         System.out.println("  help                                              #Show help");
         System.out.println("  stringify-file [file-path] [destination-path]     #Convert to base 64 string the bytes of a local or web file");
         System.out.println("  periodicity    [sentence]                         #Compile and Execute a periodicity statement");
         System.out.println("  bytegify-string");
         System.out.println("  file-to-xml");
         System.out.println("  xml-to-file");
         System.out.println("  send-email");
    }
    private static void stringify_file(String[] args){
        String path=args.length>1?args[1]:"";
        String destination_path=args.length>2?args[2]:"";
        String s=Attachment.b64_stringify_file(path).toString();
        if(destination_path.equals("")){
          System.out.println(s);    
        }else{
          File f = new File(destination_path);
            try {
                FileUtils.writeStringToFile(f, s);
            } catch (IOException ex) {
                Logger.getLogger(EmSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static void bytegify_string(String[] args){
        String destination_path=args.length>1?args[1]:"";
        String string_path=args.length>2?args[2]:"";
        File f = new File(destination_path);
        File s_file=new File(string_path);
        try {
            String string_file=FileUtils.readFileToString(s_file);
            FileUtils.writeByteArrayToFile(f, Attachment.b64_bytegify_string(string_file));
        } catch (IOException ex) {
            Logger.getLogger(EmSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void periodicity(String[] args){
        String sentence=args.length>1?args[1]:"";
        String option=args.length>2?args[2]:"";
        emCompiler.Compiler c = new emCompiler.Compiler(sentence);
        if(c.compile()){
           System.out.println(Periodicity.execute(c.variables));
        }else{
           for(String e:c.errors_messages)
                    System.out.println(e);
        }
    }
    private static void file_to_xml(String[] args){
        String path=args.length>1?args[1]:"";
        String destination_path=args.length>2?args[2]:"";
        String s="<attachment name='"+Attachment.get_file_name(path)+"'>";
        s+="<b64_bytes>"+Attachment.b64_stringify_file(path).toString()+"</b64_bytes>";
        s+="</attachment>";
        if(destination_path.equals("")){
          System.out.println(s);    
        }else{
          File f = new File(destination_path);
            try {
                FileUtils.writeStringToFile(f, s);
            } catch (IOException ex) {
                Logger.getLogger(EmSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static void xml_to_file(String[] args){
        String path=args.length>1?args[1]:"";
        String destination_path=args.length>2?args[2]:"";
        Document xml = Utils.parseXML(path);
        String file_name=xml.getRootElement().attributeValue("name");
        String s_bytes=(String)xml.getRootElement().element("b64_bytes").getData();
        destination_path+=file_name;
        File f = new File(destination_path);
        try {
            FileUtils.writeByteArrayToFile(f, Attachment.b64_bytegify_string(s_bytes));
        } catch (IOException ex) {
            Logger.getLogger(EmSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private static void send_email(String[] args){
        AdsMail adsMail = new AdsMail(args[1]);
        String response="";
        if(adsMail.has_errors()){
            response="false";
            for(String error:adsMail.errors){
                response+=":"+error;
            }
        }else if(adsMail.go()){  
            response="true";
        }
        System.out.println(response);
    }
    private static void test(String[] args){
        Test t = new Test();
        t.testPeriodicity();
    }
}