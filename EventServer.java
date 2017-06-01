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
        if(requestType.equalsIgnoreCase("EVENT_UPLOAD1")){
            eventupload1();
        }
        if(requestType.equalsIgnoreCase("EVENT_UPLOAD2")){
            eventupload2();
        }
        if(requestType.equalsIgnoreCase("EVENT_GETMESSAGE")){
            getmessage();
        }
        if(requestType.equalsIgnoreCase("EVENT_ALTER")){
            alter();
        }
        if(requestType.equalsIgnoreCase("EVENT_QUIT")){
            quit();
        }
    }
    
    private void quit(){
            HttpSession eventsession = request.getSession(false); 
            
            eventsession.removeAttribute("EVENT");
            this.makeResponse(true,"事件编辑结束",null); 
    }
    
    
    private void eventupload1(){
        try{
                 HttpSession session = request.getSession(false);
                 String curusername = (String) session.getAttribute("HASLOGIN");
                 String sql = "insert into sn_event(username,happentime,title,event,label) values (?,?,?,'',true)";
                 PreparedStatement st = connection.prepareStatement(sql); 
                 st.setString(1, curusername);
                 Timestamp time = Timestamp.valueOf(data.getString("happentime"));
                 st.setTimestamp(2, time);
                 st.setString(3, data.getString("title"));
                 int rs = st.executeUpdate();
                 
                 
                 String sql1="select max(eventid) from sn_event";
                 PreparedStatement st1 = connection.prepareStatement(sql1);
                 ResultSet rs1 = st1.executeQuery();
                 rs1.next();
                 String eventid = rs1.getString("max");
                 this.makeResponse(true,"标题和时间上传成功!",null);
                 
                 HttpSession eventsession = request.getSession(true);
                 eventsession.setAttribute("EVENT", eventid);
                 
         } catch (SQLException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JSONException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         } 
    }
    
    private void eventupload2(){
         try{
             HttpSession eventsession = request.getSession(false);
             String cureventid = (String)eventsession.getAttribute("EVENT");
             String sql = "update sn_event set event = ?,longitude = ?,latitude = ? where eventid::varchar = ?";
             PreparedStatement st = connection.prepareStatement(sql);
             st.setString(1, data.getString("event"));
             st.setDouble(2, data.getDouble("longitude"));
             st.setDouble(3, data.getDouble("latitude"));
             st.setString(4, cureventid);
             
             int rs = st.executeUpdate();
             this.makeResponse(true, "事件上传成功!", null);
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void getmessage(){
        try{
            HttpSession session = request.getSession(false);
            String curusername = (String) session.getAttribute("HASLOGIN");
            
            String sql = "select happentime,event,title ,longitude,latitude,title from sn_event where username = ?";
            PreparedStatement st = connection.prepareStatement(sql);
           
            st.setString(1, curusername);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
            String happentime = rs.getString(1);
            String event = rs.getString(2);
            String title = rs.getString(3);
            String longitude = rs.getString(4);
            String latitude = rs.getString(5);
            for(int i = 0;i<5;i++){
            
            data.put("happentime", happentime);
            data.put("event", event);
            data.put("longitude", longitude);
            data.put("latitude", latitude);
            data.put("title", title);
            break;
            }
            response.getWriter().println(happentime);
            
            this.makeResponse(true, "GetMessage Success!", data);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    private void alter(){
         try{
             HttpSession eventsession = request.getSession(false);
             String cureventid = (String) eventsession.getAttribute("EVENT");
             String sql = "update sn_event set happentime = ?,event = ?,title = ?,longitude = ?,latitude = ? where eventid = ?";
             PreparedStatement st = connection.prepareStatement(sql);
             st.setString(1, data.getString("gender"));
             st.setString(2, data.getString("introduce"));
             st.setString(3, data.getString("identity"));
             st.setString(4, data.getString("major"));
             st.setString(5, data.getString("phone"));
             st.setString(6, cureventid);
             int rs = st.executeUpdate();
             this.makeResponse(true, "Alter Success!", null);
        } catch (SQLException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
