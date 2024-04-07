package softwood.pm.myapplication;

import android.icu.text.RelativeDateTimeFormatter;

public class Data {
    private double Latitude, Longitude;
    private String Type;

    public Data(double Latitude, double Longitude, String Type) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Type = Type;
    }

        public double getLatitude() {
            return Latitude;
        }

        public double getLongitude() {
            return Longitude;
        }

        public String getType() {
            return Type;
        }

}
