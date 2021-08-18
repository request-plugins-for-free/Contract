package me.tofpu.contract.contract.factory;

import me.tofpu.contract.Util;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.impl.ContractImpl;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.data.DataManager;
import me.tofpu.contract.user.service.UserService;

import java.util.Optional;
import java.util.UUID;

public class ContractFactory {
    private static DataManager dataManager;
    private static ContractService contractService;
    private static UserService userService;

    public static void initialize(final ContractService contractService, final UserService userService, final DataManager dataManager){
        ContractFactory.dataManager = dataManager;
        ContractFactory.contractService = contractService;
        ContractFactory.userService = userService;
    }

    public static Contract create(final UUID employerId, final String contractorName, final long length, final double amount, final String description){
        final Optional<UUID> contractorId = Util.getUniqueId(contractorName);
        if (!contractorId.isPresent()) return null;
        final Contract contract = new ContractImpl(userService.getUser(employerId).orElse(dataManager.loadUser(employerId).get()), userService.getUserOrDefault(contractorId.get()), length, amount, description);

        contractService.registerContract(contract);
        return contract;
    }
}
