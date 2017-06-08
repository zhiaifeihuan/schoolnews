/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mwd
 */
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventServer extends Server {
    
     public EventServer() {
        super();
    }
    int i = 0;
    @Override
    public void run(){
        if(requestType.equalsIgnoreCase("EVENT_UPLOAD")){
            eventupload();
        }
       
        if(requestType.equalsIgnoreCase("EVENT_GETMESSAGE")){
            getmessage();
        }
        if(requestType.equalsIgnoreCase("EVENT_ALTER")){
            alter();
        }
        if(requestType.equalsIgnoreCase("EVENT_DELETE")){
            delete();
        }
        if(requestType.equalsIgnoreCase("EVENT_INIT")){
            eventinit();
        }
        if(requestType.equalsIgnoreCase("EVENT_SEARCH")){
            search();
        }
        if(requestType.equalsIgnoreCase("EVENT_GETHOTMESSAGE")){
            gethotmessage();
        }
       
    }
    
   
    private void gethotmessage(){
        try{
            HttpSession session = request.getSession(false);
            String curusername = (String) session.getAttribute("HASLOGIN");
            
            String sql = "select happentime,title,eventid,viewd from sn_event where username = ? and label = true order by viewd DESC limit 5";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, curusername);
            ResultSet rs = st.executeQuery();
            
            JSONArray array = new JSONArray();
            while(rs.next()){
            JSONObject data1 = new JSONObject();
            String happentime = rs.getString(1);
            String title = rs.getString(2);
            String eventid = rs.getString(3);
            String viewd = rs.getString(4);
            data1.put("happentime", happentime);
            data1.put("title", title);
            data1.put("eventid", eventid);
            data1.put("viewd", viewd);
            array.put(data1);
            
            }
            responseObj.put("data", array);
            responseObj.put("success", true);
            responseObj.put("message", "事件获取成功!");
            
            
        } catch (SQLException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JSONException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         }
    }      
    
    private void search(){
        try{
            if(data.getString("title").length() == 0){
                JSONArray array = new JSONArray();
                responseObj.put("data", array);
                responseObj.put("success", true);
                responseObj.put("message", "事件搜索成功!");
            }else {
           
            String sql = "select title,eventid from sn_event where title ~* ? and label = true order by viewd DESC";
            
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, data.getString("title"));
            ResultSet rs = st.executeQuery();
             
            JSONArray array = new JSONArray();
            while(rs.next()){
                JSONObject data1 = new JSONObject();
                String title = rs.getString(1);
                String eventid = rs.getString(2);
               
                data1.put("title", title);
                data1.put("eventid", eventid);
                array.put(data1);
            }
            responseObj.put("data", array);
            responseObj.put("success", true);
            responseObj.put("message", "事件搜索成功!");
            }
        } catch (SQLException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JSONException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    
    private void eventinit(){
       try{
           String sql = "select title,happentime,longitude,latitude,eventid from sn_event where happentime::varchar < ? and happentime::varchar > ?";
           PreparedStatement st = connection.prepareStatement(sql); 
           
//           Timestamp time = Timestamp.valueOf(data.getString("happentime1"));
//            response.getWriter().println(time);
//           st.setTimestamp(1, time);
          
//           Timestamp time1 = Timestamp.valueOf(data.getString("happentime2"));
//           st.setTimestamp(1, time1);
//           response.getWriter().println(time1);
           st.setString(1, data.getString("happentime2"));
           st.setString(2, data.getString("happentime1"));
           ResultSet rs = st.executeQuery();

            JSONArray array = new JSONArray(); 
           
          
            
            while(rs.next()){
            JSONObject data1 = new JSONObject();
            String title = rs.getString(1);
            String happentime = rs.getString(2);
            String longitude = rs.getString(3);
            String latitude = rs.getString(4);
            String eventid = rs.getString(5);
            
            data1.put("happentime", happentime);
            data1.put("longitude", longitude);
            data1.put("latitude", latitude);
            data1.put("title", title);
            data1.put("eventid", eventid);
            
            array.put(data1);
            
            }

            responseObj.put("data", array);
            responseObj.put("success", true);
            responseObj.put("message", "事件获取成功!");
       } catch (SQLException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JSONException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    private void delete(){
        try {
             String sql = "update sn_event set label = false where eventid::varchar = ?";
         
             PreparedStatement st = connection.prepareStatement(sql);
             st.setString(1,data.getString("eventid"));
             int rs = st.executeUpdate();
              this.makeResponse(true, "事件删除成功!",null);
         } catch (SQLException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JSONException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }
 
    
    
    private void eventupload(){
        try{
                 HttpSession session = request.getSession(false);
                 String curusername = (String) session.getAttribute("HASLOGIN");
                 String sql = "insert into sn_event(username,title,event,label,longitude,latitude,viewd) values (?,?,?,true,?,?,1)";
                 PreparedStatement st = connection.prepareStatement(sql); 
                 st.setString(1, curusername);
//                 Timestamp time = Timestamp.valueOf(data.getString("happentime"));
//                 st.setTimestamp(2, time);
                 st.setString(2, data.getString("title"));
                 st.setString(3, data.getString("event"));
                 st.setDouble(4, data.getDouble("longitude"));
                 st.setDouble(5, data.getDouble("latitude"));
                 
                 int rs = st.executeUpdate();
                 
                 
                 String sql1="select max(eventid) from sn_event";
                 PreparedStatement st1 = connection.prepareStatement(sql1);
                 ResultSet rs1 = st1.executeQuery();
                 rs1.next();
                 String eventid = rs1.getString("max");
                 JSONObject data1 = new JSONObject();
                 data1.put("eventid", eventid);
                 this.makeResponse(true, "事件上传成功!", data1);
                 
                 
         } catch (SQLException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JSONException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         } 
    }
    

    private void getmessage(){
        try{
            
            String sql = "select happentime,event,title,nickname,viewd from sn_event,sn_user where eventid::varchar = ? and sn_event.username = sn_user.username";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, data.getString("eventid"));
            ResultSet rs = st.executeQuery();
           
            rs.next();
            String happentime = rs.getString(1);
            String event = rs.getString(2);
            String title = rs.getString(3);
            String nickname = rs.getString(4);
            int viewd = rs.getInt(5);
            String viewd1 = String.valueOf(viewd);
            data.put("happentime", happentime);
            data.put("event", event);
            data.put("title", title);
            data.put("nickname", nickname);
            data.put("viewd", viewd1);
            
             
            String sql1 = "update sn_event set viewd = ? where eventid::varchar = ?";
            
            PreparedStatement st1 = connection.prepareStatement(sql1);
            
            st1.setInt(1,viewd+1);
            st1.setString(2, data.getString("eventid"));
            int rs1 = st1.executeUpdate();
            
            this.makeResponse(true, "事件提取成功!", data);
            
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void alter(){
         try{
             
             String sql = "update sn_event set event = ?,title = ?,longitude = ?,latitude = ? where eventid::varchar = ? and label = true";
             PreparedStatement st = connection.prepareStatement(sql);
//             Timestamp time = Timestamp.valueOf(data.getString("happentime"));
//             st.setTimestamp(1, time);
             st.setString(1, data.getString("event"));
             st.setString(2, data.getString("title"));
             st.setDouble(3, data.getDouble("longitude"));
             st.setDouble(4, data.getDouble("latitude"));
             st.setString(5, data.getString("eventid"));
             int rs = st.executeUpdate();
             this.makeResponse(true, "事件修改成功!", null);
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
