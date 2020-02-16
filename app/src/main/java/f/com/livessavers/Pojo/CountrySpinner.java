package f.com.livessavers.Pojo;


public class CountrySpinner {



    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String Id ;



    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String CountryName;


    @Override
    public String toString() {
        return CountryName;
    }



    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CountrySpinner){
            CountrySpinner c = (CountrySpinner )obj;
            if(c.getCountryName().equals(CountryName) && c.getId()==Id ) return true;
        }

        return false;
    }

}
