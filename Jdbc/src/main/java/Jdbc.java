import java.sql.*;
import java.util.Scanner;

/**
 * 101010010  decode([01010101010] + pre)
 * tcp / udp/..
 *
 * question1: why do we need to close resources ?  => memory leak
 * question2: how to re-use connections ? => connection pool
 * question3: sql injection
 *
 * homework:
 *  1. create maven +  jdbc project
 *  2. create author m - m  book in database
 *  3. create jdbc demo
 *      insert / delete / update / select queries
 *  4. read ORM (hibernate)  (write it in txt file)
 *      a. lazy loading vs eager loading
 *      b. why ORM
 *  deadline : 9pm cdt Sunday
 */

public class Jdbc {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/test";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "Zxc11223344";
    static final String QUERY = "SELECT * FROM test.book";

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean flag = true;

        while (flag) {

        //=================================================
            System.out.println("//=================================================");
            System.out.println("Select option:");

            System.out.println("1. INSERT");
            System.out.println("2. DELETE");
            System.out.println("3. UPDATE");
            System.out.println("4. SELECT");
            System.out.println("5. PRINT");
            System.out.println("others. QUIT");
            System.out.println("//=================================================");
            Scanner scanner = new Scanner(System.in);

            String option = scanner.nextLine();

            int option_i = Integer.parseInt(option);

        //=================================================

            try {
                //STEP 2: Register JDBC driver -> DriverManager
                Class.forName("com.mysql.cj.jdbc.Driver");

                //STEP 3: Open a connection
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                conn.setAutoCommit(false);
                //STEP 4: Execute a query
                System.out.println("Creating statement...");
                String username = "";
                String password = "'xxx OR TRUE; DROP table'";
                String sql1;

                switch (option_i) {
                    case 1:
                        sql1 = "SELECT MAX(book_id) as c from test.book";

                        stmt = conn.prepareStatement(sql1);
                        ResultSet rs = stmt.executeQuery();
                        int num = 0;
                        while(rs.next()){
                            num = rs.getInt("c");
                        }
                        num++;


                        System.out.println("Enter book name: ");
                        String name = scanner.nextLine();


                        sql1 = "INSERT INTO test.book(book_id, book_name) VALUES(" + num +", \""+ name +"\")";
                        stmt = conn.prepareStatement(sql1);
                        int rsr = stmt.executeUpdate();


                        ResultSet read = stmt.executeQuery(QUERY);

                        while(read.next()){
                            //Display values
                            System.out.print("ID: " + read.getInt("book_id"));

                            System.out.println(", Last: " + read.getString("book_name"));
                        }

                        conn.commit();

                        break;



                    case 2:

                        stmt = conn.prepareStatement(QUERY);
                        ResultSet read2 = stmt.executeQuery();

                        while(read2.next()){
                            //Display values
                            System.out.print("ID: " + read2.getInt("book_id"));

                            System.out.println(", Last: " + read2.getString("book_name"));

                        }


                        System.out.println("//=================================================");

                        System.out.println("Select delete id:");
                        String option2 = scanner.nextLine();


                        sql1 = "DELETE FROM test.book WHERE book_id = " + option2;
                        stmt = conn.prepareStatement(sql1);;
                        stmt.executeUpdate(sql1);


                        conn.commit();
                        break;



                    case 3:
                        System.out.println("//=================================================");

                        System.out.println("Select update id:");
                        String option3 = scanner.nextLine();
                        System.out.println("Type update book name:");
                        String option4 = scanner.nextLine();

                        sql1 = "UPDATE test.book SET book_name = \""+ option4 +"\" WHERE book_id = " + option3;

                        stmt = conn.prepareStatement(sql1);;
                        stmt.executeUpdate(sql1);

                        ResultSet rss = stmt.executeQuery(QUERY);

                        while(rss.next()){
                            //Display values
                            System.out.print("ID: " + rss.getInt("book_id"));

                            System.out.println(", Last: " + rss.getString("book_name"));
                        }

                        rss.close();
                        conn.commit();
                        break;



                    case 4:
                        sql1 = "SELECT test.book.book_name b, test.author.author_name a FROM test.ab INNER JOIN test.book on book_id = test.ab.ab_b INNER JOIN test.author on author_id = test.ab.ab_a";

                        stmt = conn.prepareStatement(sql1);
                        ResultSet rsss = stmt.executeQuery();


                        while(rsss.next()){

                            System.out.print("BOOK Name: " + rsss.getString("b"));

                            System.out.println(", AUTHOR Name: " + rsss.getString("a"));
                        }

                        rsss.close();
                        conn.commit();
                        break;
                    case 5:
                        stmt = conn.prepareStatement(QUERY);
                        ResultSet rssss = stmt.executeQuery();

                        while(rssss.next()){

                            System.out.print("BOOK id: " + rssss.getString("book_id")+ ", " );

                            System.out.println("name: " + rssss.getString("book_name"));
                        }
                        rssss.close();
                        conn.commit();
                        break;
                    default:
                        System.exit(0);
                }


                stmt.close();
                conn.close();


            } catch (SQLException se) {
                //Handle errors for JDBC
                try {
                    conn.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                //finally block used to close resources
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException se2) {
                }// nothing we can do
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }//end finally try
            }//end try
        }//end main

    }
}