/*    */ package gps.track;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public class GPSTrackPoint
/*    */ {
/*    */   private double latitude;
/*    */   private double longitude;
/*    */   private double height;
/*    */   private Date date;
/*    */   
/*    */   public double getLatitude() {
/* 13 */     return this.latitude;
/*    */   }
/*    */   
/*    */   public void setLatitude(double latitude) {
/* 17 */     this.latitude = latitude;
/*    */   }
/*    */   
/*    */   public double getLongitude() {
/* 21 */     return this.longitude;
/*    */   }
/*    */   
/*    */   public void setLongitude(double longitude) {
/* 25 */     this.longitude = longitude;
/*    */   }
/*    */   
/*    */   public double getHeight() {
/* 29 */     return this.height;
/*    */   }
/*    */   
/*    */   public void setHeight(double height) {
/* 33 */     this.height = height;
/*    */   }
/*    */   
/*    */   public Date getDate() {
/* 37 */     return this.date;
/*    */   }
/*    */   
/*    */   public void setDate(Date date) {
/* 41 */     this.date = date;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTime() {
/* 46 */     return this.date.getTime();
/*    */   }
/*    */ 
/*    */   
/*    */   public GPSTrackPoint clone() {
/* 51 */     return new GPSTrackPoint(this.latitude, this.longitude, this.height, this.date);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GPSTrackPoint(double latitude, double longitude, double height, Date date) {
/* 58 */     this.latitude = latitude;
/* 59 */     this.longitude = longitude;
/* 60 */     this.height = height;
/* 61 */     this.date = date;
/*    */   }
/*    */ }


/* Location:              C:\dev\TZ\GPSTrackBuilder.jar!\gps\track\GPSTrackPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */