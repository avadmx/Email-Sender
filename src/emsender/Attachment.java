/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emsender;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.*;
import org.apache.commons.codec.binary.*;
import org.apache.commons.io.*;
/**
 *
 * @author root
 */
public class Attachment{
   
    public static StringBuilder b64_stringify_file(String path){
        PathType p_type=get_path_type(path);
        StringBuilder s_builder = null;
        switch(p_type){
            case web:
                s_builder=b64_stringify_web_file(path);
                break;
            case local:
                s_builder=b64_stringify_local_file(path);
                break;
            default:
                s_builder=new StringBuilder();
                break;
        }
        return s_builder;
    }
    private static StringBuilder b64_stringify_local_file(String path){
        File l_file = new File(path);
        StringBuilder s_builder=null;
        FileInputStream fi_stream=null;
        try{
            byte[] fb_buffer = new byte[(int)l_file.length()];
            fi_stream = new FileInputStream(l_file);
            fi_stream.read(fb_buffer, 0,(int)l_file.length());
            fi_stream.close();
            s_builder = b64_stringify(fb_buffer);
        }catch(FileNotFoundException e){
            return new StringBuilder();
        }catch(IOException ioe){
            return new StringBuilder();
        }
        return s_builder;
    }
    private static StringBuilder b64_stringify_web_file(String s_url){
        InputStream i_stream = null;
        StringBuilder s_builder = null;
        try {
            URL url = new URL(s_url);
            i_stream=url.openStream();
            s_builder = b64_stringify(IOUtils.toByteArray(i_stream));
        } catch (IOException ex) {
            s_builder = new StringBuilder();
        }        
        return s_builder;
    }
    private static StringBuilder b64_stringify(byte[] buffer){
        Base64 b64 = new Base64();
        StringBuilder s_builder = new StringBuilder(b64.encodeToString(buffer));    
        return s_builder;
    }
    public static byte[] b64_bytegify_string(String b64_string){
        Base64 b64 = new Base64();
        return b64.decode(b64_string);        
    }
    private static PathType get_path_type(String path){
        if(isURL(path)){
            return PathType.web;
        }
        else if(isLocal(path)){
            return PathType.local;
        }
        return PathType.unkwon;
    }
    public static String get_file_name(String path){
        if(isURL(path)){
            URL url;
            try {
                url = new URL(path);
                return url.getFile().replace('/', '-');
            } catch (MalformedURLException ex) {
            }
            
        }else if(isLocal(path)){
            File f = new File(path);
            return f.getName();
        }
        return "";
    }
    private static boolean isURL(String path){
        try {
            URL url = new URL(path);
            return true;
        } catch (MalformedURLException exc) {
           return false;
        }
    }
    private static boolean isLocal(String path){
        File attach = new File(path);
        return attach.exists();
    }
    enum PathType{local,web,unkwon}
}
