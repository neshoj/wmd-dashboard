package opdwms.web.dashboard.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CensusStats {
    private BigInteger buses = BigInteger.ZERO;
    private BigInteger trucksAboveThreeTonnes = BigInteger.ZERO;
    private BigInteger trucksAboveSevenTonnes = BigInteger.ZERO;
}