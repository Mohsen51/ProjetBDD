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
				 //Initialisation des variables de gestion jdbc
                st = con.createStatement();
                Statement st2;
                st2 = con.createStatement();
                Statement st3;
                st3 = con.createStatement();
                Statement st4;
                st4 = con.createStatement();
                ResultSet rs2= null;
                ResultSet rs3= null;
                ResultSet rs4= null;

                int patientId;
                rs = st.executeQuery("SELECT * FROM \"Consultation\" WHERE trunc(\"DateRDV\")= to_date('2020/06/10','yyyy/mm/dd') ORDER BY (\"DateRDV\") ASC");
                int idCorres=0;
                float age=0;
                //On fait ce qui vient pour chaque consultation à la date entrée
                while (rs.next()) {
                    idCorres = rs.getInt("IDConsultation");
                    rs2 = st2.executeQuery("SELECT \"IDPatient\" from \"PatientConsultant\" WHERE \"IDConsultation\" = " + idCorres);
                    //On fait ce qui vient pour chaque patient assitant à la consultation à la date entrée
                    while (rs2.next()) {
                        //On va afficher les characteristiques des patients ici
                        patientId = rs2.getInt("IDPatient");
                        //On selectionne les patients
                        rs3 = st3.executeQuery("SELECT * FROM \"Patient\" WHERE \"ID\" = " + patientId);

                        //On affiche la date du rdv pour ca on convertit la date en format java.sql.time en java.util.date (plus clair)
                        java.sql.Timestamp dbSqlTimestamp = rs.getTimestamp("DateRDV");
                        java.util.Date dbSqlTimeConverted = new java.util.Date(dbSqlTimestamp.getTime());
                        System.out.println("Ce patient a rdv le : " + dbSqlTimeConverted);

                        //Pour chaque patient selctionné on fait ce qui vient
                        while (rs3.next()) {

                            //Ici on va calculer l'age des patients en utilisant leur date de naissance
                            rs4 = st4.executeQuery("SELECT months_between(TRUNC(sysdate), to_date('" + rs3.getDate("DOB") + "','yyyy/mm/dd'))/12 AS age FROM DUAL");
                            while (rs4.next())
                                age = rs4.getFloat("AGE");

                            if (age<12)
                                System.out.println("Consultation d'un enfant : ");
                            if (age>=12 && age<18)
                                System.out.println("Consultation d'un ado : ");
                            //Si ce sont des adultes
                            else {
                                if (rs.getInt("Couple") == 1)
                                    System.out.println("Consultation en couple : ");
                                if (rs3.getString("Sexe").equals("f"))
                                    System.out.println("Consultation d'une femme : ");
                                if (rs3.getString("Sexe").equals("m"))
                                    System.out.println("Consultation d'un homme : ");
                            }
                            System.out.println("\nprenom : " + rs3.getString(2) + "\nnom : " + rs3.getString(3) + "\nage : " + (int)age +"\nadresse : " + rs3.getString(5) + "\n");
                        }
                    }
                }
                rs.close();
                rs2.close();
                st.close();
                st2.close();
                st3.close();
         
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

             System.out.println("Entrez le sexe du patient : (m : homme, f: femme)");
             String sexe = "'" + sc.nextLine() + "'";

             System.out.println("Entrez votre date de naissance sous la forme : yyyy/mm/dd (par ex : 2003/05/19) : ");
             String dob = "'" + sc.nextLine() + "'";

             System.out.println("Entrez votre adresse : ");
             String adresse = "'" + sc.nextLine() + "'";

             System.out.println("Entrez la façon dont le patient a connu le cabinet : ");
             String psy = "'" + sc.nextLine() + "'";

             System.out.println("Entrez la profession actuelle du patient : ");
             String prof = "'" + sc.nextLine() + "'";
             
             System.out.println("Entrez votre Email : ");
             String email = sc.nextLine() ;
             
             System.out.println("Entrez un mot de passe : ");
             String password = sc.nextLine();

	        try {
	        	 //On ajoute le nouveau patient et sa profession dans la bdd
                st = con.createStatement();
                sql = "INSERT INTO \"Patient\"(\"Prenom\",\"Nom\",\"ConnaissancePsy\",\"Adresse\",\"DOB\",\"Sexe\",\"Email\",\"Password\") VALUES(" + prenomPatient + "," + nomPatient + "," + psy + "," + adresse + ",TO_DATE(" + dob + ", 'yyyy/mm/dd')," + sexe + "," + "'" + email + "' , '" + password + "')";
                st.executeQuery(sql);
                sql = "INSERT INTO \"Profession\"( \"IDProfession\" , \"IDPatient\",\"Profession\") VALUES("+ idMaker(con , "Profession","Profession", prof) + ",pat_seq.currval," + prof + ")";
                //st.executeQuery(sql);
                
                sql = "CREATE USER \"" + email + "\" " + 
                		"  IDENTIFIED BY "+ password + 
                		"  DEFAULT TABLESPACE Projet_bdd " + 
                		"	QUOTA 20M on Projet_bdd " ;
                
                
                st.executeQuery(sql);
                sql = "GRANT create session TO \"" + email +"\"" ;
                
                st.executeQuery(sql);

                st.close();
		        
			} catch (SQLException e) {
				e.printStackTrace();
				done = retry();
			}
		}while(done == 0);

	}

	private static String idMaker(Connection con , String column, String table, String prof) {
		
        
        //rs = st.executeQuery("SELECT \" " + column + "\" FROM \""+ table +"\" ;");
        try {
			rs = st.executeQuery("SELECT * FROM \""+ table +"\" ");
			while(rs.next()) {
	        	if (rs.getString(column).equals(prof)) return rs.getString("ID"+ table);
	        }
			
			switch(table) {
			case "Profession" :
				return "prof_seq.nextval";
				
			default :
				return "prof_seq.currval";
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return "1";
		}
        

		

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

	public static int menuP() {
		Scanner sc = new Scanner(System.in);
		int choice = 6;
		do {
			System.out.println("Veuillez choisir une action (entrez un nombre de 1 à 10) : ");
	        System.out.println("0.  Quitter le programme ");
	        System.out.println("1.  Consulter RDV ");
	        System.out.println("2.  Ajouter un rdv ");
	        System.out.println("3.  Entrer info sur un rdv ");
	        choice  = sc.nextInt();
		}while(choice > 3 || choice < 0);
			
			
		
		return choice;
		
	}

	
	
	
	
	
	

}
