package com.nspl.app.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Bia2FunctionsService {


	@Inject
	PropertiesUtilService propertiesUtilService;

	private final Logger log = LoggerFactory.getLogger(Bia2FunctionsService.class);

	public String callFunction(String funcName, String param)
	{
		log.info("While calling the function to get formula based values");
		if(funcName.equalsIgnoreCase("reportDetails"))
		{
			return reportDetails(param);
		}
		else if(funcName.equalsIgnoreCase("decodeDate"))
		{
			return decodeDate(param);
		}
		else if(funcName.equalsIgnoreCase("decodeToSubString"))
		{
			return decodeToSubString(param);
		}
		else if(funcName.equalsIgnoreCase("decodeToSubStringReturningWithColon"))
		{
			return decodeToSubStringReturningWithColon(param);
		}
		else if(funcName.equalsIgnoreCase("decodeToReplacingString"))
		{
			return decodeToReplacingString(param);
		}
		else if(funcName.equalsIgnoreCase("translateAndDecode"))
		{
			BigDecimal val=translateAndDecode(param);
			String valStr=val.toString();
			return valStr;
		}
		else if(funcName.equalsIgnoreCase("returnDecodeValue"))
		{
			return returnDecodeValue(param);
		}

		return null;
	}

	/**
	 * Method to find the Report Details
	 * */
	public String reportDetails(String line)
	{
		//log.info("To find the Report Details");
		String result = "Not Valid";
		if(line.length()>0)
		{
			if((!line.substring(0, 2).equalsIgnoreCase("DF")) && line.substring(43, 45).equals("11"))
			{
				result = "ROC";
				return result;
			}
			else if((!line.substring(0, 2).equalsIgnoreCase("DF")) && line.substring(43, 45).equals("20"))
			{
				result = "CBK";
				return result;
			}
			else
				return result;
		}
		//log.info("result:- "+result);
		return result;
	}



	//TO_CHAR(TO_DATE(SUBSTR(:FIELD_08,1,4)||'0101','YYYYMMDD')+SUBSTR(:FIELD_08,5,3)-1,'YYYYMMDD')
	public String decodeDate(String date)
	{
		//log.info("Decoding the date format");

		Date d;
		String new_date=null;
		try {
			d = getDateFromJulian7(date);
		
		System.out.println(d);
		String sd=getJulian7FromDate(d);
		System.out.println(sd);


		final String OLD_FORMAT = "yyyy-MM-dd";  //wants t convert date in this format      

		SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");    
		Date d1;
	
			d1 = newDateFormat.parse(newDateFormat.format(d));
			new_date=newDateFormat.format(d1);
			System.out.println("new date format"+new_date);
			newDateFormat.applyPattern(OLD_FORMAT);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		newDateFormat.applyPattern(OLD_FORMAT);
//		String new_date=newDateFormat.format(d1);
//		System.out.println("new date format"+new_date);
		return new_date;

	}


	public static Date getDateFromJulian7(String julianDate)
			throws ParseException
	{
		return new SimpleDateFormat("yyyyD").parse(julianDate);
	}

	public static String getJulian7FromDate(Date date) {
		StringBuilder sb = new StringBuilder();
		Calendar cal  = Calendar.getInstance();
		cal.setTime(date);

		return  sb.append(cal.get(Calendar.YEAR))
				.append(String.format("%03d", cal.get(Calendar.DAY_OF_YEAR)))
				.toString();
	}






	/**SubString**/
	//Not using
	public String decodeSub(String value,int i,int j,int k,int l)
	{

		//log.info("subString input value :"+value);
		String value1="";
		String value2="";
		if(j>value.length())
			value1=value.substring(i, value.length());
		else
			value1=value.substring(i, j);
		if(l>value.length())
			value2=value.substring(k, value.length());
		else
			value2=value.substring(k, l);
		//log.info("value1 :"+value1);
		//log.info("value2 :"+value2);
		String value3=value1+value2;
		//log.info("value after appending :"+value3);
		return value3;


	}

	/**SubString no need of params other than string**/

	/**SUBSTR(:FIELD_02,5,4)||SUBSTR(:FIELD_02,1,4)**/

	public String decodeToSubString(String value)
	{

		//log.info("subString input value :"+value);
		String value1="";
		String value2="";

		if(9>value.length())
			value1=value.substring(4, value.length());
		else
			value1=value.substring(4, 8);
		if(4>value.length())
			value2=value.substring(0, value.length());
		else
			value2=value.substring(0, 4);
		//log.info("value1 :"+value1);
		//log.info("value2 :"+value2);
		String value3=value1+value2;
		//log.info("value after appending :"+value3);
		return value3;


	}


	/**SUBSTR(:FIELD_03,1,2)||':'||SUBSTR(:FIELD_03,3,2)**/
	public String decodeToSubStringReturningWithColon(String value)
	{

		//log.info("subString input value :"+value);
		String value1="";
		String value2="";

		if(2>value.length())
			value1=value.substring(1, value.length());
		else
			value1=value.substring(0, 2);
		if(4>value.length())
			value2=value.substring(2, value.length());
		else
			value2=value.substring(2, 4);
		//log.info("value1 :"+value1);
		//log.info("value2 :"+value2);
		String value3=value1+":"+value2;
		//log.info("value after appending with colon:"+value3);
		return value3;


	}

	/**REPLACE(:FIELD_13,SUBSTR(:FIELD_13,7,LENGTH(:FIELD_13)-10),LPAD('X',LENGTH(:FIELD_13)-10,'X'))**/
	public String decodeToReplacingString(String value)
	{

		//log.info("replacing inputvalue and appending X :"+value);
		int startIndex=6;
		int endIndex=startIndex+value.length()-10;
		List<String> string=new ArrayList<String>();
		for(int i=0;i<value.length()-10;i++)
		{
			String str="X";
			string.add(str);
		}
		String append=string.toString().replace(",", "").replace("[", "").replace("]", "").replaceAll("\\s","");
		//log.info("string :"+string);
		//log.info("endIndex :"+endIndex);
		String replaceString=value.replace(value.substring(startIndex,endIndex), append); 
		//log.info("value after appending :"+replaceString);
		return replaceString;


	}



	/**TRANSLATE(:FIELD_09,'ABCDEFGHI{JKLMNOPQR}','12345678901234567890') /100 * DECODE(SIGN(TRANSLATE(SUBSTR(:FIELD_09,-1,1),'ABCDEFGHI{JKLMNOPQR}','11111111110000000000')),0,-1,1)**/
	public BigDecimal translateAndDecode(String value)
	{

		//log.info("TranslateAndDecode input value :"+value);
		/**TRANSLATE(:FIELD_11,'ABCDEFGHI{JKLMNOPQR}','12345678901234567890') /100 **/
		String trasValue=value
				.replace("A","1")
				.replace("B","2")
				.replace("C","3")
				.replace("D","4")
				.replace("E","5")
				.replace("F","6")
				.replace("G","7")
				.replace("H","8")
				.replace("I","9")
				.replace("{","0")
				.replace("J","1")
				.replace("K","2")
				.replace("L","3")
				.replace("M","4")
				.replace("N","5")
				.replace("O","6")
				.replace("P","7")
				.replace("Q","8")
				.replace("R","9")
				.replace("}","0");

		BigDecimal big = new BigDecimal(trasValue);
		BigDecimal div = new BigDecimal(100);

		BigDecimal finalTrasValue=big.divide(div);
		//log.info("finalTrasValue :"+finalTrasValue);


		/**DECODE(SIGN(TRANSLATE(SUBSTR(:FIELD_11,-1,1),'ABCDEFGHI{JKLMNOPQR}','11111111110000000000')),0,-1,1)**/

		char character=value.charAt(value.length()-1);
		String subString=Character.toString(character );

		String translateDec=subString
				.replace("A","1")
				.replace("B","1")
				.replace("C","1")
				.replace("D","1")
				.replace("E","1")
				.replace("F","1")
				.replace("G","1")
				.replace("H","1")
				.replace("I","1")
				.replace("{","1")
				.replace("J","0")
				.replace("K","0")
				.replace("L","0")
				.replace("M","0")
				.replace("N","0")
				.replace("O","0")
				.replace("P","0")
				.replace("Q","0")
				.replace("R","0")
				.replace("}","0");

		BigDecimal transToDouble = new BigDecimal(translateDec);
		/*BigDecimal div1 = new BigDecimal(100);


		Double transToDouble=Double.parseDouble(translateDec);*/

		/**Finding sign**/
		int sign = 0;
		//if(transToDouble>0)
		if(transToDouble.compareTo(BigDecimal.ZERO)>0)
			sign=1;
		else if(transToDouble.compareTo(BigDecimal.ZERO)==0)
			sign=0;
		else if(transToDouble.compareTo(BigDecimal.ZERO)<0)
			sign=-1;

		/**Decode**/
		int decodedVaue;
		if(sign==0)
			decodedVaue=-1;
		else
			decodedVaue=1;



		BigDecimal finalValue=	finalTrasValue.multiply(new BigDecimal(decodedVaue));
		//log.info("**finalValue** :"+finalValue);
		return finalValue;



	}


	/**DECODE(:FIELD_24,'',NULL,:FIELD_24||'-'||DECODE(:FIELD_24,'P','CPC/Corporate Purchasing Card','UNKNOWN'))**/
	public String returnDecodeValue(String value)
	{
		//log.info("returnDecodeValue value :"+value);
		String firstDecValue;
		String finalDecValue;
		if(value.isEmpty())
			firstDecValue="NULL";
		else
			firstDecValue=value;

		if(!firstDecValue.equalsIgnoreCase("NULL"))
		{
			String secDecValue;
			if(value.equalsIgnoreCase("A"))
				secDecValue="ERROR CODE";
			else if(value.equalsIgnoreCase("N"))
				secDecValue="NOT COMPLY WITH AMEX STANDARD";
			else if(value.equalsIgnoreCase("P"))
				secDecValue="CPC/Corporate Purchasing Card";
			else if(value.equalsIgnoreCase("C"))
				secDecValue="POS DATA CODE";
			else if(value.equalsIgnoreCase("H"))
				secDecValue="AUTHORIZATION CODE";
			else if(value.equalsIgnoreCase("Z"))
				secDecValue="ADJ APP-IN CODE";
			else if(value.equalsIgnoreCase("40"))
				secDecValue="ASSETS BILLING";
			else if(value.equalsIgnoreCase("41"))
				secDecValue="TAKE-ONE COMMISSIONS";
			else if(value.equalsIgnoreCase("50"))
				secDecValue="OTHER FEES";
			else
				secDecValue="UNKNOWN";

			finalDecValue= firstDecValue+"-"+secDecValue;
		}
		else
			finalDecValue=firstDecValue;

		return finalDecValue;
	}

	/**
	 * TRANSLATE(:FIELD_11,'ABCDEFGHI{JKLMNOPQR}','12345678901234567890') 
	 *DECODE(SIGN(TRANSLATE(SUBSTR(:FIELD_11,-1,1),'ABCDEFGHI{JKLMNOPQR}','11111111110000000000')),0,-1,1)
	 * @param value
	 * @return
	 */
	public static BigDecimal translateAndDecodeFun(String value)
	{
		System.out.println("TranslateAndDecode input value :"+value);
		/**TRANSLATE(:FIELD_11,'ABCDEFGHI{JKLMNOPQR}','12345678901234567890') /100 **/
		String trasValue=value
				.replace("A","1")
				.replace("B","2")
				.replace("C","3")
				.replace("D","4")
				.replace("E","5")
				.replace("F","6")
				.replace("G","7")
				.replace("H","8")
				.replace("I","9")
				.replace("{","0")
				.replace("J","1")
				.replace("K","2")
				.replace("L","3")
				.replace("M","4")
				.replace("N","5")
				.replace("O","6")
				.replace("P","7")
				.replace("Q","8")
				.replace("R","9")
				.replace("}","0");

		BigDecimal big = new BigDecimal(trasValue);
		BigDecimal div = new BigDecimal(1);

		BigDecimal finalTrasValue=big.divide(div);
		System.out.println("finalTrasValue :"+finalTrasValue);


		/**DECODE(SIGN(TRANSLATE(SUBSTR(:FIELD_11,-1,1),'ABCDEFGHI{JKLMNOPQR}','11111111110000000000')),0,-1,1)**/

		char character=value.charAt(value.length()-1);
		String subString=Character.toString(character );

		String translateDec=subString
				.replace("A","1")
				.replace("B","1")
				.replace("C","1")
				.replace("D","1")
				.replace("E","1")
				.replace("F","1")
				.replace("G","1")
				.replace("H","1")
				.replace("I","1")
				.replace("{","1")
				.replace("J","0")
				.replace("K","0")
				.replace("L","0")
				.replace("M","0")
				.replace("N","0")
				.replace("O","0")
				.replace("P","0")
				.replace("Q","0")
				.replace("R","0")
				.replace("}","0");

		BigDecimal transToDouble = new BigDecimal(translateDec);
		/*BigDecimal div1 = new BigDecimal(100);


		Double transToDouble=Double.parseDouble(translateDec);*/

		/**Finding sign**/
		int sign = 0;
		//if(transToDouble>0)
		if(transToDouble.compareTo(BigDecimal.ZERO)>0)
			sign=1;
		else if(transToDouble.compareTo(BigDecimal.ZERO)==0)
			sign=0;
		else if(transToDouble.compareTo(BigDecimal.ZERO)<0)
			sign=-1;

		/**Decode**/
		int decodedVaue;
		if(sign==0)
			decodedVaue=-1;
		else
			decodedVaue=1;

		BigDecimal finalValue=	finalTrasValue.multiply(new BigDecimal(decodedVaue));
		System.out.println("**finalValue** :"+finalValue);
		return finalValue;
	}

}
