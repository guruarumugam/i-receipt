package google.proj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;



public class OCR {

	private String ocr_return[]=null;
	private String ocr_res;
	private String filepath;
	private String username;
	private String password;
	return_entry[] total;
	IDate[] date=null;
	return_entry[] store_name=null;

	public OCR(String filePath,String username,String password) {
		this.filepath=new String(filePath);
		this.username=new String(username);
		this.password=new String(password);
	}

	/*******************************************/
	/* return codes:
	/*   return = 1, OK
	/*   return = 2, Authentication Error
	/*   return = 3, Server Error
	 *   return = 4  Unknown Error
	 */
	/*******************************************/
	public int preformOCR() throws IOException, FileNotFoundException{	
		File file=new File(filepath);
		if (!file.canRead()){
			throw  (new FileNotFoundException());}
		ocr_res=Singers_Solution(file);
		if (ocr_res==null)
			return 4;
		if (ocr_res.equals("Authentication Error"))
			return 2;
		if (ocr_res.contains("<html><head>)"))
			return 3;
		this.ocr_return = Convert_to_arr(ocr_res);
		return 1;
	}


	//this method gets a file path to a picture file.
	//the file can be **JPG ONLY** and not bigger then 1MB.
	//the function returns the OCR output of the file.
	//if file not found or a server side error null will be returned
	//in case of a server side error the folowing may be returner:
	//"bytearray reading Error"
	//"Authentication Error"
	//"Upload File to Gdocs Error"
	//"Download file from Gdocs Error"
	//html page containing any other server error will start with "<html><head>"
	private static String Singers_Solution(File file){
		//set the http connection parameters
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, 100000);
		HttpConnectionParams.setSoTimeout(params, 100000);
		HttpClient client = new DefaultHttpClient(params);

		//http post address to my app engine account
		HttpPost request = new HttpPost("http://assaf86.appspot.com/irec_server1");
		byte[] bfile;
		try {

			//convert the file into byte array and add to the request 
			bfile = getBytesFromFile(file);
			ByteArrayEntity req_entity = new ByteArrayEntity(bfile);
			request.setEntity(req_entity);

			//send the reauest
			HttpResponse response = client.execute(request);
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String result = sb.toString();
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return result;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}

	//this function converts the file to a byte array
	private static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int)length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}




	public String[] getStringsOfWords() {
		return ocr_return;
	}

	private static int How_many_words(String str) {
		int sum = 0;
		char c;
		boolean t = true;
		for (int i = 0; i < str.length(); i++) {

			c = str.charAt(i);
			if (c != '\t' && c != '\n' && c != ' '&&c!= '\r') {
				if (t) {
					sum++;
				}
				t = false;
			} else {
				t = true;
			}
		}
		return sum;
	}

	private static String[] Convert_to_arr(String src) {
		int n = How_many_words(src);
		String str_arr[] = new String[n];
		String ret_arr[] = new String[n - 35];
		int j = 0;
		char c;
		boolean t = false;
		for (j = 0; j < n; j++) {
			str_arr[j] = "";
		}
		j = 0;
		for (int i = 0; i < src.length(); i++) {
			c = src.charAt(i);
			// check if it is char
			if (c != '\t' && c != '\n' && c != ' '&& c!='\r') {
				str_arr[j] = str_arr[j] + Character.toString(c).toLowerCase();
				t = true;

			} else { // the char is \n or \t or ' '
				if (t) {
					j++;
				}
				t = false;
			}
		}
		j = 0;
		for (int i = 35; i < n; i++) {
			ret_arr[j] = str_arr[i];
			j++;
		}
		return ret_arr;
		
	}
	
	public return_entry[] getStoreName(){
		if (this.store_name!=null)
			return this.store_name;
		return_entry ret[]=new return_entry[4];
		for (int i = 0; i < ret.length; i++)
			ret[i]=new return_entry();
	    if (ocr_return==null){
	    	this.store_name=ret;
			return ret;
	    }
	    if(ocr_return.length==1){
	    	ret[0].entry=ocr_return[0];
	    	ret[0].priority=1;
	    }else{
	    	if (ocr_return.length==2){
	    	    ret[0].entry=ocr_return[0]+" "+ocr_return[1];
	    	    ret[0].priority=1;
	    	    ret[1].entry=ocr_return[0];
	    	    ret[1].priority=1;
	    	}else{
	    		if (ocr_return.length==3){
		    	    ret[0].entry=ocr_return[0]+" "+ocr_return[1];
		    	    ret[0].priority=1;
		    	    ret[1].entry=ocr_return[0]+" "+ocr_return[1]+" "+ocr_return[2];
		    	    ret[1].priority=1;
		    	    ret[2].entry=ocr_return[0];
		    	    ret[2].priority=1;
		    	    
	    		}else{
	    			if(ocr_return.length>3){
			    	    ret[0].entry=ocr_return[0]+" "+ocr_return[1];
			    	    ret[0].priority=1;
			    	    ret[1].entry=ocr_return[0]+" "+ocr_return[1]+" "+ocr_return[2];
			    	    ret[1].priority=1;
			    	    ret[2].entry=ocr_return[0];
			    	    ret[2].priority=1;
			    	    ret[3].entry=ocr_return[0]+" "+ocr_return[1]+" "+ocr_return[2]+" "+ocr_return[3];
			    	    ret[3].priority=1;
	    			}
	    		}		
	    	}
	    }	    
    	this.store_name=ret;
		return ret;    
	}
	
	
	public return_entry[] get_total(){
		if (this.total==null)
			this.total=get_totalr();
		return this.total;
		
	}
	//remove the static. it was added for test purpuses only
	public return_entry[] get_totalr(){
		return_entry ret[]=new return_entry[4];
		for (int i = 0; i < ret.length; i++)
			ret[i]=new return_entry();
	    if (ocr_return==null)
			return ret;
		String total_words[]={"credit","sum","cash","subtotal","amount","total"};
		return_entry sum;
		int priority;
		boolean exist=false;
		for (int i = 0; i < ocr_return.length-1; i++) {
			if ((priority=possible_match(total_words,i))!=-1)
				if((sum=find_amount(priority,i)) != null){//as far as you go the less likely the resault is
					exist=false;
					for (int j = 0; j < ret.length; j++) {
						if ((ret[j].getEntry()!=null)&&(ret[j].getEntry().equals(sum.entry)))
							exist=true;
					}
					if(!exist)
						add_to_ret(ret,sum);
		}
		}
		//fill_last_resort()
			int lastindex=ocr_return.length;
			return_entry ret1=null;
			for (int i = 0; i < ret.length; i++) {
				if (ret[i].getEntry()==null){
					while ((--lastindex>=0))
						if ((ret1=is_price(ocr_return[lastindex], 1))!=null)
							break;
					
							
				if (lastindex>=0){
					exist=false;
					for (int j = 0; j < ret.length; j++) {
						if ((ret[j].getEntry()!=null)&&(ret[j].getEntry().equals(ret1.entry)))
						exist=true;
				}
				if(!exist)
					ret[i]=new return_entry(1, ret1.entry);
				}
			}
			}
		return ret;

	}
	//invarient ret is orginized s.t lowest priority is first
	//array transferd by value.. watch this
	private void add_to_ret(return_entry[] ret, return_entry sum) {
		int min_pr=0;
		int min_pr_idx=0;
		for (int i = 0; i < ret.length; i++) {
			if(ret[i].getPriority()==0){
				ret[i].setEntry(sum.getEntry());
				ret[i].setPriority(sum.getPriority());
				return;
			}
			else{
				min_pr=ret[i].getPriority();
				min_pr_idx=i;
			}
		}
		if (min_pr<sum.getPriority()){
			ret[min_pr_idx].setEntry(sum.getEntry());
			ret[min_pr_idx].setPriority(sum.getPriority());		
		}	
	}

	//look for amount. ignore the $ sign. remember to deal with .29
	private return_entry find_amount(int priority, int i) {
		int ret_priority=-1;
		return_entry ret;
		String ret_price=null;
		for (int j = i+1; j < this.ocr_return.length; j++) {
			ret=is_price(this.ocr_return[j],priority);//chack if it is price and get the right format
			if (ret==null)
				continue;
			ret.setPriority(ret.getPriority()+20/(j-i));//if the price is found closer to the word the score is higher
			if(ret.getPriority()>ret_priority){
				ret_price=ret.getEntry();
				ret_priority=ret.getPriority();
			}
		}
		if (ret_priority!=-1)
			return new return_entry(ret_priority, ret_price);
		return null;			
	}
	


	private return_entry is_price(String string, int priority) {
		int score=0;
		String workspace="";
		int sep_idx=-1;
		char w;
		boolean currency=false;
		char currency_signes[]={'$','£','€'};//add more
		char ignore_tokens[]={'-'};
		char seperator='.';
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i)==seperator){
				sep_idx=i;
				workspace=workspace+seperator;
				continue;
			}
			for (int j = 0; j < currency_signes.length; j++) {
				if (string.charAt(i)==currency_signes[j])
					currency=true;
				continue;
			}
			for (int j = 0; j < ignore_tokens.length; j++) {
				if (string.charAt(i)==ignore_tokens[j])
					continue;
			}
			if ((w=convert_common_errors_back_2numbers(string.charAt(i)))!=0)
				workspace=workspace+w;
			else
				return null;

		}
		if (currency)//corrency sign adds 7 to the score;
			score=7;
		if (sep_idx==-1)
			return null;
		else
			return new return_entry(score+priority,workspace);
	}

	private char convert_common_errors_back_2numbers(char w) {
		switch (w) {
		case '!':  return 1;
		case 'I':  return 1;
		case 'i':  return 1;
		case 'B':  return 8;
		case 'o':  return 0;
		case 'O':  return 0;
		case 'S':  return 5;
		case 's':  return 5;
		}
		if ((w>=48)&&(w<=57))
			return w;
		else
			return 0;
	}

	private int possible_match(String[] total_words,int i) {
		for (int j = 0; j < total_words.length; j++) {
			if (allmost(total_words[j],this.ocr_return[i]))
				return j;
		}
		return -1;
	}


	//get char and convert it to int
	private static int right_char(char c) {

		if (c > 47 && c < 58)
			return c - 48;
		if (c == 'o' || c == 'O')
			return 0;
		if (c=='!')//<---------------------------------
			return 'l';
		if (c == 's' || c == 'S')
			return 5;
		if (c == 'i' || c == 'I')
			return 1;
		if (c == 'g')
			return 6;
		if (c == 'q')
			return 9;
		if (c == 'B')
			return 8;
		return -1;
	}

	private static boolean allmost(String src, String trg) {
		String sub = "";
		int index;
		for (int i = 0; i < trg.length() - 1; i++) {
			sub = trg.substring(i, i + 2);// in sub is the sube string of src in
			// place i and i+1;
			index = sub_string(src, sub); // get the index of sub in src
			if (index == -1)
				continue; // sub is not sub string in trg
			else { // sub is sub string in trg starts in index
				if (checkbefore(src, trg.substring(0, i), 0, index - 1)
						&& checkafter(src, trg.substring(i, trg.length()),
								index, src.length() - 1)) {
					return true;

				}

			}

		}

		return false;
	}

	// check if before the sub string it matches
	private static boolean checkbefore(String src, String trg, int start, int end) {
		int sum = 0;
		int t = 0;
		for (int i = 0; i <= end; i++) {// check from place i
			t = i;
			for (int j = 0; j < trg.length(); j++) { // check sub string of trg
				if (t <= end) {
					if (trg.charAt(j) == src.charAt(t)) {
						sum++;
					}
					t++;
				} else
					break;

			}
			if (sum + 1 >= end + 1)// there is less than one difference so it is
				// equal
				return true;

			sum = 0;
		}
		return false;
	}

	// check if after the sub string it matches
	private static boolean checkafter(String src, String trg, int start, int end) {
		int sum = 0;
		int t = 0;
		if (start + 2 >= src.length() - 1)
			return true;
		for (int i = start + 2; i <= end; i++) {
			t = i;
			for (int j = 0; j < trg.length(); j++) {
				if (t < src.length()) {
					if (src.charAt(t) == trg.charAt(j)) {
						t++;
						sum++;
					}
				} else
					break;
			}
			if (sum + 1 >= end - start - 1) {
				return true;
			}
			sum = 0;
		}
		return false;
	}

	// return the first place of the char iff sub is sub string in src and 0
	// other only for 2 char
	private static int sub_string(String src, String sub) {
		for (int i = 0; i < src.length() - 1; i++) {
			if (src.charAt(i) == sub.charAt(0)
					&& src.charAt(i + 1) == sub.charAt(1))
				return i;
		}
		return -1;
	}

	
	//**********************************************************************//
	
	
	
	// check if the date is type of month/day/year
	private static boolean date_typeA(String str) {

		int index = 0;
		int index2 = 0;
		index = str.indexOf("/");

		if (index == -1 || index == str.length() - 1)
			return false;

		index2 = str.indexOf("/", index + 1);

		if (index2 == -1 || index2 >= str.length() - 2)
			return false;
		return true;
	}

	// check if in str is more that 2 char from type A-Z
	private static boolean month_type(String str) {

		str = str.toUpperCase();
		int sum = 0;
		if (str.length() < 2 || str.length() > 8)
			return false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) > 64 && str.charAt(i) < 91)
				sum++;
			if (sum > 1)// more than 2 char in str
				return true;
		}
		return false;
	}

	// check if the date is type of month-day-year
	private static boolean date_typeB(String str) {// date is type of
													// month-day-year

		int index = 0;
		int index2 = 0;
		String s;
		index = str.indexOf("-");

		if (index == -1 || index == str.length() - 1)
			return false;

		index2 = str.indexOf("-", index + 1);

		if (index2 == -1 || index2 >= str.length() - 2)
			return false;
		// so far the string include "-" "-"
		s = str.substring(index, index2 + 1); // get the string between / /

		if (!month_type(s))
			return true;
		return false;

	}

	// date is type of 12-NOV-10
	private static boolean date_typeC(String str) {

		int index = 0;
		int index2 = 0;
		String s;
		index = str.indexOf("-");

		if (index == -1 || index == str.length() - 1)
			return false;

		index2 = str.indexOf("-", index + 1);

		if (index2 == -1 || index2 >= str.length() - 2)
			return false;
		// so far the string include "-" "-"
		s = str.substring(index, index2 + 1); // get the string between / /

		if (month_type(s))
			return true;
		return false;

	}
	
	public IDate[] getDate(){
		if (this.date==null)
			this.date=getDatehelp();
		return this.date;
		
	}
	
	private IDate[] getDatehelp() {
		IDate ret[] = new IDate[4];
		int p;
		for (p = 0; p < 4; p++) {
			ret[p] = null;
		}
		int months[] = new int[5];
		int t;
		int t2;
		int year = 0;
		int day = 0;
		String year_s;
		String month_s;
		String day_s;
		for (int i = 0; i < ocr_return.length; i++) {
			if (date_typeA(ocr_return[i])) { // date is type of month/day/year
				t = ocr_return[i].indexOf('/');
				t2 = ocr_return[i].indexOf('/', t + 1);
				month_s = ocr_return[i].substring(0, t);// get the month from the
													// string
				day_s = ocr_return[i].substring(t + 1, t2);// get the day from the
													// string
				year_s = ocr_return[i].substring(t2 + 1, ocr_return[i].length());// get the
																	// year from
																	// the
																	// string
				year = is_aYear(year_s);
				day = is_aDay(day_s);
				if (year != -1 && day != -1 && is_aMonth(month_s, months)) { // succeed
																				// to
																				// get
																				// the
																				// date
					for (int j = 0; j < months[0]; j++) {
						ret[j] = ret[j] = new IDate(year,months[1], day);
					}
					//return ret;
				}
			}
			if (date_typeB(ocr_return[i])) { // date is type of month-day-year
				t = ocr_return[i].indexOf('-');
				t2 = ocr_return[i].indexOf('-', t + 1);
				month_s = ocr_return[i].substring(0, t);// get the month from the
													// string
				day_s = ocr_return[i].substring(t + 1, t2);// get the day from the
													// string
				year_s = ocr_return[i].substring(t2 + 1, ocr_return[i].length());// get the
																	// year from
																	// the
																	// string
				year = is_aYear(year_s);
				day = is_aDay(day_s);
				if (year != -1 && day != -1 && is_aMonth(month_s, months)) { // succeed
																				// to
																				// get
																				// the
																				// date
					for (int j = 0; j < months[0]; j++) {
						ret[j] = ret[j] = new IDate(year,months[1], day);
					}
					//return ret;
				}
			}
			if (date_typeC(ocr_return[i])) { // date is type of 12-NOV-10
				t = ocr_return[i].indexOf('-');
				t2 = ocr_return[i].indexOf('-', t + 1);
				day_s = ocr_return[i].substring(0, t); // get the day from the string
				month_s = ocr_return[i].substring(t + 1, t2); // get the month from the
														// string
				year_s = ocr_return[i].substring(t2 + 1, ocr_return[i].length());// get the
																	// year from
																	// the
																	// string
				year = is_aYear(year_s);
				day = is_aDay(day_s);
				if (year != -1 && day != -1 && is_aMonth(month_s, months)) { // succeed
																				// to
																				// get
																				// the
																				// date
					for (int j = 0; j < months[0]; j++) {
						ret[j] = ret[j] = new IDate(year,months[1], day);
					}
					//return ret;
				}
			}
			year = 0;
			day = 0;
			for (p = 0; p < 5; p++) {
				months[p] = 0;
			}
		}

		return ret;
	}

	private static int is_aYear(String year) {

		int sum = 0;
		int tmp;
		if (year.length() != 2 && year.length() != 4)
			return -1;
		for (int i = 0; i < year.length(); i++) {
			tmp = right_char(year.charAt(i));
			if (tmp == -1)
				return -1;
			else
				sum = sum * 10 + tmp;
		}
		if (sum >= 0 && sum <= 20) {
			return sum + 2000;
		}
		if (sum >= 2000 && sum <= 2020)
			return sum;
		return -1;
	}
	
	private static int is_help(String month){
        if (month.equals("01")|| month.equals("1"))
                return 1;
        if (month.equals("02")|| month.equals("2"))
                return 2;
        if (month.equals("03")|| month.equals("3"))
                return 1;
        if (month.equals("04")|| month.equals("4"))
                return 4;
        if (month.equals("05")|| month.equals("5"))
                return 5;
        if (month.equals("06")|| month.equals("6"))
                return 6;
        if (month.equals("07")|| month.equals("7"))
                return 7;
        if (month.equals("08")|| month.equals("8"))
                return 8;
        if (month.equals("09")|| month.equals("9"))
                return 9;
        if (month.equals("10"))
                return 10;
        if (month.equals("11"))
                return 11;
        if (month.equals("12"))
                return 12;
        else return 0;
}


	// this function check if the string is a month or not
	// in first place is how many options do we have $ret[0]<=4
	private static boolean is_aMonth(String month, int months_arr[]) {
		int index = 1;
        int tmp= is_help(month);
        months_arr[0] = 0;
        if (tmp!=0){
                months_arr[0] = 1;
                months_arr[1] = tmp;
                index++;
        }
		
		if (is_jan(month)) {
			months_arr[index] = 1;
			months_arr[0]++;
			index++;
		}
		if (is_feb(month)) {
			months_arr[index] = 2;
			months_arr[0]++;
			index++;
		}
		if (is_mrc(month)) {
			months_arr[index] = 3;
			months_arr[0]++;
			index++;
		}
		if (is_apr(month)) {
			months_arr[index] = 4;
			months_arr[0]++;
			index++;
		}
		if (is_may(month)) {

			months_arr[index] = 5;
			months_arr[0]++;
			index++;
			if (index >= 5)
				return true;
		}
		if (is_jun(month)) {
			months_arr[index] = 6;
			months_arr[0]++;
			index++;
			if (index >= 5)
				return true;
		}
		if (is_jul(month)) {
			months_arr[index] = 7;
			months_arr[0]++;
			index++;
			if (index >= 5)
				return true;
		}
		if (is_aug(month)) {
			months_arr[index] = 8;
			months_arr[0]++;
			index++;
			if (index >= 5)
				return true;
		}
		if (is_sep(month)) {

			months_arr[index] = 9;
			months_arr[0]++;
			index++;
			if (index >= 5)
				return true;
		}
		if (is_oct(month)) {
			months_arr[index] = 10;
			months_arr[0]++;
			index++;
			if (index >= 5)
				return true;
		}
		if (is_nov(month)) {
			months_arr[index] = 11;
			months_arr[0]++;
			index++;
			if (index >= 5)
				return true;
		}
		if (is_dec(month)) {
			months_arr[index] = 12;
			months_arr[0]++;
			index++;
			if (index >= 5)
				return true;
		}
		if (months_arr[0] != 0)
			return true;
		return false;
	}

	// check if it is dec
	private static boolean is_dec(String month) {
		month = month.toUpperCase();
		return is_a(month, "DECEMBER", "DEC", "12", "12", false);
	}

	// check if it is nov
	private static boolean is_nov(String month) {
		month = month.toUpperCase();
		return is_a(month, "NOVEMBER", "NOV", "11", "11", false);
	}

	// check if it is sep
	private static boolean is_sep(String month) {
		month = month.toUpperCase();
		return is_a(month, "SEPTEMBER", "SEP", "09", "9", false);
	}

	// check if it is oct
	private static boolean is_oct(String month) {
		month = month.toUpperCase();
		return is_a(month, "OCTOBER", "OCT", "10", "10", false);
	}

	// check if it is aug
	private static boolean is_aug(String month) {
		month = month.toUpperCase();
		return is_a(month, "AUGUST", "AUG", "08", "8", false);
	}

	// check if it is jul
	private static boolean is_jul(String month) {
		month = month.toUpperCase();
		return is_a(month, "JULY", "JUL", "07", "7", true);
	}

	// check if it is jun
	private static boolean is_jun(String month) {
		month = month.toUpperCase();
		return is_a(month, "JUNE", "JUN", "06", "6", true);
	}

	// check if it is mrc
	private static boolean is_mrc(String month) {
		month = month.toUpperCase();
		return is_a(month, "MARCH", "MAR", "03", "3", false);
	}

	// check if it is may
	private static boolean is_may(String month) {
		month = month.toUpperCase();
		return is_a(month, "MAY", "MAY", "05", "5", false);
	}

	// check if it is apr
	private static boolean is_apr(String month) {
		month = month.toUpperCase();
		return is_a(month, "APRIL", "APR", "04", "4", false);
	}

	// check if it is feb
	private static boolean is_feb(String month) {
		month = month.toUpperCase();
		return is_a(month, "FEBUARY", "FEB", "02", "2", false);
	}

	// check if it is jan
	private static boolean is_jan(String month) {
		month = month.toUpperCase();
		return is_a(month, "JANUARY", "JAN", "01", "1", false);
	}

	private static boolean is_a(String month, String trg1, String trg2,
			String trg3, String trg4, boolean index) {

		if (month.equals(trg1) || month.equals(trg2) || month.equals(trg3)
				|| month.equals(trg4))
			return true;
		String str1="";
		String str2="";
		if (month.length()==3){
			str1=str1+right_char(month.charAt(0))+right_char(month.charAt(1));
			str2=str2+right_char(month.charAt(1))+right_char(month.charAt(2));
			if (str1.equals(trg3)|| str2.equals(trg3))
				return true;
		}
		if (month.length()==2){
			str1="";
			str2="";
			String str3="";
			str1=str1+right_char(month.charAt(0));
			str2=str2+right_char(month.charAt(1));
			str3=str1+str2;
			if (str1.equals(trg4)|| str2.equals(trg4)||str3.equals(trg3))
				return true;
		}
		if (allmost(month, trg1) || allmostTwo(month, trg2, index))
			return true;
		return false;
	}
	
	public static boolean allmostTwo(String src, String trg, boolean index) {
		int sum;
		int t;
		int i;
		int j;
		sum =0;
		t=0;
			for (i=0; i<src.length();i++){// run pver src
				
				for (j=t;j<trg.length();j++){//run over trg
				if (src.charAt(i)==trg.charAt(j)){
					sum++;
					t++;
					break;
				}
				}
			}
			if (sum==3)
				return true;
			if (sum==2){
				if (src.length()==2)
					return true;
				if (index){
					if (trg.equals("JUN")){
						if (src.charAt(2)=='L')
							return false;// src==JUL
						else return true; 
					}
					if (trg.equals("JUL"))
						if (src.charAt(2)=='N')
							return false;
						else return true;
				}
				return true;
			}
			return false;
			
	}		
	
	
	
	private static int is_aDay(String day) {
		int sum = 0;
		int tmp;
		int tmp1;
		int tmp2;
		if (day.length() != 1 && day.length() != 2&& day.length()!=3)
			return -1;
		if (day.length()==3){
			tmp1=is_aDay(day.substring(0,2));
			tmp2=is_aDay(day.substring(1,3));
			if(tmp1!=-1)
				return tmp1;
			return tmp2;
		}
		if (day.length()==2){
		tmp = right_char(day.charAt(0));
		if (tmp == -1)
			return -1;
		sum = tmp;
		tmp = right_char(day.charAt(1));
		if (tmp == -1)
			return -1;
		sum = sum * 10 + tmp;
		if (sum > 0 && sum < 32)
			return sum;
		else{
			int tmp3;
			tmp = right_char(day.charAt(0));
			tmp3 = right_char(day.charAt(1));
			if (tmp> 0 && tmp < 9){
				if (tmp3>0 && tmp3<9)
					return -1;
				else
					return tmp;
			}
			else
				if (tmp3>0 && tmp3<9)
					return tmp3;
		}
		}
		if (day.length()==1){
			tmp=right_char(day.charAt(0));
			if (tmp>0 && tmp <9)
				return tmp;
		}
	return -1;
		}
	public static class return_entry{
		private int priority;
		private String entry;

		public return_entry(){
			setPriority(0);
			setEntry(null);
		}

		public return_entry(int priority,String entry){
			this.entry=entry;
			this.priority=priority;		
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

		public int getPriority() {
			return priority;
		}

		public void setEntry(String entry) {
			this.entry = entry;
		}

		public String getEntry() {
			return entry;
		}

	}	

}
