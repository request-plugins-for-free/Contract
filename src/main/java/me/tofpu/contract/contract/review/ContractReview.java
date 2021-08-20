package me.tofpu.contract.contract.review;

public interface ContractReview {
    /**
     * @return the rating of the reviewer
     */
    double rate();

    /**
     * @param rate the new contract rate
     */
    void rate(final double rate);

    /**
     * @return the description of the rating whom the reviewer wrote
     */
    String description();


    /**
     * @param description the rating description whom the reviewer wrote
     */
    void description(final String description);
}
