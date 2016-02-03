package teamsanguine.sanguine;

/**
 * Created by deepa on 12/3/2015.
 */
public class donationdetail {
    String date, location;
    int cholestrol, temperature, bloodpressure;

    public donationdetail(String date, String location, int cholestrol, int temperature, int bloodpressure){
        this.date = date;
        this.location = location;
        this.cholestrol = cholestrol;
        this.temperature = temperature;
        this. bloodpressure = bloodpressure;
    }

    public String getDate(){
        return date;
    }
    public String getLocation(){
        return location;
    }
    public int getCholestrol(){
        return cholestrol;
    }
    public int getTemperature(){
        return temperature;
    }
    public int getBloodpressure(){
        return bloodpressure;
    }
}
