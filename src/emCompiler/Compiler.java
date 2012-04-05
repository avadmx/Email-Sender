/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emCompiler;
import java.util.*;
/**
 *
 * @author root
 */
public class Compiler {
    private String[] statementsNames={"is"};
    public HashMap variables;
    private String str;
    private String[] st;
    private int iToken;
    
    public List<String> errors_messages;
    public boolean error_flag;
    
    public Compiler(String sentence){
        this.str=sentence;
        this.st=this.tokenize(this.str);
        this.iToken=0;
        this.errors_messages= new ArrayList<String>();
        this.error_flag=false;
        this.variables= new HashMap();
    }
    private void setVariable(String name, String value){
        this.variables.put(name, value);
    }
    public void setError(String error){
        this.error_flag=true;
        this.errors_messages.add(error);
    }
    public String token()
    {
      return this.st[this.iToken];
    }
    public String tokenAt(int index){
        return this.st[index];
    }
    public String nextToken(){
        iToken++;
        return this.token();
    }
    public String previousToken(){
        iToken--;
        return this.token();
    }
    public boolean hasMoreTokens(){
        return (this.iToken<this.st.length-1)?true:false;
    }
    public String[] tokenize(String string)
    {
        return this.str.split("\\s");
    }
    public boolean compile(){
            boolean eval=false;
            int state=0;
            while(this.hasMoreTokens()){
                switch(state){
                    case 0:
                        if(token().equals("is")){
                          eval=this.compile_is_statement();      
                        } 
                        break;
                }
            }
            return eval;
    }
   private boolean compile_is_statement(){
        boolean eval=false;
        int state=0;
        while(this.hasMoreTokens()){
            this.nextToken();
            switch(state){
                case 0:
                    if(Utils.isDate(token())){
                        this.setVariable("date", token());
                        state=1;
                    }
                    break;
                case 1:
                    if(this.compile_periodicity()){
                        state=2;
                    }
                    break;
            }
        }
        if(state==2 && !this.error_flag)eval=true;
        return eval;
    }
   private boolean compile_periodicity(){
        boolean eval=false;
        int state=0;
        while(this.hasMoreTokens()){
            switch(state){
                case 0:
                    if(this.compile_frecuency()){
                        state=1;
                    }
                    break;
                case 1:
                    if(this.compile_repetition()){
                        state=2;
                    }
                    break;
            }
        }
        if(state==1 || state==2)eval=true;
        return eval;
    }
   private boolean compile_frecuency(){
        boolean eval=false;
        int state=0;
        boolean exit=false;
        while(this.hasMoreTokens()&&!exit){
            switch(state){
                case 0:
                    if(this.token().equals("daily")){
                        if(this.compile_daily()){
                         state=1;
                        }
                    }else if(this.token().equals("weekly")){
                        if(this.compile_weekly()){
                         state=1;
                        }   
                    }
                    break;
            }
            if(state==1){
                eval=exit=true;
            }
        }
        return eval;
    }
   private boolean compile_repetition(){
       boolean eval=false;
       int state=0;
       boolean exit=false;
       while(this.hasMoreTokens()){
           switch(state){
               case 0:
                   if(this.compile_period()){
                      state=1;
                   }
                   break;
               case 1:
                   if(this.compile_times()){
                       state=2;
                   }
                   break;
           }
       }
       if(state==1 || state==2){
           eval=true;
       }
       return eval;
   }
   private boolean compile_times(){
       boolean eval=false;
       int state=0;
       boolean exit=false;
       while(this.hasMoreTokens() && !exit){
           this.nextToken();
           switch(state){
               case 0:
                   if(this.token().equals("until")){
                       state=1;
                   }
                   break;
               case 1:
                   if(Utils.isInt(this.token())){
                       this.setVariable("times", token());
                       state=2;
                   }
                   break;
               case 2:
                   if(this.token().equals("times")){
                       state=3;
                   }
                   break;
           }
           if(state==3)
           {
               eval=exit=true;
           }
       }
       return eval;
   }
   private boolean compile_period(){
       boolean eval=false;
       int state=0;
       boolean exit=false;
       while(this.hasMoreTokens() && !exit){
           this.nextToken();
           switch(state){
               case 0:
                   if(this.token().equals("since")){
                       state=1;
                   }else{
                       this.setError("Expecting 'since' instead: "+this.token());
                   }
                   break;
               case 1:
                   if(Utils.isDate(this.token())){
                      this.setVariable("since_date", token());
                      state=2;
                   }else{
                       this.setError("Excpecting a valid date instead: "+this.token());
                   }
               case 2:
                   if(this.hasMoreTokens()){
                       this.nextToken();
                       if(this.token().equals("to")){
                           state=3;
                       }else{
                           this.previousToken();
                           this.previousToken();
                           exit=true;
                       }
                   }
                   break;
               case 3:
                   if(Utils.isDate(this.token())){
                       this.setVariable("to_date", token());
                       state=2;
                   }else{
                       this.setError("Excpecting a valid date instead: "+this.token());
                   }
                   break;
           }
       }
       if(state==2){
           eval=exit=true;
       }else{
           this.setError("Not a valid period");
       }
       return eval;
   }
   private boolean compile_daily(){
       boolean eval=false;
       int state=0;
       boolean exit=false;
       while(this.hasMoreTokens() && !exit){
           this.nextToken();
           switch(state){
               case 0:
                   if(token().equals("every")){
                       state=1;
                   }else if(token().equals("every-weekday")){
                       this.setVariable("action", "is_daily_every_weekday");
                       state=4;
                   }else if(token().equals("every-weekendday")){
                       this.setVariable("action", "is_daily_every_weekendday");
                       state=5;
                   }
                   break;
               case 1:
                   if(Utils.isInt(token())){
                       this.setVariable("interval", token());
                       state=2;
                   }
                   break;
               case 2:
                   if(token().equals("days")){
                       this.setVariable("action", "is_daily_every");
                       state=3;
                   }
                   break;
           }
           if(state==3 || state==4 || state==5){
               exit=true;
               eval=true;
           }
       }
       return eval;
   }
   private boolean compile_weekly(){
       boolean eval=false;
       int state=0;
       boolean exit=false;
       while(this.hasMoreTokens() && !exit){
           this.nextToken();
           switch(state){
               case 0:
                   if(token().equals("every")){
                       state=1;
                   }
                   break;
               case 1:
                   if(Utils.isInt(token())){
                       this.setVariable("interval", token());
                       state=2;
                   }
                   break;
               case 2:
                   if(token().equals("weeks")){
                       this.setVariable("action", "is_weekly_every");
                       state=3;
                   }
                   break;
           }
           if(state==3){
               exit=true;
               eval=true;
           }
       }
       return eval;
   }
}
