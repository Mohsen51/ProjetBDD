package main;

import classes.User;
import tool.ToolBox;
import tool.Role;

public class RdvMain {

	 public static void main(String[] args) throws Exception{
	        Class.forName("oracle.jdbc.driver.OracleDriver");
	        
	        User user = new User();
	        
	        
	        
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
		        		ToolBox.displayRDV(user.con);
		        		break;
		        	case 2 :
		        		ToolBox.newPatient(user.con);
		        		break;
		        	case 3 :
		        		ToolBox.addRDV(user.con);
		        		break;
		        	case 4 :
		        		break;
		        	case 5 :
		        		break;
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
		        		ToolBox.displayRDV(user.con);
		        		break;
		        	case 2 :
		        		ToolBox.addRDV(user.con);
		        		break;
		        	case 3 :
		        		break;
	        		}
	        	}
	        	
	        }
	        
	 }
}
