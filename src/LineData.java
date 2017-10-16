import java.util.*;

/**
 * LineData
 */
public class LineData {

    HashMap<Integer, Double> lineMap;
    int label = -1;
    double distance;
    String lineStr;
    double sumSquare = 0;

    // If normalizing, consider using BigDecimal for the count
    public LineData(String str) {
        this.lineStr = str;
        Scanner sc = new Scanner(str);
        lineMap = new HashMap<>();
        while (sc.hasNext()) {
            Integer id = sc.nextInt();
            Double count = (double) sc.nextInt();
            lineMap.put(id, count);
            this.sumSquare += count * count;
        }
        this.sumSquare = Math.sqrt(this.sumSquare);
        sc.close();
    }

    public LineData() {
        lineMap = new HashMap<>();
    }

    public void setLabel(int k) {
        this.label = k;
    }

    public void clearLabel() {
        this.label = -1;
    }

    public boolean containsKey(Integer key) {
        return this.lineMap.containsKey(key);
    }

    public double get(Integer key) {
        return this.containsKey(key) ? this.lineMap.get(key) : 0.0;
    }

    public double cosDistance(LineData ld) {
        double sum = 0;
        for (Map.Entry<Integer, Double> entry : this.lineMap.entrySet()) {
            Integer key = entry.getKey();
            Double value = entry.getValue();
            if (ld.containsKey(key)) {
                sum += value * ld.get(key);
            }
        }
        return (sum / (this.sumSquare * ld.sumSquare));
    }

    public double euDistance(LineData ld) {
        HashMap<Integer, Double> tempMap = new HashMap<>(ld.lineMap);
        tempMap.keySet().removeAll(this.lineMap.keySet());
        double sum = 0;
        for (Double it : tempMap.values()) {
            sum += it * it;
        } // This takes care of the values in ld that are not in this
        for (Map.Entry<Integer, Double> entry : this.lineMap.entrySet()) {
            Integer key = entry.getKey();
            Double value = entry.getValue();
            if (ld.containsKey(key)) {
                double temp = value - ld.get(key);
                sum += temp * temp;
            } else {
                sum += value * value;
            }
        }
        return Math.sqrt(sum);
    }

    public void addFeature(Integer key, Double value) {
        if (!this.containsKey(key)) {
            this.lineMap.put(key, value);
        } else {
            Double val = this.get(key);
            this.lineMap.put(key, value + val);
        }
    }

    public static LineData getCenter(ArrayList<LineData> lineList) {
        double size = (double) lineList.size();
        LineData lineData = new LineData();

        for (LineData ld : lineList) {
            for (Map.Entry<Integer, Double> entry : ld.lineMap.entrySet()) {
                Integer key = entry.getKey();
                Double value = entry.getValue();
                lineData.addFeature(key, value);
            }
            ld.clearLabel();
        }
        for (Map.Entry<Integer, Double> entry : lineData.lineMap.entrySet()) {
            entry.setValue(entry.getValue() / size);
        }
        return lineData;
    }

    public static ArrayList<LineData> fileToList(String filename)
    {
        Scanner sc = FTools.fileOpener(filename);
        ArrayList<LineData> retList = new ArrayList<>();
        while(sc.hasNext())
        {
            retList.add(new LineData(sc.nextLine()));
        }
        sc.close();
        return retList;
    }

}