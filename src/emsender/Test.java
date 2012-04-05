/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emsender;

import java.util.*;

/**
 *
 * @author root
 */
public class Test {
  public HashMap periodicitys;
  public Test(){
    this.initPeriodicities();     
  }
  public void initPeriodicities()
  {
      this.periodicitys = new HashMap();
      //Dailys
      this.periodicitys.put("Basic Daily Periodicity 1","is 01/01/2012 daily every 3 days since 01/01/2012: true");
      this.periodicitys.put("Basic Daily Periodicity 2","is 02/01/2012 daily every 3 days since 01/01/2012: false");
      this.periodicitys.put("Basic Daily Periodicity 3","is 03/01/2012 daily every 3 days since 01/01/2012: false");
      this.periodicitys.put("Basic Daily Periodicity 4","is 04/01/2012 daily every 3 days since 01/01/2012: true");
      this.periodicitys.put("Basic Daily Range 1","is 04/01/2012 daily every 3 days since 01/01/2012 to 30/01/2012: true");
      this.periodicitys.put("Basic Daily Range 2","is 31/01/2012 daily every 3 days since 01/01/2012 to 30/01/2012: false");
      this.periodicitys.put("Basic Daily Range + Times 1","is 04/01/2012 daily every 3 days since 01/01/2012 to 30/01/2012 until 1 times: false");
      this.periodicitys.put("Basic Daily Range + Times 2","is 01/01/2012 daily every 3 days since 01/01/2012 to 30/01/2012 until 1 times: true");
      this.periodicitys.put("Basic Daily Times 1","is 01/01/2012 daily every 3 days since 01/01/2012 until 0 times: false");
      //Weeklys
      this.periodicitys.put("Basic Weekly 1","is 01/01/2012 weekly every 1 weeks since 01/01/2012: true");
      this.periodicitys.put("Basic Weekly 2","is 08/01/2012 weekly every 1 weeks since 01/01/2012: true");
      this.periodicitys.put("Basic Weekly 3","is 08/01/2012 weekly every 2 weeks since 01/01/2012: false");
      this.periodicitys.put("Basic Weekly 4","is 15/01/2012 weekly every 2 weeks since 01/01/2012: true");
      this.periodicitys.put("Basic Weekly 5","is 09/01/2012 weekly every 1 weeks since 01/01/2012: false");
      
  }
  public boolean evalPeriodicity(String sentence){  
    boolean returnValue=false;
    emCompiler.Compiler c = new emCompiler.Compiler(sentence);
        if(c.compile()){
           returnValue=emCompiler.Periodicity.execute(c.variables);
        }
    return returnValue;
  }   
  public void testPeriodicity()
  {
    Iterator it = this.periodicitys.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pairs = (Map.Entry)it.next();
        String sentence=pairs.getValue().toString().split(":")[0];
        boolean value=new Boolean(pairs.getValue().toString().split(":")[1].trim());
        boolean eval=this.evalPeriodicity(sentence);
        String testResult=value==eval?"Correct":"Fail";
        String output= testResult+" -  Test "+pairs.getKey().toString()+" : "+ sentence + " : " +value+ " => "+eval;
        System.out.println(output);
    }
  }
}
