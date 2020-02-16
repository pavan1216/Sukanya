package f.com.livessavers.Pojo;

/**
 * Created by ASTIN Android on 6/15/2018.
 */

public class StateSpinner {




    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String Id;
    public String StateName;

    public String getCountryId() {
        return CountryId;
    }

    public void setCountryId(String countryId) {
        CountryId = countryId;
    }

    public String CountryId;



    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }




    @Override
    public String toString() {
        return StateName;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof StateSpinner){
            StateSpinner c = (StateSpinner )obj;
            if(c.getStateName().equals(StateName) && c.getId()==Id && c.getCountryId().equals(CountryId)) return true;
        }

        return false;
    }

}
