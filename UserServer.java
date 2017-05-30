/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author pcwang
 */
public class UserServer extends Server{

    
    public UserServer() {
        super();
    }
   
   
    @Override
    public void run(){
        if(requestType.equalsIgnoreCase("USER_LOGIN")){
            login();
        }
        if(requestType.equalsIgnoreCase("USER_REGISTE")){
            registe();
        }
        if(requestType.equalsIgnoreCase("USER_PSMESSAGE")){
            psmessage();
        }
        if(requestType.equalsIgnoreCase("USER_CHECK")){
            check();
        }
        if(requestType.equalsIgnoreCase("USER_GETMESSAGE")){
            getmessage();
        }
    }

    private void login() {
        
        try {
            String sql = "select count(1) from sn_user where username=? and password=md5(?)";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, data.getString("username"));
            st.setString(2, data.getString("password"));
            ResultSet rs = st.executeQuery();
            rs.next();
            String username = data.getString("username");
            HttpSession session = request.getSession();
            session.setAttribute("HASLOGIN",username);
            //response.getWriter().println(session.getAttribute("HASLOGIN"));
            if(rs.getInt(1) ==1){
                this.makeResponse(true, "登录成功啦", null);  
            }
            else{
                this.makeResponse(false,"Login Failed!测试中文",null);
            }
               
               
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    private void registe() {
        try{
            String sql1 = "select count(1) from sn_user where username=? or nickname = ?";
            PreparedStatement st1 = connection.prepareStatement(sql1);
            st1.setString(1, data.getString("username"));
            st1.setString(2, data.getString("nickname"));
            ResultSet rs1 = st1.executeQuery();
            rs1.next();
            if(rs1.getInt(1) == 1){
                this.makeResponse(false, "Registe Failed!123", null);
            }else{
                
                 String sql = "insert into sn_user(username,nickname,password) values (?,?,md5(?))";
                 PreparedStatement st = connection.prepareStatement(sql);
                 String username = data.getString("username");
                 String nickname = data.getString("nickname");
                 String password = data.getString("password");
                 st.setString(1, username);
                 st.setString(2, nickname);
                 st.setString(3, password);
                
                 if(username.length() !=0 && nickname.length() != 0 && password.length() != 0){
                      int rs = st.executeUpdate();
                      this.makeResponse(true,"Registe Success!465",null);
                 } else {
                      this.makeResponse(false, "Can't be empty!", null);
                 }
            }
          
          
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void psmessage(){
        try{
             HttpSession session = request.getSession(false);
             String curusername = (String) session.getAttribute("HASLOGIN");
             String sql1 = "select count(1) from sn_personmessage where username = ?";
             PreparedStatement st1 = connection.prepareStatement(sql1);
             st1.setString(1, curusername);
             ResultSet rs1 = st1.executeQuery();
             rs1.next();
             if(rs1.getInt(1) == 1){
                String sql2 = "update sn_personmessage set gender = ?,introduce = ?,identity = ?,major = ?,phone = ? where username = ?";
                PreparedStatement st2 = connection.prepareStatement(sql2);
                st2.setString(1, data.getString("gender"));
                st2.setString(2, data.getString("introduce"));
                st2.setString(3, data.getString("identity"));
                st2.setString(4, data.getString("major"));
                st2.setString(5, data.getString("phone"));
                st2.setString(6, curusername);
                int rs = st2.executeUpdate();
                this.makeResponse(false, "Alter Success!", null);
            }else{
             String sql = "insert into sn_personmessage(username,gender,introduce,identity,major,phone) values (?,?,?,?,?,?)";
             PreparedStatement st = connection.prepareStatement(sql);
             st.setString(1, curusername);
             st.setString(2, data.getString("gender"));
             st.setString(3, data.getString("introduce"));
             st.setString(4, data.getString("identity"));
             st.setString(5, data.getString("major"));
             st.setString(6, data.getString("phone"));
             int rs = st.executeUpdate();
             this.makeResponse(true,"UpLoad Success!",null);
             }
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void check() {
        HttpSession session = request.getSession(true);
        Object curusername = session.getAttribute("HASLOGIN");
        if(curusername == null){
            this.makeResponse(false, "Please Login First", null);
        } else {
            this.makeResponse(true, "HASLOGIN", null);
        }
        session.invalidate();
       } 
    
    
    private void getmessage(){
        try{
            HttpSession session = request.getSession(false);
            String curusername = (String) session.getAttribute("HASLOGIN");
            
            String sql = "select gender,introduce,identity,major,phone from sn_personmessage where username = ?";
            PreparedStatement st = connection.prepareStatement(sql);
           
            st.setString(1, curusername);
            ResultSet rs = st.executeQuery();
            
            while(rs.next()){
            String gender = rs.getString("gender");
            String introduce = rs.getString("introduce");
            String identity = rs.getString("identity");
            String major = rs.getString("major");
            String phone = rs.getString("phone");
            response.getWriter().println(phone);
            this.makeResponse(true, "GetMessage Success!", data);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
