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
import org.json.JSONException;
import org.json.JSONObject;

public class EventServer extends Server {
    
     public EventServer() {
        super();
    }
    
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
       
    }
    
    private void delete(){
        try {
             String sql = "delete from sn_event where eventid::varchar = ?";
         
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
                 String sql = "insert into sn_event(username,happentime,title,event,label,longitude,latitude) values (?,?,?,?,true,?,?)";
                 PreparedStatement st = connection.prepareStatement(sql); 
                 st.setString(1, curusername);
                 Timestamp time = Timestamp.valueOf(data.getString("happentime"));
                 st.setTimestamp(2, time);
                 st.setString(3, data.getString("title"));
                 st.setString(4, data.getString("event"));
                 st.setDouble(5, data.getDouble("longitude"));
                 st.setDouble(6, data.getDouble("latitude"));
                 
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
            
            String sql = "select happentime,event,title ,longitude,latitude,title from sn_event where eventid::varchar = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, data.getString("eventid"));
            ResultSet rs = st.executeQuery();
           
            rs.next();
            String happentime = rs.getString(1);
            String event = rs.getString(2);
            String title = rs.getString(3);
            String longitude = rs.getString(4);
            String latitude = rs.getString(5);
            
            data.put("happentime", happentime);
            data.put("event", event);
            data.put("longitude", longitude);
            data.put("latitude", latitude);
            data.put("title", title);
           
            this.makeResponse(true, "事件提取成功!", data);
            
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void alter(){
         try{
             
             String sql = "update sn_event set happentime = ?,event = ?,title = ?,longitude = ?,latitude = ? where eventid::varchar = ? and label = true";
             PreparedStatement st = connection.prepareStatement(sql);
             Timestamp time = Timestamp.valueOf(data.getString("happentime"));
             st.setTimestamp(1, time);
             st.setString(2, data.getString("event"));
             st.setString(3, data.getString("title"));
             st.setDouble(4, data.getDouble("longitude"));
             st.setDouble(5, data.getDouble("latitude"));
             st.setString(6, data.getString("eventid"));
             int rs = st.executeUpdate();
             this.makeResponse(true, "Alter Success!", null);
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
