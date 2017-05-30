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
    }
    private void eventupload(){
        try{
                 HttpSession session = request.getSession(false);
                 String curusername = (String) session.getAttribute("HASLOGIN");
                 String sql = "insert into sn_event(username,event,happentime,positionx,positiony) values (?,?,?,?,?)";
                 PreparedStatement st = connection.prepareStatement(sql); 
                 st.setString(1, curusername);
                 st.setString(2, data.getString("event"));
                 Timestamp time = Timestamp.valueOf(data.getString("happentime"));
                 st.setTimestamp(3, time);
                 st.setDouble(4, Double.valueOf(data.getString("positionx")));
                 st.setDouble(5, Double.valueOf(data.getString("positiony")));
                 int rs = st.executeUpdate();
                 this.makeResponse(true,"UpLoad Success!",null);
         } catch (SQLException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JSONException ex) {
             Logger.getLogger(EventServer.class.getName()).log(Level.SEVERE, null, ex);
         } 
    }
    
}
