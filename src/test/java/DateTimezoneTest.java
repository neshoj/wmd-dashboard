import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class DateTimezoneTest {

    @Test
    public void convertToActualEATTime() throws ParseException {
        String source = "2020-07-25T07:59:31.000Z";


        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        parser.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("EAT"));

        Date date = parser.parse(source);
        System.out.println("=====================================date = " + date);
        String formattedDate = formatter.format(date);
        System.out.println("=====================================formattedDate = " + formattedDate);

        assertEquals(0,0, "0 x 0 must be 0");

    }
}
