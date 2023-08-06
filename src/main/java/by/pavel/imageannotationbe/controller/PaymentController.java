package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.model.License;
import by.pavel.imageannotationbe.model.LicenseType;
import by.pavel.imageannotationbe.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/purchase/intent/{licenseTypeId}")
    public PaymentService.CreateIntentResponse purchaseLicense(@PathVariable Long licenseTypeId) {
        return paymentService.createPayment(licenseTypeId);
    }

    @GetMapping("/licenseTypes")
    public List<LicenseType> getLicenseTypes() {
        return paymentService.getLicenseTypes();
    }

    @PostMapping("/purchase/{licenseTypeId}")
    public License createLicense(@PathVariable Long licenseTypeId) {
        return paymentService.createLicense(licenseTypeId);
    }

}
