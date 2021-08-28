package me.tofpu.contract.contract.review.factory;

import me.tofpu.contract.contract.review.ContractReview;
import me.tofpu.contract.contract.review.impl.ContractReviewImpl;

public class ContractReviewFactory {

    public static ContractReview create() {
        return new ContractReviewImpl(0, null);
    }

    public static ContractReview create(final double rated, final String description) {
        return new ContractReviewImpl(rated, description);
    }
}
