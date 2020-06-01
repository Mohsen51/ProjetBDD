import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws Exception{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Scanner sc = new Scanner(System.in);
        Boolean user = true;
        //Lancement du programme do while pour l'interface de connexion
        do {
            System.out.println("Veuillez entrer votre nom d'utilisateur : ");
            String userName = sc.nextLine();
            System.out.println("Veuillez entrer votre mot de passe : ");
            String password = sc.nextLine();
            //Try pour la gestion de l'exception SQLException en cas de compte ou mot de passe inexistant
            try {
                //Connection à la db avec les infos données par l'utilisateur
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", userName, password);
                //Boolean pour la condition d'arret de la boucle
                user = false;
                //Boucle pour le choix des fonctions utilisateur
                int quit = 0;
                while (quit == 0) {
                    System.out.println("Veuillez choisir une action (entrez un nombre de 1 à 10) : ");
                    System.out.println("0.  Quitter le programme ");
                    System.out.println("1.  Consulter RDV ");
                    System.out.println("2.  Enregistrer un patient ");
                    System.out.println("3.  Ajouter un rdv ");
                    System.out.println("4.  Entrer info sur un rdv suite à une consultation ");
                    System.out.println("5.  Modifier info patient ");
                    System.out.println("6.  Consulter RDV passés patient ");
                    int choice = sc.nextInt();

                    //Initialisation des variables de gestion jdbc
                    Statement st;
                    ResultSet rs;
                    String sql;
                    switch (choice) {
                        case 0 :
                            System.out.println("Êtes-vous sûr de vouloir quitter le programme ? (0 pour revenir) ");
                            quit = sc.nextInt();
                            break;
                        case 1:
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

                            String consultation;
                            System.out.println("Entrez la date que vous voulez vérifier sous la forme : yyyy/mm/dd (par ex : 2003/05/19) : ");
                            sc.nextLine();
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
                                        System.out.println("\nprenom : " + rs3.getString(2) + "\nnom : " + rs3.getString(3) + "\nage : " + (int)age +"\nadresse : " + rs3.getString(5) + "\n");
                                    }
                                }
                            }
                            if (occ==0)
                                System.out.println("Pas de RDV ce jour la ! ");
                            st.close();
                            st2.close();
                            st3.close();
                            break;
                        case 2 :
                            sc.nextLine();
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

                            //On ajoute le nouveau patient et sa profession dans la bdd
                            st = con.createStatement();
                            sql = "INSERT INTO \"Patient\"(\"Prenom\",\"Nom\",\"ConnaissancePsy\",\"Adresse\",\"DOB\",\"Sexe\") VALUES(" + prenomPatient + "," + nomPatient + "," + psy + "," + adresse + ",TO_DATE(" + dob + ", 'yyyy/mm/dd')," + sexe + ")";
                            st.executeQuery(sql);
                            sql = "INSERT INTO \"Profession\"(\"IDPatient\",\"Profession\") VALUES(pat_seq.currval," + prof + ")";
                            st.executeQuery(sql);

                            st.close();
                            break;
                        case 3 :
                            sc.nextLine();

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
                            break;
                        case 4:
                            sc.nextLine();

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
                            break;
                        case 5:
                            sc.nextLine();
                            System.out.println("Entrez le nom du patient dont vous voulez modifier les infos : ");
                            String nom = "'" + sc.nextLine() + "'";

                            System.out.println("Entrez le prenom du patient dont vous voulez modifier les infos  : ");
                            String prenom = "'" + sc.nextLine() + "'";

                            System.out.println("Entrez la nouvelle adresse du patient dont vous voulez modifier les infos  : ");
                            String newAdresse = "'" + sc.nextLine() + "'";

                            System.out.println("Entrez la nouvelle profession actuelle du patient : ");
                            String profession = "'" + sc.nextLine() + "'";

                            //On cherche l'id du patient pour lequel on veut modifier les données
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
                            st.executeQuery(sql);
                            st.close();
                            break;
                        case 6 :

                            sc.nextLine();
                            Statement state2;

                            ResultSet consultations;
                            System.out.println("Entrez le nom du patient dont vous voulez consulter les rdv : ");
                            String nomRdv = "'" + sc.nextLine() + "'";

                            System.out.println("Entrez le prenom du patient dont vous voulez consulter les rdv : ");
                            String prenomRdv = "'" + sc.nextLine() + "'";

                            //On cherche l'id du patient
                            st = con.createStatement();
                            sql = "SELECT \"ID\" FROM \"Patient\" WHERE \"Nom\" =" + nomRdv + " AND \"Prenom\" =" + prenomRdv;
                            rs = st.executeQuery(sql);
                            int idP=0;
                            while (rs.next()) {
                                idP = rs.getInt("ID");
                            }
                            //On cherche les consultations que ce patient a pris
                            sql = "SELECT \"IDConsultation\" FROM \"PatientConsultant\" WHERE \"IDPatient\" =" + idP + " ORDER BY \"IDConsultation\" DESC";
                            rs = st.executeQuery(sql);
                            int idC=0;
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
                            break;
                        default:
                            System.out.println("Vous avez entr� une option innexistante");
                    }
                }
                System.out.println("Au revoir !");
                con.close();
            } catch (SQLException e) {
                System.out.println("Erreur de connexion (le nom d'utilisateur ou le mot de passe est erron�) : " + e.getMessage());
            }
        } while (user);
    }
}
