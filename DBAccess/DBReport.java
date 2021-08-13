package DBAccess;

import Database.DBConnection;
import Model.Appointment;
import Model.GenerateReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.KeyManagementException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DBReport {
    public static ObservableList<GenerateReport> getPlanningType(){
        ObservableList<GenerateReport>  planingType= FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps= DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                String type=rs.getString("Type");



            GenerateReport planning=new GenerateReport(type);
            planingType.add(planning);


            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return planingType;
    }
    public static ObservableList<GenerateReport> getMayApps(){
        ObservableList<GenerateReport> mayAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String May=rs.getString("Start");
                if(May .startsWith("05", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String may=rs.getString("Start");
                        GenerateReport May1=new GenerateReport(may);
                        mayAppointments.add(May1);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mayAppointments;
    }
    public static ObservableList<GenerateReport> getJanApps(){
        ObservableList<GenerateReport> janApps=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String Jan=rs.getString("Start");
                if(Jan.startsWith("01", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String january=rs.getString("Start");
                        GenerateReport May1=new GenerateReport(january);
                        janApps.add(May1);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return janApps;
    }
    public static ObservableList<GenerateReport> getFebApps(){
        ObservableList<GenerateReport> febAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Start");
                if(start.startsWith("02", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String Feb=rs.getString("Start");
                        GenerateReport feb=new GenerateReport(Feb);
                        febAppointments.add(feb);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return febAppointments;
    }

    public static ObservableList<GenerateReport> getMarApps(){
        ObservableList<GenerateReport> marAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Start");
                if(start.startsWith("03", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String March=rs.getString("Start");
                        GenerateReport mar=new GenerateReport(March);
                        marAppointments.add(mar);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return marAppointments;
    }
    public static ObservableList<GenerateReport> getAprApps(){
        ObservableList<GenerateReport> aprAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Start");
                if(start.startsWith("04", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                            String April=rs.getString("Start");
                        GenerateReport apr=new GenerateReport(April);
                        aprAppointments.add(apr);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return aprAppointments;
    }
    public static ObservableList<GenerateReport> getJunApps(){
        ObservableList<GenerateReport> juneAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Start");
                if(start.startsWith("06", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String June=rs.getString("Start");
                        GenerateReport june=new GenerateReport(June);
                        juneAppointments.add(june);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return juneAppointments;
    }
    public static ObservableList<GenerateReport> getJulyApps(){
        ObservableList<GenerateReport> julyAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Start");
                if(start.startsWith("07", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String July=rs.getString("Start");
                        GenerateReport july=new GenerateReport(July);
                        julyAppointments.add(july);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return julyAppointments;
    }
    public static ObservableList<GenerateReport> getAugApps(){
        ObservableList<GenerateReport> augAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Start");
                if(start.startsWith("08", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String Aug=rs.getString("Start");
                        GenerateReport aug=new GenerateReport(Aug);
                        augAppointments.add(aug);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return augAppointments;
    }
    public static ObservableList<GenerateReport> getSepApps(){
        ObservableList<GenerateReport> sepAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Start");
                if(start.startsWith("09", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String Sep=rs.getString("Start");
                        GenerateReport sep=new GenerateReport(Sep);
                        sepAppointments.add(sep);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sepAppointments;
    }
    public static ObservableList<GenerateReport> getOctApps(){
        ObservableList<GenerateReport> octAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Start");
                if(start.startsWith("10", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String Oct=rs.getString("Start");
                        GenerateReport oct=new GenerateReport(Oct);
                        octAppointments.add(oct);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return octAppointments;
    }
    public static ObservableList<GenerateReport> getNovApps(){
        ObservableList<GenerateReport> novAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Start");
                if(start.startsWith("11", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String Nov=rs.getString("Start");
                        GenerateReport nov=new GenerateReport(Nov);
                        novAppointments.add(nov);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return novAppointments;
    }
    public static ObservableList<GenerateReport> getDecApps(){
        ObservableList<GenerateReport> decAppointments=FXCollections.observableArrayList();
        try{
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Start");
                if(start.startsWith("12", 5)){
                    String query="Select Count(Start) as Start from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String Dec=rs.getString("Start");
                        GenerateReport dec=new GenerateReport(Dec);
                        decAppointments.add(dec);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decAppointments;
    }
    public static ObservableList<GenerateReport> getModifiedToday(){
        ObservableList<GenerateReport> mod=FXCollections.observableArrayList();
        try{
            LocalDate date=LocalDate.now();
            String sql="Select * from appointments";
            PreparedStatement ps=DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String start=rs.getString("Last_Update");
                if(start.startsWith(String.valueOf(date))){
                    String query="Select Count(Last_Update) as Last_Update from appointments";
                    PreparedStatement prs=DBConnection.getConnection().prepareStatement(query);
                    ResultSet res=prs.executeQuery();
                    while (res.next()){
                        String modified=rs.getString("Last_Update");
                        GenerateReport Modified=new GenerateReport(modified);
                        mod.add(Modified);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mod;
    }


}
