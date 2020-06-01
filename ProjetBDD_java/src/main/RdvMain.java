package main;

import java.util.Scanner;

import classes.User;
import tool.ToolBox;
import tool.Role;

public class RdvMain {

	 public static void main(String[] args) throws Exception{
	        Class.forName("oracle.jdbc.driver.OracleDriver");
	        
	        User user = new User();
	        int choose;
	        
	        
	        while(user.connected) {
	        	switch(user.role) {
	        	case Admin :
	        		switch(ToolBox.menu()) {
		        	case 0 :
		        		try {
		        			if(ToolBox.exit() != 0) user.connected = false;
		        		}catch(Exception e) {
		        			user.connected = false;
		        		}
		        		break;
		        	case 1 :
		        		
						do {
							
							ToolBox.displayRDVS(user.con);
							System.out.println("Consulter un autre jour ? (0 pour oui)");
							Scanner sc = new Scanner(System.in);
		                     try {
		                    	 choose = sc.nextInt();
		                     }catch(Exception e1) {
		                    	 choose = 1;
		                     }
		        		}while(choose == 0);
		        		
		        		break;
		        	case 2 :
		        		ToolBox.newPatient(user.con);
		        		break;
		        	case 3 :
		        		
						do {
							
							ToolBox.addRDV(user.con);
							System.out.println("Ajouter un autre RDV ? (0 pour oui)");
							Scanner sc = new Scanner(System.in);
		                     try {
		                    	 choose = sc.nextInt();
		                     }catch(Exception e1) {
		                    	 choose = 1;
		                     }
		        		}while(choose == 0);
		        		
		        		break;
		        	case 4 :
		        		do {
		        			
			        		ToolBox.updateRDV(user.con);
							System.out.println("Modifier un autre RDV ? (0 pour oui)");
							Scanner sc = new Scanner(System.in);
		                     try {
		                    	 choose = sc.nextInt();
		                     }catch(Exception e1) {
		                    	 choose = 1;
		                     }
		        		}while(choose == 0);
		        		
		        		break;
		        	case 5 :
		        		ToolBox.updateP(user.con);
		        		break;
		        	case 6 :
		        		ToolBox.delRDV(user.con);
		        		break;
		        	case 7 :
		        		ToolBox.prevRDV(user.con);
		        	}	
	        		break;
	        		
	        	case Patient :
	        		switch(ToolBox.menuP()) {
		        	case 0 :
		        		try {
		        			if(ToolBox.exit() != 0) user.connected = false;
		        		}catch(Exception e) {
		        			user.connected = false;
		        		}
		        		break;
		        	case 1 :
		        		ToolBox.displayRDVP(user);
		        		break;
	        		}
	        	}
	        	
	        }
	        
	 }
}
