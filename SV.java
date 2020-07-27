
public class SV {

	private String STR = "";
	private String CHR;
	private int POS;
	private String ID;
	private String REF;
	private String ALT;
	private String QUAL;
	private String FILTER;
	private int END;
	private int SVLEN;
	
	public SV(String S1, String S2, int i2, String S3, int i3, int i4, String Q,String S4, String S5, String S6){
		STR = S1;
		CHR = S2;
		POS = i2;
		ALT = S3;
		END = i3;
		SVLEN = i4;
		ID = S4;
		REF = S5;
		FILTER = S6;
		QUAL = Q;
	}
	
	public String getSTR() {
		return STR;
	}
	
	public String getCHR() {
		return CHR;
	}
	
	public int getPOS() {
		return POS;
	}
	
	public String getQUAL() {
		return QUAL;
	}
	
	public String getALT() {
		return ALT;
	}

	public int getEND() {
		return END;
	}
	
	public int getSVLEN() {
		return SVLEN;
	}
	
	
}
