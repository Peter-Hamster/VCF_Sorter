import java.util.*;
import java.io.*;


public class VCF_Sorter {
	static String description = "";
	
	
	
	public static ArrayList<SV> build_SV_list(String filename){
		ArrayList<SV> SV_list = new ArrayList<SV>();
		
		try {
			File f = new File(filename);
			System.out.println("*************************************************************************");

			System.out.println("File [" + filename + "] received");
			System.out.println("Processing Data...");
			Scanner stdin2 = new Scanner(f);
			
			while(true) {
				String read = stdin2.nextLine();
				description = description + read;
				if (read.charAt(0) == '#' && read.charAt(1) != '#') {
					break;
				}
				description = description +"\n";
			}
			stdin2.nextLine();
			
			while(true) {
				String CHR = "";
				CHR = stdin2.next();
				String POS_S = "";
				POS_S = stdin2.next();
				int POS = Integer.parseInt(POS_S);
				String ID_S = "";
				ID_S = stdin2.next();
				String REF = "";
				REF = stdin2.next();
				String ALT = "";
				ALT = stdin2.next();
				
				if(ALT.length() != 5) {
					stdin2.nextLine();
					continue;
				}
				else {
//					System.out.println(QUAL);
				}
				String QUAL = "";
				QUAL = stdin2.next();
				String FILTER  = "";
				FILTER = stdin2.next();
				
				String rest = stdin2.nextLine();
				int pos_SVLEN_S = rest.indexOf("SVLEN");
				int pos_SVLEN_E = rest.indexOf(";", pos_SVLEN_S);
				String SVLEN_S = rest.substring(pos_SVLEN_S + 6, pos_SVLEN_E);
				
				int pos_END_S = rest.indexOf("END");
				int pos_END_E = rest.indexOf(";", pos_END_S);
				String END_S = rest.substring(pos_END_S + 4, pos_END_E);
				
//				System.out.println(SVLEN_S);
//				System.out.println(END_S);
				int SVLEN = Integer.parseInt(SVLEN_S);
				int END = Integer.parseInt(END_S);
				
				String combination = CHR + "	" + POS_S + "	" + ID_S + "	" + REF
						+ "	" + ALT + "	"  + QUAL + "	" + FILTER  + rest;
				
				SV newSV = new SV(combination, CHR, POS, ALT, END, SVLEN,QUAL,ID_S,REF,FILTER);
				SV_list.add(newSV);
				
//				System.out.println(ALT);
//				System.out.println(POS_S);
//				System.out.println(QUAL);
			}
			

			
			
		}
		catch(IOException ioe) {
			System.out.println("File not found!");
		}
		catch(Exception ex) {
			System.out.println("Finish Processing!");
		}
		System.out.println(SV_list.size() + " SV detected...");
		
		return SV_list;
		
	}

	public static ArrayList<SV> sortBySVLEN(ArrayList<SV> SV_list) {
		int size = SV_list.size();
		for(int i = 0; i < size-2; i++) {
			for(int j = size-1; j > i; j--) {
				if(SV_list.get(j).getSVLEN() > SV_list.get(j-1).getSVLEN()) {
					SV tempSV = SV_list.get(j);
					SV_list.set(j, SV_list.get(j-1));
					SV_list.set(j-1, tempSV);
				}
			}
		}
		return SV_list;
	}
	
	public static void export(ArrayList<SV> SV_list, String filename, String type) {
		try {
			File f = new File(filename + "_" + type + ".txt");
			FileWriter fw = new FileWriter(f, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(description);
			pw.flush();			
			for(int i = 0; i < SV_list.size(); i++) {
				pw.println(SV_list.get(i).getSTR());
				pw.flush();
			}
		}
		catch(IOException ioe) {
		}

		System.out.println("Export the sorted file as [" + filename + "_" + type + ".txt]");

	}
	
	public static void sortBySVLEN_Separate(ArrayList<SV> SV_list, String filename, String type) {
		ArrayList<SV> DUP_list = new ArrayList<SV>();
		ArrayList<SV> INV_list = new ArrayList<SV>();
		ArrayList<SV> DEL_list = new ArrayList<SV>();
		int size = SV_list.size();
		for(int i = 0; i < size-2; i++) {
			for(int j = size-1; j > i; j--) {
				if(SV_list.get(j).getSVLEN() > SV_list.get(j-1).getSVLEN()) {
					SV tempSV = SV_list.get(j);
					SV_list.set(j, SV_list.get(j-1));
					SV_list.set(j-1, tempSV);
				}
			}
		}
		for(int i = 0; i < size; i++) {
			String ALT = SV_list.get(i).getALT();
			
			switch (ALT) {
			
			case ("<DUP>"):{
				DUP_list.add(SV_list.get(i));
				break;
			}
			
			case ("<INV>"):{
				INV_list.add(SV_list.get(i));
				break;
			}
			
			case ("<DEL>"):{
				DEL_list.add(SV_list.get(i));
				break;
			}
			}
		}
		System.out.println(DUP_list.size() + " duplication SV detected...");
		export(DUP_list, filename, (type + "_DUP"));
		System.out.println();
		
		System.out.println(INV_list.size() + " inversion SV detected...");
		export(INV_list, filename, (type + "_INV"));
		System.out.println();
		
		System.out.println(DEL_list.size() + " deletion type of SV detected...");
		export(DEL_list, filename, (type + "_DEL"));
		

		
	}
	
	
	

	
	public static void main(String[] args) {
		
		boolean loop1 = true;

		String filename = "";

		
		while(loop1) {
			System.out.println("*************************************************************************");
			System.out.println("Hello, and welcome to the VCF_Sorter");
			System.out.println("Please give the name of VCF file:--> ");
			Scanner stdin1 = new Scanner(System.in);
			filename = stdin1.nextLine();
			
			ArrayList<SV> SV_list = build_SV_list(filename);

			filename = filename.substring(0,filename.length()-4);
			
			
			while(true) {
				System.out.println("*************************************************************************");
				
				System.out.println("Do you want to sperate different SV to different files (y/n)?");
				
				String tempS = stdin1.nextLine();

				
				if(tempS.equals("y")) {
					System.out.println("Start to separate SV to different files by SV types in the order of SVLEN...");
					System.out.println("*************************************************************************");
					sortBySVLEN_Separate(SV_list, filename, "SVLEN_Sorted");
					break;
				}
				else if(tempS.equals("n")) {
					System.out.println("Start to sort the SV in the order of SVLEN...");
					System.out.println("*************************************************************************");
					ArrayList<SV> SV_list_SVLENsorted = sortBySVLEN(SV_list);
					export(SV_list_SVLENsorted, filename, "SVLEN_Sorted");
					break;
				}
				else {
					System.out.println("Invalid input! Please enter 'y' or 'n'...");
				}
				
				
			}
			
			System.out.println("*************************************************************************");
			
//			for(int i = 0; i < SV_list_SVLENsorted.size(); i++) {
//				System.out.println(SV_list_SVLENsorted.get(i).getSTR());
//			}
//			
			System.out.println("VCF Sorter terminated...");
			
			
			break;
		}
		
	
		
	}
	
	
}
