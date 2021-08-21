package me.tofpu.contract.contract.factory;

import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.impl.ContractImpl;
import me.tofpu.contract.contract.review.ContractReview;
import me.tofpu.contract.contract.review.factory.ContractReviewFactory;

import java.util.UUID;

public class ContractFactory {
    public static Contract create(final UUID id, final boolean frozen, final String employerName, final UUID employerId, final String contractorName, final UUID contractorId, final ContractReview review, final String description, final long startedAt, final long length, final double amount) {
        return new ContractImpl(id, frozen, employerName, employerId, contractorName, contractorId, review, description, startedAt, length, amount);
    }

    public static Contract create(final UUID id, final String employerName, final UUID employerId, final String contractorName, final UUID contractorId, final ContractReview review, final String description, final long startedAt, final long length, final double amount) {
        return new ContractImpl(id, false, employerName, employerId, contractorName, contractorId, review, description, startedAt, length, amount);
    }

    public static Contract create(final String employerName, final UUID employerId, final String contractorName, final UUID contractorId, final String description, final long length, final double amount) {
        return create(UUID.randomUUID(), employerName, employerId, contractorName, contractorId, ContractReviewFactory.create(), description, System.nanoTime(), length, amount);
    }

    //    public static Contract create(final UUID employerId, final String contractorName, final long length, final double amount, final String description){
    //        final Optional<UUID> contractorId = Util.getUniqueId(contractorName);
    //        if (!contractorId.isPresent()) return null;
    //        final Contract contract = new ContractImpl(userService.getUser(employerId).orElse(dataManager.loadUser(employerId).get()), userService.getUserOrDefault(contractorId.get()), description, length, amount);
    //
    //        contractService.registerContract(contract);
    //        return contract;
    //    }
}
