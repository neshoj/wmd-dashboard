package opdwms.web.weighingtransactions.es.documents;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.Id;
import java.util.Date;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@Document(indexName = "static_station_trans")
public class StaticStationTransaction {


    @Field(type = FieldType.Text,name = "transaction_date")
    private String transactionDate;

    @Field(type = FieldType.Text)
    private String status;

    @Field(type = FieldType.Text, name = "ticket_no")
    private String ticketNo;
//    @MultiField(mainField = @Field(type = Text, fielddata = true), otherFields = { @InnerField(suffix = "verbatim", type = Keyword) })
    @Field(type = FieldType.Text, name = "station_name")
    private String stationName;

    @Field(type = FieldType.Text, name = "vehicle_no")
    private String vehicleNo;

    @Field(type = FieldType.Text, name = "axle_configuration")
    private String axleConfiguration;

    @Field(type = FieldType.Double, name = "vehicle_GVM")
    private Double vehicleGVM;

    @Field(type = FieldType.Text)
    private String operator;

    @Field(type = FieldType.Text, name = "operator_shift")
    private String operatorShift;

    @Field(type = FieldType.Text, name = "permit_no")
    private String permitNo;

    @Id
    private String id;


}
