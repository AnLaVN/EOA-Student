package Controller;
//Make by Thành Đạt || Dat09
//Edit by Bình An || AnLaVN || KatoVN

import static Controller.LocalData.CurrentBT;
import static Controller.LocalData.Lang;
import static Controller.LocalData.ParentComponent;
import static Controller.LocalData.arrCH;
import DataAccessObject.BaoCaoDAO;
import DataAccessObject.BuoiThiDAO;
import DataAccessObject.CauHoiDAO;
import DataAccessObject.DeThiDAO;
import DataAccessObject.GiangVienDAO;
import DataAccessObject.LopDAO;
import DataAccessObject.SinhVienDAO;
import ObjectClass.CauHoi;
import com.AnLa.FILE.Log;
import com.AnLa.JDBC.SQL;
import com.AnLa.UI.Mode;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DatabaseData {
    private static final String //URL = "jdbc:sqlserver://localhost:1433;DatabaseName=EOA;encrypt=true;trustServerCertificate=true",//"jdbc:sqlserver://AChatDB.mssql.somee.com;DatabaseName=EOA;encrypt=true;trustServerCertificate=true"
                                
    
    public static final SQL EOA_DB = new SQL(URL, DB_USERNAME, DB_PASSWORD);
    
    public static GiangVienDAO GVDAO = new GiangVienDAO();
    
    public static LopDAO LDAO = new LopDAO();
    public static DeThiDAO DTDAO = new DeThiDAO();
    public static BuoiThiDAO BTDAO = new BuoiThiDAO();
    public static BaoCaoDAO BCDAO = new BaoCaoDAO();
    public static SinhVienDAO SVDAO = new SinhVienDAO();
    public static CauHoiDAO CHDAO = new CauHoiDAO();
    
    public static void DB_Connect(){
        try{    EOA_DB.Connect();   }
        catch(Exception ex){
            Mode.WMessage(ParentComponent, Lang.getString("DBError").replaceFirst("\\.", ".\n"), Lang.getString("Notifi"), JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    private static PreparedStatement get_PreparedStatement(String query, Object... args) throws SQLException{
        PreparedStatement ps = null;
        if (query.trim().startsWith("{"))   {   ps = EOA_DB.getConnect().prepareCall(query);        }
        else                                {   ps = EOA_DB.getConnect().prepareStatement(query);   }
        for(int i = 0; i < args.length; i++){   ps.setObject(i + 1, args[i]);                       }
        return ps;
    }
    
    public static void DB_Update(String query, Object... args){
        try {
            PreparedStatement ps = get_PreparedStatement(query, args);
            ps.executeUpdate(); }
        catch (SQLException ex) {
            Log.add("!!! Error try to execute update query: " + query);
            Logger.getLogger(DatabaseData.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
    }

    public static ResultSet DB_Query(String query, Object... args) {
        try {
            PreparedStatement ps = get_PreparedStatement(query, args);
            return ps.executeQuery();   }
        catch (SQLException ex) {
            Log.add("!!! Error try to execute query: " + query);
            Logger.getLogger(DatabaseData.class.getName()).log(Level.SEVERE, null, ex); 
            throw new RuntimeException();
        }
    }
    
    public static final ArrayList<CauHoi>   arrA = new ArrayList<>(),
                                            arrB = new ArrayList<>(),
                                            arrC = new ArrayList<>(),
                                            arrD = new ArrayList<>();
    public static void getSentences(){
        arrCH = CHDAO.selectAllByDeThi(CurrentBT.getIDDeThi());
        for(CauHoi ch : arrCH){
            if(ch.getLevel() == 0){arrA.add(ch);}
            if(ch.getLevel() == 1){arrB.add(ch);}
            if(ch.getLevel() == 2){arrC.add(ch);}
            if(ch.getLevel() == 3){arrD.add(ch);}
        }
        arrCH.clear();
        Double percent = DTDAO.selectById(CurrentBT.getIDDeThi()).getPercentT()/100.0;
        int temp = 0,   numA=(int) Math.rint(arrA.size()*percent),
                        numB=(int) Math.rint(arrB.size()*percent),
                        numC=(int) Math.rint(arrC.size()*percent),
                        numD=(int) Math.rint(arrD.size()*percent);
        Random ran = new Random();
        for(int i = 0 ; i < numA; i++){ temp = ran.nextInt(arrA.size()); arrCH.add(arrA.get(temp));  arrA.remove(temp);}
        for(int i = 0 ; i < numB; i++){ temp = ran.nextInt(arrB.size()); arrCH.add(arrB.get(temp));  arrB.remove(temp);}
        for(int i = 0 ; i < numC; i++){ temp = ran.nextInt(arrC.size()); arrCH.add(arrC.get(temp));  arrC.remove(temp);}
        for(int i = 0 ; i < numD; i++){ temp = ran.nextInt(arrD.size()); arrCH.add(arrD.get(temp));  arrD.remove(temp);}
        Collections.shuffle(arrCH);
    }
}