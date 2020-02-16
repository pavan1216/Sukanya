package f.com.livessavers.Pojo;

public class BloodGroupSpinner {

    String Status;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public   int Id;



    public String getBloodGroupName() {
        return BloodGroupName;
    }

    public void setBloodGroupName(String bloodGroupName) {
        BloodGroupName = bloodGroupName;
    }



    public  String BloodGroupName;



    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BloodGroupSpinner){
            BloodGroupSpinner c = (BloodGroupSpinner)obj;
            if(c.getBloodGroupName().equals(BloodGroupName) && c.getId()==Id ) return true;
        }

        return false;
    }

}
