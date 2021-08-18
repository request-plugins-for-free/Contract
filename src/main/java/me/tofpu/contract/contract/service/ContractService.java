package me.tofpu.contract.contract.service;

import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.User;

import java.util.List;
import java.util.Optional;

public interface ContractService {

    /**
     * @param contract adds the contract to the list
     */
    void registerContract(final Contract contract);

//    /**
//     * @param uniqueId the contractor uniqueId (whom accepts contract)
//     *
//     * @return a list of available contracts that the contractor accepted
//     */
//    List<Contract> getContractorContracts(final UUID uniqueId);

    /**
     * @param employerId the employer uniqueId (whom creates a contract)
     *
     * @return a list of available contracts that the employer contracted
     */
    List<Contract> getEmployerContracts(final User employerId);

    /**
     * @param contractorId the contractor uniqueId (whom accepts contracts)
     *
     * @return an available contract
     */
    Optional<Contract> getContractorContract(final User contractorId);
}
