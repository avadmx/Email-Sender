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
public class Periodicity {
    public static int week_interval=7;
    public static String[] periods={"daily","weekly","monthly","yearly"};
    public static boolean isPeriod(String period){
        boolean eval_result=false;
        for(String iPeriod:Periodicity.periods){
            if(period.equals(iPeriod)){eval_result=true;}
            if(eval_result)break;
        }
        return eval_result;
    }
    public static boolean is_weekly_every(HashMap args){
        boolean eval_result=false;
        String s_date=(String)args.get("date");
        int interval = Integer.parseInt((String)args.get("interval"));
        
        String s_since_date=args.get("since_date")==null?"":(String)args.get("since_date");
        String s_to_date=args.get("to_date")==null?"":(String)args.get("to_date");
        
        String until_times=args.get("times")==null?"":(String)args.get("times");
        Integer times = until_times!=""? new Integer(until_times):null;
        Date date = Utils.toDate(s_date);
        Date since_date = Utils.toDate(s_since_date);
        Date to_date= Utils.toDate(s_to_date);
        Calendar pivot_c = Calendar.getInstance();
        pivot_c.setTime(since_date);
        
        int index_times=1;
        while(is_between(pivot_c.getTime(),since_date,to_date) 
              && is_between(date,since_date,to_date)
              && is_eq_before(pivot_c.getTime(),date)){ 
            if(times!=null)
                if(times<index_times)
                    break;
            if(pivot_c.getTime().compareTo(date)==0){
                        eval_result=true;
            }
            if(eval_result)break;
            pivot_c.add(Calendar.DATE, Periodicity.week_interval*interval);
            index_times++;
        }
        
        return eval_result;
    }
    public static boolean is_daily_every(HashMap args){
        boolean eval_result=false;
        
        String s_date=(String)args.get("date");
        int interval = Integer.parseInt((String)args.get("interval"));
        
        String s_since_date=args.get("since_date")==null?"":(String)args.get("since_date");
        String s_to_date=args.get("to_date")==null?"":(String)args.get("to_date");
        
        String until_times=args.get("times")==null?"":(String)args.get("times");
        Integer times = until_times!=""? new Integer(until_times):null;
        Date date = Utils.toDate(s_date);
        Date since_date = Utils.toDate(s_since_date);
        Date to_date= Utils.toDate(s_to_date);
        Calendar pivot_c = Calendar.getInstance();
        pivot_c.setTime(since_date);
        
        int index_times=1;
        while(is_between(pivot_c.getTime(),since_date,to_date) 
              && is_between(date,since_date,to_date)
              && is_eq_before(pivot_c.getTime(),date)){ 
            if(times!=null)
                if(times<index_times)
                    break;
            if(pivot_c.getTime().compareTo(date)==0){
                        eval_result=true;
            }
            if(eval_result)break;
            pivot_c.add(Calendar.DATE, interval);
            index_times++;
        }
        return eval_result;
    }
    public static boolean is_daily_every_weekday(HashMap args){
        boolean eval_result=false;
        String s_date=(String)args.get("date");
        String s_since_date=args.get("since_date")==null?"":(String)args.get("since_date");
        String s_to_date=args.get("to_date")==null?"":(String)args.get("to_date");
        
        
        Date date = Utils.toDate(s_date);
        Date since_date=Utils.toDate(s_since_date);
        Date to_date=Utils.toDate(s_to_date);
        
        
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if(is_weekday(c.get(Calendar.DAY_OF_WEEK)) && is_between(date,since_date,to_date))
            eval_result=true;
        return eval_result;
    }
    public static boolean is_daily_every_weekendday(HashMap args){
        boolean eval_result=false;
        String s_date=(String)args.get("date");
        String s_since_date=args.get("since_date")==null?"":(String)args.get("since_date");
        String s_to_date=args.get("to_date")==null?"":(String)args.get("to_date");
        
        
        Date date = Utils.toDate(s_date);
        Date since_date=Utils.toDate(s_since_date);
        Date to_date=Utils.toDate(s_to_date);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if(!is_weekday(c.get(Calendar.DAY_OF_WEEK)) && is_between(date,since_date,to_date))
            eval_result=true;
        return eval_result;
    }
    private static boolean is_weekday(int day){
        if(day>=2 && day<=6)
            return true;
        return false;
    }
    private static boolean is_between(Date date,Date since_date, Date to_date){
        if(to_date!=null){
            if(is_eq_after(date,since_date) && is_eq_before(date,to_date) )
                return true;
        }else{
            if(is_eq_after(date,since_date))
                return true;
        }
        return false;
    }
    private static boolean is_eq_after(Date date,Date since_date){
        boolean eval_result=false;
        if(!date.before(since_date)){
            eval_result=true;
        }
        return eval_result;
    }
    private static boolean is_eq_before(Date date, Date to_date){
        boolean eval_result=false;
        if(!date.after(to_date)){
            eval_result=true;
        }
        return eval_result;
    }
    public static boolean execute(HashMap args){
        boolean exec_result=false;
        String action = args.get("action")==null?"":(String)args.get("action");
        if(action.equals("is_daily_every")){
            exec_result=Periodicity.is_daily_every(args);
        }else if(action.equals("is_daily_every_weekday")){
            exec_result=Periodicity.is_daily_every_weekday(args);
        }else if(action.equals("is_daily_every_weekendday")){
            exec_result=Periodicity.is_daily_every_weekendday(args);
        }else if (action.equals("is_weekly_every")){
            exec_result=Periodicity.is_weekly_every(args);
        }
        return exec_result;
    }
}
