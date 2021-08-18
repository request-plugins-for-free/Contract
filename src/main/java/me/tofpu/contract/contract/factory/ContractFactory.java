package me.tofpu.contract.contract.factory;

import me.tofpu.contract.Util;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.impl.ContractImpl;
import me.tofpu.contract.contract.service.ContractService;

import java.util.Optional;
import java.util.UUID;

public class ContractFactory {
    private static ContractService contractService;
    public static void initialize(final ContractService contractService){
        ContractFactory.contractService = contractService;
    }

    public static boolean create(final UUID employerId, final String contractorName, final long length, final double amount){
        final Optional<UUID> contractorId = Util.getUniqueId(contractorName);
        if (!contractorId.isPresent()) return false;
        final Contract contract = new ContractImpl(employerId, contractorId.get(), length, amount);

        contractService.registerContract(contract);
        return true;
    }
}
