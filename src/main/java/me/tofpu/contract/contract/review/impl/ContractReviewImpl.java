package me.tofpu.contract.contract.review.impl;

import me.tofpu.contract.contract.review.ContractReview;

public class ContractReviewImpl implements ContractReview {
    private double rate;
    private String description;

    public ContractReviewImpl(final double rate, final String description) {
        this.rate = rate;
        this.description = description;
    }

    /**
     * @return the rating of the reviewer
     */
    @Override
    public double rate() {
        return this.rate;
    }

    /**
     * @param rate the contract rate
     */
    @Override
    public void rate(final double rate) {
        this.rate = rate;
    }

    /**
     * @return the description of the rating whom the reviewer wrote
     */
    @Override
    public String description() {
        return this.description;
    }

    /**
     * @param description the rating description whom the reviewer wrote
     */
    @Override
    public void description(final String description) {
        this.description = description;
    }
}
