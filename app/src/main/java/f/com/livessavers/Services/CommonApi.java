package f.com.livessavers.Services;

/**
 * Created by ASTIN Android on 3/15/2018.
 */

public class CommonApi {

    //openuser
    public static String URL = "http://lifessaver.com/WebAPI/WebServiceAPI.asmx";

    public static String NAMESPACE = "http://lifessaver.com/WebAPI";


    public static String DONNORLOGIN="UserLogin";

    public static String USERREGISTRATION="InsertUserProfile";

    public static String USERCHANGEPASSWORD="UserChangePassword";

    public static String GETUSERDETAILS="GetUserProfileBYUserId";

    public static String UPDATEUSERPROFILE="UpdateUserProfile";

    public static String GETDONORSLIST="DonorSearchResult";

    public static String POSTBLOODREQUEST="PostBloodRequest";

    public static String UserForgotPassword="UserForgotPassword";

    public static String InsertPayment="InsertPayment";

    public static String FillPayment="FillPaymentDetails";

    public static String UpdatePayment="UpdatePayment";

    //sms getway

    public static  String smsgateway ="http://182.18.168.112:8082/Rest/AIwebservice/Bulk";

    public static String username="lifessaver";
    public static String password="Life@123";
    public static String sid="SAVERS";
    public static String mtype="n";




}
