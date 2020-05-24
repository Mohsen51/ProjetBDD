import java.sql.*;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws Exception{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Scanner sc = new Scanner(System.in);
        Boolean user = true;
        do {
            System.out.println("Veuillez entrer votre nom d'utilisateur : ");
            String userName = sc.nextLine();
            System.out.println("Veuillez entrer votre mot de passe : ");
            String password = sc.nextLine();
            try {
                //Connection to the database with the info provided by the user
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", userName, password);
                user = false;
                int quit = 0;
                while (quit == 0) {
                    System.out.println("Veuillez choisir une action (entrez un nombre de 1 à 10) : ");
                    System.out.println("0.  Quitter le programme ");
                    System.out.println("1.  Consulter RDV ");
                    System.out.println("2.  Enregistrer un patient ");
                    System.out.println("3.  Ajouter un rdv ");
                    System.out.println("4.  Entrer info sur un rdv ");
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

                            rs = st.executeQuery("select * from \"Patient\"");

                            while (rs.next())
                                System.out.println("ID : " + rs.getInt(2) + " prenom : " + rs.getString(4) + " nom : " + rs.getString(3) + " age : " + rs.getInt(1) + " " + rs.getString(5));

                            rs.close();
                            st.close();
                            break;
                        case 2 :
                            sc.nextLine();
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

                            st = con.createStatement();
                            sql = "INSERT INTO \"Patient\"(\"Prenom\",\"Nom\",\"ConnaissancePsy\",\"Adresse\",\"DOB\") VALUES(" + prenomPatient + "," + nomPatient + "," + psy + "," + adresse + ",TO_DATE(" + dob + ", 'yyyy/mm/dd'))";
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
                            sql = "UPDATE \"Consultation\" SET \"Prix\"=" + prix + ",\"Paiement\"=" + paiement + ",\"Note\"=" + note + ",\"Anxiete\"=" + anxiete + " WHERE IDConsultation=cons_seq.currval";

                            st.close();
                            break;
                        case 5:
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
                            sql = "UPDATE \"Patient\" SET \"adresse\"=" + newAdresse + " WHERE ID = " + idPatient;
                            st.executeQuery(sql);
                            sql = "INSERT INTO \"Profession\"(\"IDPatient\",\"Profession\") VALUES(" + idPatient + "," + profession + ")";
                            st.executeQuery(sql);
                            st.close();
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
