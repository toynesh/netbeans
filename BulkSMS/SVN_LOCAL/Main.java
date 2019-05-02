package com.pdsl.main;
import cn.com.huawei.www.schema.common.v2_1.RequestSOAPHeader;
import cn.com.huawei.www.schema.common.v2_1.RequestSOAPHeaderE;
import com.huawei.sdp.sms.soap.client.NonceGenerator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;//04215277643/0721313812
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.ChargingInformation;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.SendSms;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.SendSmsE;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.SendSmsResponseE;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub.SimpleReference;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.Vector;
import com.pdsl.properties.LoadProp;
import airtel7.Airtel7;
//import com.pdsl.process.Submission;
//import com.pdsl.process.SenderDeposit;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.pdsl.main.TestStatus;
import com.pdsl.bean.DBBean;
import com.pdsl.sql.GolfPostQuery;
import java.math.BigDecimal;
import com.pdsl.sql.Datastore;
import java.util.Random;
import airtel7.Airtel7;
import ronogi.RONOGI;
//import com.pdsl.staginggi.StagingGI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import com.pdsl.airtelmoney.AirtelMoneyGI;
import kcsgilive.GIgaming;
import gitest1.Kcsliveresults;
import com.pdsl.airtelsms.KCSairtel;


public class Main {

    private static Connection con;
    private static java.sql.Statement s;
    private static java.sql.ResultSet rs;
    private static PreparedStatement pst;
    private  static String  correlator = "KCSManagementAcount";
    private static    Logger log = Logger.getLogger(Main.class.getName());
	
	static DBBean bean=new DBBean();
        static KCSairtel test = new KCSairtel();
        static AirtelMoneyGI airtelgi = new AirtelMoneyGI();
        static GIgaming gi = new GIgaming();
        static RONOGI gistage = new RONOGI();
        static Kcsliveresults gilive = new Kcsliveresults();
	static Datastore data=new Datastore();
    public static void main(String args[]) throws Exception{
 
        con=null;
        s=null;
        pst=null;
        rs=null;
         

         String url= "jdbc:mysql://localhost/KcsMngtAcc?user=root&password=1root2";
         Runtime run = Runtime.getRuntime();
         long availMem = run.freeMemory();

        Thread t = new Thread();
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "Starting the Application", "");
        System.out.println("At program start we have : " + availMem + " bytes");
        do{

            try{

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url);

            }catch(ClassNotFoundException cnfex){
                cnfex.printStackTrace();
            }
             Main chk = new Main();
             GolfPostQuery queryUpdate= new GolfPostQuery();
              
             queryUpdate.queryKannel();
             queryUpdate.queryMailInsert();
             queryUpdate.queryUpdateKannel(1);
             queryUpdate.queryMobilewallet();
             queryUpdate.queryMobileInsert();
             queryUpdate.queryUpdateMobile(1);

      if(checkSafcomsmsIfExist()){

       // SmsQCheck();
      }else{

         getValues();
        }
        if(chk.getStartimer()==true){

             if(chk.getTimechecker()==true){
             
               // SmsQCheck();
             	
             
             }if(chk.getTimecheckerResend()==true  ){
              System.out.println("SUB EXIST ??::"+chk.getTimechecker());
              Vector up=new Vector();
                    up.clear();
                    up=bean.getContactsForSub();
                 System.out.println("Records for Subs total:"+up.size());

                int  success= bean.setRecordsForSub(up);
                System.out.println("Success is No:"+success);
                          
              }

             }else{
               chk.pastscheduledtime();
		}
         //
        
        
        t.sleep(500);
        
       } 

           
        while(true);

    }

   public static boolean SmsUpdate2(int msgid) throws Exception{
        boolean temp = false;
        String sql = "update inbound set status =1 where inbound_id ="+msgid;
        try{
            s = con.createStatement();
            int i = s.executeUpdate(sql);
            if (i>0) {
                temp = true;
            }
            else{
                temp = false;
            }
        }
        catch(Exception e){e.printStackTrace();}
        finally{

        }
        return temp;
    }
public static void setRegistrationStatus(String msisdn) throws Exception{
        //boolean temp = false;
        String sql = "update Transaction_accnt set status =1 where msisdn ='"+msisdn+"'";
        try{
            s = con.createStatement();
            int i = s.executeUpdate(sql);
            
        }
        catch(Exception e){e.printStackTrace();}
        finally{

        }
        
    }

	//(isNumericString)
	
public static boolean isNumericString(String input) {
    boolean result = false;

    if(input != null && input.length() > 0) {
        char[] charArray = input.toCharArray();

        for(char c : charArray) {
            if(c >= '0' && c <= '9') {
                // it is a digit
                result = true;
            } else {
                result = false;
                break;
            }
        }
    }

    return result;
}

public static String  req(String msisdn, String message){
      String results=null;

       try {

           URL url = new URL ("http://41.223.58.157:55000/");

           String encoding = "cGRzMTIzOmFmcjIwY2E=";



           HttpURLConnection connection = (HttpURLConnection) url.openConnection();

           connection.setRequestMethod("POST");

           connection.setDoInput(true);

           connection.setDoOutput(true);

           connection.setRequestProperty  ("Authorization", "Basic " + encoding);

           connection.setRequestProperty  ("HOST", "197.248.31.217");

           connection.setRequestProperty  ("Content-Type", "text/xml");

           OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());



           writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><message><sms type=\"mt\"><destination><address><number type=\"international\">"+msisdn+"</number></address></destination><source><address><number type=\"unknown\">22225</number></address></source><rsr type=\"all\"/><ud type=\"text\" encoding=\"default\">"+message+"</ud></sms></message>");

           writer.flush();

           

           InputStream content = (InputStream)connection.getInputStream();

           BufferedReader in   = 

               new BufferedReader (new InputStreamReader (content));

           String line;

           while ((line = in.readLine()) != null) {

               System.out.println(line);
               results=line;

           }

       } catch(Exception e) {

           e.printStackTrace();

       }
return results;

  

   }

public static  void getValues() {

  try{ 
    Date cal2 = new Date();

    System.out.println("\nDate is:"+cal2);

   

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Calendar calendar = Calendar.getInstance();



    String time2 = formatter.format(cal2);

    System.out.println("\n\nDate format:"+time2);







        int year       = calendar.get(Calendar.YEAR);

	int month      = calendar.get(Calendar.MONTH);

    

        int  date = calendar.get(Calendar.DAY_OF_MONTH);





        System.out.println("yearInLong = "+year);

     

   

    String month1=null;

    String date1=null;

    if(month<10 ){

    month1="0"+Integer.toString(month+1);

    }else{

    month1=Integer.toString(month+1);

    }

   if(date<10 ){
 

    date1="0"+Integer.toString(date);



    }else{

   date1=Integer.toString(date);

    }

        String enddate=Integer.toString(year)+month1+date1+"235958";
      
       
        String details=Airtel7.getTransactionByTime("8392451", "kEn#ch@43", "20140519161600","20160408235959");
        if(details==null){

     System.out.println("CONNECTION NI JIDAA MINGI!!:");

    }else{
     System.out.println("Responses:"+details);
         if(counterAirtelcomma(details)!=0){
        System.out.println("Airtel Counter:"+counterAirtelcomma(details));
         
        
         System.out.println(getAirtelMoneysmsArray(getAirtelTransactions(details)));
         }else{

         System.out.println(getAirtelMoneysms(details));

         }
        }
    } catch(Exception e){e.printStackTrace();}
        
     }

//(checkRegistered(msisdn))
//     [java] Responses:[1505027548500£254739103382£50£15£LOTTO£20150520132332£GEOFFREY£WAWERU]

/*[1506031280523£254734411383£50£15£LUCKY3£20150623214359£NANCY£WAWERU],
 [1506031275857£254734411383£50£15£DEPOSIT£20150623210027£NANCY£WAWERU],
 [1506031275305£254734411383£50£15£LOTTO£20150623205619£NANCY£WAWERU]*/
public static  Boolean getTimechecker(){
 
 TestStatus process = new TestStatus();
 System.out.println("Reading timer");
 String date=process.getTimeDate();
 //String time=process.getTimeChecker();
 System.out.println("\n\n\n Date is :"+date);
 

 return getTimecheckerResend();
}

public static int counterAirtelcomma(String sms ){
		int countercomma=0;

		for(int i=0;i<sms.length();i++){
			
			if(sms.charAt(i)==','){
				countercomma++;
			}
		}
		return countercomma;
		
	}

public static  Boolean getTimecheckerResend(){
 Boolean ck=false;
 TestStatus process = new TestStatus();
 String times=process.getTimeChecker().trim();
if(times.equals("13:06")  ||times.equals("10:30") ){
 ck=true;  
}else{
 ck=false;  
}
 System.out.println("Uploading Status:"+ck);

 return ck;
}


public static String[] getAirtelTransactions(String sms ){


        


        String result=null;
        int counter= counterAirtelcomma(sms);
        String[] tempairtel= sms.split(",",counter+1);

      
        return tempairtel;

}

public static int counterallcommas(String sms ){
		int countercomma=0;

		for(int i=0;i<sms.length();i++){
			
			if(sms.charAt(i)==','){
				countercomma++;
			}
		}
		return countercomma;
		
	}

public static String getAirtelMoneysmsArray(String[] sms ) throws Exception {



        //String[] smss=sms;
        String output=null;
       try{
        
       for(int a=0;a<sms.length;a++){
        String ReferenceId="";
         String mobileno="";
	String amount="";
        String charges="";
        String gametype="";
        String date_timestamp="";
        String firstname="";
        String lastname="";
        String result=null;
        int openbra=sms[a].indexOf("[");
        int closedbra=sms[a].lastIndexOf("]");
        String sms2=sms[a].substring(openbra+1,closedbra);
        String[] tempairtel= sms2.split("£",8);
   
      for(int b =0; b < tempairtel.length ; b++){
        ReferenceId=tempairtel[0];
	mobileno=tempairtel[1];
        amount=tempairtel[2];
        charges=tempairtel[3];
        gametype=tempairtel[4];
        date_timestamp=tempairtel[5];
        firstname=tempairtel[6];
        lastname=tempairtel[7];


        }
    String name=firstname+" "+lastname;
//   mobileno="254730214600";
  //ReferenceId="Ref_airtelTest3";
 String REF2="AIRTEL_"+ReferenceId;
//amount="0";

  boolean chk1=checkregairtel(REF2,mobileno);
  System.out.println("\n\nCODE EXIST :"+chk1);
 
    if(chk1==false){
    

 String insertinbound = "insert into inbound_airtel (msisdn,message,correlator) values (" +mobileno + ",'" +gametype+ "','" +REF2+ "') ";
  
   data.insert(insertinbound);

    //String REF=Integer.toString(getAirtelRef(mobileno,ReferenceId));
  // String update="update inbound_airtel set status =1 where correlator='"+ReferenceId+"'";


String requ = "<SMSAPI><MSISDN>" + mobileno + "</MSISDN><SESSIONID>5000122</SESSIONID><MESSAGE>" + gametype + "</MESSAGE><MPESAREF>" + name + "~" + amount + "~" + mobileno + "~" + REF2 + "</MPESAREF></SMSAPI>";

String acceptresp = airtelgi.mpesaPDSL(requ);

String  output1=null;
String  output2=null;
                                  System.out.println("From Gi Live:>> "+acceptresp);
if(acceptresp.contains("PER")){

output2="STRICTLY Ksh.50 PER GAME.For enquires Call 0723383149.";
}else if(acceptresp.contains("Not")){
output2="Your bet is being processed";

}else if(acceptresp.contains("Wrong")){

output2="Wrong account no, Should be lucky 3 or lotto or kenno";
}

else{
                                   output1= getExtractedRes(acceptresp);
                                  System.out.println("Ouput 1:>> "+output1);
				   output2=getExtractedFinal(output1);
                                  System.out.println("Ouput 2:>> "+output2);
}
String req=req(mobileno,output2);
System.out.println("Airtel Response:"+req);
if (req.equalsIgnoreCase("Ok")){
String insert = "insert into outbound_airtel (msisdn,message,ReferenceID,status) values (" +mobileno + ",'" + output2 + "','" +REF2 + "'," +1+ ")";
             data.insert(insert);
}else{
String insert = "insert into outbound_airtel (msisdn,message,ReferenceID,status) values (" +mobileno + ",'" + output2 + "','" +REF2 + "'," +0+ ")";
             data.insert(insert);

}

  



       }else{ System.out.println("\n TRANSACTION REF:"+ReferenceId+" =>Exist  !!!"); }






  System.out.println( a+ "\nReference Id:"+ReferenceId+"\nMobile No:"+mobileno+"\nAmount:"+amount+"\nCharges:"+charges+"\nGame Type:"+gametype+"\nDate:"+date_timestamp+"\nFirst Name:"+firstname+"\nLast Name:"+lastname);

  System.out.println("===================================================");


        output="\nReference Id:"+ReferenceId+"\nMobile No:"+mobileno+"\nAmount:"+amount+"\nCharges:"+charges+"\nGame Type:"+gametype+"\nDate:"+date_timestamp+"\nFirst Name:"+firstname+"\nLast Name:"+lastname+"===================================================" ;
 }
}catch(Exception e){e.printStackTrace();}
return output;
}

public static String getAirtelMoneysms(String sms ) throws Exception {


        String ReferenceId="";
        String mobileno="";
	String amount="";
        String charges="";
        String gametype="";
        String date_timestamp="";
        String firstname="";
        String lastname="";
        String output=null;
        int openbra=sms.indexOf("[");
        int closedbra=sms.lastIndexOf("]");
        String sms2=sms.substring(openbra+1,closedbra);
        String[] tempairtel= sms2.split("£",8);
        String result=null;
     int counter= counterAirtelcomma(sms);
     try{

      for(int b =0; b < tempairtel.length ; b++){
        ReferenceId=tempairtel[0];
	mobileno=tempairtel[1];
        amount=tempairtel[2];
        charges=tempairtel[3];
        gametype=tempairtel[4];
        date_timestamp=tempairtel[5];
        firstname=tempairtel[6];
        lastname=tempairtel[7];


        }
   
     String name=firstname+" "+lastname;
     String REF2="AIRTEL_"+ReferenceId;
//amount="0";

  boolean chk1=checkregairtel(REF2,mobileno);
  System.out.println("\n\nCODE EXIST :"+chk1);
 
    if(chk1==false){
    

 String insertinbound = "insert into inbound_airtel (msisdn,message,correlator) values (" +mobileno + ",'" +gametype+ "','" +REF2+ "') ";
  
   data.insert(insertinbound);

    //String REF=Integer.toString(getAirtelRef(mobileno,ReferenceId));
  // String update="update inbound_airtel set status =1 where correlator='"+ReferenceId+"'";


String requ = "<SMSAPI><MSISDN>" + mobileno + "</MSISDN><SESSIONID>5000122</SESSIONID><MESSAGE>" + gametype + "</MESSAGE><MPESAREF>" + name + "~" + amount + "~" + mobileno + "~" + REF2 + "</MPESAREF></SMSAPI>";

String acceptresp = airtelgi.mpesaPDSL(requ);

String  output1=null;
String  output2=null;
                                  System.out.println("From Gi Live:>> "+acceptresp);
if(acceptresp.contains("PER")){

output2="STRICTLY Ksh.50 PER GAME.For enquires Call 0723383149.";
}else if(acceptresp.contains("Not")){
output2="Your bet is being processed";

}else if(acceptresp.contains("Wrong")){

output2="Wrong account no, Should be lucky 3 or lotto or kenno";
}

else{
                                   output1= getExtractedRes(acceptresp);
                                  System.out.println("Ouput 1:>> "+output1);
				   output2=getExtractedFinal(output1);
                                  System.out.println("Ouput 2:>> "+output2);
}
String req=req(mobileno,output2);
System.out.println("Airtel Response:"+req);
if (req.equalsIgnoreCase("Ok")){
String insert = "insert into outbound_airtel (msisdn,message,ReferenceID,status) values (" +mobileno + ",'" + output2 + "','" +REF2 + "'," +1+ ")";
             data.insert(insert);
}else{
String insert = "insert into outbound_airtel (msisdn,message,ReferenceID,status) values (" +mobileno + ",'" + output2 + "','" +REF2 + "'," +0+ ")";
             data.insert(insert);

}
       }else{ System.out.println("\n TRANSACTION REF:"+ReferenceId+" =>Exist  !!!") ;}
     
    }catch(Exception e){e.printStackTrace();}
return result;

}

 /*public static int checkReferenceDetails(String ref, String mobileno) {
        int regg = 0;
        String sql = "select count(*) as Counter  from inbound where msisdn ="+mobileno+" and correlator='"+ref+"'";
             System.out.println(sql);  
      try{
            s = con.createStatement();
            rs = s.executeQuery(sql);
            while(rs.next()){	 
			regg=rs.getInt("Counter");
		}
        }
        catch(Exception e){e.printStackTrace();}
        finally{

        }
        return regg;
    }*/

public static  Boolean checkregairtel(String ref, String mobileno){
               Boolean chk1 =false;
               String sql = "select msisdn,correlator  from inbound_airtel where msisdn ="+mobileno+" and correlator='"+ref+"'";
         try{
            s = con.createStatement();
            rs = s.executeQuery(sql);
            while(rs.next()){	 
			if (rs.getString("msisdn").trim().equals(mobileno)&& rs.getString("correlator").trim().equals(ref) )
			{	
                                
                               
				chk1 = true;
				break;
			}
			else
			{	

				chk1 = false;
				
			}
		}
           }
        catch(Exception e){e.printStackTrace();
           }
              
          
           return chk1;//1428623824  22119645517
        }
public static  int  getAirtelRef(String mobileno, String ref){
               Boolean chk1 =false;
int inboundID=0;
               String sql = "select inbound_id  from inbound_airtel where msisdn ="+mobileno+" and correlator='"+ref+"'";
         try{
            s = con.createStatement();
            rs = s.executeQuery(sql);
           rs.next();
		inboundID=rs.getInt("inbound_id");
           }
        catch(Exception e){e.printStackTrace();
           }
              
          
           return inboundID;//1428623824  22119645517
        }
public static  Boolean checkSafcomsmsIfExist(){
               Boolean chk1 =false;
               String sql = "select count(*) as Counter from inbound where status=0  and(msisdn like'25473%' or msisdn like'25478%' or msisdn like'25470%' or msisdn like'25471%' or msisdn like'25472%')";
         try{
            s = con.createStatement();
            rs = s.executeQuery(sql);
            while(rs.next()){	 
			if (rs.getInt("Counter") !=0)
			{	
                                
                               
				chk1 = true;
				break;
			}
			else
			{	

				chk1 = false;
				
			}
		}
           }
        catch(Exception e){e.printStackTrace();
           }
              
          
           return chk1;
        }
public static String getAirtelRegistration(String Mobileno){
    int mypin=getRandomPin();
	
   int mywebpass=getRandomWebPass();
String response=null;
//String insert = "insert into outbound (msisdn,message,status) values (" + destination + ",'" + message + "'," +1+ ")";
               response="Your Username is "+Mobileno+". Your PIN is "+mypin+". Your Web Password is "+mywebpass +". To Top up use Airtel Money";
 String insert = "insert into Registered (msisdn,Reg_details,Pin,WebPassword) values (" + Mobileno+ ",'" + response + "',"+mypin+","+mywebpass+")";

         data.insert(insert);

   
    
      
 
  return response; 
  }

public static boolean hasDuplicates(String sms) {
	boolean res=false;
	
     int shitcount=countercomma(sms);
String[] strArray =sms.split("-|\\.|\\,|\\;|\\:|\\_",shitcount+1);

	for (int i = 0; i < strArray.length-1; i++)
        {
            for (int j = i+1; j < strArray.length; j++)
            {
                if( (strArray[i].equals(strArray[j]))  )
                {
					res=true;
                   
                                      System.out.println("Duplicate  Element is : "+strArray[i]);        
        }else{
					System.out.println("No  Element is : "+strArray[i]);
					//System.out.println("No Element is : "+strArray[j]);
					
				}
            }
			if(res){
				i=strArray.length;
			}
        }
	// No duplicates found.
	return res;
    }
	

public static boolean checkRegisteredDetails(String msisdn,int pin) {
        boolean regg = false;
        String sql = "select msisdn,Pin from Registered where msisdn ='"+msisdn+"' and Pin="+pin;
        try{
            s = con.createStatement();
            rs = s.executeQuery(sql);
            while(rs.next()){	 
			if (rs.getString("msisdn").trim().equals(msisdn) && rs.getInt("Pin")==pin )
			{	
				
				regg = true;
				break;
			}
			else
			{	
				
				regg = false;
				
			}
		}
        }
        catch(Exception e){e.printStackTrace();}
        finally{

        }
        return regg;
    }
/*

mysql> select * from inbound where status=0 order by inbound_id desc limit 5;
+------------+--------------+---------+--------+---------------------+
| inbound_id | msisdn       | message | status | msg_timestamp       |
+------------+--------------+---------+--------+---------------------+
|        323 | 254720875653 | KENNO   |      0 | 2015-04-22 15:51:01 |
|        322 | 254720875653 | KENNO   |      0 | 2015-04-22 15:51:01 | 
|        321 | 254723972280 | Lucky3  |      0 | 2015-04-22 15:51:01 |
|        320 | 254723972280 | Lucky3  |      0 | 2015-04-22 15:51:01 |
|        319 | 254723972280 | Lucky3  |      0 | 2015-04-22 15:51:01 | 
+------------+--------------+---------+--------+---------------------+*/
  

    public static void SmsQCheck() throws Exception{

        String sql = "select * from inbound where status=0  and(msisdn like'25470%' or msisdn like'25473%'or  msisdn like'25478%'  or msisdn like'25471%' or msisdn like'25472%') order by inbound_id desc limit 20";
       String res=null;
       String  res2=null;
       String type=null;
       String acceptresp=null;
       String smss=null;
       String linkid=null;
        try{
            s = con.createStatement();
            rs = s.executeQuery(sql);
            while( rs.next() ){

                    if(SmsUpdate2(rs.getInt("inbound_id"))){
                                System.out.println( rs.getInt("inbound_id") + " status changed to 1");
                                System.out.println(" ");
                                smss=rs.getString("message");

                                int id=rs.getInt("inbound_id");
			         linkid=rs.getString("correlator");
                                String  msisdn=rs.getString("msisdn");
                               System.out.println("\nMessage:"+smss +"\nID="+id +"\nMobile No:"+msisdn);

                                if((smss.equalsIgnoreCase("accept") ||smss.toLowerCase().startsWith("acc") ||smss.startsWith("Accept"))&& (filterinvalid(smss)==false)){
                               // if ( smss.toLowerCase().startsWith("accept".toLowerCase())){
                                   // String acceptresp=null;
                                   if(checkRegistered(msisdn)){
				      acceptresp=getRegdetailsResponse(msisdn);
                                     
					}else{

				      acceptresp=getAcceptResponse(msisdn,smss);

				     }
            
                                   sendsms(id,msisdn,acceptresp,linkid);
                                  System.out.println("Records To Process : " + msisdn+" SMS>>"+acceptresp);
                          
				}else if((smss.equalsIgnoreCase("results") ||smss.toLowerCase().startsWith("res"))&&(msisdn.startsWith("25472")|| msisdn.startsWith("25471")|| msisdn.startsWith("25470")||msisdn.startsWith("25479")) ){
                       gilive.getFeedback(msisdn,smss,Integer.toString(id),linkid);


                     System.out.println("From Gi Live:>> "+acceptresp);


							//smss.toLowerCase().contains("Ke".toLowerCase())
				}
				else if((smss.equalsIgnoreCase("Balance") ||smss.toLowerCase().startsWith("ba"))&& (filterinvalid(smss)==false) ){
                                //else if ( smss.toLowerCase().startsWith("bal".toLowerCase())){
                                 if(checkRegistered(msisdn)){
                                    if(msisdn.startsWith("25470")||msisdn.startsWith("25471")||msisdn.startsWith("25472")){
                                   acceptresp="Your KCS balance is KSH "+getBalanceResponse(msisdn)+".You can play by sending lotto,kenno or lucky to 22225 to play once or no. of bets#gametype e.g 2#kenno or 40,34,53#kenno ";
                                     }else{
 acceptresp="Your KCS balance is KSH "+getBalanceResponse(msisdn)+".You can play by sending lotto,kenno or lucky to 22225 to play once or no. of bets#gametype e.g 2#kenno or 40,34,53#kenno ";
                                      }
				     }else{
                                     
				      acceptresp="Kindly register to the service by sending the word accept and the county residence i.e accept#county e.g accept#nairobi";

				     } 
                                   sendsms(id,msisdn,acceptresp,linkid);
                                   System.out.println("Records To Process : " + msisdn+" SMS>>"+acceptresp);

								
				}/*
				else if(smss.equalsIgnoreCase("Withdraw") ||smss.toLowerCase().startsWith("w") ){
                                       if(checkRegistered(msisdn)){
                                          acceptresp=getWithdrawalResponse(msisdn,smss,linkid);
                                        
                                         
					 }else{
                                      acceptresp="Kindly register to the service by sending the word accept and the county residence i.e accept#county e.g accept#nairobi";
					}
 				   sendsms(id,msisdn,acceptresp,linkid);
                                   System.out.println("Records To Process : " + msisdn+" SMS>>"+acceptresp);
                                    //original.toLowerCase().contains(tobeChecked.toLowerCase()) 
								//smss.toLowerCase().contains("Ke".toLowerCase())
				}*/
else if((smss.toLowerCase().contains("Ke".toLowerCase()) ||smss.toLowerCase().contains("Kn".toLowerCase())||smss.toLowerCase().contains("Ko".toLowerCase()))&& (filterinvalid(smss)==false) ){
                                        String git=null;
                                         String ReferenceId="";
                                         String mobileno="";
					 String amount="";
        				 String gametype="";
        				 String name="";
        				 String ID="";
					String output1=null;
					 String output2=null;
                                   if(checkRegistered(msisdn)){
                                        
                                      if((countercomma(smss)>=1) && (counterhash(smss)>=1) ){
                                                     
                                        acceptresp=getManualKennoResponse(msisdn,smss,id,linkid);
                                        System.out.println("RESULTS>> "+acceptresp);
                                         if(acceptresp.trim().startsWith("Ok")){
                                          git=getResponseForGi(acceptresp);
                                          
                                         String[] tempsaf= git.split(",",6);
   
                                          for(int b =0; b < tempsaf.length ; b++){
					    mobileno=tempsaf[0];
                                            ID=tempsaf[1];
                                            ReferenceId=tempsaf[2];
					    name=tempsaf[3];
        				    amount=tempsaf[4];
        				    gametype=tempsaf[5];

                                              }
                               //acceptresp = gi.getFeedback(mobileno.substring(3),Integer.toString(id),ReferenceId,name,amount,gametype);
String fortest="<SMSAPI><MSISDN>"+mobileno.substring(3)+"</MSISDN><SESSIONID>"+Integer.toString(id)+"</SESSIONID><MESSAGE>"+gametype+"</MESSAGE><MPESAREF>"+name+"~"+amount+"~"+mobileno.substring(3)+"~"+ReferenceId+"</MPESAREF></SMSAPI>";
                                 

                                   acceptresp = gistage.setMessage(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);
output1= getExtractedRes(acceptresp);
                                  System.out.println("Ouput 1:>> "+output1);
				   output2=getExtractedFinal(output1);
                                  System.out.println("Ouput 2:>> "+output2);

String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," + mobileno + ",'" + output2 + "','" + ReferenceId + "'," +1+ ")";
             data.insert(insert);
                                           }else{
                                            sendsms(id,msisdn,acceptresp,linkid); 

                                           }
                                        //sendsms(id,msisdn,acceptresp,linkid); 
                                   							
					}else if((countercomma(smss)==0) && (counterhash(smss)>=1)){
                                         
					 acceptresp=getKennoResponse(msisdn,smss,id,linkid);
                                        System.out.println("RESULTS>> "+acceptresp);
                                         if(acceptresp.trim().startsWith("Ok")){
                                          git=getResponseForGi(acceptresp);
                                          
                                         String[] tempsaf= git.split(",",6);
   
                                          for(int b =0; b < tempsaf.length ; b++){
					    mobileno=tempsaf[0];
                                            ID=tempsaf[1];
                                            ReferenceId=tempsaf[2];
					    name=tempsaf[3];
        				    amount=tempsaf[4];
        				    gametype=tempsaf[5];

                                              }
                     // acceptresp = gi.getFeedback(mobileno.substring(3),Integer.toString(id),ReferenceId,name,amount,gametype);
String fortest="<SMSAPI><MSISDN>"+mobileno.substring(3)+"</MSISDN><SESSIONID>"+Integer.toString(id)+"</SESSIONID><MESSAGE>"+gametype+"</MESSAGE><MPESAREF>"+name+"~"+amount+"~"+mobileno.substring(3)+"~"+ReferenceId+"</MPESAREF></SMSAPI>";
                                 

                                   acceptresp = gistage.setMessage(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);
                                  output1= getExtractedRes(acceptresp);
                                  System.out.println("Ouput 1:>> "+output1);
				   output2=getExtractedFinal(output1);
                                  System.out.println("Ouput 2:>> "+output2);

String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," + mobileno + ",'" + output2 + "','" + ReferenceId + "'," +1+ ")";
             data.insert(insert);
                                           }else{
                                            sendsms(id,msisdn,acceptresp,linkid); 

                                           }
                                         }else{

                                        //start validate sms
                                         acceptresp=getKennoResponse(msisdn,smss,id,linkid);
                                       System.out.println("RESULTS>> "+acceptresp);
                                          if(acceptresp.trim().startsWith("Ok")){
                                          git=getResponseForGi(acceptresp);
                                          
                                         String[] tempsaf= git.split(",",6);
   
                                          for(int b =0; b < tempsaf.length ; b++){
					    mobileno=tempsaf[0];
                                            ID=tempsaf[1];
                                            ReferenceId=tempsaf[2];
					    name=tempsaf[3];
        				    amount=tempsaf[4];
        				    gametype=tempsaf[5];

                                              }
                           // acceptresp = gi.getFeedback(mobileno.substring(3),Integer.toString(id),ReferenceId,name,amount,gametype);
String fortest="<SMSAPI><MSISDN>"+mobileno.substring(3)+"</MSISDN><SESSIONID>"+Integer.toString(id)+"</SESSIONID><MESSAGE>"+gametype+"</MESSAGE><MPESAREF>"+name+"~"+amount+"~"+mobileno.substring(3)+"~"+ReferenceId+"</MPESAREF></SMSAPI>";
                                 

                                   acceptresp = gistage.setMessage(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);
 output1= getExtractedRes(acceptresp);
                                  System.out.println("Ouput 1:>> "+output1);
				   output2=getExtractedFinal(output1);
                                  System.out.println("Ouput 2:>> "+output2);

String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," + mobileno + ",'" + output2 + "','" + ReferenceId + "'," +1+ ")";
             data.insert(insert);

                                           }else{
                                            sendsms(id,msisdn,acceptresp,linkid); 

                                           }
					}
                                       }else{
                                      acceptresp="Kindly register to the service by sending the word accept and the county residence i.e accept#county e.g accept#nairobi";
                                            sendsms(id,msisdn,acceptresp,linkid); 

					}
                                 
                                  System.out.println("Records To Process : " + msisdn+" SMS>>"+acceptresp);
			
				}

else if(smss.equalsIgnoreCase("cancel") || (smss.contains("can")) ){
                                         String git=null;
                                         String ReferenceId="";
                                         String mobileno="";
                                         String amount="";
                                         String gametype="";
                                         String name="";
                                         String ID="";



          if(checkRegistered(msisdn)){

                                          //setRegistrationStatus(msisdn);

       acceptresp=getCancelResponse(msisdn,smss,id,linkid);

        System.out.println("RESULTS>> "+acceptresp);

                                          if(acceptresp.trim().startsWith("Ok")){

                                          git=getResponseForGi(acceptresp);

                                          

                                         String[] tempsaf= git.split(",",6);

   

                                          for(int b =0; b < tempsaf.length ; b++){

                                            mobileno=tempsaf[0];

                                            ID=tempsaf[1];

                                            ReferenceId=tempsaf[2];

                                            name=tempsaf[3];

                                            amount=tempsaf[4];

                                             gametype=tempsaf[5];



                                              }

                           // acceptresp = gi.getFeedback(mobileno.substring(3),Integer.toString(id),ReferenceId,name,amount,gametype);
/*
String fortest="<SMSAPI><MSISDN>"+mobileno.substring(3)+"</MSISDN><SESSIONID>"+Integer.toString(id)+"</SESSIONID><MESSAGE>"+gametype+"</MESSAGE><MPESAREF>"+name+"~"+amount+"~"+mobileno.substring(3)+"~"+ReferenceId+"</MPESAREF></SMSAPI>";

  */                               



                                   /*acceptresp = gistage.setMessage(fortest);

                                  System.out.println("From Gi Staging:>> "+acceptresp);

*/
              gilive.getFeedback(mobileno,gametype,ID,ReferenceId);

                                           }else{

                                            sendsms(id,msisdn,acceptresp,linkid); 



                                           }





       

                                          //acceptresp="Your Tkt ID 158956337 has been cancelled. Your Ksh. 100 has been credited back to your KCS account. //Balance 1400"; 

                                           

                                         

      }else{

                                      acceptresp="Kindly register to the service by sending the word accept and the county residence i.e accept#county e.g accept#nairobi .Your Deposit amount is kept safely.";

     

       sendsms(id,msisdn,acceptresp,linkid);

                                   System.out.println("Records To Process : " + msisdn+" SMS>>"+acceptresp);

     }   

    }

         else if(smss.equalsIgnoreCase("Withdraw") ||smss.toLowerCase().startsWith("w") ){
                                       if(checkRegistered(msisdn)){
                                          acceptresp=getWithdrawalResponse(msisdn,smss,linkid);
                                        
                                         
					 }else{
                                      acceptresp="Kindly register to the service by sending the word accept and the county residence i.e accept#county e.g accept#nairobi";
					}
 				   sendsms(id,msisdn,acceptresp,linkid);
                                   System.out.println("Records To Process : " + msisdn+" SMS>>"+acceptresp);
                                    //original.toLowerCase().contains(tobeChecked.toLowerCase()) 
								//smss.toLowerCase().contains("Ke".toLowerCase())
				}else if(smss.equalsIgnoreCase("stop") ||smss.toLowerCase().startsWith("stop") ){
                       
    /*String fortest="<SMSAPI><MSISDN>" + msisdn.substring(3)+ "</MSISDN><SESSIONID>"  +Integer.toString(id)+  "</SESSIONID><MESSAGE>"+smss+"</MESSAGE></SMSAPI>";
    

                                 acceptresp = gistage.setSMSMessage(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);


*/
                     gilive.getFeedback(msisdn,smss,Integer.toString(id),linkid);


                     System.out.println("From Gi Live:>> "+acceptresp);
                      
				}else if(smss.equalsIgnoreCase("last") ||smss.toLowerCase().startsWith("las") ){
   
/*
<SMSAPI><MSISDN>254727543088</MSISDN><SESSIONID>110</SESSIONID><MESSAGE>stop</MESSAGE><MPESAREF>AIR_4654898</MPESAREF></SMSAPI>
*/


                     /* String fortest="<SMSAPI><MSISDN>" + msisdn + "</MSISDN><SESSIONID>"+Integer.toString(id)+ "</SESSIONID><MESSAGE>"+smss+"</MESSAGE> </SMSAPI>";
                         System.out.println("Request to Gi:"+fortest);   

                                 acceptresp = gistage.setSMSMessage(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);
*/

                     gilive.getFeedback(msisdn,smss,Integer.toString(id),linkid);


                     System.out.println("From Gi Live:>> "+acceptresp);


				}else if( smss.toLowerCase().contains("paybill")|| smss.toLowerCase().contains("safaricom")||smss.toLowerCase().contains("how") ||smss.toLowerCase().contains("mpesa") || smss.toLowerCase().contains("airtel") || smss.toLowerCase().contains("money") ){
if(msisdn.startsWith("25472")||msisdn.startsWith("25470")||msisdn.startsWith("25471")){

String outsms="Go to Mpesa, select Lipa na Mpesa, then Paybill, enter Bus number 292222 Enter account. Either Lotto Kenno or Lucky 3 Enter Amount 50 enter personal PIN.";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  }else{
String outsms="Go to Airtel Money, click make payment then Paybill, Others. Under Business, put KCSLOTTERY Amount 50 put personal pin. Ref is game name LOTTO KENNO or LUCKY3";
                                sendsms(id,msisdn,outsms,linkid);
               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);

}
                      
                     System.out.println("From Gi Live:>> "+acceptresp);


							//smss.toLowerCase().contains("Ke".toLowerCase())
				}else if((smss.equalsIgnoreCase("results") ||smss.toLowerCase().startsWith("res"))&&(msisdn.startsWith("25473")|| msisdn.startsWith("25478")) ){
                      // gilive.getFeedback(msisdn,smss,Integer.toString(id),linkid);
String output1=null;
String output2=null;

      String requ = "<SMSAPI><MSISDN>" + msisdn + "</MSISDN><SESSIONID>" + Integer.toString(id) + "</SESSIONID><MESSAGE>" + smss + "</MESSAGE></SMSAPI>";

 System.out.println("The Request is  " + requ);
String respon = test.smsapi(requ);
System.out.println("The Request is  " + respon);


output1= getExtractedRes(respon);
System.out.println("Ouput 1:>> "+output1);
  output2=getExtractedFinal(output1);
  System.out.println("Ouput 2:>> "+output2);
String req=req(msisdn,output2);
System.out.println("Airtel Response:"+req);
if (req.equalsIgnoreCase("Ok")){
String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," +msisdn + ",'" + output2 + "','" +linkid + "'," +1+ ")";
             data.insert(insert);
}else{
String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," +msisdn + ",'" + output2 + "','" +linkid + "'," +0+ ")";
             data.insert(insert);
//System.out.println("Airtel Response:"+req);
}


    //reponsSystem.out.println("The Request is  " + requ);


							//smss.toLowerCase().contains("Ke".toLowerCase())
				}else if((smss.toLowerCase().contains("Lu".toLowerCase()) || smss.toLowerCase().contains("Ra".toLowerCase()) ||smss.toLowerCase().contains("L3".toLowerCase()))&& (filterinvalid(smss)==false)){

			               String git=null;
                                         String ReferenceId="";
                                         String mobileno="";
					 String amount="";
        				 String gametype="";
        				 String name="";
        				 String ID="";
                                         String output1=null;
					 String output2=null;
                                   if(checkRegistered(msisdn)){
                                        
                                      if((countercomma(smss)>=1) && (counterhash(smss)>=1) ){
                                        //
                                        System.out.println("START MANUAL!!>> "+smss);                         
                                        acceptresp=getManualLucky3Response(msisdn,smss,id,linkid);
                                        System.out.println("RESULTS>> "+acceptresp);
                                         if(acceptresp.trim().startsWith("Ok")){
                                          git=getResponseForGi(acceptresp);
                                          
                                         String[] tempsaf= git.split(",",6);
   
                                          for(int b =0; b < tempsaf.length ; b++){
					    mobileno=tempsaf[0];
                                            ID=tempsaf[1];
                                            ReferenceId=tempsaf[2];
					    name=tempsaf[3];
        				    amount=tempsaf[4];
        				    gametype=tempsaf[5];

                                              }

                 /*acceptresp = gi.getFeedback(mobileno.substring(3),Integer.toString(id),ReferenceId,name,amount,gametype);*/
                                String fortest="<SMSAPI><MSISDN>"+mobileno.substring(3)+"</MSISDN><SESSIONID>"+Integer.toString(id)+"</SESSIONID><MESSAGE>"+gametype+"</MESSAGE><MPESAREF>"+name+"~"+amount+"~"+mobileno.substring(3)+"~"+ReferenceId+"</MPESAREF></SMSAPI>";
                                 

                                   acceptresp = gistage.setMessage(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);
   
                                  output1= getExtractedRes(acceptresp);
                                  System.out.println("Ouput 1:>> "+output1);
				   output2=getExtractedFinal(output1);
                                  System.out.println("Ouput 2:>> "+output2);

String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," + mobileno + ",'" + output2 + "','" + ReferenceId + "'," +1+ ")";
             data.insert(insert);
                                        }else{
                                            sendsms(id,msisdn,acceptresp,linkid); 

                                           }
                                      // sendsms(id,msisdn,acceptresp,linkid);    							
					}else if((countercomma(smss)==0) && (counterhash(smss)>=1)){
					 acceptresp=getLucky3Response(msisdn,smss,id,linkid);
                                         System.out.println("RESULTS>> "+acceptresp);
                                         if(acceptresp.trim().startsWith("Ok")){
                                          git=getResponseForGi(acceptresp);
                                          
                                         String[] tempsaf= git.split(",",6);
   
                                          for(int b =0; b < tempsaf.length ; b++){
					    mobileno=tempsaf[0];
                                            ID=tempsaf[1];
                                            ReferenceId=tempsaf[2];
					    name=tempsaf[3];
        				    amount=tempsaf[4];
        				    gametype=tempsaf[5];

                                              }
/*acceptresp = gi.getFeedback(mobileno.substring(3),Integer.toString(id),ReferenceId,name,amount,gametype);*/
                                String fortest="<SMSAPI><MSISDN>"+mobileno.substring(3)+"</MSISDN><SESSIONID>"+Integer.toString(id)+"</SESSIONID><MESSAGE>"+gametype+"</MESSAGE><MPESAREF>"+name+"~"+amount+"~"+mobileno.substring(3)+"~"+ReferenceId+"</MPESAREF></SMSAPI>";
                                 

                                   acceptresp = gistage.setMessage(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);
                                   output1= getExtractedRes(acceptresp);
                                  System.out.println("Ouput 1:>> "+output1);
				   output2=getExtractedFinal(output1);
                                  System.out.println("Ouput 2:>> "+output2);

String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," + mobileno + ",'" + output2 + "','" + ReferenceId + "'," +1+ ")";
             data.insert(insert);

                                           }else{
                                            sendsms(id,msisdn,acceptresp,linkid); 

                                           }
                                         }else{

                                         acceptresp=getLucky3Response(msisdn,smss,id,linkid);
				         System.out.println("RESULTS>> "+acceptresp);
                                        if(acceptresp.trim().startsWith("Ok")){
                                          git=getResponseForGi(acceptresp);

                                         String[] tempsaf= git.split(",",6);

                                          for(int b =0; b < tempsaf.length ; b++){
					    mobileno=tempsaf[0];
                                            ID=tempsaf[1];
                                            ReferenceId=tempsaf[2];
					    name=tempsaf[3];
        				    amount=tempsaf[4];
        				    gametype=tempsaf[5];

                                              }
/*acceptresp = gi.getFeedback(mobileno.substring(3),Integer.toString(id),ReferenceId,name,amount,gametype);*/
                                String fortest="<SMSAPI><MSISDN>"+mobileno.substring(3)+"</MSISDN><SESSIONID>"+Integer.toString(id)+"</SESSIONID><MESSAGE>"+gametype+"</MESSAGE><MPESAREF>"+name+"~"+amount+"~"+mobileno.substring(3)+"~"+ReferenceId+"</MPESAREF></SMSAPI>";
                                 

                                   acceptresp = gistage.setMessage(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);

                                  output1= getExtractedRes(acceptresp);
                                  System.out.println("Ouput 1:>> "+output1);
				   output2=getExtractedFinal(output1);
                                  System.out.println("Ouput 2:>> "+output2);

String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," + mobileno + ",'" + output2 + "','" + ReferenceId + "'," +1+ ")";
             data.insert(insert);
                                           }else{
                                            sendsms(id,msisdn,acceptresp,linkid); 

                                           }
					}
                                       }else{
                                      acceptresp="Kindly register to the service by sending the word accept and the county residence i.e accept#county e.g accept#nairobi";
                                   sendsms(id,msisdn,acceptresp,linkid);
					}

                                   System.out.println("Records To Process : " + msisdn+" SMS>>"+acceptresp);
			
				}
				else if(smss.equalsIgnoreCase("Deposit") || (smss.contains("De")) ){

				      if(checkRegistered(msisdn)){
                                          setRegistrationStatus(msisdn); 
                                          acceptresp="Your current KCS Wallet account is "+getBalanceResponse(msisdn)+" You can play by sending lotto,kenno or lucky to 22225 to play once or no. of bets#gametype e.g 2#kenno or 40,34,53#kenno"; 
                                           
                                         
					 }else{
                                      acceptresp="Kindly register to the service by sending the word accept and the county residence i.e accept#county e.g accept#nairobi .Your Deposit amount is kept safely.";
					}
 				  sendsms(id,msisdn,acceptresp,linkid);
                                   System.out.println("Records To Process : " + msisdn+" SMS>>"+acceptresp);
								
				}  
else if( smss.toLowerCase().contains("ussd") ){


String outsms="KCS Menu\n========\n1.Registration\n2.Play Game\n3.Check Results\n4.Check Winning\n5.Claim Winning\n98.More";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  


							//smss.toLowerCase().contains("Ke".toLowerCase())
				}else if(smss.equals("13")){
String outsms="Go to Mpesa, select Lipa na Mpesa, then Paybill, enter Bus number 292222 Enter account. Either Lotto Kenno or Lucky 3 Enter Amount 50 enter personal PIN.";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);

                }
else if( smss.equals("2")){


String outsms="Play Menu\n========\n6.Lucky 3\n7.Kenno\n8.Lotto\n";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  


							//smss.toLowerCase().contains("Ke".toLowerCase())
				}
else if( smss.equals("6")){
                                                        //smss.toLowerCase().contains("Ke".toLowerCase())
String outsms="Lucky3 Menu\n========\n11.Lucky Pick\n12.Select Number\n";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);


}else if( smss.equals("7")){
                                                        //smss.toLowerCase().contains("Ke".toLowerCase())
String outsms="Kenno Menu\n========\n11.Lucky Pick\n12.Select Number\n";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);


}

else if( smss.equals("11")){
                                                        //smss.toLowerCase().contains("Ke".toLowerCase())
String outsms="Luck Pick \n========\n13.Pay \n";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);


}else if( smss.equals("12")){
                                                        //smss.toLowerCase().contains("Ke".toLowerCase())
String outsms="Select Menu \n========\nChoose number of balls Min2 max 10 \n";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);


}else if( smss.equals("8")){
                                                        //smss.toLowerCase().contains("Ke".toLowerCase())
String outsms="Lotto Menu\n========\n11.Lucky Pick\n12.Select Number\n";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);


}else if( smss.length()==12 && isNumericString(smss)==true){


//String outsms;
 //String smssu="results";
                    gilive.getFeedback(msisdn,smss,Integer.toString(id),linkid);


                     System.out.println("From Gi Live:>> "+acceptresp);
                             //   sendsms(id,msisdn,outsms,linkid);
                               //System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  


                                                        //smss.toLowerCase().contains("Ke".toLowerCase())
                                }
else if( smss.equals("3")){


//String outsms;
 String smssu="results";
                    gilive.getFeedback(msisdn,smssu,Integer.toString(id),linkid);


                     System.out.println("From Gi Live:>> "+acceptresp);
                             //   sendsms(id,msisdn,outsms,linkid);
                               //System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  


							//smss.toLowerCase().contains("Ke".toLowerCase())
				}else if( smss.equals("4")){


String outsms="Please enter your prize code. ";
                                sendsms(id,msisdn,outsms,linkid);
                       System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  //gilive.getFeedback(msisdn,smss,Integer.toString(id),linkid);


                    // System.out.println("From Gi Live:>> "+acceptresp);



							//smss.toLowerCase().contains("Ke".toLowerCase())
				}else if( smss.equals("17")){


String outsms="Please enter your Name. ";
                                sendsms(id,msisdn,outsms,linkid);
                       System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  //gilive.getFeedback(msisdn,smss,Integer.toString(id),linkid);


                    // System.out.println("From Gi Live:>> "+acceptresp);



                                                        //smss.toLowerCase().contains("Ke".toLowerCase())
                                }else if( smss.equals("18")){


String outsms="Please enter your National ID. ";
                                sendsms(id,msisdn,outsms,linkid);
                       System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  //gilive.getFeedback(msisdn,smss,Integer.toString(id),linkid);


                    // System.out.println("From Gi Live:>> "+acceptresp);



                                                        //smss.toLowerCase().contains("Ke".toLowerCase())
                                }else if( smss.equals("19")){


String outsms="Please enter your County of residence. ";
                                sendsms(id,msisdn,outsms,linkid);
                       System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  //gilive.getFeedback(msisdn,smss,Integer.toString(id),linkid);


                    // System.out.println("From Gi Live:>> "+acceptresp);



                                                        //smss.toLowerCase().contains("Ke".toLowerCase())
                                }
else if( smss.equals("98")){


String outsms="Other Menu\n========\n1.Result \n2.Last\n3.Stop\n4.Cancel\n";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  


							//smss.toLowerCase().contains("Ke".toLowerCase())
				}else if( smss.equals("1")){


String outsms="Registration Menu\n========\n17.Name \n18.National ID\n19.County\n";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  


							//smss.toLowerCase().contains("Ke".toLowerCase())
				}else if( smss.equals("5")){


String outsms="Your claim prize has been credited to your wallet";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
  


							//smss.toLowerCase().contains("Ke".toLowerCase())
				}
else if((smss.toLowerCase().contains("Lo".toLowerCase()) || smss.toLowerCase().contains("Ro".toLowerCase()) || smss.toLowerCase().contains("Rt".toLowerCase())) && (smss.toLowerCase().contains("GD".toLowerCase()))) {
	 String outsms="Thanks for participating in KCS Lotto grand draw promotion. Unfortunately, the draw is already closed. Visit www.kenyacharity.co.ke for more details";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
							   
	
}
else if(((smss.toLowerCase().contains("Lo".toLowerCase()) || smss.toLowerCase().contains("Ro".toLowerCase()) || smss.toLowerCase().contains("Rt".toLowerCase())) && (!smss.toLowerCase().contains("GD".toLowerCase()))) && (filterinvalid(smss)==false)) {
					String git=null;
                                         String ReferenceId="";
                                         String mobileno="";
					 String amount="";
        				 String gametype="";
        				 String name="";
        				 String ID="";
                                         String output1=null;
					 String output2=null;
				     if(checkRegistered(msisdn)){
                                        
                                      if((countercomma(smss)>=1) && (counterhash(smss)>=1) ){
                                                     
                                        acceptresp=getManualLottoResponse(msisdn,smss,id,linkid);
                                         System.out.println("RESULTS>> "+acceptresp);
                                         if(acceptresp.trim().startsWith("Ok")){
                                          git=getResponseForGi(acceptresp);
                                          
                                         String[] tempsaf= git.split(",",6);
   
                                          for(int b =0; b < tempsaf.length ; b++){
					    mobileno=tempsaf[0];
                                            ID=tempsaf[1];
                                            ReferenceId=tempsaf[2];
					    name=tempsaf[3];
        				    amount=tempsaf[4];
        				    gametype=tempsaf[5];

                                              }
                         // acceptresp = gi.getFeedback(mobileno.substring(3),Integer.toString(id),ReferenceId,name,amount,gametype);
         String fortest="<SMSAPI><MSISDN>"+mobileno.substring(3)+"</MSISDN><SESSIONID>"+Integer.toString(id)+"</SESSIONID><MESSAGE>"+gametype+"</MESSAGE><MPESAREF>"+name+"~"+amount+"~"+mobileno.substring(3)+"~"+ReferenceId+"</MPESAREF></SMSAPI>";
                                 

                                   acceptresp = gistage.setMessage(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);
                                    output1= getExtractedRes(acceptresp);
                                  System.out.println("Ouput 1:>> "+output1);
				   output2=getExtractedFinal(output1);
                                  System.out.println("Ouput 2:>> "+output2);

String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," + mobileno + ",'" + output2 + "','" + ReferenceId + "'," +1+ ")";
                                   data.insert(insert);

                                           }else{
                                            sendsms(id,msisdn,acceptresp,linkid); 

                                           }
                                        //sendsms(id,msisdn,acceptresp,linkid); 
                                } else if((countercomma(smss)==0) && (counterhash(smss)>=1)){
					 acceptresp=getLottoResponse(msisdn,smss,id,linkid);
					 System.out.println("RESULTS>> "+acceptresp);
                                        if(acceptresp.trim().startsWith("Ok")){
                                          git=getResponseForGi(acceptresp);
                                          
                                         String[] tempsaf= git.split(",",6);
   
                                          for(int b =0; b < tempsaf.length ; b++){
					    mobileno=tempsaf[0];
                                            ID=tempsaf[1];
                                            ReferenceId=tempsaf[2];
					    name=tempsaf[3];
        				    amount=tempsaf[4];
        				    gametype=tempsaf[5];

                                              }
              // acceptresp = gi.getFeedback(mobileno.substring(3),Integer.toString(id),ReferenceId,name,amount,gametype);
String fortest="<SMSAPI><MSISDN>"+mobileno.substring(3)+"</MSISDN><SESSIONID>"+Integer.toString(id)+"</SESSIONID><MESSAGE>"+gametype+"</MESSAGE><MPESAREF>"+name+"~"+amount+"~"+mobileno.substring(3)+"~"+ReferenceId+"</MPESAREF></SMSAPI>";
                                 

                                   acceptresp = gistage.setMessage(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);

                                           }else{
                                            sendsms(id,msisdn,acceptresp,linkid); 

                                           }
                                         }else{
                                          
                                         acceptresp=getLottoResponse(msisdn,smss,id,linkid);
					 System.out.println("RESULTS>> "+acceptresp);
                                        if(acceptresp.trim().startsWith("Ok")){
                                          git=getResponseForGi(acceptresp);
                                          
                                         String[] tempsaf= git.split(",",6);
   
                                          for(int b =0; b < tempsaf.length ; b++){
					    mobileno=tempsaf[0];
                                            ID=tempsaf[1];
                                            ReferenceId=tempsaf[2];
					    name=tempsaf[3];
        				    amount=tempsaf[4];
        				    gametype=tempsaf[5];

                                              }
                   //acceptresp = gi.getFeedback(mobileno.substring(3),Integer.toString(id),ReferenceId,name,amount,gametype);
String fortest="<SMSAPI><MSISDN>"+mobileno.substring(3)+"</MSISDN><SESSIONID>"+Integer.toString(id)+"</SESSIONID><MESSAGE>"+gametype+"</MESSAGE><MPESAREF>"+name+"~"+amount+"~"+mobileno.substring(3)+"~"+ReferenceId+"</MPESAREF></SMSAPI>";
                                 

                                     acceptresp = airtelgi.mpesaPDSL(fortest);
                                  System.out.println("From Gi Staging:>> "+acceptresp);
                                    output1= getExtractedRes(acceptresp);
                                  System.out.println("Ouput 1:>> "+output1);
				   output2=getExtractedFinal(output1);
                                  System.out.println("Ouput 2:>> "+output2);

String req=req(mobileno,output2);
System.out.println("Airtel Response:"+req);
if (req.equalsIgnoreCase("Ok")){
String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," +mobileno + ",'" + output2 + "','" +ReferenceId + "'," +1+ ")";
             data.insert(insert);
}else{
String insert = "insert into outboundS (msgid,msisdn,message,ReferenceID,status) values ("+id+"," +mobileno + ",'" + output2 + "','" +ReferenceId + "'," +0+ ")";
             data.insert(insert);

}
                                           }else{
                                            sendsms(id,msisdn,acceptresp,linkid); 

                                           }
					}
                                       }else{
                                      acceptresp="Kindly register to the service by sending the word accept and the county residence i.e accept#county e.g accept#nairobi";
                                  sendsms(id,msisdn,acceptresp,linkid);
					}

                                   System.out.println("Records To Process : " + msisdn+" SMS>>"+acceptresp);
								
				
                             }else{
                               String outsms="Invalid Sms format,please change the format and try again i.e nomfbetts#gametype#numofdays e.g 2#kenno#5";
                                sendsms(id,msisdn,outsms,linkid);
                               System.out.println("Records To Process : " + msisdn+" SMS>>"+outsms);
				}
                                                             

                            }
                            else{
                                System.out.println( rs.getString("inbound_id") + " status is NOT changed to 1!");
                                System.out.println(" ");
                            }

                }

        }
        catch(Exception e){e.printStackTrace();}
      /*  finally{
            if(rs!=null)  rs.close();
            if(s!=null)  s.close();
            if(con!=null)  con.close();
        }*/
    }

public static String getExtractedRes(String sms ){
     String[] temp= sms.split("0~",2);
    String others=null;
    String res=null;

      for(int b =0; b < temp.length ; b++){
        res=temp[1];
	others=temp[0];
       }

return res;
}
public static String getExtractedFinal(String sms ){
     String[] temp= sms.split("</MSISDN>",2);
    String others=null;
    String res=null;

      for(int b =0; b < temp.length ; b++){
        res=temp[0];
	others=temp[1];
       }

return res;
}
public static  Boolean getReport(){
 TestStatus process = new TestStatus();
 System.out.println("Checking Report DB...");
 return process.reporttest();
}
public void pastscheduledtime (){
System.out.println("\n Subscription past or not started!! ");
}
public void nomailsposted (){
System.out.println("\nNo subscription Have Been Process at the Moment !! ");
}//8.00
public static  Boolean 	getStartimer(){
 
 TestStatus process = new TestStatus();
 System.out.println("Subscription  Starterd..:");
 String timer=process.getTimeChecker();
 System.out.println("\n\n\n Time is :"+timer);


 return process.getTimeCheckerStarthour(process.getTimeCheckerStart(timer));
}




public static String getKennoResponse(String Mobileno,String kennosms,int msgID,String linkid) throws Exception{

/*Lotto - LT, LOTTO, Rotto, Lot, Lott, Lo, Rot, Rott,Lotta
Kenno - Kenn, Keno, Ken, Ke, Kno, KO, KN, Keino
Lucky3- Luk, Lu, Luck, Lucy, Rucky3, Lacky3, Luky, L3, Luc, Luk.*/



	String result=null;
	

	 String delimiterkenno = "#";
	 int length=0;
	 String MsgID=Integer.toString(msgID);
         String[] tempkenno;
         String kennogame="";
         String numberofbets="";
	String numberofdays="";
	int amountspent=0;
	int currentbalance=0;
	int balanceafter=0;
	boolean retval = kennosms.contains(delimiterkenno);
	int counter=counterhash(kennosms);
	System.out.println("Counter:"+counter);
  try{
      if(counter==2){
		
      tempkenno= kennosms.split(delimiterkenno,3);
	
      for(int b =0; b < tempkenno.length ; b++){
  

    numberofbets=tempkenno[0];
	kennogame=tempkenno[1];
	numberofdays=tempkenno[2];
	
         }
     if((numberofbets.length()==1) && (numberofdays.length()<=2) && (isNumericString(numberofbets)==true) && (isNumericString(numberofdays)==true)){
    
   if((kennogame.startsWith("k") || kennogame.startsWith("K"))&& (Integer.parseInt(numberofbets))<=3 && (Integer.parseInt(numberofdays))<=31 ){
    kennogame="Kenno";
    int usaged=Integer.parseInt(numberofbets)*50;
    int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;

     Vector vr = new Vector();
     Vector subs = new Vector();
     subs.clear();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1);
     subs=vecRec.getSubDetailsStatus(Mobileno);   

       //Name,msisdn,Message,Mpesa_code,Balance 
 String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+kennogame+"','"+linkid+"',"+usaged+","+balanceafterplay+")";


     data.insert(insert);

  
   
String sms2go=null;
String lev=null;
 
if(linkid.contains("Day")){
// 1#s#kenno#1#7
lev=getSubLevel(linkid);

sms2go=numberofbets+"#s#Kenno#50#"+lev+"#"+numberofdays;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+kennosms+"','"+kennogame+"','"+sms2go+"','"+linkid+"',"+numberofbets+","+numberofdays+","+usaged+","+Integer.parseInt(lev)+")";

data.insert(sub);


}else{




sms2go=numberofbets+"#s#Kenno#50#1#"+numberofdays;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+kennosms+"','"+kennogame+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+Integer.parseInt(numberofdays)+","+usaged+","+1+")";

data.insert(sub);

}

                                     //start validate sms 2#kenno#3
 /*result = gi.getFeedback(mobileno.substring(3),"3950","AIRTEL_"+ReferenceId,name,amount,gametype);
    System.out.println("RESPONSE"+result);*/

if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}       
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"/=";
         }

        }else{
        
result="Invalid format "+kennosms+" kindly try again i.e No of bets#gametype#days e.g 3#kenno#7.NB max number of bets per session is 3 and max number of days is 31 "; 
        }
    
  }else{

result="Invalid format "+kennosms+" kindly try again i.e No of bets#gametype#days e.g 3#kenno#7.NB max number of bets per session is 3 and  max number of days is 31 ";
        }


    
        
    

      
}else if(counter==1){
	
    tempkenno= kennosms.split(delimiterkenno,2);
	
    for(int b =0; b < tempkenno.length ; b++){	
	
	//if(tempkenno[1].length()<=2){
	
	if((tempkenno[0].startsWith("k") ||tempkenno[0].startsWith("K")) && (tempkenno[1].length()<=2)){
				kennogame="Kenno";
				numberofdays=tempkenno[1];
				if(isNumericString(numberofdays)==true){
				
		if(Integer.parseInt(tempkenno[1])<=31){	
	int usaged=50;
        int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
        //String name=getTransName(Mobileno); 
     Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 



       //Name,msisdn,Message,Mpesa_code,Balance 
 String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+"',"+Mobileno+",'"+kennogame+"','"+linkid+"',"+usaged+","+balanceafterplay+")";

data.insert(insert);
String sms2go=null;
String lev=null;

if(linkid.toLowerCase().contains("Day")){
// 1#s#kenno#1#7
lev=getSubLevel(linkid);
sms2go="1#s#Kenno#50#"+lev+"#"+numberofdays;
       
String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+kennosms+"','"+kennogame+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(numberofdays)+","+usaged+","+Integer.parseInt(lev)+")";
 data.insert(sub);
}else{

sms2go="1#s#Kenno#50#1#"+numberofdays;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+kennosms+"','"+kennogame+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(numberofdays)+","+usaged+","+1+")";

data.insert(sub);

}





 /*result="You have susbcribed to "+kennogame+"  for "+numberofdays+" days, you will receive the confirmation Bets shortly and KCS Wallet balance is Ksh "+balanceafterplay+"/="; */

if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+",Kenno";
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+",Kenno";
System.out.println("RESULT For GI>>:"+result); 

}    
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
		 }else{
   result="Invalid format "+kennosms+" kindly try again i.e No of bets#gametype e.g 3#kenno.NB max number of bets per session is 3 and max number of days is 31"; 
        }
				 }else{
   result="Invalid format "+kennosms+" kindly try again i.e No of bets#gametype e.g 3#kenno.NB max number of bets per session is 3 and max number of days is 31"; 
        }
		 
    }else{	
        numberofbets=tempkenno[0];
	    kennogame="Kenno";
	
        if(numberofbets.length()==1 && isNumericString(numberofbets)==true){
        int usaged=Integer.parseInt(numberofbets)*50;
        int balanceafterplay=0;
	//	if(isNumericString(numberofbets)==true){
		
if(Integer.parseInt(numberofbets)<=3){
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
     Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
 String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+kennogame+"','"+linkid+"',"+usaged+","+balanceafterplay+")";
        
/*String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Numberofbets,Days,Amount_daily) values (" + Mobileno+ ",'"+kennosms+"','"+kennogame+"',"+Integer.parseInt(numberofbets)+","+1+","+usaged+")";

*/


     //data.insert(sub);
     data.insert(insert);
String sms2go=numberofbets+"#lp#Kenno";


String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+kennosms+"','"+kennogame+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+1+","+usaged+","+1+")";

     data.insert(sub);
    

 /*result="You will receive confirmation of  "+numberofbets+" number of  bets for Kenno game.KCS Wallet balance is Ksh "+balanceafterplay+"/="; */   
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}      
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
    }else{
   result="Invalid format "+kennosms+" kindly try again i.e No of bets#gametype e.g 3#kenno.NB max number of bets per session is 3 and max number of days is 31"; 
        }
		}else{
   result="Invalid format "+kennosms+" kindly try again i.e No of bets#gametype e.g 3#kenno.NB max number of bets per session is 3 and max number of days is 31"; 
        }
	
	}
     b=b+1;	
       }
	 
    }
	else if(kennosms.toLowerCase().startsWith("k")){
     int usaged=50;
     int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
     Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
 String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+kennogame+"','"+linkid+"',"+usaged+","+balanceafterplay+")";
 /*String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Numberofbets,Days,Amount_daily) values (" + Mobileno+ ",'"+kennosms+"','"+kennosms+"',"+1+","+1+","+usaged+")";
     data.insert(sub);*/


       data.insert(insert);

String sms2go="Kenno";


String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+kennosms+"','"+kennogame+"','"+sms2go+"','"+linkid+"',"+1+","+1+","+usaged+","+1+")";

     data.insert(sub);
 
       //result="You will receive the confirmation Bets for Kenno shortly, your KCS Wallet balance is Ksh "+balanceafterplay+"/=";
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}     
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
   }else{
      result="Invalid format "+kennosms+" kindly try again i.e No of bets#gametype e.g 3#kenno.NB max number of bets per session is 3 and max number ofdays is 30"; 
     }
   
}
  catch(Exception e){e.printStackTrace();}
   return result;
}

public static String getManualKennoResponse(String Mobileno,String kennosms,int msgID,String linkid){

        int counter=counterhash(kennosms);
	 String MsgID=Integer.toString(msgID);
        String numofbetts=null;
        String gametype=null;
       String kannobetts=null;
       String result=null;
       int balanceafterplay=0;
       int usaged=50;
     if(counter==1){
       gametype=getKennogame(kennosms);
       kannobetts=getKennobetts(kennosms);
      if(getSuccess(kannobetts) && (hasDuplicates(kannobetts)==false)){
        if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
        //String name=getTransName(Mobileno); 
      Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 
 String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+gametype+"','"+linkid+"',"+usaged+","+balanceafterplay+")";
     data.insert(insert);
     String validsms=getValidsms(kannobetts);
     String sms2go=validsms+"#Kenno";
 //result="Congratulations!Your Kenno Bets "+kannobetts+" would be put to draw  and  KCS Wallet balance is Ksh "+balanceafterplay+"/="; 
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}        
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }

      }else{
    result="Invalid kenno bets.Kindly choose between 2 and 10 numbers within the range of 1-60.Repetition of numbers not allowed";

       }
       }else{
          numofbetts=getNumberofbetts(kennosms);
          kannobetts=getKennobetts2(kennosms);
          gametype=getKennogametype2(kennosms);
    if(numofbetts.length()==1 && (isNumericString(numofbetts)==true)){
		
		
		
        if(getSuccess(kannobetts) && (hasDuplicates(kannobetts)==false)){
        if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=(usaged*2))){
         balanceafterplay=getBalanceResponse(Mobileno)-(usaged*Integer.parseInt(numofbetts));
         int totalusaged=(usaged*Integer.parseInt(numofbetts));
       // String name=getTransName(Mobileno); 
    Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 


 String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+gametype+"','"+linkid+"',"+totalusaged+","+balanceafterplay+")";
     data.insert(insert);
     String validsms=getValidsms(kannobetts);
String sms2go=numofbetts+"#"+validsms+"#Kenno";

 //result="Congratulations!Your Kenno Bets "+kannobetts+" would be put to draw  and  KCS Wallet balance is Ksh "+balanceafterplay+"/="; 
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(totalusaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(totalusaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}        
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }

      }else{
    result="Invalid kenno bets.Kindly choose between 2 and 10 numbers within the range of 1-60.Repetition of numbers not allowed";

       }
	   }else{
    result="Invalid kenno bets.Kindly choose between 2 and 10 numbers within the range of 1-60.Repetition of numbers not allowed";

       }
      }
       
      // System.out.println("SUCCESS: "+kannobetts+" Results:"+getSuccess(kannobetts));
       
        

        
	return result;
}
	


public static boolean  getSuccessLotto(String smsm) throws NumberFormatException{ 
     // String numberofbets=smsm ; 
     int count=0;
     count=countercomma(smsm); 
     String[] strArray =smsm.split(",|\\:|\\;|\\-|\\_|\\.",count+1);
     boolean success=false;
      
     System.out.println("ARRAY:"+strArray.length+"\n");
     System.out.println("COUNT:"+count+"\n");
    //System.out.println("Bets:"+getbetts(smsm)+"\n");
   try{
      for(int b =0; b < strArray.length ; b++){
         if(isNumericString(strArray[b])==true){
          if(Integer.parseInt(strArray[b])>=1 && Integer.parseInt(strArray[b])<=36 && strArray.length==6){
             
             success=true;
             

         }else{

          

        success=false;
          break;
          } }else{

        success=false;

         break;

          }
                 
                
        } }
        catch(NumberFormatException e){e.printStackTrace();}
        finally{

        }

return success;

       }

	   
 public static boolean  getlucky3Success(String smsm) throws NumberFormatException{ 
     // String numberofbets=smsm ; 
     int count=0;
     count=countercomma(smsm); 
     String[] strArray =smsm.split(",|\\:|\\;|\\-|\\_|\\.",count+1);
     boolean success=true;
     // int[] numbers = new int[numberStrs.length];
     int[] numbers = new int[strArray.length];

     System.out.println("ARRAY:"+strArray.length+"\n");
     System.out.println("COUNT:"+count+"\n");
    //System.out.println("Bets:"+getbetts(smsm)+"\n");
   try{
      for(int b =0; b < strArray.length ; b++){
          if(isNumericString(strArray[b])==true){ 
          numbers[b] = Integer.parseInt(strArray[b]);
          
          if(strArray[b].length()==3){
             //(templucky3[0].length()==3)
             success=true;
            
            //System.out.println("Bets Shits:"+numbers[b]+"\n");

         }else{

          

        success=false;
         break;

          } }else{

        success=false;

         break;

          }
                 
                
        } }
        catch(NumberFormatException e){e.printStackTrace();}
        finally{

        }

return success;

       }

public static String getManualLottoResponse(String Mobileno,String lottosms,int msgID,String linkid){

    /*	*/

      int counter=counterhash(lottosms);
	 String MsgID=Integer.toString(msgID);
        String numofbetts=null;
       String gametype=null;
       String lottobetts=null;
       //String numofbetts=null;
       String result=null;
       int balanceafterplay=0;
       int usaged=50;
       if(counter==2){

      numofbetts=getNumberofLottobetts(lottosms);
      lottobetts=getLottobetts2(lottosms);
      gametype=getLottogametype2(lottosms);
	  if(numofbetts.length()==1 && (isNumericString(numofbetts)==true)){
      int totalusaged=Integer.parseInt(numofbetts)*50;      
      if(getSuccessLotto(lottobetts) && (hasDuplicates(lottobetts)==false)){
        if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=(totalusaged))){
         balanceafterplay=getBalanceResponse(Mobileno)-totalusaged;
        //String name=getTransName(Mobileno); 
    Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 
 String insert = "insert into Transaction_accnt(Name,msisdn,Play,Mpesa_code,Balance) values ('" + name+ "',"+Mobileno+","+totalusaged+",'"+linkid+"',"+balanceafterplay+")";
     data.insert(insert);
     String validsms=getValidsms(lottobetts);

String sms2go=numofbetts+"#"+validsms+"#Lotto";

 //result="Congratulations!Your Lotto Bets "+lottobetts+" would be put to draw  and  KCS Wallet balance is Ksh "+balanceafterplay+"/=";
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(totalusaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(totalusaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}     
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }

      }else{
       result="Invalid lotto bets.Kindly Pick 6 numbers between 1 and 36.Repetition of numbers not allowed";

       } 
	   }else{
result="Invalid lotto bets.Kindly Pick 6 numbers between 1 and 36.Repetition of numbers not allowed";

       }
        }else{

        gametype=getLottogame(lottosms);
        lottobetts=getLottobetts(lottosms);
        
      if(getSuccessLotto(lottobetts) && (hasDuplicates(lottobetts)==false)){
        if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
        //String name=getTransName(Mobileno); 
      Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 
 String insert = "insert into Transaction_accnt(Name,msisdn,Play,Mpesa_code,Balance) values ('" + name+ "',"+Mobileno+","+usaged+",'"+linkid+"',"+balanceafterplay+")";
     data.insert(insert);
     String validsms=getValidsms(lottobetts);
     String sms2go=validsms+"#Lotto";
// result="Congratulations!Your Lotto Bets "+lottobetts+" would be put to draw  and  KCS Wallet balance is Ksh "+balanceafterplay+"/="; 
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}         
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }

      }else{
       result="Invalid lotto bets.Kindly Pick 6 numbers between 1 and 36.Repetition of numbers not allowed";

       }
       }

       
        

        
	return result;
}
	
public static String getLottoResponse(String Mobileno,String lottosms,int msgID,String linkid){
//2#lotto
	String result=null;
	

	 String delimiterlotto = "#";
	 int length=0;
	 String MsgID=Integer.toString(msgID);
         String[] templotto;
         String lottogame="";
         String numberofbets="";
	String numberofdays="";
	int amountspent=0;
	int currentbalance=0;
	int balanceafter=0;
	boolean retval = lottosms.contains(delimiterlotto);
	int counter=counterhash(lottosms);
	System.out.println("Counter:"+counter);
  
      if(counter==2){
		
      templotto= lottosms.split(delimiterlotto,3);
	
      for(int b =0; b < templotto.length ; b++){
    numberofbets=templotto[0];
	lottogame=templotto[1];
	numberofdays=templotto[2];
	
       }
	     if((numberofbets.length()==1) && (numberofdays.length()<=2) && (isNumericString(numberofbets)==true) && (isNumericString(numberofdays)==true)){
   
	   //&& (isNumericString(numofbetts)==true)
if((lottogame.startsWith("l") || lottogame.startsWith("L")) && (Integer.parseInt(numberofbets))<=3 && (Integer.parseInt(numberofdays))<=31){
	
	
    lottogame="Lotto";
    int usaged=Integer.parseInt(numberofbets)*50;
    int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
    Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 


       //Name,msisdn,Message,Mpesa_code,Balance 
 String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+"',"+Mobileno+",'"+lottogame+"','"+linkid+"',"+usaged+","+balanceafterplay+")";
 
 data.insert(insert);

 
String sms2go=null;
String lev=null;
//String sms2go=numberofbets+"#lp#Lotto";
if(linkid.toLowerCase().contains("Day")){
// 1#s#kenno#1#7
lev=getSubLevel(linkid);
sms2go=numberofbets+"#s#lotto#50#"+lev+"#"+numberofdays;
       
String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lottosms+"','"+lottogame+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+Integer.parseInt(numberofdays)+","+usaged+","+Integer.parseInt(lev)+")";
 data.insert(sub);
}else{

sms2go=numberofbets+"#s#lotto#50#1#"+numberofdays;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lottosms+"','"+lottogame+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+Integer.parseInt(numberofdays)+","+usaged+","+1+")";

data.insert(sub);

}
 
 
 
 	 
	 
     //data.insert(sub);

 /*result="You have successfully subscribed to Lotto for "+numberofdays+" days.Ksh "+usaged+" will be deducted daily.KCS Wallet balance is Ksh "+balanceafterplay+"/=";  */
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}   
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
     }else{
        
result="Invalid format "+lottosms+" kindly try again i.e No of bets#gametype#days e.g 3#lotto#7.NB max number of bets per session is 3 and max number of days is 31 "; 
        }
		 }else{
        
result="Invalid format "+lottosms+" kindly try again i.e No of bets#gametype#days e.g 3#lotto#7.NB max number of bets per session is 3 and max number of days is 31 "; 
        }

      
}else if(counter==1){
	
    templotto= lottosms.split(delimiterlotto,2);
	int b =0;
    for(b =0; b < templotto.length ; b++){
		
	if(templotto[0].toLowerCase().startsWith("l") && templotto[1].length()<=2){
		
		/*if((templucky3[1].toLowerCase().startsWith("lu")|| templucky3[1].toLowerCase().startsWith("ru") ||templucky3[1].toLowerCase().startsWith("ra"))&& templucky3[0].length()==1 && templucky3[3].length()<=2 && templucky3[2].length()<=4)*/
				//if(Integer.parseInt(numberofbets)<=3 && Integer.parseInt(costpergame)>=32)
		
	       lottogame=templotto[0];
	       numberofdays=templotto[1];
 if(isNumericString(numberofdays)==true) {
		   
		   if(Integer.parseInt(numberofdays)<=31  ){
		 		
	    int usaged=50; 
        int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
     Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
 String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+"',"+Mobileno+",'"+lottogame+"','"+linkid+"',"+usaged+","+balanceafterplay+")";
 
 
     data.insert(insert);

String sms2go=null;
String lev=null;
//String sms2go=numberofbets+"#lp#Lotto";
if(linkid.toLowerCase().contains("Day")){
// 1#s#kenno#1#7
lev=getSubLevel(linkid);
sms2go="1#s#lotto#50#"+lev+"#"+numberofdays;
       
String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lottosms+"','"+lottogame+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(numberofdays)+","+usaged+","+Integer.parseInt(lev)+")";
 data.insert(sub);
}else{

sms2go="1#s#lotto#50#1#"+numberofdays;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lottosms+"','"+lottogame+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(numberofdays)+","+usaged+","+1+")";

data.insert(sub);

}
	 
	 
	 

/* result="You have successfully subscribed to Lotto for "+numberofdays+" days.Ksh 50/= will be deducted daily.KCS Wallet balance is Ksh "+balanceafterplay+"/="; */ 


if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}      
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
		 }else{
        
result="Invalid format "+lottosms+" kindly try again i.e No of bets#gametype#days e.g 3#lotto#7.NB max number of bets per session is 3 and max number of days is 31 "; 
        } }else{

 result="Invalid format "+lottosms+" kindly try again i.e No of bets#gametype#days e.g 3#lotto#7.NB max number of bets per session is 3 and max number of days is 31 ";
        }

 }
       else if(templotto[1].toLowerCase().startsWith("l") && templotto[0].length()==1){	
        numberofbets=templotto[0];
	   lottogame=templotto[1];
if (isNumericString(numberofbets)==true){
           if(Integer.parseInt(numberofbets)<=3 ){
	
        int usaged=(Integer.parseInt(numberofbets))*50;
        int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
         Vector vr = new Vector();
         vr.clear();
         DBBean vecRec = new DBBean();
        vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
 String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+lottogame+"','"+linkid+"',"+usaged+","+balanceafterplay+")";


String sms2go=numberofbets+"#lp#Lotto";


String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lottosms+"','"+lottogame+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+1+","+usaged+","+1+")";

     data.insert(sub);
     data.insert(insert);

 //result="You will receive the confirmation Bets for Lotto shortly, your KCS Wallet balance is Ksh "+balanceafterplay+""; 
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}      
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
      }else{
 result="Invalid format "+lottosms+" kindly try again i.e No of bets#gametype e.g 3#lotto.NB max number of bets per session is 3 and max number of bets is 3";
      } 
}else{ result="Invalid format "+lottosms+" kindly try again i.e No of bets#gametype#days e.g 3#lotto#7.NB max number of bets per session is 3 and max number of days is 31 ";
}
	}else{
         result="Invalid format "+lottosms+" kindly try again i.e No of bets#gametype#days e.g 3#lotto#7.NB max number of bets per session is 3 and max number of days is 31";
}
	
       b=b+1;
      }
		
    } else if(lottosms.toLowerCase().startsWith("Lo".toLowerCase()) || lottosms.toLowerCase().startsWith("Ro".toLowerCase())||lottosms.toLowerCase().startsWith("Rt".toLowerCase())) {
     int usaged=50;
     int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
         Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+"',"+Mobileno+",'"+lottosms+"','"+linkid+"',"+usaged+","+balanceafterplay+")";

String sms2go="Lotto";


String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lottosms+"','"+lottogame+"','"+sms2go+"','"+linkid+"',"+1+","+1+","+usaged+","+1+")";


       data.insert(sub);
       data.insert(insert);

       //result="You will receive the confirmation Bets for Lotto shortly,your KCS Wallet balance is Ksh "+balanceafterplay+"";

if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}         
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
  }
else{
        
result="Invalid format "+lottosms+" kindly try again i.e No of bets#gametype#days e.g 3#lotto#7.NB max number of bets per session is 3 and max number of days is 31"; 
        }
   return result;
}

//automatic lucky3

public static String getLucky3Response(String Mobileno,String lucky3sms,int msgID,String linkid){
//2#lucky3
	String result=null;
	

	 String delimiterlucky3 = "#";
	 int length=0;
    String MsgID=Integer.toString(msgID); 
         String[] templucky3;
         String lucky3game="";
         String numberofbets="";
	String betts="";
	String numberofdays="";
	String costpergame="";
	int amountspent=0;
	int currentbalance=0;
	int balanceafter=0;
	boolean retval = lucky3sms.contains(delimiterlucky3);
	int counter=counterhash(lucky3sms);
	System.out.println("Counter:"+counter);
  
if(counter==3){
		
      templucky3= lucky3sms.split(delimiterlucky3,4);
	  
      int b=0;	  
     for( b =0; b < templucky3.length ; b++){
		 
		  //if((numberofbets.length()==1) && (numberofdays.length()<=2)){
   
if((templucky3[1].toLowerCase().startsWith("lu")|| templucky3[1].toLowerCase().startsWith("ru") ||templucky3[1].toLowerCase().startsWith("ra"))&& templucky3[0].length()==1 && templucky3[3].length()<=2 && templucky3[2].length()<=4){
//2#lp#lucky3#250


             numberofbets=templucky3[0];
	     lucky3game="Lucky3";
	     costpergame=templucky3[2];
	     numberofdays=templucky3[3];
	if((isNumericString(numberofdays)==true)&& (isNumericString(numberofbets)==true)) {
        if(Integer.parseInt(numberofbets)<=3 && Integer.parseInt(numberofdays)<=31 &&  Integer.parseInt(costpergame)>=50){
    int usaged=(Integer.parseInt(numberofbets))*(Integer.parseInt(costpergame));
    int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
     Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" +name+"',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";


String sms2go=null;
String lev=null;
//String sms2go=numberofbets+"#lp#Lotto";
if(linkid.toLowerCase().contains("Day")){
// 1#s#kenno#1#7
lev=getSubLevel(linkid);
sms2go=numberofbets+"#s#lucky3#"+costpergame+"#"+lev+"#"+numberofdays;
       
String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+Integer.parseInt(numberofdays)+","+usaged+","+Integer.parseInt(lev)+")";
 data.insert(sub);
}else{

sms2go=numberofbets+"#s#lucky3#"+costpergame+"#1#"+numberofdays;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(numberofdays)+","+usaged+","+1+")";

data.insert(sub);

}


     data.insert(insert);


 /*result="You will receive the confirmation Bets for "+lucky3game+" shortly, Ksh "+usaged+" will be deducted daily for "+numberofdays+" your KCS Wallet balance is Ksh "+balanceafterplay+""; */

//String sms2go=numberofbets+"#lp#Lucky3#"+costpergame;

if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}         
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
	}else{
		         result="Invalid format kindly sms i.e number of betts#betts#gametype#amount e.g 2#235#lucky3#250   to 22225.";
	
	}
	}else{
		         result="Invalid format kindly sms i.e number of betts#betts#gametype#amount e.g 2#235#lucky3#250   to 22225.";
	
	}
		  
	  }
	else if((templucky3[2].toLowerCase().startsWith("lu")|| templucky3[2].toLowerCase().startsWith("ru") ||templucky3[2].toLowerCase().startsWith("ra"))&& templucky3[0].length()==1 && templucky3[1].length()==3 && templucky3[3].length() <=4) {
//2#234#lucky3#250	
         numberofbets=templucky3[0];
	     betts=templucky3[1];
	     lucky3game="Lucky3";
	     costpergame=templucky3[3];
	if((isNumericString(betts)==true)&& (isNumericString(numberofbets)==true) && (isNumericString(costpergame)==true)) {
       if(Integer.parseInt(numberofbets)<=3 && Integer.parseInt(costpergame)>=32){
		   
		   
    int usaged=Integer.parseInt(numberofbets)*Integer.parseInt(costpergame);
    int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
     Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";


String sms2go=numberofbets+"#"+betts+"#Lucky3#"+costpergame;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+1+","+usaged+","+1+")";




     data.insert(insert);
     data.insert(sub);


 /*result="Congratulations,your "+betts+"  bett will be put to draw Ksh "+usaged+" have been deducted ,your KCS Wallet balance is Ksh "+balanceafterplay+""; */


if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}        
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
	
	}else{
		         result="Invalid format kindly sms i.e number of betts#betts#gametype#amount e.g 2#235#lucky3#250   to 22225.";
	
	}
	}else{
		         result="Invalid format kindly sms i.e number of betts#betts#gametype#amount e.g 2#235#lucky3#250   to 22225.";
	
	}
	}else{
		         result="Invalid format kindly sms i.e number of betts#betts#gametype#amount e.g 2#235#lucky3#250   to 22225.";
	
	}
	
	b=b+3;
}
	
}else if(counter==2){
		
      templucky3= lucky3sms.split(delimiterlucky3,3);
	int b=0;
      for( b =0; b < templucky3.length ; b++){
		  
  if((templucky3[1].toLowerCase().startsWith("lu")|| templucky3[1].toLowerCase().startsWith("ru") ||templucky3[1].toLowerCase().startsWith("ra")) && templucky3[2].length()<=4  && templucky3[0].length()==1) {

//2#lp#lucky3#250

           numberofbets=templucky3[0];

           lucky3game="Lucky3";

           costpergame=templucky3[2];

     

     if((isNumericString(templucky3[0])==true) && (isNumericString(templucky3[2])==true)) {

     if(Integer.parseInt(costpergame)>=32  && Integer.parseInt(numberofbets)<=3){

      

      

       int usaged=Integer.parseInt(numberofbets)*Integer.parseInt(costpergame);

       int balanceafterplay=0;

       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){

         balanceafterplay=getBalanceResponse(Mobileno)-usaged;

       Vector vr = new Vector();

     vr.clear();

     DBBean vecRec = new DBBean();

     vr=vecRec.getNameAndMpesaCode(Mobileno);  

   // int totconts=vr.size();

        String name=(String)vr.get(0); 

        String mpesa_code=(String)vr.get(1); 



       //Name,msisdn,Message,Mpesa_code,Balance 

String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";







String sms2go=numberofbets+"#lp#Lucky3#"+costpergame;



String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+1+","+usaged+","+1+")";



     data.insert(insert);

     data.insert(sub);



 /*result="You will receive the confirmation Bets for Lucky3  shortly Ksh "+usaged+" will be deducted  and KCS Wallet balance is Ksh "+balanceafterplay+""; */





if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){



result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;

System.out.println("RESULT For GI>>:"+result); 

}else{

result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;

System.out.println("RESULT For GI>>:"+result); 



}       

         }else{

   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222.Available balance is Ksh "+getBalanceResponse(Mobileno)+"";

      

     

}

}  

    else if(Integer.parseInt(templucky3[2])<=31 && Integer.parseInt(templucky3[0])<=3){

//2#lp#lucky3#2



       numberofbets=templucky3[0];

           lucky3game="Lucky3";

           numberofdays=templucky3[2];

     

    

     int usaged=Integer.parseInt(numberofbets)*50;

       int balanceafterplay=0;

       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){

         balanceafterplay=getBalanceResponse(Mobileno)-usaged;

        Vector vr = new Vector();

     vr.clear();

     DBBean vecRec = new DBBean();

     vr=vecRec.getNameAndMpesaCode(Mobileno);  

   // int totconts=vr.size();

        String name=(String)vr.get(0); 

        String mpesa_code=(String)vr.get(1); 



       //Name,msisdn,Message,Mpesa_code,Balance 

String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";



String sms2go=null;

String lev=null;

//String sms2go=numberofbets+"#lp#Lotto";

if(linkid.toLowerCase().contains("Day")){

// 1#s#kenno#1#7

lev=getSubLevel(linkid);

sms2go=numberofbets+"#s#lucky3#50#"+lev+"#"+numberofdays;

       

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+Integer.parseInt(numberofdays)+","+usaged+","+Integer.parseInt(lev)+")";

 data.insert(sub);

}else{



sms2go=numberofbets+"#s#lucky3#50#1#"+numberofdays;



String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+Integer.parseInt(numberofdays)+","+usaged+","+1+")";



data.insert(sub);



}





     data.insert(insert);





 /*result="You have subscribed to lucky3 game for "+numberofdays+"."+usaged+" will be deducted daily,your KCS Wallet balance is Ksh "+balanceafterplay+""; */

//String sms2go=numberofbets+"#s#Lucky3";



result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;

System.out.println("RESULT For GI>>:"+result);       

         }else{

   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222.Available balance is Ksh "+getBalanceResponse(Mobileno)+"";

         }

   }  else{



         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";

}





     

     

    }  else{



         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";

}

}		   



/*   
if((templucky3[1].toLowerCase().startsWith("lu")|| templucky3[1].toLowerCase().startsWith("ru") ||templucky3[1].toLowerCase().startsWith("ra")) && templucky3[2].length()<=4  && templucky3[0].length()==1) {
//2#lp#lucky3#250
	          numberofbets=templucky3[0];
	          lucky3game="Lucky3";
	          costpergame=templucky3[2];
			  
			  if((isNumericString(numberofbets)==true) && (isNumericString(costpergame)==true)) {
			  if(Integer.parseInt(costpergame)>=32  && Integer.parseInt(numberofbets)<=3){
				  
				  
       int usaged=Integer.parseInt(numberofbets)*Integer.parseInt(costpergame);
       int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
       Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";



String sms2go=numberofbets+"#lp#Lucky3#"+costpergame;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+1+","+usaged+","+1+")";

     data.insert(insert);
     data.insert(sub);

 


if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}       
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222.Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
      
			  
}
}  else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
}
}  else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
}
}
*/



		
		else if((templucky3[1].toLowerCase().startsWith("lu")|| templucky3[1].toLowerCase().startsWith("lu")||templucky3[1].toLowerCase().startsWith("ra")) &&
templucky3[2].length()<=4  && (templucky3[0].length()==3)) {
//245#lucky3#250
		      betts=templucky3[0];
	          lucky3game="Lucky3";
	          costpergame=templucky3[2];
			   if((isNumericString(betts)==true) && (isNumericString(costpergame)==true)) {
			 if( Integer.parseInt(costpergame)>=50 ){
			  //if(costpergame.length()<=4){
			  
			  int usaged=Integer.parseInt(costpergame);
       int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
          balanceafterplay=getBalanceResponse(Mobileno)-usaged;
    Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+"',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";



String sms2go="1#"+betts+"#Lucky3#"+costpergame;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+1+","+usaged+","+1+")";




     data.insert(insert);
     data.insert(sub);
/*Check again*/
//result="Congratulations!Your "+betts+" will be put to draw  and KCS Wallet balance is Ksh "+balanceafterplay+"/=";  


result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
//result="Ok:"+Mobileno+",4900,SAF_"+mpesa_code+","+name+","+Integer.toString(usaged)+",Lucky3";
System.out.println("RESULT For GI>>:"+result);      
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
		 }  else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
}
}  else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
}

			  
		  }  /* else if((templucky3[1].toLowerCase().startsWith("lu")|| templucky3[1].toLowerCase().startsWith("ru") ||templucky3[1].toLowerCase().startsWith("ra")) && templucky3[0].length()==1 && templucky3[2].length()<=2){
//2#lp#lucky3#2

		  numberofbets=templucky3[0];
	          lucky3game="Lucky3";
	          numberofdays=templucky3[2];
			  
			  if((isNumericString(numberofbets)==true) && (isNumericString(numberofdays)==true)){
			  if(Integer.parseInt(numberofdays)<=31 && Integer.parseInt(numberofbets)<=3){
			  int usaged=Integer.parseInt(numberofbets)*50;
       int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
        Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";

String sms2go=null;
String lev=null;
//String sms2go=numberofbets+"#lp#Lotto";
if(linkid.toLowerCase().contains("Day")){
// 1#s#kenno#1#7
lev=getSubLevel(linkid);
sms2go=numberofbets+"#s#lucky3#"+lev+"#50#"+numberofdays;
       
String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+Integer.parseInt(numberofdays)+","+usaged+","+Integer.parseInt(lev)+")";
 data.insert(sub);
}else{

sms2go=numberofbets+"#s#lucky3#1#50#"+numberofdays;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+Integer.parseInt(numberofdays)+","+usaged+","+1+")";

data.insert(sub);

}


     data.insert(insert);


 

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result);       
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222.Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
		 }  else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
}
}  else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
}

			  
			  
		  }*/ else if((templucky3[2].toLowerCase().startsWith("lu")|| templucky3[2].toLowerCase().startsWith("ru") || templucky3[2].toLowerCase().startsWith("ra")) && templucky3[1].length()==3 && templucky3[0].length()==1){
//2#234#Lucky3#		
                 numberofbets=templucky3[0];
	          lucky3game="Lucky3";
	          betts=templucky3[1];
			  if((isNumericString(numberofbets)==true) && (isNumericString(betts)==true)){
			 if(Integer.parseInt(numberofbets)<=3 ){ 
       int usaged=Integer.parseInt(numberofbets)*50;
       int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
        Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+"',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";



String sms2go=numberofbets+"#"+betts+"#Lucky3#";

String sub = "insert into SubscribedDaily(msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Manual_betts,Manual_betts,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+",'"+betts+"',"+1+","+usaged+","+1+")";



     data.insert(insert);
     data.insert(sub);
/*Check for manual*/
 //result="Congratulations your "+betts+" will be put to draw. Ksh "+usaged+" has been deducted,your KCS Wallet balance is Ksh "+balanceafterplay+""; 



if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}        
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222.Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
			 } else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
}
} else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
}
			  
			  
		  }else if((templucky3[0].toLowerCase().startsWith("lu")|| templucky3[0].toLowerCase().startsWith("ru") ||templucky3[0].toLowerCase().startsWith("ra")) && (templucky3[1].length()<=4) && (templucky3[2].length()<=2)){
	//lucky3#250#4
	lucky3game="Lucky3";
	costpergame=templucky3[1];
	numberofdays=templucky3[2];
	if((isNumericString(costpergame)==true) && (isNumericString(numberofdays)==true)){
	if(Integer.parseInt(costpergame)>=32 && Integer.parseInt(numberofdays)<=31){
		
	int usaged=Integer.parseInt(costpergame);
       int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
        Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";

String sms2go=null;
String lev=null;
//String sms2go=numberofbets+"#lp#Lotto";
if(linkid.toLowerCase().contains("Day")){
// 1#s#kenno#1#7
lev=getSubLevel(linkid);
sms2go="1#s#lucky3#"+lev+"#"+costpergame+"#"+numberofdays;


       
String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(numberofdays)+","+usaged+","+Integer.parseInt(lev)+")";
 data.insert(sub);
}else{

sms2go="1#s#lucky3#1#"+costpergame+"#"+numberofdays;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(numberofdays)+","+usaged+","+1+")";

data.insert(sub);

}


     data.insert(insert);




if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}         
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
         }  else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
        }
		
		}  else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
        }
		}  else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
        }
		
       b=b+2;	
       }
	   
       

  }

    
else if(counter==1){
System.out.println("SHIDA KAMA CHELADIES INAANZA HAPA!!!!!!!!");
    templucky3= lucky3sms.split(delimiterlucky3,2);
	int b =0;
    for(b =0; b < templucky3.length ; b++){

System.out.println("Records no 1:"+templucky3[0] +"\nRecords no 2:"+templucky3[1]+"\n\n");
         
            
   
        
  if( (templucky3[0].toLowerCase().startsWith("lu")||templucky3[0].toLowerCase().startsWith("ru")||templucky3[0].toLowerCase().startsWith("ra")) && (templucky3[1].length()<=4)){
//1#lp#lucky3#250
        lucky3game=templucky3[0];
         costpergame=templucky3[1];
		if((isNumericString(templucky3[1])==true)){
		if( Integer.parseInt(costpergame)>=32 ){
      int usaged=Integer.parseInt(costpergame);


       int balanceafterplay=0;


       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;

        Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+"',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";




String sms2go="1#lp#lucky3#"+costpergame;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+1+","+usaged+","+1+")";

data.insert(sub);



 data.insert(insert);

 /*result="You have successfully subscribed to "+lucky3game+" KSH "+usaged+"   will be deducted .KCS Wallet balance is Ksh "+balanceafterplay+"/=";*/



if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}         
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         } 
		 
         } 
      else if(Integer.parseInt(templucky3[1])<=31){
 int usaged=50; 
        int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
     Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
 String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+"',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";
 
 
     data.insert(insert);

String sms2go=null;
String lev=null;
//String sms2go=numberofbets+"#lp#Lotto";
if(linkid.toLowerCase().contains("Day")){
// 1#s#kenno#1#7
lev=getSubLevel(linkid);
sms2go="1#s#lucky#"+lev+"#50#"+templucky3[1];
       
String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(templucky3[1])+","+usaged+","+Integer.parseInt(lev)+")";
 data.insert(sub);
}else{

sms2go="1#s#lucky#1#50#"+templucky3[1];

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(templucky3[1])+","+usaged+","+1+")";

data.insert(sub);

}
	 
	 
	 

/* result="You have successfully subscribed to Lotto for "+numberofdays+" days.Ksh 50/= will be deducted daily.KCS Wallet balance is Ksh "+balanceafterplay+"/="; */ 


if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}      
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
	

     }

 else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250  or 	 to 22225.";
        }
	}

 else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250  or 	 to 22225.";
        }	

	}
/*else if( (templucky3[0].toLowerCase().startsWith("lu")||templucky3[0].toLowerCase().startsWith("ru")||templucky3[0].toLowerCase().startsWith("ra")) && (templucky3[1].length()<=2)){

	lucky3game=templucky3[0];
	numberofdays=templucky3[1];
System.out.println("SHIDA KAMA");
	if (Integer.parseInt(numberofdays)<32){
	  numberofdays=templucky3[1];
	   int usaged=50;
        int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
        Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+"',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";

String sms2go=null;
String lev=null;

if(linkid.toLowerCase().contains("Day")){

lev=getSubLevel(linkid);
sms2go="1#s#lucky3#"+lev+"#50#"+numberofdays;
       
String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(numberofdays)+","+usaged+","+Integer.parseInt(lev)+")";
 data.insert(sub);
}else{

sms2go="1#s#lucky3#1#50#"+numberofdays;

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+Integer.parseInt(numberofdays)+","+usaged+","+1+")";

data.insert(sub);

}



     data.insert(insert);

 
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}      
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         } 
		  }  else{

         result="Invalid format kindly sms i.e bets#gametype#amount e.g 235#lucky3#250   to 22225.";
        }
	} 
*/
else if( (templucky3[1].toLowerCase().startsWith("lu")||templucky3[1].toLowerCase().startsWith("ru")||templucky3[1].toLowerCase().startsWith("ra"))&& templucky3[0].length()==3){
//258#Lucky3
		betts=templucky3[0];
		lucky3game="Lucky3";
	if((isNumericString(betts)==true)){
	   // numberofdays=templucky3[1];
	   int usaged=50;
        int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
        Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+lucky3game+"','"+mpesa_code+"',"+usaged+","+balanceafterplay+")";
/*String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Days,Amount_daily) values (" + Mobileno+ 
",'"+lucky3sms+"','"+lucky3game+"',"+1+","+Integer.parseInt(numberofdays)+","+usaged+")";
    */
String sms2go=betts+"#Lucky3";

String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Manual_betts,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+1+","+usaged+",'"+betts+"',"+1+")";




 data.insert(sub);
     data.insert(insert);
/*To Check Manual*/
 //result="Congratulations!Your lucky Bet "+betts+" would be put to draw  and  KCS Wallet balance is Ksh "+balanceafterplay+"/=";


if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}       
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         } }else{	
		 result="Invalid format "+lucky3sms+" kindly try again i.e No of bets#gametype#days e.g 3#lucky.NB max number of bets per session is 3 and max number of days is 31"; 
       
	}
	
	
	}else if( (templucky3[1].toLowerCase().startsWith("lu")||templucky3[1].toLowerCase().startsWith("ru")||templucky3[1].toLowerCase().startsWith("ra")) && (templucky3[0].length()==1)){
//2#lp#lucky3
        numberofbets=templucky3[0];
	lucky3game="Lucky3";
	
	if((isNumericString(numberofbets)==true)){
	if(Integer.parseInt(numberofbets)<=3){
        int usaged=(Integer.parseInt(numberofbets))*50;
        int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
        Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+ "',"+Mobileno+",'"+lucky3game+"','"+linkid+"',"+usaged+","+balanceafterplay+")";

String sms2go=numberofbets+"#lp#Lucky3";



String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+Integer.parseInt(numberofbets)+","+1+","+usaged+","+1+")";



     data.insert(sub);
     data.insert(insert);

 //result="You will receive the confirmation Bets for "+lucky3game+" shortly,your KCS Wallet balance is Ksh "+balanceafterplay+"";

if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}        
         }else{
result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
		

}else{	result="Invalid format "+lucky3sms+" kindly try again i.e No of bets#gametype#days e.g 3#lucky.NB max number of bets per session is 3 and max number of days is 31"; 
       
	}
	}else{	result="Invalid format "+lucky3sms+" kindly try again i.e No of bets#gametype#days e.g 3#lucky.NB max number of bets per session is 3 and max number of days is 31"; 
       
	}
	
	}else{	result="Invalid format "+lucky3sms+" kindly try again i.e No of bets#gametype#days e.g 3#lucky.NB max number of bets per session is 3 and max number of days is 31"; 
       
	}
     b=b+1;
      }  
		
    } 
else if(lucky3sms.toLowerCase().startsWith("Lu".toLowerCase()) || lucky3sms.toLowerCase().startsWith("Ra".toLowerCase())||lucky3sms.toLowerCase().startsWith("la".toLowerCase())||lucky3sms.toLowerCase().startsWith("L3".toLowerCase())){
     int usaged=50;
     int balanceafterplay=0;
       if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=usaged)){
         balanceafterplay=getBalanceResponse(Mobileno)-usaged;
       Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);  
   // int totconts=vr.size();
        String name=(String)vr.get(0); 
        String mpesa_code=(String)vr.get(1); 

       //Name,msisdn,Message,Mpesa_code,Balance 
String insert = "insert into Transaction_accnt(Name,msisdn,Message,Mpesa_code,Play,Balance) values ('" + name+"',"+Mobileno+",'"+lucky3sms+"','"+linkid+"',"+usaged+","+balanceafterplay+")";

String sms2go="Lucky3";



String sub = "insert into SubscribedDaily (msisdn,Message,Game_type,Message_To_Gi,Correletor,Numberofbets,Days,Amount_daily,Level) values (" + Mobileno+ ",'"+lucky3sms+"','"+lucky3game+"','"+sms2go+"','"+linkid+"',"+1+","+1+","+usaged+","+1+")";



     data.insert(sub);
       data.insert(insert);

      
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(usaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}         
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
  }
else{	result="Invalid format "+lucky3sms+" kindly try again i.e No of bets#gametype#days e.g 3#lucky.NB max  number of bets per session is 3  and max number of days is 31"; 
       
	}
   return result;
}  //getLucky3Response getManualLucky3Response


public static String getManualLucky3Response(String Mobileno,String lucky3sms,int msgID,String linkid){

    /*	*/

      int counter=counterhash(lucky3sms);
	  int comma=countercomma(lucky3sms);
	 String MsgID=Integer.toString(msgID);
        String numofbetts=null;
       String gametype=null;
       String lucky3betts=null;
       String costpergame=null;
       String result=null;
       int balanceafterplay=0;
       int usaged=50;
	   //String[] tempLucky3manual=
	   if(counter==3){ 
	  
      numofbetts=getNumberofLucky3betts(lucky3sms);
      lucky3betts=getLucky3bettsManual(lucky3sms);
      gametype=getLucky3gameManual(lucky3sms);
      costpergame=getLucky3Costpergame(lucky3sms);
       int countersmscoma=counterallcommas(lucky3betts);
	   if((isNumericString(numofbetts)==true) && (isNumericString(costpergame)==true)){
	   if((numofbetts.length()==1) && (costpergame.length()<=4)){
int totalusaged=(Integer.parseInt(costpergame)*Integer.parseInt(numofbetts));
//2#245#Lucky3#150
boolean checkiftrue=false;
System.out.println("START CHECKS !!!.. "+getlucky3Success(lucky3betts));
checkiftrue=getlucky3Success(lucky3betts);
      if(checkiftrue==true){

       if((countersmscoma+1)==(Integer.parseInt(numofbetts))){

        if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=totalusaged)){
        // balanceafterplay=getBalanceResponse(Mobileno)-(usaged*2);
        //String name=getTransName(Mobileno);
balanceafterplay=getBalanceResponse(Mobileno)-totalusaged;
        //String name=getTransName(Mobileno);

Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);
   // int totconts=vr.size();
        String name=(String)vr.get(0);
        String mpesa_code=(String)vr.get(1);

 
 String insert = "insert into Transaction_accnt(Name,msisdn,Play,Mpesa_code,Balance) values ('" + name+ "',"+Mobileno+","+totalusaged+",'"+linkid+"',"+balanceafterplay+")";
     data.insert(insert);
     String validsms=getValidsms(lucky3betts);
System.out.println("Valid:"+validsms);
 //result="Congratulations!Your Lucky3 Bets "+lucky3betts+" would be put to draw  and  KCS Wallet balance is Ksh "+balanceafterplay+"/=";     

String sms2go=numofbetts+"#"+validsms+"#Lucky3#"+costpergame;
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(totalusaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(totalusaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}     
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }
    }else{

       result="Invalid lucky3 bets.Kindly Pick 3 numbers between 001 and 999 e.g   2#123,342#lucky3";
       }


      }else{
       result="Invalid lucky3 bets.Kindly Pick 3 numbers between 001 and 999";

       }
	    }else{

       result="Invalid lucky3 bets.Kindly Pick 3 numbers between 001 and 999 e.g   2#123,342#lucky3";
       }
	    }else{

       result="Invalid lucky3 bets.Kindly Pick 3 numbers between 001 and 999 e.g   2#123,342#lucky3";
       }
	   
}else{
      numofbetts=getNumberofLucky3bettsM2(lucky3sms);
      lucky3betts=getLucky3betts2(lucky3sms);
      gametype=getLucky3gametype2(lucky3sms);
	  
	  if((isNumericString(numofbetts)==true) && (isNumericString(lucky3betts)==true)){
              int countersmscoma=counterallcommas(lucky3betts);
      if(getlucky3Success(lucky3betts)){
		  if(numofbetts.length()==1){
               if((countersmscoma+1)==(Integer.parseInt(numofbetts))){

        if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=(usaged*2))){
         balanceafterplay=getBalanceResponse(Mobileno)-(usaged*Integer.parseInt(numofbetts));
       // String name=getTransName(Mobileno);
int totalusaged=(50*Integer.parseInt(numofbetts));
Vector vr = new Vector();
     vr.clear();
     DBBean vecRec = new DBBean();
     vr=vecRec.getNameAndMpesaCode(Mobileno);
   // int totconts=vr.size();
        String name=(String)vr.get(0);
        String mpesa_code=(String)vr.get(1);
 
 String insert = "insert into Transaction_accnt(Name,msisdn,Play,Mpesa_code,Balance) values ('" + name+ "',"+Mobileno+","+totalusaged+",'"+linkid+"',"+balanceafterplay+")";
     data.insert(insert);
     String validsms=getValidsms(lucky3betts);
System.out.println("Valid:"+validsms);
//result="Congratulations!Your Lucky3 Bets "+lucky3betts+" would be put to draw  and  KCS Wallet balance is Ksh "+balanceafterplay+"/=";

String sms2go=numofbetts+"#"+validsms+"#Lucky3";
if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+Integer.toString(totalusaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 
}else{
result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+Integer.toString(totalusaged)+","+sms2go;
System.out.println("RESULT For GI>>:"+result); 

}          
         }else{
   result="Insufficient Amount in your Account to Play Kindly top up your account using mpesa or airtel,the business no. is 292222 .Available balance is Ksh "+getBalanceResponse(Mobileno)+"";
         }

       }else{

       result="Invalid lucky3 bets.Kindly Pick 3 numbers between 001 and 999 e.g   2#123,342#lucky3";
       }
	   }else{

       result="Invalid lucky3 bets.Kindly Pick 3 numbers between 001 and 999 e.g   2#123,342#lucky3";
       }


	   }else{
       result="Invalid lucky3 bets.Kindly Pick 3 numbers between 001 and 999";

       }
	   	   }else{
       result="Invalid lucky3 bets.Kindly Pick 3 numbers between 001 and 999";

       }
		   
	   
        }
	   
                
        

        
	return result;
}

public static boolean filterinvalid(String sms ){
		boolean  counterchecker=false;


			
if(sms.contains("@")|| sms.contains("!") || sms.contains("$")||sms.contains("%")||sms.contains("^")||sms.contains("&") || sms.contains("(") || sms.contains(")") || sms.contains("~") || sms.contains("?") || sms.contains("<") || sms.contains(">") ){
				counterchecker=true;
			}else{
						counterchecker=false;

                       }
		return counterchecker;
		
	}
	
public static boolean  getSuccess(String smsm) throws NumberFormatException{ 
     // String numberofbets=smsm ; 
     int count=0;
     count=countercomma(smsm); 
     String[] strArray =smsm.split(",|\\:|\\;|\\-|\\_|\\.",count+1);
     boolean success=true;
     // int[] numbers = new int[numberStrs.length];
     int[] numbers = new int[strArray.length];

     System.out.println("ARRAY:"+strArray.length+"\n");
     System.out.println("COUNT:"+count+"\n");
    //System.out.println("Bets:"+getbetts(smsm)+"\n");
   try{
      for(int b =0; b < strArray.length ; b++){
if(isNumericString(strArray[b])==true){
          numbers[b] = Integer.parseInt(strArray[b]);
          
          if((numbers[b]>=1)  && (numbers[b]<=60 ) && ( strArray.length>=2) && (strArray.length<=10)){
             
             success=true;
            
            //System.out.println("Bets Shits:"+numbers[b]+"\n");

         }else{

          

        success=false;
         break;

          }}else{



          



        success=false;

         break;



          }
                 
                
        } }
        catch(NumberFormatException e){e.printStackTrace();}
        finally{

        }

return success;

       }


public static boolean checkRegistered(String msisdn) throws Exception{
        boolean regg = false;
        String sql = "select msisdn from Registered where msisdn ='"+msisdn+"'";
        try{
            s = con.createStatement();
            rs = s.executeQuery(sql);
            while(rs.next()){	 
			if (rs.getString("msisdn").trim().equals(msisdn) )
			{	
				
				regg = true;
				break;
			}
			else
			{	
				
				regg = false;
				
			}
		}
        }
        catch(Exception e){e.printStackTrace();}
        finally{

        }
        return regg;
    }
 public static int getBalanceResponse(String msisdn){
  int bal=0 ;

        try{
            s = con.createStatement();
           // String details = s.execute(sql);
            rs = s.executeQuery("select Balance from Transaction_accnt where msisdn='"+msisdn+"' order by trans_id desc limit 1");
			
	rs.next();
	bal = rs.getInt("Balance");
        }
        catch(Exception e){e.printStackTrace();}
        finally{

        }
        return bal;

}

public static String getRegdetailsResponse(String msisdn){
  String details=null ;

        try{
            s = con.createStatement();
           // String details = s.execute(sql);
            rs = s.executeQuery("select Reg_details from Registered where msisdn='"+msisdn+"'");
			
	rs.next();
	details = rs.getString("Reg_details");
        }
        catch(Exception e){e.printStackTrace();}
        finally{

        }
        return details;

}
public static String getTransName(String msisdn){
  String name=null ;

        try{
            s = con.createStatement();
           // String details = s.execute(sql);
            rs = s.executeQuery("select Name from Deposit_accnt where msisdn='"+msisdn+"' limit 1");
			
	rs.next();
	name = rs.getString("Name");
        }
        catch(Exception e){e.printStackTrace();}
        finally{

        }
        return name;

}
  
  public static  int getRandomPin() {
    
   int pin = Math.abs(new Random().nextInt(9999-1000)+1000);
    return pin;
  }
  
  public static  int getRandomWebPass() {
    
   int pass = Math.abs(new Random().nextInt(999999-100000)+100000);
    return pass;
  }


  
  



/*ADDED ACCEPT CHANGES*/

public static String getAcceptResponse(String Mobileno,String smsss){
    int mypin=getRandomPin();
	
   int mywebpass=getRandomWebPass();
String response=null;
   String delimiterAccept = "#";
   if(smsss.contains("#")){
     String[] tempregdetails;
    String accept="";
    String county="";
    tempregdetails= smsss.split(delimiterAccept,2);
    for(int b =0; b < tempregdetails.length ; b++){
    accept=tempregdetails[0];
    county=tempregdetails[1];
    }//String insert = "insert into outbound (msisdn,message,status) values (" + destination + ",'" + message + "'," +1+ ")";
    if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){
      response="Your Username is "+Mobileno+". Your PIN is "+mypin+". Your Web Password is "+mywebpass +". To Top up use Paybill no.292222";

 String insert = "insert into Registered (msisdn,Reg_details,County,Pin,WebPassword) values (" + Mobileno+ ",'" + response + "','" +county + "',"+mypin+","+mywebpass+")";

         data.insert(insert);
     }else{
 response="Your Username is "+Mobileno+". Your PIN is "+mypin+". Your Web Password is "+mywebpass +". To Top up use Airtel Money";

 String insert = "insert into Registered (msisdn,Reg_details,County,Pin,WebPassword) values (" + Mobileno+ ",'" + response + "','" +county + "',"+mypin+","+mywebpass+")";

         data.insert(insert);

     }

   }else{
       if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){
     response="Your Username is "+Mobileno+". Your PIN is "+mypin+". Your Web Password is "+mywebpass +". To Top up use Paybill no. 292222";
         String insert = "insert into Registered (msisdn,Reg_details,Pin,WebPassword) values (" + Mobileno+ ",'" + response + "',"+mypin+","+mywebpass+")";
         data.insert(insert);

      }else{
 response="Your Username is "+Mobileno+". Your PIN is "+mypin+". Your Web Password is "+mywebpass +". To Top up use Airtel Money";
         String insert = "insert into Registered (msisdn,Reg_details,Pin,WebPassword) values (" + Mobileno+ ",'" + response + "',"+mypin+","+mywebpass+")";
         data.insert(insert);
       }
  }
    
      
 
  return response; 
  }

public static String getCancelResponse(String Mobileno,String smss,int MsgID,String linkid){

    //int mypin=getRandomPin();

    // int mywebpass=getRandomWebPass();

 

 //int ticketserial=get
String result=null;
//String response=null;

   String delimiterCancel = "#";

   Vector vr = new Vector();

     vr.clear();

     DBBean vecRec = new DBBean();

     vr=vecRec.getNameAndMpesaCode(Mobileno);

   // int totconts=vr.size();

        String name=(String)vr.get(0);

        String mpesa_code=(String)vr.get(1);

   if(smss.contains("#")){

     String[] tempregdetails;

    String cancel="";

    String ticket="";

    tempregdetails= smss.split(delimiterCancel,2);

    for(int b =0; b < tempregdetails.length ; b++){

    cancel=tempregdetails[0];

    ticket=tempregdetails[1];

     

    }

 if((isNumericString(ticket)==true ) && ticket.length()==10) {

  if(Mobileno.startsWith("25472")||Mobileno.startsWith("25470")||Mobileno.startsWith("25471")){

 //String insert = "insert into outbound (msisdn,message,status) values (" + destination + ",'" + message + "'," +1+ ")";

    result="Ok:"+Mobileno+","+MsgID+",SAF_"+linkid+","+name+","+0+","+smss;

    System.out.println("RESULT For GI>>:"+result);

  }else{

  result="Ok:"+Mobileno+","+MsgID+",AIRTEL_"+linkid+","+name+","+0+","+smss;

    System.out.println("RESULT For GI>>:"+result); 

   

  }



     

  }else{

       result="Invalid format "+smss+".Kindly change and try again i.e cancel#ticketserial e.g cancel#1562898925";



       }

    }else{

       result="Invalid format "+smss+".Kindly specify the serial number i.e cancel#ticketserial e.g cancel#1562898925";



       }

  return result; 

  }

public static String getWithdrawalResponse(String Mobileno,String sms,String linkid){

//W#1500#7777
String response="";
String delimiterWithdraw = "#";
   if(sms.contains("#")){
     String[] tempWithdrawdetails;
    String Withdraw="";
    String Amount="";
    String Pin="";
    int balafter=0;
    tempWithdrawdetails= sms.split(delimiterWithdraw,3);
    for(int b =0; b < tempWithdrawdetails.length ; b++){
    Withdraw=tempWithdrawdetails[0];
    Amount=tempWithdrawdetails[1];
    Pin=tempWithdrawdetails[2];
    }if((getBalanceResponse(Mobileno)!=0 )&& (getBalanceResponse(Mobileno)>=(Integer.parseInt(Amount)))){

      
    int pin=Integer.parseInt(Pin);

   if(checkRegisteredDetails(Mobileno,pin)){//check pin availability
     balafter=getBalanceResponse(Mobileno)-(Integer.parseInt(Amount));

  String name=getTransName(Mobileno);

   String insert = "insert into Transaction_accnt(Name,msisdn,Withdraw,Balance) values ('" + name+ "',"+Mobileno+","+Integer.parseInt(Amount)+","+balafter+")";
     data.insert(insert);

 response="The Mpesa account will then be credited with "+Amount+" and your current KCS Wallet balance after Withdrawal is Ksh "+balafter+"/=";
    }else{ //wrong pin
     response="Invalid PIN.Kindly use the right PIN provided during registration and try again.";
    }
    }else{//insufficent bal
     response="Insufficient Amount in your Account to Withdraw Ksh "+Amount+" /= .Available balance is Ksh "+getBalanceResponse(Mobileno)+"/=";
  }
}else{ //invalid fomart
     response="Invalid sms .Kindly withdraw by sending sms i.e W#Amount#PIN e.g W#500#7777 ";
 }


    
 
  return response; 
  }
 public static int getSumDeposit(String msisdn){
  int bal=0 ;

        try{
            s = con.createStatement();
           // String details = s.execute(sql);
//mysql> select sum(mpesa_amt) from mpesa where mpesa_msisdn='254724267980' and status=0 and date(timestamp)='2015-05-11';

            rs = s.executeQuery("select sum(Deposit) as Deposit from Deposit_accnt where msisdn='"+msisdn+"' and status=0");
			
	rs.next();
	bal = rs.getInt("Deposit");
        }
        catch(Exception e){e.printStackTrace();}
        finally{

        }
        return bal;

}



  public static String getReportResponse(String Mobileno){
    int mypin=getRandomPin();
	
	int mywebpass=getRandomWebPass();
    String response="Your Username is "+Mobileno+". Your PIN is "+mypin+". Your Web Password is "+mywebpass +". To Top up use Paybill no. 292222";
 
  return response; 
  }


public static int counterhash(String sms ){
		int counterhash=0;

		for(int i=0;i<sms.length();i++){
			
			if(sms.charAt(i)=='#'){
				counterhash++;
			}
		}
		return counterhash;
		
	}
	public static int countercomma(String sms ){
		int countercomma=0;

		for(int i=0;i<sms.length();i++){
			
if(sms.charAt(i)==',' || sms.charAt(i)==';' || sms.charAt(i)==':'||sms.charAt(i)=='.'||sms.charAt(i)=='-'||sms.charAt(i)=='_'){
				countercomma++;
			}
		}
		return countercomma;
		
	}
public static String getNumberofbetts(String sms ){
    String[] tempkenno= sms.split("#",3);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
      for(int b =0; b < tempkenno.length ; b++){
        numofbetts=tempkenno[0];
	betts=tempkenno[1];
	gametype=tempkenno[2];

        }

		
		return numofbetts;
		

	
	}

public static String getSubLevel(String sms ){
    String[] tempkenno= sms.split("_",3);
    String Cor=null;
    String day=null;
    String level=null;
      for(int b =0; b < tempkenno.length ; b++){
        Cor=tempkenno[0];
	day=tempkenno[1];
	level=tempkenno[2];

        }

		
		return level;
		

	
	}

public static String getKennobetts2(String sms ){
     String[] tempkenno= sms.split("#",3);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
      for(int b =0; b < tempkenno.length ; b++){
        numofbetts=tempkenno[0];
	betts=tempkenno[1];
	gametype=tempkenno[2];

        }

      
		
		return betts;
		

	
	}
public static String getKennogametype2(String sms ){
     String[] tempkenno= sms.split("#",3);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
      for(int b =0; b < tempkenno.length ; b++){
        numofbetts=tempkenno[0];
	betts=tempkenno[1];
	gametype=tempkenno[2];

        }

		
		return gametype;
		

	
	}

public static String getValidsms(String sms ){
    
    String validsms=null;
    String betts=null;
    String str =sms.replaceAll(",|\\:|\\;|\\-|\\_|\\.",";");
StringBuffer sbfstr = new StringBuffer();
int shitcount=countercomma2(str);
String[] strArray =str.split(";",shitcount+1);

for(int b =0; b < strArray.length ; b++){
      if( Integer.parseInt(strArray[b])<10){

       sbfstr.append("0").append(strArray[b]).append(" ");
    }else{ 

            sbfstr.append(strArray[b]).append(" ");
      }
	

  }



String finals=sbfstr.toString().trim();
validsms=finals.replaceAll(" ",";");

		
return validsms;
		

	
}


public static int countercomma2(String sms ){
		int countercomma=0;

		for(int i=0;i<sms.length();i++){
			
                    if( sms.charAt(i)==';' ){
				countercomma++;
			}
		}
		return countercomma;
		
	}


public static String getKennobetts(String sms ){
    String[] tempkenno= sms.split("#",2);
    String gametype=null;
    String betts=null;
      for(int b =0; b < tempkenno.length ; b++){
        betts=tempkenno[0];
	gametype=tempkenno[1];

        }

		
		return betts;
		

	
	}
public static String getKennogame(String sms ){
    String[] tempkenno= sms.split("#",2);
    String gametype=null;
    String betts=null;
      for(int b =0; b < tempkenno.length ; b++){
        betts=tempkenno[0];
	gametype=tempkenno[1];

        }

		
		return gametype;
		

	
	}
/*Lotto*/

public static String getNumberofLottobetts(String sms ){
    String[] tempLotto= sms.split("#",3);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
      for(int b =0; b < tempLotto.length ; b++){
        numofbetts=tempLotto[0];
	betts=tempLotto[1];
	gametype=tempLotto[2];

        }

		
		return numofbetts;
		

	
	}

public static String getLottobetts2(String sms ){
     String[] tempLotto= sms.split("#",3);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
      for(int b =0; b < tempLotto.length ; b++){
        numofbetts=tempLotto[0];
	betts=tempLotto[1];
	gametype=tempLotto[2];

        }

      
		return betts;
		

	
	}
public static String getLottogametype2(String sms ){
     String[] tempLotto= sms.split("#",3);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
      for(int b =0; b < tempLotto.length ; b++){
        numofbetts=tempLotto[0];
	betts=tempLotto[1];
	gametype=tempLotto[2];

        }

		
		return gametype;
		

	
	}

public static String getLottogame(String sms ){
    String[] templotto= sms.split("#",2);
    String gametype=null;
    String betts=null;
      for(int b =0; b < templotto.length ; b++){
        gametype=templotto[1];
	betts=templotto[0];

        }

		
		return gametype;
		

	
	}

public static String getLottobetts(String sms ){
    String[] templotto= sms.split("#",2);
    String gametype=null;
    String betts=null;
      for(int b =0; b < templotto.length ; b++){
        gametype=templotto[1];
	betts=templotto[0];

        }

		
		return betts;
		

	
	

}


public static String getResponseForGi(String sms ){
    String[] tempGame= sms.split(":",2);
    String ok=null;
    String togitech=null;
      for(int b =0; b < tempGame.length ; b++){
        ok=tempGame[0];
	togitech=tempGame[1];

        }

		
	return togitech;
		

	
	

}

//lucky3

public static String getNumberofLucky3betts(String sms ){
    String[] tempLucky3= sms.split("#",4);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
	String costpergame=null;
      for(int b =0; b < tempLucky3.length ; b++){
       numofbetts=tempLucky3[0];
	   betts=tempLucky3[1];
	   gametype=tempLucky3[2];
       costpergame=tempLucky3[3];
        }

		
		return numofbetts;
		

	
	}
	
public static String getLucky3bettsManual(String sms ){
    String[] tempLucky3= sms.split("#",4);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
	String costpergame=null;
      for(int b =0; b < tempLucky3.length ; b++){
       numofbetts=tempLucky3[0];
	   betts=tempLucky3[1];
	   gametype=tempLucky3[2];
       costpergame=tempLucky3[3];
        }

		
		return betts;
		

	
	}
	
	
public static String getLucky3gameManual(String sms ){
    String[] tempLucky3= sms.split("#",4);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
	String costpergame=null;
      for(int b =0; b < tempLucky3.length ; b++){
       numofbetts=tempLucky3[0];
	   betts=tempLucky3[1];
	   gametype=tempLucky3[2];
       costpergame=tempLucky3[3];
        }

		
		return gametype;
		

	
	}
	
	public static String getLucky3Costpergame(String sms ){
    String[] tempLucky3= sms.split("#",4);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
	String costpergame=null;
      for(int b =0; b < tempLucky3.length ; b++){
       numofbetts=tempLucky3[0];
	   betts=tempLucky3[1];
	   gametype=tempLucky3[2];
       costpergame=tempLucky3[3];
        }

		
		return costpergame;
		

	
	}
	

public static String getNumberofLucky3bettsM2(String sms ){
    String[] tempLucky3= sms.split("#",3);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
      for(int b =0; b < tempLucky3.length ; b++){
        numofbetts=tempLucky3[0];
	betts=tempLucky3[1];
	gametype=tempLucky3[2];

        }

		
		return numofbetts;
		

	
	}

public static String getLucky3betts2(String sms ){
     String[] tempLucky3= sms.split("#",3);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
      for(int b =0; b < tempLucky3.length ; b++){
        numofbetts=tempLucky3[0];
	betts=tempLucky3[1];
	gametype=tempLucky3[2];

        }

      
		return betts;
		

	
	}
public static String getLucky3gametype2(String sms ){
     String[] tempLucky3= sms.split("#",3);
    String gametype=null;
    String betts=null;
    String numofbetts=null;
      for(int b =0; b < tempLucky3.length ; b++){
        numofbetts=tempLucky3[0];
	betts=tempLucky3[1];
	gametype=tempLucky3[2];

        }

		
		return gametype;
		

	
	}
//sendsms(id,msisdn,acceptresp,linkid);
public static void sendsms(int id , String mobile, String message,String linkid)throws Exception
    {

      System.out.println("SMS SEND Start  : ");
      
      String msg = message;
	  
          try{
        System.out.println("The length of message is " + msg.length());
         log.log(Level.INFO, msg);
 
        // msg= URLEncoder.encode(msg.toString(), "UTF-8");
       }
       catch (Exception uee) {
         System.err.println(uee);
       }
/*
             String spid = "601469";
	     String serviceid = "6014692000068706";
	     String accesscode ="29099";

*/
              String spid = null;
             String serviceid = null;
             String destination = null;
             String  accesscode = null;
             
           if(message.startsWith("Thank") || message.startsWith("Go")) {
		 spid = "601470";
              serviceid = "6014702000098263";
              accesscode = "706804";
			 
		}
      else{

             spid = "601470";
             serviceid = "6014702000022206";
             accesscode = "704308";
              }


              destination = mobile;




         
             String smic="";
             if ((destination.startsWith("25472")) || (destination.startsWith("25471")) || (destination.startsWith("25470")))
              {
          smic = "SAFTX";

              }
             else if ((destination.startsWith("25477")) || (destination.startsWith("077"))) {
              smic = "ORTRX";
             }
            else if ((destination.startsWith("25473")) || (destination.startsWith("25478"))) {
             smic = "CELTX";
           
             }      
  
      sendSms(createHeader(destination,spid, serviceid), createBody(destination, message, accesscode));

           String insert = "insert into outbound (msgid,msisdn,message,status) values ("+id+"," + destination + ",'" + message + "'," +1+ ")";
             data.insert(insert);
      
    }
  
    public static RequestSOAPHeaderE createHeader(String address,String spid, String serviceid)
    {
      RequestSOAPHeaderE requestHeaderE = new RequestSOAPHeaderE();
      RequestSOAPHeader requestHeader = new RequestSOAPHeader();
  
      String spPassword = "Prs2016#";
  
      String spId = spid;
  
      String serviceId = serviceid;
  
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
      String created = sdf.format(Calendar.getInstance().getTime());
      String password = NonceGenerator.getInstance().MD5Gen(spId + spPassword + created);
  
      requestHeader.setSpId(spId);
  
      requestHeader.setSpPassword(password);
      requestHeader.setServiceId(serviceId);
  
      
  
      requestHeader.setTimeStamp(created);
    /* if(linkid.length()>2){
            
            requestHeader.setLinkid(linkid);
            requestHeader.setOA(address);
            requestHeader.setFA(address);
        }else{
            
        }
 */
      requestHeaderE.setRequestSOAPHeader(requestHeader);
      return requestHeaderE;
    }
  
     public static  SendSmsServiceStub.SendSmsE createBody(String mobile, String message, String code)
    {
      try
      {
        System.out.println("message: " + message);
        System.out.println("address: " + mobile);
  
         URI address = new URI("tel:" + mobile);
  
               URI endpoint = new URI("http://197.248.31.217:8084/Notification/SmsNotificationService");
  
        SendSmsServiceStub.ChargingInformation charging = new SendSmsServiceStub.ChargingInformation();
        charging.setAmount(new BigDecimal(1));
        charging.setCode("111");
        charging.setCurrency("KES");
        charging.setDescription("description");
        SendSmsServiceStub.SimpleReference sim = new SendSmsServiceStub.SimpleReference();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        String created = sdf.format(Calendar.getInstance().getTime());
        sim.setCorrelator(created);
       // this.correlator = created;
  
        sim.setEndpoint(endpoint);
        sim.setInterfaceName("SmsNotification");
        SendSmsServiceStub.SendSms param = new SendSmsServiceStub.SendSms();
   
        param.addAddresses(address);
  
        param.setMessage(message);
        param.setReceiptRequest(sim);
        param.setSenderName(code);
  
        SendSmsServiceStub.SendSmsE sendSms = new SendSmsServiceStub.SendSmsE();
        sendSms.setSendSms(param);
        return sendSms; } catch (URI.MalformedURIException e) {
      }
      return null;
    }
  
    public static void sendSms(RequestSOAPHeaderE header, SendSmsServiceStub.SendSmsE body)
    {
      try
      {
        SendSmsServiceStub stub = new SendSmsServiceStub("http://196.201.216.13:8310/SendSmsService/services/SendSms");
   //SendSmsServiceStub stub = new SendSmsServiceStub("http://127.0.0.1:8030/SendSmsService/services/SendSms");
  //254731329177
        stub._getServiceClient().addHeader(header.getOMElement(RequestSOAPHeaderE.MY_QNAME, OMAbstractFactory.getSOAP11Factory()));
  
       SendSmsResponseE response = stub.sendSms(body);
      }
      catch (AxisFault e)
      {
        SendSmsServiceStub.SendSmsResponseE response;
        e.printStackTrace();
      }
      catch (RemoteException e) {
        e.printStackTrace();
      }
      catch (PolicyException e) {
        e.printStackTrace();
      }
      catch (ServiceException e) {
        e.printStackTrace();
     }
      catch (ADBException e) {
        e.printStackTrace();
      }
    }


	
}

