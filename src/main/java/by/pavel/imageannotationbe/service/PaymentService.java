package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.exception.NotFoundException;
import by.pavel.imageannotationbe.model.License;
import by.pavel.imageannotationbe.model.LicenseType;
import by.pavel.imageannotationbe.model.User;
import by.pavel.imageannotationbe.repository.LicenseRepository;
import by.pavel.imageannotationbe.repository.LicenseTypeRepository;
import by.pavel.imageannotationbe.security.UserDetailsImpl;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PaymentService implements UserAware {

    private final LicenseTypeRepository licenseTypeRepository;
    private final LicenseRepository licenseRepository;

    static {
        Stripe.apiKey = "sk_test_51MxXxKDn9IcXEBBEBUuTSyPgJ3lCWzARZsHgg0TxRw9L3j63MGtvcSywGNa3CWZNqzPcLsIjS63IjXQitc3aqjgd00FAoRcPKO";
    }

    public record CreateIntentResponse(String clientSecret) {};

    @SneakyThrows
    public CreateIntentResponse createPayment(Long licenseTypeId) {
        LicenseType licenseType = licenseTypeRepository.findById(licenseTypeId).orElseThrow(() -> new NotFoundException("License type not found"));
        long price = licenseType.getPrice().movePointRight(2).longValueExact();

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(price)
                        .setCurrency("pln")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        // Create a PaymentIntent with the order amount and currency
        return new CreateIntentResponse(PaymentIntent.create(params).getClientSecret());
    }

    public License createLicense(Long licenseTypeId) {
        UserDetailsImpl currentUser = getCurrentUser();
        LicenseType licenseType = licenseTypeRepository.findById(licenseTypeId).orElseThrow(() -> new NotFoundException("License type not found"));

        License newLicense = License.builder()
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(licenseType.getDuration()))
                .owner(User.builder().id(currentUser.getId()).build())
                .licenseType(licenseType)
                .build();

        return licenseRepository.save(newLicense);
    }

    public List<LicenseType> getLicenseTypes() {
        List<LicenseType> licenseTypes = IterableUtils.toList(licenseTypeRepository.findAll());
        return licenseTypes.stream().filter(licenseType -> licenseType.getPrice().intValue() != 0).toList();
    }
}
