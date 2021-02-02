/*     */ package gps.track;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GPSTrack
/*     */ {
    /*  31 */
    /*     */
/*     */   private final String trackName;
/*     */   
/*     */   private final String desc;
/*     */   
/*     */   private final List<GPSTrackPoint> points;
/*     */   private static Random rand;

    /*     */
/*     */   public List<GPSTrackPoint> getPoints() {
/*  49 */     return this.points;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GPSTrack(String sourceFileName, String trackName, String desc) {
/*  56 */     this.trackName = trackName;
/*  57 */     this.desc = desc;
/*     */     
/*  59 */     this.points = new ArrayList<>();
/*     */     
/*  61 */     rand = new Random();
/*     */     
/*  63 */     parseGpx(sourceFileName, this.points);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseGpx(String sourceFileName, List<GPSTrackPoint> points) {
/*     */     try {
/*  73 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*     */       
/*  75 */       DocumentBuilder builder = factory.newDocumentBuilder();
/*     */       
/*  77 */       Document doc = builder.parse(new File(sourceFileName));
/*     */       
/*  79 */       doc.getDocumentElement().normalize();
/*     */       
/*  81 */       Element gpxElement = doc.getDocumentElement();
/*     */       
/*  83 */       NodeList trkptNodes = gpxElement.getElementsByTagName("trkpt");
/*     */ 
/*     */       
/*  86 */       for (int i = 0; i < trkptNodes.getLength(); i++) {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/*  91 */           Node trkptNode = trkptNodes.item(i);
/*  92 */           GPSTrackPoint point = makeTrackPointFromNode(trkptNode);
/*     */           
/*  94 */           if (point != null) {
/*  95 */             points.add(point);
/*     */           }
/*  97 */         } catch (ParseException e) {
/*     */           
/*  99 */           e.printStackTrace();
/*     */         }
/*     */       
/*     */       } 
/* 103 */     } catch (ParserConfigurationException|org.xml.sax.SAXException|java.io.IOException e) {
/*     */       
/* 105 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private GPSTrackPoint makeTrackPointFromNode(Node node) throws ParseException {
/* 113 */     if (node.getNodeType() != 1) {
/* 114 */       return null;
/*     */     }
/* 116 */     Element e = (Element)node;
/*     */     
/* 118 */     double lat = Double.parseDouble(e.getAttribute("lat"));
/*     */     
/* 120 */     double lon = Double.parseDouble(e.getAttribute("lon"));
/*     */     
/* 122 */     double height = 0.0D;
/*     */     
/* 124 */     Date date = null;
/*     */     
/* 126 */     NodeList heightValues = e.getElementsByTagName("ele");
/* 127 */     if (heightValues != null && heightValues.getLength() > 0) {
/* 128 */       height = Double.parseDouble(heightValues.item(0).getTextContent());
/*     */     }
/* 130 */     NodeList timeValues = e.getElementsByTagName("time");
/* 131 */     if (timeValues != null && timeValues.getLength() > 0) {
/*     */       
/* 133 */       String timeStamp = timeValues.item(0).getTextContent();
/* 134 */       date = convertStringToDate(timeStamp, "yyyy-MM-dd'T'HH:mm:ss");
/*     */     } 
/*     */     
/* 137 */     return new GPSTrackPoint(lat, lon, height, date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<GPSTrackPoint> getTrackWithDeviation(Date fromDate, Date toDate, double deltaCoord, double newHeight, double deltaHeight) {
/* 144 */     return createTrackWithDeviation(getPoints(), fromDate, toDate, deltaCoord, newHeight, deltaHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<GPSTrackPoint> createTrackWithDeviation(List<GPSTrackPoint> srcPoints, Date fromDate, Date toDate, double deltaCoord, double newHeight, double deltaHeight) {
/* 153 */     if (srcPoints.size() < 2) {
/* 154 */       return null;
/*     */     }
/* 156 */     long secondsBetweenDates = (toDate.getTime() - fromDate.getTime()) / 1000L;
/* 157 */     long secondsBetweenPoints = secondsBetweenDates / (srcPoints.size() - 1);
/* 158 */     int deltaSec = (int)(secondsBetweenPoints / 3L);
/*     */     
/* 160 */     List<GPSTrackPoint> resPoints = new ArrayList<>();
/*     */     
/* 162 */     for (int i = 0; i < srcPoints.size(); i++) {
/*     */ 
/*     */       
/* 165 */       GPSTrackPoint p = srcPoints.get(i);
/*     */       
/* 167 */       double changedLat = getRandomShiftedValue(p.getLatitude(), deltaCoord);
/* 168 */       double changedLon = getRandomShiftedValue(p.getLongitude(), deltaCoord);
/* 169 */       double changedHeight = getRandomShiftedValue(newHeight, deltaHeight);
/*     */       
/* 171 */       Date changedDate;
/* 172 */       Calendar calendar = Calendar.getInstance();
/* 173 */       if (i == 0) {
/* 174 */         changedDate = fromDate;
/* 175 */       } else if (i == srcPoints.size() - 1) {
/* 176 */         changedDate = toDate;
/*     */       } else {
/*     */         
/* 179 */         changedDate = generateShiftedDate(fromDate, (int)(i * secondsBetweenPoints), deltaSec);
/*     */       } 
/* 181 */       calendar.setTime(changedDate);
/* 182 */       calendar.add(Calendar.HOUR, -3);
/* 183 */       changedDate = calendar.getTime();
/*     */       
/* 185 */       GPSTrackPoint newPoint = new GPSTrackPoint(changedLat, changedLon, 
/* 186 */           changedHeight, changedDate);
/*     */       
/* 188 */       resPoints.add(newPoint);
/*     */     } 
/* 190 */     return resPoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static double getRandomShiftedValue(double value, double deltaShift) {
/* 197 */     double sign = ((rand.nextDouble() < 0.5D) ? 1 : -1);
/* 198 */     double shift = rand.nextDouble() * deltaShift;
/* 199 */     return value + shift * sign;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Date generateShiftedDate(Date initDate, int offsetSec, int deltaSec) {
/* 206 */     Calendar calendar = Calendar.getInstance();
/*     */     
/* 208 */     calendar.setTime(initDate);
/*     */     
/* 210 */     int sign = (rand.nextDouble() < 0.5D) ? 1 : -1;
/* 211 */     int shift = (deltaSec == 0) ? 0 : (rand.nextInt(deltaSec) * sign);
/*     */     
/* 213 */     calendar.add(Calendar.SECOND, offsetSec + shift);
/* 214 */     return calendar.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeGpxFile(String outFileName, List<GPSTrackPoint> points) {
/*     */     try {
/* 224 */       Document doc = buildDocument(points);
/*     */       
/* 226 */       Transformer transformer = TransformerFactory.newInstance().newTransformer();
/*     */       
/* 228 */       transformer.setOutputProperty("indent", "yes");
/* 229 */       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
/*     */       
/* 231 */       DOMSource source = new DOMSource(doc);
/* 232 */       StreamResult result = new StreamResult(new File(outFileName));
/* 233 */       transformer.transform(source, result);
/*     */     }
/* 235 */     catch (ParserConfigurationException|javax.xml.transform.TransformerException e) {
/*     */       
/* 237 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Document buildDocument(List<GPSTrackPoint> points) throws ParserConfigurationException {
/* 246 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 247 */     DocumentBuilder builder = factory.newDocumentBuilder();
/*     */     
/* 249 */     Document doc = builder.newDocument();
/* 250 */     Element gpxElement = doc.createElement("gpx");
/*     */     
/* 252 */     setRootAttributes(gpxElement);
/* 253 */     doc.appendChild(gpxElement);
/*     */ 
/*     */     
/* 256 */     Date metaDate = points.get(points.size() - 1).getDate();
/* 257 */     Element metaElement = createGarminMetaData(doc, metaDate);
/* 258 */     gpxElement.appendChild(metaElement);
/*     */     
/* 260 */     Element trkElement = doc.createElement("trk");
/* 261 */     gpxElement.appendChild(trkElement);
/*     */     
/* 263 */     Element nameElement = doc.createElement("name");
/*     */     
/* 265 */     nameElement.appendChild(doc.createTextNode(this.trackName));
/* 266 */     trkElement.appendChild(nameElement);
/*     */     
/* 268 */     Element descElement = doc.createElement("desc");
/*     */     
/* 270 */     descElement.appendChild(doc.createTextNode(this.desc));
/* 271 */     trkElement.appendChild(descElement);
/*     */     
/* 273 */     Element trksegElement = doc.createElement("trkseg");
/* 274 */     trkElement.appendChild(trksegElement);
/*     */ 
/*     */     
/* 277 */     appendPointsToSegment(doc, trksegElement, points);
/*     */     
/* 279 */     return doc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendPointsToSegment(Document doc, Element segElement, List<GPSTrackPoint> points) {
/* 286 */     for (GPSTrackPoint point : points) {
/*     */ 
/*     */       
/* 289 */       Element trkptElement = doc.createElement("trkpt");
/*     */       
/* 291 */       trkptElement.setAttribute("lat", String.valueOf(point.getLatitude()));
/* 292 */       trkptElement.setAttribute("lon", String.valueOf(point.getLongitude()));
/* 293 */       segElement.appendChild(trkptElement);
/*     */       
/* 295 */       Element heightElement = doc.createElement("ele");
/*     */       
/* 297 */       heightElement.appendChild(doc.createTextNode(String.valueOf(point.getHeight())));
/* 298 */       trkptElement.appendChild(heightElement);
/*     */       
/* 300 */       Element timeElement = doc.createElement("time");
/*     */       
/* 302 */       String dateString = convertDateToString(point.getDate(), "yyyy-MM-dd'T'HH:mm:ss");
/*     */       
/* 304 */       timeElement.appendChild(doc.createTextNode(dateString));
/* 305 */       trkptElement.appendChild(timeElement);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setRootAttributes(Element root) {
/* 313 */     root.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
/* 314 */     root.setAttribute("xmlns:gpxx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
/* 315 */     root.setAttribute("xmlns:wptx1", "http://www.garmin.com/xmlschemas/WaypointExtension/v1");
/* 316 */     root.setAttribute("xmlns:gpxtpx", "http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
/* 317 */     root.setAttribute("creator", "Astro 320");
/* 318 */     root.setAttribute("version", "1.1");
/* 319 */     root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
/* 320 */     root.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www8.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/WaypointExtension/v1 http://www8.garmin.com/xmlschemas/WaypointExtensionv1.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Element createGarminMetaData(Document doc, Date date) {
/* 335 */     Element metaElement = doc.createElement("metadata");
/*     */     
/* 337 */     Element linkElement = doc.createElement("link");
/* 338 */     linkElement.setAttribute("href", "http://www.garmin.com");
/* 339 */     metaElement.appendChild(linkElement);
/*     */     
/* 341 */     Element textElement = doc.createElement("text");
/* 342 */     textElement.appendChild(doc.createTextNode("Garmin International"));
/* 343 */     linkElement.appendChild(textElement);
/*     */     
/* 345 */     Element timeElement = doc.createElement("time");
/* 346 */     String dateString = convertDateToString(date, "yyyy-MM-dd'T'HH:mm:ss");
/* 347 */     timeElement.appendChild(doc.createTextNode(dateString));
/* 348 */     metaElement.appendChild(timeElement);
/*     */     
/* 350 */     return metaElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<GPSTrackPoint> getExtendedTrack(List<GPSTrackPoint> points, int pointsPerSection) {
/* 358 */     List<GPSTrackPoint> extPoints = new ArrayList<>();
/* 359 */     for (int i = 0; i < points.size() - 1; i++) {
/*     */ 
/*     */       
/* 362 */       extPoints.add(points.get(i));
/*     */       
/* 364 */       List<GPSTrackPoint> subList = 
/* 365 */         makeIntermediatePoints(points, i, i + 1, pointsPerSection);
/* 366 */       extPoints.addAll(subList);
/*     */     } 
/*     */     
/* 369 */     extPoints.add(points.get(points.size() - 1));
/*     */     
/* 371 */     return extPoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<GPSTrackPoint> makeIntermediatePoints(List<GPSTrackPoint> list, int beginPos, int endPos, int num) {
/* 380 */     List<GPSTrackPoint> result = new ArrayList<>();
/*     */     
/* 382 */     GPSTrackPoint beginPoint = list.get(beginPos);
/* 383 */     GPSTrackPoint endPoint = list.get(endPos);
/*     */     
/* 385 */     double deltaLat = (endPoint.getLatitude() - beginPoint.getLatitude()) / (num + 1);
/* 386 */     double deltaLon = (endPoint.getLongitude() - beginPoint.getLongitude()) / (num + 1);
/* 387 */     double deltaHeight = (endPoint.getHeight() - beginPoint.getHeight()) / (num + 1);
/*     */     
/* 389 */     long secondsBetweenDates = (
/* 390 */       endPoint.getDate().getTime() - beginPoint.getDate().getTime()) / 1000L;
/* 391 */     long secondsBetweenPoints = secondsBetweenDates / (num + 1);
/* 392 */     int deltaSec = (int)(secondsBetweenPoints / 3L);
/*     */     
/* 394 */     for (int i = 1; i <= num; i++) {
/*     */ 
/*     */       
/* 397 */       double newLat = beginPoint.getLatitude() + i * deltaLat;
/* 398 */       double newLon = beginPoint.getLongitude() + i * deltaLon;
/* 399 */       double newHeight = beginPoint.getHeight() + i * deltaHeight;
/*     */       
/* 401 */       Date newDate = generateShiftedDate(beginPoint.getDate(), 
/* 402 */           (int)(i * secondsBetweenPoints), deltaSec);
/*     */       
/* 404 */       GPSTrackPoint p = new GPSTrackPoint(newLat, newLon, newHeight, newDate);
/*     */       
/* 406 */       result.add(p);
/*     */     } 
/* 408 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void interpolateHeigts(List<GPSTrackPoint> points1, List<GPSTrackPoint> points2) {
/* 467 */     int idx0 = 0;
/* 468 */     int idx1 = (points1.size() - 1) / 5;
/* 469 */     int idx2 = (points1.size() - 1) / 2;
/* 470 */     int idx3 = points1.size() - 1;
/*     */     
/* 472 */     GPSTrackPoint p0 = points1.get(idx0).clone();
/* 473 */     GPSTrackPoint p1 = points1.get(idx1).clone();
/* 474 */     GPSTrackPoint p2 = points1.get(idx2).clone();
/* 475 */     GPSTrackPoint p3 = points1.get(idx3).clone();
/*     */ 
/*     */     
/* 478 */     for (int i = 0; i < points1.size(); i++) {
/*     */       
/* 480 */       GPSTrackPoint p = points1.get(i);
/*     */       
/* 482 */       double t = (float)(p.getTime() - p0.getTime()) / (
/* 483 */         p3.getTime() - p0.getTime());
/*     */       
/* 485 */       double h = Math.pow(1.0D - t, 3.0D) * p0.getHeight() + 
/* 486 */         3.0D * Math.pow(1.0D - t, 2.0D) * t * p1.getHeight() + 
/* 487 */         3.0D * (1.0D - t) * Math.pow(t, 2.0D) * p2.getHeight() + 
/* 488 */         Math.pow(t, 3.0D) * p3.getHeight();
/*     */ 
/*     */       
/* 491 */       p.setHeight(h);
/*     */       
/* 493 */       points2.get(i).setHeight(h);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date convertStringToDate(String timeString, String pattern) throws ParseException {
/* 502 */     DateFormat iso8601 = new SimpleDateFormat(pattern);
/* 503 */     return iso8601.parse(timeString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String convertDateToString(Date date, String pattern) {
/* 510 */     SimpleDateFormat format = new SimpleDateFormat(pattern);
/* 511 */     return format.format(date);
/*     */   }
/*     */ }


/* Location:              C:\dev\TZ\GPSTrackBuilder.jar!\gps\track\GPSTrack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */