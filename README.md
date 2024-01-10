## Pharmacy management system
- Login
  
  ```bash
  //login method
    public Employees loginQuery(String user, String password) {
        String query = "SELECT * FROM employees WHERE username = ? AND password = ?";
        Employees employee = new Employees();

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            //send parameters
            pst.setString(1, user);
            pst.setString(2, password);
            rs = pst.executeQuery();

            if (rs.next()) {
                employee.setId(rs.getInt("id"));
                id_user = employee.getId();
                employee.setFull_name(rs.getString("full_name"));
                full_name_user = employee.getFull_name();
                employee.setUsername(rs.getString("username"));
                username_user = employee.getUsername();
                employee.setAddress(rs.getString("address"));
                address_user = employee.getAddress();
                employee.setTelephone(rs.getString("telephone"));
                telephone_user = employee.getTelephone();
                employee.setEmail(rs.getString("email"));
                email_user = employee.getEmail();
                employee.setRol(rs.getString("rol"));
                rol_user = employee.getRol();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error getting employee " + e);
        }
        return employee;
    }
  ```

- Instance connection mysql
    
   ```bash
     public class ConnectionMySQL {
    
        private String database_name = "pharmacy_database";
        private String user = "root";
        private String password = "root";
        private String url = "jdbc:mysql://0.0.0.0:3306/" + database_name;
        Connection conn = null;
    
        public Connection getConnection() {
            try {
                // get value from driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                // get the connection
                conn = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                System.err.println("has occurred ClassNotFoundException " + e.getMessage());
            }catch(SQLException e) {
                System.err.println("has occurred SQLException " + e.getMessage());
            }
            return conn;
        }
    }
    ```

- Library

   ![Captura de pantalla 2024-01-09 210058](https://github.com/hermezHK/Pharmacy---SWING---MYSQL/assets/113315995/ac867091-1617-46bf-882b-8c1a2dd820a5)

- Implemented methods:
  
  - login creation
  - register, search, modify, delete employees
  - register, search, modify, delete clients
  - register, search, modify, delete suppliers
  - register, search, modify, delete categories
  - register, search, modify, delete products
  - register, search, modify, delete, report purchases
  - register, search, modify, delete, report sales
