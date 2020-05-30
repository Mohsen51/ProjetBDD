import javax.swing.plaf.nimbus.State;
import java.sql.*;
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
                    int choice = sc.nextInt();
                    Statement st;
                    ResultSet rs;
                    String sql;
                    switch (choice) {
                        case 0 :
                            System.out.println("Êtes-vous sûr de vouloir quitter le programme ? (0 pour revenir) ");
                            quit = sc.nextInt();
                            break;
                        case 1:
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
                            rs = st.executeQuery("SELECT * FROM \"Consultation\" WHERE trunc(\"DateRDV\")= to_date('2020/06/10','yyyy/mm/dd') ");
                            int idCorres=0;
                            float age=0;
                            while (rs.next()) {
                                idCorres = rs.getInt("IDConsultation");
                                rs2 = st2.executeQuery("SELECT \"IDPatient\" from \"PatientConsultant\" WHERE \"IDConsultation\" = " + idCorres);
                                while (rs2.next()) {
                                    patientId = rs2.getInt("IDPatient");
                                    rs3 = st3.executeQuery("SELECT * FROM \"Patient\" WHERE \"ID\" = " + patientId);
                                    System.out.println("Consultation à la date du : " + rs.getDate("DateRDV"));
                                    while (rs3.next()) {
                                        rs4 = st4.executeQuery("SELECt months_between(TRUNC(sysdate), to_date('" + rs3.getDate("DOB") + "','yyyy/mm/dd'))/12 AS age FROM DUAL");
                                        while (rs4.next())
                                            age = rs4.getFloat("AGE");
                                        if (age<12)
                                            System.out.println("Consultation d'un enfant : ");
                                        if (age>=12 && age<18)
                                            System.out.println("Consultation d'un ado : ");
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

                            st = con.createStatement();
                            sql = "INSERT INTO \"Patient\"(\"Prenom\",\"Nom\",\"ConnaissancePsy\",\"Adresse\",\"DOB\",\"Sexe\") VALUES(" + prenomPatient + "," + nomPatient + "," + psy + "," + adresse + ",TO_DATE(" + dob + ", 'yyyy/mm/dd')," + sexe + ")";
                            st.executeQuery(sql);
                            sql = "INSERT INTO \"Profession\"(\"IDPatient\",\"Profession\") VALUES(pat_seq.currval," + prof + ")";
                            st.executeQuery(sql);

                            st.close();
                            break;
                        case 3 :
                            sc.nextLine();

                            System.out.println("Entrez la date du RDV sous la forme : yyyy/mm/dd hh24:mi (par ex : 2003/05/19 16:00) : ");
                            String date = "'" + sc.nextLine() + "'";

                            st = con.createStatement();
                            sql = "INSERT INTO \"Consultation\"(\"DateRDV\", \"Couple\") VALUES(TO_DATE(" + date + ", 'yyyy/mm/dd hh24:mi'),0)";
                            st.executeQuery(sql);

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

                                sql = "SELECT ID FROM \"Patient\" WHERE \"Nom\" =" + nom + " AND \"Prenom\" =" + prenom;
                                rs = st.executeQuery(sql);
                                while (rs.next()) {
                                    id = rs.getInt("ID");
                                }
                                sql= "INSERT INTO \"PatientConsultant\" VALUES(cons_seq.currval,"+ id + ")";
                                st.executeQuery(sql);
                            }

                            if (nb==2) {
                                System.out.println("Consultation en couple ? (1 si oui 0 sinon) : ");
                                int couple = sc.nextInt();
                                if (couple == 1) {
                                    sql = "UPDATE \"Consultation\" SET \"Couple\"=" + couple + " WHERE IDConsultation = cons_seq.currval";
                                }
                            }

                            st.close();
                            break;
                        case 4:
                            sc.nextLine();
                            System.out.println("Entrez le prix qu'a coûté la consultation : ");
                            int prix = sc.nextInt();
                            sc.nextLine();

                            System.out.println("Quel a été le moyen de paiement du patient : ");
                            String paiement = "'" + sc.nextLine() + "'";

                            System.out.println("Quels sont vos commentaires sur cette consultation : ");
                            String note = "'" + sc.nextLine() + "'";

                            System.out.println("Quel était le degré d'anxieté du patient ? : ");
                            int anxiete = sc.nextInt();

                            st = con.createStatement();
                            sql = "UPDATE \"Consultation\" SET \"Prix\"=" + prix + ",\"Paiement\"=" + paiement + ",\"Note\"=" + note + ",\"Anxiete\"=" + anxiete + " WHERE \"IDConsultation\" = cons_seq.currval";

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

                            st = con.createStatement();
                            sql = "SELECT ID FROM \"Patient\" WHERE \"Nom\" =" + nom + " AND \"Prenom\" =" + prenom;
                            rs = st.executeQuery(sql);
                            int idPatient = 0;
                            while (rs.next()) {
                                idPatient = rs.getInt("ID");
                            }
                            System.out.println(idPatient);
                            sql = "UPDATE \"Patient\" SET \"Adresse\"=" + newAdresse + " WHERE ID = " + idPatient;
                            st.executeQuery(sql);
                            sql = "INSERT INTO \"Profession\"(\"IDPatient\",\"Profession\") VALUES(" + idPatient + "," + profession + ")";
                            st.executeQuery(sql);
                            st.close();
                            break;
                        default:
                            System.out.println("Vous avez entré une option innexistante");
                    }
                }
                System.out.println("Au revoir !");
                con.close();
            } catch (SQLException e) {
                System.out.println("Erreur de connexion (le nom d'utilisateur ou le mot de passe est erroné) : " + e.getMessage());
            }
        } while (user);
    }
}
