/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author pcwang
 */
public class Server {

    protected HttpServletRequest request=null;
    protected HttpServletResponse response = null;
    protected JSONObject requestObj = null;
    protected JSONObject responseObj = null;
    protected JSONObject data = null;
    protected String requestType="UNKNOWN";
    
    protected Connection connection = null;
    
    public Server() {
        responseObj = new JSONObject();
        try {
            responseObj.put("success",false);
            responseObj.put("message", "Unknown Error");
            
            
            
            
        } catch (JSONException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public void setRequestAndResponse(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }
    
    
    public void setRequest(JSONObject obj){
        this.requestObj = obj;
        try {
            requestType = requestObj.getString("type");
            data = requestObj.getJSONObject("data"); 
        } catch (JSONException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JSONObject getResponse(){
        return this.responseObj;
    }
    
    public void run(){
        
    }
    
    public void makeResponse(boolean success, String message, JSONObject data){
        try {
            responseObj.put("success", success);
            responseObj.put("message", message);
            responseObj.put("data",data);
        } catch (JSONException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connectToDB(){
        try {
            Class.forName("org.postgresql.Driver");
            String url="jdbc:postgresql://127.0.0.1:5432/schoolnewsdb";
            this.connection = DriverManager.getConnection(url,"mwd","mwd19950308");
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void closeDB(){
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void beginTransaction(){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void endTansaction(){
        try {
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
