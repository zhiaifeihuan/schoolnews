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
import org.json.JSONArray;
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
//        if(requestType.equalsIgnoreCase("USER_UPLOAD")){
//            upload();
//        }
        if(requestType.equalsIgnoreCase("USER_ALTER")){
            alter();
        }
        
        if(requestType.equalsIgnoreCase("USER_CHECK")){
            check();
        }
        if(requestType.equalsIgnoreCase("USER_GETMESSAGE")){
            getmessage();
        }
        if(requestType.equalsIgnoreCase("USER_QUIT")){
            quit();
        }
        if(requestType.equalsIgnoreCase("USER_GETEVENT")){
            getevent();
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
            
            //response.getWriter().println(session.getAttribute("HASLOGIN"));
            if(rs.getInt(1) ==1){
                this.makeResponse(true, "登录成功啦", null); 
                HttpSession session = request.getSession();
                session.setAttribute("HASLOGIN",username);
            }
            else{
                this.makeResponse(false,"登录失败，用户名或密码不正确",null);
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
                this.makeResponse(false, "用户已存在!", null);
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
                      this.makeResponse(true,"注册成功!",null);
                 } else {
                      this.makeResponse(false, "用户名，昵称或密码不能为空!", null);
                 }
            }
          
          
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    private void upload(){
//        try{
//             HttpSession session = request.getSession(false);
//             String curusername = (String) session.getAttribute("HASLOGIN");
//             
//             String sql = "insert into sn_personmessage(username,gender,introduce,identity,major,phone) values (?,?,?,?,?,?)";
//             PreparedStatement st = connection.prepareStatement(sql);
//             st.setString(1, curusername);
//             st.setString(2, data.getString("gender"));
//             st.setString(3, data.getString("introduce"));
//             st.setString(4, data.getString("identity"));
//             st.setString(5, data.getString("major"));
//             st.setString(6, data.getString("phone"));
//             int rs = st.executeUpdate();
//             this.makeResponse(true,"UpLoad Success!",null);
//             
//        } catch (SQLException ex) {
//            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (JSONException ex) {
//            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    private void check() {
        HttpSession session = request.getSession(true);
        Object curusername = session.getAttribute("HASLOGIN");
        if(curusername == null){
            this.makeResponse(false, "请先登录!", null);
        } else {
            this.makeResponse(true, "已登录成功!", null);
        }
       
       } 
    
    
    private void getmessage(){
        try{
            HttpSession session = request.getSession(false);
            String curusername = (String) session.getAttribute("HASLOGIN");
            
            String sql = "select gender,introduce,identity,major,phone,nickname from sn_user where username = ? and label = true";
            PreparedStatement st = connection.prepareStatement(sql);
           
            st.setString(1, curusername);
            ResultSet rs = st.executeQuery();
            
            rs.next();
            String gender = rs.getString("gender");
            String introduce = rs.getString("introduce");
            String identity = rs.getString("identity");
            String major = rs.getString("major");
            String phone = rs.getString("phone");
            String nickname = rs.getString("nickname");
            data.put("gender", gender);
            data.put("introduce", introduce);
            data.put("identity", identity);
            data.put("major", major);
            data.put("phone", phone);
            data.put("nickname", nickname);
            
            String sql1 = "select count(username) from sn_event where username = ? and label = true";
            PreparedStatement st1 = connection.prepareStatement(sql1);
             
            st1.setString(1, curusername);
            
            ResultSet rs1 = st1.executeQuery();
            rs1.next();
            String count = rs1.getString("count");
            
            data.put("count", count);
            this.makeResponse(true, "获取个人信息成功!", data);
            
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void quit(){
       
            
            HttpSession session = request.getSession(false); 
            
            session.removeAttribute("HASLOGIN");
            this.makeResponse(true,"登出成功",null); 
    }
    
    private void alter(){
        try{
             HttpSession session = request.getSession(false);
             String curusername = (String) session.getAttribute("HASLOGIN");
             String sql = "update sn_user set gender = ?,introduce = ?,identity = ?,major = ?,phone = ?,nickname = ? where username = ?";
             PreparedStatement st = connection.prepareStatement(sql);
             st.setString(1, data.getString("gender"));
             st.setString(2, data.getString("introduce"));
             st.setString(3, data.getString("identity"));
             st.setString(4, data.getString("major"));
             st.setString(5, data.getString("phone"));
             st.setString(6, data.getString("nickname"));
             st.setString(7, curusername);
             int rs = st.executeUpdate();
             this.makeResponse(true, "修改个人信息成功!", null);
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getevent() {
         try{
            HttpSession session = request.getSession(false);
            String curusername = (String) session.getAttribute("HASLOGIN");
            String sql = "select happentime,title,eventid,viewd from sn_event where username = ? and label = true order by happentime DESC limit 5 offset ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, curusername);
            
            st.setInt(2,data.getInt("param")*5);
            ResultSet rs = st.executeQuery();

            JSONArray array = new JSONArray(); 
           
          
            
            while(rs.next()){
            JSONObject data1 = new JSONObject();
            String happentime = rs.getString(1);
            String title = rs.getString(2);
            String eventid = rs.getString(3);
            String viewd = rs.getString(4);
//            String longitude = rs.getString(3);
//            String latitude = rs.getString(4);
            data1.put("happentime", happentime);
//            data1.put("longitude", longitude);
//            data1.put("latitude", latitude);
            data1.put("title", title);
            data1.put("eventid", eventid);
            data1.put("viewd", viewd);
            
            array.put(data1);
            
            }

            responseObj.put("data", array);
            responseObj.put("success", true);
            responseObj.put("message", "事件获取成功!");
            
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
