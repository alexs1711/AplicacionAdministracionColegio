package loginapp;

import dbUtil.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    Connection connection; /* this connection es igual a esta connection, para eso era el this*/

    public LoginModel(){ /*constructor comprobar si esta conectado a la db*/
        try {
            this.connection = dbConnection.getConnection();/*importas dbConnection para asi conectarte*/
        }
        catch
        (SQLException exception){
            exception.printStackTrace();
        }

        if(this.connection == null){
            System.exit(1);/**/
        }
    }

        public boolean isDatabaseConnected(){
            return this.connection != null;
        }

    public boolean isLogin(String user,String pass,String opt) throws Exception{
        PreparedStatement pr = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM login where username = ? and password = ? and division = ?";

        try{
            pr = this.connection.prepareStatement(sql);/*EL statement preparado lo tengo para hacer la consulta sin mandar los datos manualmente asi se tarda menos  , en las ?
                se introducen las variables recibidas por parametros con le setString*/
            pr.setString(1,user);
            pr.setString(2,pass);
            pr.setString(3,opt);

            rs = pr.executeQuery(); /*necesito recoger la consulta sql si no se harian cambios y no se verian*/

            boolean boll1;

            if(rs.next()){
                return true;
            }
            return false;
        }
        catch (SQLException excp) {
            return false;
        }

        finally {
            pr.close();
            rs.close();
        }
    }
}
