package com.glowvia.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Prices in the database are stored in Nepalese Rupees (NPR).
 * USD seed values were converted at {@link #USD_TO_NPR} per dollar.
 */
public final class CurrencyUtil {

    /** Used when migrating old USD prices: NPR per 1 USD. */
    public static final double USD_TO_NPR = 155.0;

    private CurrencyUtil() {
    }

    /** Format an NPR amount for display (no conversion). */
    public static String formatNpr(double npr) {
        return String.format("%.2f", round2(npr));
    }

    public static String formatNpr(BigDecimal npr) {
        if (npr == null) {
            return "0.00";
        }
        return npr.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /** Convert a legacy USD amount to NPR (e.g. for one-time DB migration). */
    public static double usdToNpr(double usd) {
        return round2(usd * USD_TO_NPR);
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
