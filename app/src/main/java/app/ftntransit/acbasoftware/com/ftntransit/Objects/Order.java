package app.ftntransit.acbasoftware.com.ftntransit.Objects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2016-10-21.
 */
public class Order {
    public long order_number, start_dest_id,end_dest_id,truck_id,customer_id,terminal_id;
    public String size,container,shipping_line,pickup_sku,delivery_sku,status;
    public BigDecimal driver_commission;
    public Date pickup_datetime;
   public String lfd;
    public Order(long on, long sdest, long edest, BigDecimal comm, long truck, long cust, String size, String cont, long terminal, String sl, Date pkdt, String pksku,String delsku,String lfd,String status){
        this.order_number=on;
        this.start_dest_id=sdest;
        this.end_dest_id=edest;
        this.driver_commission=comm;
        this.truck_id=truck;
        this.customer_id=cust;
        this.size=size;
        this.container=container;
        this.terminal_id=terminal;
        this.shipping_line=sl;
        this.pickup_datetime=pkdt;
        this.pickup_sku=pksku;
        this.delivery_sku=delsku;
        this.lfd=getLFD(lfd);
        this.status=status;
    }

    private String getLFD(String lfd) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String l = "Not yet determined".toUpperCase();
        if(lfd==null || lfd.contains("0000-00-00 00:00:00"))return l;
        try {
            l = format.parse(lfd).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    public String toString(){
        return this.order_number+" "+start_dest_id+" "+end_dest_id+" "+pickup_datetime+" ";
    }
}
