package classes;

import java.sql.*;
import java.util.Scanner;

import tool.Role;

public class User {

	public String iD;
	public String password;
	public boolean connected ; 
	public Connection con;
	public Role role;
	
	public User() throws Exception{
		do {
			
		}while(identification());
		connected = true;
		
		//add function to give the good role according to the IDs
	}


	private boolean identification() throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez entrer votre nom d'utilisateur : ");
		this.iD = sc.nextLine();
        System.out.println("Veuillez entrer votre mot de passe : ");
        this.password = sc.nextLine();
        
        ResultSet rs = null ;
        try {
        	this.con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", this.iD, this.password);
        	this.role = Role.Admin;
        	
        }catch (SQLException e) {
        	try {
        		this.con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "\"" +this.iD + "\"", this.password);
            	this.role = Role.Patient;
        	}catch (SQLException e2) {
            	System.out.println("Erreur de connexion (le nom d'utilisateur ou le mot de passe est erroné) : " + e2.getMessage());
            	return true;
            }
        }
        	
		
		
		return false;
	}


	private boolean isAdmin(Statement st, ResultSet rs) {
		try {
			rs = st.executeQuery("SELECT * from \"Admin\" ");
			while(rs.next()) {
	    		if (rs.getString("UserName").equals(iD)) return true;
	    	}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	
		return false;
	}
	
}
