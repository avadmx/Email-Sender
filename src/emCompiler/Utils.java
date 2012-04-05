/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emCompiler;
import java.text.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
import java.io.*;

/**
 *
 * @author root
 */
import java.text.DateFormat;
public class Utils {
    public static boolean isDate(String s_date){
        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");
        try{
          df.parse(s_date);
          return true;
        }catch(ParseException e){return false;}
    }
    public static Date toDate(String s_date){
        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");
        Date date=null;
        try{
          date = df.parse(s_date);
        }catch(ParseException e){}
        return date;
    }
    public static boolean isInt(String s_int){
        try{
          Integer.parseInt(s_int);
          return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    public static Document parseXML(String path){
        SAXReader reader = new SAXReader();
        File xml_file = new File(path);
        Document docXML=null;
        try{
            docXML= reader.read(xml_file);
        }catch(DocumentException e){}
        return docXML;
    }
}
