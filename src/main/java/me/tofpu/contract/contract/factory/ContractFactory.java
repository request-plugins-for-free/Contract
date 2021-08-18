package me.tofpu.contract.contract.factory;

import me.tofpu.contract.Util;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.impl.ContractImpl;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.service.UserService;

import java.util.Optional;
import java.util.UUID;

public class ContractFactory {
    private static ContractService contractService;
    private static UserService userService;

    public static void initialize(final ContractService contractService, final UserService userService){
        ContractFactory.contractService = contractService;
        ContractFactory.userService = userService;
    }

    public static boolean create(final User employerId, final String contractorName, final long length, final double amount){
        final Optional<UUID> contractorId = Util.getUniqueId(contractorName);
        if (!contractorId.isPresent()) return false;
        final Contract contract = new ContractImpl(employerId, userService.getUserOrDefault(contractorId.get()), length, amount);

        contractService.registerContract(contract);
        return true;
    }
}
