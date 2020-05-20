package ru.otus;

import java.util.ArrayList;
import java.util.List;

/**
 * Currency is the group of CurrencyBundle's
 */
public class Currency {
    private final List<CurrencyBundle> bundles = new ArrayList<>();

    public static Currency.Builder newBuilder() {
        return new Currency().new Builder();
    }

    public class Builder {
        private Builder(){}

        public Currency.Builder addBundle(CurrencyBundle bundle) {
            Currency.this.bundles.add(bundle);
            return this;
        }

        public Currency build() {
            return Currency.this;
        }
    }

    public List<CurrencyBundle> getBundles() {
        return bundles;
    }

    public String toString() {
        return String.format("Currency(%s)", bundles);
    }
}
