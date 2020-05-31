package tool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ToolBox {
	public static  Statement st ;
    public static  ResultSet rs ;
    public static String sql;
    
	
	public static int menu() {
		
		Scanner sc = new Scanner(System.in);
		int choice = 6;
		do {
			System.out.println("Veuillez choisir une action (entrez un nombre de 1 à 10) : ");
	        System.out.println("0.  Quitter le programme ");
	        System.out.println("1.  Consulter RDV ");
	        System.out.println("2.  Enregistrer un patient ");
	        System.out.println("3.  Ajouter un rdv ");
	        System.out.println("4.  Entrer info sur un rdv ");
	        System.out.println("5.  Modifier info patient ");
	        choice  = sc.nextInt();
		}while(choice > 5 || choice < 0);
			
			
		
		return choice;
	}

	public static int exit() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Êtes-vous sûr de vouloir quitter le programme ? (0 pour revenir) ");
        return sc.nextInt();
        
		
	}

	public static void displayRDV(Connection con) {
		int done = 1;
		do {
			try {
				st = con.createStatement();
				rs = st.executeQuery("select * from \"Patient\"");

	         while (rs.next())
	             System.out.println("ID : " + rs.getInt(2) + " prenom : " + rs.getString(4) + " nom : " + rs.getString(3) + " age : " + rs.getInt(1) + " " + rs.getString(5));

	         rs.close();
	         st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				done = retry();
				
			}

		}while(done == 0);
		
         
		
	}

	
	
	public static void newPatient(Connection con) {
		
		Scanner sc = new Scanner(System.in);
		int done = 1;
		do {
			
	        System.out.println("Entrez le nom du patient : ");
	        String nomPatient = "'" + sc.nextLine() + "'";

	        System.out.println("Entrez le prenom du patient : ");
	        String prenomPatient = "'" + sc.nextLine() + "'";

	        System.out.println("Entrez votre date de naissance sous la forme : yyyy/mm/dd (par ex : 2003/05/19) : ");
	        String dob = "'" + sc.nextLine() + "'";

	        System.out.println("Entrez votre adresse : ");
	        String adresse = "'" + sc.nextLine() + "'";

	        System.out.println("Entrez la façon dont le patient a connu le cabinet : ");
	        String psy = "'" + sc.nextLine() + "'";

	        System.out.println("Entrez la profession actuelle du patient : ");
	        String prof = "'" + sc.nextLine() + "'";

	        try {
				st = con.createStatement();
				sql = "INSERT INTO \"Patient\"(\"Prenom\",\"Nom\",\"ConnaissancePsy\",\"Adresse\",\"DOB\") VALUES(" + prenomPatient + "," + nomPatient + "," + psy + "," + adresse + ",TO_DATE(" + dob + ", 'yyyy/mm/dd'))";
		        st.executeQuery(sql);
		        sql = "INSERT INTO \"Profession\"(\"IDPatient\",\"Profession\") VALUES("+ idMaker() +"," + prof + ")";
		        st.executeQuery(sql);
		        st.close();
		        
			} catch (SQLException e) {
				e.printStackTrace();
				done = retry();
			}
		}while(done == 0);

	}

	private static int idMaker() {
		
		return 1;
	}

	private static int retry() {
		int choice;
		Scanner sc = new Scanner(System.in);
		System.out.println("Do you want to retry ? (0 to retry)");
		try {
			choice = sc.nextInt();
		} catch (Exception e) {
			choice = 1;
		}
		return choice;
	}

	public static void addRDV(Connection con) {
		
		
		
	}

	
	
	
	
	
	

}
