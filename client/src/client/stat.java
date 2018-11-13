/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;





 import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mnm
 */
public class stat {

public String printUsage() {
  OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
double cpu=0,fmem=0,tmem=1;
  for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
    method.setAccessible(true);
//    getTotalPhysicalMemorySize
//
//

    if (method.getName().startsWith("getTotalPhysicalMemorySize")&& Modifier.isPublic(method.getModifiers()))
    {
        try {
            tmem= Double.parseDouble(method.invoke(operatingSystemMXBean)+"");
        } catch (Exception e) {
            
        } 
        
    }
    if (method.getName().startsWith("getFreePhysicalMemorySize")&& Modifier.isPublic(method.getModifiers()))
    {
        try {
            fmem= Double.parseDouble(method.invoke(operatingSystemMXBean)+"");
        } catch (Exception e) {
            
        } 
        
    }
    if (method.getName().startsWith("getSystemCpuLoad")&& Modifier.isPublic(method.getModifiers()))
    {
        try {
            cpu= Double.parseDouble(method.invoke(operatingSystemMXBean)+"");
        } catch (Exception e) {
            
        } 
        
    }
    
  } // for
  
  ;
  return "cpu usage:"+String.format("%.2f", cpu)+"(%) \tMemory Usage:"+String.format("%.2f", ((tmem-fmem)/tmem)*100)+"(%)";
}

public static void main(String a[]){
    stat s=new stat();
    System.out.println(s.printUsage());
}
}
