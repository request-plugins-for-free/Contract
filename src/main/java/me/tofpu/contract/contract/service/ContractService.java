package me.tofpu.contract.contract.service;

import com.google.gson.Gson;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.User;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    List<Contract> getEmployerContracts(final UUID employerId);

    /**
     * @param contractorId the contractor uniqueId (whom accepts contracts)
     *
     * @return an available contract
     */
    Optional<Contract> getContractorContract(final UUID contractorId);

    /**
     * @param contractId the contract id
     *
     * @return an instance of contract id, if it's available
     */
    Optional<Contract> getContractById(final UUID contractId);

    /**
     * Loads all the contracts stored in the directory param
     *
     * @param directory the directory where all the contracts is stored at
     */
    void loadAll(final File directory);

    /**
     * Saves all the contracts to that specific directory
     *
     * @param directory the directory to save all the contracts data
     */
    void saveAll(final File directory);
}
