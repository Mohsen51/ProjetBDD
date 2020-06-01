package tool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import classes.User;



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
	        System.out.println("6.  Annuler un rdv ");
	        System.out.println("7.  Consulter un RDV anterieur ");
	        choice  = sc.nextInt();
		}while(choice > 7 || choice < 0);
			
			
		
		return choice;
	}

	public static int exit() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Êtes-vous sûr de vouloir quitter le programme ? (0 pour revenir) ");
        return sc.nextInt();
        
		
	}

	public static void displayRDV(Connection con, String string) {
		int done = 1;
		do {
			try {
				Scanner sc = new Scanner(System.in);
				
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
                int idCorres=0;
                float age=0;
                String date;
                
                	 
                     System.out.println("Entrez la date " + string + " sous la forme : yyyy/mm/dd (par ex : 2003/05/19) : ");
                     date = sc.nextLine();
                     rs = st.executeQuery("SELECT * FROM \"Consultation\" WHERE trunc(\"DateRDV\")= to_date('" + date + "','yyyy/mm/dd') ORDER BY (\"DateRDV\") ASC");
                     
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
                                 System.out.println("\nprenom : " + rs3.getString(2) + "\nnom : " + rs3.getString(3) + "\nage : " + (int)age +"\nadresse : " + rs3.getString(6) + "\n");
                             }
                         }
                     }
                     
                     
                     
               
                

               
                /*rs.close();
                rs2.close();
                st.close();
                st2.close();
                st3.close();*/
         
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
                try {
                	st.executeQuery(sql);
                }catch(SQLException e2) {};
                
                
                
                //Create a new user on the databasis
                sql = "CREATE USER \"" + email + "\" " + 
                		"  IDENTIFIED BY "+ password + 
                		"  DEFAULT TABLESPACE Projet_bdd " + 
                		"	QUOTA 20M on Projet_bdd " ;
                
                //Give access to connect and perform select
                st.executeQuery(sql);
                sql = "GRANT create session TO \"" + email +"\"" ;
                st.executeQuery(sql);
                sql = "GRANT SELECT ON \"Consultation\" TO \"" + email +"\"" ;
                st.executeQuery(sql);
                
                sql = "GRANT SELECT ON \"PatientConsultant\" TO \"" + email +"\"" ;
                st.executeQuery(sql);
                
                sql = "GRANT SELECT ON \"Patient\" TO \"" + email +"\"" ;
                st.executeQuery(sql);
                
                st.close();
		        
			} catch (SQLException e) {
				System.out.println(e.getMessage());
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
		
		Scanner sc = new Scanner(System.in);
		
		
        try {
        	//Initialisation des variables pour la boucle while
            int day=0;
            int hours = 0;
            st = con.createStatement();
            int count = 20;
            String date = null;
            int exists=1;

            /*Si le numéro de jour correspond à dimanche on redemande la date idem pour une heure en dehors du crénau 8h 20h
            Si le total de rdv excede 20 cela veut dire que la psychologue travaille plus de 10h*/
            while (day==0 || count>=20 || (hours >20 || hours<8) || exists==1) {
                System.out.println("Entrez la date du RDV sous la forme : yyyy/mm/dd hh24:mi (par ex : 2003/05/19 16:00) : ");
                date = sc.nextLine();

                //On convertit le string en date pour obtenir le jour (dimanche par exemple) et pour pouvoir étudier l'heure de rdv
                Date dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(date);
                java.util.Date dateConverted = new java.util.Date(dateFormat.getTime());

                //Splinting to have date and time separated
                String[] parts = date.split("\\s+");

                //On extrait le jour pour confirmer une erreur en cas de rdv dimanche (0: dimanche)
                day = dateConverted.getDay();

                //On extrait l'heure pour confirmer une erreur en cas de rdv en dehors de 8h 20h
                hours = dateConverted.getHours();
                if (day == 0)
                    System.out.println("Le cabinet n'est pas ouvert le dimanche ! ");
                if (hours >20 || hours<8)
                    System.out.println("Le cabinet est ouvert de 8h à 20h seulement ! ");

                //On compte le nombre de rdv pris pour vérifier le quota de 10h de travail
                rs = st.executeQuery("SELECT COUNT(\"IDConsultation\") AS total FROM \"Consultation\" WHERE trunc(\"DateRDV\")= to_date('" + parts[0] + "','yyyy/mm/dd')");
                while (rs.next())
                    count = rs.getInt("total");
                //On vérifie que le créneau est disponible

                //Ici en particulier on va ajouter 30minutes a la date pour vérifier les créneaux
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat);
                cal.add(Calendar.MINUTE, 30);
                dateFormat = cal.getTime();
                DateFormat formadate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String strDate = formadate.format(dateFormat);

                rs = st.executeQuery("SELECT COUNT(\"IDConsultation\") AS exist FROM \"Consultation\" WHERE (\"DateRDV\") BETWEEN to_date('" + date + "','yyyy/mm/dd hh24:mi') AND to_date('" + strDate +  "','yyyy/mm/dd hh24:mi')");
                while (rs.next())
                    exists = rs.getInt("exist");
                if (count>=20)
                    System.out.println("Il n'y a plus de place pour ce jour la !");
                if (exists==1) {
                    System.out.println("Ce creneau est déjà pris !");
                    rs = st.executeQuery("SELECT \"DateRDV\" FROM \"Consultation\" WHERE trunc(\"DateRDV\")= to_date('" + parts[0] + "','yyyy/mm/dd')");
                    System.out.println("Pour info, les creneaux suivants sont déjà pris donc indisponibles : ");
                    while (rs.next())
                        System.out.println(rs.getTime("DateRDV"));
                    System.out.println("Vous devez choisir un créneau à au moins 30 minutes d'intervalle avec ces creneaux ! ");
                }
            }

            System.out.println("Consultation en couple ? (1 si oui 0 sinon) : ");
            int couple = sc.nextInt();
            sc.nextLine();

            sql = "INSERT INTO \"Consultation\"(\"DateRDV\", \"Couple\") VALUES(TO_DATE('" + date + "', 'yyyy/mm/dd hh24:mi')," + couple + ")";
            st.executeQuery(sql);

            //Verification que le nombre de patient est entre 1 et 3
            int nb = 0;
            do {
                System.out.println("Combien de patients vont assister à ce RDV ? : (1 à 3 patients) ");
                nb = sc.nextInt();
                sc.nextLine();
            } while (nb>3 || nb<1);

            int id = 0;
            for (int i=0;i<nb;i++) {
                System.out.println("Entrez le nom du patient prenant le RDV: ");
                String nom = "'" + sc.nextLine() + "'";

                System.out.println("Entrez le prenom du patient prenant le RDV: ");
                String prenom = "'" + sc.nextLine() + "'";

                //On cherche le patient dans la bdd
                sql = "SELECT ID FROM \"Patient\" WHERE \"Nom\" =" + nom + " AND \"Prenom\" =" + prenom;
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    id = rs.getInt("ID");
                }

                //On associe la consultation a un ou plusieurs patient(s) spécifiques
                sql= "INSERT INTO \"PatientConsultant\" VALUES(cons_seq.currval,"+ id + ")";
                st.executeQuery(sql);
            }
            st.close();
		} catch (SQLException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}

	public static int menuP() {
		Scanner sc = new Scanner(System.in);
		int choice = 6;
		do {
			System.out.println("Veuillez choisir une action (entrez un nombre de 1 à 10) : ");
	        System.out.println("0.  Quitter le programme ");
	        System.out.println("1.  Consulter ses RDV ");
	        choice  = sc.nextInt();
		}while(choice > 1 || choice < 0);
			
			
		
		return choice;
		
	}

	public static void updateRDV(Connection con) {
		Scanner sc = new Scanner(System.in);
		
		try {
			st = con.createStatement();
			String rdv = null;
	        System.out.println("Entrez la date du RDV sous la forme : yyyy/mm/dd hh24:mi (par ex : 2003/05/19 16:00) : ");
	        rdv = sc.nextLine();

	        int idc=0;
	        rs = st.executeQuery("SELECT \"IDConsultation\" FROM \"Consultation\" WHERE (\"DateRDV\")= to_date('" + rdv + "','yyyy/mm/dd hh24:mi')");
	        while (rs.next())
	            idc = rs.getInt("IDConsultation");

	        System.out.println("Entrez le prix qu'a coûté la consultation : ");
	        int prix = sc.nextInt();
	        sc.nextLine();

	        System.out.println("Quel a été le moyen de paiement du patient : ");
	        String paiement = "'" + sc.nextLine() + "'";

	        System.out.println("Quels sont vos commentaires sur cette consultation : ");
	        String note = "'" + sc.nextLine() + "'";

	        System.out.println("Quel était le degré d'anxieté du patient ? : ");
	        int anxiete = sc.nextInt();

	        //On associe les données recueillies lors d'une consultation et on les mets dans la bdd
	        sql = "UPDATE \"Consultation\" SET \"Prix\"=" + prix + ",\"Reglement\"=" + paiement + ",\"Note\"=" + note + ",\"Anxiete\"=" + anxiete + " WHERE \"IDConsultation\" =" + idc;
	        st.executeQuery(sql);

	        st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());;
		}
        
		
	}

	public static void delRDV(Connection con) {
		Scanner sc = new Scanner(System.in);
		Scanner sc2 = new Scanner(System.in);
		Scanner sc3 = new Scanner(System.in);
		
		try {
			st = con.createStatement();
			String rdv = null;
			int choose;
			int choice = 1;
			do {
				
				System.out.println("Entrez la date du RDV sous la forme : yyyy/mm/dd hh24:mi (par ex : 2003/05/19 16:00) : ");
		        rdv = sc.nextLine();
		        rs = st.executeQuery("SELECT \"IDConsultation\" FROM \"Consultation\" WHERE (\"DateRDV\")= to_date('" + rdv + "','yyyy/mm/dd hh24:mi')");
		        int idc=0;
		        while (rs.next())
		            idc = rs.getInt("IDConsultation");
		        System.out.println("Etes vous sur de vouloir annuler le rdv du " + rdv + " (0 pour oui)");
		        choice = sc3.nextInt();
		        
		        if (choice == 0) {
		        	
		        	
		        	rs = st.executeQuery("DELETE FROM \"PatientConsultant\" WHERE \"IDConsultation\" = " + idc);
		        	rs = st.executeQuery("DELETE FROM \"Consultation\" WHERE (\"DateRDV\")= to_date('" + rdv + "','yyyy/mm/dd hh24:mi')");
		        	System.out.println("Le rdv du " + rdv + " a �t� annul�.");
		        }
		       
		       

		        st.close();
				
				
		        System.out.println("Voulez vous annuler un autre rdv ? (0 pour oui)");
	            try {
	           	 choose = sc2.nextInt();
	            }catch(Exception e1) {
	           	 choose = 1;
	            }
				
			}while (choose  == 0);
	        
		} catch (SQLException e) {
			
			System.out.println(e.getMessage());
		}
        
		
		
	}

	public static void displayRDVP(User user) {
		
		try {
			st = user.con.createStatement();
			sql = "SELECT \"DateRDV\"  FROM \"Consultation\" c INNER JOIN \"PatientConsultant\" pc ON c.\"IDConsultation\" = pc.\"IDConsultation\" INNER JOIN \"Patient\" p ON pc.\"IDPatient\"  = p.ID WHERE p.\"Email\" = '" + user.iD +"' " ;
			// Make sure that the Good Schema is targeted 
			rs = st.executeQuery("ALTER SESSION SET current_schema = PSY");
			// Run the Query to select only the Rdv link to the user
			rs = st.executeQuery(sql);
			System.out.println("Vos Rdv :");
			while(rs.next()) {
				java.sql.Timestamp dbSqlTimestamp = rs.getTimestamp("DateRDV");
                java.util.Date dbSqlTimeConverted = new java.util.Date(dbSqlTimestamp.getTime());
                System.out.println(dbSqlTimeConverted);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		
	}

	public static void prevRDV(Connection con) {
		Scanner sc = new Scanner(System.in);
		Scanner sc2 = new Scanner(System.in);
		Statement state2;
		String nomRdv;
		String prenomRdv;
		int idP = 0;
		int idC = 0;
		int choose = 1;
		ResultSet consultations;
		do {
			System.out.println("Entrez le nom du patient dont vous voulez consulter les rdv : ");
	        nomRdv = "'" + sc.nextLine() + "'";
	        
	        System.out.println("Entrez le prenom du patient dont vous voulez consulter les rdv : ");
	        prenomRdv = "'" + sc.nextLine() + "'";
	        //On cherche l'id du patient
	        try {
				st = con.createStatement();
				 sql = "SELECT \"ID\" FROM \"Patient\" WHERE \"Nom\" =" + nomRdv + " AND \"Prenom\" =" + prenomRdv;
			        rs = st.executeQuery(sql);
			        
			        while (rs.next()) {
			            idP = rs.getInt("ID");
			        }
			        //On cherche les consultations que ce patient a pris
			        sql = "SELECT \"IDConsultation\" FROM \"PatientConsultant\" WHERE \"IDPatient\" =" + idP + " ORDER BY \"IDConsultation\" DESC";
			        rs = st.executeQuery(sql);
			        
			        while (rs.next()) {
			            //On va chercher les dates correspondantes à ces consultations
			            idC = rs.getInt("IDConsultation");
			            sql = "SELECT \"DateRDV\" FROM \"Consultation\" WHERE \"IDConsultation\" =" + idC;

			            state2 = con.createStatement();
			            consultations = state2.executeQuery(sql);
			            while (consultations.next()) {
			                //On convertit pour un plus bel affichage de la date
			                java.sql.Timestamp rdvTimestamp = consultations.getTimestamp("DateRDV");
			                java.util.Date rdvTimeConverted = new java.util.Date(rdvTimestamp.getTime());
			                System.out.println("Ce patient a pris un rdv le : " + rdvTimeConverted);
					
			            }
			        }
			        
			        
	        
		
        
        
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
            
            System.out.println("Voulez vous consulter les RDVs d'un autre Patient? (0 pour oui)");
            try {
           	 choose = sc2.nextInt();
            }catch(Exception e1) {
           	 choose = 1;
            } 
		}while(choose  == 0);
       	
	}

	public static void updateP(Connection con) {
		Scanner sc = new Scanner(System.in);
		Scanner sc2 = new Scanner(System.in);
		int choose;
		do {
			
			 System.out.println("Entrez le nom du patient dont vous voulez modifier les infos : ");
             String nom = "'" + sc.nextLine() + "'";

             System.out.println("Entrez le prenom du patient dont vous voulez modifier les infos  : ");
             String prenom = "'" + sc.nextLine() + "'";

             System.out.println("Entrez la nouvelle adresse du patient dont vous voulez modifier les infos  : ");
             String newAdresse = "'" + sc.nextLine() + "'";

             System.out.println("Entrez la nouvelle profession actuelle du patient : ");
             String profession = "'" + sc.nextLine() + "'";

             //On cherche l'id du patient pour lequel on veut modifier les données
             try {
				st = con.createStatement();
				sql = "SELECT \"ID\" FROM \"Patient\" WHERE \"Nom\" =" + nom + " AND \"Prenom\" =" + prenom;
	             rs = st.executeQuery(sql);
	             int idPatient = 0;
	             while (rs.next()) {
	                 idPatient = rs.getInt("ID");
	             }
	             //On actualise les données du patient
	             sql = "UPDATE \"Patient\" SET \"Adresse\"=" + newAdresse + " WHERE ID = " + idPatient;
	             st.executeQuery(sql);
	             //On lui ajoute une nouvelle profession
	             sql = "INSERT INTO \"Profession\"(\"IDPatient\",\"Profession\") VALUES(" + idPatient + "," + profession + ")";
	             try {
	            	 st.executeQuery(sql);
	             }catch(SQLException e3) {
	            	 System.out.println("Implementation de la Profession : FAILED");
	             }
	             
	             st.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
             
			
			
			 System.out.println("Voulez vous modifier les infos d'un autre Patient? (0 pour oui)");
	            try {
	           	 choose = sc2.nextInt();
	            }catch(Exception e1) {
	           	 choose = 1;
	            } 
		}while(choose == 0);
	}

	public static void displayRDVS(Connection con) {
		Scanner sc = new Scanner(System.in);
		//Initialisation des variables de gestion jdbc
        try {
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

	        String consultation;
	        System.out.println("Entrez la date que vous voulez vérifier sous la forme : yyyy/mm/dd (par ex : 2003/05/19) : ");
	        consultation = sc.nextLine();

	        int patientId;
	        System.out.println("Voulez vous vérifier pour la semaine suivant cette date ? (1 si oui) ");
	        int semaine = sc.nextInt();
	        sc.nextLine();
	        if (semaine==1) {
	            //Process to add seven days to the date
	            Date date1=new SimpleDateFormat("yyyy/MM/dd").parse(consultation);
	            Calendar cal = Calendar.getInstance();
	            cal.setTime(date1);
	            cal.add(Calendar.DATE, 7);
	            date1 = cal.getTime();
	            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	            String strDate = format.format(date1);

	            rs = st.executeQuery("SELECT * FROM \"Consultation\" WHERE trunc(\"DateRDV\") BETWEEN to_date('" + consultation + "','yyyy/mm/dd') AND to_date('" + strDate + "','yyyy/mm/dd') ORDER BY (\"DateRDV\") ASC");
	        }
	        else
	            rs = st.executeQuery("SELECT * FROM \"Consultation\" WHERE trunc(\"DateRDV\")= to_date('" + consultation + "','yyyy/mm/dd') ORDER BY (\"DateRDV\") ASC");
	        int idCorres=0;
	        float age=0;
	        //Occ pour verifier s'il y a des consultations a une date donnée
	        int occ=0;
	        //On fait ce qui vient pour chaque consultation à la date entrée
	        while (rs.next()) {
	            occ=1;
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
	                    System.out.println("\nprenom : " + rs3.getString(2) + "\nnom : " + rs3.getString(3) + "\nage : " + (int)age +"\nadresse : " + rs3.getString(6) + "\n");
	                }
	            }
	        }
	        if (occ==0)
	            System.out.println("Pas de RDV ce jour la ! ");
	        st.close();
	        st2.close();
	        st3.close();
		} catch (SQLException | ParseException e) {
			System.out.println(e.getMessage());
		}
        
	}
}
