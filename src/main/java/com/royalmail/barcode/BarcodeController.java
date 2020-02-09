package com.royalmail.barcode;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@Validated
public class BarcodeController {

    private static final List<Integer> WEIGHTS = Arrays.asList(8, 6, 4, 2, 3, 5, 9, 7);

    @GetMapping(value = "/validate/{barcode}", produces = "application/json")
    public Boolean validate(@PathVariable @Pattern(regexp = "^[A-Z]{2}[0-9]{9}GB$") final String barcode) {

        final List<Integer> digits = java.util.regex.Pattern.compile("\\B")
                .splitAsStream(barcode.substring(2, 10))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        final int sum = IntStream.range(0, 8)
                .map(i -> digits.get(i) * WEIGHTS.get(i))
                .sum();

        int computedCheckDigit = 11 - (sum % 11);
        if (computedCheckDigit == 10) {
            computedCheckDigit = 0;
        } else if (computedCheckDigit == 11) {
            computedCheckDigit = 1;
        }

        final int barcodeCheckDigit = Integer.parseInt(barcode.substring(10, 11));
        return barcodeCheckDigit == computedCheckDigit;
    }
}
