package gps.track;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GPSTrack {
    private final String trackName;
    private final String desc;
    private final List<GPSTrackPoint> points;
    private static Random rand;

    public List<GPSTrackPoint> getPoints() {
        return this.points;
    }

    public GPSTrack(String sourceFileName, String trackName, String desc) {
        this.trackName = trackName;
        this.desc = desc;

        this.points = new ArrayList<>();

        rand = new Random();

        parseGpx(sourceFileName, this.points);
    }

    private void parseGpx(String sourceFileName, List<GPSTrackPoint> points) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new File(sourceFileName));

            doc.getDocumentElement().normalize();

            Element gpxElement = doc.getDocumentElement();

            NodeList trkptNodes = gpxElement.getElementsByTagName("trkpt");

            for (int i = 0; i < trkptNodes.getLength(); i++) {

                try {

                    Node trkptNode = trkptNodes.item(i);
                    GPSTrackPoint point = makeTrackPointFromNode(trkptNode);

                    if (point != null) {
                        points.add(point);
                    }
                } catch (ParseException e) {

                    e.printStackTrace();
                }

            }
        } catch (ParserConfigurationException | org.xml.sax.SAXException | java.io.IOException e) {

            e.printStackTrace();
        }
    }


    private GPSTrackPoint makeTrackPointFromNode(Node node) throws ParseException {
        if (node.getNodeType() != 1) {
            return null;
        }
        Element e = (Element) node;

        double lat = Double.parseDouble(e.getAttribute("lat"));

        double lon = Double.parseDouble(e.getAttribute("lon"));

        double height = 0.0D;

        Date date = null;

        NodeList heightValues = e.getElementsByTagName("ele");
        if (heightValues != null && heightValues.getLength() > 0) {
            height = Double.parseDouble(heightValues.item(0).getTextContent());
        }
        NodeList timeValues = e.getElementsByTagName("time");
        if (timeValues != null && timeValues.getLength() > 0) {

            String timeStamp = timeValues.item(0).getTextContent();
            date = convertStringToDate(timeStamp, "yyyy-MM-dd'T'HH:mm:ss");
        }

        return new GPSTrackPoint(lat, lon, height, date);
    }


    public List<GPSTrackPoint> getTrackWithDeviation(Date fromDate, Date toDate, double deltaCoord, double newHeight, double deltaHeight) {
        return createTrackWithDeviation(getPoints(), fromDate, toDate, deltaCoord, newHeight, deltaHeight);
    }


    public static List<GPSTrackPoint> createTrackWithDeviation(List<GPSTrackPoint> srcPoints, Date fromDate, Date toDate, double deltaCoord, double newHeight, double deltaHeight) {
        if (srcPoints.size() < 2) {
            return null;
        }
        long secondsBetweenDates = (toDate.getTime() - fromDate.getTime()) / 1000L;
        long secondsBetweenPoints = secondsBetweenDates / (srcPoints.size() - 1);
        int deltaSec = (int) (secondsBetweenPoints / 3L);

        List<GPSTrackPoint> resPoints = new ArrayList<>();

        for (int i = 0; i < srcPoints.size(); i++) {


            GPSTrackPoint p = srcPoints.get(i);

            double changedLat = getRandomShiftedValue(p.getLatitude(), deltaCoord);
            double changedLon = getRandomShiftedValue(p.getLongitude(), deltaCoord);
            double changedHeight = getRandomShiftedValue(newHeight, deltaHeight);

            Date changedDate;
            Calendar calendar = Calendar.getInstance();
            if (i == 0) {
                changedDate = fromDate;
            } else if (i == srcPoints.size() - 1) {
                changedDate = toDate;
            } else {

                changedDate = generateShiftedDate(fromDate, (int) (i * secondsBetweenPoints), deltaSec);
            }
            calendar.setTime(changedDate);
            calendar.add(Calendar.HOUR, -3);
            changedDate = calendar.getTime();

            GPSTrackPoint newPoint = new GPSTrackPoint(changedLat, changedLon,
                    changedHeight, changedDate);

            resPoints.add(newPoint);
        }
        return resPoints;
    }


    private static double getRandomShiftedValue(double value, double deltaShift) {
        double sign = ((rand.nextDouble() < 0.5D) ? 1 : -1);
        double shift = rand.nextDouble() * deltaShift;
        return value + shift * sign;
    }


    private static Date generateShiftedDate(Date initDate, int offsetSec, int deltaSec) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(initDate);

        int sign = (rand.nextDouble() < 0.5D) ? 1 : -1;
        int shift = (deltaSec == 0) ? 0 : (rand.nextInt(deltaSec) * sign);

        calendar.add(Calendar.SECOND, offsetSec + shift);
        return calendar.getTime();
    }


    public void writeGpxFile(String outFileName, List<GPSTrackPoint> points) {
        try {
            Document doc = buildDocument(points);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(outFileName));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | javax.xml.transform.TransformerException e) {

            e.printStackTrace();
        }
    }


    private Document buildDocument(List<GPSTrackPoint> points) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.newDocument();
        Element gpxElement = doc.createElement("gpx");

        setRootAttributes(gpxElement);
        doc.appendChild(gpxElement);


        Date metaDate = points.get(points.size() - 1).getDate();
        Element metaElement = createGarminMetaData(doc, metaDate);
        gpxElement.appendChild(metaElement);

        Element trkElement = doc.createElement("trk");
        gpxElement.appendChild(trkElement);

        Element nameElement = doc.createElement("name");

        nameElement.appendChild(doc.createTextNode(this.trackName));
        trkElement.appendChild(nameElement);

        Element descElement = doc.createElement("desc");

        descElement.appendChild(doc.createTextNode(this.desc));
        trkElement.appendChild(descElement);

        Element trksegElement = doc.createElement("trkseg");
        trkElement.appendChild(trksegElement);


        appendPointsToSegment(doc, trksegElement, points);

        return doc;
    }


    private void appendPointsToSegment(Document doc, Element segElement, List<GPSTrackPoint> points) {
        for (GPSTrackPoint point : points) {


            Element trkptElement = doc.createElement("trkpt");

            trkptElement.setAttribute("lat", String.valueOf(point.getLatitude()));
            trkptElement.setAttribute("lon", String.valueOf(point.getLongitude()));
            segElement.appendChild(trkptElement);

            Element heightElement = doc.createElement("ele");

            heightElement.appendChild(doc.createTextNode(String.valueOf(point.getHeight())));
            trkptElement.appendChild(heightElement);

            Element timeElement = doc.createElement("time");

            String dateString = convertDateToString(point.getDate(), "yyyy-MM-dd'T'HH:mm:ss");

            timeElement.appendChild(doc.createTextNode(dateString));
            trkptElement.appendChild(timeElement);
        }
    }


    private void setRootAttributes(Element root) {
        root.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
        root.setAttribute("xmlns:gpxx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
        root.setAttribute("xmlns:wptx1", "http://www.garmin.com/xmlschemas/WaypointExtension/v1");
        root.setAttribute("xmlns:gpxtpx", "http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
        root.setAttribute("creator", "Astro 320");
        root.setAttribute("version", "1.1");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www8.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/WaypointExtension/v1 http://www8.garmin.com/xmlschemas/WaypointExtensionv1.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");
    }


    public Element createGarminMetaData(Document doc, Date date) {
        Element metaElement = doc.createElement("metadata");

        Element linkElement = doc.createElement("link");
        linkElement.setAttribute("href", "http://www.garmin.com");
        metaElement.appendChild(linkElement);

        Element textElement = doc.createElement("text");
        textElement.appendChild(doc.createTextNode("Garmin International"));
        linkElement.appendChild(textElement);

        Element timeElement = doc.createElement("time");
        String dateString = convertDateToString(date, "yyyy-MM-dd'T'HH:mm:ss");
        timeElement.appendChild(doc.createTextNode(dateString));
        metaElement.appendChild(timeElement);

        return metaElement;
    }


    public static List<GPSTrackPoint> getExtendedTrack(List<GPSTrackPoint> points, int pointsPerSection) {
        List<GPSTrackPoint> extPoints = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {


            extPoints.add(points.get(i));

            List<GPSTrackPoint> subList =
                    makeIntermediatePoints(points, i, i + 1, pointsPerSection);
            extPoints.addAll(subList);
        }

        extPoints.add(points.get(points.size() - 1));

        return extPoints;
    }

    private static List<GPSTrackPoint> makeIntermediatePoints(List<GPSTrackPoint> list, int beginPos, int endPos, int num) {
        List<GPSTrackPoint> result = new ArrayList<>();

        GPSTrackPoint beginPoint = list.get(beginPos);
        GPSTrackPoint endPoint = list.get(endPos);

        double deltaLat = (endPoint.getLatitude() - beginPoint.getLatitude()) / (num + 1);
        double deltaLon = (endPoint.getLongitude() - beginPoint.getLongitude()) / (num + 1);
        double deltaHeight = (endPoint.getHeight() - beginPoint.getHeight()) / (num + 1);

        long secondsBetweenDates = (
                endPoint.getDate().getTime() - beginPoint.getDate().getTime()) / 1000L;
        long secondsBetweenPoints = secondsBetweenDates / (num + 1);
        int deltaSec = (int) (secondsBetweenPoints / 3L);

        for (int i = 1; i <= num; i++) {


            double newLat = beginPoint.getLatitude() + i * deltaLat;
            double newLon = beginPoint.getLongitude() + i * deltaLon;
            double newHeight = beginPoint.getHeight() + i * deltaHeight;

            Date newDate = generateShiftedDate(beginPoint.getDate(),
                    (int) (i * secondsBetweenPoints), deltaSec);

            GPSTrackPoint p = new GPSTrackPoint(newLat, newLon, newHeight, newDate);

            result.add(p);
        }
        return result;
    }

    public static void interpolateHeigts(List<GPSTrackPoint> points1, List<GPSTrackPoint> points2) {
        int idx0 = 0;
        int idx1 = (points1.size() - 1) / 5;
        int idx2 = (points1.size() - 1) / 2;
        int idx3 = points1.size() - 1;

        GPSTrackPoint p0 = points1.get(idx0).clone();
        GPSTrackPoint p1 = points1.get(idx1).clone();
        GPSTrackPoint p2 = points1.get(idx2).clone();
        GPSTrackPoint p3 = points1.get(idx3).clone();


        for (int i = 0; i < points1.size(); i++) {

            GPSTrackPoint p = points1.get(i);

            double t = (float) (p.getTime() - p0.getTime()) / (
                    p3.getTime() - p0.getTime());

            double h = Math.pow(1.0D - t, 3.0D) * p0.getHeight() +
                    3.0D * Math.pow(1.0D - t, 2.0D) * t * p1.getHeight() +
                    3.0D * (1.0D - t) * Math.pow(t, 2.0D) * p2.getHeight() +
                    Math.pow(t, 3.0D) * p3.getHeight();

            p.setHeight(h);

            points2.get(i).setHeight(h);
        }
    }

    public static Date convertStringToDate(String timeString, String pattern) throws ParseException {
        DateFormat iso8601 = new SimpleDateFormat(pattern);
        return iso8601.parse(timeString);
    }

    public static String convertDateToString(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}