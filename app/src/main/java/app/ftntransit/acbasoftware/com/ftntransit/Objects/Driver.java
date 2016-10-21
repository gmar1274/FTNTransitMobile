package app.ftntransit.acbasoftware.com.ftntransit.Objects;

/**
 * Created by user on 2016-10-21.
 */
public class Driver {
    private long user_id,driver_id;
    private String name,fname,mname,lname;
    public Driver(long user_id,long id,String f, String m, String l){
        this.user_id=user_id;
        this.driver_id=id;
        this.fname=f;
        this.mname=m;
        this.lname=l;
        this.name=formatName();
    }
    public String getName(){return this.name;}
    public String formatName(){
        String n="";
        if(mname==null || mname.length()==0){
            n=fname+" "+lname;
        }else{
           n=fname+" "+mname+" "+lname;
        }
        return n;
    }
    public String getFName(){return this.fname;}
    public String getMName(){return this.mname;}
    public String getLName(){return this.lname;}
    public long getUserID(){return this.user_id;}
    public long getDriverID(){return this.driver_id;}
    public String toString(){return "ID: "+this.driver_id+" Name: "+this.getName()+" User ID: "+this.user_id;}
}
